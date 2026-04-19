package com.tom.springboot.tx.note.appilcation.user.service;

import com.tom.springboot.tx.note.appilcation.user.dto.DataSourceConnNumDTO;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年01月08日 07:03:00
 */
@Service
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
}
