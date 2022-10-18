import db.IDbExecutor;
import db.MySqlDbExecutor;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.IDbConfig;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static utils.PrettyTabtePrint.processResultSet;
import static utils.SqlMethods.*;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);


    public static void main(String... args) throws Exception {

//        1. Создать таблицу Student (Колонки id, fio, sex, id_group)
        createTable("Student", "id INT, fio VARCHAR(30), sex VARCHAR(1), id_group INT");
//        2. Создать таблицу Group (Колонки id, name, id_curator)
        createTable("Study_group", "id INT, name VARCHAR(10), id_curator INT");
//        3. Создать таблицу Curator (Колонки id, fio)
        createTable("Curator", "id INT, fio VARCHAR(30)");

//        4. Заполнить таблицы данными (15 студентов, 3 группы, 4 куратора)
        insertToTable("Student",
                "(001, 'Amanda Chapman', 'f', 101)," +
                        " (002, 'Douglas Romero', 'm', 103)," +
                        " (003, 'Nathan Garcia', 'm', 102)," +
                        " (004, 'Christina Thomas', 'f', 101)," +
                        " (005, 'Melvin Taylor', 'm', 102)," +
                        " (006, 'Penny Smith', 'f', 103)," +
                        " (007, 'Robert Rice', 'm', 101)," +
                        " (008, 'Rebecca Rodriguez', 'f', 102)," +
                        " (009, 'Joyce Reid', 'f', 103)," +
                        " (010, 'Kenneth Cummings', 'm', 102)," +
                        " (011, 'William Stokes', 'm', 101)," +
                        " (012, 'Sarah Simmons', 'm', 102)," +
                        " (013, 'Tammy Tate', 'f', 103)," +
                        " (014, 'William Hopkins', 'm', 101)," +
                        " (015, 'Holly Watson', 'f', 103)");

        insertToTable("Study_group",
                "(101, 'group_1', 201)," +
                        " (102, 'group_2', 202)," +
                        " (103, 'group_3', 203)");

        insertToTable("Curator",
                "(201, 'Joe Murphy')," +
                        " (202, 'Joe Murphy')," +
                        " (203, 'Penny Watson')," +
                        " (204, 'William Thomas')");


//      5. Вывести на экран информацию о всех студентах включая название группы и имя куратора
        ResultSet result = selectQuery("SELECT s.fio, s.sex, g.name, c.fio" +
                " FROM Student s" +
                " LEFT JOIN Study_group g ON s.id_group = g.id" +
                " LEFT JOIN Curator c ON g.id_curator = c.id;");
        logger.info("Все студенты включая название группы и имя куратора:");
        System.out.println(processResultSet(result));

//      6. Вывести на экран количество студентов
        ResultSet result2 = selectQuery("SELECT COUNT(*) FROM Student;");
        while (result2.next()) {
            logger.info(String.format("Количество студентов: %s", result2.getString(1)));
        }

//        7. Вывести студенток
        ResultSet result3 = selectQuery("SELECT * FROM Student WHERE sex = 'f';");
        logger.info("Студентки:");
        System.out.println(processResultSet(result3));

//        8. Обновить данные по группе сменив куратора
        IDbExecutor iDbExecutor = new MySqlDbExecutor();
        iDbExecutor.execute("UPDATE Study_group SET id_curator = 203 WHERE id_curator = 201;", false);

//        9. Вывести список групп с их кураторами
        ResultSet result4 = selectQuery("SELECT s.name, c.fio" +
                " FROM Study_group s" +
                " LEFT JOIN Curator c ON s.id_curator = c.id;");
        logger.info("Список групп с их кураторами:");
        System.out.println(processResultSet(result4));

//        10. Вывести на экран студентов из определенной группы(поиск по имени группы)
        ResultSet result5 = selectQuery("SELECT fio FROM Student" +
                " WHERE id_group IN" +
                " (SELECT id FROM Study_group WHERE name = 'group_1');");
        logger.info("Студенты из определенной группы:");
        System.out.println(processResultSet(result5));

    }
}
