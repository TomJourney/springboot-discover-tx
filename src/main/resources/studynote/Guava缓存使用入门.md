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

















