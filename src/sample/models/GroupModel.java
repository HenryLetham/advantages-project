package sample.models;

import sample.configs.SessionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GroupModel {
    public Connection conn;

    public GroupModel() throws SQLException {
        DB db = DB.getInstance();
        conn = db.getConn();
    }

    public int addGroup(String name, int userId) throws SQLException {
        Statement statement = conn.createStatement();
        String sql =  "INSERT INTO `target`.`groups` (`name`, `id_user`) VALUES " +
                "('"+ name + "', '" + userId  + "')";

        return statement.executeUpdate(sql);
    }

    public int deleteGroupById(int id) throws SQLException {
        TargetModel targetModel = new TargetModel();
        targetModel.deleteTargetsByGroupId(id);

        Statement statement = conn.createStatement();

        String sql =  "DELETE FROM target.groups WHERE id = '" + id + "'";

        return statement.executeUpdate(sql);
    }

    public ResultSet getGroupNamesAndCntDoneTargets() throws SQLException {
        Statement statement = conn.createStatement();

        String sql = "SELECT `groups`.`name`, count(`targets`.`name`) as 'cnt' FROM `groups`, `targets` WHERE `groups`.`id` = `targets`.id_group and `targets`.is_done = 1 group by `groups`.`name`";



        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }

    public ArrayList<String> getGroupsNamesList(int userId) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT name FROM target.groups WHERE `id_user` = '"+ userId +"'";

        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<String> names = new ArrayList<>();

        while (resultSet.next()) {
            names.add(resultSet.getString("name"));
        }

        return names;
    }

    public ResultSet getGroupsList(int userId) throws SQLException {
        Statement statement = conn.createStatement();

        String sql =  "SELECT * FROM target.groups WHERE `id_user` = '"+ userId +"'";

        ResultSet resultSet = statement.executeQuery(sql);

        return resultSet;
    }

    public String getNameById(int groupId) throws SQLException {
        Statement statement = conn.createStatement();
        String sql =  "SELECT name FROM target.groups WHERE `id` = '"+ groupId +"'";

        ResultSet resultSet = statement.executeQuery(sql);

        resultSet.first();

        return resultSet.getString("name");
    }

    public int getIdByName(String name, int userId) throws SQLException {

        Statement statement = conn.createStatement();

        String sql =  "SELECT id FROM target.groups WHERE name = '" + name + "' and `id_user` = '" + userId + "'";

        ResultSet resultSet = statement.executeQuery(sql);

        if(resultSet.first()) {
            int id = resultSet.getInt("id");
            return id;
        } else {
            return 0;
        }
    }
}
