package it.polimi.ingsw.lim.network.server;

import java.sql.*;

/**
 *
 */
public class JDBC {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String USER = "root";
    static final String PASS = "root";

    private Connection connection;
    private Statement statement;
    String DB_URL = "jdbc:mysql://127.0.0.1:3306/";

    public JDBC () throws SQLException, ClassNotFoundException{
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        this.statement = this.connection.createStatement();
    }

    public void createDataBase() throws SQLException, ClassNotFoundException{
        String sql = "CREATE DATABASE USERS";
        this.statement.executeUpdate(sql);
        DB_URL = "jdbc:mysql://127.0.0.1:3306/".concat("USERS");
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        this.statement = this.connection.createStatement();
    }

    public void createTable() throws SQLException{
        String sql = "CREATE TABLE REGISTRATION ".concat("(userName VARCHAR(255) not NULL, ").concat(" password VARCHAR(255) not NULL, ").concat(" PRIMARY KEY ( userName ))");
        this.statement.executeUpdate(sql);
    }

    public void insertRecord(String userName, String password) throws SQLException{
        String sql = "INSERT INTO REGISTRATION VALUES ('".concat(userName).concat("', '").concat(password.concat("')"));
        this.statement.executeUpdate(sql);
    }

    public boolean isContain (String userName, String password) throws SQLException{
        String sql = "SELECT userName, password FROM REGISTRATION";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            if (resultSet.getString("userName").equals(userName)){
                if (resultSet.getString("password").equals(password)){
                    return true;
                }
            }
        }
        resultSet.close();
        return false;
    }

    public boolean isAlreadySelected(String userName) throws SQLException{
        String sql = "SELECT userName, password FROM Registration";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            if (resultSet.getString("userName").equals(userName)){
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    public void deleteUser(String userName) throws SQLException{
        String sql = "DELETE FROM Registration ".concat("WHERE userName = '").concat(userName).concat("'");
        statement.executeUpdate(sql);
    }

    private void dropDataBase() throws  SQLException{
        String sql = "DROP DATABASE USERS";
        statement.executeUpdate(sql);
    }

    public static void main(String args[]){
        try {
            JDBC jdbc = new JDBC();
            jdbc.createDataBase();
            jdbc.createTable();
            jdbc.insertRecord("user", "pass");
            System.out.println(jdbc.isContain("user", "pass"));
            System.out.println(jdbc.isContain("asd", "zxc"));
            jdbc.dropDataBase();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
