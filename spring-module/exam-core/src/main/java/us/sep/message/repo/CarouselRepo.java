package us.sep.message.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.message.entity.CarouselDO;

import java.util.List;

@Repository
public interface CarouselRepo extends JpaRepository<CarouselDO,Long> {

    CarouselDO findByCarouselId(String carouselId);

    List<CarouselDO> findByTitleContaining(String title);

    List<CarouselDO> findByLabel(String label);

    void deleteByCarouselId(String carouselId);

    void deleteByTitle(String title);

    void deleteByLabel(String label);

    boolean existsByCarouselId(String carouseId);

    boolean existsByTitleContaining(String title);

    boolean existsByLabel(String label);
}
