package us.sep.message.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import us.sep.message.entity.ChannelDO;

import java.util.Optional;

public interface ChannelRepo extends JpaRepository<ChannelDO,Long> {

    Optional<ChannelDO> findByChannelId(String channelId);

    void deleteByChannelId(String channelId);

    void deleteAllByExamTypeId(String id);

    boolean existsByChannelId(String channelId);

    boolean existsByExamTypeId (String examTypeId);
}
