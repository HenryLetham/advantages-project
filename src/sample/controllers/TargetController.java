package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import sample.configs.SessionManager;
import sample.models.GroupModel;
import sample.models.SubTargetModel;
import sample.models.TargetModel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TargetController extends Controller {

    @FXML
    private VBox unreachTargetWrapp;

    @FXML
    private VBox reachTargetWrapp;

    @FXML
    private VBox wrappGroups;

    @FXML
    private DatePicker dateEnd;

    @FXML
    private DatePicker dateStart;

    @FXML
    private TextArea errorFields;

    public int groupId = -1;

    public void showTargetsByPeriod() throws SQLException {

        if(groupId == -1) {
            errorFields.setText("Сначала выберите группу");
            return;
        }

        LocalDate left = dateStart.getValue();
        LocalDate right = dateEnd.getValue();

        if(left == null || right == null) {
            errorFields.setText("Один из периодов не задан");
            return;
        }

        TargetModel targetModel = new TargetModel();
        ResultSet resultSet = targetModel.getTargetsByDatePeriod(groupId, left,right);

        if (!resultSet.isBeforeFirst() ) {
            errorFields.setText("Нет данных за этот период");
            return;
        }

        reachTargetWrapp.getChildren().removeAll(reachTargetWrapp.getChildren());
        unreachTargetWrapp.getChildren().removeAll(unreachTargetWrapp.getChildren());

        createShowTargetsTree(resultSet);

        errorFields.setText("Данные выведены");

    }

    public void showGroups() throws SQLException {
        SessionManager sessionManager = SessionManager.getInstance();
        int userId = sessionManager.getUserId();

        GroupModel groupModel = new GroupModel();
        ResultSet resultSet = groupModel.getGroupsList(userId);

        while (resultSet.next()) {

            Button button = new Button(resultSet.getString("name"));
            button.setUserData(resultSet.getInt("id"));

            button.setOnAction(
                    event -> {
                        int groupId = (int) button.getUserData();

                        try {
                            showTargetsByGroupId(groupId);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
            );

            Button remove = new Button("Удалить");
            remove.setUserData(resultSet.getInt("id"));

            remove.setOnAction(
                    event -> {
                        int groupId = (int) button.getUserData();

                        try {
                            removeGroup(groupId);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
            );

            HBox wrappGroup = new HBox(button, remove);

            wrappGroups.getChildren().add(wrappGroup);
        }
    }

    public void initialize() throws SQLException {
        showMenu();

        showGroups();
    }

    public void removeGroup(int groupId) throws SQLException  {

        GroupModel groupModel = new GroupModel();
        groupModel.deleteGroupById(groupId);

        wrappGroups.getChildren().removeAll(wrappGroups.getChildren());

        showGroups();
    }

    public void createShowTargetsTree(ResultSet resultSet) throws SQLException {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);

        int counter = 1;

        SubTargetModel subTargetModel = new SubTargetModel();

        while (resultSet.next()) {

            Label numTarget = new Label("Цель");

            GridPane gridPane = new GridPane();
            gridPane.getColumnConstraints().addAll(col1, col2, col2);
            gridPane.getRowConstraints().addAll(row1, row2);

            TextArea titleTarget = new TextArea(resultSet.getString("name"));
            titleTarget.setEditable(false);

            Date dateReach = resultSet.getDate("date_reach");
            Date dateCurr = new Date();

            long milliseconds = dateReach.getTime() - dateCurr.getTime();
            int daysDiff = (int) (milliseconds / (24 * 60 * 60 * 1000));

            TextArea descTarget = new TextArea(resultSet.getString("desciption"));
            descTarget.setEditable(false);

            Button delite = new Button("Удалить");
            delite.setUserData(resultSet.getInt("id"));


            delite.setOnAction(
                    event -> {
                        Scene scene = delite.getScene();
                        VBox vBox = (VBox) scene.lookup("#wrappTaget" + delite.getUserData());
                        try {
                            removeTarget((Integer) delite.getUserData());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        VBox vBox2 = (VBox) vBox.getParent();
                        vBox2.getChildren().remove(vBox);
                    }
            );

            Button edit = new Button("Редактировать");
            edit.setUserData(resultSet.getInt("id"));
            edit.setOnAction(
                    event -> {
                        try {
                            editTarget((Integer) edit.getUserData());
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                        }
                    }
            );

            Button addSub = new Button("Добавить подцель");
            addSub.setUserData(resultSet.getInt("id"));
            addSub.setOnAction(
                    event -> {
                        try {
                            addSubTarget((int)addSub.getUserData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );

            TextArea datesTarget = new TextArea();
            datesTarget.setEditable(false);

            Button doReach = new Button();
            if(!resultSet.getBoolean("is_done")) {
                datesTarget.setText( "Дата завершения: " + dateReach + "\n" +
                        "Осталось дней до завершения " + daysDiff);

                doReach.setText("Цель выполнена");
                doReach.setUserData(resultSet.getInt("id"));
                doReach.setOnAction(
                        event -> {
                            try {
                                doReach((int)doReach.getUserData());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
            } else {
                datesTarget.setText("Дата завершения: " + dateReach + "\n" + "Цель выполнена");
                doReach.setText("Отменить выполнение");
                doReach.setUserData(resultSet.getInt("id"));
                doReach.setOnAction(
                        event -> {
                            try {
                                doUnReach((int)doReach.getUserData());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }

            VBox btnTargetWrapp = new VBox(doReach, edit, delite, addSub);

            gridPane.add(titleTarget, 0, 0);
            gridPane.add(descTarget, 0,1);
            gridPane.add(datesTarget, 2, 0);
            gridPane.add(btnTargetWrapp,2,1);

            gridPane.setGridLinesVisible(true);

            VBox wrappTarget = new VBox(numTarget, gridPane);
            wrappTarget.setId("wrappTaget" + resultSet.getInt("id"));
            wrappTarget.setStyle("-fx-padding: 0px 0px 50px 0px");

//          ПОДЦЕЛИ
            ResultSet subTargets = subTargetModel.getSubtargetsByTargetId(resultSet.getInt("id"));

            int subCounter = 1;
            while (subTargets.next()) {
                Label numSubTarget = new Label("Подцель #" + subCounter);
                GridPane gridPaneSub = new GridPane();
                gridPaneSub.getColumnConstraints().addAll(col1, col2, col2);
                gridPaneSub.getRowConstraints().addAll(row1, row2);

                TextArea titleSubTarget = new TextArea(subTargets.getString("name"));
                titleSubTarget.setEditable(false);

                Date dateReachSub = subTargets.getDate("date_reach");

                milliseconds = dateReachSub.getTime() - dateCurr.getTime();
                daysDiff = (int) (milliseconds / (24 * 60 * 60 * 1000));

                TextArea descSubTarget = new TextArea(subTargets.getString("description"));
                descSubTarget.setEditable(false);

                Button deliteSub = new Button("Удалить");
                deliteSub.setUserData(subTargets.getInt("id"));
                deliteSub.setOnAction(
                        event -> {
                            Scene scene = deliteSub.getScene();
                            VBox vBox = (VBox) scene.lookup("#wrappSubTaget" + deliteSub.getUserData());

                            try {
                                removeSubTarget((Integer) deliteSub.getUserData());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            VBox vBox2 = (VBox) vBox.getParent();
                            vBox2.getChildren().remove(vBox);
                        }
                );

                Button editSub = new Button("Редактировать");
                editSub.setUserData(subTargets.getInt("id"));
                editSub.setOnAction(
                        event -> {
                            try {
                                editSubTarget((Integer) editSub.getUserData());
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );

                TextArea datesSubTarget = new TextArea();
                datesSubTarget.setEditable(false);

                Button doSubReach = new Button();
                if(!subTargets.getBoolean("is_done")) {
                    datesSubTarget.setText("Дата завершения: " + dateReachSub + "\n" +
                            "Осталось дней до завершения " + daysDiff);

                    doSubReach.setText("Цель выполнена");
                    doSubReach.setUserData(subTargets.getInt("id"));
                    doSubReach.setOnAction(
                            event -> {
                                try {
                                    doSubReach((int)doSubReach.getUserData());
                                } catch (SQLException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                } else {
                    datesSubTarget.setText("Дата завершения: " + dateReachSub + "\n" + "Цель выполнена");

                    doSubReach.setText("Отменить выполнение");
                    doSubReach.setUserData(subTargets.getInt("id"));
                    doSubReach.setOnAction(
                            event -> {
                                try {
                                    doSubUnReach((int)doSubReach.getUserData());
                                } catch (SQLException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                }

                VBox btnSubTargetWrapp = new VBox(doSubReach, editSub, deliteSub);

                gridPaneSub.add(titleSubTarget, 0, 0);
                gridPaneSub.add(descSubTarget, 0,1);
                gridPaneSub.add(datesSubTarget, 2, 0);
                gridPaneSub.add(btnSubTargetWrapp,2,1);

                gridPaneSub.setGridLinesVisible(true);

                VBox wrappSubTarget = new VBox(numSubTarget, gridPaneSub);
                wrappSubTarget.setId("wrappSubTaget" + subTargets.getInt("id"));
                wrappSubTarget.setStyle("-fx-padding: 0px 0px 20px 0px");

                wrappTarget.getChildren().add(wrappSubTarget);

                subCounter++;
            }


            if(resultSet.getBoolean("is_done")) {
                reachTargetWrapp.getChildren().add(wrappTarget);
            } else {
                unreachTargetWrapp.getChildren().add(wrappTarget);
            }

            counter++;
        }
    }

    public void showTargetsByGroupId(int groupId) throws SQLException {
        this.groupId = groupId;

        reachTargetWrapp.getChildren().removeAll(reachTargetWrapp.getChildren());
        unreachTargetWrapp.getChildren().removeAll(unreachTargetWrapp.getChildren());


        TargetModel targetModel = new TargetModel();
        ResultSet resultSet = targetModel.getTargetsByGroupId(groupId);

        createShowTargetsTree(resultSet);
    }

    public void doSubReach(int subTargetId) throws IOException, SQLException{
        SubTargetModel subTargetModel = new SubTargetModel();
        subTargetModel.setReachById(subTargetId);
        showTargetsByGroupId(groupId);
    }

    public void doSubUnReach(int subTargetId) throws IOException, SQLException{
        SubTargetModel subTargetModel = new SubTargetModel();
        subTargetModel.setUnreachById(subTargetId);
        showTargetsByGroupId(groupId);
    }


    public void removeSubTarget(int subTargetId) throws SQLException {
        SubTargetModel subTargetModel = new SubTargetModel();
        subTargetModel.deleteSubTarget(subTargetId);
    }

    public void editSubTarget(int subTargetId) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/edit_subtarget.fxml"));
        Parent targets = loader.load();

        EditSubTargetController controller = loader.getController();

        Scene scene = new Scene(targets);
        myStage.setScene(scene);

        controller.setTargetId(subTargetId);
        controller.setStage(myStage);

        myStage.show();
    }

    public void doUnReach(int targetId) throws SQLException {
        TargetModel targetModel = new TargetModel();

        targetModel.setUnreachById(targetId);
        showTargetsByGroupId(groupId);
    }

    public void doReach(int targetId) throws SQLException {
        TargetModel targetModel = new TargetModel();

        targetModel.setReachById(targetId);
        showTargetsByGroupId(groupId);
    }

    public void removeTarget(int targetId) throws SQLException {
        TargetModel targetModel = new TargetModel();

        targetModel.deleteTargetById(targetId);
    }

    public void editTarget(int targetId) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/edit_taget.fxml"));
        Parent targets = loader.load();

        EditTargetController controller = loader.getController();

        Scene scene = new Scene(targets);
        myStage.setScene(scene);

        controller.setTargetId(targetId);
        controller.setStage(myStage);

        myStage.show();
    }

    public void addSubTarget(int targetId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/add_subtarget.fxml"));
        Parent targets = loader.load();

        AddSubStageController controller = loader.getController();

        Scene scene = new Scene(targets);
        myStage.setScene(scene);

        controller.setStage(myStage);
        controller.setTargetId(targetId);

        myStage.show();
    }

    @FXML
    void addGroup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/add_group.fxml"));
        Parent addGroup = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(addGroup);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }

    @FXML
    void addTarget(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/add_target.fxml"));
        Parent addTarget = loader.load();

        Controller controller = loader.getController();
        Scene scene = new Scene(addTarget);

        myStage.setScene(scene);

        controller.setStage(myStage);

        myStage.show();
    }
}
