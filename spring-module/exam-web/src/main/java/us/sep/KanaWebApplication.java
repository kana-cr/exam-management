package us.sep;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.enums.ExamTypeEnum;
import us.sep.biz.user.enums.RoleTypeEnum;
import us.sep.exam.entity.ExamTypeDO;
import us.sep.exam.repo.ExamTypeRepo;
import us.sep.message.entity.ChannelDO;
import us.sep.message.repo.ChannelRepo;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;
import us.sep.user.repo.RoleRepo;
import us.sep.user.repo.UserRepo;
import us.sep.user.repo.UserRoleRepo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Optional;


/**
 * @author kana
 */

@EnableJpaAuditing
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync
@SpringBootApplication()
public class KanaWebApplication  {

    @Value("${kana.password}")
    private String password;

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
    private BizIdFactory bizIdFactory;



    public static void main(String[] args) {
        SpringApplication.run(KanaWebApplication.class, args);
    }

    @PostConstruct
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
                    .userId(bizIdFactory.getUserId()).build();
            userRepo.save(user);
        }
        if (user != null) {
            Role role = roleRepo.findByName(RoleTypeEnum.ADMIN.getName()).get();
            UserRole userRole = new UserRole(user, role);
            if (!userRoleRepo.findByRoleAndUser(userRole.getRole(), userRole.getUser()).isPresent())
                userRoleRepo.save(userRole);
            userRole = null;
        }
    }
}
