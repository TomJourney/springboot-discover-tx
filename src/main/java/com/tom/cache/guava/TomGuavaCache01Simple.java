package com.tom.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.Locale;
import java.util.Objects;

/**
 * @author tom
 * @version 1.0.0Ò
 * @Description guava缓存 ( https://www.baeldung.com/guava-cache )
 * @createTime 2025年12月21日 21:20:00
 */
public class TomGuavaCache01Simple {

    public static void main(String[] args) {
        testGuava01();
    }

    public static void testGuava01() {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };
        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(cacheLoader);
        System.out.println(cache.size()); // 0
        System.out.println(cache.getUnchecked("hello")); // HELLO
        System.out.println(cache.size()); // 1
    }
}
