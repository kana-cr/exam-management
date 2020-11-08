package us.sep.biz.common.util;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long expireTime = 30L;

    public static ThreadLocal<String> holder = new ThreadLocal<>();

    @PostConstruct
    public StringRedisTemplate setStringRedisTemplate(){
        //开启事务支持
        this.stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }

    public Boolean setConcurrentLock(String key) throws InterruptedException {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        while (!ops.setIfAbsent(key, JSON.toJSONString(String.valueOf(System.currentTimeMillis() + expireTime)))) {
			//监视key
            stringRedisTemplate.watch(key);
            Long expire = Long.parseLong(ops.get(key));
            if (expire != null && expire < System.currentTimeMillis()) {
                stringRedisTemplate.multi();
                String Expire = String.valueOf(System.currentTimeMillis() + expireTime);
                Long oldExpire = Long.parseLong(ops.getAndSet(key,Expire));
				//cas获取超时 获取失败直接返回
                if (stringRedisTemplate.exec() != null && oldExpire != null && oldExpire < System.currentTimeMillis()) {
                    break;
                }
            } else {
				//解除key的监视
                stringRedisTemplate.unwatch();
            }
            TimeUnit.MILLISECONDS.sleep(3);
        }
        holder.set(ops.get(key));
        return true;
    }

    public void deleteConcurrentLock(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String expire = ops.get(key);
        if(expire.equals(holder.get())){
            stringRedisTemplate.delete(key);
        }
        holder.remove();
    }
}
