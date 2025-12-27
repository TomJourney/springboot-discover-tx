package com.tom.cache.guava;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.util.concurrent.TimeUnit;

/**
 * guava缓存 ( https://www.baeldung.com/guava-cache )
 *
 * @author tom
 * @version 1.0.0Ò
 * @createTime 2025年12月21日 21:20:00
 */
public class TomGuavaCache01Simple {

    public static void main(String[] args) throws InterruptedException {
        testWhenNullThenOptional();
    }


    public static void testWhenNullThenOptional() throws InterruptedException {
        // 值为Optional实例创建cache加载器
        CacheLoader<String, Optional<String>> cacheLoader = new CacheLoader<>() {
            @Override
            public Optional<String> load(String key) throws Exception {
                return Optional.fromNullable(getSuffix(key));
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, Optional<String>> cache = CacheBuilder.newBuilder()
                .build(cacheLoader);
        System.out.println(cache.getUnchecked("test.txt").get()); // txt
        System.out.println(cache.getUnchecked("hello").isPresent()); // false
    }

    private static String getSuffix(String str) {
        int lastIndex = str.lastIndexOf(".");
        if (lastIndex == -1) {
            return null;
        }
        return str.substring(lastIndex + 1);
    }


    public static void testEvictBySoftReference() throws InterruptedException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().softValues() // 设置value为弱引用
                .build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.getIfPresent("first"));//FIRST
    }

    public static void testEvictByWeakReference() throws InterruptedException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().weakKeys() // 设置key为弱引用
                .weakValues() // 设置value为弱引用
                .build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.getIfPresent("first"));//FIRST
    }

    public static void testEvictByTtl() throws InterruptedException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS) // 设置缓存项存活时间最多为2s
                .build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.getIfPresent("first"));//FIRST

        System.out.println(cache.getUnchecked("second"));//SECOND
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.getIfPresent("second"));//null

        System.out.println(cache.getUnchecked("third"));//THIRD
        TimeUnit.SECONDS.sleep(3);
        System.out.println(cache.getIfPresent("third"));//null
    }

    public static void testEvictByGtIdle() throws InterruptedException {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };


        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.SECONDS) // 设置缓存项空闲时间最多为2s
                .build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.getIfPresent("first"));//FIRST

        System.out.println(cache.getUnchecked("second"));//SECOND
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.getIfPresent("second"));//null

        System.out.println(cache.getUnchecked("third"));//THIRD
        TimeUnit.SECONDS.sleep(3);
        System.out.println(cache.getIfPresent("third"));//null
    }


    public static void testDiyWeightFunction() {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 创建计量器
        Weigher<String, String> weigherByLenth = new Weigher<>() {
            @Override
            public int weigh(String key, String value) {
                return value.length();
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumWeight(6).weigher(weigherByLenth).build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        System.out.println(cache.getUnchecked("second"));//SECOND
        System.out.println(cache.getUnchecked("third"));//THIRD
        System.out.println(cache.getUnchecked("fourth"));//FOURTH
        System.out.println(cache.getUnchecked("last"));//LAST
        System.out.println(cache.size());//1
        System.out.println(cache.getIfPresent("first"));//null
        System.out.println(cache.getIfPresent("second"));//null
        System.out.println(cache.getIfPresent("third"));//null
        System.out.println(cache.getIfPresent("fourth"));//null
        System.out.println(cache.getIfPresent("last"));//LAST
    }

    public static void testLimitCacheSize() {
        // 创建cache加载器
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // 根据cache加载器创建缓存
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(3).build(cacheLoader);
        System.out.println(cache.getUnchecked("first"));//FIRST
        System.out.println(cache.getUnchecked("second"));//SECOND
        System.out.println(cache.getUnchecked("third"));//THIRD
        System.out.println(cache.getUnchecked("fourth"));//FOURTH
        System.out.println(cache.size());//3
        System.out.println(cache.getIfPresent("first"));//null
        System.out.println(cache.getIfPresent("fourth"));//FOURTH
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
