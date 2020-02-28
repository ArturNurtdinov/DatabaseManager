package application.controllers;

import db.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductWindowController {
    MainWindowController parent;
    Operations operation;

    @FXML
    TextField title;

    @FXML
    TextField price;

    @FXML
    Label priceLabel;

    @FXML
    private void handleClick() {
        try {
            String stitle = title.getText();
            int cost = 0;
            if (price.isEditable()) {
                cost = Integer.parseInt(price.getText());
            }

            parent.executeOperationOn(new Product(0, stitle, cost), operation);

            Stage stage = (Stage) title.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong. Please read information below.");
            alert.setContentText("Wrong format of input.");

            alert.showAndWait();
        }
    }


    public void provideParent(MainWindowController parent, Operations operation) {
        this.parent = parent;
        this.operation = operation;
        switch (operation) {
            case ADD:
                title.setEditable(true);
                price.setEditable(true);
                break;
            case EDIT:
                priceLabel.setText("New price:");
                title.setEditable(true);
                price.setEditable(true);
                break;
            case DELETE:
            case FIND:
                title.setEditable(true);
                price.setEditable(false);
                break;
        }
    }
}
