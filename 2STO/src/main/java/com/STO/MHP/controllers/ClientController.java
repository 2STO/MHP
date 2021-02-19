package com.STO.MHP.controllers;


import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import com.STO.MHP.config.StageManager;
import com.STO.MHP.models.Client;
import com.STO.MHP.services.impl.ClientService;
import com.STO.MHP.views.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;


@Controller
public class ClientController implements Initializable {

    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField address;
    
    @FXML
    private TextField contact;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private RadioButton rbMale;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;

    @FXML
    private Button btn_package;
    
    @FXML
    private TableView<Client> userTable;

    @FXML
    private TableColumn<Client, Long> colUserId;

    @FXML
    private TableColumn<Client, String> colFirstName;

    @FXML
    private TableColumn<Client, String> colLastName;

    @FXML
    private TableColumn<Client, String> colAddress;
    
    @FXML
    private TableColumn<Client, String> colContact;

    @FXML
    private TableColumn<Client, Boolean> colEdit;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ClientService userService;

    private ObservableList<Client> userList = FXCollections.observableArrayList();

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }
    
    @FXML
    private void btn_package(ActionEvent event){
        stageManager.switchScene(FxmlView.PACKAGE);
    }

    @FXML
    private void saveUser(ActionEvent event) {

        if (validate("First Name", getFirstName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Last Name", getLastName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Address", getAddress(),"([a-zA-Z]{3,30}\\s*)+")
                && validate("Contact", getContact(),"([1,2,3,4,5,6,7,8,9,0]{3,30}\\s*)")) {

            if (userId.getText() == null || "".equals(userId.getText())) {
                if (true) {

                    Client user = new Client();
                    user.setFirstName(getFirstName());
                    user.setLastName(getLastName());
                    user.setAddress(getAddress());
                    user.setContact(getContact());

                    Client newUser = userService.save(user);

                    saveAlert(newUser);
                }

            } else {
                Client user = userService.find(Long.parseLong(userId.getText()));
                user.setFirstName(getFirstName());
                user.setLastName(getLastName());
                user.setAddress(getAddress());
                user.setContact(getContact());
                Client updatedUser = userService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadUserDetails();
        }

    }

    @FXML
    private void deleteUsers(ActionEvent event) {
        List<Client> users = userTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected user?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            userService.deleteInBatch(users);
        }

        loadUserDetails();
    }

    private void clearFields() {
        userId.setText(null);
        firstName.clear();
        lastName.clear();
        address.clear();
        contact.clear();
        rbMale.setSelected(true);
        rbFemale.setSelected(false);
    }

    private void saveAlert(Client user) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + user.getFirstName() + " " + user.getLastName() + " has been created and \n" + " id is " + user.getId() + ".");
        alert.showAndWait();
        loadUserDetails();
    }

    private void updateAlert(Client user) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("User updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + user.getFirstName() + " " + user.getLastName() + " has been updated.");
        alert.showAndWait();
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getAddress() {
        return address.getText();
    }
    
    public String getContact() {
        return contact.getText();
    }

    private void setColumnProperties() {


        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> cellFactory
            = new Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>>() {
        @Override
        public TableCell<Client, Boolean> call(final TableColumn<Client, Boolean> param) {
            final TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
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
                            Client user = getTableView().getItems().get(getIndex());
                            updateUser(user);
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

                private void updateUser(Client user) {
                    userId.setText(Long.toString(user.getId()));
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    address.setText(user.getAddress());
                    contact.setText(user.getContact());
                }
            };
            return cell;
        }
    };

    private void loadUserDetails() {
        userList.clear();
        userList.addAll(userService.findAll());

        userTable.setItems(userList);
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
        Alert alert = new Alert(AlertType.WARNING);
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
    public void initialize(URL location, ResourceBundle resources) {

        userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();
        loadUserDetails();
    }




}
