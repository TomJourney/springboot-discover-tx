package com.tom.springboot.tx.note.infrastructure.springboot.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName BusiSchedulerConfig.java
 * @Description TODO
 * @createTime 2025年04月06日 21:49:00
 */
@Configuration
@EnableScheduling
public class BusiSchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler());
    }

    @Bean(name = "taskScheduler", destroyMethod = "shutdown")
    public ScheduledThreadPoolExecutor taskScheduler() {
        ScheduledThreadPoolExecutor scheduledExecutorService =
                new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.DiscardPolicy());
        return scheduledExecutorService;
    }
}
