package us.sep.message.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.message.entity.MessageDO;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<MessageDO,Long> {

    List<MessageDO> findByExamTypeId(String examType);

    List<MessageDO> findByChannel(String channel);

}
