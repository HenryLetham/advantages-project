package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.configs.SessionManager;
import sample.models.GroupModel;

import java.sql.SQLException;

public class AddGroupCоntroller extends Controller {
    @FXML
    private TextField nameGroup;

    @FXML
    private TextArea errorField;

    @FXML
    void addGroup(ActionEvent event) throws SQLException {
        if (nameGroup.getText().trim().length() != 0) {
            SessionManager sessionManager = SessionManager.getInstance();
            int userId = sessionManager.getUserId();

            GroupModel groupModel = new GroupModel();
            groupModel.addGroup(nameGroup.getText().trim(), userId);

            errorField.setText("Группа добавлена");
        } else
        {
            errorField.setText("Введите название группы, чтобы её добавить");
        }
    }
}
