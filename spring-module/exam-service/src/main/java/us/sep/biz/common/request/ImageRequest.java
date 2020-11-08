package us.sep.biz.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {


    private String imageId;

    //用于拼接获取图片地址
    @NotBlank
    private String imageName;

    //上传者id
    @NotBlank
    private String userId;

    //分类 可用于标识用途
    @NotBlank
    private String tag;




}
