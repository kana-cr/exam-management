package us.sep.biz.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.biz.user.request.UserInfoRequest;
import us.sep.user.builder.UserInfoBO;
import us.sep.user.entity.User;
import us.sep.user.entity.UserInfoDO;
import us.sep.user.repo.UserInfoRepo;
import us.sep.user.repo.UserRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.Optional;

/**
  * @Author kana-cr
  * @Date  2020/10/11 9:41
  **/
@Service
@Slf4j
public class UserInfoService {

    @Resource
    UserInfoRepo userInfoRepo;

    @Resource
    UserRepo userRepo;

    //录入详细信息
    @Transactional(rollbackFor = Exception.class)
    public UserInfoBO Save(UserInfoRequest request) {
        //该用户注册过才能完成用户信息的补充录入
        if (userRepo.existsByUserName(request.getUsername()) ) {
            String userId = userRepo.findByUserName(request.getUsername()).get().getUserId();
            if (!userInfoRepo.existsByUserId(userId)) {
                UserInfoDO userInfo = new UserInfoDO();
                BeanUtils.copyProperties(request, userInfo);
                userInfo.setUserId(userId);
                userInfoRepo.save(userInfo);
                return userInfo.ToUserInfoBO();
            }
        }
        throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户名不存在");
    }
        //更新详细信息
        @Transactional(rollbackFor = Exception.class)
        public UserInfoBO Update(UserInfoRequest request) {
            //存在该用户才能完成信息的更新
            if (userRepo.existsByUserName(request.getUsername()) ) {
                String userId = userRepo.findByUserName(request.getUsername()).get().getUserId();
                if (userInfoRepo.existsByUserId(userId)) {
                    UserInfoDO userInfo = userInfoRepo.findByUserId(userId).get();

                    if (!StringUtils.isEmpty(request.getClassName()))
                        userInfo.setClassName(request.getClassName());

                    if (!StringUtils.isEmpty(request.getIdentificationNumber()))
                        userInfo.setIdentificationNumber(request.getIdentificationNumber());

                    if (!StringUtils.isEmpty(request.getMajor()))
                        userInfo.setMajor(request.getMajor());

                    if (!StringUtils.isEmpty(request.getRealName()))
                        userInfo.setRealName(request.getRealName());

                    if (!StringUtils.isEmpty(request.getStuNo()))
                        userInfo.setStuNo(request.getStuNo());

                    userInfoRepo.save(userInfo);
                    return userInfo.ToUserInfoBO();
                }
                throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户信息未曾录入");
            }
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户");
        }

        //通过用户名查找
        public UserInfoBO findByName(String username){
        Optional<User> userDO = userRepo.findByUserName(username);
        if (userDO.isPresent()) {
            String userid = userDO.get().getUserId();
            Optional<UserInfoDO> userInfo = userInfoRepo.findByUserId(userid);
            if (userInfo.isPresent()) {
                UserInfoDO user = userInfo.get();
                UserInfoBO userInfoBO = new UserInfoBO();
                BeanUtils.copyProperties(user, userInfoBO);
                return userInfoBO;
            }
        }
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户名不存在");

        }


       //通过userid查找
        public UserInfoBO findById(String userid){
        Optional<UserInfoDO> userInfo = userInfoRepo.findByUserId(userid);
       if (userInfo.isPresent()) {
          UserInfoDO user = userInfo.get();
          UserInfoBO userInfoBO = new UserInfoBO();
          BeanUtils.copyProperties(user,userInfoBO);
          return userInfoBO;
       }
           throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户id不存在");

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String username){
        Optional<User> userDO = userRepo.findByUserName(username);
        if (userDO.isPresent()) {
            String userid = userDO.get().getUserId();
            Optional<UserInfoDO> userInfo = userInfoRepo.findByUserId(userid);
            if (userInfo.isPresent()) {
                userInfoRepo.deleteByUserId(userid);
                return;
            }
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"用户信息未录入,无法删除");
        }

        throw new CustomizeException(CommonResultCode.UNFOUNDED,"要删除的用户账号名不存在");
    }


}
