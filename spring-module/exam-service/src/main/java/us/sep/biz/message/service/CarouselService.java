package us.sep.biz.message.service;

import us.sep.biz.message.request.CarouselRequest;
import us.sep.message.builder.CarouselBO;

import java.util.List;

public interface CarouselService {

    //分页查询
    List<CarouselBO> getCarousel(int pageNum , int pageSize);

    //根据标题模糊查询
    List<CarouselBO> getCarouselByTitle(String title);

    //根据标签查询
    List<CarouselBO> getCarouselByLabel(String label);

    //根据id唯一查询
    CarouselBO getCarouselById(String carouselId);

    CarouselBO createCarousel(CarouselRequest request);

    CarouselBO deleteByCarouselId(String carouselId);

    List<CarouselBO> deleteByLabel(String label);

    CarouselBO updateCarouse(CarouselRequest request);

}
