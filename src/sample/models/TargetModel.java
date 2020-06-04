package sample.models;

import java.sql.*;
import java.time.LocalDate;

public class TargetModel {
    public Connection conn;

    public TargetModel() throws SQLException {
        DB db = DB.getInstance();
        conn = db.getConn();
    }



    public ResultSet getTargetsByGroupId(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.targets WHERE id_group = '" + id + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }

    public int alterTargetById(int id, int idGroup, String name, String desc,  LocalDate dateReach) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`targets` SET " +
                "`id_group` = '" + idGroup +"', " +
                "`name` = '" + name + "', " +
                "`desciption` = '" + desc + "', " +
                "`date_reach` = '"+ dateReach +"' " +
                "WHERE `id` = '"+ id +"'";

        return statement.executeUpdate(sql);
    }

    public ResultSet getTargetById(int targetId) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.targets WHERE id = '" + targetId + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }


    public int addTargetByGroupId(int idGroup, String name, String desc, LocalDate dateCreate, LocalDate dateReach,  int isDone) throws SQLException {

        Statement statement = conn.createStatement();

        String sql =  "INSERT INTO `target`.`targets` (`id_group`, `name`, `desciption`, `date_create`, `date_reach`, `is_done`)" +
                " VALUES ('"+ idGroup + "', '" + name + "', '" + desc + "', '" + dateCreate + "', '" + dateReach + "', '" + isDone + "')";

        return statement.executeUpdate(sql);
    }

    public int deleteTargetById(int targetId) throws SQLException {
        SubTargetModel subTargetModel = new SubTargetModel();

        subTargetModel.deliteSubTargetsByTargetId(targetId);

        Statement statement = conn.createStatement();

        String sql =  "DELETE FROM target.targets WHERE id = '" + targetId + "'";

        return statement.executeUpdate(sql);
    }

    public int deleteTargetsByGroupId(int groupId) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "DELETE FROM target.targets WHERE id_group = '" + groupId + "'";

        return statement.executeUpdate(sql);
    }

    public ResultSet getTargetsByDatePeriod(int groupId, LocalDate left, LocalDate right) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.targets WHERE date_reach > '" + left + "' and date_reach < '"+ right + "' and id_group = '" + groupId + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }

    public int setReachById(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`targets` SET " +
                "`is_done` = '" + 1 + "' " +
                "WHERE (`id` = '"+ id +"')";

        return statement.executeUpdate(sql);
    }

    public int setUnreachById(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`targets` SET " +
                "`is_done` = '" + 0 + "' " +
                "WHERE (`id` = '"+ id +"')";

        return statement.executeUpdate(sql);
    }

}
