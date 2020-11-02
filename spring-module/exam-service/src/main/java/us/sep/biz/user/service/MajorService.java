package us.sep.biz.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.biz.user.request.MajorRequest;
import us.sep.user.builder.MajorBO;
import us.sep.user.entity.MajorDO;
import us.sep.user.repo.MajorRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MajorService {

    @Resource
    MajorRepo majorRepo;

    @Transactional(rollbackFor = Exception.class)
    public MajorBO createMajor(MajorRequest request){
        if (majorRepo.existsByClassName(request.getClassName()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"已存在该专业班级");
        MajorDO major = new MajorDO();
        BeanUtils.copyProperties(request,major);
        majorRepo.save(major);
        return major.ToMajorBO();
    }

    public List<MajorBO> findMajors(int pageNum , int pageSize){
        return majorRepo.findAll(PageRequest.of(pageNum,pageSize)).stream().map(MajorDO::ToMajorBO).collect(Collectors.toList());
    }


    public List<MajorBO> findMajorByName(String major){
        if (!majorRepo.existsByMajor(major))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到该专业");
        List<MajorDO> majors=majorRepo.findByMajor(major);
        return majors.stream().map(MajorDO::ToMajorBO).collect(Collectors.toList());
    }


    public MajorBO findMajorByClass(String className){
        Optional<MajorDO> optional=majorRepo.findByClassName(className);
        if (optional.isPresent())
            return optional.get().ToMajorBO();

        throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到该班级");
    }

   /* @Transactional(rollbackFor = Exception.class)
    public MajorBO modifyMajor(MajorRequest request){
        Optional<MajorDO> optional=majorRepo.findByDisciplineAndClassName(request.getDiscipline(),request.getClassName());
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到该专业");

        MajorDO major = optional.get();
        if (!StringUtils.isEmpty(request.getDiscipline()))
            major.setDiscipline(request.getDiscipline());

        if (!StringUtils.isEmpty(request.getClassName()))
            major.setClassNumber(request.getClassName());

        if (!StringUtils.isEmpty(request.getClassNumber()))
            major.setClassNumber(request.getClassNumber());

        majorRepo.save(major);

            return major.ToMajorBO();

    }*/

    @Transactional(rollbackFor = Exception.class)
    public List<MajorBO> deleteMajor(String major){
        if (!majorRepo.existsByMajor(major))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到该专业");

        List<MajorDO> majors = majorRepo.findByMajor(major);
        majorRepo.deleteByMajor(major);

      return majors.stream().map(MajorDO::ToMajorBO).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public MajorBO deleteByClassName(String className){
        if (!majorRepo.existsByClassName(className))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到该班级");
        MajorBO major = majorRepo.findByClassName(className).get().ToMajorBO();
        majorRepo.deleteByClassName(className);
        return  major;
    }

}
