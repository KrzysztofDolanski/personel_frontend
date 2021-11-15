package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.dto.ItemSaveDto;
import sample.dto.QuantityTypeDto;
import sample.dto.WarehouseDto;
import sample.handler.ProcessFinishHandler;
import sample.rest.ItemRestClient;
import sample.rest.QuantityTypeRestClient;

import java.net.URL;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {


    @FXML
    private BorderPane addItemBorderPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private ComboBox<QuantityTypeDto> quantityTypeComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private WarehouseDto selectedWarehouseDto;
    private final ItemRestClient itemRestClient;
    private final QuantityTypeRestClient quantityTypeRestClient;

    public AddItemController() {
        quantityTypeRestClient = new QuantityTypeRestClient();
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(x->{

            String name = nameTextField.getText();
            Double doubleQuantity = Double.parseDouble(quantityTextField.getText());
            Long idQuantityType = quantityTypeComboBox.getSelectionModel().getSelectedItem().getIdQuantityType();
            Long idWarehouse = selectedWarehouseDto.getIdWarehouse();
            ItemSaveDto dto = new ItemSaveDto(name, doubleQuantity, idQuantityType, idWarehouse);
            Thread thread = new Thread(()->{
                itemRestClient.saveItem(dto, ()->{
                    getStage().close();
                });
            });

        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x) ->{
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) addItemBorderPane.getScene().getWindow();
    }

}

