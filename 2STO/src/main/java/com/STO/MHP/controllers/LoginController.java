package com.STO.MHP.controllers;

import com.STO.MHP.config.StageManager;
import com.STO.MHP.services.impl.ClientService;
import com.STO.MHP.views.FxmlView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Coke Serdanco
 */
@Controller
public class LoginController implements Initializable {

    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_pwd;
    @FXML
    private Button btn_sign;
    
    @FXML
    private Label lbl_login;

    @Autowired
    private ClientService userService;
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @FXML
    void btn_sign(ActionEvent event) {

    stageManager.switchScene(FxmlView.USER);

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
