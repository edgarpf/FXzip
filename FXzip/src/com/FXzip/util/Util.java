package com.FXzip.util;

import javafx.scene.control.Alert;

public class Util {

    public static void showErrorDialog(String title, String headerText, String message) {
        Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
        dialogoErro.setTitle(title);
        dialogoErro.setHeaderText(headerText);
        dialogoErro.setContentText(message);
        dialogoErro.showAndWait();
    }
}
