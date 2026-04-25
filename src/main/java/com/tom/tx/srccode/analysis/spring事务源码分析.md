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
2. <font color=red>spring定义了一个类 DataSourceTransactionManager-数据源事务管理器</font>，事务管理器保存了数据源；
   1. <font color=red>数据源事务管理器提供的api包括：提交，回滚，创建，开启，挂起，恢复事务</font>；
   2. 事务管理器的dataSource需要手动传给事务管理器。如下。

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

3. 数据源不代表数据库连接： 具体的连接对象，<font color=red>spring中有一个类 DataSourceTransactionObject-数据源事务对象（DataSourceTransactionManager内部类）</font>；
   1. DataSourceTransactionObject包含了Connection对象；

```java
DataSourceTransactionObject{
  previousIsolationLevel; // 隔离级别 
  savepointAllowed;  // 是否允许savepoint
  currentConnection; // 当前连接
  transactionActive; // 事务是否活跃 
  mustRestoreAutoCommit; // 是否需要重置自动提交 
  Connection currentConnection; // 当前连接信息 
}
```

<br>

4. <font color=red>spring中定义了事务状态类-TransactionStatus </font>：其属性有 spring中事务是否可读，事务是否已完成，事务是否是一个新事务等；

```java
class TransactionStatus{// 事务状态
  boolean rollbackOnly = false; // 是否只读
  boolean completed = false; // 是否完成 
  Object savepoint; // 保存点 
  boolean isNewTransaction; // 是否新开事务
  boolean isNewSynchronization; // 是否同步
}
```

5. <font color=red>spring定义了事务属性类-TransactionAttribute (TransactionAspectSupport内部类)</font>，其属性有事务传播机制，回滚异常，是否需要指定隔离级别；（<font color=red>封装@Transactional注解的属性值</font>）

```java
public interface TransactionAttribute extends TransactionDefinition {
    @Nullable
    String getQualifier();
    Collection<String> getLabels();
    boolean rollbackOn(Throwable ex);
}
```

6. <font color=red>spring定义了TransactionAspectSupport.TransactionInfo</font> 类，把上述4个类关联起来，TransactionInfo提供了事务的所有操作与信息； 
   1. <font color=red>事务管理器： transactionManager  </font>；包含了数据源，提供事务操作接口，如提交，回滚，挂起，恢复等；
   2. <font color=red>事务属性：TransactionAttribute ，@Transactional注解属性值，由程序员配置</font>； 
   3. <font color=red>事务状态： TransactionStatus </font>； 事务状态，如是否只读，是否完成，是否新开事务，是否已有事务，是否嵌套事务，是否有保存点，创建或回滚或释放保存点等；
      1. <font color=red>数据源事务对象：DataSourceTransactionObject （DataSourceTransactionManager内部类）</font>；
         1. 包含事务隔离级别，所属db连接，是否允许保存点；
      2. DataSourceTransactionObject 作为了TransactionStatus的属性存在；

```java
protected static final class TransactionInfo {
    @Nullable
    private final PlatformTransactionManager transactionManager;// 事务管理器：提交，回滚，创建，开启，挂起，恢复事务 
    @Nullable
    private final TransactionAttribute transactionAttribute; // 事物属性 @Transactional注解
    private final String joinpointIdentification;
    @Nullable
    private TransactionStatus transactionStatus; // 事务状态，如是否只读，是否完成，是否新开事务，是否已有事务，是否嵌套事务，是否有保存点，创建或回滚或释放保存点等；
    @Nullable
    private TransactionInfo oldTransactionInfo;

    public TransactionInfo(@Nullable PlatformTransactionManager transactionManager, @Nullable TransactionAttribute transactionAttribute, String joinpointIdentification) {
        this.transactionManager = transactionManager;
        this.transactionAttribute = transactionAttribute;
        this.joinpointIdentification = joinpointIdentification;
    }

    public PlatformTransactionManager getTransactionManager() {
        Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");
        return this.transactionManager;
    }

    @Nullable
    public TransactionAttribute getTransactionAttribute() {
        return this.transactionAttribute;
    }

    public String getJoinpointIdentification() {
        return this.joinpointIdentification;
    }

    public void newTransactionStatus(@Nullable TransactionStatus status) {
        this.transactionStatus = status;
    }

    @Nullable
    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public boolean hasTransaction() {
        return this.transactionStatus != null;
    }

    private void bindToThread() {
        this.oldTransactionInfo = (TransactionInfo)TransactionAspectSupport.transactionInfoHolder.get();
        TransactionAspectSupport.transactionInfoHolder.set(this);
    }

    private void restoreThreadLocalStatus() {
        TransactionAspectSupport.transactionInfoHolder.set(this.oldTransactionInfo);
    }

    public String toString() {
        return this.transactionAttribute != null ? this.transactionAttribute.toString() : "No transaction";
    }
}
```

<br>

---

# 【2】spring事务的源码研究涉及的2大问题

1. spring事务原理主要是两个大问题：
   1. spirng如何完成拦截和增强的；
   2. 增强逻辑是什么？也就是它怎么开启事务，提交事务，回滚事务的？

<br>

## 【2.1】spring如何完成拦截与增强

1. @EnableTransactionManagement：该注解的作用是增强含有@Transactional注解方法的bean；靠 BeanPostProcess-bean后置处理器来增强； 
   1. <font color=red>@EnableTransactionManagement主要导入了2个类，包括AutoProxyRegistrar，ProxyTransactionManagementConfiguration </font>;


### 【2.1.1】引入的第1个类：AutoProxyRegistrar-自动代理注册

<font color=red>AutoProxyRegistrar给spring容器注册了后置处理器InfrastructureAdvisorAutoProxyCreator（BeanPostProcessor的实现类），后置处理器的after方法用于对bean进行增强</font>；

```java
@Import({TransactionManagementConfigurationSelector.class})
public @interface EnableTransactionManagement {
    boolean proxyTargetClass() default false;
    AdviceMode mode() default AdviceMode.PROXY;
    int order() default Integer.MAX_VALUE;
}

// TransactionManagementConfigurationSelector 是一个Selector
public class TransactionManagementConfigurationSelector extends AdviceModeImportSelector<EnableTransactionManagement> {
    protected String[] selectImports(AdviceMode adviceMode) {
        String[] var10000;
        switch (adviceMode) {
            case PROXY -> var10000 = new String[]{AutoProxyRegistrar.class.getName(), ProxyTransactionManagementConfiguration.class.getName()};
            case ASPECTJ -> var10000 = new String[]{this.determineTransactionAspectClass()};
            default -> throw new IncompatibleClassChangeError();
        }
        return var10000;
    }
}
// Selector的作用是: 实现ImportSelector类重写selectImports方法，该方法返回的字符串数组是全限定类名，则spring会将其视为BeanDefinition进行bean实例化
public interface ImportSelector {
	String[] selectImports(AnnotationMetadata importingClassMetadata);
}

// 所以：@EnableTransactionManagement主要导入了2个类，包括AutoProxyRegistrar，ProxyTransactionManagementConfiguration 
```

<br>

【AutoProxyRegistrar】实现ImportBeanDefinitionRegistrar接口，给spring容器当中注册了一个后置处理器InfrastructureAdvisorAutoProxyCreator；

- InfrastructureAdvisorAutoProxyCreator: 实现了后置处理器 BeanPostProcessor，其after方法用户创建bean的代理；

```java
public class AutoProxyRegistrar implements ImportBeanDefinitionRegistrar {
  // 用户注册BeanDefinition，这个bean是InfrastructureAdvisorAutoProxyCreator
  // InfrastructureAdvisorAutoProxyCreator
}

// InfrastructureAdvisorAutoProxyCreator: 实现了后置处理器 BeanPostProcessor
public class InfrastructureAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.initBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }
//...
}

// BeanPostProcessor: bean后置处理器接口，用于bean生命周期管理 
public interface BeanPostProcessor {
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    // after方法主要用于创建bean的代理
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}

// 后置处理器 AbstractAutoProxyCreator 的postProcessBeforeInstantiation()与postProcessAfterInitialization
// after方法调用wrapIfNecessary方法，wrapIfNecessary方法创建代理
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
        if (bean != null) {
            Object cacheKey = this.getCacheKey(bean.getClass(), beanName);
            if (this.earlyBeanReferences.remove(cacheKey) != bean) {
                return this.wrapIfNecessary(bean, beanName, cacheKey);// 
            }
        }
        return bean;
    }
```

<br>

【总结】<font color=red>@EnableTransactionManagement注解作用</font>：

1. 用来拦截符合规则的业务类；
2. 创建bean的代理（增强bean）；因为该注解导入了一个后置处理器，对所有符合规则的bean进行增强；

<br>

### 【2.1.1】引入的第2个类：ProxyTransactionManagementConfiguration-配置类

1. spring-aop代理：增强bean（传统aop）所需做的工作
   1. 定义一个切面-Advisor，是一个类；（<font color=red> Advisor可以翻译为切面顾问或通知顾问 </font>）
   2. 定义一个切点 pointCut；
   3. 定义一个连接点JointPoint（描述需要被增强的规则，即哪些类哪些方法需要被增强）， 属于切点的一条记录，即多个连接点组成一个切点； 
   4. 通知-Advice：invoke方法，即增强逻辑；

2. ProxyTransactionManagementConfiguration是配置类：一般是提供加了@Bean注解的方法，用于注册bean；<font color=red>共注册了3个bean</font>：
   1. BeanFactoryTransactionAttributeSourceAdvisor；

   2. TransactionAttributeSource； 

   3. TransactionInterceptor ；


```java
@Configuration(proxyBeanMethods = false)
@Role(2)
@ImportRuntimeHints({TransactionRuntimeHints.class})
public class ProxyTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {
    
  @Bean(name={"org.springframework.transaction.config.internalTransactionAdvisor"})
    @Role(2)
    public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(TransactionAttributeSource transactionAttributeSource, TransactionInterceptor transactionInterceptor) {
        BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
        advisor.setTransactionAttributeSource(transactionAttributeSource);
        advisor.setAdvice(transactionInterceptor);
        if (this.enableTx != null) {
            advisor.setOrder((Integer)this.enableTx.getNumber("order"));
        }

        return advisor;
    }

    @Bean
    @Role(2)
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource(false);
    }

    @Bean
    @Role(2)
    public TransactionInterceptor transactionInterceptor(TransactionAttributeSource transactionAttributeSource) {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionAttributeSource(transactionAttributeSource);
        if (this.txManager != null) {
            interceptor.setTransactionManager(this.txManager);
        }

        return interceptor;
    }
}
```

<br>

### 【ProxyTransactionManagementConfiguration注册的3个bean】第1个bean：BeanFactoryTransactionAttributeSourceAdvisor-定义一个aop切面

1. <font color=red>BeanFactoryTransactionAttributeSourceAdvisor：spring事务最重要的类，它是一个切面类</font>，基于aop增量@Transactional注解方法，实现事务管理；
2. <font color=red>Advisor切面类补充</font>：spring-aop代理：增强bean（传统aop）所需做的工作
   1. 定义一个切面-Advisor，是一个类；（<font color=red> Advisor可以翻译为切面顾问或通知顾问 </font>）
   2. 定义一个切点 pointCut；
   3. 定义一个连接点JointPoint（描述需要被增强的规则，即哪些类哪些方法需要被增强）， 属于切点的一条记录，即多个连接点组成一个切点； 
   4. 通知-Advice：invoke方法，即增强逻辑；

3. BeanFactoryTransactionAttributeSourceAdvisor：创建时有2个参数：
   1. TransactionAttributeSource ： ； 
   2. <font color=red>TransactionInterceptor ： 他就是通知，通知就是增强逻辑（@Transactional注解方法的增强逻辑）</font>；

```java
@Role(2)
public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(TransactionAttributeSource transactionAttributeSource, TransactionInterceptor transactionInterceptor) {
    BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
    advisor.setTransactionAttributeSource(transactionAttributeSource);
    advisor.setAdvice(transactionInterceptor);
    if (this.enableTx != null) {
        advisor.setOrder((Integer)this.enableTx.getNumber("order"));
    }
    return advisor;
}

// TransactionInterceptor定义

```

<br>

4. 执行带有@Transactional注解的方法，先执行TransactionInterceptor.invoke()方法的增强逻辑，然后再执行目标方法；【原因说明】
   1. spring创建bean时，通过JDK动态代理创建；
   2. TransactionInterceptor实现了MethodInterceptor，重写其invoke()方法；
      1. <font color=red>因为 MethodInterceptor 是spring事务通知（即增强逻辑）</font>，详情参见[springBean创建步骤与spring事务增强逻辑分析](https://blog.csdn.net/PacosonSWJTU/article/details/160504545?sharetype=blogdetail&sharerId=160504545&sharerefer=PC&sharesource=PacosonSWJTU&spm=1011.2480.3001.8118)

<br>

### @Transactional注解方法的底层执行流程：

调用@Transactional注解的方法，底层是调用 MethodInterceptor的invoke方法；

```java
public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor, Serializable {
    public TransactionInterceptor() {
    }

    public TransactionInterceptor(TransactionManager ptm, TransactionAttributeSource tas) {
        this.setTransactionManager(ptm);
        this.setTransactionAttributeSource(tas);
    }

    /** @deprecated */
    @Deprecated
    public TransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource tas) {
        this.setTransactionManager(ptm);
        this.setTransactionAttributeSource(tas);
    }

    /** @deprecated */
    @Deprecated
    public TransactionInterceptor(PlatformTransactionManager ptm, Properties attributes) {
        this.setTransactionManager(ptm);
        this.setTransactionAttributes(attributes);
    }

  // 通知， @Transactional注解方法的增强逻辑  
    @Nullable
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
        Method var10001 = invocation.getMethod();
        Objects.requireNonNull(invocation);
      // 调用 TransactionAspectSupport-invokeWithinTransaction(..)方法 
        return this.invokeWithinTransaction(var10001, targetClass, invocation::proceed);
    }
}
```



<br>

---

## 【2.2】spirng事务增强逻辑（如何开启事务，提交事务，回滚事务）















