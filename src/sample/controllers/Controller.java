package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.configs.SessionManager;
import sample.models.GroupModel;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    @FXML
    public Button pageTargets;

    @FXML
    public Button pageChart;
    @FXML
    public Button pageLogin;
    @FXML
    public Button pageRegist;

    @FXML
    private Button logoutBtn;

    private SessionManager sessionManager;

    protected Stage myStage;

    protected void showMenu() {
        SessionManager sessionManager = SessionManager.getInstance();

        if(sessionManager.isLogin()) {
            logoutBtn.setVisible(true);
            pageLogin.setVisible(false);
            pageRegist.setVisible(false);
            pageChart.setVisible(true);
            pageTargets.setVisible(true);
        }
        else {
            logoutBtn.setVisible(false);
            pageLogin.setVisible(true);
            pageRegist.setVisible(true);
            pageChart.setVisible(false);
            pageTargets.setVisible(false);
        }
    }


    public void initialize() throws SQLException {
        showMenu();
    }

    public void setStage(Stage mStage) {
        this.myStage = mStage;
    }

    public void goLogout(ActionEvent actionEvent) throws IOException {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/loginn.fxml"));
        Parent login = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(login);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }

    public void goRegistPage(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/regist.fxml"));
        Parent regist = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(regist);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }

    public void goLoginPage(ActionEvent actionEvent) throws IOException {

        sessionManager = SessionManager.getInstance();
        if(sessionManager.isLogin()) {
            System.out.println("Вы уже авторизованы");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/loginn.fxml"));
        Parent login = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(login);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }

    public void goTargetsPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/targets.fxml"));
        Parent targets = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(targets);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }

    public void goChartPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/report.fxml"));
        Parent report = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(report);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }
}
