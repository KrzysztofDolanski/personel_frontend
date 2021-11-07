package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {


    private static final String EMPLOYEE_FXML = "/fxml/employee.fxml";
    private static final String WAREHOUSE_FXML = "/fxml/warehouse.fxml";
    private static final String LOGIN_FXML = "/fxml/login.fxml";

    @FXML
    private Pane appPane;

    @FXML
    private BorderPane borderPaneMenu;


    @FXML
    private MenuItem employeeMenu;

    @FXML
    private MenuItem warehouseMenu;

    @FXML
    private MenuItem logoutMenu;

    @FXML
    private MenuItem exitMenu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDefaultView();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        warehouseMenu.setOnAction(x -> loadModuleView(WAREHOUSE_FXML));
        employeeMenu.setOnAction(x -> loadModuleView(EMPLOYEE_FXML));
        logoutMenu.setOnAction(x -> logout());
        exitMenu.setOnAction(x-> getStage().close());
    }

    private void logout() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(LOGIN_FXML));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.show();
            getStage().close();
        } catch (IOException e) {
            throw new RuntimeException("Cant load file: " + LOGIN_FXML);
        }
    }


    public void loadDefaultView() {
        loadModuleView(EMPLOYEE_FXML);
    }

    private void loadModuleView(String s) {
        appPane.getChildren().clear();
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(s));
            appPane.getChildren().add(borderPane);
        } catch (IOException e) {
            throw new RuntimeException("Cant reach xml file: " + s);
        }
    }

    private Stage getStage() {
        return (Stage) borderPaneMenu.getScene().getWindow();
    }

}

