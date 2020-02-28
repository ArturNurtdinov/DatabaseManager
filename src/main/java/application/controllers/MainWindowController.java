package application.controllers;

import application.ProductGUI;
import db.Product;
import db.ProductDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;


public class MainWindowController {
    private ProductGUI app;
    private ProductDAO dao;
    private int numberToGenerate;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<?, ?> idColumn;
    @FXML
    private TableColumn<?, ?> prodidColumn;
    @FXML
    private TableColumn<?, ?> titleColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private void add() {
        openProductWindowWith(Operations.ADD);
    }

    @FXML
    private void delete() {
        openProductWindowWith(Operations.DELETE);
    }

    @FXML
    private void showAll() {
        refreshTable();
    }

    @FXML
    private void find() {
        openProductWindowWith(Operations.FIND);
    }

    @FXML
    private void edit() {
        openProductWindowWith(Operations.EDIT);
    }

    @FXML
    private void filter() {
        openProductWindowWith(Operations.FILTER);
    }

    public void provideApp(ProductGUI app) {
        this.app = app;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodidColumn.setCellValueFactory(new PropertyValueFactory<>("prodid"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    public void provideDao(ProductDAO dao) {
        this.dao = dao;
    }

    public void provideNumberToGenerate(int numberToGenerate) {
        this.numberToGenerate = numberToGenerate;
    }

    public TableView<Product> getTable() {
        return productTable;
    }

    public void executeOperationOn(Product product, Operations operation, int costTo) {
        try {
            switch (operation) {
                case ADD:
                    dao.add(product);
                    productTable.getItems().add(product);
                    refreshTable();
                    break;
                case DELETE:
                    dao.deleteByName(product.getTitle());
                    refreshTable();
                    break;
                case EDIT:
                    dao.changePrice(product.getTitle(), product.getCost());
                    refreshTable();
                    break;
                case FIND:
                    Product productToFind = dao.getByTitle(product.getTitle());
                    productTable.getItems().clear();
                    productTable.getItems().add(productToFind);
                    break;
                case FILTER:
                    productTable.getItems().clear();
                    productTable.getItems().addAll(dao.filterByPrice(product.getCost(), costTo));
                    break;
            }
        } catch (RuntimeException e) {
            app.showErrorWindow(e.getMessage());
        }
    }

    private void refreshTable() {
        productTable.getItems().clear();
        productTable.getItems().addAll(dao.list());
    }

    private void openProductWindowWith(Operations operation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/product_window.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Enter product");
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);
            stage.show();

            ProductWindowController controller = loader.getController();
            controller.provideParent(this, operation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
