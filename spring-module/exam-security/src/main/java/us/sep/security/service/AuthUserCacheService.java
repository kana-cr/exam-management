package us.sep.security.service;

import com.alibaba.fastjson.JSONObject;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @date 2020/6/27 22:41
 * @since 1.0.0
 */
@Service
public class AuthUserCacheService  {


    @Autowired
    private RedisTemplate redisTemplate;

    private BoundHashOperations<String, String, AuthUser> valueOperations;

    private static final String AUTH_KEY = "JUST-AUTH-KANA::USERS";

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.boundHashOps(AUTH_KEY);
    }

    public AuthUser save(AuthUser user) {
        valueOperations.put(user.getUuid(), user);
        return user;
    }

    public AuthUser getByUuid(String uuid) {
        Object user = valueOperations.get(uuid);
        if(null == user) {
            return null;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(user), AuthUser.class);
    }

    public List<AuthUser> listAll() {
        return new LinkedList<>(Objects.requireNonNull(valueOperations.values()));
    }

    public void remove(String uuid) {
        valueOperations.delete(uuid);
    }


}
