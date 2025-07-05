package com.tom.springboot.tx.note.infrastructure.springboot.scheduler;

import com.tom.springboot.tx.note.infrastructure.common.util.BusiDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName BusiScheduleTask.java
 * @Description 探测redis状态定时任务
 * @createTime 2025年04月05日 21:24:00
 */
@Component
@Slf4j
public class BusiProbeRedisScheduleTask {

    @Autowired
    @Qualifier("taskScheduler")
    private ScheduledThreadPoolExecutor taskScheduler;

    @Scheduled(fixedRate = 5000)
    public void reportCurTime() {
        BlockingQueue<Runnable> queue = taskScheduler.getQueue();
        log.info("reportCurTime: curTime = {}, threadName={}, blockQueueSize={}"
                , BusiDateTimeUtils.getNowText(), Thread.currentThread().getName(), queue.size());
    }
}
