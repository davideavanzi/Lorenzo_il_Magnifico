package it.polimi.ingsw.lim.network.server;

import it.polimi.ingsw.lim.Log;

import java.sql.*;

/**
 *
 */
public class JDBC {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final String USER = "root";
    private static final String PASS = "root";

    private Connection connection;
    private Statement statement;
    private String DB_URL = "jdbc:mysql://127.0.0.1:3306/";

    public JDBC () throws SQLException, ClassNotFoundException{
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        this.statement = this.connection.createStatement();
    }

    public void createDataBase() {
        try {
            String sql = "CREATE DATABASE USERS";
            this.statement.executeUpdate(sql);
            DB_URL = DB_URL.concat("USERS");
        }
        catch (SQLException e){
            DB_URL = DB_URL.concat("USERS");
            Log.getLog().info("databse already exists");
        }
    }

    public void createTable() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
            this.statement = this.connection.createStatement();
            String sql = "CREATE TABLE REGISTRATION ".concat("(userName VARCHAR(255) not NULL, ").concat(" password VARCHAR(255) not NULL, ").concat(" PRIMARY KEY ( userName ))");
            this.statement.executeUpdate(sql);
        }
        catch (SQLException e){
            Log.getLog().info("table already exists");
        }
    }

    public void insertRecord(String userName, String password) throws SQLException{
        String sql = "INSERT INTO REGISTRATION VALUES ('".concat(userName).concat("', '").concat(password.concat("')"));
        this.statement.executeUpdate(sql);
        Log.getLog().info("user: ".concat(userName).concat(" added"));
    }

    public boolean isContain (String userName, String password) throws SQLException{
        String sql = "SELECT userName, password FROM REGISTRATION";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            if (resultSet.getString("userName").equals(userName)){
                Log.getLog().info("user: ".concat(userName).concat(" exist"));
                if (resultSet.getString("password").equals(password)){
                    Log.getLog().info("password correct");
                    return true;
                }
                Log.getLog().info("password wrong");
            }
        }
        resultSet.close();
        Log.getLog().info("user: ".concat(userName).concat(" not exist"));
        return false;
    }

    public boolean isAlreadySelected(String userName) throws SQLException{
        String sql = "SELECT userName, password FROM Registration";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            if (resultSet.getString("userName").equals(userName)){
                Log.getLog().info("user: ".concat(userName).concat(" already exists"));
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    public void deleteUser(String userName) throws SQLException{
        String sql = "DELETE FROM REGISTRATION ".concat("WHERE userName = '").concat(userName).concat("'");
        statement.executeUpdate(sql);
        Log.getLog().info("user: ".concat(userName).concat(" deleted"));
    }

    private void dropDataBase() throws SQLException{
        String sql = "DROP DATABASE USERS";
        statement.executeUpdate(sql);
    }
}