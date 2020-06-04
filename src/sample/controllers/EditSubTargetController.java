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
import sample.models.SubTargetModel;
import sample.models.TargetModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EditSubTargetController extends Controller {
    @FXML
    private DatePicker dateTarget;


    @FXML
    private TextArea descTarget;

    @FXML
    private TextArea errorField;

    @FXML
    private TextField nameTarget;

    protected int targetId;

    public void setTargetId(int targetId) throws SQLException {
        this.targetId = targetId;

        SubTargetModel targetModel = new SubTargetModel();
        GroupModel groupModel = new GroupModel();

        System.out.println(this.targetId);
        ResultSet resultSet = targetModel.getSubtargetById(targetId);

        resultSet.first();
        nameTarget.setText(resultSet.getString("name"));
        descTarget.setText(resultSet.getString("description"));
        dateTarget.setValue(resultSet.getDate("date_reach").toLocalDate());
    }

    @FXML
    public void doEditSubTarget(ActionEvent event) throws SQLException {
        SessionManager sessionManager = SessionManager.getInstance();
        int userId = sessionManager.getUserId();

        ArrayList<String> error = new ArrayList<String>();
        GroupModel groupModel = new GroupModel();
        SubTargetModel targetModel = new SubTargetModel();

        String name = nameTarget.getText().trim();
        String desc = descTarget.getText().trim();
        LocalDate date = dateTarget.getValue();

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
            targetModel.alterSubTargetById(targetId, name, desc, date);

            errorField.setText("ПодЦель отредактирована");
        }
    }
}
