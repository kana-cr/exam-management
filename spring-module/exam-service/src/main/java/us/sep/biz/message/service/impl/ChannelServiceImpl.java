package us.sep.biz.message.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.message.request.ChannelRequest;
import us.sep.biz.message.service.ChannelService;
import us.sep.message.builder.ChannelBO;
import us.sep.message.entity.ChannelDO;
import us.sep.message.repo.ChannelRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelRepo channelRepo;

    @Resource
    private BizIdFactory bizIdFactory;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChannelBO createChannel(ChannelRequest request) {
        if (!channelRepo.existsByExamTypeId(request.getExamTypeId())) {
            ChannelDO channelDO = new ChannelDO();
            BeanUtils.copyProperties(request, channelDO);
            channelDO.setChannelId(bizIdFactory.getChannelId());
            channelRepo.save(channelDO);
            return channelDO.ToChannelBO();
        }
        throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该类型频道已经存在");
    }


    @Override
    public List<ChannelBO> getChannel(ChannelRequest request, int pageNum, int pageSize) {

        if (!StringUtils.isEmpty(request.getChannelId())){
            Optional<ChannelDO> optional = channelRepo.findByChannelId(request.getChannelId());
            if (optional.isPresent()){
                ChannelDO channelDO = optional.get();
                List<ChannelBO> list = new ArrayList<>();
                list.add(channelDO.ToChannelBO());
                return list;
            }

        }

        ChannelDO channelDO = new ChannelDO();

        if (!StringUtils.isEmpty(request.getChannel()))
            channelDO.setChannel(request.getChannel());

        if (!StringUtils.isEmpty(request.getExamTypeId()))
            channelDO.setExamTypeId(request.getExamTypeId());


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("channelId")
                .withMatcher("channel" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examTypeId",ExampleMatcher.GenericPropertyMatchers.startsWith());
        Example<ChannelDO> example = Example.of(channelDO , matcher);

        return channelRepo.findAll(example, PageRequest.of(pageNum,pageSize)).stream().map(ChannelDO::ToChannelBO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChannelBO updateChannel(ChannelRequest request) {

        Optional<ChannelDO> optional = channelRepo.findByChannelId(request.getChannelId());
        if (!optional.isPresent()){
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该频道！");
        }
        ChannelDO channelDO = optional.get();
        if (!StringUtils.isEmpty(request.getChannel()))
            channelDO.setChannel(request.getChannel());

        if (!StringUtils.isEmpty(request.getExamTypeId()))
            channelDO.setExamTypeId(request.getExamTypeId());

        channelRepo.save(channelDO);
        return channelDO.ToChannelBO();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteChannel(String channelId) {
        if (!channelRepo.existsByChannelId(channelId)) {
            throw new CustomizeException(CommonResultCode.UNFOUNDED , "找不到该频道！");
        }
        channelRepo.deleteByChannelId(channelId);

    }
}
