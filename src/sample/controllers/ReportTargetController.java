package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.configs.SessionManager;
import sample.models.GroupModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportTargetController extends Controller {
    @FXML
    private PieChart chartTarget;

    public void initialize() throws SQLException {
        showMenu();

        GroupModel groupModel = new GroupModel();
        ResultSet resultSet = groupModel.getGroupNamesAndCntDoneTargets();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int cnt = resultSet.getInt("cnt");

            PieChart.Data slice = new PieChart.Data(name, cnt);

            chartTarget.getData().add(slice);
        }
    }
}
