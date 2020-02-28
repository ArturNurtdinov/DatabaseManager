package application;

import application.controllers.EnterWindowController;
import application.controllers.LoginWindowController;
import application.controllers.MainWindowController;
import db.Product;
import db.ProductDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductGUI extends Application {

    Stage primaryStage;
    ProductDAO dao;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        showLoginWindow();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_window.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Login");

            LoginWindowController controller = loader.getController();
            controller.provideApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainWindow(int numberToGenerate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_window.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Products");

            MainWindowController controller = loader.getController();
            controller.provideApp(this);
            controller.provideDao(dao);
            controller.provideNumberToGenerate(numberToGenerate);
            dao.clear();
            for (int i = 0; i < numberToGenerate; i++) {
                dao.add(new Product(i, "product " + i, i * 10));
                controller.getTable().getItems().add(new Product(i, "product " + i, i * 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEnterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/enter_window.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Please enter amount of rows to generate");

            EnterWindowController controller = loader.getController();
            controller.provideApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong. Please read information below.");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void provideDao(ProductDAO dao) {
        this.dao = dao;
    }
}
