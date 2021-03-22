package cn.LJW.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    private static Connection connection;

    private JDBCUtils(){

    }

    public static Connection getConnection(String url, String driverClassName, String username, String password) {
        if(connection!=null){
            return connection;
        }
        try {
            Class.forName(driverClassName);
            connection= DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return connection;
    }

    public static void closeStream(AutoCloseable ...autoCloseables){
        for (AutoCloseable autoCloseable : autoCloseables) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
