package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.configs.PasswordAuthentication;
import sample.configs.SessionManager;
import sample.models.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController extends Controller {
    @FXML
    private TextField password;

    @FXML
    private TextArea errorFields;

    @FXML
    private TextField email;


    @FXML
    void doLogin(ActionEvent event) throws SQLException, IOException {
        ArrayList<String> error = new ArrayList<String>();

        String pass = password.getText().trim();
        String login = email.getText().trim();

        UserModel userModel = new UserModel();

        String token = "";

        if(userModel.getUserIdByLogin(login) != 0) {
            token = userModel.getUserTokenByLogin(login);
        } else {
            errorFields.setText("Нет пользователя с таким логином");
            return;
        }

        PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

        if(passwordAuthentication.authenticate(pass, token)) {
            errorFields.setText("Вы авторизованны");
            SessionManager sessionManager = SessionManager.getInstance();

            sessionManager.login("Alex", userModel.getUserIdByLogin(login));
            goTargetsPage(event);
        } else {
            errorFields.setText("Неверный логин или пароль");
        }
    }
}
