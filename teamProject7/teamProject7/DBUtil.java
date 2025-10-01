//DBUtil.java: 현재 자바 파일과 SQLite를 연결시켜주는 역할을 수행

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:User.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}