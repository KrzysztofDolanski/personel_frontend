package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import sample.dto.OperatorCredentialsDto;
import sample.factory.PopupFactory;
import sample.rest.Authenticator;
import sample.rest.AuthenticatorImpl;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {

    private static final String APP_PLACE = "/fxml/app.fxml";
    private static final String APP_TITLE = "TITLE";
    private PopupFactory popupFactory;
    private Authenticator auth;

    public LoginController() {
        popupFactory = new PopupFactory();
        auth = new AuthenticatorImpl();
    }

    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private Button confirmButton;
    @FXML
    private Button exitButton;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitButton();
        loginButton();
    }

    private void loginButton() {
        confirmButton.setOnAction(x ->
                performAuth());
    }

    public void performAuth() {
        Stage waitingPopup = popupFactory.createWaitingPopup("Connecting....");
        waitingPopup.show();
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        OperatorCredentialsDto dto = new OperatorCredentialsDto();
        dto.setLogin(login);
        dto.setPassword(password);
        auth.authenticate(dto, (authenticationResult) -> {
            Platform.runLater(() ->{
                waitingPopup.close();
                if (authenticationResult.isAuthenticated()){
                    openAppAndCloseLoginStage();
                } else {
                    shoIncorrectCredentialsMessage();

                }
            });
        });

    }

    private void shoIncorrectCredentialsMessage() {
        System.err.println("Incorrect....");
    }

    private void openAppAndCloseLoginStage() {
        Stage appStage = new Stage();
        Parent appParentRoot = null;
        try {
            appParentRoot = FXMLLoader.load(getClass().getResource(APP_PLACE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(appParentRoot, 1200, 800);
        appStage.setTitle(APP_TITLE);
        appStage.setScene(scene);
        appStage.show();
        getStage().close();

    }

    private void exitButton() {
        exitButton.setOnAction((x) -> {
                getStage().close();});
    }


    private Stage getStage() {
        return (Stage) loginAnchorPane.getScene().getWindow();
    }
}
