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

自定义缓存清除策略，移除空闲时间超过2s的缓存项

（<font color=red>expireAfterAccess(2, TimeUnit.SECONDS)方法：超过2s没有访问或读写某缓存项，则该缓存项失效 </font>）。

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
            .expireAfterAccess(2, TimeUnit.SECONDS) // 设置缓存项无读写请求的空闲时间最多为2s 
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

## 【2.4】通过缓存项的更新时间来清除

【例】expireAfterWrite(2, TimeUnit.SECONDS)-超过2s没有更新，则该缓存失效

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
            .expireAfterWrite(2, TimeUnit.SECONDS) // 超过2s没有更新，则该缓存失效
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

```java
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
```

<br>

---

# 【6】刷新缓存

## 【6.1】手动刷新缓存

使用LoadingCache.refresh(key) 手动刷新缓存。

refresh()方法会强行让LoadingCache加载键key的缓存值value。<font color=red>直到新缓存值成功加载，get(key)会返回之前的缓存值value</font>。

```java
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
```

<br>

---

## 【6.2】自动刷新

我们也可以使用 *CacheBuilder.refreshAfterWrite(duration)*  自动刷新缓存值。

需要注意的是，`refreshAfterWrite(duration)` 仅使键在指定的持续时间后才有资格刷新。<font color=red>实际上，只有当使用 `get(key)` 查询相应的条目时，值才会真正刷新</font>。

refreshAfterWrite(1,TimeUnit.MINUTES)：缓存项key-value写入1分钟后才刷新；

```java
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
```

【打印结果】

```c++
FIRST
FIRST
load方法，创建缓存key-value键值对
SECOND
load方法，创建缓存key-value键值对
THIRD
```

<br>

---

# 【7】预加载缓存

可以使用putAll()方法插入多条记录的缓存。

【例】使用Map添加多条记录到我们的缓存。

```java
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
```

【打印结果】

```c++
FIRST
SECOND
load方法，创建缓存key-value键值对
THIRD

```

<br>

---

# 【8】缓存移除通知

有些时候，当缓存项被移除时，我们需要采取相应动作。

我们可以注册一个*RemovalListener*监听器来接受缓存项被移除的通知。我们也可以通过*getCause()*方法获取缓存被移除的原因。

【例】获取缓存被移除的通知：当第4个元素因为超过缓存大小而被移除时，我们的程序会收到通知。

```java
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
```

【打印结果】

```c++
FIRST
SECOND
THIRD
缓存移除原因：SIZE，键=first 
FOUR

```

# 【9】补充

下面有一些关于guava缓存的额外笔记

1. 它是线程安全的；
2. 我们可以通过put(k,v) 插入缓存；
3. 可以使用*CacheStats* ( *hitRate()*, *missRate()*, ..) 衡量我们的缓存性能；

# 【10】总结

参考[深入Guava Cache的refresh和expire刷新机制](!https://blog.csdn.net/csdnlijingran/article/details/138545990)

## 【10.1】缓存失效的3种方式

1. expireAfterAccess表示多久没有访问（读或写）就失效
2. expireAfterWrite表示多久没有更新就失效
    expireAfterAccess和expireAfterWrite注意点如下：
    使用场景：业务非常注重缓存的时效性
    缺点：性能较差，缓存过期后，所有线程都要等待和锁争用，尽管guava可以保证只有一个线程load缓存（很好地防止缓存失效的瞬间大量请求穿透到后端引起雪崩效应），但是其他线程也要等待和锁争用
3. refreshAfterWrite表示继上次更新后多久刷新
    优点与缺点： 优点是refresh性能要比load好很多，guava保证只有一个线程refresh缓存，缺点是其他缓存返回旧值，这个旧值可能是很多之前的旧值（原因refresh动作不是自动轮询执行的，而是在get请求的时候才会检查是否需要refresh、如需要在refresh，其他线程直接返回旧值可能是很久之前的，有效减少等待和锁的争用，性能较好）
4. 另外补充一下，load时value不能是null，否则get时会抛出异常，如果value值可能是null，则要用Optional包一下，避免通过try-catch处理null

## 【10.2】获取缓存的2种方式

1. get(key) 方法：
2. getUnchecked(key)方法： 



























