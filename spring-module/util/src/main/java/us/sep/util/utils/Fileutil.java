/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import org.springframework.web.multipart.MultipartFile;
import us.sep.util.common.Result;
import us.sep.util.enums.FileEnums;

import java.io.*;

/**
 * @author kiminomiku.ywj
 * @version : FileUtil.java 2018/12/12 PM 1:35 kiminomiku.ywj
 */

public class Fileutil {
    public class UploadUtil {

        private static final String BASE_PATH = "";
        /**
         * 默认路径
         */
        private static final String DEFAULT_FILE_PATH = "file/default/";

        /**
         * 图片默认路径
         */
        private static final String IMAGE_FILE_PATH = "file/image/";

        /**
         * 视频默认路径
         */
        private static final String VIDEO_FILE_PATH = "file/video/";


        /**
         * 上传文件
         *
         * @param file     文件
         * @param filePath 文件路径
         * @return Result
         */
        public Result upload(MultipartFile file, String filePath) {
            String filename = file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    File newFile = new File(BASE_PATH + filePath + filename);
                    //如果不存在文件路径则创建
                    try {
                        if (!newFile.getParentFile().exists()) {
                            try {
                                newFile.getParentFile().mkdirs();
                                newFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {

                    }
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(newFile));
                    out.write(file.getBytes());
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {


                    e.printStackTrace();
                    return new Result(false, FileEnums.UPLOAD_FALSE.name(), e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Result(false, FileEnums.UPLOAD_FALSE.name(), e.getMessage());
                }
                return new Result(true, FileEnums.UPLOAD_SUCCESSFUL.name(), filePath + filename);
            } else {
                return new Result(false, FileEnums.UPLOAD_NULL.name(), "文件为空");
            }
        }


        /**
         * 上传文件
         *
         * @param file 文件
         * @return Result
         */
        public Result upload(MultipartFile file) {
            return upload(file, DEFAULT_FILE_PATH);
        }

        /**
         * 上传图片
         *
         * @param file
         * @return
         */
        public Result uploadImage(MultipartFile file) {
            return upload(file, IMAGE_FILE_PATH);
        }

        /**
         * 不使用路径上传
         *
         * @param file
         * @return
         */
        public Result uploadWithoutPath(MultipartFile file) {
            return upload(file, "");
        }
    }

}
