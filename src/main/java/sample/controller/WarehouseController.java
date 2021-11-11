package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import sample.dto.ItemDto;
import sample.dto.WarehouseDto;
import sample.dto.WarehouseModuleDto;
import sample.rest.ItemRestClient;
import sample.rest.WarehouseRestClient;
import sample.table.ItemTableModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {


    @FXML
    private BorderPane warehouseBorderPane;

    @FXML
    private Button addButton;

    @FXML
    private Button ViewButton;

    @FXML
    private Button EditButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button refreshButton;


    @FXML
    private ComboBox<WarehouseDto> warehouseComboBox;

    @FXML
    private TableView<ItemTableModel> warehouseTableView;

    private ObservableList<ItemTableModel> data;

    private final ItemRestClient itemRestClient;

    private final WarehouseRestClient warehouseRestClient;

    public WarehouseController() {
        itemRestClient = new ItemRestClient();
        data = FXCollections.observableArrayList();
        warehouseRestClient = new WarehouseRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCombobox();
        initializeTableView();
    }

    private void initializeCombobox() {
        warehouseComboBox.valueProperty().addListener((obsevable, oldValue, newValue)->{
            if (newValue==null){
                return;
            }
            if (!newValue.equals(oldValue)&& oldValue!=null){
                WarehouseDto warehouseDto = warehouseComboBox.getSelectionModel().getSelectedItem();
                loadItemData(warehouseDto);
            }

        });
    }

    private void initializeTableView() {
        warehouseTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("Quantity type");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("quantityType"));

        warehouseTableView.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);
        warehouseTableView.setItems(data);
        loadItemData();
    }


    private void loadItemData(){
        Thread thread = new Thread(()->{
            WarehouseModuleDto warehouseModuleDto = warehouseRestClient.getWarehouseModuleDto();
            data.clear();
            setWarehouseComboBoxItem(warehouseModuleDto);
            data.addAll(warehouseModuleDto.getItemDtoList().stream().map(ItemTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }



    private void loadItemData(WarehouseDto warehouseDto){
        Thread thread = new Thread(()->{
            WarehouseModuleDto warehouseModuleDto = warehouseRestClient.getWarehouseModuleDto(warehouseDto.getIdWarehouse());
            data.clear();
            setWarehouseComboBoxItem(warehouseModuleDto);
            data.addAll(warehouseModuleDto.getItemDtoList().stream().map(ItemTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }

    private void setWarehouseComboBoxItem(WarehouseModuleDto warehouseModuleDto) {
        List<WarehouseDto> warehouseDtoList = warehouseModuleDto.getWarehouseDtoList();
        ObservableList<WarehouseDto> warehouseComoBoxItems = FXCollections.observableArrayList();
        warehouseComoBoxItems.addAll(warehouseDtoList);
        Platform.runLater(()->{
            warehouseComboBox.setItems(warehouseComoBoxItems);
            warehouseComboBox.getSelectionModel()
                    .select(warehouseDtoList.indexOf(warehouseModuleDto.getSelectedWarehouse()));
        });
    }
}
