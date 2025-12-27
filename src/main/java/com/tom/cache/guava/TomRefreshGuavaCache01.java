package com.tom.cache.guava;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * guava缓存 ( https://www.baeldung.com/guava-cache )
 *
 * @author tom
 * @version 1.0.0Ò
 * @createTime 2025年12月21日 21:20:00
 */
public class TomRefreshGuavaCache01 {

    public static void main(String[] args) throws Exception {
        testRefreshAfterWrite();
    }

    public static void testRefreshAfterWrite() throws ExecutionException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println("load方法，创建缓存key-value键值对");
                return key.toUpperCase();
            }
        };
        // 根据cache加载器创建缓存
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.MINUTES) // 设置自动刷新时间为1分钟
                .build(cacheLoader);
        System.out.println(loadingCache.get("first")); // 第1次获取key=first的缓存值, 输出FIRST
        System.out.println(loadingCache.get("first")); // 第2次获取key=first的缓存值, 输出FIRST
        System.out.println(loadingCache.get("second")); // 第1次获取key=second的缓存值， 输出SECOND
        System.out.println(loadingCache.getUnchecked("third")); // 第1次获取key=third的缓存值, 输出THIRD
    }


    public static void testGuava01() throws ExecutionException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };
        // 根据cache加载器创建缓存
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().build(cacheLoader);
        System.out.println(loadingCache.get("first")); // 第1次获取key=first的缓存值
        loadingCache.refresh("first");
        System.out.println(loadingCache.get("first")); // 第2次获取key=first的缓存值
    }
}
