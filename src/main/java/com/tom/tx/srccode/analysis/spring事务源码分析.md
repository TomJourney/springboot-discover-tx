# 【README】

总结自B站： [https://www.bilibili.com/video/BV1Rw4m197uX?spm_id_from=333.788.player.switch&vd_source=a7f8b3035e870c8df27f1d01a17aac7f&p=2](https://www.bilibili.com/video/BV1Rw4m197uX?spm_id_from=333.788.player.switch&vd_source=a7f8b3035e870c8df27f1d01a17aac7f&p=2)

---

# 【1】spring事务源码分析

## 【1.1】JDBC事务

spring事务基于jdbc事务，我们先了解jdbc事务

```java
Connection connection = getConnection();
connection.setAutoCommit(false);
// 执行sql操作
// 执行sql完成后提交
connection.commit();
connection.rollback();
```

<br>

## 【1.2】spring中与事务相关的对象

1. 首先spring如果需要操作事务离不开jdbc那一套，也就是要先获取连接；为了管理连接创建，关闭，回收等操作，需要引入数据源 DataSource（用于管理数据库连接池）；
   1. <font color=red>数据源定义：用于管理数据库连接池的标准接口，相比直接使用JDBC `DriverManager`，它能有效提高数据库访问性能、分配及回收连接</font>;
      1. 主流实现包括 **[HikariCP](https://www.google.com/search?q=HikariCP&sca_esv=7996c7c19c681191&biw=1201&bih=863&sxsrf=ANbL-n6jq-6bDc-Crtg6C_Wu8DlbBrV_0g%3A1776565485059&ei=7TzkafGmA66m2roP4_fvwAQ&ved=2ahUKEwjz8Oaa7viTAxW28DQHHdE4F7kQgK4QegQIARAB&uact=5&oq=java数据源&gs_lp=Egxnd3Mtd2l6LXNlcnAiDWphdmHmlbDmja7mupAyBBAjGCcyBRAAGO8FMgUQABjvBTIFEAAY7wUyBRAAGO8FMgUQABjvBUimLlC1IlimJHADeACQAQCYAYABoAG2A6oBAzAuNLgBA8gBAPgBAZgCBKACd8ICCBAAGIAEGLADwgIJEAAYsAMYBxgemAMAiAYBkAYKkgcDMy4xoAeOBrIHAzAuMbgHbcIHBTAuMy4xyAcJgAgA&sclient=gws-wiz-serp&mstk=AUtExfAd3wq2bfb1X8_K6EwzgOw62UB2scIOdFe8a08a_xfJQMCGyNO-uoMQjczsNmw-cVtuA7X7tTP_ncGDvXDZR1p4NtqKa-ADarLtWYV0UD2eDA_o3E21cgz-oYx35VKsoAYvAgpVUeSHKwXOWirGv72zSR2OQGMSxC0U8fRT-KF_5QOS7GSjWc_lwNEgkFKz1H_8JmyZ_imTiO7icIlLo0ZUr5PJSnBFW2ha95OiSkuRHq-nFLofY4wkmRdfYgm0sfY3bdu7P4FdqPNZs5EVKtCD&csui=3)**（SpringBoot默认，速度快）、**[Druid](https://www.google.com/search?q=Druid&sca_esv=7996c7c19c681191&biw=1201&bih=863&sxsrf=ANbL-n6jq-6bDc-Crtg6C_Wu8DlbBrV_0g%3A1776565485059&ei=7TzkafGmA66m2roP4_fvwAQ&ved=2ahUKEwjz8Oaa7viTAxW28DQHHdE4F7kQgK4QegQIARAC&uact=5&oq=java数据源&gs_lp=Egxnd3Mtd2l6LXNlcnAiDWphdmHmlbDmja7mupAyBBAjGCcyBRAAGO8FMgUQABjvBTIFEAAY7wUyBRAAGO8FMgUQABjvBUimLlC1IlimJHADeACQAQCYAYABoAG2A6oBAzAuNLgBA8gBAPgBAZgCBKACd8ICCBAAGIAEGLADwgIJEAAYsAMYBxgemAMAiAYBkAYKkgcDMy4xoAeOBrIHAzAuMbgHbcIHBTAuMy4xyAcJgAgA&sclient=gws-wiz-serp&mstk=AUtExfAd3wq2bfb1X8_K6EwzgOw62UB2scIOdFe8a08a_xfJQMCGyNO-uoMQjczsNmw-cVtuA7X7tTP_ncGDvXDZR1p4NtqKa-ADarLtWYV0UD2eDA_o3E21cgz-oYx35VKsoAYvAgpVUeSHKwXOWirGv72zSR2OQGMSxC0U8fRT-KF_5QOS7GSjWc_lwNEgkFKz1H_8JmyZ_imTiO7icIlLo0ZUr5PJSnBFW2ha95OiSkuRHq-nFLofY4wkmRdfYgm0sfY3bdu7P4FdqPNZs5EVKtCD&csui=3)**（阿里开源，监控强）、[**C3P0**](https://www.google.com/search?q=C3P0&sca_esv=7996c7c19c681191&biw=1201&bih=863&sxsrf=ANbL-n6jq-6bDc-Crtg6C_Wu8DlbBrV_0g%3A1776565485059&ei=7TzkafGmA66m2roP4_fvwAQ&ved=2ahUKEwjz8Oaa7viTAxW28DQHHdE4F7kQgK4QegQIARAD&uact=5&oq=java数据源&gs_lp=Egxnd3Mtd2l6LXNlcnAiDWphdmHmlbDmja7mupAyBBAjGCcyBRAAGO8FMgUQABjvBTIFEAAY7wUyBRAAGO8FMgUQABjvBUimLlC1IlimJHADeACQAQCYAYABoAG2A6oBAzAuNLgBA8gBAPgBAZgCBKACd8ICCBAAGIAEGLADwgIJEAAYsAMYBxgemAMAiAYBkAYKkgcDMy4xoAeOBrIHAzAuMbgHbcIHBTAuMy4xyAcJgAgA&sclient=gws-wiz-serp&mstk=AUtExfAd3wq2bfb1X8_K6EwzgOw62UB2scIOdFe8a08a_xfJQMCGyNO-uoMQjczsNmw-cVtuA7X7tTP_ncGDvXDZR1p4NtqKa-ADarLtWYV0UD2eDA_o3E21cgz-oYx35VKsoAYvAgpVUeSHKwXOWirGv72zSR2OQGMSxC0U8fRT-KF_5QOS7GSjWc_lwNEgkFKz1H_8JmyZ_imTiO7icIlLo0ZUr5PJSnBFW2ha95OiSkuRHq-nFLofY4wkmRdfYgm0sfY3bdu7P4FdqPNZs5EVKtCD&csui=3)和DBCP。通过配置数据源，应用可实现多数据库切换或连接池高效管理。
2. spring定义了一个类 TransactionManager-事务管理器，事务管理器保存了数据源，事务管理器的dataSource需要手动传给事务管理器。如下。

```java
@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/myspring?useUnicode=true&rewriteBatchedStatement=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        return mybatisSqlSessionFactoryBean;
    }

    /**
     * 事务管理器 -- 关联一个数据源
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
```



















