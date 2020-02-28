package application.controllers;

import application.ProductGUI;
import db.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginWindowController {
    private ProductGUI app;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    private void login() {
        try {
            ProductDAO dao = new ProductDAO(username.getText(), password.getText());
            app.provideDao(dao);
            app.showEnterWindow();
        } catch (RuntimeException e) {
            app.showErrorWindow("Failed to connect to database.");
        }
    }

    @FXML
    private void clear() {
        username.clear();
        password.clear();
    }


    public void provideApp(ProductGUI app) {
        this.app = app;
    }
}
