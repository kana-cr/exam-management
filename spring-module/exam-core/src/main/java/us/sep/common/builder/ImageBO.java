package us.sep.common.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageBO {


    private String imageId;

    //用于拼接获取图片地址
    private String imageName;

    //上传者id
    private String userId;

    //分类 可用于标识用途
    private String tag;

    //由DO拼接出的url
    private String url;




}
