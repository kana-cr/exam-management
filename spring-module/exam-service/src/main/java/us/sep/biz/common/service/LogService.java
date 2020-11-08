package us.sep.biz.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.biz.common.request.LogRequest;
import us.sep.common.builder.LogBO;
import us.sep.common.entity.LogDO;
import us.sep.common.repository.LogRepo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogService {

    @Resource
    LogRepo logDORepo;

    @Transactional(rollbackFor = Exception.class)
    public void Save(LogRequest request){
        LogDO logDO = new LogDO();
        logDO.setAction(request.getOperationName());
        logDO.setIp(request.getIp());
        logDO.setMessage(request.getMessage());
        logDO.setTime(request.getTime());
        logDO.setTransferUserName(request.getUserName());
        Boolean result = request.getIfSuccess().equals("Y");
        logDO.setResult(result);

        logDORepo.save(logDO);
    }


    public Page<LogBO> getLog(int pageNum, int pageSize){
        return logDORepo.findAll(PageRequest.of(pageNum, pageSize)).map(LogDO::ToLogBO);
    }


    public List<LogBO> getLogByName(int pageNum, int pageSize , LogRequest request){
        if (StringUtils.isEmpty(request.getUserName()))
            return logDORepo.findAll(PageRequest.of(pageNum, pageSize)).map(LogDO::ToLogBO).getContent();

        return logDORepo.findByTransferUserName(request.getUserName(),PageRequest.of(pageNum, pageSize))
                .map(LogDO::ToLogBO).getContent();

    }


    public List<LogBO> getLogByIp(int pageNum, int pageSize , LogRequest request){
        if (StringUtils.isEmpty(request.getIp()))
            return logDORepo.findAll(PageRequest.of(pageNum, pageSize)).map(LogDO::ToLogBO).getContent();

        return logDORepo.findByIp(request.getIp(),PageRequest.of(pageNum, pageSize))
                .map(LogDO::ToLogBO).getContent();

    }


    public List<LogBO> getLogByResult(int pageNum, int pageSize , LogRequest request){
        if (StringUtils.isEmpty(request.getIfSuccess()) || (!request.getIfSuccess().equals("Y")) && (!request.getIfSuccess().equals("N")))
            return logDORepo.findAll(PageRequest.of(pageNum, pageSize)).map(LogDO::ToLogBO).getContent();

        return logDORepo.findByResult(request.getIfSuccess().equals("Y"),PageRequest.of(pageNum, pageSize))
                .map(LogDO::ToLogBO).getContent();

    }


    public List<LogBO> getLogByOperation(int pageNum, int pageSize , LogRequest request){
        if (StringUtils.isEmpty(request.getOperationName()))
            return logDORepo.findAll(PageRequest.of(pageNum, pageSize)).map(LogDO::ToLogBO).getContent();

        return logDORepo.findByAction(request.getOperationName(),PageRequest.of(pageNum, pageSize))
                .map(LogDO::ToLogBO).getContent();

    }

    public List<LogBO> getLogByCreateAtTime(String createTime){
        return logDORepo.findAll().stream().filter(log -> log.getCreatedAt().toString().startsWith(createTime)).map(LogDO::ToLogBO).collect(Collectors.toList());
    }

    public List<LogBO> getLogByCondition(int pageNum, int pageSize ,LogRequest request){
        LogDO logDO = new LogDO();
        if (!StringUtils.isEmpty(request.getIp()))
        logDO.setIp(request.getIp());

        if (!StringUtils.isEmpty(request.getUserName()))
        logDO.setTransferUserName(request.getUserName());

        if (!StringUtils.isEmpty(request.getTime()))
        logDO.setTime(request.getTime());

        if (!StringUtils.isEmpty(request.getOperationName()))
        logDO.setAction(request.getOperationName());

        if (request.getIfSuccess().equals("Y")) {
            logDO.setResult(true);
        }else if (request.getIfSuccess().equals("N")){
            logDO.setResult(false);
        }
        logDO.setMessage(request.getMessage());


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("transferUserName", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{transferUserName}%
                .withMatcher("action" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{action}%
                .withIgnorePaths("message");//忽略字段，即不管password是什么值都不加入查询条件*/
        Example<LogDO> example = Example.of(logDO , matcher);
        return logDORepo.findAll(example,PageRequest.of(pageNum, pageSize)).getContent().stream().
                filter(log -> log.getCreatedAt().toString().startsWith(request.getCreateTime())).map(LogDO::ToLogBO).collect(Collectors.toList());

    }


}
