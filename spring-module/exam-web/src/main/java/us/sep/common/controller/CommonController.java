package us.sep.common.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import us.sep.util.aliyun.AliyunOssUtil;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/common")
public class CommonController {

    @Resource
    private AliyunOssUtil aliyunOssUtil;

    /**
     * 上传文件到aliyun云存储
     * @param multipartFile
     * @return
     * @throws IOException
     */

    @PostMapping("/aliyun")
    @ResponseBody
    public Result<String> uploadImgAliyunOSS(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") String fileName) throws IOException {

                FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
                String path =aliyunOssUtil.uploadFile(fileName + ".png", "/file/", inputStream);
                Map map=new HashMap(4);
                map.put("path", path);
                return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(), "上传成功");
    }

}
