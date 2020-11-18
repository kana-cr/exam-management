/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.sep.util.aliyun;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author MessiahJK
 * @version : AliyunOssUtil.java 2019/10/06 0:10 MessiahJK
 */

@Component
public class AliyunOssUtil {

    /**
     * 斜杠
     */
    private final String FLAG_SLANTING_ROD = "/";
    /**
     * http://
     */
    private final String FLAG_HTTP = "http://";
    /**
     * https://
     */
    private final String FLAG_HTTPS = "https://";
    /**
     * 空字符串
     */
    private final String FLAG_EMPTY_STRING = "";
    /**
     * 点号
     */
    private final String FLAG_DOT = ".";
    /**
     * 横杠
     */
    private final String FLAG_CROSSBAR = "-";

    /**
     * 缺省的最大上传文件大小：20M
     */
    private final int DEFAULT_MAXIMUM_FILE_SIZE = 20;

    private static final String url = "https://kana-bucket.oss-cn-beijing.aliyuncs.com/";

    /**
     * endpoint
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * access key id
     */
    @Value("${aliyun.oss.keyid}")
    private String accessKeyId;

    /**
     * access key secret
     */
    @Value("${aliyun.oss.keysecret}")
    private String accessKeySecret;

    /**
     * bucket name (namespace)
     */
    @Value("${aliyun.oss.bucketname}")
    private String bucketName;

    /**
     * file host (dev/test/prod)
     */
    @Value("${aliyun.oss.filehost}")
    private String fileHost;


    /**
     * 以文件流的方式上传文件
     *
     * @param fileName    文件名称
     * @param filePath    文件路径
     * @param inputStream 文件输入流
     * @return
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    public String uploadFile(String fileName, String filePath, InputStream inputStream) {
        return coreUpload(fileName, filePath, inputStream);
    }

    /**
     * 核心上传功能
     *
     * @param fileName    文件名
     * @param filePath    文件路径
     * @param inputStream 文件输入流
     * @return
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    private String coreUpload(String fileName, String filePath, InputStream inputStream) {
        if (StringUtils.isEmpty(fileName) || inputStream == null) {
            return null;
        }
        if (StringUtils.isEmpty(filePath)) {
            Calendar rightNow = Calendar.getInstance();
            String dateCategory = rightNow.get(Calendar.YEAR) + "" + (rightNow.get(Calendar.MONTH) + 1) + "" + rightNow.get(Calendar.DATE) + "" + rightNow.get(Calendar.HOUR_OF_DAY);
            filePath = FLAG_SLANTING_ROD.concat(dateCategory).concat(FLAG_SLANTING_ROD);
        }
        String fileUrl;
        OSSClient ossClient = null;
        try {
            // Create OSS instance
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            // Create bucket if not exists
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            /*********************************/
            // List the bucket in my account
            //listBuckets(ossClient);
            /*********************************/

            // File path format
            if (!filePath.startsWith(FLAG_SLANTING_ROD)) {
                filePath = FLAG_SLANTING_ROD.concat(filePath);
            }
            if (!filePath.endsWith(FLAG_SLANTING_ROD)) {
                filePath = filePath.concat(FLAG_SLANTING_ROD);
            }

            // File url
            StringBuilder buffer = new StringBuilder();
            buffer.append(fileHost).append(filePath).append(fileName);
            fileUrl = buffer.toString();

            // Upload file and set ACL
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream));
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            fileUrl = getHostUrl().concat(fileUrl);

            /***********************************/
            // List objects in your bucket
            //listObjects(ossClient);
            /***********************************/

        } catch (Exception e) {
            e.printStackTrace();
            fileUrl = null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileUrl;
    }

    /**
     * 列出buckets下的所有文件
     *
     * @param ossClient
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    private void listObjects(OSSClient ossClient) {
        System.out.println("Listing objects");
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    /**
     * 列出当前用户下的所有bucket
     *
     * @param ossClient
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    private void listBuckets(OSSClient ossClient) {
        System.out.println("Listing buckets");
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setMaxKeys(500);
        for (Bucket bucket : ossClient.listBuckets()) {
            System.out.println(" - " + bucket.getName());
        }
        System.out.println();
    }

    /**
     * 以文件的形式上传文件
     *
     * @param fileName
     * @param filePath
     * @param file
     * @return
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    public String uploadFile(String fileName, String filePath, File file) {
        if (file == null) {

            return null;
        }
        if (StringUtils.isEmpty(fileName)) {
            String uuidFileName = UUID.randomUUID().toString().replace(FLAG_CROSSBAR, FLAG_EMPTY_STRING);
            String fname = file.getName();
            String suffix = fname.substring(fname.lastIndexOf(FLAG_DOT), fname.length());
            fileName = uuidFileName.concat(suffix);
        }
        InputStream inputStream = null;
        String fileUrl = null;
        try {
            inputStream = new FileInputStream(file);
            fileUrl = uploadFile(fileName, filePath, inputStream);
        } catch (Exception e) {
        } finally {
            IOUtils.safeClose(inputStream);
        }
        return fileUrl;
    }

    /**
     * 获取访问的base地址
     *
     * @return
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    private String getHostUrl() {
        String hostUrl = null;

        if ((this.endpoint).startsWith(FLAG_HTTP)) {
            hostUrl = FLAG_HTTP.concat(this.bucketName).concat(FLAG_DOT)
                    .concat(this.endpoint.replace(FLAG_HTTP, FLAG_EMPTY_STRING)).concat(FLAG_SLANTING_ROD);
        } else if (this.endpoint.startsWith(FLAG_HTTPS)) {
            return FLAG_HTTPS.concat(this.bucketName).concat(FLAG_DOT)
                    .concat(this.endpoint.replace(FLAG_HTTPS, FLAG_EMPTY_STRING)).concat(FLAG_SLANTING_ROD);
        }
        return hostUrl == null?"":hostUrl;
    }


    /**
     * 删除文件
     *
     * @param fileUrl 文件访问的全路径
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)
                || (!fileUrl.startsWith(FLAG_HTTP)
                && !fileUrl.startsWith(FLAG_HTTPS))) {
            return;
        }
        OSSClient ossClient = null;
        try {
            /**
             * http:// bucketname                                dev/test/pic/abc.jpg = key
             * http:// captainad.oss-ap-southeast-1.aliyuncs.com/dev/test/pic/abc.jpg
             */
            String key = fileUrl.replace(getHostUrl(), FLAG_EMPTY_STRING);
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName, key);
        } catch (Exception e) {

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问的全路径
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    public void deleteFileUrl(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)
                || (!fileUrl.startsWith(FLAG_HTTP)
                && !fileUrl.startsWith(FLAG_HTTPS))) {
            return;
        }
        OSSClient ossClient = null;
        try {

            String key = fileUrl.replace(url, FLAG_EMPTY_STRING);
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName, key);
        } catch (Exception e) {

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * 批量删除文件
     *
     * @param urls 文件访问的全路径集合
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     */
    public void deleteFileUrlInBatch(List<String> urls) {
        if (urls.isEmpty()) {
            return;
        }
        OSSClient ossClient = null;
        try {

            List<String> keys = urls.stream().map(key ->  key.replace(url, FLAG_EMPTY_STRING)).collect(Collectors.toList());
            keys.forEach(System.out::println);
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
            //help gc
            keys = null;
            //DeleteObjectsResult deleteObjectsResult =
            //List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

        } catch (Exception e) {

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
