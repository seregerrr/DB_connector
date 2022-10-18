package db;

import org.aeonbits.owner.ConfigFactory;
import utils.IDbConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySqlDbExecutor implements IDbExecutor {

    private static Connection connection;
    private static Statement statement;

    @Override
    public ResultSet execute(String sqlRequest, boolean isResult) throws SQLException {

        IDbConfig cfg = ConfigFactory.create(IDbConfig.class);

        if (connection == null) {
            connection = DriverManager.getConnection(cfg.url(), cfg.username(), cfg.password());
            statement = connection.createStatement();
        }

        if (isResult) {
            return statement.executeQuery(sqlRequest);
        }
        statement.execute(sqlRequest);

        return null;
    }


    @Override
    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
