package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.dto.WarehouseDto;
import sample.dto.WarehouseModuleDto;
import sample.rest.ItemRestClient;
import sample.rest.WarehouseRestClient;
import sample.table.ItemTableModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {


    private static final String ADD_ITEM_FXML = "/fxml/add-item.fxml";
    private static final String VIEW_ITEM_FXML = "/fxml/view-item.fxml";
    private static final String EDIT_ITEM_FXML = "/fxml/edit-item.fxml";
    private static final String DELETE_ITEM_FXML = "/fxml/delete-item.fxml";
    @FXML
    private BorderPane warehouseBorderPane;

    @FXML
    private Button addButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

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
        initializeAddItemButton();
        initializeViewItemButton();
        initializeEditItemButton();
        initializeDeleteButton();
        initializeRefreshButton();
    }

    private void initializeRefreshButton() {
        refreshButton.setOnAction(x -> {
            loadItemData();
        });
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(x -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            try {
                Stage stage = createItemCrudStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(DELETE_ITEM_FXML));
                Scene scene = new Scene(loader.load(), 400, 200);
                stage.setScene(scene);
                DeleteItemController controller = loader.getController();
                controller.loadItem(selectedItem);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException("Something wrong in delete " + DELETE_ITEM_FXML);
            }
        });
    }

    private void initializeEditItemButton() {
        editButton.setOnAction(x -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            } else {
                try {
                    Stage stage = createItemCrudStage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_ITEM_FXML));
                    Scene scene = new Scene(loader.load(), 550, 400);
                    stage.setScene(scene);
                    EditItemController controller = loader.getController();
                    controller.loadItemData(selectedItem.getIdItem());
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException("Can't load file FXML");
                }
            }
        });
    }

    private void initializeAddItemButton() {
        addButton.setOnAction(x -> {
            try {
                Stage stage = createItemCrudStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ADD_ITEM_FXML));
                Scene scene = new Scene(loader.load(), 500, 400);
                stage.setScene(scene);
                AddItemController controller = loader.getController();
                WarehouseDto selectedWarehouseDto = warehouseComboBox.getSelectionModel().getSelectedItem();
                controller.setWarehouseDto(selectedWarehouseDto);
                controller.loadQuantityTypes();
                stage.show();
            } catch (IOException e) {
                System.out.println("Something wrong in initializeAddItemButton");
            }
        });
    }


    private void initializeViewItemButton() {
        viewButton.setOnAction(x -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            try {
                Stage stage = createItemCrudStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_ITEM_FXML));
                Scene scene = new Scene(loader.load(), 500, 400);
                stage.setScene(scene);
                ViewItemController controller = loader.getController();
                controller.loadItemData(selectedItem.getIdItem());
                stage.show();
            } catch (IOException e) {
                System.out.println("Something wrong in initializeAddItemButton");
            }
        });
    }

    private Stage createItemCrudStage() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    private void initializeCombobox() {
        warehouseComboBox.valueProperty().addListener((obsevable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (!newValue.equals(oldValue) && oldValue != null) {
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


    private void loadItemData() {
        Thread thread = new Thread(() -> {
            WarehouseModuleDto warehouseModuleDto = warehouseRestClient.getWarehouseModuleDto();
            data.clear();
            setWarehouseComboBoxItem(warehouseModuleDto);
            data.addAll(warehouseModuleDto.getItemDtoList().stream().map(ItemTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }


    private void loadItemData(WarehouseDto warehouseDto) {
        Thread thread = new Thread(() -> {
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
        Platform.runLater(() -> {
            warehouseComoBoxItems.addAll(warehouseDtoList);
            warehouseComboBox.setItems(warehouseComoBoxItems);
            warehouseComboBox.getSelectionModel()
                    .select(warehouseDtoList.indexOf(warehouseModuleDto.getSelectedWarehouse()));
        });
    }
}
