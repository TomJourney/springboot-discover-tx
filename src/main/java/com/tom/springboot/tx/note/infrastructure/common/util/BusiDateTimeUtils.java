package com.tom.springboot.tx.note.infrastructure.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName BusiDateTimeUtils.java
 * @Description TODO
 * @createTime 2025年04月05日 21:25:00
 */
public final class BusiDateTimeUtils {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getNowText() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    public static void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("线程sleep异常");
        }
    }

    private BusiDateTimeUtils() {
        // do nothing.
    }
}
