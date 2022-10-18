package utils;

import com.sun.tools.javac.Main;
import db.IDbExecutor;
import db.MySqlDbExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlMethods {

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void dropTable(String table_name) throws SQLException {
        IDbExecutor iDbExecutor = new MySqlDbExecutor();
        try {
            iDbExecutor.execute(String.format("DROP TABLE IF EXISTS %s;", table_name), false);
        } finally {
            iDbExecutor.close();
        }
    }


    public static void createTable(String table_name, String columns) throws SQLException {
        dropTable(table_name);

        IDbExecutor iDbExecutor = new MySqlDbExecutor();
        try {
            StringBuilder query = new StringBuilder("CREATE TABLE ");
            query.append(table_name);
            query.append(" (");
            query.append(columns);
            query.append(");");

            logger.info(query);
            iDbExecutor.execute(String.valueOf(query), false);
            logger.info(String.format("Создана таблица %s", table_name));
        } finally {
            iDbExecutor.close();
        }
    }


    public static void insertToTable(String table_name, String data) throws SQLException {
        IDbExecutor iDbExecutor = new MySqlDbExecutor();

        try {
            iDbExecutor.execute(String.format("INSERT INTO %s values %s;", table_name, data), false);
        } finally {
            iDbExecutor.close();
        }
    }


    public static ResultSet selectQuery(String query) throws SQLException {
        IDbExecutor iDbExecutor = new MySqlDbExecutor();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet cachedRowSet = factory.createCachedRowSet();

        try {
            ResultSet result = iDbExecutor.execute(query, true);
            cachedRowSet.populate(result);
            return cachedRowSet;
        } finally {
            iDbExecutor.close();
        }
    }
}
