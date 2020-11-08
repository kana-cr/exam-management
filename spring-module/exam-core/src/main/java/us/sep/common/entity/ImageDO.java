package us.sep.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.common.builder.ImageBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image", indexes = {
        @Index(name = "image_id", columnList = "imageId")})
public class ImageDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 32,updatable = false)
    private String imageId;

    //用于拼接获取图片地址
    @Column(nullable = false)
    private String imageName;

    //上传者id
    @Column(nullable = false )
    private String userId;

    //分类 可用于标识用途
    @Column(nullable = false)
    private String tag;


    public ImageBO toImageBO(){
        return ImageBO.builder().imageId(imageId).imageName(imageName).userId(userId).tag(tag).build();
    }


}
