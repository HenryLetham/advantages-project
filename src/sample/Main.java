package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.configs.SessionManager;
import sample.controllers.Controller;
import sample.models.GroupModel;
import sample.models.TargetModel;

import java.sql.ResultSet;
import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/loginn.fxml"));
        Parent login = loader.load();
        Scene scene = new Scene(login);

        stage.setScene(scene);
        Controller controller = loader.getController();
        controller.setStage(stage);

        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}


