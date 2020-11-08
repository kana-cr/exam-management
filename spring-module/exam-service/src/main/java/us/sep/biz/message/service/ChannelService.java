package us.sep.biz.message.service;

import us.sep.biz.message.request.ChannelRequest;
import us.sep.message.builder.ChannelBO;

import java.util.List;

public interface ChannelService {

    ChannelBO createChannel(ChannelRequest request);

    List<ChannelBO> getChannel(ChannelRequest request , int pageNum , int pageSize);

    ChannelBO updateChannel(ChannelRequest request);

    void deleteChannel(String channelId);

}
