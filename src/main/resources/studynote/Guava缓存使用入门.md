# 【README】

本文总结自[guava_cache](https://www.baeldung.com/guava-cache)

本文主要关注guava缓存的使用，包括基本使用，清除策略，刷新缓存，以及一些批处理操作；

<br>

---

# 【1】如何使用guava缓存

创建guava缓存，并获取key的缓存值。

【maven的pom.xml引入guava依赖】

```xml
<dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>33.5.0-jre</version>
        </dependency>
```

【java代码实现】

```java
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
```

需要注意的是，我们使用了getUnchecked()函数，它计算并加载key对应的值到缓存中，如果key的值不存在的话；

<br>

---

# 【2】清除策略

## 【2.1】通过大小size清除 

我们可以使用*maximumSize()*方法限制缓存大小。如果缓存大小达到限制值，则清除最老缓存项。

【例：限制缓存大小为3】

```java
public static void testLimitCacheSize() {
    // 创建cache加载器
    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @Override
        public String load(String key) throws Exception {
            return key.toUpperCase();
        }
    };

    // 根据cache加载器创建缓存
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(3) // 设置缓存最大大小为3 
            .build(cacheLoader);
    System.out.println(cache.getUnchecked("first"));//FIRST
    System.out.println(cache.getUnchecked("second"));//SECOND
    System.out.println(cache.getUnchecked("third"));//THIRD
    System.out.println(cache.getUnchecked("fourth"));//FOURTH
    System.out.println(cache.size());//3
    System.out.println(cache.getIfPresent("first"));//null
    System.out.println(cache.getIfPresent("fourth"));//FOURTH
}
```

---

<br>

## 【2.2】通过权重清除

我们也可以通过自定义权重函数设置缓存大小，进而清除缓存。

【例：我们length()作为自定义权重函数】

```java
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
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumWeight(6)
            .weigher(weigherByLenth)
            .build(cacheLoader);
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
```

<br>

---

## 【2.3】通过缓存项的空闲时间来清除

自定义缓存清除策略，移除空闲时间超过2ms的缓存项。

```java
public static void testEvictByGtIdle() throws InterruptedException {
    // 创建cache加载器
    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @Override
        public String load(String key) throws Exception {
            return key.toUpperCase();
        }
    };


    // 根据cache加载器创建缓存
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.SECONDS) // 设置缓存项空闲时间最多为2s 
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
```

<br>

---

## 【2.4】通过缓存项的存活时间ttl来清除

【例：设置缓存项存活时间ttl为2s，2s后清除缓存】

```java
public static void testEvictByTtl() throws InterruptedException {
    // 创建cache加载器
    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @Override
        public String load(String key) throws Exception {
            return key.toUpperCase();
        }
    };

    // 根据cache加载器创建缓存
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS) // 设置缓存项存活时间最多为2s
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
```

<br>

---

# 【3】弱引用key

我们可以设置缓存的key为弱引用，这允许垃圾收集器收集那些不被任何其他地方引用的缓存key。

默认情况下，缓存key与缓存值都是强引用，但我们可以使用 weakKeys()函数使我们的缓存保存弱引用的键。

```java
public static void testEvictByWeakReference() throws InterruptedException {
    // 创建cache加载器
    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @Override
        public String load(String key) throws Exception {
            return key.toUpperCase();
        }
    };

    // 根据cache加载器创建缓存
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .weakKeys() // 设置key为弱引用
            .weakValues() // 设置value为弱引用
            .build(cacheLoader);
    System.out.println(cache.getUnchecked("first"));//FIRST
    TimeUnit.SECONDS.sleep(1);
    System.out.println(cache.getIfPresent("first"));//FIRST
}
```

<br>

---

# 【4】软引用key

我们也可以允许垃圾收集器清理基于softValue()函数的缓存值。

<font color=red>注意：过多的软引用可能会影响系统性能，最佳选择是使用maximumSize()函数限制缓存大小</font>

```java
public static void testEvictBySoftReference() throws InterruptedException {
    // 创建cache加载器
    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @Override
        public String load(String key) throws Exception {
            return key.toUpperCase();
        }
    };

    // 根据cache加载器创建缓存
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .softValues() // 设置value为弱引用
            .build(cacheLoader);
    System.out.println(cache.getUnchecked("first"));//FIRST
    TimeUnit.SECONDS.sleep(1);
    System.out.println(cache.getIfPresent("first"));//FIRST
}
```

<br>

---

# 【5】处理值为null的缓存项

默认情况下，当我们尝试加载值为null的缓存时，guava会抛出异常，因为null是没有意义的。

但是，如果一个null值在我们的代码中有意义，则可以使用Optional类来读取值为null的缓存项。





















