package us.sep.message.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import us.sep.message.entity.UserSubscriptionDO;

import java.util.List;

public interface UserSubscriptionRepo extends JpaRepository<UserSubscriptionDO,Long> {

    List<UserSubscriptionDO> findByUserId(String userId);

    Page<UserSubscriptionDO> findByUserId(String userId , Pageable request);

    Page<UserSubscriptionDO> findByChannelId(String channel , Pageable request);

    boolean existsByUserIdAndChannelId(String userId , String channelId);

    boolean existsByUserChannelId(String userChannelId);

    void deleteByUserChannelId(String userChannelId);

    boolean existsByChannelId(String channelId);

    void deleteAllByChannelId(String channelId);

    boolean existsByUserId(String userId);

    void deleteAllByUserId(String userId);

}
