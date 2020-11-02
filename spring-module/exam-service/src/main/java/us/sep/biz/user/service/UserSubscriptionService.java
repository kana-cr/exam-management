package us.sep.biz.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.user.request.UserSubRequest;
import us.sep.message.builder.MessageBO;
import us.sep.message.builder.UserSubscriptionBO;
import us.sep.message.entity.ChannelDO;
import us.sep.message.entity.MessageDO;
import us.sep.message.entity.UserSubscriptionDO;
import us.sep.message.repo.ChannelRepo;
import us.sep.message.repo.MessageRepo;
import us.sep.message.repo.UserSubscriptionRepo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserSubscriptionService {

    @Resource
    UserSubscriptionRepo userSubscriptionRepo;

    @Resource
    ChannelRepo channelRepo;

    @Resource
    MessageRepo messageRepo;

    @Resource
    BizIdFactory bizIdFactory;

    @Transactional(rollbackFor = Exception.class)
    public List<MessageBO> getChannelMessage(String userId){
        List<MessageBO> messageBOList = new ArrayList<>();
        List<String> channelIds = findByUserId(userId);
        if (channelIds.isEmpty())
            return new ArrayList<>();
        for (String channelId:channelIds) {
            Optional<ChannelDO> optional = channelRepo.findByChannelId(channelId);
            if (!optional.isPresent())
                continue;
           String channel = optional.get().getChannel();
            for (MessageDO messageDO:messageRepo.findByChannel(channel)) {
                messageBOList.add(messageDO.ToMessageBO());
            }
        }
        return messageBOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserSubscriptionBO createUserSub(UserSubRequest request){

        UserSubscriptionDO userSubscriptionDO = new UserSubscriptionDO();
        BeanUtils.copyProperties(request,userSubscriptionDO);
        userSubscriptionDO.setUserChannelId(bizIdFactory.getUserChannelId());
        userSubscriptionRepo.save(userSubscriptionDO);
       return userSubscriptionDO.ToUserSubBO();
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelUserSubByUserChannelId(String userChannelId){
        if (userSubscriptionRepo.existsByUserChannelId(userChannelId))
        userSubscriptionRepo.deleteByUserChannelId(userChannelId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelUserSubByUserId(String userId){
        if (userSubscriptionRepo.existsByUserId(userId))
            userSubscriptionRepo.deleteAllByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelUserSubByChannelId(String channelId){
        if (userSubscriptionRepo.existsByChannelId(channelId))
            userSubscriptionRepo.deleteAllByChannelId(channelId);
    }


    public List<UserSubscriptionBO> findAll(int pageNum ,int pageSize){
        return userSubscriptionRepo.findAll(PageRequest.of(pageNum,pageSize)).stream()
                .map(UserSubscriptionDO::ToUserSubBO).collect(Collectors.toList());
    }


    public List<UserSubscriptionBO> findByUserId(String userId,int pageNum ,int pageSize){
        return userSubscriptionRepo.findByUserId(userId,PageRequest.of(pageNum,pageSize)).getContent().stream()
                .map(UserSubscriptionDO::ToUserSubBO).collect(Collectors.toList());
    }


    public List<UserSubscriptionBO> findByChannelId(String channelId,int pageNum ,int pageSize){
        return userSubscriptionRepo.findByChannelId(channelId,PageRequest.of(pageNum,pageSize)).getContent().stream()
                .map(UserSubscriptionDO::ToUserSubBO).collect(Collectors.toList());
    }


    public boolean ifUserSubExist(UserSubRequest request){
        return userSubscriptionRepo.existsByUserIdAndChannelId(request.getUserId(), request.getChannelId());
    }

    private List<String> findByUserId(String userId){
        return userSubscriptionRepo.findByUserId(userId).stream().map(UserSubscriptionDO::getChannelId).collect(Collectors.toList());
    }
}
