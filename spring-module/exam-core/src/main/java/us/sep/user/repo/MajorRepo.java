package us.sep.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import us.sep.user.entity.MajorDO;

import java.util.List;
import java.util.Optional;

public interface MajorRepo extends JpaRepository<MajorDO, Long> {

    List<MajorDO> findByMajor(String major);

    Optional<MajorDO> findByClassName(String className);

    Optional<MajorDO> findByDisciplineAndClassName(String discipline,String className);

    void deleteByMajor(String major);

    void deleteByClassName(String className);

    boolean existsByClassName(String className);

    boolean existsByDiscipline(String discipline);

    boolean existsByMajor(String major);



}
