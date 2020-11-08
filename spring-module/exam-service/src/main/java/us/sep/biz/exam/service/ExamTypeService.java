package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamTypeRequest;
import us.sep.exam.builder.ExamTypeBO;

import java.util.List;

/**
  * @Author kana-cr
  * @Date  2020/10/13 14:32
  **/
public interface ExamTypeService {

    /**
     * 增添新的考试大类
     * @return
     */
    ExamTypeBO save(ExamTypeRequest request);

    /**
     * 删减考试大类(不能删减系统自带的)
     */
    void delete(ExamTypeRequest request);

    /**
     * 通过考试类型id删除
     */
    void delete(String examTypeId);
    /**
     * 查找考试大类信息
     */
    List<ExamTypeBO> find(ExamTypeRequest request , int pageNum , int pageSize);

    /**
     * 更新考试大类信息
     * @return
     */
    ExamTypeBO update(ExamTypeRequest request) throws InterruptedException;
}
