package com.STO.MHP.controllers;

import com.STO.MHP.views.FxmlView;
import com.STO.MHP.models.Package;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.springframework.stereotype.Controller;
import com.STO.MHP.config.StageManager;
import com.STO.MHP.models.Event;
import com.STO.MHP.services.impl.PackageService;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * FXML Controller class
 *
 * @author Coke Serdanco
 */
@Controller
public class PackageController implements Initializable {

    @FXML
    private Label lbl_packageId;

    @FXML
    private RadioButton rbtn_package1;
    @FXML
    private RadioButton rbtn_package2;
    @FXML
    private RadioButton rbtn_package3;
    @FXML
    private ToggleGroup amount;
    
    @FXML
    private RadioButton rbtn_cash;
    
    @FXML
    private RadioButton rbtn_credit;
    
    @FXML
    private ToggleGroup payment;
    
    @FXML
    private TableView<Package> packageTable;

    @FXML
    private TableColumn<Package, Long> col_ID;
    
    @FXML
    private TableColumn<Package, String> col_package;

    @FXML
    private TableColumn<Package, String> col_Amount;
    
    @FXML
    private TableColumn<Package, String> col_Payment;

    @FXML
    private Button btn_confirm;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_save;

    @Lazy
    @Autowired
    private StageManager stageManager;
    
    private ObservableList<Package> packageList = FXCollections.observableArrayList();

    @Autowired
    private PackageService packageService;


    @FXML
    void btn_confirm(ActionEvent event) {
        stageManager.switchScene(FxmlView.EVENT);
    }
    
    @FXML
    void btn_delete(ActionEvent event) {

        List<Package> packages = packageTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected package?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            packageService.deleteInBatch(packages);
        }

        loadPackageDetails();
    }


    @FXML
    void btn_save(ActionEvent event) {
        if (lbl_packageId.getText() == null || "".equals(lbl_packageId.getText())) {
            if (true) {

                Package packages = new Package();
                packages.setAmount(getAmount());
                packages.setPackages(getPackages());
                packages.setPayment(getPayment());

                Package newPackage = packageService.save(packages);

                saveAlert(newPackage);
            }

        } else {
            Package packages = packageService.find(Long.parseLong(lbl_packageId.getText()));
            packages.setAmount(getAmount());
            packages.setPackages(getPackages());
            packages.setPayment(getPayment());

            Package updatedPackage = packageService.update(packages);
            updateAlert(updatedPackage);
        }

        clearFields();
        loadPackageDetails();
    }

    private void clearFields() {
        lbl_packageId.setText(null);
        rbtn_package1.setSelected(true);
        rbtn_package2.setSelected(false);
        rbtn_package3.setSelected(false);
        rbtn_cash.setSelected(false);
        rbtn_credit.setSelected(false);
    }

    private void saveAlert(Package packages) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Package saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The Package has been confirmed and \n" + " ID is " + packages.getId() + ".");
        alert.showAndWait();
        loadPackageDetails();
    }

    private void updateAlert(Package packages) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The package has been updated.");
        alert.showAndWait();
    }

    public String getAmount() {
        return rbtn_package1.isSelected() ? "PHP 7,000" : "PHP 10,000";
    }
    
    public String getPackages() {
        return rbtn_package1.isSelected() ? "Package 1" : "Package 2";
    }
    
    public String getPayment() {
        return rbtn_cash.isSelected() ? "Cash" : "Credit Card";
    }

    private void setColumnProperties() {


        col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        col_package.setCellValueFactory(new PropertyValueFactory<>("Packages"));
        col_Payment.setCellValueFactory(new PropertyValueFactory<>("Payment"));

    }

    Callback<TableColumn<Package, Boolean>, TableCell<Package, Boolean>> cellFactory
            = new Callback<TableColumn<Package, Boolean>, TableCell<Package, Boolean>>() {
        @Override
        public TableCell<Package, Boolean> call(final TableColumn<Package, Boolean> param) {
            final TableCell<Package, Boolean> cell = new TableCell<Package, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Package packages = getTableView().getItems().get(getIndex());
                            updatePackage(packages);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updatePackage(Package packages) {
                    lbl_packageId.setText(Long.toString(packages.getId()));
                    
                    if (packages.getAmount().equals("PHP 7,000")) {
                        rbtn_package1.setSelected(true);
                    }

                    else {
                        rbtn_package2.setSelected(true);
                    }
                    if (packages.getPackages().equals("Package 1")) {
                        rbtn_package1.setSelected(true);
                    }

                    else {
                        rbtn_package2.setSelected(true);
                    }
                    if (packages.getPayment().equals("Cash")) {
                        rbtn_cash.setSelected(true);
                    }

                    else {
                        rbtn_credit.setSelected(true);
                    }
                }
            };
            return cell;
        }
    };
    
        private void loadPackageDetails() {
        packageList.clear();
        packageList.addAll(packageService.findAll());

        packageTable.setItems(packageList);
    }

     private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        packageTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();
        loadPackageDetails();
    }

}
