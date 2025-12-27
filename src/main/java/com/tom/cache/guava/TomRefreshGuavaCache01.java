package com.tom.cache.guava;

import com.google.common.base.Optional;
import com.google.common.cache.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        testRemovalNotification();
    }

    public static void testRemovalNotification() throws ExecutionException, InterruptedException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 新建缓存移除监听器
        RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
            @Override
            public void onRemoval(RemovalNotification<String, String> notification) {
                if (notification.wasEvicted()) {
                    System.out.printf("缓存移除原因：%s，键=%s \n", notification.getCause(), notification.getKey());
                    // SIZE, first
                }
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(3) // 设置缓存大小为3
                .removalListener(removalListener) // 传入缓存移除监听器用于构造缓存
                .build(cacheLoader);

        System.out.println(loadingCache.getUnchecked("first"));
        System.out.println(loadingCache.getUnchecked("second"));
        System.out.println(loadingCache.getUnchecked("third"));
        System.out.println(loadingCache.getUnchecked("four"));
    }


    public static void testPreloadCache() throws ExecutionException, InterruptedException {
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
                .refreshAfterWrite(3, TimeUnit.SECONDS) // 设置自动刷新时间为3秒
                .build(cacheLoader);

        Map<String, String> preloadMap = new HashMap<>();
        preloadMap.put("first", "FIRST");
        preloadMap.put("second", "SECOND");
        // 预加载缓存(如应用启动时加载)
        loadingCache.putAll(preloadMap);

        System.out.println(loadingCache.get("first"));
        System.out.println(loadingCache.get("second"));
        System.out.println(loadingCache.getUnchecked("third"));
    }


    public static void testRefreshAfterWrite() throws ExecutionException, InterruptedException {
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
                .refreshAfterWrite(3, TimeUnit.SECONDS) // 设置自动刷新时间为3秒
                .build(cacheLoader);
        System.out.println(loadingCache.get("first")); // 第1次获取key=first的缓存值, 输出FIRST
        // 睡眠11秒
        TimeUnit.SECONDS.sleep(11);

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
