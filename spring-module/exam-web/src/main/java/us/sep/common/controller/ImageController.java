package us.sep.common.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.common.request.ImageRequest;
import us.sep.biz.common.service.ImageService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.common.builder.ImageBO;
import us.sep.util.aliyun.AliyunOssUtil;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Resource
    ImageService imageService;

    @Resource
    private AliyunOssUtil aliyunOssUtil;

    @GetMapping
    public Result<ImageBO> getSingleImage(String imageId){
        AssertUtil.assertStringNotBlank(imageId,"图片id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.findByImageId(imageId));
    }


    @GetMapping("/all")
    public Result<List<ImageBO>> getAllImage(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.findAll(pageNum,pageSize));
    }
    @GetMapping("/user")
    public Result<List<ImageBO>> getImageByUser(String userId){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.findByUserId(userId));
    }

    @GetMapping("/tag")
    public Result<List<ImageBO>> getImageByTag(String tag){
        AssertUtil.assertStringNotBlank(tag,"图片标签不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.findByTag(tag));
    }

    @GetMapping("/userAndName")
    public Result<ImageBO> getImageByUserIdAndName(String userId , String imageName){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        AssertUtil.assertStringNotBlank(imageName,"图片名称不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.findByImageNameAndUserId(imageName,userId));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ImageBO> createImage(ImageRequest request, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.createImage(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ImageBO> modifyImage(ImageRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getImageId(),"图片id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),imageService.modifyImage(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ImageBO> deleteImage(String imageId, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(imageId,"图片id不能为空");
        ImageBO image = imageService.findByImageId(imageId);
        imageService.deleteByImageId(imageId);
        aliyunOssUtil.deleteFileUrl(image.getUrl());
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),image);
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ImageBO>> deleteImageByUser(String userId, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        List<ImageBO> images =  imageService.findByUserId(userId);
        imageService.deleteByUserId(userId);
        aliyunOssUtil.deleteFileUrlInBatch(images.stream().map(ImageBO::getUrl).collect(Collectors.toList()));
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),images);
    }
}
