package com.FXzip.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Util {

    public static void showErrorDialog(String title, String headerText, String message, AlertType alertType) {
        Alert dialog = new Alert(alertType);
        dialog.setTitle(title);        
        dialog.setHeaderText(headerText);
        dialog.setContentText(message);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
        dialog.showAndWait();
    }
}
