package us.sep.biz.user.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.biz.user.exception.RoleAlreadyExistException;
import us.sep.biz.user.request.RoleCreateRequest;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;
import us.sep.user.repo.RoleRepo;
import us.sep.user.repo.UserRepo;

import javax.annotation.Resource;
import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {

    private static final String ROLENAME = "rolename:";

    @Resource
    RoleRepo roleRepo;

    @Resource
    UserRepo userRepo;

    @Transactional(rollbackFor = Exception.class)
    public void save(RoleCreateRequest request){
        ensureRoleNotExist(request.getName());
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        roleRepo.save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(RoleCreateRequest request){
        try {
           Role role = roleRepo.findByName(request.getName()).orElseThrow(() -> new RoleNotFoundException("角色不存在") );
            if (Objects.nonNull(request.getName()))
                role.setName(request.getName());
            if (Objects.nonNull(request.getDescription()))
                role.setDescription(request.getDescription());
            roleRepo.save(role);
        } catch (RoleNotFoundException e) {
            log.info("角色不存在");
        }

    }

    public Role find(RoleCreateRequest request){
       Optional<Role> role = roleRepo.findByName(request.getName());
       return role.orElse(null);
    }


    public List<Role> getAllRole(){
        return roleRepo.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(RoleCreateRequest request){
       if (!roleRepo.existsByName(request.getName())){
           throw new us.sep.biz.user.exception.RoleNotFoundException(ImmutableMap.of(ROLENAME, request.getName()));
       }
        roleRepo.deleteByName(request.getName());
    }

    public Boolean IsManagerOrAdmin(String username){
        Optional<User> userOptional = userRepo.findByUserName(username);
        if(userOptional.isPresent()) {
            User user =userOptional.get();
            List<UserRole> userRole = user.getUserRoles();
            for (UserRole role:userRole) {
               if (StringUtils.equals(role.getRole().getName(),"MANAGER") || StringUtils.equals( role.getRole().getName(),"ADMIN"))
                   return true;
            }
        }
        log.info("找不到该用户");
        return false;
    }

    private void ensureRoleNotExist(String roleName) {
        boolean exist = roleRepo.findByName(roleName).isPresent();
        if (exist) {
            throw new RoleAlreadyExistException(ImmutableMap.of(ROLENAME, roleName));
        }
    }


}
