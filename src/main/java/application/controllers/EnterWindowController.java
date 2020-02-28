package application.controllers;

import application.ProductGUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EnterWindowController {
    ProductGUI app;

    @FXML
    TextField input;

    @FXML
    private void handleClick() {
        try {
            int number = Integer.parseInt(input.getText());
            app.showMainWindow(number);
        } catch (NumberFormatException e) {
            app.showErrorWindow("Wrong format. Please enter number.");
        }
    }

    public void provideApp(ProductGUI app) {
        this.app = app;
    }
}
