package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.configs.SessionManager;
import sample.models.GroupModel;
import sample.models.TargetModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddTargetController extends Controller {
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

    @FXML
    void addtarget(ActionEvent event) throws SQLException {
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

            LocalDate dateCreate = new Date().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();

            targetModel.addTargetByGroupId( idGroup, name, desc, dateCreate, date, 0);

            errorField.setText("Цель добавлена");
        }
    }

    public void initialize() throws SQLException {
        showMenu();

        SessionManager sessionManager = SessionManager.getInstance();
        int userId = sessionManager.getUserId();

        GroupModel groupModel = new GroupModel();

        ObservableList<String> langs = FXCollections.observableArrayList(groupModel.getGroupsNamesList(userId));
        groupTarget.setItems(langs);
    }



}
