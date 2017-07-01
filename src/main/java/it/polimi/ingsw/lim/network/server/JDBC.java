package it.polimi.ingsw.lim.network.server;

import it.polimi.ingsw.lim.Log;

import java.sql.*;

/**
 *
 */
public class JDBC {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";

    private static final String USER = "root";
    private static final String PASS = "root";

    private Connection connection;
    private Statement statement;
    private String DB_URL = "jdbc:sqlite:USERS.db/";

    public JDBC () throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL);
        this.statement = this.connection.createStatement();
        this.createTable();
    }

    public void createTable() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
            this.statement = this.connection.createStatement();
            String sql = "CREATE TABLE REGISTRATION ".concat("(userName VARCHAR(255) not NULL, ").concat(" password VARCHAR(255) not NULL, ").concat(" victory INT DEFAULT 0,").concat(" PRIMARY KEY ( userName ))");
            this.statement.executeUpdate(sql);
            Log.getLog().info("table created");
        }
        catch (SQLException e){
            e.printStackTrace();
            Log.getLog().info("table already exists");
        }
    }

    public void insertRecord(String userName, String password) throws SQLException{
        String sql = "INSERT INTO REGISTRATION VALUES ('".concat(userName).concat("', '").concat(password.concat("', 0)"));
        this.statement.executeUpdate(sql);
        Log.getLog().info("user: ".concat(userName).concat(" added"));
    }

    public boolean isUserContained(String userName, String password) throws SQLException{
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

    public boolean isAlreadySelectedUserName (String userName) throws SQLException{
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

    public static void main (String args[]){
        try {
            JDBC jdbc = new JDBC();
            jdbc.createTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}