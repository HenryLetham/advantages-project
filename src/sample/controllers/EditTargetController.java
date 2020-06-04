package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.configs.SessionManager;
import sample.models.GroupModel;
import sample.models.TargetModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EditTargetController extends Controller {

    @FXML
    private DatePicker dateTarget;


    @FXML
    private TextArea descTarget;

    @FXML
    private TextArea errorField;

    @FXML
    private TextField nameTarget;


    @FXML
    private ChoiceBox<String> groupTarget;

    protected int targetId;

    public void setTargetId(int targetId) throws SQLException {
        this.targetId = targetId;

        TargetModel targetModel = new TargetModel();
        GroupModel groupModel = new GroupModel();

        System.out.println(this.targetId);
        ResultSet resultSet = targetModel.getTargetById(targetId);

        resultSet.first();
        nameTarget.setText(resultSet.getString("name"));
        descTarget.setText(resultSet.getString("desciption"));
        dateTarget.setValue(resultSet.getDate("date_reach").toLocalDate());


        SessionManager sessionManager = SessionManager.getInstance();
        int userId = sessionManager.getUserId();

        ObservableList<String> langs = FXCollections.observableArrayList(groupModel.getGroupsNamesList(userId));
        groupTarget.setItems(langs);

        String groupName= groupModel.getNameById(resultSet.getInt("id_group"));

        groupTarget.setValue(groupName);
    }

    @FXML
    public void doEditTarget(ActionEvent event) throws SQLException {
        SessionManager sessionManager = SessionManager.getInstance();
        int userId = sessionManager.getUserId();

        ArrayList<String> error = new ArrayList<String>();
        GroupModel groupModel = new GroupModel();
        TargetModel targetModel = new TargetModel();

        String name = nameTarget.getText().trim();
        String desc = descTarget.getText().trim();
        LocalDate date = dateTarget.getValue();

        String groupName = groupTarget.getValue();
        int idGroup = groupModel.getIdByName(groupName, userId);

        if(name.length() == 0) {
            error.add("Имя не введено/n");
        }

        if(!error.isEmpty()) {
            String fullLog = "";
            for (String err: error) {
                fullLog += err + "\n";
            }
            errorField.setText(fullLog);
        } else {
            targetModel.alterTargetById(targetId, idGroup, name, desc, date);

            errorField.setText("Цель отредактирована");
        }
    }
}
