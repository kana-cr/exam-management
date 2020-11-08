package us.sep.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.common.entity.ImageDO;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<ImageDO,Long> {

    List<ImageDO> findByUserId(String userId);

    ImageDO findByImageId(String imageId);

    ImageDO findByImageNameAndUserId(String imageName , String userId);

    List<ImageDO> findByTag(String tag);

    void deleteByUserId(String userId);

    void deleteByImageId(String imageId);

    boolean existsByUserId(String userId);

    //用来标识用户上传的头像图片
    boolean existsByUserIdAndTag(String userId , String tag);

    //用户不能上传同名图片
    boolean existsByImageNameAndUserId(String imageName , String userId);

    boolean existsByImageId(String imageId);
}
