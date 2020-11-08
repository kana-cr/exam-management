package us.sep.biz.message.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.message.request.CarouselRequest;
import us.sep.biz.message.service.CarouselService;
import us.sep.message.builder.CarouselBO;
import us.sep.message.entity.CarouselDO;
import us.sep.message.repo.CarouselRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Resource
    BizIdFactory bizIdFactory;

    @Resource
    CarouselRepo carouselRepo;

    @Override
    public List<CarouselBO> getCarousel(int pageNum, int pageSize) {
        return carouselRepo.findAll(PageRequest.of(pageNum,pageSize)).stream().map(CarouselDO::toCarouselBO).collect(Collectors.toList());
    }

    @Override
    public List<CarouselBO> getCarouselByTitle(String title) {
        if (!carouselRepo.existsByTitleContaining(title))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该轮播信息相关标题！");

        return carouselRepo.findByTitleContaining(title).stream().map(CarouselDO::toCarouselBO).collect(Collectors.toList());
    }

    @Override
    public List<CarouselBO> getCarouselByLabel(String label) {
        if (!carouselRepo.existsByLabel(label))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该标签！");
        return carouselRepo.findByLabel(label).stream().map(CarouselDO::toCarouselBO).collect(Collectors.toList());
    }

    @Override
    public CarouselBO getCarouselById(String carouselId) {
        if (!carouselRepo.existsByCarouselId(carouselId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该轮播消息id！");
        return carouselRepo.findByCarouselId(carouselId).toCarouselBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CarouselBO createCarousel(CarouselRequest request) {
        CarouselDO carouselDO = new CarouselDO();
        BeanUtils.copyProperties(request,carouselDO);

        //help gc
        request = null;

        carouselDO.setCarouselId(bizIdFactory.getCarousel());
        carouselRepo.save(carouselDO);
        return carouselDO.toCarouselBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CarouselBO deleteByCarouselId(String carouselId) {
        if (!carouselRepo.existsByCarouselId(carouselId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该轮播消息id！");

       CarouselBO carouselBO = carouselRepo.findByCarouselId(carouselId).toCarouselBO();
       carouselRepo.deleteByCarouselId(carouselId);
        return carouselBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CarouselBO> deleteByLabel(String label) {
        if (!carouselRepo.existsByLabel(label))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该标签！");

        List<CarouselBO> carouselList= carouselRepo.findByLabel(label).stream().map(CarouselDO::toCarouselBO).collect(Collectors.toList());
        carouselRepo.deleteByLabel(label);
        return carouselList;
    }

    @Override
    public CarouselBO updateCarouse(CarouselRequest request) {

        if (!carouselRepo.existsByCarouselId(request.getCarouselId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该轮播消息id！");

        CarouselDO carousel = carouselRepo.findByCarouselId(request.getCarouselId());

        if (!StringUtils.isEmpty(request.getTitle()))
            carousel.setTitle(request.getTitle());

        if (!StringUtils.isEmpty(request.getText()))
            carousel.setText(request.getText());

        if (!StringUtils.isEmpty(request.getLabel()))
            carousel.setCarouselId(request.getCarouselId());

        if (!StringUtils.isEmpty(request.getSubDate()))
            carousel.setSubDate(request.getSubDate());

        if (!StringUtils.isEmpty(request.getNote()))
            carousel.setNote(request.getNote());

        carouselRepo.save(carousel);

        return carousel.toCarouselBO();


    }
}
