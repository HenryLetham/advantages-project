package sample.models;

import java.sql.*;
import java.time.LocalDate;

public class SubTargetModel {
    public Connection conn;

    public SubTargetModel() throws SQLException {
        DB db = DB.getInstance();
        conn = db.getConn();
    }

    public ResultSet getSubtargetsByTargetId(int idTarget) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.subtargets WHERE id_target = '" + idTarget + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }

    public ResultSet getSubtargetById(int idTarget) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.subtargets WHERE id = '" + idTarget + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }


    public int alterSubTargetById(int id, String name, String desc,  LocalDate dateReach) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`subtargets` SET " +
                "`name` = '" + name + "', " +
                "`description` = '" + desc + "', " +

                "`date_reach` = '"+ dateReach +"' " +
                "WHERE `id` = '"+ id +"'";

        return statement.executeUpdate(sql);
    }

    public int addSubTargetByTargetId(int idTarget, String name, String desc, LocalDate dateCreate, LocalDate dateReach, int isDone) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "INSERT INTO `target`.`subtargets` (`id_target`, `name`, `description`, `date_create`, `date_reach`, `is_done`)" +
                " VALUES ('" + idTarget + "', '" + name + "', '" + desc + "', '" + dateCreate + "', '" + dateReach + "', '" + isDone + "')";

        return statement.executeUpdate(sql);
    }

    public int deleteSubTarget(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "DELETE FROM target.subtargets WHERE id = '" + id + "'";

        return statement.executeUpdate(sql);
    }

    public int deliteSubTargetsByTargetId(int targetId) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "DELETE FROM target.subtargets WHERE id_target = '" + targetId + "'";

        return statement.executeUpdate(sql);
    }

    public int setReachById(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`subtargets` SET " +
                "`is_done` = '" + 1 + "' " +
                "WHERE (`id` = '"+ id +"')";

        return statement.executeUpdate(sql);
    }

    public int setUnreachById(int id) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "UPDATE `target`.`subtargets` SET " +
                "`is_done` = '" + 0 + "' " +
                "WHERE (`id` = '"+ id +"')";

        return statement.executeUpdate(sql);
    }
}
