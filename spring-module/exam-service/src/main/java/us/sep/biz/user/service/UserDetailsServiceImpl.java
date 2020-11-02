package us.sep.biz.user.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import us.sep.biz.user.JwtUser;
import us.sep.user.entity.User;


/**
 * @description UserDetailsService实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userService.find(name);
        return new JwtUser(user);
    }

}
