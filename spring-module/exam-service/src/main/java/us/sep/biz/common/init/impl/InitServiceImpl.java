package us.sep.biz.common.init.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.common.init.InitService;
import us.sep.biz.common.util.RedisUtil;
import us.sep.biz.exam.enums.ExamTypeEnum;
import us.sep.biz.user.enums.RoleTypeEnum;
import us.sep.exam.entity.ExamDetailDO;
import us.sep.exam.entity.ExamTypeDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamTypeRepo;
import us.sep.message.entity.ChannelDO;
import us.sep.message.repo.ChannelRepo;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;
import us.sep.user.repo.RoleRepo;
import us.sep.user.repo.UserRepo;
import us.sep.user.repo.UserRoleRepo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static us.sep.biz.common.config.RedisConfig.*;

@Service
public class InitServiceImpl implements InitService {

    @Value("${kana.password}")
    private String password;

    @Value("${kana.email}")
    private String email;
    @Resource
    private UserRepo userRepo;

    @Resource
    private RoleRepo roleRepo;

    @Resource
    private UserRoleRepo userRoleRepo;

    @Resource
    private ExamTypeRepo examTypeRepo;

    @Resource
    private ChannelRepo channelRepo;

    @Resource
    private ExamDetailRepo examDetailRepo;

    @Resource
    private BizIdFactory bizIdFactory;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void init() {

        //初始化考试大类信息以及对应频道
        for (ExamTypeEnum examType:ExamTypeEnum.values()) {

            if (!examTypeRepo.findByExamTypeName(examType.getName()).isPresent()) {
                ExamTypeDO examTypeDO =  new ExamTypeDO();
                examTypeDO.setExamTypeId(bizIdFactory.getExamTypeId());
                examTypeDO.setExamLimit("本校大一至大四学生");
                examTypeDO.setExamTypeName(examType.getName());
                examTypeDO.setExamTypeDescription(examType.getDescription());
                examTypeRepo.save(examTypeDO);
            }

            //存在该考试类型 录入频道
            Optional<ExamTypeDO> optional = examTypeRepo.findByExamTypeName(examType.getName());
            if (optional.isPresent()){
                String examTypeId = optional.get().getExamTypeId();
                if (!channelRepo.existsByExamTypeId(examTypeId)) {
                    ChannelDO channelDO = new ChannelDO();
                    channelDO.setExamTypeId(examTypeId);
                    channelDO.setChannel(examType.getName());
                    channelDO.setChannelId(bizIdFactory.getChannelId());
                    channelRepo.save(channelDO);
                }
            }

        }
        //初始化角色信息
        for (RoleTypeEnum roleType : RoleTypeEnum.values()) {
            if (!roleRepo.findByName(roleType.getName()).isPresent())
                roleRepo.save(new Role(roleType.getName(), roleType.getDescription()));
        }
        //初始化一个 admin 用户
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = null;
        Optional root = userRepo.findByUserName("kana");
        if (!root.isPresent()) {
            user = User.builder().enabled(true).fullName("admin").userName("kana").password(bCryptPasswordEncoder.encode(password))
                    .userId(bizIdFactory.getUserId()).email(email).build();
            userRepo.save(user);
        }
        if (user != null) {
            Role role = roleRepo.findByName(RoleTypeEnum.ADMIN.getName()).get();
            UserRole userRole = new UserRole(user, role);
            if (!userRoleRepo.findByRoleAndUser(userRole.getRole(), userRole.getUser()).isPresent())
                userRoleRepo.save(userRole);
            userRole = null;
        }
        //全量缓存
        cacheInit();
    }

    private void cacheInit(){
       List<ExamTypeDO> examTypes =  examTypeRepo.findAll();

        for (ExamTypeDO examType:examTypes) {
            redisUtil.zAdd(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examType.getExamTypeId() ,examType.getId());
            redisUtil.hPut(EXAM_TYPE,EXAM_TYPE_ID + examType.getExamTypeId(), JSON.toJSONString(examType.ToExamTypeBO()));
        }

       List<ExamDetailDO> examDetails =  examDetailRepo.findAll();
        for (ExamDetailDO examDetailDO:examDetails) {
            //写入cache
            redisUtil.zAdd(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailDO.getExamDetailId(),examDetailDO.getId());
            redisUtil.hPut(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailDO.getExamDetailId() , JSON.toJSONString(examDetailDO.ToExamDetailBO()));

        }

    }



}
