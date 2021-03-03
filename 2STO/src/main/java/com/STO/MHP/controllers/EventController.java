package com.STO.MHP.controllers;

import com.STO.MHP.config.StageManager;
import com.STO.MHP.services.impl.EventService;
import com.STO.MHP.views.FxmlView;
import com.STO.MHP.models.Event;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Coke Serdanco
 */
@Controller
public class EventController implements Initializable {

    @FXML
    private Button btn_back;

    @FXML
    private TextField txt_venue;

    @FXML
    private TextField txt_location;

    @FXML
    private DatePicker dob_sched;

    @FXML
    private TextField txt_hour;

    @FXML
    private RadioButton rbtn_AM;

    @FXML
    private ToggleGroup time;

    @FXML
    private RadioButton rbtn_PM;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_save;

    @FXML
    private Label lbl_eventID;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Long> colEventID;

    @FXML
    private TableColumn<Event, String> colVenue;

    @FXML
    private TableColumn<Event, String> colLocation;

    @FXML
    private TableColumn<Event, LocalDate> colSchedule;

    @FXML
    private TableColumn<Event, String> colTime;
    
    @FXML
    private TableColumn<Event, String> colHour;


    @FXML
    private TableColumn<Event, Boolean> colEdit;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EventService eventService;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();

    @FXML
    void btn_clear(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btn_delete(ActionEvent event) {

        List<Event> events = eventTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected event?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            eventService.deleteInBatch(events);
        }

        loadEventDetails();
    }

    @FXML
    void btn_save(ActionEvent event) {
        if (validate("Venue", getVenue(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Location", getLocation(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Hour", getHour(), "([1,2,3,4,5,6,7,8,9,0]{1,30}\\s*)+")
                 && emptyValidation("DOB", dob_sched.getEditor().getText().isEmpty())) {

            if (lbl_eventID.getText() == null || "".equals(lbl_eventID.getText())) {
                if (true) {

                    Event events = new Event();
                    events.setVenue(getVenue());
                    events.setLocation(getLocation());
                    events.setDob(getDob());
                    events.setHour(getHour());
                    events.setTime(getTime());

                    Event newEvent = eventService.save(events);

                    saveAlert(newEvent);
                }

            } else {
                Event events = eventService.find(Long.parseLong(lbl_eventID.getText()));
                events.setVenue(getVenue());
                events.setLocation(getLocation());
                events.setDob(getDob());
                events.setHour(getHour());
                events.setTime(getTime());

                Event updatedEvent = eventService.update(events);
                updateAlert(updatedEvent);
            }

            clearFields();
            loadEventDetails();
        }
    }

    private void clearFields() {
        lbl_eventID.setText(null);
        txt_venue.clear();
        txt_location.clear();
        dob_sched.getEditor().clear();
        txt_hour.clear();
        rbtn_AM.setSelected(true);
        rbtn_PM.setSelected(false);
    }

    private void saveAlert(Event events) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The Event " + events.getVenue() + " " + events.getLocation() + " has been created and \n" + " id is " + events.getId() + ".");
        alert.showAndWait();
        loadEventDetails();
    }

    private void updateAlert(Event events) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The event " + events.getVenue() + " " + events.getLocation() + " has been updated.");
        alert.showAndWait();
    }
    public String getVenue() {
        return txt_venue.getText();
    }

    public String getLocation() {
        return txt_location.getText();
    }

    public LocalDate getDob() {
        return dob_sched.getValue();
    }
    
    public String getHour(){
        return txt_hour.getText();
    }
    
        public String getTime() {
        return rbtn_AM.isSelected() ? "AM" : "PM";
    }

    private void setColumnProperties() {

        colEventID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colHour.setCellValueFactory(new PropertyValueFactory<>("hour"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Event, Boolean>, TableCell<Event, Boolean>> cellFactory
            = new Callback<TableColumn<Event, Boolean>, TableCell<Event, Boolean>>() {
        @Override
        public TableCell<Event, Boolean> call(final TableColumn<Event, Boolean> param) {
            final TableCell<Event, Boolean> cell = new TableCell<Event, Boolean>() {
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
                            Event event = getTableView().getItems().get(getIndex());
                            updateEvent(event);
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

                private void updateEvent(Event events) {
                    lbl_eventID.setText(Long.toString(events.getId()));
                    txt_venue.setText(events.getVenue());
                    txt_location.setText(events.getLocation());
                    dob_sched.setValue(events.getDob());
                    txt_hour.setText(events.getHour());
                    if (events.getTime().equals("AM")) {
                        rbtn_AM.setSelected(true);
                    } else {
                        rbtn_PM.setSelected(true);
                    }
                }
            };
            return cell;
        }
    };

    private void loadEventDetails() {
        eventList.clear();
        eventList.addAll(eventService.findAll());

        eventTable.setItems(eventList);
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
        eventTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        loadEventDetails();
    }

}
