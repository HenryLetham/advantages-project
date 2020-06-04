package sample.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserModel {
    public Connection conn;

    public UserModel() throws SQLException {
        DB db = DB.getInstance();
        conn = db.getConn();
    }

    public int addUser(String name,  String email, String token) throws SQLException {

        Statement statement = conn.createStatement();
        String sql =  "INSERT INTO `target`.`users` (`name`, `email`, `token`) VALUES " +
                "('"+ name + "', '"+ email + "', '" + token +"')";

        statement.executeUpdate(sql);

        return getUserIdByLogin(email);

    }

    public int getUserIdByLogin(String email) throws SQLException {
        Statement statement = conn.createStatement();
        String sql = "SELECT `id` FROM target.users WHERE email='" + email + "'";
        statement.executeQuery(sql);

        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.first()) {
            return resultSet.getInt("id");
        } else {
            return 0;
        }
    }

    public String getUserTokenByLogin(String email) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT `token` FROM target.users WHERE email='" + email + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.first()) {
            String token = resultSet.getString("token");
            return token;
        } else {
            return "Нет такого токена";
        }
    }
}
