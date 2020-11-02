package us.sep.biz.user.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.biz.user.exception.UserNameNotFoundException;
import us.sep.biz.user.request.UserRoleRequest;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;
import us.sep.user.repo.RoleRepo;
import us.sep.user.repo.UserRepo;
import us.sep.user.repo.UserRoleRepo;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Slf4j
public class UserRoleManager {

    private static final String USERNAME = "username:";

    private static final String ROLENAME = "rolename:";

    @Resource
    private UserRepo userRepo;
    @Resource
    private RoleRepo roleRepo;
    @Resource
    private UserRoleRepo userRoleRepo;

    @Transactional(rollbackFor = Exception.class)
    public void createUserRole(UserRoleRequest request){
        String userName = request.getUserName();
        String roleName = request.getRoleName();
        if (!userRepo.existsByUserName(userName)) {
            throw new UserNameNotFoundException(ImmutableMap.of(USERNAME, userName));
        }
        if (!roleRepo.existsByName(roleName)){
            throw new us.sep.biz.user.exception.RoleNotFoundException(ImmutableMap.of(ROLENAME, roleName));
        }
        User user = userRepo.findByUserName(userName).get();
        Role role = roleRepo.findByName(roleName).get();
        userRoleRepo.save(new UserRole(user,role));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUserRole(UserRoleRequest request){
        String userName = request.getUserName();
        String roleName = request.getRoleName();
        if (!userRepo.existsByUserName(userName)) {
            throw new UserNameNotFoundException(ImmutableMap.of(USERNAME, userName));
        }
        if (!roleRepo.existsByName(roleName)){
            throw new us.sep.biz.user.exception.RoleNotFoundException(ImmutableMap.of(ROLENAME, roleName));
        }
        User user = userRepo.findByUserName(userName).get();
        Role role = roleRepo.findByName(roleName).get();
        Optional<UserRole> userRole = userRoleRepo.findByRoleAndUser(role,user);
        userRole.ifPresent(value -> userRoleRepo.delete(value));
    }



}
