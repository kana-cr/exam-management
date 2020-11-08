package us.sep.biz.common.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.common.enums.ImageEnum;
import us.sep.biz.common.request.ImageRequest;
import us.sep.common.builder.ImageBO;
import us.sep.common.entity.ImageDO;
import us.sep.common.repository.ImageRepo;
import us.sep.user.repo.UserRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Resource
    UserRepo userRepo;

    @Resource
    ImageRepo imageRepo;

    @Resource
    BizIdFactory bizIdFactory;

    @Value("${aliyun.oss.url}")
    private String url;

    private static final String suffix = ".png";

    @Transactional(rollbackFor = Exception.class)
    public ImageBO createImage(ImageRequest request){

        if (checkImageTag(request.getTag()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"标签类型不匹配");

        //检测用户是否传过同名图片
        if (imageRepo.existsByImageNameAndUserId(request.getImageName(),request.getUserId()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"用户已上传过该名称图片");

        //检测用户是否存在
        if (checkUserId(request.getUserId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户id不存在");

        //用户上传过头像类型图片
        if (imageRepo.existsByUserIdAndTag(request.getUserId(),request.getTag()) && request.getTag().equals(ImageEnum.AVATAR.getTag()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"用户已上传过头像");

        ImageDO image = new ImageDO();
        BeanUtils.copyProperties(request,image);
        image.setImageId(bizIdFactory.getImage());
        imageRepo.save(image);
        ImageBO imageBO = image.toImageBO();
        imageBO.setUrl(url + image.getImageName() + imageBO.getUserId() + suffix );
        //help gc
        image = null;
        return imageBO;
    }

    public List<ImageBO> findAll(int pageNum , int pageSize){
        return imageRepo.findAll(PageRequest.of(pageNum,pageSize)).stream().map(ImageDO::toImageBO).
                peek(imageBO -> imageBO.setUrl(url + imageBO.getImageName() + imageBO.getUserId() + suffix )).collect(Collectors.toList());
    }

    public List<ImageBO> findByUserId(String userId){
        if (checkUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户id不存在");

        return imageRepo.findByUserId(userId).stream().map(ImageDO::toImageBO).
                peek(imageBO -> imageBO.setUrl(url + imageBO.getImageName() + imageBO.getUserId() + suffix)).collect(Collectors.toList());
    }

    public ImageBO findByImageId(String imageId){

        if (!imageRepo.existsByImageId(imageId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"图片id不存在");

        ImageBO imageBO = imageRepo.findByImageId(imageId).toImageBO();
        imageBO.setUrl(url + imageBO.getImageName() + imageBO.getUserId() + suffix);
        return imageBO;
    }

    public ImageBO findByImageNameAndUserId(String imageName , String userId){
        if (checkUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户id不存在");

        ImageBO imageBO = imageRepo.findByImageNameAndUserId(imageName,userId).toImageBO();
        imageBO.setUrl(url + imageBO.getImageName()+ imageBO.getUserId() + suffix );
        return imageBO;
    }

    public List<ImageBO> findByTag(String tag){
        if (checkImageTag(tag))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"标签类型不匹配");

        return imageRepo.findByTag(tag).stream().map(ImageDO::toImageBO).peek(imageBO -> imageBO.setUrl(url + imageBO.getImageName() + imageBO.getUserId() + suffix )).collect(Collectors.toList());
    }

     @Transactional(rollbackFor = Exception.class)
     public  ImageBO modifyImage(ImageRequest request){

        if (!imageRepo.existsByImageId(request.getImageId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"图片id不存在");

        ImageDO image = imageRepo.findByImageId(request.getImageId());

        //检测用户是否存在
        if (!StringUtils.isEmpty(request.getUserId() )) {
            if (checkUserId(request.getUserId()))
                throw new CustomizeException(CommonResultCode.UNFOUNDED, "用户id不存在");
            image.setUserId(request.getUserId());
        }

        if (!StringUtils.isEmpty(request.getTag() )) {
            if (checkImageTag(request.getTag()))
                throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS, "标签类型不匹配");
            image.setTag(request.getTag());
        }

        if (!StringUtils.isEmpty(request.getImageName()))
            image.setImageName(request.getImageName());

       imageRepo.save(image);

       return image.toImageBO();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByImageId(String imageId){
        if (!imageRepo.existsByImageId(imageId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"图片id不存在");

        imageRepo.deleteByImageId(imageId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(String userId){
        if (checkUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户id不存在");

        imageRepo.deleteByUserId(userId);
    }

    //校验用户id
    private boolean checkUserId(String userId){
        return !userRepo.existsByUserId(userId);
    }

    //校验图片类型
    private boolean checkImageTag(String tag){
        for (ImageEnum imageType:ImageEnum.values()) {
            if (tag.equals(imageType.getTag()))
                return false;
        }
        return true;
    }
}
