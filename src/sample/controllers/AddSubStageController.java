package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.models.SubTargetModel;
import sample.models.TargetModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddSubStageController extends Controller {
    private int targetId = -1;

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    @FXML
    private TextField nameSubTarget;

    @FXML
    private TextArea descSubTarget;

    @FXML
    private TextArea errorField;

    @FXML
    private DatePicker dateSubTarget;


    @FXML
    void addSubTarget(ActionEvent event) throws SQLException {
        System.out.println();
        ArrayList<String> error = new ArrayList<String>();
        SubTargetModel subTargetModelTargetModel = new SubTargetModel();

        String name = nameSubTarget.getText().trim();
        String desc = descSubTarget.getText().trim();
        LocalDate date = dateSubTarget.getValue();

        if(name.length() == 0) {
            error.add("Имя не введено/n");
        }

        if(!error.isEmpty()) {
            String fullLog = "";
            for (String err: error) {
                fullLog += err;
            }
            errorField.setText(fullLog);
        } else {
            LocalDate dateCreate = new Date().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            subTargetModelTargetModel.addSubTargetByTargetId(targetId, name, desc, dateCreate, date, 0);

            errorField.setText("Подцель добавлена");
        }

    }
}
