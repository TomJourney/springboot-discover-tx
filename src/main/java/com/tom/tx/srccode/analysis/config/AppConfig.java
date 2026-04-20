package com.tom.tx.srccode.analysis.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;

import javax.sql.DataSource;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月19日 09:55:00
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.tom.tx.srccode.analysis")
@MapperScan("com/tom/tx/srccode/analysis/mapper/**/*.mapper.xml")
public class AppConfig {

    @Bean
    public DataSource jdbcDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/myspring?useUnicode=true&rewriteBatchedStatement=true");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
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

    @Transactional()
    public void f1() {
        TransactionAttribute transactionAttribute;
        TransactionStatus transactionStatus;
        TransactionAspectSupport transactionAspectSupport; //
        DataSourceTransactionManager dataSourceTransactionManager;
    }
}
