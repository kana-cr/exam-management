package us.sep.message.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.message.request.CarouselRequest;
import us.sep.biz.message.service.CarouselService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.message.builder.CarouselBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
  * @author kana-cr
  * @version  2020/11/5 9:07
  */
@CrossOrigin
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Resource
    CarouselService carouselService;

    @GetMapping
   public Result<List<CarouselBO>> getAllCarousel( @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.getCarousel(pageNum,pageSize));
    }

    @GetMapping("/title")
    public Result<List<CarouselBO>> getCarouselByTitle(String title){
        AssertUtil.assertStringNotBlank(title,"标题不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.getCarouselByTitle(title));
    }

    @GetMapping("/label")
    public Result<List<CarouselBO>> getAllCarouselByLabel(String label){
        AssertUtil.assertStringNotBlank(label,"标签不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.getCarouselByLabel(label));
    }

    @GetMapping("/single")
    public Result<CarouselBO> getByCarouselId(String carouselId){
        AssertUtil.assertStringNotBlank(carouselId,"轮播消息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.getCarouselById(carouselId));
    }


    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<CarouselBO> createByCarouselId(@Valid  CarouselRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.createCarousel(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<CarouselBO> modifyCarousel(CarouselRequest request , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getCarouselId(),"轮播消息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.updateCarouse(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<CarouselBO> delByCarouselId(String carouselId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(carouselId,"轮播消息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.deleteByCarouselId(carouselId));
    }

    @DeleteMapping("/label")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<CarouselBO>> delAllCarouselByLabel(String label , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(label,"标签不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),carouselService.deleteByLabel(label));
    }



}
