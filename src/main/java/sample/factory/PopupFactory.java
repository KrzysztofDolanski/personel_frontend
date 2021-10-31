package sample.factory;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupFactory {


    public Stage createWaitingPopup(String s){
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        VBox pane = new VBox();
        pane.setStyle(waitingPopupStyle());
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(8.5);
        stage.setScene(new Scene(pane, 200, 100));
        Label label = new Label(s);
        label.setStyle(waitingLabelStyle());
        ProgressBar pb = new ProgressBar();
        pane.getChildren().addAll(label, pb);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    private String waitingLabelStyle() {
        return "-fx-text-fill: #003366";
    }

    private String waitingPopupStyle() {
        return "-fx-background-color: #c3c3c3; -fx-border-color: #003322";

    }
}

