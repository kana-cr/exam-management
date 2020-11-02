package us.sep.biz.exam.service;

import us.sep.biz.exam.request.MessageRequest;
import us.sep.message.builder.MessageBO;

import java.util.List;

public interface MessageService {

     void save(MessageRequest request);

     void delete(MessageRequest request);

     List<MessageBO> find(MessageRequest request, int pageNum, int pageSize);
}
