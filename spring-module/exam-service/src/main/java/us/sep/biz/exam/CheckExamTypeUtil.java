package us.sep.biz.exam;

import org.springframework.stereotype.Component;
import us.sep.biz.exam.enums.ExamTypeEnum;
import us.sep.exam.repo.ExamTypeRepo;

import javax.annotation.Resource;

@Component
public class CheckExamTypeUtil {

    @Resource
    ExamTypeRepo examTypeRepo;

    //判断类型是否存在
    public boolean checkExamType(String examType){
        for (ExamTypeEnum examTypeEnum:ExamTypeEnum.values()) {
        if (examTypeEnum.getName().equals(examType))
            return true;
        }
        return examTypeRepo.existsByExamTypeName(examType);
    }

    /**
     * 判断是否是初始化时就有的考试类型
     * @param examType
     * @return
     */
    public boolean ifInitType(String examType){
        for (ExamTypeEnum examTypeEnum:ExamTypeEnum.values()) {
            if (examTypeEnum.getName().equals(examType))
                return true;
        }
        return false;
    }

    /**
     * 是否存在该种类型(排除了初始的类型)
     * @param name
     * @return
     */
    public boolean checkOtherExamType(String name){
        for (ExamTypeEnum examTypeEnum:ExamTypeEnum.values()) {
            if (examTypeEnum.getName().equals(name))
                return false;
        }
        return examTypeRepo.existsByExamTypeName(name);
    }
}
