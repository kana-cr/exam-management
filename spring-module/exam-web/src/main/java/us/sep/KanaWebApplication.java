package us.sep;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import us.sep.biz.common.init.InitService;
import us.sep.biz.common.util.RedisUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import static us.sep.biz.common.config.RedisConfig.*;
import static us.sep.biz.common.config.RedisConfig.EXAM_DETAIL_PAGE;


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

    @Resource
    private InitService initService;

    @Resource
    private RedisUtil redisUtil;

    private static KanaWebApplication app;

    public static void main(String[] args) {
        SpringApplication.run(KanaWebApplication.class, args);
        //应用初始化
        app.initService.init();
    }

    @PostConstruct
    private void init() {
        app = this;
        app.initService = this.initService;
        app.redisUtil = this.redisUtil;
    }

    @PreDestroy
    private void shutdown(){
            app.redisUtil.delete(EXAM_TYPE);
            redisUtil.delete(EXAM_TYPE_PAGE);
            redisUtil.delete(EXAM_DETAIL);
            redisUtil.delete(EXAM_DETAIL_PAGE);


    }
}
