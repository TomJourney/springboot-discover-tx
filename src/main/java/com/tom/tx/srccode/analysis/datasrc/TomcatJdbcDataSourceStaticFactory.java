package com.tom.tx.srccode.analysis.datasrc;

import javax.sql.DataSource;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月26日 08:04:00
 */
public class TomcatJdbcDataSourceStaticFactory {

    public static DataSource newDataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        // 基本配置
        dataSource.setUrl("jdbc:mysql://localhost:3306/myspring");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 专属配置
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(2);
        dataSource.setMaxIdle(2);
        dataSource.setMinIdle(2);
        dataSource.setMaxWait(10000);

        // 连接探测
        // 一条sql语句，用来验证数据库连接是否正常。这条语句必须是一个查询模式，并至少返回一条数据。可以为任何可以验证数据库连接是否正常的sql-
        dataSource.setValidationQuery("select 1");
        // 当从连接池取连接时，验证这个连接是否有效
        dataSource.setTestOnBorrow(true);
        // 空闲时测试连接，必须配置validationQuery才有效
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(2000000);
        // 连接的超时时间
        dataSource.setMinEvictableIdleTimeMillis(60000);

        // 移除
        // 是否自动回收超时连接
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(60);
        dataSource.setLogAbandoned(true);
        return dataSource;
    }
}
