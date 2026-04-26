package com.tom.tx.srccode.analysis.datasrc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月26日 07:50:00
 */
public class DataSourceMain {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = TomcatJdbcDataSourceStaticFactory.newDataSource();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("insert into user_account_tbl(user_id, user_name, balance) values(?, ?, ?)");) {
            preparedStatement.setObject(1, "tom-202600426");
            preparedStatement.setObject(2, "汤姆-202600426");
            preparedStatement.setObject(3, "123");
            preparedStatement.execute();
        }
    }
}
