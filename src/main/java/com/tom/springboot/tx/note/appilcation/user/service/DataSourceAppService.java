package com.tom.springboot.tx.note.appilcation.user.service;

import com.tom.springboot.tx.note.appilcation.user.dto.DataSourceConnNumDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年01月08日 07:03:00
 */
@Service
@Slf4j
public class DataSourceAppService {

    @Autowired
    private DataSource dataSource;

    public DataSourceConnNumDTO qryDataSourceIdleNum() {
        return DataSourceConnNumDTO.build(
                dataSource.getIdle(),
                dataSource.getNumIdle(),
                dataSource.getActive(),
                dataSource.getNumActive()
        );
    }

    public boolean probe() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select version()");
            if (rs.next()) {
                log.info(rs.getString(1));
            }
            return true;
        } catch (Exception e) {
            log.error("数据源探测异常", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("关闭数据库链接异常", e);
                }
            }
        }
        //实际使用中一般是在应用启动时初始化数据源，应用从数据源中获取连接；并不会关闭数据源。
//        datasource.close();
        return false;
    }
}
