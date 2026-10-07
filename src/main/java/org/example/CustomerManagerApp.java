package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerManagerApp extends Application {

    // Customer list
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // -------------------------
        // TITLE
        // -------------------------
        Label title = new Label("Customer Manager");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // -------------------------
        // NAME FIELD
        // -------------------------
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");
        nameField.setAccessibleText("Customer name");

        // -------------------------
        // PROVINCE LIST
        // -------------------------
        Label provinceLabel = new Label("Province:");

        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceBox.setPromptText("Select province");
        provinceBox.setAccessibleText("Customer province");

        // -------------------------
        // BUTTONS
        // -------------------------
        Button addButton = new Button("Add Customer");
        Button deleteButton = new Button("Delete Customer");

        // Keyboard access
        addButton.setMnemonicParsing(true);
        deleteButton.setMnemonicParsing(true);

        // -------------------------
        // TABLE
        // -------------------------
        TableView<Customer> table = new TableView<>(customers);

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        nameColumn.setPrefWidth(250);
        provinceColumn.setPrefWidth(200);

        table.getColumns().addAll(nameColumn, provinceColumn);

        table.setPlaceholder(
                new Label("No customers added yet.")
        );

        // -------------------------
        // ADD CUSTOMER
        // -------------------------
        addButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            // Validation
            if (name.isEmpty()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please enter the customer's name."
                );
                nameField.requestFocus();
                return;
            }

            if (province == null || province.isEmpty()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please select a province."
                );
                provinceBox.requestFocus();
                return;
            }

            // Add customer
            Customer customer = new Customer(name, province);
            customers.add(customer);

            // Clear form
            nameField.clear();
            provinceBox.setValue(null);

            nameField.requestFocus();
        });

        // -------------------------
        // DELETE CUSTOMER
        // -------------------------
        deleteButton.setOnAction(event -> {

            Customer selectedCustomer =
                    table.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "No Customer Selected",
                        "Please select a customer to delete."
                );
                return;
            }

            // Confirmation dialog
            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Customer");
            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName()
                            + "?"
            );

            Optional<ButtonType> result =
                    confirmation.showAndWait();

            if (result.isPresent()
                    && result.get() == ButtonType.OK) {

                customers.remove(selectedCustomer);
            }
        });

        // -------------------------
        // FORM LAYOUT
        // -------------------------
        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        form.add(provinceLabel, 0, 1);
        form.add(provinceBox, 1, 1);

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(
                addButton,
                deleteButton
        );

        form.add(buttons, 1, 2);

        // -------------------------
        // MAIN LAYOUT
        // -------------------------
        VBox root = new VBox(15);

        root.setPadding(new Insets(20));

        root.getChildren().addAll(
                title,
                form,
                table
        );

        // -------------------------
        // KEYBOARD ACCESS
        // -------------------------

        // Enter = Add customer
        nameField.setOnAction(event ->
                addButton.fire()
        );

        provinceBox.setOnAction(event ->
                addButton.fire()
        );

        // Delete key = delete selected customer
        table.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("DELETE")) {
                deleteButton.fire();
            }
        });

        // -------------------------
        // SCENE
        // -------------------------
        Scene scene = new Scene(root, 600, 500);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        nameField.requestFocus();
    }

    // -------------------------
    // ALERT METHOD
    // -------------------------
    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // -------------------------
    // CUSTOMER CLASS
    // -------------------------
    public static class Customer {

        private final String name;
        private final String province;

        public Customer(String name, String province) {
            this.name = name;
            this.province = province;
        }

        public String getName() {
            return name;
        }

        public String getProvince() {
            return province;
        }
    }

    // -------------------------
    // MAIN METHOD
    // -------------------------
    public static void main(String[] args) {
        launch(args);
    }
}