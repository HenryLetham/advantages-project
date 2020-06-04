package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.configs.PasswordAuthentication;
import sample.configs.SessionManager;
import sample.models.UserModel;

import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;


import java.sql.SQLException;
import java.util.ArrayList;

public class RegistController extends Controller {
    @FXML
    private TextField userName;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private TextArea errorField;

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @FXML
    void addUser(ActionEvent event) throws SQLException, IOException {
        ArrayList<String> error = new ArrayList<String>();

        String usName = userName.getText().trim();
        String login = email.getText().trim();
        String pass = password.getText().trim();

        if(usName.length() == 0) {
            error.add("Не введено имя пользователя");
        }

        if(!isValidEmailAddress(login)) {
            error.add("Не верно введён email");
        }

        PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
        String token = passwordAuthentication.hash(pass);

        if(!error.isEmpty()) {
            String fullLog = "";
            for (String err: error) {
                fullLog += err;
            }
            errorField.setText(fullLog);
        } else {
            UserModel userModel = new UserModel();

            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.login(usName, userModel.addUser(usName, login, token));

            errorField.setText("Пользователь добавлен");

            goTargetsPage(event);
        }
    }
}
