package com.tom.springboot.tx.note.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月19日 10:37:00
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setName("tomcatDataSource");
        poolProperties.setUrl("jdbc:mysql://localhost:3306/myspring?useUnicode=true&rewriteBatchedStatement=true");
        poolProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        poolProperties.setUsername("root");
        poolProperties.setPassword("rootroot");
        poolProperties.setJmxEnabled(true);
        poolProperties.setTestWhileIdle(false);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setTestOnReturn(false);
        poolProperties.setValidationInterval(30000);
        poolProperties.setTimeBetweenEvictionRunsMillis(30000);
        poolProperties.setMaxActive(100);
        poolProperties.setInitialSize(10);
        poolProperties.setMaxWait(10000);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setMinEvictableIdleTimeMillis(30000);
        poolProperties.setMinIdle(10);
        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

        DataSource datasource = new DataSource();
        datasource.setPoolProperties(poolProperties);
        return datasource;
    }
}
