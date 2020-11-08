package us.sep.common.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.util.aliyun.AliyunOssUtil;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @AvoidRepeatableCommit
    @PostMapping("/aliyun")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> uploadImgAliyunOSS(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") String fileName , HttpServletRequest request) throws IOException {

                FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
                String path =aliyunOssUtil.uploadFile(fileName + ".png", "/file/", inputStream);
                Map map=new HashMap(4);
                map.put("path", path);
                return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(), "上传成功");
    }

    /**
     * 删除oss中的文件
     * @param fileUrl
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @DeleteMapping("/aliyun")
    public Result<String> deleteImgAliyunOSS(@RequestParam("fileurl") String fileUrl , HttpServletRequest request) throws IOException {

       aliyunOssUtil.deleteFileUrl(fileUrl);
        return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(), "删除成功");
    }


}
