package us.sep.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import us.sep.common.entity.LogDO;

@Repository
public interface LogRepo extends JpaRepository<LogDO,Long> , JpaSpecificationExecutor<LogDO>  {

   Page<LogDO> findByTransferUserName(String name, Pageable pageable);
    /*
     * 传入Y / N
    */
    Page<LogDO> findByResult(Boolean result, Pageable pageable);

    Page<LogDO> findByIp(String ip, Pageable pageable);

    Page<LogDO> findByAction(String action, Pageable pageable);

    Page<LogDO> findByTransferUserNameAndResultAndIpAndAction(String name,Boolean result,String ip ,String action , Pageable pageable);
}
