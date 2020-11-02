package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ChannelRequest;
import us.sep.message.builder.ChannelBO;

import java.util.List;

public interface ChannelService {

    ChannelBO createChannel(ChannelRequest request);

    List<ChannelBO> getChannel(ChannelRequest request , int pageNum , int pageSize);

    ChannelBO updateChannel(ChannelRequest request);

    void deleteChannel(String channelId);

}
