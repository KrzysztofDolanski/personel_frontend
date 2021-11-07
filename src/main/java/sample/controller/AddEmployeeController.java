package sample.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import sample.dto.EmployeeDto;
import sample.factory.PopupFactory;
import sample.handler.EmployeeLoadedHandler;
import sample.rest.EmployeeRestClient;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {


    private final PopupFactory popupFactory;
    private final EmployeeRestClient employeeRestClient;

    @FXML
    private GridPane gridPane;

    @FXML
    private BorderPane addEmployeeBorderPane;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField salaryTextField;



    public AddEmployeeController() {
        popupFactory = new PopupFactory();
        employeeRestClient = new EmployeeRestClient();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(x -> {
            EmployeeDto dto = createEmployeeDto();
            Stage waitingPopup = popupFactory.createWaitingPopup("Connecting to the server...");
            waitingPopup.show();
            employeeRestClient.saveEmployee(dto, () -> {
                waitingPopup.close();
                Stage infoPopup = popupFactory.createInfoPopup("Employee has been saved", () -> {
                    getStage().close();
                });
                infoPopup.show();
            });
        });
    }

    private EmployeeDto createEmployeeDto() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String salary = salaryTextField.getText();
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setSalary(salary);
        return dto;
    }


    public void loadEmployeeData(Long idEmployee, EmployeeLoadedHandler employeeLoadedHandler) {
        Thread thread = new Thread(() -> {
            EmployeeDto dto = employeeRestClient.getEmployee(idEmployee);
            Platform.runLater(() -> {
                firstNameTextField.setText(dto.getFirstName());
                lastNameTextField.setText(dto.getLastName());
                salaryTextField.setText(dto.getSalary());
                employeeLoadedHandler.handle();
            });
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x) ->{
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) addEmployeeBorderPane.getScene().getWindow();
    }

}
