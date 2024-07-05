package ru.clevertec.check.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The class to connect to the database
 */
public class DBUtils {
    /** URL of database*/
    private final String dbURL;
    /** the user's name of database*/
    private final String dbUserName;
    /** the user's password of database */
    private final String dbPassword;


    public DBUtils(String dbURL,String dbUserName,String dbPassword) {
        this.dbURL=dbURL;
        this.dbUserName=dbUserName;
        this.dbPassword=dbPassword;
    }

    /**
     * The method of connection to the database
     * @return returns the object of class Connection
     */
    public Connection getConnection () {
        Connection connection=null;
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Тут не работает");
            }
            connection= DriverManager.getConnection(dbURL,dbUserName,dbPassword);
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так....");
        }
        return connection;
    }
}
