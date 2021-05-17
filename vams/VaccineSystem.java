package vaccinesystem;

// Imports
import java.awt.Desktop;
import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import static vaccinesystem.AppointmentManagement.appointmentsArray;
import static vaccinesystem.UserManagement.*;
import static vaccinesystem.VaccineManagement.*;


/**
 *
 * @author seanomahony
 */
public class VaccineSystem extends Application {
    
    // ------------------------------------------------------------------------
    //                        MAIN METHOD
    // ------------------------------------------------------------------------
    
    public static void main(String[] args) {
        // Populate Arrays from textfile
        populateArrays();
        // Launch GUI
        launch(args);
    }
    
    // Scenes, variables and controls needed throughout code
    Scene sceneHome, sceneUserLogin, sceneClinicianLogin, scenePhaLogin;
    
    // User Scenes
    Scene sceneUserRegistration, sceneUserQuestionnaire, sceneUserHome, sceneUserBookAppointment, sceneUserViewAppointments, sceneUserEditProfile, sceneUserViewVaccineInfo;
    
    // Clinician Scenes
    Scene sceneClinicianHome, sceneClinicianCreateQuestionnaires, sceneClinicianCreateAppointment;
    
    // Admin Scenes
    Scene sceneAdminHome, sceneAdminEditVaccineInfo, sceneAdminCreateVaccine, sceneAdminEditVaccineCentres, sceneAdminCreateVaccineCentre;
    
    // Mixed Scenes
    Scene sceneViewAppointments, sceneViewPatients, sceneViewQuestionnaires;
    
    // User Persistence String
    String userLoggedIn;
    
    // Login Booleans
    Boolean adminLoggedIn = false;
    Boolean clinicianLoggedIn = false;
    
    // User Appointments Table
    TableView<Appointment> tblUserViewAppointments = new TableView();
    
    // Add create questionnaire name combobox
    ComboBox cboCreateName = new ComboBox();
    
    // User Edit Profile TextFields
    TextField txtUserEditProfileName, txtUserEditProfileEmail, txtUserEditProfilePassword, txtUserEditProfileTown, txtUserEditProfileEircode, txtUserEditProfileNumber;
    
    // ------------------------------------------------------------------------
    //                        GRAPHICAL USER INTERFACE
    // ------------------------------------------------------------------------
    
    @Override
    public void start(Stage primaryStage) {
        
        // Confirmation to close on click X
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close this application?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                we.consume();
            } else {
                System.exit(0);
            }
        });
        
        
        /* ------------------------------------------------------------------------
                                    HOME PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneHome = new GridPane();    

        //Setting size for the pane 
        gridPaneHome.setMinSize(400, 250);

        //Setting the padding  
        gridPaneHome.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneHome.setVgap(10); 
        gridPaneHome.setHgap(2);       

        //setting the alignment
        gridPaneHome.setAlignment(Pos.CENTER);

        // Add Image
        Image imgHomeHeader = new Image("https://i.imgur.com/Ieddqbj.png");
        ImageView imgViewHome = new ImageView(imgHomeHeader);
        gridPaneHome.add(imgViewHome, 0, 0, 3, 1);
        GridPane.setHalignment(imgViewHome, HPos.CENTER);
        GridPane.setMargin(imgViewHome, new Insets(20, 0, 20, 0));

        //Add header  
        Label lblSelectUserHeader = new Label("Please Select User:");
        lblSelectUserHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneHome.add(lblSelectUserHeader, 0,1,2,1);
        GridPane.setHalignment(lblSelectUserHeader, HPos.CENTER);
        GridPane.setMargin(lblSelectUserHeader, new Insets(20, 0, 10, 0));
        
        // Add user login button
        Button btnUser = new Button("Vaccine Portal for Patients");
        btnUser.setPrefHeight(40);
        btnUser.setPrefWidth(210);
        gridPaneHome.add(btnUser, 0, 2, 2, 1);
        GridPane.setHalignment(btnUser, HPos.CENTER);
        GridPane.setMargin(btnUser, new Insets(10, 0, 10, 0));
        btnUser.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUser.setOnAction(event -> {
            // Change scene
            primaryStage.setScene(sceneUserLogin);
        });

        // Add clinican login button
        Button btnClinician = new Button("Clinician Appointment Management");
        btnClinician.setPrefHeight(40);
        btnClinician.setPrefWidth(230);
        gridPaneHome.add(btnClinician, 0, 3, 2, 1);
        GridPane.setHalignment(btnClinician, HPos.CENTER);
        GridPane.setMargin(btnClinician, new Insets(10, 0, 10, 0));
        btnClinician.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinician.setOnAction(event -> {
            // Change scene
            primaryStage.setScene(sceneClinicianLogin);
        });

        // Add healthcare provider login button
        Button btnHealthProvider = new Button("Heathcare Provider Administration");
        btnHealthProvider.setPrefHeight(40);
        btnHealthProvider.setPrefWidth(210);
        gridPaneHome.add(btnHealthProvider, 0, 4, 2, 1);
        GridPane.setHalignment(btnHealthProvider, HPos.CENTER);
        GridPane.setMargin(btnHealthProvider, new Insets(10, 0, 10, 0));
        btnHealthProvider.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnHealthProvider.setOnAction(event -> {
            // Change scene
            primaryStage.setScene(scenePhaLogin);
        });

        //Styling nodes  
        gridPaneHome.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        sceneHome = new Scene(gridPaneHome, 350, 475); 

        // Setting title to the Stage   
        primaryStage.setTitle("Vaccine Administration System"); 

        // Adding scene to the stage 
        primaryStage.setScene(sceneHome);

        // Disable resize
        primaryStage.setResizable(false);
        
        //Displaying the contents of the stage 
        primaryStage.show(); 

        /* ------------------------------------------------------------------------
                                       USER LOGIN
           ------------------------------------------------------------------------ */

        //Creating a Grid Pane 
        GridPane gridPaneUserLogin = new GridPane();    

        //Setting size for the pane 
        gridPaneUserLogin.setMinSize(500, 450);

        //Setting the padding  
        gridPaneUserLogin.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneUserLogin.setVgap(10); 
        gridPaneUserLogin.setHgap(2);       

        //setting the alignment
        gridPaneUserLogin.setAlignment(Pos.CENTER);

        // Add Header
        Label lblUserLoginHeader = new Label("User Login");
        lblUserLoginHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserLogin.add(lblUserLoginHeader, 1,0,2,1);
        GridPane.setHalignment(lblUserLoginHeader, HPos.CENTER);
        GridPane.setMargin(lblUserLoginHeader, new Insets(20, 0,20,0));

        // Add Email Label
        Label lblUserLoginEmail = new Label("Email:");
        GridPane.setHalignment(lblUserLoginEmail, HPos.CENTER);
        gridPaneUserLogin.add(lblUserLoginEmail, 1, 1);

        // Add Email Textbox
        TextField txtUserLoginEmail = new TextField();
        GridPane.setHalignment(txtUserLoginEmail, HPos.CENTER);
        txtUserLoginEmail.setPrefHeight(40);
        gridPaneUserLogin.add(txtUserLoginEmail, 2, 1);

        // Add Password Label
        Label lblUserLoginPassword = new Label("Password:");
        GridPane.setHalignment(lblUserLoginPassword, HPos.CENTER);
        gridPaneUserLogin.add(lblUserLoginPassword, 1, 2);

        // Add Password Field
        PasswordField txtUserLoginPassword = new PasswordField();
        GridPane.setHalignment(txtUserLoginPassword, HPos.CENTER);
        txtUserLoginPassword.setPrefHeight(40);
        gridPaneUserLogin.add(txtUserLoginPassword, 2, 2);
        
        //Add a button for new users
        Button btnNewUser = new Button("New User? Click Here to Register");
        btnNewUser.setPrefHeight(40);
        btnNewUser.setPrefWidth(325);
        gridPaneUserLogin.add(btnNewUser, 1, 4, 5, 1);
        GridPane.setMargin(btnNewUser, new Insets(20, 0,20,0));
        GridPane.setHalignment(btnNewUser, HPos.CENTER);
        btnNewUser.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnNewUser.setOnAction(event -> {
            // Change scene
            primaryStage.setScene(sceneUserRegistration);
        });
        
        //Add home button
        Button btnUserHome = new Button("Click here to return to the homepage");
        btnUserHome.setPrefHeight(40);
        btnUserHome.setPrefWidth(350);
        gridPaneUserLogin.add(btnUserHome, 1, 5, 5, 1);
        GridPane.setMargin(btnUserHome, new Insets(20, 0,20,0));
        GridPane.setHalignment(btnUserHome, HPos.CENTER);
        btnUserHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserHome.setOnAction(event -> {
            // Change scene
            primaryStage.setScene(sceneHome);
        });
        
        // Add Submit Button
        Button btnSubmitUserLogin = new Button("Login");
        btnSubmitUserLogin.setPrefHeight(40);
        btnSubmitUserLogin.setDefaultButton(true);
        btnSubmitUserLogin.setPrefWidth(100);
        gridPaneUserLogin.add(btnSubmitUserLogin, 2, 3, 2, 1);
        GridPane.setHalignment(btnSubmitUserLogin, HPos.CENTER);
        GridPane.setMargin(btnSubmitUserLogin, new Insets(20, 0,20,0));
        btnSubmitUserLogin.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnSubmitUserLogin.setOnAction(event -> {
            // Validation
            if(txtUserLoginEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserLogin.getScene().getWindow(), "Form Error", "Please enter your email!");
            } else if(txtUserLoginPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserLogin.getScene().getWindow(), "Form Error", "Please enter your password!");
            } else {
                
                String email = txtUserLoginEmail.getText();
                String password = txtUserLoginPassword.getText();
                
                for(Patient patient:patientsArray){
                    if(patient.getEmail().equals(email) && patient.getPassword().equals(password))
                    userLoggedIn = email; // save user email for persistence
                }
        
                if(userLoggedIn == null) {
                    showAlert(Alert.AlertType.ERROR, gridPaneUserLogin.getScene().getWindow(), "Form Error", "User does not exist.");
                } else {
                    
                    for(Patient patient:patientsArray){
                        if(patient.getEmail().equals(userLoggedIn)) {
                            if(patient.getQuestionnaireDone() == true) {
                                showAlert(Alert.AlertType.INFORMATION, gridPaneUserLogin.getScene().getWindow(), "Success", "Welcome " + userLoggedIn + ". You have logged in.");
                                primaryStage.setScene(sceneUserHome);
                            } else {
                                txtUserLoginEmail.clear();
                                txtUserLoginPassword.clear();       
                                primaryStage.setScene(sceneUserQuestionnaire);
                            }
                        }
                    }
                    
                }
            }
        });
        
        // Add Clear Button
        Button btnClearUserLogin = new Button("Clear");
        btnClearUserLogin.setPrefHeight(40);
        btnClearUserLogin.setPrefWidth(100);
        gridPaneUserLogin.add(btnClearUserLogin, 0, 3, 2, 1);
        GridPane.setHalignment(btnClearUserLogin, HPos.CENTER);
        GridPane.setMargin(btnClearUserLogin, new Insets(20, 0,20,0));
        btnClearUserLogin.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClearUserLogin.setOnAction(event -> {
            txtUserLoginEmail.clear();
            txtUserLoginPassword.clear();        
        });

        //Styling nodes  
        gridPaneUserLogin.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        sceneUserLogin = new Scene(gridPaneUserLogin, 400, 450); 
                
        // ------------------------------------------------------------------------
        //                          USER REGISTRATION
        // ------------------------------------------------------------------------


        // Create the registration form grid pane
        GridPane gridPaneUserRegistration = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneUserRegistration.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneUserRegistration.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneUserRegistration.setHgap(10);

        // Set the vertical gap 
        gridPaneUserRegistration.setVgap(10);

        //Background colour
        gridPaneUserRegistration.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblUserRegHeader = new Label("User Registration Form");
        lblUserRegHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserRegistration.add(lblUserRegHeader, 0,0,2,1);
        GridPane.setHalignment(lblUserRegHeader, HPos.CENTER);
        GridPane.setMargin(lblUserRegHeader, new Insets(20, 0,20,0));

        // Add firstname
        Label lblUserRegFirstName = new Label("First Name:");
        gridPaneUserRegistration.add(lblUserRegFirstName, 0,1);

        // Add Name Text Field
        TextField txtFirstName = new TextField();
        txtFirstName.setPrefHeight(40);
        gridPaneUserRegistration.add(txtFirstName, 1,1);

        //Add middle name label
        Label lblUserRegMiddleName = new Label("Middle Name:");
        gridPaneUserRegistration.add(lblUserRegMiddleName, 0,2);

        //Add middle name text box
        TextField txtMiddleName = new TextField();
        txtMiddleName.setPrefHeight(40);
        gridPaneUserRegistration.add(txtMiddleName, 1,2);

        // Add surname label
        Label lblUserRegSurname = new Label("Surname:");
        gridPaneUserRegistration.add(lblUserRegSurname, 0,3);

        // Add Name textbox
        TextField txtSurname = new TextField();
        txtSurname.setPrefHeight(40);
        gridPaneUserRegistration.add(txtSurname, 1,3);

        //Mother's maiden name label
        Label lblUserRegMaidenName = new Label("Mother's Maiden Name:");
        gridPaneUserRegistration.add(lblUserRegMaidenName, 0,4);

        //Mother's maiden name textbox
        TextField txtMaidenName = new TextField();
        txtMaidenName.setPrefHeight(40);
        gridPaneUserRegistration.add(txtMaidenName, 1,4);

        // Add age label
        Label lblUserRegDOB = new Label("Date of Birth:");
        gridPaneUserRegistration.add(lblUserRegDOB, 0,5);

        // Add age textbox
        TextField txtDOB = new TextField();
        txtDOB.setPrefHeight(40);
        gridPaneUserRegistration.add(txtDOB, 1,5);

        // Add town label
        Label lblUserTown = new Label("Town:");
        gridPaneUserRegistration.add(lblUserTown, 0,6);

        // Add town textbox
        TextField txtTown = new TextField();
        txtTown.setPrefHeight(40);
        gridPaneUserRegistration.add(txtTown, 1,6);

        // Add eircode label
        Label lblUserRegEircode = new Label("Eircode:");
        gridPaneUserRegistration.add(lblUserRegEircode, 0,7);

        // Add eircode textbox
        TextField txtEircode = new TextField();
        txtEircode.setPrefHeight(40);
        gridPaneUserRegistration.add(txtEircode, 1,7);

        // Add phone label
        Label lblUserRegPhoneNumber = new Label("Phone Number:");
        gridPaneUserRegistration.add(lblUserRegPhoneNumber, 0,8);

        // Add phone textbox
        TextField txtPhoneNumber = new TextField();
        txtPhoneNumber.setPrefHeight(40);
        gridPaneUserRegistration.add(txtPhoneNumber, 1,8);

        // Add pps number label
        Label lblUserRegPPSNumber = new Label("PPS Number:");
        gridPaneUserRegistration.add(lblUserRegPPSNumber, 0,9);

        // Add pps textbox
        TextField txtPPSNumber = new TextField();
        txtPPSNumber.setPrefHeight(40);
        gridPaneUserRegistration.add(txtPPSNumber, 1,9);

        // Add Email Label
        Label lblUserRegEmail = new Label("Email:");
        gridPaneUserRegistration.add(lblUserRegEmail, 0, 10);

        // Add Email Textbox
        TextField txtEmail = new TextField();
        txtEmail.setPrefHeight(40);
        gridPaneUserRegistration.add(txtEmail, 1, 10);

        // Add Password Label
        Label lblUserRegPassword = new Label("Password:");
        gridPaneUserRegistration.add(lblUserRegPassword, 0, 11);

        // Add Password Field
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPrefHeight(40);
        gridPaneUserRegistration.add(txtPassword, 1, 11);

        // Add Submit Button
        Button btnUserRegSubmit = new Button("Next Page");
        btnUserRegSubmit.setPrefHeight(40);
        btnUserRegSubmit.setDefaultButton(true);
        btnUserRegSubmit.setPrefWidth(125);
        gridPaneUserRegistration.add(btnUserRegSubmit, 1, 12, 1, 1);
        GridPane.setHalignment(btnUserRegSubmit, HPos.CENTER);
        GridPane.setMargin(btnUserRegSubmit, new Insets(20, 0,20,0));
        btnUserRegSubmit.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserRegSubmit.setOnAction((ActionEvent event) -> {
            if(txtFirstName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your First Name!");
                event.consume();
            } else if(txtSurname.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Surname!");
                event.consume();
            } else if(txtDOB.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Date of Birth!");
                event.consume();
            } else if(txtTown.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Address!");
                event.consume();
            } else if(txtEircode.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Eircode!");
                event.consume();
            } else if(txtPhoneNumber.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Phone Number!");
                event.consume();
            } else if(txtPPSNumber.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your PPS Number!");
                event.consume();
            } else if(txtEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Email!");
                event.consume();
            } else if(txtPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "Please enter your Password!");
                event.consume();
            } else {
                Boolean userAlreadyExists = false;
                for(Patient patient:patientsArray){
                    if(patient.getEmail().equals(txtEmail.getText()))
                    userAlreadyExists = true;
                }
                if(userAlreadyExists == true) {
                    showAlert(Alert.AlertType.ERROR, gridPaneUserRegistration.getScene().getWindow(), "Form Error", "User already exists!");
                    txtEmail.clear();
                } else {
                    String userName = txtFirstName.getText() + " " + txtSurname.getText();
                    userLoggedIn = txtEmail.getText();
                    showAlert(Alert.AlertType.CONFIRMATION, gridPaneUserRegistration.getScene().getWindow(), "Registration Successful!", "Welcome " + userName + ". Please fill out the following questionnaire to determine elegibility.");
                    UserManagement.addPatient(userName, txtDOB.getText(), txtTown.getText(), txtEircode.getText(), txtPhoneNumber.getText(), txtPPSNumber.getText(), txtEmail.getText(), txtPassword.getText(), "WAITING", false);
                    txtFirstName.clear();
                    txtSurname.clear();
                    txtMaidenName.clear();
                    txtMiddleName.clear();
                    txtDOB.clear();
                    txtTown.clear();
                    txtEircode.clear();
                    txtPhoneNumber.clear();
                    txtPPSNumber.clear();
                    txtEmail.clear();
                    txtPassword.clear(); 
                    primaryStage.setScene(sceneUserQuestionnaire);
                }
            }
        });
        
        // Add Clear Button
        Button btnUserRegClear = new Button("Clear");
        btnUserRegClear.setPrefHeight(40);
        btnUserRegClear.setPrefWidth(100);
        gridPaneUserRegistration.add(btnUserRegClear, 0, 12, 1, 1);
        GridPane.setHalignment(btnUserRegClear, HPos.CENTER);
        GridPane.setMargin(btnUserRegClear, new Insets(20, 0,20,0));
        btnUserRegClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserRegClear.setOnAction(event -> {
            txtFirstName.clear();
            txtSurname.clear();
            txtMaidenName.clear();
            txtMiddleName.clear();
            txtDOB.clear();
            txtTown.clear();
            txtEircode.clear();
            txtPhoneNumber.clear();
            txtPPSNumber.clear();
            txtEmail.clear();
            txtPassword.clear();           
        });
        
        //Add home button
        Button btnUserRegBack = new Button("Click here to return to the previous page.");
        btnUserRegBack.setPrefHeight(40);
        btnUserRegBack.setPrefWidth(375);
        gridPaneUserRegistration.add(btnUserRegBack, 0, 13, 2, 1);
        GridPane.setMargin(btnUserRegBack, new Insets(20, 0,20,0));
        GridPane.setHalignment(btnUserRegBack, HPos.CENTER);
        btnUserRegBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserRegBack.setOnAction(event -> {
            txtFirstName.clear();
            txtSurname.clear();
            txtMaidenName.clear();
            txtMiddleName.clear();
            txtDOB.clear();
            txtTown.clear();
            txtEircode.clear();
            txtPhoneNumber.clear();
            txtPPSNumber.clear();
            txtEmail.clear();
            txtPassword.clear();      
            primaryStage.setScene(sceneUserLogin);
        });

        // Create a scene with registration form
        sceneUserRegistration = new Scene(gridPaneUserRegistration, 450, 700);
        
        // ------------------------------------------------------------------------
        //                          USER QUESTIONNAIRE
        // ------------------------------------------------------------------------

        // Create the registration form grid pane
        GridPane gridPaneUserQuestionnaire = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneUserQuestionnaire.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneUserQuestionnaire.setPadding(new Insets(40,40,40,40));

        // Set the horizontal gap 
        gridPaneUserQuestionnaire.setHgap(10);

        // Set the vertical gap 
        gridPaneUserQuestionnaire.setVgap(10);

        //Background colour
        gridPaneUserQuestionnaire.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblUserQuesHeader = new Label("User Questionnaire Form");
        lblUserQuesHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserQuestionnaire.add(lblUserQuesHeader, 0,0,2,1);
        GridPane.setHalignment(lblUserQuesHeader, HPos.CENTER);
        GridPane.setMargin(lblUserQuesHeader, new Insets(20, 0,20,0));

        // Add label first question
        Label lblUserQuesOne = new Label("Have you been diagnosed with COVID-19 within the last four weeks?");
        gridPaneUserQuestionnaire.add(lblUserQuesOne, 0,1);

        //Yes radio button
        RadioButton rbYesOne = new RadioButton("Yes");
        
        //No radio button
        RadioButton rbNoOne = new RadioButton("No");
        
        //toggle q1 radio buttons
        ToggleGroup radioGroup1 = new ToggleGroup();
        rbYesOne.setToggleGroup(radioGroup1);
        rbNoOne.setSelected(true);
        rbNoOne.setToggleGroup(radioGroup1);
        HBox hbox1 = new HBox(20,rbYesOne, rbNoOne);
        gridPaneUserQuestionnaire.add(hbox1, 0,2);
        
        // Add label second question
        Label lblUserQuesTwo = new Label("Have you had any other vaccines within the last 14 days?");
        gridPaneUserQuestionnaire.add(lblUserQuesTwo, 0,5);

        //Yes radio button
        RadioButton rbYesTwo = new RadioButton("Yes");
       
        //No radio button
        RadioButton rbNoTwo = new RadioButton("No");
        
        //toggle q2 radio buttons
        ToggleGroup radioGroup2 = new ToggleGroup();
        rbYesTwo.setToggleGroup(radioGroup2);
        rbNoTwo.setSelected(true);
        rbNoTwo.setToggleGroup(radioGroup2);
        HBox hbox2 = new HBox(20,rbYesTwo, rbNoTwo);
        gridPaneUserQuestionnaire.add(hbox2, 0,6);
        
        // Add label third question
        Label lblUserQuesThree = new Label("Are you pregnant?");
        gridPaneUserQuestionnaire.add(lblUserQuesThree, 0,9);

        //Yes radio button
        RadioButton rbYesThree = new RadioButton("Yes");
       
        //No radio button
        RadioButton rbNoThree = new RadioButton("No");
        
        //toggle q3 radio buttons
        ToggleGroup radioGroup3 = new ToggleGroup();
        rbYesThree.setToggleGroup(radioGroup3);
        rbNoThree.setSelected(true);
        rbNoThree.setToggleGroup(radioGroup3);
        HBox hbox3 = new HBox(20,rbYesThree, rbNoThree);
        gridPaneUserQuestionnaire.add(hbox3, 0,10);
        
        // Add label fourth question
        Label lblUserQuesFour = new Label("Have you had Anaphylaxis (serious systemic allergic reaction requiring medical intervention) "
                + "following a dose of any vaccine?");
        gridPaneUserQuestionnaire.add(lblUserQuesFour, 0,13);

        //Yes radio button
        RadioButton rbYesFour = new RadioButton("Yes");
       
        //No radio button
        RadioButton rbNoFour = new RadioButton("No");
        
        //toggle q4 radio buttons
        ToggleGroup radioGroup4 = new ToggleGroup();
        rbYesFour.setToggleGroup(radioGroup4);
        rbNoFour.setSelected(true);
        rbNoFour.setToggleGroup(radioGroup4);
        HBox hbox4 = new HBox(20,rbYesFour, rbNoFour);
        gridPaneUserQuestionnaire.add(hbox4, 0,14);
        
        // Add label fifth question
        Label lblUserQuesFive = new Label("Do you have any pre-existing health conditions?");
        gridPaneUserQuestionnaire.add(lblUserQuesFive, 0,17);

        //Yes radio button
        RadioButton rbYesFive = new RadioButton("Yes");
     
        //No radio button
        RadioButton rbNoFive = new RadioButton("No");
       
        //toggle q5 radio buttons
        ToggleGroup radioGroup5 = new ToggleGroup();
        rbYesFive.setToggleGroup(radioGroup5);
        rbNoFive.setSelected(true);
        rbNoFive.setToggleGroup(radioGroup5);
        HBox hbox5 = new HBox(20, rbYesFive, rbNoFive);
        gridPaneUserQuestionnaire.add(hbox5, 0,18);
        
        // Add label pre existing conditions
        Label lblUserQuesPre = new Label("If yes, please list your pre-existing health conditions:");
        gridPaneUserQuestionnaire.add(lblUserQuesPre, 0,21);
        
        // Add pre existing condition textbox
        TextField txtCondition = new TextField();
        txtCondition.setPrefHeight(20);
        gridPaneUserQuestionnaire.add(txtCondition, 0, 22);

        // Add Submit Button
        Button btnUserQuesSubmit = new Button("Submit");
        btnUserQuesSubmit.setPrefHeight(40);
        btnUserQuesSubmit.setDefaultButton(true);
        btnUserQuesSubmit.setPrefWidth(100);
        gridPaneUserQuestionnaire.add(btnUserQuesSubmit, 0, 23, 1, 1);
        GridPane.setHalignment(btnUserQuesSubmit, HPos.CENTER);
        GridPane.setMargin(btnUserQuesSubmit, new Insets(20, 0,20,0));
        btnUserQuesSubmit.setStyle("-fx-font: 15px arial; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserQuesSubmit.setOnAction(event -> {
            Boolean diagnosedCOVID14 = false;
            Boolean vaccine14 = false;
            Boolean pregnant = false;
            Boolean anaphylaxis = false; 
            Boolean preexistingcondition = false;
            String conditions = null;
            
            if(rbYesOne.isSelected()) {
                diagnosedCOVID14 = true;
            } else if(rbNoOne.isSelected()) {
                diagnosedCOVID14 = false;
            }
            
            if(rbYesTwo.isSelected()) {
                vaccine14 = true;
            } else if(rbNoTwo.isSelected()) {
                vaccine14 = false;
            }
            
            if(rbYesThree.isSelected()) {
                pregnant = true;
            } else if(rbNoThree.isSelected()) {
                pregnant = false;
            }
            
            if(rbYesFour.isSelected()) {
                pregnant = true;
            } else if(rbNoFour.isSelected()) {
                pregnant = false;
            }
            
            if(rbYesFive.isSelected()) {
                preexistingcondition = true;
                conditions = txtCondition.getText();
            } else if(rbNoFive.isSelected()) {
                preexistingcondition = false;
                conditions = null;
            }
            
            int searchList = patientsArray.size();
            Patient patientName = null;
            for(int i = 0; i < searchList; i++) {
                if(patientsArray.get(i).getEmail().equals(userLoggedIn)) {
                    patientName = patientsArray.get(i);
                    patientsArray.get(i).setQuestionnaireDone(true);
                }
            }
            String userName = patientName.getName();
            UserManagement.refreshPatientFile(); 
            
            UserManagement.addQuestionnaire(userName, diagnosedCOVID14, vaccine14, pregnant, anaphylaxis, preexistingcondition, conditions);
            showAlert(Alert.AlertType.INFORMATION, gridPaneUserQuestionnaire.getScene().getWindow(), "Success", "Questionnaire complete.");
            primaryStage.setScene(sceneUserHome);
        });

        // Create a scene with registration form
        sceneUserQuestionnaire = new Scene(gridPaneUserQuestionnaire, 800, 700);
        
        // ------------------------------------------------------------------------
        //                         CLINICIAN LOGIN
        // ------------------------------------------------------------------------

        //Creating a Grid Pane 
        GridPane gridPaneClinicianLogin = new GridPane();    

        //Setting size for the pane 
        gridPaneClinicianLogin.setMinSize(375, 350);

        //Setting the padding  
        gridPaneClinicianLogin.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneClinicianLogin.setVgap(10); 
        gridPaneClinicianLogin.setHgap(10);       

        //setting the alignment
        gridPaneClinicianLogin.setAlignment(Pos.CENTER);

        // Add Header
        Label lblClinicLoginHeader = new Label("Clinician Login");
        lblClinicLoginHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianLogin.add(lblClinicLoginHeader, 0,0,3,1);
        GridPane.setHalignment(lblClinicLoginHeader, HPos.CENTER);
        GridPane.setMargin(lblClinicLoginHeader, new Insets(20, 0,20,0));

        // Add Email Label
        Label lblEmailClinicLogin = new Label("Email:");
        GridPane.setHalignment(lblEmailClinicLogin, HPos.CENTER);
        gridPaneClinicianLogin.add(lblEmailClinicLogin, 1, 1);

        // Add Email Textbox
        TextField txtClinicLoginEmail = new TextField();
        GridPane.setHalignment(txtClinicLoginEmail, HPos.CENTER);
        txtClinicLoginEmail.setPrefHeight(40);
        gridPaneClinicianLogin.add(txtClinicLoginEmail, 2, 1);

        // Add Password Label
        Label lblPasswordClinicLogin = new Label("Password:");
        GridPane.setHalignment(lblPasswordClinicLogin, HPos.CENTER);
        gridPaneClinicianLogin.add(lblPasswordClinicLogin, 1, 2);

        // Add Password Field
        PasswordField txtClinicLoginPassword = new PasswordField();
        GridPane.setHalignment(txtClinicLoginPassword, HPos.CENTER);
        txtClinicLoginPassword.setPrefHeight(40);
        gridPaneClinicianLogin.add(txtClinicLoginPassword, 2, 2);

        // Add Submit Button
        Button btnSubmitClinicLogin = new Button("Submit");
        btnSubmitClinicLogin.setPrefHeight(40);
        btnSubmitClinicLogin.setDefaultButton(true);
        btnSubmitClinicLogin.setPrefWidth(100);
        GridPane.setMargin(btnSubmitClinicLogin, new Insets(20, 0,20,0));
        btnSubmitClinicLogin.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnSubmitClinicLogin.setOnAction(event -> {
            if(txtClinicLoginEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianLogin.getScene().getWindow(), "Form Error", "Please enter your email!");
            } else if(txtClinicLoginPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianLogin.getScene().getWindow(), "Form Error", "Please enter your password!");
            } else {
                String email = txtClinicLoginEmail.getText();
                String password = txtClinicLoginPassword.getText();
                
                for(Clinician clinician:cliniciansArray){
                    if(clinician.getEmail().equals(email) && clinician.getPassword().equals(password))
                    userLoggedIn = email; // save email for persistence
                }
        
                if(userLoggedIn == null) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianLogin.getScene().getWindow(), "Form Error", "User does not exist.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, gridPaneClinicianLogin.getScene().getWindow(), "Success", "Welcome " + userLoggedIn + ". You have logged in.");
                    adminLoggedIn = false;
                    clinicianLoggedIn = true;
                    primaryStage.setScene(sceneClinicianHome);
                }
            }
        });

        // Add Clear Button
        Button btnClearClinicLogin = new Button("Clear");
        btnClearClinicLogin.setPrefHeight(40);
        btnClearClinicLogin.setPrefWidth(100);
        GridPane.setMargin(btnClearClinicLogin, new Insets(20, 0,20,0));
        btnClearClinicLogin.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClearClinicLogin.setOnAction(event -> {
            txtClinicLoginEmail.clear();
            txtClinicLoginPassword.clear();        
        });
        
        // Add clear/submit hbox
        HBox hboxClinicianClearSubmit = new HBox(25);
        hboxClinicianClearSubmit.getChildren().addAll(btnClearClinicLogin, btnSubmitClinicLogin);
        gridPaneClinicianLogin.add(hboxClinicianClearSubmit, 1, 3, 3, 1);
        GridPane.setMargin(hboxClinicianClearSubmit, new Insets(10, 0, 10, 0));
        
        //Add home button
        Button btnClinicianHome = new Button("Return to the homepage");
        btnClinicianHome.setPrefHeight(40);
        btnClinicianHome.setPrefWidth(250);
        gridPaneClinicianLogin.add(btnClinicianHome, 1, 4, 3, 1);
        GridPane.setMargin(btnClinicianHome, new Insets(20, 0,20,0));
        btnClinicianHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinicianHome.setOnAction(event -> {
            primaryStage.setScene(sceneHome);
        });
        
        //Styling nodes  
        gridPaneClinicianLogin.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        sceneClinicianLogin = new Scene(gridPaneClinicianLogin, 375, 350); 
 
        // ------------------------------------------------------------------------
        //                          ADMINISTRATOR LOGIN
        // ------------------------------------------------------------------------

        //Creating a Grid Pane 
        GridPane gridPaneAdminLogin = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminLogin.setMinSize(375, 350);

        //Setting the padding  
        gridPaneAdminLogin.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminLogin.setVgap(10); 
        gridPaneAdminLogin.setHgap(10);       

        //setting the alignment
        gridPaneAdminLogin.setAlignment(Pos.CENTER);

        // Add Header
        Label lblAdminLoginHeader = new Label("Administrator Login");
        lblAdminLoginHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminLogin.add(lblAdminLoginHeader, 0,0,3,1);
        GridPane.setHalignment(lblAdminLoginHeader, HPos.CENTER);
        GridPane.setMargin(lblAdminLoginHeader, new Insets(20, 0,20,0));


        // Add Email Label
        Label lblAdminLoginName = new Label("Organisation Name:");
        gridPaneAdminLogin.add(lblAdminLoginName, 1, 1);

        // Add Email Textbox
        TextField txtAdminLoginEmail = new TextField();
        txtAdminLoginEmail.setPrefHeight(40);
        gridPaneAdminLogin.add(txtAdminLoginEmail, 2, 1);

        // Add Password Label
        Label lblAdminLoginPassword = new Label("Password:");
        gridPaneAdminLogin.add(lblAdminLoginPassword, 1, 2);

        // Add Password Field
        PasswordField txtAdminLoginPassword = new PasswordField();
        txtAdminLoginPassword.setPrefHeight(40);
        gridPaneAdminLogin.add(txtAdminLoginPassword, 2, 2);

        // Add Submit Button
        Button btnAdminLoginSubmit = new Button("Submit");
        btnAdminLoginSubmit.setPrefHeight(40);
        btnAdminLoginSubmit.setDefaultButton(true);
        btnAdminLoginSubmit.setPrefWidth(100);
        GridPane.setMargin(btnAdminLoginSubmit, new Insets(20, 0,20,0));
        btnAdminLoginSubmit.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminLoginSubmit.setOnAction(event -> {
            if(txtAdminLoginEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminLogin.getScene().getWindow(), "Form Error", "Please enter your email!");
            } else if(txtAdminLoginPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminLogin.getScene().getWindow(), "Form Error", "Please enter your password!");
            } else {
                String name = txtAdminLoginEmail.getText();
                String password = txtAdminLoginPassword.getText();
                
                for(Administrator admin:administrationArray){
                    if(admin.getName().equals(name) && admin.getPassword().equals(password))
                    userLoggedIn = name; // save email for persistence
                }
        
                if(userLoggedIn == null) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminLogin.getScene().getWindow(), "Form Error", "User does not exist.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, gridPaneAdminLogin.getScene().getWindow(), "Success", "Welcome " + userLoggedIn + ". You have logged in.");
                    adminLoggedIn = true;
                    clinicianLoggedIn = false;
                    primaryStage.setScene(sceneAdminHome);
                }
            }
        });

        // Add Clear Button
        Button btnAdminLoginClear = new Button("Clear");
        btnAdminLoginClear.setPrefHeight(40);
        btnAdminLoginClear.setPrefWidth(100);
        GridPane.setMargin(btnAdminLoginClear, new Insets(20, 0,20,0));
        btnAdminLoginClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminLoginClear.setOnAction(event -> {
            txtAdminLoginEmail.clear();
            txtAdminLoginPassword.clear();        
        });
        
        // Add clear/submit hbox
        HBox hboxAdminClearSubmit = new HBox(25);
        hboxAdminClearSubmit.getChildren().addAll(btnAdminLoginClear, btnAdminLoginSubmit);
        gridPaneAdminLogin.add(hboxAdminClearSubmit, 1, 3, 3, 1);
        GridPane.setMargin(hboxAdminClearSubmit, new Insets(10, 0, 10, 0));
        
        //Add home button
        Button btnAdminLoginHome = new Button("Return to the homepage");
        btnAdminLoginHome.setPrefHeight(40);
        btnAdminLoginHome.setPrefWidth(250);
        gridPaneAdminLogin.add(btnAdminLoginHome, 1, 4, 2, 1);
        GridPane.setMargin(btnAdminLoginHome, new Insets(20, 0,20,0));
        btnAdminLoginHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminLoginHome.setOnAction(event -> {
            txtAdminLoginEmail.clear();
            txtAdminLoginPassword.clear(); 
            primaryStage.setScene(sceneHome);
        });
        
        //Styling nodes  
        gridPaneAdminLogin.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        scenePhaLogin = new Scene(gridPaneAdminLogin, 400, 350); 

        /* ------------------------------------------------------------------------
                                    USER HOME PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneUserHome = new GridPane();    

        //Setting size for the pane 
        gridPaneUserHome.setMinSize(400, 250);

        //Setting the padding  
        gridPaneUserHome.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneUserHome.setVgap(10); 
        gridPaneUserHome.setHgap(2);       

        //setting the alignment
        gridPaneUserHome.setAlignment(Pos.CENTER);

        //Add header  
        Label lblUserSelectOption = new Label("Welcome user.\nPlease Select an Option:");
        lblUserSelectOption.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserHome.add(lblUserSelectOption, 0, 0, 2, 1);
        GridPane.setHalignment(lblUserSelectOption, HPos.CENTER);
        GridPane.setMargin(lblUserSelectOption, new Insets(10, 0, 20, 0));
        
        // Add vaccine appointment button
        Button btnUserSchedule = new Button("Schedule a Vaccine Appointment");
        btnUserSchedule.setPrefHeight(40);
        btnUserSchedule.setPrefWidth(210);
        gridPaneUserHome.add(btnUserSchedule, 0, 1, 2, 1);
        GridPane.setHalignment(btnUserSchedule, HPos.CENTER);
        GridPane.setMargin(btnUserSchedule, new Insets(0, 0,20,0));
        btnUserSchedule.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserSchedule.setOnAction(event -> {
            primaryStage.setScene(sceneUserBookAppointment);
        });

        // Add view appointments button
        Button btnUserAppointment = new Button("View Scheduled Appointments");
        btnUserAppointment.setPrefHeight(40);
        btnUserAppointment.setPrefWidth(210);
        gridPaneUserHome.add(btnUserAppointment, 0, 2, 2, 1);
        GridPane.setHalignment(btnUserAppointment, HPos.CENTER);
        GridPane.setMargin(btnUserAppointment, new Insets(20, 0,20,0));
        btnUserAppointment.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserAppointment.setOnAction(event -> {
            populateUserAppointmentsTable(tblUserViewAppointments);
            primaryStage.setScene(sceneUserViewAppointments);
        });

        // Add view edit profile button
        Button btnUserEditProfile = new Button("Edit your profile");
        btnUserEditProfile.setPrefHeight(40);
        btnUserEditProfile.setPrefWidth(210);
        gridPaneUserHome.add(btnUserEditProfile, 0, 3, 2, 1);
        GridPane.setHalignment(btnUserEditProfile, HPos.CENTER);
        GridPane.setMargin(btnUserEditProfile, new Insets(20, 0,20,0));
        btnUserEditProfile.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserEditProfile.setOnAction(event -> {
            populateUserEditProfile(txtUserEditProfileName, txtUserEditProfileEmail, txtUserEditProfilePassword, txtUserEditProfileTown, txtUserEditProfileEircode, txtUserEditProfileNumber);
            primaryStage.setScene(sceneUserEditProfile);
        });
        
        // Add view vaccine information button
        Button btnUserVaccineInformation = new Button("View Vaccine Information");
        btnUserVaccineInformation.setPrefHeight(40);
        btnUserVaccineInformation.setPrefWidth(210);
        gridPaneUserHome.add(btnUserVaccineInformation, 0, 4, 2, 1);
        GridPane.setHalignment(btnUserVaccineInformation, HPos.CENTER);
        GridPane.setMargin(btnUserVaccineInformation, new Insets(20, 0,20,0));
        btnUserVaccineInformation.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserVaccineInformation.setOnAction(event -> {
            primaryStage.setScene(sceneUserViewVaccineInfo);
        });
        
        // Add logout button
        Button btnUserLogout = new Button("Logout of Vaccine System");
        btnUserLogout.setPrefHeight(40);
        btnUserLogout.setDefaultButton(true);
        btnUserLogout.setPrefWidth(210);
        gridPaneUserHome.add(btnUserLogout, 0, 5, 2, 1);
        GridPane.setHalignment(btnUserLogout, HPos.CENTER);
        GridPane.setMargin(btnUserLogout, new Insets(20, 0,20,0));
        btnUserLogout.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserLogout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close this application?", ButtonType.YES, ButtonType.NO);

            // clicking X also means no
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                System.exit(0);
            }
        });

        //Styling nodes  
        gridPaneUserHome.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        sceneUserHome = new Scene(gridPaneUserHome, 375, 600);
        
        /* ------------------------------------------------------------------------
                            USER BOOK APPOINTMENT PAGE
           ------------------------------------------------------------------------ */
        
        // Create the registration form grid pane
        GridPane gridPaneUserBookAppointment = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneUserBookAppointment.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneUserBookAppointment.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneUserBookAppointment.setHgap(10);

        // Set the vertical gap 
        gridPaneUserBookAppointment.setVgap(10);

        //Background colour
        gridPaneUserBookAppointment.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblBookAppointmentHeader = new Label("Book a vaccine appointment");
        lblBookAppointmentHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserBookAppointment.add(lblBookAppointmentHeader, 0,0,2,1);
        GridPane.setHalignment(lblBookAppointmentHeader, HPos.CENTER);
        GridPane.setMargin(lblBookAppointmentHeader, new Insets(20, 0,20,0));
        

        // Add date label
        Label lblBookAppointmentDate = new Label("Pick an appointment date:");
        gridPaneUserBookAppointment.add(lblBookAppointmentDate, 0,3);

        // Add datebox
        DatePicker appointmentDate = new DatePicker();
        gridPaneUserBookAppointment.add(appointmentDate, 1, 3);
        appointmentDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        
        // Add time label
        Label lblBookAppointmentTime = new Label("Pick an appointment time:");
        gridPaneUserBookAppointment.add(lblBookAppointmentTime, 0,4);
        
        // Add time combobox
        ComboBox cboAppointmentTime = new ComboBox();
        gridPaneUserBookAppointment.add(cboAppointmentTime, 1,4);
        cboAppointmentTime.getItems().addAll("8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00");
        
        // Add location label
        Label lblBookAppointmentLocation = new Label("Pick an appointment location:");
        gridPaneUserBookAppointment.add(lblBookAppointmentLocation, 0,5);
        
        // Add time combobox
        ComboBox cboBookAppointmentLocation = new ComboBox();
        gridPaneUserBookAppointment.add(cboBookAppointmentLocation, 1,5);
        int searchUserVaccineCentreList = vaccineCentresArray.size();
        for(int i = 0; i < searchUserVaccineCentreList; i++) {
            cboBookAppointmentLocation.getItems().add(vaccineCentresArray.get(i).getVcName());
        }
        
        // Add dose label
        Label lblBookAppointmentDose = new Label("Have you already received your first dose?");
        gridPaneUserBookAppointment.add(lblBookAppointmentDose, 0,6);
        
        // Add dose checkbox
        CheckBox cbBookAppointmentDose = new CheckBox("I've received my first dose.");
        gridPaneUserBookAppointment.add(cbBookAppointmentDose, 1,6);
        
        // Add Submit Button
        Button btnUserBookAppointment = new Button("Book Appointment");
        btnUserBookAppointment.setPrefHeight(40);
        btnUserBookAppointment.setDefaultButton(true);
        btnUserBookAppointment.setPrefWidth(200);
        gridPaneUserBookAppointment.add(btnUserBookAppointment, 1, 7, 1, 1);
        GridPane.setHalignment(btnUserBookAppointment, HPos.CENTER);
        GridPane.setMargin(btnUserBookAppointment, new Insets(20, 0,20,0));
        btnUserBookAppointment.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        
        // Add Clear Button
        Button btnUserAppointmentClear = new Button("Clear");
        btnUserAppointmentClear.setPrefHeight(40);
        btnUserAppointmentClear.setPrefWidth(200);
        gridPaneUserBookAppointment.add(btnUserAppointmentClear, 0, 7, 1, 1);
        GridPane.setHalignment(btnUserAppointmentClear, HPos.CENTER);
        GridPane.setMargin(btnUserAppointmentClear, new Insets(20, 0,20,0));
        btnUserAppointmentClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserAppointmentClear.setOnAction(event -> {
            appointmentDate.getEditor().clear();
            cboAppointmentTime.setValue(null);
            cboBookAppointmentLocation.setValue(null);
            cbBookAppointmentDose.setSelected(false);
        });
        
        //Add home button
        Button btnUserAppointmentHome = new Button("Click here to return to the previous page.");
        btnUserAppointmentHome.setPrefHeight(40);
        btnUserAppointmentHome.setPrefWidth(400);
        gridPaneUserBookAppointment.add(btnUserAppointmentHome, 0, 8, 2, 1);
        GridPane.setHalignment(btnUserAppointmentHome, HPos.CENTER);
        GridPane.setMargin(btnUserAppointmentHome, new Insets(20, 0,20,0));
        btnUserAppointmentHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserAppointmentHome.setOnAction(event -> {
            primaryStage.setScene(sceneUserHome);
        });
        
        // Book appointment action
        btnUserBookAppointment.setOnAction((ActionEvent event) -> {
            
            // Validation
            if(appointmentDate.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserBookAppointment.getScene().getWindow(), "Form Error", "Please pick a date!");
            } else if(cboAppointmentTime.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserBookAppointment.getScene().getWindow(), "Form Error", "Please pick a time!");
            } else if(cboBookAppointmentLocation.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserBookAppointment.getScene().getWindow(), "Form Error", "Please pick an appointment location!");
            } else { 
            
                String strUserAppointmentName = null;

                int searchList = patientsArray.size();
                for(int i = 0; i < searchList; i++) {
                    if(patientsArray.get(i).getEmail().equals(userLoggedIn)) {
                        strUserAppointmentName = patientsArray.get(i).getName();
                        patientsArray.get(i).setStatus("SCHEDULED");
                        UserManagement.refreshPatientFile();
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, gridPaneUserBookAppointment.getScene().getWindow(), "Registration Successful!", "Thanks for booking an appointment, " + strUserAppointmentName);

                int vaccineDoseNumber = 1;

                if(cbBookAppointmentDose.isSelected() == true) {
                    vaccineDoseNumber = 2;
                }
                // Format date to save in textfile
                String date = appointmentDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                AppointmentManagement.addAppointment(userLoggedIn, strUserAppointmentName, date, cboAppointmentTime.getValue().toString(), cboBookAppointmentLocation.getValue().toString(), null, vaccineDoseNumber, false, "PENDING");
                        
                primaryStage.setScene(sceneUserHome);
            }  
            
        });
        
        // Create a scene object
        sceneUserBookAppointment = new Scene(gridPaneUserBookAppointment, 550, 500);
        
        /* ------------------------------------------------------------------------
                            USER VIEW APPOINTMENTS PAGE
           ------------------------------------------------------------------------ */
        
        // Create the registration form grid pane
        GridPane gridPaneUserViewAppointments = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneUserViewAppointments.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneUserViewAppointments.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneUserViewAppointments.setHgap(10);

        // Set the vertical gap 
        gridPaneUserViewAppointments.setVgap(10);

        //Background colour
        gridPaneUserViewAppointments.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblViewAppointmentsHeader = new Label("View your booked appointments.");
        lblViewAppointmentsHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserViewAppointments.add(lblViewAppointmentsHeader, 0,0,5,1);
        GridPane.setHalignment(lblViewAppointmentsHeader, HPos.CENTER);
        GridPane.setMargin(lblViewAppointmentsHeader, new Insets(20, 0, 20, 0));
        
        // Create table
        TableColumn<Appointment, String> colUserEmail = new TableColumn<>("Email");
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("appointmentEmail"));
        TableColumn<Appointment, String> colUserName = new TableColumn<>("Name");
        colUserName.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        TableColumn<Appointment, String> colUserDate = new TableColumn<>("Date");
        colUserDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        TableColumn<Appointment, String> colUserTime = new TableColumn<>("Time");
        colUserTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        TableColumn<Appointment, String> colUserLocation = new TableColumn<>("Location");
        colUserLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        TableColumn<Appointment, Integer> colUserDose = new TableColumn<>("Dose No.");
        colUserDose.setCellValueFactory(new PropertyValueFactory<>("doseNo"));
        TableColumn<Appointment, Boolean> colUserApptStatus = new TableColumn<>("Status");
        colUserApptStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        
        // Add columns to table
        tblUserViewAppointments.getColumns().add(colUserEmail);
        tblUserViewAppointments.getColumns().add(colUserName);
        tblUserViewAppointments.getColumns().add(colUserDate);
        tblUserViewAppointments.getColumns().add(colUserTime);
        tblUserViewAppointments.getColumns().add(colUserLocation);
        tblUserViewAppointments.getColumns().add(colUserDose);
        tblUserViewAppointments.getColumns().add(colUserApptStatus);
        
        // Add table to gridpane
        tblUserViewAppointments.setPrefWidth(850);
        gridPaneUserViewAppointments.add(tblUserViewAppointments, 0, 1, 5, 1);
        GridPane.setHalignment(tblUserViewAppointments, HPos.CENTER);
        
        // Add delete appointment button
        Button btnUserViewAppointmentsDelete = new Button("Click here to cancel selected appointment");
        btnUserViewAppointmentsDelete.setPrefHeight(40);
        btnUserViewAppointmentsDelete.setPrefWidth(400);
        gridPaneUserViewAppointments.add(btnUserViewAppointmentsDelete, 0, 2, 5, 1);
        GridPane.setHalignment(btnUserViewAppointmentsDelete, HPos.CENTER);
        GridPane.setMargin(btnUserViewAppointmentsDelete, new Insets(20, 0,20,0));
        btnUserViewAppointmentsDelete.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        
        // Add delete appointment button action
        btnUserViewAppointmentsDelete.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to cancel this appointment?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblUserViewAppointments.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                    
                    Appointment appointmentRow = tblUserViewAppointments.getItems().get(selectedIndex);
                    int searchList = appointmentsArray.size();
                    for(int i = 0; i < searchList; i++) {
                        if(appointmentsArray.get(i) == appointmentRow) {
                            appointmentsArray.get(i).setTaskStatus("CANCELLED");
                            appointmentsArray.get(i).setNoShow(true);
                        }
                    }
                    tblUserViewAppointments.getItems().clear();
                    AppointmentManagement.refreshAppointmentFile();
                    populateUserAppointmentsTable(tblUserViewAppointments);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneUserViewAppointments.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        //Add home button
        Button btnUserViewAppointmentsBack = new Button("Click here to return to the previous page.");
        btnUserViewAppointmentsBack.setPrefHeight(40);
        btnUserViewAppointmentsBack.setDefaultButton(true);
        btnUserViewAppointmentsBack.setPrefWidth(400);
        gridPaneUserViewAppointments.add(btnUserViewAppointmentsBack, 0, 3, 5, 1);
        GridPane.setHalignment(btnUserViewAppointmentsBack, HPos.CENTER);
        GridPane.setMargin(btnUserViewAppointmentsBack, new Insets(20, 0,20,0));
        btnUserViewAppointmentsBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserViewAppointmentsBack.setOnAction(event -> {
            primaryStage.setScene(sceneUserHome);
        });
        
        // Creating scene object
        sceneUserViewAppointments = new Scene(gridPaneUserViewAppointments, 775, 500);
        
        /* ------------------------------------------------------------------------
                            USER EDIT PROFILE PAGE
           ------------------------------------------------------------------------ */
        
        // Create the registration form grid pane
        GridPane gridPaneUserEditProfile = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneUserEditProfile.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneUserEditProfile.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneUserEditProfile.setHgap(15);

        // Set the vertical gap 
        gridPaneUserEditProfile.setVgap(10);

        //Background colour
        gridPaneUserEditProfile.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblEditUserProfileHeader = new Label("Edit Your Profile");
        lblEditUserProfileHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneUserEditProfile.add(lblEditUserProfileHeader, 0,0,3,1);
        GridPane.setHalignment(lblEditUserProfileHeader, HPos.CENTER);
        GridPane.setMargin(lblEditUserProfileHeader, new Insets(20, 0, 20, 0));
        
        // Name Label
        Label lblEditUserProfileName = new Label("Name:");
        gridPaneUserEditProfile.add(lblEditUserProfileName, 0, 1);
        
        // Name TextField
        txtUserEditProfileName = new TextField();
        txtUserEditProfileName.setPrefHeight(40);
        txtUserEditProfileName.setDisable(true);
        gridPaneUserEditProfile.add(txtUserEditProfileName, 1, 1);
        
        // Email Label
        Label lblEditUserProfileEmail = new Label("Email:");
        gridPaneUserEditProfile.add(lblEditUserProfileEmail, 0, 2);
        
        // Email TextField
        txtUserEditProfileEmail = new TextField();
        txtUserEditProfileEmail.setPrefHeight(40);
        gridPaneUserEditProfile.add(txtUserEditProfileEmail, 1, 2);
        
        // Password Label
        Label lblEditUserProfilePassword = new Label("Password:");
        gridPaneUserEditProfile.add(lblEditUserProfilePassword, 0, 3);
        
        // Password TextField
        txtUserEditProfilePassword = new TextField();
        txtUserEditProfilePassword.setPrefHeight(40);
        gridPaneUserEditProfile.add(txtUserEditProfilePassword, 1, 3);
        
        // Town Label
        Label lblEditUserProfileTown = new Label("Town:");
        gridPaneUserEditProfile.add(lblEditUserProfileTown, 0, 4);
        
        // Town TextField
        txtUserEditProfileTown = new TextField();
        txtUserEditProfileTown.setPrefHeight(40);
        gridPaneUserEditProfile.add(txtUserEditProfileTown, 1, 4);
        
        // Eircode Label
        Label lblEditUserProfileEircode = new Label("Eircode:");
        gridPaneUserEditProfile.add(lblEditUserProfileEircode, 0, 5);
        
        // Eircode TextField
        txtUserEditProfileEircode = new TextField();
        txtUserEditProfileEircode.setPrefHeight(40);
        gridPaneUserEditProfile.add(txtUserEditProfileEircode, 1, 5);
        
        // Number Label
        Label lblEditUserProfileNumber = new Label("Phone Number:");
        gridPaneUserEditProfile.add(lblEditUserProfileNumber, 0, 6);
        
        // Number TextField
        txtUserEditProfileNumber = new TextField();
        txtUserEditProfileNumber.setPrefHeight(40);
        gridPaneUserEditProfile.add(txtUserEditProfileNumber, 1, 6);
        
        // Add Update Button
        Button btnUserSubmitEditProfile = new Button("Update Profile");
        btnUserSubmitEditProfile.setPrefHeight(40);
        btnUserSubmitEditProfile.setPrefWidth(300);
        gridPaneUserEditProfile.add(btnUserSubmitEditProfile, 0, 7, 2, 1);
        GridPane.setHalignment(btnUserSubmitEditProfile, HPos.CENTER);
        GridPane.setMargin(btnUserSubmitEditProfile, new Insets(20, 0,20,0));
        btnUserSubmitEditProfile.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserSubmitEditProfile.setOnAction((ActionEvent event) -> {
            // Validation
            if(txtUserEditProfileEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserEditProfile.getScene().getWindow(), "Form Error", "Please enter an email!");
            } else if(txtUserEditProfilePassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserEditProfile.getScene().getWindow(), "Form Error", "Please pick a password!");
            } else if(txtUserEditProfileTown.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserEditProfile.getScene().getWindow(), "Form Error", "Please enter your town!");
            } else if(txtUserEditProfileEircode.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserEditProfile.getScene().getWindow(), "Form Error", "Please enter an eircode!");
            } else if(txtUserEditProfileNumber.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneUserEditProfile.getScene().getWindow(), "Form Error", "Please enter a number!");
            } else {
                patientsArray.forEach((Patient patient) -> {
                    if(patient.getEmail().equals(userLoggedIn)) {
                        patient.setEmail(txtUserEditProfileEmail.getText());
                        patient.setPassword(txtUserEditProfilePassword.getText());
                        patient.setTown(txtUserEditProfileTown.getText());
                        patient.setEircode(txtUserEditProfileEircode.getText());
                        patient.setNumber(txtUserEditProfileNumber.getText());
                        UserManagement.refreshPatientFile();
                    }
                });
                
                appointmentsArray.forEach((Appointment appointment) -> {
                    if(appointment.getAppointmentEmail().equals(userLoggedIn)) {
                        appointment.setAppointmentEmail(txtUserEditProfileEmail.getText());
                        AppointmentManagement.refreshAppointmentFile();
                    }
                });
                
                userLoggedIn = txtUserEditProfileEmail.getText();
                populateUserAppointmentsTable(tblUserViewAppointments);
                showAlert(Alert.AlertType.INFORMATION, gridPaneUserEditProfile.getScene().getWindow(), "Update Successful!", "Profile updated successfully. Please remember your details!");
                primaryStage.setScene(sceneUserHome);  
            }
        });
        
        
        // Add home button
        Button btnUserSubmitProfileCancel = new Button("Cancel and Return");
        btnUserSubmitProfileCancel.setPrefHeight(40);
        btnUserSubmitProfileCancel.setDefaultButton(true);
        btnUserSubmitProfileCancel.setPrefWidth(300);
        gridPaneUserEditProfile.add(btnUserSubmitProfileCancel, 0, 8, 2, 1);
        GridPane.setHalignment(btnUserSubmitProfileCancel, HPos.CENTER);
        GridPane.setMargin(btnUserSubmitProfileCancel, new Insets(20, 0,20,0));
        btnUserSubmitProfileCancel.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserSubmitProfileCancel.setOnAction(event -> {
            primaryStage.setScene(sceneUserHome);
        });
        
        // Create a scene object
        sceneUserEditProfile = new Scene(gridPaneUserEditProfile, 400, 500);
        
        /* ------------------------------------------------------------------------
                         USER VACCINE INFO AND INFECTIOUS DISEASES PAGE
           ------------------------------------------------------------------------ */
        
        // Add gridpane
        GridPane GridPaneVaccineInfo = new GridPane();
        GridPaneVaccineInfo.setAlignment(Pos.CENTER);
        
        // Set a padding 
        GridPaneVaccineInfo.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        GridPaneVaccineInfo.setHgap(15);

        // Set the vertical gap 
        GridPaneVaccineInfo.setVgap(10);

        //Background colour
        GridPaneVaccineInfo.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add header
        Label lblUserVaccineInfoHeader = new Label("Vaccine Administration - Vaccine Information");
        GridPaneVaccineInfo.add(lblUserVaccineInfoHeader, 0, 0, 2, 1);
        lblUserVaccineInfoHeader.setFont(new Font("Verdana", 30));
        
        // Combobox label
        Label lblUserCboVaccines = new Label("Select a vaccine:");
        GridPaneVaccineInfo.add(lblUserCboVaccines, 0, 1, 1, 1);
        
        // Combobox for patients
        ComboBox cboUserVaccines = new ComboBox();
        vaccinesArray.forEach((Vaccine vaccine) -> {cboUserVaccines.getItems().add(vaccine.getVaccineName());});
        GridPaneVaccineInfo.add(cboUserVaccines, 1, 1, 1, 1);
        
        // Add Description Label
        Label lblUserVaccineDescription = new Label("Vaccine Description:");
        GridPaneVaccineInfo.add(lblUserVaccineDescription, 0, 2);
        GridPane.setHalignment(lblUserVaccineDescription, HPos.CENTER);

        // Add Description Textbox
        TextArea txtUserVaccineDescription = new TextArea();
        txtUserVaccineDescription.setPrefRowCount(2);
        txtUserVaccineDescription.setEditable(false);
        txtUserVaccineDescription.setWrapText(true);
        GridPaneVaccineInfo.add(txtUserVaccineDescription, 1, 2);
        
        // Populate Textbox
        cboUserVaccines.setOnAction(event -> {
            vaccinesArray.forEach((Vaccine vaccine) -> {
                if(vaccine.getVaccineName().equals(cboUserVaccines.getValue())) {
                    txtUserVaccineDescription.setText(vaccine.getVaccineDescription());
                }
            });
        });
        
        // Add button
        Button btnLinkDisease = new Button("CLICK HERE to learn more about infectious diseases");
        btnLinkDisease.setPrefHeight(40);
        btnLinkDisease.setPrefWidth(400);
        GridPaneVaccineInfo.add(btnLinkDisease, 0, 3, 2, 1);
        GridPane.setHalignment(btnLinkDisease, HPos.CENTER);
        GridPane.setMargin(btnLinkDisease, new Insets(20, 0,20,0));
        btnLinkDisease.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        
        // Button action
        btnLinkDisease.setOnAction(event -> {
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                URI oURL = new URI("https://www.hpsc.ie/topicsa-z/a/");
                desktop.browse(oURL);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });
        
        // Add button
        Button btnLinkVaccines = new Button("CLICK HERE to learn more about the COVID vaccines");
        btnLinkVaccines.setPrefHeight(40);
        btnLinkVaccines.setPrefWidth(400);
        GridPaneVaccineInfo.add(btnLinkVaccines, 0, 4, 2, 1);
        GridPane.setHalignment(btnLinkVaccines, HPos.CENTER);
        btnLinkVaccines.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        
        // Button action
        btnLinkVaccines.setOnAction(event -> {
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                URI oURL = new URI("http://www.hpra.ie/homepage/medicines/covid-19-updates/approval-of-covid-19-vaccines");
                desktop.browse(oURL);
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });
        
        // Add home button
        Button btnUserViewVaccineInfoHome = new Button("Return to Home Page");
        btnUserViewVaccineInfoHome.setPrefHeight(40);
        btnUserViewVaccineInfoHome.setPrefWidth(300);
        btnUserViewVaccineInfoHome.setDefaultButton(true);
        GridPaneVaccineInfo.add(btnUserViewVaccineInfoHome, 0, 5, 2, 1);
        GridPane.setHalignment(btnUserViewVaccineInfoHome, HPos.CENTER);
        GridPane.setMargin(btnUserViewVaccineInfoHome, new Insets(20, 0,20,0));
        btnUserViewVaccineInfoHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnUserViewVaccineInfoHome.setOnAction(event -> {
            primaryStage.setScene(sceneUserHome);
        });
        
        // Create a scene object
        sceneUserViewVaccineInfo = new Scene(GridPaneVaccineInfo, 800, 350);
        
        /* ------------------------------------------------------------------------
                                    CLINICIAN HOME PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneClinicianHome = new GridPane();    

        //Setting size for the pane 
        gridPaneClinicianHome.setMinSize(400, 250);

        //Setting the padding  
        gridPaneClinicianHome.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneClinicianHome.setVgap(10); 
        gridPaneClinicianHome.setHgap(2);       

        //setting the alignment
        gridPaneClinicianHome.setAlignment(Pos.CENTER);

        //Add header  
        Label lblClinicianSelectOption = new Label("Welcome clinician.\nPlease Select an Option:");
        lblClinicianSelectOption.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianHome.add(lblClinicianSelectOption, 0, 0, 2, 1);
        GridPane.setHalignment(lblClinicianSelectOption, HPos.CENTER);
        GridPane.setMargin(lblClinicianSelectOption, new Insets(10, 0, 20, 0));
        
        // Add view patients button
        Button btnClinicianViewPatients = new Button("View Patients");
        btnClinicianViewPatients.setPrefHeight(40);
        btnClinicianViewPatients.setPrefWidth(210);
        gridPaneClinicianHome.add(btnClinicianViewPatients, 0, 1, 2, 1);
        GridPane.setHalignment(btnClinicianViewPatients, HPos.CENTER);
        GridPane.setMargin(btnClinicianViewPatients, new Insets(0, 0,20,0));
        btnClinicianViewPatients.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinicianViewPatients.setOnAction(event -> {
            primaryStage.setScene(sceneViewPatients);
        });
        
        // Add view questionnaires button
        Button btnClinicianViewQuestionnaires = new Button("View Questionnaires");
        btnClinicianViewQuestionnaires.setPrefHeight(40);
        btnClinicianViewQuestionnaires.setPrefWidth(210);
        gridPaneClinicianHome.add(btnClinicianViewQuestionnaires, 0, 2, 2, 1);
        GridPane.setHalignment(btnClinicianViewQuestionnaires, HPos.CENTER);
        GridPane.setMargin(btnClinicianViewQuestionnaires, new Insets(20, 0,20,0));
        btnClinicianViewQuestionnaires.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinicianViewQuestionnaires.setOnAction(event -> {
            primaryStage.setScene(sceneViewQuestionnaires);
        });
        
        // Add view appointments button
        Button btnClinicianViewAppointments = new Button("View Appointments");
        btnClinicianViewAppointments.setPrefHeight(40);
        btnClinicianViewAppointments.setPrefWidth(210);
        gridPaneClinicianHome.add(btnClinicianViewAppointments, 0, 3, 2, 1);
        GridPane.setHalignment(btnClinicianViewAppointments, HPos.CENTER);
        GridPane.setMargin(btnClinicianViewAppointments, new Insets(20, 0,20,0));
        btnClinicianViewAppointments.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinicianViewAppointments.setOnAction(event -> {
            primaryStage.setScene(sceneViewAppointments);
        });
        
        // Add logout button
        Button btnClinicianLogout = new Button("Logout of Vaccine System");
        btnClinicianLogout.setPrefHeight(40);
        btnClinicianLogout.setDefaultButton(true);
        btnClinicianLogout.setPrefWidth(210);
        gridPaneClinicianHome.add(btnClinicianLogout, 0, 4, 2, 1);
        GridPane.setHalignment(btnClinicianLogout, HPos.CENTER);
        GridPane.setMargin(btnClinicianLogout, new Insets(20, 0,20,0));
        btnClinicianLogout.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinicianLogout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close this application?", ButtonType.YES, ButtonType.NO);

            // clicking X also means no
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                System.exit(0);
            }
        });

        //Styling nodes  
        gridPaneClinicianHome.setStyle("-fx-background-color: #f0f5f5;"); 

        // Creating a scene object 
        sceneClinicianHome = new Scene(gridPaneClinicianHome, 375, 500);
    
    /* ------------------------------------------------------------------------
                            CLINICIAN VIEW PATIENTS PAGE
        ------------------------------------------------------------------------ */
        // Create the patient view grid pane
        GridPane gridPaneClinicianViewPatients = new GridPane();

        // Position the pane at the middle of the screen
        gridPaneClinicianViewPatients.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneClinicianViewPatients.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneClinicianViewPatients.setHgap(10);

        // Set the vertical gap 
        gridPaneClinicianViewPatients.setVgap(5);

        //Background colour
        gridPaneClinicianViewPatients.setStyle("-fx-background-color: #f0f5f5;");

        // Add Header
        Label lblClinViewPatientsHeader = new Label("View Patients");
        lblClinViewPatientsHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianViewPatients.add(lblClinViewPatientsHeader, 0, 0, 5, 1);
        GridPane.setHalignment(lblClinViewPatientsHeader, HPos.CENTER);
        GridPane.setMargin(lblClinViewPatientsHeader, new Insets(20, 0, 20, 0));
        
        // Combobox label
        Label lblClinCboPatients = new Label("Select a patient:");
        gridPaneClinicianViewPatients.add(lblClinCboPatients, 0, 1, 5, 1);
        
        // Combobox for patients
        ComboBox cboPatients = new ComboBox();
        patientsArray.forEach((Patient patients) -> {cboPatients.getItems().add(patients.getName());});
        gridPaneClinicianViewPatients.add(cboPatients, 1, 1, 5, 1);
        
        // Add Email Label
        Label lblClinUserRegEmail = new Label("Email:");
        gridPaneClinicianViewPatients.add(lblClinUserRegEmail, 0, 2);
        GridPane.setHalignment(lblClinUserRegEmail, HPos.CENTER);

        // Add Email Textbox
        TextField txtClinEmail = new TextField();
        gridPaneClinicianViewPatients.add(txtClinEmail, 1, 2);
        GridPane.setHalignment(txtClinEmail, HPos.CENTER);
        txtClinEmail.setDisable(true);
        
        // Add age label
        Label lblClinRegDOB = new Label("Date of Birth:");
        gridPaneClinicianViewPatients.add(lblClinRegDOB, 0,3);
        GridPane.setHalignment(lblClinRegDOB, HPos.CENTER);

        // Add age textbox
        TextField txtClinRegDOB = new TextField();
        gridPaneClinicianViewPatients.add(txtClinRegDOB, 1,3);
        GridPane.setHalignment(txtClinRegDOB, HPos.CENTER);

        // Add town label
        Label lblClinUserTown = new Label("Town:");
        gridPaneClinicianViewPatients.add(lblClinUserTown, 0,4);
        GridPane.setHalignment(lblClinUserTown, HPos.CENTER);

        // Add town textbox
        TextField txtClinTown = new TextField();
        gridPaneClinicianViewPatients.add(txtClinTown, 1,4);
        GridPane.setHalignment(txtClinTown, HPos.CENTER);

        // Add eircode label
        Label lblClinUserRegEircode = new Label("Eircode:");
        gridPaneClinicianViewPatients.add(lblClinUserRegEircode, 0,5);
        GridPane.setHalignment(lblClinUserRegEircode, HPos.CENTER);

        // Add eircode textbox
        TextField txtClinEircode = new TextField();
        gridPaneClinicianViewPatients.add(txtClinEircode, 1,5);
        GridPane.setHalignment(txtClinEircode, HPos.CENTER);

        // Add phone label
        Label lblClinUserRegPhoneNumber = new Label("Phone Number:");
        gridPaneClinicianViewPatients.add(lblClinUserRegPhoneNumber, 0,6);
        GridPane.setHalignment(lblClinUserRegPhoneNumber, HPos.CENTER);

        // Add phone textbox
        TextField txtClinPhoneNumber = new TextField();
        gridPaneClinicianViewPatients.add(txtClinPhoneNumber, 1,6);
        GridPane.setHalignment(txtClinPhoneNumber, HPos.CENTER);

        // Add pps number label
        Label lblClinUserRegPPSNumber = new Label("PPS Number:");
        gridPaneClinicianViewPatients.add(lblClinUserRegPPSNumber, 0,7);
        GridPane.setHalignment(lblClinUserRegPPSNumber, HPos.CENTER);

        // Add pps textbox
        TextField txtClinPPSNumber = new TextField();
        gridPaneClinicianViewPatients.add(txtClinPPSNumber, 1,7);
        GridPane.setHalignment(txtClinPPSNumber, HPos.CENTER);
        
        // Add status label
        Label lblClinUserStatus = new Label("Status:");
        gridPaneClinicianViewPatients.add(lblClinUserStatus, 0,8);
        GridPane.setHalignment(lblClinUserStatus, HPos.CENTER);

        // Add status textbox
        TextField txtClinStatus = new TextField();
        gridPaneClinicianViewPatients.add(txtClinStatus, 1,8);
        GridPane.setHalignment(txtClinStatus, HPos.CENTER);
        
        // Combo Change Event
        cboPatients.setOnAction(event -> {
            int searchList = patientsArray.size();
            for(int i = 0; i < searchList; i++) {
                if(patientsArray.get(i).getName() == cboPatients.getValue()) {
                    txtClinEmail.setText(patientsArray.get(i).getEmail());
                    txtClinRegDOB.setText(patientsArray.get(i).getDateofbirth());
                    txtClinTown.setText(patientsArray.get(i).getTown());
                    txtClinEircode.setText(patientsArray.get(i).getEircode());
                    txtClinPhoneNumber.setText(patientsArray.get(i).getNumber());
                    txtClinPPSNumber.setText(patientsArray.get(i).getPpsNumber());
                    txtClinStatus.setText(patientsArray.get(i).getStatus());
                }
            }
        });
        
        //Add update button
        Button btnClinUpdatePatients = new Button("Update selected profile");
        btnClinUpdatePatients.setPrefHeight(40);
        btnClinUpdatePatients.setPrefWidth(400);
        GridPane.setHalignment(btnClinUpdatePatients, HPos.CENTER);
        gridPaneClinicianViewPatients.add(btnClinUpdatePatients, 0, 9, 5, 1);
        GridPane.setMargin(btnClinUpdatePatients, new Insets(20, 0,10,0));
        btnClinUpdatePatients.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinUpdatePatients.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this profile?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                // Validation
                if(txtClinRegDOB.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please enter a date of birth!");
                } else if(txtClinTown.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please enter a town!");
                } else if(txtClinEircode.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please pick an eircode!");
                } else if(txtClinPhoneNumber.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please pick a phone number!");
                } else if(txtClinPPSNumber.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please pick a PPS number!");
                } else if(txtClinStatus.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewPatients.getScene().getWindow(), "Form Error", "Please enter a status!");
                } else { 
                    int searchList = patientsArray.size();
                    for(int i = 0; i < searchList; i++) {
                        if(patientsArray.get(i).getName() == cboPatients.getValue()) {
                            patientsArray.get(i).setDateofbirth(txtClinRegDOB.getText());
                            patientsArray.get(i).setTown(txtClinTown.getText());
                            patientsArray.get(i).setEircode(txtClinEircode.getText());
                            patientsArray.get(i).setNumber(txtClinPhoneNumber.getText());
                            patientsArray.get(i).setPpsNumber(txtClinPPSNumber.getText());
                            patientsArray.get(i).setStatus(txtClinStatus.getText());
                            UserManagement.refreshPatientFile();                
                            showAlert(Alert.AlertType.INFORMATION, gridPaneClinicianViewPatients.getScene().getWindow(), "Success", "Profile Updated");
                            txtClinEmail.clear();
                            txtClinRegDOB.clear();
                            txtClinTown.clear();
                            txtClinEircode.clear();
                            txtClinPhoneNumber.clear();
                            txtClinPPSNumber.clear();
                            txtClinStatus.clear();
                            cboPatients.getItems().clear();
                            patientsArray.forEach((Patient patients) -> {cboPatients.getItems().add(patients.getName());});
                        }
                    }   
                }
            }
        });
        
        //Add delete button
        Button btnClinDeletePatients = new Button("Delete selected profile");
        btnClinDeletePatients.setPrefHeight(40);
        btnClinDeletePatients.setPrefWidth(400);
        GridPane.setHalignment(btnClinDeletePatients, HPos.CENTER);
        gridPaneClinicianViewPatients.add(btnClinDeletePatients, 0, 10, 5, 1);
        GridPane.setMargin(btnClinDeletePatients, new Insets(10, 0,10,0));
        btnClinDeletePatients.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinDeletePatients.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this patient?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                String nameToRemove = cboPatients.getValue().toString();
                
                for(int i = 0; i < appointmentsArray.size(); i++) {
                    if(appointmentsArray.get(i).getAppointmentName().equals(nameToRemove)) {
                        appointmentsArray.get(i).setNoShow(true);
                        appointmentsArray.get(i).setTaskStatus("CANCELLED");
                    }
                }
                AppointmentManagement.refreshAppointmentFile();
                
                Patient patientToDelete = null;
                for(int i = 0; i < patientsArray.size(); i++) {
                    if(patientsArray.get(i).getName().equals(nameToRemove)) {
                        patientToDelete = patientsArray.get(i);
                    }
                }
                patientsArray.remove(patientToDelete);
                UserManagement.refreshPatientFile();
                
                Questionnaire questionnaireToDelete = null;
                for(int i = 0; i < questionnaireArray.size(); i++) {
                    if(questionnaireArray.get(i).getQuestionnaireName().equals(nameToRemove)) {
                        questionnaireToDelete = questionnaireArray.get(i);
                    }
                }
                questionnaireArray.remove(questionnaireToDelete);
                UserManagement.refreshQuestionnaireFile();
                
                showAlert(Alert.AlertType.INFORMATION, gridPaneClinicianViewPatients.getScene().getWindow(), "Success", "Profile Deleted");
                txtClinEmail.clear();
                txtClinRegDOB.clear();
                txtClinTown.clear();
                txtClinEircode.clear();
                txtClinPhoneNumber.clear();
                txtClinPPSNumber.clear();
                txtClinStatus.clear();
                cboPatients.getItems().clear();
                patientsArray.forEach((Patient patients) -> {cboPatients.getItems().add(patients.getName());});
            }
        });
        
        //Add home button
        Button btnClinViewPatientsBack = new Button("Click here to return to the previous page");
        btnClinViewPatientsBack.setPrefHeight(40);
        btnClinViewPatientsBack.setDefaultButton(true);
        btnClinViewPatientsBack.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewPatientsBack, HPos.CENTER);
        gridPaneClinicianViewPatients.add(btnClinViewPatientsBack, 0, 11, 5, 1);
        GridPane.setMargin(btnClinViewPatientsBack, new Insets(10, 0,10,0));
        btnClinViewPatientsBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewPatientsBack.setOnAction(event -> {
            if(clinicianLoggedIn == true) {
                primaryStage.setScene(sceneClinicianHome);
            } else if(adminLoggedIn == true) {
                primaryStage.setScene(sceneAdminHome);
            }
        });
        
        // Create a scene object
        sceneViewPatients = new Scene(gridPaneClinicianViewPatients, 500, 550);
                
        /* ------------------------------------------------------------------------
                        CLINICIAN VIEW QUESTIONNAIRES PAGE
        ------------------------------------------------------------------------ */
        
        // Create the patient view grid pane
        GridPane gridPaneClinicianViewQuestionnaires = new GridPane();

        // Position the pane at the middle of the screen
        gridPaneClinicianViewQuestionnaires.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneClinicianViewQuestionnaires.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneClinicianViewQuestionnaires.setHgap(10);

        // Set the vertical gap 
        gridPaneClinicianViewQuestionnaires.setVgap(5);

        //Background colour
        gridPaneClinicianViewQuestionnaires.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblClinViewQuestionnairesHeader = new Label("View Questionnaires");
        lblClinViewQuestionnairesHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianViewQuestionnaires.add(lblClinViewQuestionnairesHeader, 0, 0, 2, 1);
        GridPane.setHalignment(lblClinViewQuestionnairesHeader, HPos.CENTER);
        GridPane.setMargin(lblClinViewQuestionnairesHeader, new Insets(20, 0, 20, 0));
        
        // Create table
        TableView<Questionnaire> tblClinViewQuestionnaires = new TableView();
    
        TableColumn<Questionnaire, String> colQuesName = new TableColumn<>("Name");
        colQuesName.setCellValueFactory(new PropertyValueFactory<>("questionnaireName"));
        TableColumn<Questionnaire, Boolean> colCOVID14Days = new TableColumn<>("Had COVID last 14 days?");
        colCOVID14Days.setCellValueFactory(new PropertyValueFactory<>("questionnaireCOVID14Days"));
        TableColumn<Questionnaire, Boolean> colVaccine14Days = new TableColumn<>("Had vaccine last 14 days?");
        colVaccine14Days.setCellValueFactory(new PropertyValueFactory<>("questionnaireVaccine14Days"));
        TableColumn<Questionnaire, Boolean> colIsPregnant = new TableColumn<>("Is pregnant?");
        colIsPregnant.setCellValueFactory(new PropertyValueFactory<>("questionnaireIsPregnant"));
        TableColumn<Questionnaire, Boolean> colHadAnaphylaxis = new TableColumn<>("Had anaphylaxis?");
        colHadAnaphylaxis.setCellValueFactory(new PropertyValueFactory<>("questionnaireHadAnaphylaxis"));
        TableColumn<Questionnaire, Boolean> colHasExistingConditions = new TableColumn<>("Has existing conditions?");
        colHasExistingConditions.setCellValueFactory(new PropertyValueFactory<>("questionnaireHasExistingConditions"));
        TableColumn<Questionnaire, String> colExistingConditions = new TableColumn<>("Existing Conditions");
        colExistingConditions.setCellValueFactory(new PropertyValueFactory<>("questionnaireExistingConditions"));
        TableColumn<Questionnaire, String> colMedicalStatus = new TableColumn<>("Medical Status");
        colMedicalStatus.setCellValueFactory(new PropertyValueFactory<>("questionnaireMedicalStatus"));
        
        // Add columns
        tblClinViewQuestionnaires.getColumns().add(colQuesName);
        tblClinViewQuestionnaires.getColumns().add(colCOVID14Days);
        tblClinViewQuestionnaires.getColumns().add(colVaccine14Days);
        tblClinViewQuestionnaires.getColumns().add(colIsPregnant);
        tblClinViewQuestionnaires.getColumns().add(colHadAnaphylaxis);
        tblClinViewQuestionnaires.getColumns().add(colHasExistingConditions);
        tblClinViewQuestionnaires.getColumns().add(colExistingConditions);
        tblClinViewQuestionnaires.getColumns().add(colMedicalStatus);
        
        // Populate table and add to gridpane
        questionnaireArray.forEach((Questionnaire questionnaire) -> {tblClinViewQuestionnaires.getItems().add(questionnaire);});
        tblClinViewQuestionnaires.getSortOrder().add(colQuesName);
        
        tblClinViewQuestionnaires.setPrefWidth(1250);
        gridPaneClinicianViewQuestionnaires.add(tblClinViewQuestionnaires, 0, 1, 5, 1);
        GridPane.setHalignment(tblClinViewQuestionnaires, HPos.CENTER);
        
        // Add COVID14Days label
        Label lblCOVID14Days = new Label("Had COVID in last 14 days?");
        
        // Add COVID14Days combobox
        ComboBox cboCOVID14Days = new ComboBox();
        cboCOVID14Days.getItems().addAll("true", "false");
        cboCOVID14Days.getSelectionModel().select(1);
        
        // Add COVID14Days hbox
        HBox hboxCOVID14Days = new HBox(10);
        hboxCOVID14Days.getChildren().addAll(lblCOVID14Days, cboCOVID14Days);
        gridPaneClinicianViewQuestionnaires.add(hboxCOVID14Days, 0, 2, 5, 1);
        GridPane.setHalignment(hboxCOVID14Days, HPos.CENTER);
        GridPane.setMargin(hboxCOVID14Days, new Insets(20, 0,0,0));
        
        // Add Vaccine14Days label
        Label lblVaccine14Days = new Label("Had vaccine in last 14 days?");
        
        // Add Vaccine14Days combobox
        ComboBox cboVaccine14Days = new ComboBox();
        cboVaccine14Days.getItems().addAll("true", "false");
        cboVaccine14Days.getSelectionModel().select(1);
        
        // Add Vaccine14Days hbox
        HBox hboxVaccine14Days = new HBox(10);
        hboxVaccine14Days.getChildren().addAll(lblVaccine14Days, cboVaccine14Days);
        gridPaneClinicianViewQuestionnaires.add(hboxVaccine14Days, 0, 3, 5, 1);
        GridPane.setHalignment(hboxVaccine14Days, HPos.CENTER);
        GridPane.setMargin(hboxVaccine14Days, new Insets(10, 0,0,0));
        
        // Add IsPregnant label
        Label lblIsPregnant = new Label("Is currently pregnant?");
        
        // Add IsPregnant combobox
        ComboBox cboIsPregnant = new ComboBox();
        cboIsPregnant.getItems().addAll("true", "false");
        cboIsPregnant.getSelectionModel().select(1);
        
        // Add IsPregnant hbox
        HBox hboxIsPregnant = new HBox(44);
        hboxIsPregnant.getChildren().addAll(lblIsPregnant, cboIsPregnant);
        gridPaneClinicianViewQuestionnaires.add(hboxIsPregnant, 0, 4, 5, 1);
        GridPane.setHalignment(hboxIsPregnant, HPos.CENTER);
        GridPane.setMargin(hboxIsPregnant, new Insets(10, 0,0,0));
        
        // Add HadAnaphylaxis label
        Label lblHadAnaphylaxis = new Label("Previously had anaphylaxis?");
        
        // Add HadAnaphylaxis combobox
        ComboBox cboHadAnaphylaxis = new ComboBox();
        cboHadAnaphylaxis.getItems().addAll("true", "false");
        cboHadAnaphylaxis.getSelectionModel().select(1);
        
        // Add HadAnaphylaxis hbox
        HBox hboxHadAnaphylaxis = new HBox(10);
        hboxHadAnaphylaxis.getChildren().addAll(lblHadAnaphylaxis, cboHadAnaphylaxis);
        gridPaneClinicianViewQuestionnaires.add(hboxHadAnaphylaxis, 0, 5, 5, 1);
        GridPane.setHalignment(hboxHadAnaphylaxis, HPos.CENTER);
        GridPane.setMargin(hboxHadAnaphylaxis, new Insets(10, 0,0,0));
        
        // Add HasExistingConditions label
        Label lblHasExistingConditions = new Label("Has existing conditions?");
        
        // Add HasExistingConditions combobox
        ComboBox cboHasExistingConditions = new ComboBox();
        cboHasExistingConditions.getItems().addAll("true", "false");
        cboHasExistingConditions.getSelectionModel().select(1);
        
        // Add HasExistingConditions hbox
        HBox hboxHasExistingConditions = new HBox(34);
        hboxHasExistingConditions.getChildren().addAll(lblHasExistingConditions, cboHasExistingConditions);
        gridPaneClinicianViewQuestionnaires.add(hboxHasExistingConditions, 0, 6, 5, 1);
        GridPane.setHalignment(hboxHasExistingConditions, HPos.CENTER);
        GridPane.setMargin(hboxHasExistingConditions, new Insets(10, 0,0,0));
        
        // Add ExistingConditions label
        Label lblExistingConditions = new Label("If yes, what conditions?");
        
        // Add ExistingConditions textbox
        TextField txtExistingConditions = new TextField();
        txtExistingConditions.setDisable(true);
        
        // Action to enable/disable
        cboHasExistingConditions.setOnAction(event -> {
            if(cboHasExistingConditions.getSelectionModel().isSelected(0)) {
                txtExistingConditions.setDisable(false);
            } else {
                txtExistingConditions.setDisable(true);
            }
        });
        
        // Add ExistingConditions hbox
        HBox hboxExistingConditions = new HBox(20);
        hboxExistingConditions.getChildren().addAll(lblExistingConditions, txtExistingConditions);
        gridPaneClinicianViewQuestionnaires.add(hboxExistingConditions, 0, 7, 5, 1);
        GridPane.setHalignment(hboxExistingConditions, HPos.CENTER);
        GridPane.setMargin(hboxExistingConditions, new Insets(10, 0,0,0));
        
        // Add MedicalStatus label
        Label lblMedicalStatus = new Label("Medically approved for vaccine?");
        
        // Add MedicalStatus combobox
        ComboBox cboMedicalStatus = new ComboBox();
        cboMedicalStatus.getItems().addAll("PENDING", "UNDER REVIEW", "APPROVED", "DENIED & CONTACTED");
        cboMedicalStatus.getSelectionModel().selectFirst();
        
        // Add MedicalStatus hbox
        HBox hboxMedicalStatus = new HBox(10);
        hboxMedicalStatus.getChildren().addAll(lblMedicalStatus, cboMedicalStatus);
        gridPaneClinicianViewQuestionnaires.add(hboxMedicalStatus, 0, 8, 5, 1);
        GridPane.setHalignment(hboxMedicalStatus, HPos.CENTER);
        GridPane.setMargin(hboxMedicalStatus, new Insets(10, 0,0,0));
        
        //Add update button
        Button btnClinViewQuestionnairesUpdate = new Button("Update the selected record");
        btnClinViewQuestionnairesUpdate.setPrefHeight(40);
        btnClinViewQuestionnairesUpdate.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewQuestionnairesUpdate, HPos.CENTER);
        gridPaneClinicianViewQuestionnaires.add(btnClinViewQuestionnairesUpdate, 0, 9, 5, 1);
        GridPane.setMargin(btnClinViewQuestionnairesUpdate, new Insets(10, 0,10,0));
        btnClinViewQuestionnairesUpdate.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewQuestionnairesUpdate.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this questionnaire?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblClinViewQuestionnaires.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                        Questionnaire questionnaireRow = tblClinViewQuestionnaires.getItems().get(selectedIndex);
                        int searchList = questionnaireArray.size();
                        for(int i = 0; i < searchList; i++) {
                            if(questionnaireArray.get(i) == questionnaireRow) {
                                questionnaireArray.get(i).setQuestionnaireCOVID14Days(Boolean.parseBoolean(cboCOVID14Days.getValue().toString()));
                                questionnaireArray.get(i).setQuestionnaireVaccine14Days(Boolean.parseBoolean(cboVaccine14Days.getValue().toString()));
                                questionnaireArray.get(i).setQuestionnaireIsPregnant(Boolean.parseBoolean(cboIsPregnant.getValue().toString()));
                                questionnaireArray.get(i).setQuestionnaireHadAnaphylaxis(Boolean.parseBoolean(cboHadAnaphylaxis.getValue().toString()));
                                questionnaireArray.get(i).setQuestionnaireHasExistingConditions(Boolean.parseBoolean(cboHasExistingConditions.getValue().toString()));
                                if(cboHasExistingConditions.getValue().toString().equals("true")) {
                                    questionnaireArray.get(i).setQuestionnaireExistingConditions(txtExistingConditions.getText());
                                }
                                questionnaireArray.get(i).setQuestionnaireMedicalStatus(cboMedicalStatus.getValue().toString());
                            }
                        }
                        tblClinViewQuestionnaires.getItems().clear();
                        UserManagement.refreshQuestionnaireFile();
                        questionnaireArray.forEach((Questionnaire questionnaire) -> {tblClinViewQuestionnaires.getItems().add(questionnaire);});
                        tblClinViewQuestionnaires.getSortOrder().add(colQuesName);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewQuestionnaires.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        //Add delete button
        Button btnClinViewQuestionnairesDelete = new Button("Delete the selected record");
        btnClinViewQuestionnairesDelete.setPrefHeight(40);
        btnClinViewQuestionnairesDelete.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewQuestionnairesDelete, HPos.CENTER);
        gridPaneClinicianViewQuestionnaires.add(btnClinViewQuestionnairesDelete, 0, 10, 5, 1);
        GridPane.setMargin(btnClinViewQuestionnairesDelete, new Insets(10, 0,10,0));
        btnClinViewQuestionnairesDelete.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewQuestionnairesDelete.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this questionnaire?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblClinViewQuestionnaires.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                        Questionnaire questionnaireRow = tblClinViewQuestionnaires.getItems().get(selectedIndex);
                        int searchList = questionnaireArray.size();
                        Questionnaire questionnaireToDelete = null;
                        for(int i = 0; i < searchList; i++) {
                            if(questionnaireArray.get(i) == questionnaireRow) {
                                questionnaireToDelete = questionnaireArray.get(i);
                            }
                        }
                        
                        int searchListTwo = patientsArray.size();
                        for(int i = 0; i < searchListTwo; i++) {
                            if(patientsArray.get(i).getName().equals(questionnaireToDelete.getQuestionnaireName())) {
                                patientsArray.get(i).setQuestionnaireDone(false);
                            }
                        }
                        questionnaireArray.remove(questionnaireToDelete);
                        tblClinViewQuestionnaires.getItems().clear();
                        UserManagement.refreshQuestionnaireFile();
                        UserManagement.refreshPatientFile();
                        questionnaireArray.forEach((Questionnaire questionnaire) -> {tblClinViewQuestionnaires.getItems().add(questionnaire);});
                        tblClinViewQuestionnaires.getSortOrder().add(colQuesName);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewQuestionnaires.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        //Add create button
        Button btnClinViewQuestionnairesCreate = new Button("Create a new record");
        btnClinViewQuestionnairesCreate.setPrefHeight(40);
        btnClinViewQuestionnairesCreate.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewQuestionnairesCreate, HPos.CENTER);
        gridPaneClinicianViewQuestionnaires.add(btnClinViewQuestionnairesCreate, 0, 11, 5, 1);
        GridPane.setMargin(btnClinViewQuestionnairesCreate, new Insets(10, 0,10,0));
        btnClinViewQuestionnairesCreate.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewQuestionnairesCreate.setOnAction(event -> {
            if(populateCreateQuestionnaireCombo(cboCreateName) == false) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewQuestionnaires.getScene().getWindow(), "Form Error", "No patients awaiting questionnaire.");
            } else {
                primaryStage.setScene(sceneClinicianCreateQuestionnaires);
            }
        });
        
        //Add home button
        Button btnClinViewQuestionnairesBack = new Button("Click here to return to the previous page");
        btnClinViewQuestionnairesBack.setPrefHeight(40);
        btnClinViewQuestionnairesBack.setDefaultButton(true);
        btnClinViewQuestionnairesBack.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewQuestionnairesBack, HPos.CENTER);
        gridPaneClinicianViewQuestionnaires.add(btnClinViewQuestionnairesBack, 0, 12, 5, 1);
        GridPane.setMargin(btnClinViewQuestionnairesBack, new Insets(10, 0,10,0));
        btnClinViewQuestionnairesBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewQuestionnairesBack.setOnAction(event -> {
            if(clinicianLoggedIn == true) {
                primaryStage.setScene(sceneClinicianHome);
            } else if(adminLoggedIn == true) {
                primaryStage.setScene(sceneAdminHome);
            }
        });
        
        // Create a scene object
        sceneViewQuestionnaires = new Scene(gridPaneClinicianViewQuestionnaires, 800, 800);
        
        /* ------------------------------------------------------------------------
                        CLINICIAN CREATE QUESTIONNAIRES PAGE
        ------------------------------------------------------------------------ */
        
        // Create the patient view grid pane
        GridPane gridPaneClinicianCreateQuestionnaires = new GridPane();

        // Position the pane at the middle of the screen
        gridPaneClinicianCreateQuestionnaires.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneClinicianCreateQuestionnaires.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneClinicianCreateQuestionnaires.setHgap(10);

        // Set the vertical gap 
        gridPaneClinicianCreateQuestionnaires.setVgap(5);

        //Background colour
        gridPaneClinicianCreateQuestionnaires.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblClinCreateQuestionnairesHeader = new Label("Create Questionnaire");
        lblClinCreateQuestionnairesHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianCreateQuestionnaires.add(lblClinCreateQuestionnairesHeader, 0, 0, 2, 1);
        GridPane.setHalignment(lblClinCreateQuestionnairesHeader, HPos.CENTER);
        GridPane.setMargin(lblClinCreateQuestionnairesHeader, new Insets(20, 0, 20, 0));
        
        // Add name label
        Label lblCreateName = new Label("Patient's name:");
        
        // Add COVID14Days hbox
        HBox hboxCreateName = new HBox(10);
        hboxCreateName.getChildren().addAll(lblCreateName, cboCreateName);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateName, 0, 1, 5, 1);
        GridPane.setHalignment(hboxCreateName, HPos.CENTER);
        GridPane.setMargin(hboxCreateName, new Insets(20, 0,0,0));
        
        // Add COVID14Days label
        Label lblCreateCOVID14Days = new Label("Had COVID in last 14 days?");
        
        // Add COVID14Days combobox
        ComboBox cboCreateCOVID14Days = new ComboBox();
        cboCreateCOVID14Days.getItems().addAll("true", "false");
        cboCreateCOVID14Days.getSelectionModel().select(1);
        
        // Add COVID14Days hbox
        HBox hboxCreateCOVID14Days = new HBox(10);
        hboxCreateCOVID14Days.getChildren().addAll(lblCreateCOVID14Days, cboCreateCOVID14Days);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateCOVID14Days, 0, 2, 5, 1);
        GridPane.setHalignment(hboxCreateCOVID14Days, HPos.CENTER);
        GridPane.setMargin(hboxCreateCOVID14Days, new Insets(20, 0,0,0));
        
        // Add Vaccine14Days label
        Label lblCreateVaccine14Days = new Label("Had vaccine in last 14 days?");
        
        // Add Vaccine14Days combobox
        ComboBox cboCreateVaccine14Days = new ComboBox();
        cboCreateVaccine14Days.getItems().addAll("true", "false");
        cboCreateVaccine14Days.getSelectionModel().select(1);
        
        // Add Vaccine14Days hbox
        HBox hboxCreateVaccine14Days = new HBox(10);
        hboxCreateVaccine14Days.getChildren().addAll(lblCreateVaccine14Days, cboCreateVaccine14Days);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateVaccine14Days, 0, 3, 5, 1);
        GridPane.setHalignment(hboxCreateVaccine14Days, HPos.CENTER);
        GridPane.setMargin(hboxCreateVaccine14Days, new Insets(10, 0,0,0));
        
        // Add IsPregnant label
        Label lblCreateIsPregnant = new Label("Is currently pregnant?");
        
        // Add IsPregnant combobox
        ComboBox cboCreateIsPregnant = new ComboBox();
        cboCreateIsPregnant.getItems().addAll("true", "false");
        cboCreateIsPregnant.getSelectionModel().select(1);
        
        // Add IsPregnant hbox
        HBox hboxCreateIsPregnant = new HBox(44);
        hboxCreateIsPregnant.getChildren().addAll(lblCreateIsPregnant, cboCreateIsPregnant);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateIsPregnant, 0, 4, 5, 1);
        GridPane.setHalignment(hboxCreateIsPregnant, HPos.CENTER);
        GridPane.setMargin(hboxCreateIsPregnant, new Insets(10, 0,0,0));
        
        // Add HadAnaphylaxis label
        Label lblCreateHadAnaphylaxis = new Label("Previously had anaphylaxis?");
        
        // Add HadAnaphylaxis combobox
        ComboBox cboCreateHadAnaphylaxis = new ComboBox();
        cboCreateHadAnaphylaxis.getItems().addAll("true", "false");
        cboCreateHadAnaphylaxis.getSelectionModel().select(1);
        
        // Add HadAnaphylaxis hbox
        HBox hboxCreateHadAnaphylaxis = new HBox(10);
        hboxCreateHadAnaphylaxis.getChildren().addAll(lblCreateHadAnaphylaxis, cboCreateHadAnaphylaxis);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateHadAnaphylaxis, 0, 5, 5, 1);
        GridPane.setHalignment(hboxCreateHadAnaphylaxis, HPos.CENTER);
        GridPane.setMargin(hboxCreateHadAnaphylaxis, new Insets(10, 0,0,0));
        
        // Add HasExistingConditions label
        Label lblCreateHasExistingConditions = new Label("Has existing conditions?");
        
        // Add HasExistingConditions combobox
        ComboBox cboCreateHasExistingConditions = new ComboBox();
        cboCreateHasExistingConditions.getItems().addAll("true", "false");
        cboCreateHasExistingConditions.getSelectionModel().select(1);
        
        // Add HasExistingConditions hbox
        HBox hboxCreateHasExistingConditions = new HBox(34);
        hboxCreateHasExistingConditions.getChildren().addAll(lblCreateHasExistingConditions, cboCreateHasExistingConditions);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateHasExistingConditions, 0, 6, 5, 1);
        GridPane.setHalignment(hboxCreateHasExistingConditions, HPos.CENTER);
        GridPane.setMargin(hboxCreateHasExistingConditions, new Insets(10, 0,0,0));
        
        // Add ExistingConditions label
        Label lblCreateExistingConditions = new Label("If yes, what conditions?");
        
        // Add ExistingConditions textbox
        TextField txtCreateExistingConditions = new TextField();
        txtCreateExistingConditions.setDisable(true);
        
        // Action to enable/disable
        cboCreateHasExistingConditions.setOnAction(event -> {
            if(cboCreateHasExistingConditions.getSelectionModel().isSelected(0)) {
                txtCreateExistingConditions.setDisable(false);
            } else {
                txtCreateExistingConditions.setDisable(true);
            }
        });
        
        // Add ExistingConditions hbox
        HBox hboxCreateExistingConditions = new HBox(20);
        hboxCreateExistingConditions.getChildren().addAll(lblCreateExistingConditions, txtCreateExistingConditions);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateExistingConditions, 0, 7, 5, 1);
        GridPane.setHalignment(hboxCreateExistingConditions, HPos.CENTER);
        GridPane.setMargin(hboxCreateExistingConditions, new Insets(10, 0,0,0));
        
        // Add MedicalStatus label
        Label lblCreateMedicalStatus = new Label("Medically approved for vaccine?");
        
        // Add MedicalStatus combobox
        ComboBox cboCreateMedicalStatus = new ComboBox();
        cboCreateMedicalStatus.getItems().addAll("PENDING", "UNDER REVIEW", "APPROVED", "DENIED & CONTACTED");
        cboCreateMedicalStatus.getSelectionModel().selectFirst();
        
        // Add MedicalStatus hbox
        HBox hboxCreateMedicalStatus = new HBox(10);
        hboxCreateMedicalStatus.getChildren().addAll(lblCreateMedicalStatus, cboCreateMedicalStatus);
        gridPaneClinicianCreateQuestionnaires.add(hboxCreateMedicalStatus, 0, 8, 5, 1);
        GridPane.setHalignment(hboxCreateMedicalStatus, HPos.CENTER);
        GridPane.setMargin(hboxCreateMedicalStatus, new Insets(10, 0, 0, 0));
        
        //Add create button
        Button btnClinCreateQuestionnaire = new Button("Create record");
        btnClinCreateQuestionnaire.setPrefHeight(40);
        btnClinCreateQuestionnaire.setPrefWidth(400);
        GridPane.setHalignment(btnClinCreateQuestionnaire, HPos.CENTER);
        gridPaneClinicianCreateQuestionnaires.add(btnClinCreateQuestionnaire, 0, 11, 5, 1);
        GridPane.setMargin(btnClinCreateQuestionnaire, new Insets(10, 0, 10, 0));
        btnClinCreateQuestionnaire.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinCreateQuestionnaire.setOnAction(event -> {
            if(cboCreateHasExistingConditions.getValue().equals("true") && txtCreateExistingConditions.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateQuestionnaires.getScene().getWindow(), "Form Error", "Enter existing conditions.");
                event.consume();
            } else {
                int searchList = patientsArray.size();
                for(int i = 0; i < searchList; i++) {
                    if(patientsArray.get(i).getName().equals(cboCreateName.getValue().toString())) {
                        patientsArray.get(i).setQuestionnaireDone(true);
                    }
                }
                UserManagement.addQuestionnaire(cboCreateName.getValue().toString(), Boolean.parseBoolean(cboCreateCOVID14Days.getValue().toString()), 
                        Boolean.parseBoolean(cboCreateVaccine14Days.getValue().toString()), Boolean.parseBoolean(cboCreateIsPregnant.getValue().toString()), 
                        Boolean.parseBoolean(cboCreateHadAnaphylaxis.getValue().toString()), Boolean.parseBoolean(cboCreateHasExistingConditions.getValue().toString()), txtCreateExistingConditions.getText());
                
                tblClinViewQuestionnaires.getItems().clear();
                UserManagement.refreshQuestionnaireFile();
                UserManagement.refreshPatientFile();
                showAlert(Alert.AlertType.INFORMATION, gridPaneClinicianCreateQuestionnaires.getScene().getWindow(), "Success", "Record created!");
                primaryStage.setScene(sceneViewQuestionnaires);
                questionnaireArray.forEach((Questionnaire questionnaire) -> {tblClinViewQuestionnaires.getItems().add(questionnaire);});
                tblClinViewQuestionnaires.getSortOrder().add(colQuesName);
            }
        });
        
        //Add home button
        Button btnClinCreateQuestionnairesBack = new Button("Click here to return to the previous page");
        btnClinCreateQuestionnairesBack.setPrefHeight(40);
        btnClinCreateQuestionnairesBack.setDefaultButton(true);
        btnClinCreateQuestionnairesBack.setPrefWidth(400);
        GridPane.setHalignment(btnClinCreateQuestionnairesBack, HPos.CENTER);
        gridPaneClinicianCreateQuestionnaires.add(btnClinCreateQuestionnairesBack, 0, 12, 5, 1);
        GridPane.setMargin(btnClinCreateQuestionnairesBack, new Insets(10, 0, 10, 0));
        btnClinCreateQuestionnairesBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinCreateQuestionnairesBack.setOnAction(event -> {
            primaryStage.setScene(sceneViewQuestionnaires);
        });
        
        // Create a scene object
        sceneClinicianCreateQuestionnaires = new Scene(gridPaneClinicianCreateQuestionnaires, 550, 600);
        
        /* ------------------------------------------------------------------------
                            CLINICIAN VIEW APPOINTMENTS PAGE
        ------------------------------------------------------------------------ */
        
        // Create the registration form grid pane
        GridPane gridPaneClinicianViewAppointments = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneClinicianViewAppointments.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneClinicianViewAppointments.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneClinicianViewAppointments.setHgap(10);

        // Set the vertical gap 
        gridPaneClinicianViewAppointments.setVgap(5);

        //Background colour
        gridPaneClinicianViewAppointments.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblClinViewAppointmentsHeader = new Label("View patients booked appointments");
        lblClinViewAppointmentsHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianViewAppointments.add(lblClinViewAppointmentsHeader, 0,0,5,1);
        GridPane.setHalignment(lblClinViewAppointmentsHeader, HPos.CENTER);
        GridPane.setMargin(lblClinViewAppointmentsHeader, new Insets(20, 0, 20, 0));
        
        // Create table
        TableView<Appointment> tblClinViewAppointments = new TableView();
    
        TableColumn<Appointment, String> colClinUserEmail = new TableColumn<>("Email");
        colClinUserEmail.setCellValueFactory(new PropertyValueFactory<>("appointmentEmail"));
        TableColumn<Appointment, String> colClinUserName = new TableColumn<>("Name");
        colClinUserName.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));
        TableColumn<Appointment, String> colClinUserDate = new TableColumn<>("Date");
        colClinUserDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        TableColumn<Appointment, String> colClinUserTime = new TableColumn<>("Time");
        colClinUserTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        TableColumn<Appointment, String> colClinUserLocation = new TableColumn<>("Location");
        colClinUserLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        TableColumn<Appointment, String> colClinUserVaccineType = new TableColumn<>("Vaccine Type");
        colClinUserVaccineType.setCellValueFactory(new PropertyValueFactory<>("vaccineType"));
        TableColumn<Appointment, Integer> colClinUserDose = new TableColumn<>("Dose No.");
        colClinUserDose.setCellValueFactory(new PropertyValueFactory<>("doseNo"));
        TableColumn<Appointment, Boolean> colClinUserNoShow = new TableColumn<>("No Show?");
        colClinUserNoShow.setCellValueFactory(new PropertyValueFactory<>("noShow"));
        TableColumn<Appointment, String> colClinUserApptStatus = new TableColumn<>("Status");
        colClinUserApptStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        
        // Add columns
        tblClinViewAppointments.getColumns().add(colClinUserEmail);
        tblClinViewAppointments.getColumns().add(colClinUserName);
        tblClinViewAppointments.getColumns().add(colClinUserDate);
        tblClinViewAppointments.getColumns().add(colClinUserTime);
        tblClinViewAppointments.getColumns().add(colClinUserLocation);
        tblClinViewAppointments.getColumns().add(colClinUserVaccineType);
        tblClinViewAppointments.getColumns().add(colClinUserDose);
        tblClinViewAppointments.getColumns().add(colClinUserNoShow);
        tblClinViewAppointments.getColumns().add(colClinUserApptStatus);
        
        // Populate table and add to gridpane
        appointmentsArray.forEach((Appointment appointments) -> {tblClinViewAppointments.getItems().add(appointments);});
        tblClinViewAppointments.getSortOrder().add(colClinUserDate);
        
        tblClinViewAppointments.setPrefWidth(1250);
        gridPaneClinicianViewAppointments.add(tblClinViewAppointments, 0, 1, 5, 1);
        GridPane.setHalignment(tblClinViewAppointments, HPos.CENTER);
        
        // Add vaccinetype label
        Label lblUpdateVaccineType = new Label("Assign a vaccine type:");
        
        // Add vaccinetype combobox
        ComboBox cboVaccineType = new ComboBox();
        int searchClinVaccineList = vaccinesArray.size();
        for(int i = 0; i < searchClinVaccineList; i++) {
            cboVaccineType.getItems().add(vaccinesArray.get(i).getVaccineName());
        }
        cboVaccineType.getSelectionModel().selectFirst();
        
        // Add vaccinetype hbox
        HBox hboxVaccineType = new HBox(10);
        hboxVaccineType.getChildren().addAll(lblUpdateVaccineType, cboVaccineType);
        gridPaneClinicianViewAppointments.add(hboxVaccineType, 0, 2, 5, 1);
        GridPane.setHalignment(hboxVaccineType, HPos.CENTER);
        GridPane.setMargin(hboxVaccineType, new Insets(20, 0,0,0));
        
        // Add status label
        Label lblUpdateVaccineStatus = new Label("Set appointment status:");
        
        // Add status combobox
        ComboBox cboUpdateVaccineStatus = new ComboBox();
        cboUpdateVaccineStatus.getItems().addAll("PENDING", "APPOINTMENT CONFIRMED", "AWAITING VACCINE DOSE", "VACCINE DOSE ASSIGNED", "VACCINE ADMINISTERED", "FOLLOW UP CONFIRMED");
        cboUpdateVaccineStatus.getSelectionModel().selectFirst();
        
        // Add status hbox
        HBox hboxVaccineStatus = new HBox(10);
        hboxVaccineStatus.getChildren().addAll(lblUpdateVaccineStatus, cboUpdateVaccineStatus);
        gridPaneClinicianViewAppointments.add(hboxVaccineStatus, 0, 3, 5, 1);
        GridPane.setHalignment(hboxVaccineStatus, HPos.CENTER);
        
        // Add update appointment button
        Button btnClinUpdateAppointments = new Button("Click here to update selected appointment");
        btnClinUpdateAppointments.setPrefHeight(40);
        btnClinUpdateAppointments.setPrefWidth(400);
        GridPane.setHalignment(btnClinUpdateAppointments, HPos.CENTER);
        btnClinUpdateAppointments.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinUpdateAppointments.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this appointment?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblClinViewAppointments.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                        Appointment appointmentRow = tblClinViewAppointments.getItems().get(selectedIndex);
                        int searchList = appointmentsArray.size();
                        for(int i = 0; i < searchList; i++) {
                            if(appointmentsArray.get(i) == appointmentRow) {
                                appointmentsArray.get(i).setVaccineType(cboVaccineType.getValue().toString());
                                appointmentsArray.get(i).setTaskStatus(cboUpdateVaccineStatus.getValue().toString());
                            }
                        }
                        tblClinViewAppointments.getItems().clear();
                        AppointmentManagement.refreshAppointmentFile();
                        appointmentsArray.forEach((Appointment appointments) -> {tblClinViewAppointments.getItems().add(appointments);});
                        tblClinViewAppointments.getSortOrder().add(colClinUserDate);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewAppointments.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        // Add noshow appointment button
        Button btnClinNoShowAppointment = new Button("Click here to mark selected appointment 'no-show'");
        btnClinNoShowAppointment.setPrefHeight(40);
        btnClinNoShowAppointment.setPrefWidth(450);
        GridPane.setHalignment(btnClinNoShowAppointment, HPos.CENTER);
        btnClinNoShowAppointment.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinNoShowAppointment.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this appointment?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblClinViewAppointments.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                        Appointment appointmentRow = tblClinViewAppointments.getItems().get(selectedIndex);
                        int searchList = appointmentsArray.size();
                        for(int i = 0; i < searchList; i++) {
                            if(appointmentsArray.get(i) == appointmentRow) {
                                appointmentsArray.get(i).setNoShow(true);
                                appointmentsArray.get(i).setTaskStatus("CANCELLED");
                            }
                        }
                        tblClinViewAppointments.getItems().clear();
                        AppointmentManagement.refreshAppointmentFile();
                        appointmentsArray.forEach((Appointment appointments) -> {tblClinViewAppointments.getItems().add(appointments);});
                        tblClinViewAppointments.getSortOrder().add(colClinUserDate);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewAppointments.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        // Add buttons hbox
        HBox hboxUpdateOrNoShow = new HBox(5);
        GridPane.setMargin(hboxUpdateOrNoShow, new Insets(20, 0,20,0));
        hboxUpdateOrNoShow.getChildren().addAll(btnClinUpdateAppointments, btnClinNoShowAppointment);
        gridPaneClinicianViewAppointments.add(hboxUpdateOrNoShow, 0, 4, 5, 1);
        GridPane.setHalignment(hboxUpdateOrNoShow, HPos.CENTER);
        
        // Add cancel appointment button
        Button btnClinViewAppointmentsCancel = new Button("Click here to cancel selected appointment");
        btnClinViewAppointmentsCancel.setPrefHeight(40);
        btnClinViewAppointmentsCancel.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewAppointmentsCancel, HPos.CENTER);
        GridPane.setMargin(btnClinViewAppointmentsCancel, new Insets(20, 0,20,0));
        btnClinViewAppointmentsCancel.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewAppointmentsCancel.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to cancel this appointment?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                int selectedIndex = tblClinViewAppointments.getSelectionModel().getSelectedIndex();
                
                if (selectedIndex >= 0) {
                        Appointment appointmentRow = tblClinViewAppointments.getItems().get(selectedIndex);
                        int searchList = appointmentsArray.size();
                        for(int i = 0; i < searchList; i++) {
                            if(appointmentsArray.get(i) == appointmentRow) {
                                if(appointmentsArray.get(i).getTaskStatus().equals("CANCELLED")) {
                                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewAppointments.getScene().getWindow(), "Form Error", "Appointment already cancelled.");
                                    event.consume();
                                } else {
                                    appointmentsArray.get(i).setTaskStatus("CANCELLED");
                                    appointmentsArray.get(i).setNoShow(true);
                                }
                            }
                        }
                        tblClinViewAppointments.getItems().clear();
                        AppointmentManagement.refreshAppointmentFile();
                        appointmentsArray.forEach((Appointment appointments) -> {tblClinViewAppointments.getItems().add(appointments);});
                        tblClinViewAppointments.getSortOrder().add(colClinUserDate);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianViewAppointments.getScene().getWindow(), "Form Error", "Please select a table row.");
                }
            }
        });
        
        // Add create appointment button
        Button btnClinCreateAppointments = new Button("Click here to create a new appointment");
        btnClinCreateAppointments.setPrefHeight(40);
        btnClinCreateAppointments.setPrefWidth(400);
        GridPane.setHalignment(btnClinCreateAppointments, HPos.CENTER);
        GridPane.setMargin(btnClinCreateAppointments, new Insets(20, 0,20,0));
        btnClinCreateAppointments.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinCreateAppointments.setOnAction(event -> {
            primaryStage.setScene(sceneClinicianCreateAppointment);
        });
        
        // Add buttons hbox
        HBox hboxCancelOrCreate = new HBox(5);
        GridPane.setMargin(hboxCancelOrCreate, new Insets(20, 0,20,0));
        gridPaneClinicianViewAppointments.add(hboxCancelOrCreate, 0, 5, 5, 1);
        hboxCancelOrCreate.getChildren().addAll(btnClinViewAppointmentsCancel, btnClinCreateAppointments);
        GridPane.setHalignment(hboxCancelOrCreate, HPos.CENTER);
        
        //Add home button
        Button btnClinViewAppointmentsBack = new Button("Click here to return to the previous page");
        btnClinViewAppointmentsBack.setPrefHeight(40);
        btnClinViewAppointmentsBack.setDefaultButton(true);
        btnClinViewAppointmentsBack.setPrefWidth(400);
        GridPane.setHalignment(btnClinViewAppointmentsBack, HPos.CENTER);
        gridPaneClinicianViewAppointments.add(btnClinViewAppointmentsBack, 0, 6, 5, 1);
        GridPane.setMargin(btnClinViewAppointmentsBack, new Insets(20, 0,20,0));
        btnClinViewAppointmentsBack.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinViewAppointmentsBack.setOnAction(event -> {
            if(clinicianLoggedIn == true) {
                primaryStage.setScene(sceneClinicianHome);
            } else if(adminLoggedIn == true) {
                primaryStage.setScene(sceneAdminHome);
            }
        });
        
        // Creating a scene object
        sceneViewAppointments = new Scene(gridPaneClinicianViewAppointments, 1000, 750);
        
        /* ------------------------------------------------------------------------
                            CLINICIAN CREATE APPOINTMENTS PAGE
        ------------------------------------------------------------------------ */
        
        // Create the registration form grid pane
        GridPane gridPaneClinicianCreateAppointments = new GridPane();
        
        // Position the pane at the middle of the screen
        gridPaneClinicianCreateAppointments.setAlignment(Pos.CENTER);

        // Set a padding 
        gridPaneClinicianCreateAppointments.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap 
        gridPaneClinicianCreateAppointments.setHgap(10);

        // Set the vertical gap 
        gridPaneClinicianCreateAppointments.setVgap(5);

        //Background colour
        gridPaneClinicianCreateAppointments.setStyle("-fx-background-color: #f0f5f5;");
        
        // Add Header
        Label lblClinCreateAppointmentsHeader = new Label("Create a new appointment");
        lblClinCreateAppointmentsHeader.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneClinicianCreateAppointments.add(lblClinCreateAppointmentsHeader, 0,0,5,1);
        GridPane.setHalignment(lblClinCreateAppointmentsHeader, HPos.CENTER);
        GridPane.setMargin(lblClinCreateAppointmentsHeader, new Insets(20, 0, 20, 0));
        
        // Add name label
        Label lblClinBookAppointmentName = new Label("Enter the Patient's name:");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentName, 0, 1);
        
        // Add name textfield
        TextField txtClinAppointmentName = new TextField();
        gridPaneClinicianCreateAppointments.add(txtClinAppointmentName, 1, 1);
        
        // Add email label
        Label lblClinBookAppointmentEmail = new Label("Enter the Patient's Email:");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentEmail, 0, 2);
        
        // Add email textfield
        TextField txtClinAppointmentEmail = new TextField();
        gridPaneClinicianCreateAppointments.add(txtClinAppointmentEmail, 1, 2);
        
        // Add date label
        Label lblClinBookAppointmentDate = new Label("Pick an appointment date:");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentDate, 0, 3);

        // Add datebox
        DatePicker clinAppointmentDate = new DatePicker();
        gridPaneClinicianCreateAppointments.add(clinAppointmentDate, 1, 3);
        clinAppointmentDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        
        // Add time label
        Label lblClinBookAppointmentTime = new Label("Pick an appointment time:");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentTime, 0,4);
        
        // Add time combobox
        ComboBox cboClinAppointmentTime = new ComboBox();
        gridPaneClinicianCreateAppointments.add(cboClinAppointmentTime, 1,4);
        cboClinAppointmentTime.getItems().addAll("8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00");
        
        // Add location label
        Label lblClinBookAppointmentLocation = new Label("Pick an appointment location:");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentLocation, 0,5);
        
        // Add time combobox
        ComboBox cboClinBookAppointmentLocation = new ComboBox();
        gridPaneClinicianCreateAppointments.add(cboClinBookAppointmentLocation, 1,5);
        int searchClinVaccineCentreList = vaccineCentresArray.size();
        for(int i = 0; i < searchClinVaccineCentreList; i++) {
            cboClinBookAppointmentLocation.getItems().add(vaccineCentresArray.get(i).getVcName());
        }
        
        // Add dose label
        Label lblClinBookAppointmentDose = new Label("Patient received their first dose?");
        gridPaneClinicianCreateAppointments.add(lblClinBookAppointmentDose, 0,6);
        
        // Add dose checkbox
        CheckBox cbClinBookAppointmentDose = new CheckBox("They've received their first dose.");
        gridPaneClinicianCreateAppointments.add(cbClinBookAppointmentDose, 1,6);
        
        // Add Submit Button
        Button btnClinBookAppointment = new Button("Book Appointment");
        btnClinBookAppointment.setPrefHeight(40);
        btnClinBookAppointment.setDefaultButton(true);
        btnClinBookAppointment.setPrefWidth(200);
        gridPaneClinicianCreateAppointments.add(btnClinBookAppointment, 1, 7, 1, 1);
        GridPane.setHalignment(btnClinBookAppointment, HPos.CENTER);
        GridPane.setMargin(btnClinBookAppointment, new Insets(20, 0,20,0));
        btnClinBookAppointment.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinBookAppointment.setOnAction((ActionEvent event) -> {
            // Validation
            if(txtClinAppointmentName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "Please enter a name!");
            } else if(txtClinAppointmentEmail.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "Please enter an email!");
            } else if(clinAppointmentDate.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "Please pick a date!");
            } else if(cboClinAppointmentTime.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "Please pick a time!");
            } else if(cboClinBookAppointmentLocation.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "Please pick an appointment location!");
            } else { 
                int searchList = patientsArray.size();
                Boolean userDoesNotExist = true;
                for(int i = 0; i < searchList; i++) {
                    if(patientsArray.get(i).getEmail().equals(txtClinAppointmentEmail.getText())) {
                        userDoesNotExist = false;
                        patientsArray.get(i).setStatus("SCHEDULED");
                        UserManagement.refreshPatientFile();
                        showAlert(Alert.AlertType.INFORMATION, gridPaneUserBookAppointment.getScene().getWindow(), "Registration Successful!", "Appointment for: " + txtClinAppointmentName.getText() + " confirmed.");

                        int vaccineDoseNumber = 1;

                        if(cbClinBookAppointmentDose.isSelected() == true) {
                            vaccineDoseNumber = 2;
                        }
                        // Format date to save in textfile
                        String date = clinAppointmentDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        AppointmentManagement.addAppointment(txtClinAppointmentEmail.getText(), txtClinAppointmentName.getText(), date, cboClinAppointmentTime.getValue().toString(), cboClinBookAppointmentLocation.getValue().toString(), null, vaccineDoseNumber, false, "PENDING");
                        tblClinViewAppointments.getItems().clear();
                        AppointmentManagement.refreshAppointmentFile();
                        appointmentsArray.forEach((Appointment appointments) -> {tblClinViewAppointments.getItems().add(appointments);});
                        tblClinViewAppointments.getSortOrder().add(colClinUserDate);
                        primaryStage.setScene(sceneViewAppointments);
                    }
                }
                
                if(userDoesNotExist == true) {
                    showAlert(Alert.AlertType.ERROR, gridPaneClinicianCreateAppointments.getScene().getWindow(), "Form Error", "User does not exist!");
                    txtClinAppointmentEmail.clear();
                }
                
             
            }
        });
        
        // Add Clear Button
        Button btnClinAppointmentClear = new Button("Clear");
        btnClinAppointmentClear.setPrefHeight(40);
        btnClinAppointmentClear.setPrefWidth(200);
        gridPaneClinicianCreateAppointments.add(btnClinAppointmentClear, 0, 7, 1, 1);
        GridPane.setHalignment(btnClinAppointmentClear, HPos.CENTER);
        GridPane.setMargin(btnClinAppointmentClear, new Insets(20, 0,20,0));
        btnClinAppointmentClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinAppointmentClear.setOnAction(event -> {
            txtClinAppointmentName.clear();
            txtClinAppointmentEmail.clear();
            clinAppointmentDate.getEditor().clear();
            cboClinAppointmentTime.setValue(null);
            cboClinBookAppointmentLocation.setValue(null);
            cbClinBookAppointmentDose.setSelected(false);
        });
        
        //Add home button
        Button btnClinCreateAppointmentHome = new Button("Click here to return to the previous page");
        btnClinCreateAppointmentHome.setPrefHeight(40);
        btnClinCreateAppointmentHome.setPrefWidth(400);
        gridPaneClinicianCreateAppointments.add(btnClinCreateAppointmentHome, 0, 8, 2, 1);
        GridPane.setHalignment(btnClinCreateAppointmentHome, HPos.CENTER);
        GridPane.setMargin(btnClinCreateAppointmentHome, new Insets(20, 0,20,0));
        btnClinCreateAppointmentHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnClinCreateAppointmentHome.setOnAction(event -> {
            primaryStage.setScene(sceneViewAppointments);
        });
        
        // Creating a scene object
        sceneClinicianCreateAppointment = new Scene(gridPaneClinicianCreateAppointments, 550, 400);
        
        /* ------------------------------------------------------------------------
                                    ADMIN HOME PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneAdminHome = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminHome.setMinSize(400, 250);

        //Setting the padding  
        gridPaneAdminHome.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminHome.setVgap(10); 
        gridPaneAdminHome.setHgap(2);       

        //setting the alignment
        gridPaneAdminHome.setAlignment(Pos.CENTER);

        //Add header  
        Label lblAdminSelectOption = new Label("Welcome admin.\nPlease Select an Option:");
        lblAdminSelectOption.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminHome.add(lblAdminSelectOption, 0, 0, 2, 1);
        GridPane.setHalignment(lblAdminSelectOption, HPos.CENTER);
        GridPane.setMargin(lblAdminSelectOption, new Insets(10, 0, 20, 0));
        
        // Add view patients button
        Button btnAdminViewPatients = new Button("View Patients");
        btnAdminViewPatients.setPrefHeight(40);
        btnAdminViewPatients.setPrefWidth(210);
        btnAdminViewPatients.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminViewPatients.setOnAction(event -> {
            primaryStage.setScene(sceneViewPatients);
        });
        
        // Add edit vaccine info button
        Button btnAdminEditVaccineInfo = new Button("Edit Vaccine Info");
        btnAdminEditVaccineInfo.setPrefHeight(40);
        btnAdminEditVaccineInfo.setPrefWidth(210);
        btnAdminEditVaccineInfo.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminEditVaccineInfo.setOnAction(event -> {
            primaryStage.setScene(sceneAdminEditVaccineInfo);
        });
        
        // HBox for patients/vaccine info
        HBox hboxPatientsAndVaccine = new HBox(10);
        hboxPatientsAndVaccine.getChildren().addAll(btnAdminViewPatients, btnAdminEditVaccineInfo);
        gridPaneAdminHome.add(hboxPatientsAndVaccine, 0, 1, 2, 1);
        GridPane.setHalignment(hboxPatientsAndVaccine, HPos.CENTER);
        GridPane.setMargin(hboxPatientsAndVaccine, new Insets(0, 0,20,0));
        
        // Add view questionnaires button
        Button btnAdminViewQuestionnaires = new Button("View Questionnaires");
        btnAdminViewQuestionnaires.setPrefHeight(40);
        btnAdminViewQuestionnaires.setPrefWidth(210);
        btnAdminViewQuestionnaires.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminViewQuestionnaires.setOnAction(event -> {
            primaryStage.setScene(sceneViewQuestionnaires);
        });
        
        // Add edit vaccine centres button
        Button btnAdminEditVaccineCentres = new Button("Edit Vaccine Centres");
        btnAdminEditVaccineCentres.setPrefHeight(40);
        btnAdminEditVaccineCentres.setPrefWidth(210);
        btnAdminEditVaccineCentres.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminEditVaccineCentres.setOnAction(event -> {
            primaryStage.setScene(sceneAdminEditVaccineCentres);
        });
        
        // HBox for questionnaires/vaccine centres
        HBox hboxQuestionnairesAndCentres = new HBox(10);
        hboxQuestionnairesAndCentres.getChildren().addAll(btnAdminViewQuestionnaires, btnAdminEditVaccineCentres);
        gridPaneAdminHome.add(hboxQuestionnairesAndCentres, 0, 2, 2, 1);
        GridPane.setHalignment(hboxQuestionnairesAndCentres, HPos.CENTER);
        GridPane.setMargin(hboxQuestionnairesAndCentres, new Insets(0, 0,20,0));
        
        // Add view appointments button
        Button btnAdminViewAppointments = new Button("View Appointments");
        btnAdminViewAppointments.setPrefHeight(40);
        btnAdminViewAppointments.setPrefWidth(400);
        gridPaneAdminHome.add(btnAdminViewAppointments, 0, 3, 2, 1);
        GridPane.setHalignment(btnAdminViewAppointments, HPos.CENTER);
        GridPane.setMargin(btnAdminViewAppointments, new Insets(0, 0,20,0));
        btnAdminViewAppointments.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminViewAppointments.setOnAction(event -> {
            primaryStage.setScene(sceneViewAppointments);
        });
        
        // Add logout button
        Button btnAdminLogout = new Button("Logout of Vaccine System");
        btnAdminLogout.setPrefHeight(40);
        btnAdminLogout.setDefaultButton(true);
        btnAdminLogout.setPrefWidth(400);
        gridPaneAdminHome.add(btnAdminLogout, 0, 4, 2, 1);
        GridPane.setHalignment(btnAdminLogout, HPos.CENTER);
        GridPane.setMargin(btnAdminLogout, new Insets(0, 0,20,0));
        btnAdminLogout.setStyle("-fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminLogout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close this application?", ButtonType.YES, ButtonType.NO);

            // clicking X also means no
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                System.exit(0);
            }
        });
        
        // Creating a scene object 
        sceneAdminHome = new Scene(gridPaneAdminHome, 475, 450);
        
        /* ------------------------------------------------------------------------
                                EDIT VACCINE INFO PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneAdminEditVaccineInfo = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminEditVaccineInfo.setMinSize(400, 250);

        //Setting the padding  
        gridPaneAdminEditVaccineInfo.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminEditVaccineInfo.setVgap(10); 
        gridPaneAdminEditVaccineInfo.setHgap(2);       

        //setting the alignment
        gridPaneAdminEditVaccineInfo.setAlignment(Pos.CENTER);

        //Add header  
        Label lblAdminEditVaccineInfo = new Label("Edit Vaccine Info");
        lblAdminEditVaccineInfo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminEditVaccineInfo.add(lblAdminEditVaccineInfo, 0, 0, 2, 1);
        GridPane.setHalignment(lblAdminEditVaccineInfo, HPos.CENTER);
        GridPane.setMargin(lblAdminEditVaccineInfo, new Insets(10, 0, 20, 0));
        
        // Add name label
        Label lblEditVaccinesName = new Label("Select a vaccine:");
        
        // Combobox for vaccines
        ComboBox cboEditVaccinesName = new ComboBox();
        vaccinesArray.forEach((Vaccine vaccine) -> {cboEditVaccinesName.getItems().add(vaccine.getVaccineName());});
        cboEditVaccinesName.getSelectionModel().selectFirst();
        
        // Names Hbox
        HBox hboxEditVaccinesName = new HBox(40);
        hboxEditVaccinesName.getChildren().addAll(lblEditVaccinesName, cboEditVaccinesName);
        gridPaneAdminEditVaccineInfo.add(hboxEditVaccinesName, 0, 1);
        
        // Add description label
        Label lblEditVaccinesDescription = new Label("Vaccine description:");
        
        // TextArea for vaccine description
        TextArea txtEditVaccinesDescription = new TextArea();
        txtEditVaccinesDescription.setPrefRowCount(5);
        txtEditVaccinesDescription.setPrefWidth(250);
        txtEditVaccinesDescription.setWrapText(true);
        int vaccineNameSearchList = vaccinesArray.size();
        for(int i = 0; i < vaccineNameSearchList; i++) {
            if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                txtEditVaccinesDescription.setText(vaccinesArray.get(i).getVaccineDescription());
            }
        }
        
        // Description Hbox
        HBox hboxEditVaccinesDescription = new HBox(20);
        hboxEditVaccinesDescription.getChildren().addAll(lblEditVaccinesDescription, txtEditVaccinesDescription);
        gridPaneAdminEditVaccineInfo.add(hboxEditVaccinesDescription, 0, 2);
        
        // Add conditions label
        Label lblEditVaccinesConditions = new Label("Vaccine storage\nconditions:");
        
        // TextField for vaccine conditions
        TextField txtEditVaccinesConditions = new TextField();
        txtEditVaccinesConditions.setPrefWidth(250);
        for(int i = 0; i < vaccineNameSearchList; i++) {
            if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                txtEditVaccinesConditions.setText(vaccinesArray.get(i).getVaccineStorageConditions());
            }
        }
        
        // Conditions Hbox
        HBox hboxEditVaccinesConditions = new HBox(40);
        hboxEditVaccinesConditions.getChildren().addAll(lblEditVaccinesConditions, txtEditVaccinesConditions);
        gridPaneAdminEditVaccineInfo.add(hboxEditVaccinesConditions, 0, 3);
        
        // Add Expiry label
        Label lblEditVaccinesExpiry = new Label("Batch expiry date:");
        
        // TextField for vaccine conditions
        TextField txtEditVaccinesExpiry = new TextField();
        txtEditVaccinesExpiry.setPrefWidth(250);
        for(int i = 0; i < vaccineNameSearchList; i++) {
            if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                txtEditVaccinesExpiry.setText(vaccinesArray.get(i).getVaccineExpiryDate());
            }
        }
        
        // Expiry Hbox
        HBox hboxEditVaccinesExpiry = new HBox(33);
        hboxEditVaccinesExpiry.getChildren().addAll(lblEditVaccinesExpiry, txtEditVaccinesExpiry);
        gridPaneAdminEditVaccineInfo.add(hboxEditVaccinesExpiry, 0, 4);
        
        // Add Quantity label
        Label lblEditVaccinesQuantity = new Label("Current stocked quantity:");
        
        // TextField for Quantity
        TextField txtEditVaccinesQuantity = new TextField();
        txtEditVaccinesQuantity.setPrefWidth(218);
        for(int i = 0; i < vaccineNameSearchList; i++) {
            if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                txtEditVaccinesQuantity.setText(Integer.toString(vaccinesArray.get(i).getVaccineQuantity()));
            }
        }
        
        // Quantity Hbox
        HBox hboxEditVaccinesQuantity = new HBox(25);
        hboxEditVaccinesQuantity.getChildren().addAll(lblEditVaccinesQuantity, txtEditVaccinesQuantity);
        gridPaneAdminEditVaccineInfo.add(hboxEditVaccinesQuantity, 0, 5);
        
        // populate Vaccine Details 
        cboEditVaccinesName.setOnAction(event -> {
            int editVaccineSearch = vaccinesArray.size();
            for(int i = 0; i < editVaccineSearch; i++) {
                if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                    txtEditVaccinesDescription.setText(vaccinesArray.get(i).getVaccineDescription());
                    txtEditVaccinesConditions.setText(vaccinesArray.get(i).getVaccineStorageConditions());
                    txtEditVaccinesExpiry.setText(vaccinesArray.get(i).getVaccineExpiryDate());
                    txtEditVaccinesQuantity.setText(Integer.toString(vaccinesArray.get(i).getVaccineQuantity()));
                }
            }
        });
        
        //Add update button
        Button btnEditVaccine = new Button("Update selected vaccine");
        btnEditVaccine.setPrefHeight(40);
        btnEditVaccine.setPrefWidth(400);
        GridPane.setHalignment(btnEditVaccine, HPos.CENTER);
        gridPaneAdminEditVaccineInfo.add(btnEditVaccine, 0, 6, 5, 1);
        GridPane.setMargin(btnEditVaccine, new Insets(20, 0,10,0));
        btnEditVaccine.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnEditVaccine.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this profile?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                // Validation
                if(txtEditVaccinesDescription.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Form Error", "Please enter a description");
                } else if(txtEditVaccinesConditions.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Form Error", "Please enter storage conditions");
                } else if(txtEditVaccinesExpiry.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Form Error", "Please pick an expiry date");
                } else if(txtEditVaccinesQuantity.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Form Error", "Please enter a quantity");
                } else { 
                    int searchList = vaccinesArray.size();
                    for(int i = 0; i < searchList; i++) {
                        if(vaccinesArray.get(i).getVaccineName() == cboEditVaccinesName.getValue()) {
                            vaccinesArray.get(i).setVaccineDescription(txtEditVaccinesDescription.getText());
                            vaccinesArray.get(i).setVaccineStorageConditions(txtEditVaccinesConditions.getText());
                            vaccinesArray.get(i).setVaccineExpiryDate(txtEditVaccinesExpiry.getText());
                            vaccinesArray.get(i).setVaccineQuantity(Integer.parseInt(txtEditVaccinesQuantity.getText()));
                            VaccineManagement.refreshVaccinesFile();
                            showAlert(Alert.AlertType.INFORMATION, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Success", "Vaccine Updated");
                            cboEditVaccinesName.getItems().clear();
                            for(int j = 0; j < vaccinesArray.size(); j++) {
                                cboEditVaccinesName.getItems().add(vaccinesArray.get(j).getVaccineName());
                            }
                            cboEditVaccinesName.getSelectionModel().select(i);
                        }
                    }   
                }
            }
        });
        
        //Add delete button
        Button btnDeleteVaccine = new Button("Delete selected vaccine");
        btnDeleteVaccine.setPrefHeight(40);
        btnDeleteVaccine.setPrefWidth(400);
        GridPane.setHalignment(btnDeleteVaccine, HPos.CENTER);
        gridPaneAdminEditVaccineInfo.add(btnDeleteVaccine, 0, 7, 5, 1);
        btnDeleteVaccine.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnDeleteVaccine.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this vaccine?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                String nameToRemove = cboEditVaccinesName.getValue().toString();
                Vaccine vaccineToRemove = null;
                for(int i = 0; i < vaccinesArray.size(); i++) {
                    if(vaccinesArray.get(i).getVaccineName().equals(nameToRemove)) {
                        vaccineToRemove = vaccinesArray.get(i);
                    }
                }
                
                vaccinesArray.remove(vaccineToRemove);
                VaccineManagement.refreshVaccinesFile();
                showAlert(Alert.AlertType.INFORMATION, gridPaneAdminEditVaccineInfo.getScene().getWindow(), "Success", "Vaccine Deleted");
                txtEditVaccinesDescription.clear();
                txtEditVaccinesConditions.clear();
                txtEditVaccinesExpiry.clear();
                txtEditVaccinesQuantity.clear();
                cboEditVaccinesName.getItems().clear();
                vaccinesArray.forEach((Vaccine vaccine) -> {
                    cboEditVaccinesName.getItems().add(vaccine.getVaccineName());
                });
                cboEditVaccinesName.getSelectionModel().selectFirst();
                for(int i = 0; i < vaccineNameSearchList; i++) {
                    if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                        txtEditVaccinesDescription.setText(vaccinesArray.get(i).getVaccineDescription());
                        txtEditVaccinesConditions.setText(vaccinesArray.get(i).getVaccineStorageConditions());
                        txtEditVaccinesExpiry.setText(vaccinesArray.get(i).getVaccineExpiryDate());
                        txtEditVaccinesQuantity.setText(Integer.toString(vaccinesArray.get(i).getVaccineQuantity()));
                    }
                }
            }
        });
        
        //Add create vaccine button
        Button btnAdminCreateNewVaccine = new Button("Create a new vaccine record");
        btnAdminCreateNewVaccine.setPrefHeight(40);
        btnAdminCreateNewVaccine.setPrefWidth(400);
        gridPaneAdminEditVaccineInfo.add(btnAdminCreateNewVaccine, 0, 8, 2, 1);
        GridPane.setHalignment(btnAdminCreateNewVaccine, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateNewVaccine, new Insets(10, 0,0,0));
        btnAdminCreateNewVaccine.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateNewVaccine.setOnAction(event -> {
            primaryStage.setScene(sceneAdminCreateVaccine);
        });
        
        //Add home button
        Button btnAdminEditVaccineInfoHome = new Button("Click here to return to the previous page");
        btnAdminEditVaccineInfoHome.setPrefHeight(40);
        btnAdminEditVaccineInfoHome.setPrefWidth(400);
        btnAdminEditVaccineInfoHome.setDefaultButton(true);
        gridPaneAdminEditVaccineInfo.add(btnAdminEditVaccineInfoHome, 0, 9, 2, 1);
        GridPane.setHalignment(btnAdminEditVaccineInfoHome, HPos.CENTER);
        GridPane.setMargin(btnAdminEditVaccineInfoHome, new Insets(10, 0,0,0));
        btnAdminEditVaccineInfoHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminEditVaccineInfoHome.setOnAction(event -> {
            primaryStage.setScene(sceneAdminHome);
        });
        
        // Creating a scene object 
        sceneAdminEditVaccineInfo = new Scene(gridPaneAdminEditVaccineInfo, 500, 650);
        
        /* ------------------------------------------------------------------------
                                CREATE VACCINE PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneAdminCreateVaccine = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminCreateVaccine.setMinSize(400, 250);

        //Setting the padding  
        gridPaneAdminCreateVaccine.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminCreateVaccine.setVgap(10); 
        gridPaneAdminCreateVaccine.setHgap(10);       

        //setting the alignment
        gridPaneAdminCreateVaccine.setAlignment(Pos.CENTER);

        //Add header  
        Label lblAdminCreateVaccine = new Label("Create a new vaccine record");
        lblAdminCreateVaccine.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminCreateVaccine.add(lblAdminCreateVaccine, 0, 0, 2, 1);  
        lblAdminCreateVaccine.setPadding(new Insets(0, 0, 20, 0)); 
        GridPane.setHalignment(lblAdminCreateVaccine, HPos.CENTER);
        
        // Add name label
        Label lblCreateVaccineName = new Label("Enter the Vaccine's name:");
        gridPaneAdminCreateVaccine.add(lblCreateVaccineName, 0, 1);
        
        // Add name textfield
        TextField txtCreateVaccineName = new TextField();
        txtCreateVaccineName.setMaxWidth(220);
        gridPaneAdminCreateVaccine.add(txtCreateVaccineName, 1, 1);
        
        // Add description label
        Label lblCreateVaccineDescription = new Label("Enter the Vaccine's\ndescription:");
        gridPaneAdminCreateVaccine.add(lblCreateVaccineDescription, 0, 2);
        
        // Add description textfield
        TextArea txtCreateVaccineDescription = new TextArea();
        txtCreateVaccineDescription.setMaxWidth(220);
        txtCreateVaccineDescription.setPrefRowCount(4);
        txtCreateVaccineDescription.setWrapText(true);
        gridPaneAdminCreateVaccine.add(txtCreateVaccineDescription, 1, 2);
        
        // Add conditions label
        Label lblCreateVaccineConditions = new Label("Enter the Vaccine's\nstorage conditions:");
        gridPaneAdminCreateVaccine.add(lblCreateVaccineConditions, 0, 3);
        
        // Add conditions textfield
        TextField txtCreateVaccineConditions = new TextField();
        txtCreateVaccineConditions.setMaxWidth(220);
        gridPaneAdminCreateVaccine.add(txtCreateVaccineConditions, 1, 3);
        
        // Add expiry label
        Label lblCreateVaccineExpiry = new Label("Enter the batch's expiry date:");
        gridPaneAdminCreateVaccine.add(lblCreateVaccineExpiry, 0, 4);
        
        // Add expiry textfield
        TextField txtCreateVaccineExpiry = new TextField();
        txtCreateVaccineExpiry.setMaxWidth(220);
        gridPaneAdminCreateVaccine.add(txtCreateVaccineExpiry, 1, 4);
        
        // Add quantity label
        Label lblCreateVaccineQuantity = new Label("Enter the current stocked\nquantity:");
        gridPaneAdminCreateVaccine.add(lblCreateVaccineQuantity, 0, 5);
        
        // Add quantity textfield
        TextField txtCreateVaccineQuantity = new TextField();
        txtCreateVaccineQuantity.setMaxWidth(220);
        gridPaneAdminCreateVaccine.add(txtCreateVaccineQuantity, 1, 5);
        
        //Add create button
        Button btnAdminCreateVaccine = new Button("Create vaccine record");
        btnAdminCreateVaccine.setPrefHeight(40);
        btnAdminCreateVaccine.setPrefWidth(200);
        gridPaneAdminCreateVaccine.add(btnAdminCreateVaccine, 0, 6, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccine, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccine, new Insets(5, 0,0,0));
        btnAdminCreateVaccine.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccine.setOnAction(event -> {
            // Validation
            if(txtCreateVaccineName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter the vaccine's name");
            } else if(txtCreateVaccineDescription.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter the vaccine's descripton");
            } else if(txtCreateVaccineConditions.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter the vaccine's storage conditions");
            } else if(txtCreateVaccineExpiry.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter the batch's expiry date");
            } else if(txtCreateVaccineQuantity.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter the batch's stocked quantity");
            } else if (isNumeric(txtCreateVaccineQuantity.getText()) == false) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccine.getScene().getWindow(), "Form Error", "Please enter a numeric quantity");
            } else {
                VaccineManagement.addVaccine(txtCreateVaccineName.getText(), txtCreateVaccineDescription.getText(), txtCreateVaccineConditions.getText(), txtCreateVaccineExpiry.getText(), Integer.parseInt(txtCreateVaccineQuantity.getText()));
                showAlert(Alert.AlertType.INFORMATION, gridPaneAdminCreateVaccine.getScene().getWindow(), "Success", "Vaccine created");
                txtCreateVaccineName.setText(null);
                txtCreateVaccineDescription.setText(null);
                txtCreateVaccineConditions.setText(null);
                txtCreateVaccineExpiry.setText(null);
                txtCreateVaccineQuantity.setText(null);
                txtEditVaccinesDescription.clear();
                txtEditVaccinesConditions.clear();
                txtEditVaccinesExpiry.clear();
                txtEditVaccinesQuantity.clear();
                cboEditVaccinesName.getItems().clear();
                vaccinesArray.forEach((Vaccine vaccine) -> {
                    cboEditVaccinesName.getItems().add(vaccine.getVaccineName());
                });
                cboEditVaccinesName.getSelectionModel().selectFirst();
                for(int i = 0; i < vaccineNameSearchList; i++) {
                    if(vaccinesArray.get(i).getVaccineName().equals(cboEditVaccinesName.getValue())) {
                        txtEditVaccinesDescription.setText(vaccinesArray.get(i).getVaccineDescription());
                        txtEditVaccinesConditions.setText(vaccinesArray.get(i).getVaccineStorageConditions());
                        txtEditVaccinesExpiry.setText(vaccinesArray.get(i).getVaccineExpiryDate());
                        txtEditVaccinesQuantity.setText(Integer.toString(vaccinesArray.get(i).getVaccineQuantity()));
                    }
                }
                primaryStage.setScene(sceneAdminEditVaccineInfo);
            }
        });
        
        //Add clear button
        Button btnAdminCreateVaccineClear = new Button("Clear all fields");
        btnAdminCreateVaccineClear.setPrefHeight(40);
        btnAdminCreateVaccineClear.setPrefWidth(200);
        gridPaneAdminCreateVaccine.add(btnAdminCreateVaccineClear, 0, 7, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccineClear, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccineClear, new Insets(10, 0,0,0));
        btnAdminCreateVaccineClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccineClear.setOnAction(event -> {
            txtCreateVaccineName.setText(null);
            txtCreateVaccineDescription.setText(null);
            txtCreateVaccineConditions.setText(null);
            txtCreateVaccineExpiry.setText(null);
            txtCreateVaccineQuantity.setText(null);
        });
        
        //Add home button
        Button btnAdminCreateVaccineHome = new Button("Click here to return to the previous page");
        btnAdminCreateVaccineHome.setPrefHeight(40);
        btnAdminCreateVaccineHome.setPrefWidth(360);
        btnAdminCreateVaccineHome.setDefaultButton(true);
        gridPaneAdminCreateVaccine.add(btnAdminCreateVaccineHome, 0, 8, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccineHome, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccineHome, new Insets(10, 0,0,0));
        btnAdminCreateVaccineHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccineHome.setOnAction(event -> {
            txtCreateVaccineName.setText(null);
            txtCreateVaccineDescription.setText(null);
            txtCreateVaccineConditions.setText(null);
            txtCreateVaccineExpiry.setText(null);
            txtCreateVaccineQuantity.setText(null);
            primaryStage.setScene(sceneAdminEditVaccineInfo);
        });
        
        // Creating a scene object
        sceneAdminCreateVaccine = new Scene(gridPaneAdminCreateVaccine, 470, 575);
        
        /* ------------------------------------------------------------------------
                                EDIT VACCINE CENTRES PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneAdminEditVaccineCentres = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminEditVaccineCentres.setMinSize(400, 250);

        //Setting the padding  
        gridPaneAdminEditVaccineCentres.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminEditVaccineCentres.setVgap(10); 
        gridPaneAdminEditVaccineCentres.setHgap(2);       

        //setting the alignment
        gridPaneAdminEditVaccineCentres.setAlignment(Pos.CENTER);

        //Add header  
        Label lblAdminEditVaccineCentres = new Label("Edit Vaccine Centres");
        lblAdminEditVaccineCentres.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminEditVaccineCentres.add(lblAdminEditVaccineCentres, 0, 0, 2, 1);
        GridPane.setHalignment(lblAdminEditVaccineCentres, HPos.CENTER);
        GridPane.setMargin(lblAdminEditVaccineCentres, new Insets(10, 0, 20, 0));
        
        // Add name label
        Label lblEditVaccineCentresName = new Label("Select a centre:");
        
        // Combobox for vaccine centres
        ComboBox cboEditVaccineCentresName = new ComboBox();
        vaccineCentresArray.forEach((VaccinationCentre vc) -> {cboEditVaccineCentresName.getItems().add(vc.getVcName());});
        cboEditVaccineCentresName.getSelectionModel().selectFirst();
        
        // Names Hbox
        HBox hboxEditVaccineCentresName = new HBox(40);
        hboxEditVaccineCentresName.getChildren().addAll(lblEditVaccineCentresName, cboEditVaccineCentresName);
        gridPaneAdminEditVaccineCentres.add(hboxEditVaccineCentresName, 0, 1);
        
        // Add location label
        Label lblEditVaccineCentresLocation = new Label("Centre location:");
        
        // TextField for centre location
        TextField txtEditVaccineCentresLocation = new TextField();
        txtEditVaccineCentresLocation.setPrefWidth(230);
        for(int i = 0; i < vaccineCentresArray.size(); i++) {
            if(vaccineCentresArray.get(i).getVcName().equals(cboEditVaccineCentresName.getValue())) {
                txtEditVaccineCentresLocation.setText(vaccineCentresArray.get(i).getVcLocation());
            }
        }
        
        // Location Hbox
        HBox hboxEditVaccineCentresLocation = new HBox(37);
        hboxEditVaccineCentresLocation.getChildren().addAll(lblEditVaccineCentresLocation, txtEditVaccineCentresLocation);
        gridPaneAdminEditVaccineCentres.add(hboxEditVaccineCentresLocation, 0, 2);
        
        // Add active label
        Label lblEditVaccineCentresActive = new Label("Centre Active?:");
        
        // Togglegroup for actively vaccinating
        ToggleGroup tgIsActivelyVaccinating = new ToggleGroup();
        
        // Radio Buttons for actively vaccinating
        RadioButton rbActiveTrue = new RadioButton("Yes");
        rbActiveTrue.setToggleGroup(tgIsActivelyVaccinating);
        RadioButton rbActiveFalse = new RadioButton("No");
        rbActiveFalse.setToggleGroup(tgIsActivelyVaccinating);
        for(int i = 0; i < vaccineCentresArray.size(); i++) {
            if(vaccineCentresArray.get(i).getVcName().equals(cboEditVaccineCentresName.getValue())) {
                if(vaccineCentresArray.get(i).getVcIsActive() == true) {
                    rbActiveTrue.setSelected(true);
                } else {
                    rbActiveFalse.setSelected(true);
                }
            }
        }
        
        // Actively vaccinating Hbox
        HBox hboxEditVaccineCentresActive = new HBox(45);
        hboxEditVaccineCentresActive.getChildren().addAll(lblEditVaccineCentresActive, rbActiveTrue, rbActiveFalse);
        gridPaneAdminEditVaccineCentres.add(hboxEditVaccineCentresActive, 0, 3);
        
        // populate Vaccine Details 
        cboEditVaccineCentresName.setOnAction(event -> {
            int editVaccineSearch = vaccineCentresArray.size();
            for(int i = 0; i < editVaccineSearch; i++) {
                if(vaccineCentresArray.get(i).getVcName().equals(cboEditVaccineCentresName.getValue())) {
                    txtEditVaccineCentresLocation.setText(vaccineCentresArray.get(i).getVcLocation());
                    if(vaccineCentresArray.get(i).getVcIsActive() == true) {
                        rbActiveTrue.setSelected(true);
                    } else {
                        rbActiveFalse.setSelected(true);
                    }
                }
            }
        });
        
        //Add update button
        Button btnEditVaccineCentre = new Button("Update selected centre");
        btnEditVaccineCentre.setPrefHeight(40);
        btnEditVaccineCentre.setPrefWidth(400);
        GridPane.setHalignment(btnEditVaccineCentre, HPos.CENTER);
        gridPaneAdminEditVaccineCentres.add(btnEditVaccineCentre, 0, 6, 2, 1);
        GridPane.setMargin(btnEditVaccineCentre, new Insets(10, 0,10,0));
        btnEditVaccineCentre.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnEditVaccineCentre.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to update this profile?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                System.out.println("yes");
                // Validation
                if(txtEditVaccineCentresLocation.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPaneAdminEditVaccineCentres.getScene().getWindow(), "Form Error", "Please enter a location");
                } else { 
                    for(int i = 0; i < vaccineCentresArray.size(); i++) {
                        if(vaccineCentresArray.get(i).getVcName() == cboEditVaccineCentresName.getValue()) {
                            vaccineCentresArray.get(i).setVcLocation(txtEditVaccineCentresLocation.getText());
                            if(rbActiveTrue.isSelected() == true) {
                                vaccineCentresArray.get(i).setVcIsActive(true);
                            } else if(rbActiveFalse.isSelected() == true) {
                                vaccineCentresArray.get(i).setVcIsActive(false);
                            }
                            VaccineManagement.refreshVaccineCentresFile();
                            showAlert(Alert.AlertType.INFORMATION, gridPaneAdminEditVaccineCentres.getScene().getWindow(), "Success", "Vaccine Centre Updated");
                            cboEditVaccineCentresName.getItems().clear();
                            for(int j = 0; j < vaccineCentresArray.size(); j++) {
                                cboEditVaccineCentresName.getItems().add(vaccineCentresArray.get(j).getVcName());
                            }
                            cboEditVaccineCentresName.getSelectionModel().select(i);
                        }
                    }   
                }
            }
        });
        
        //Add delete button
        Button btnDeleteVaccineCentre = new Button("Delete selected centre");
        btnDeleteVaccineCentre.setPrefHeight(40);
        btnDeleteVaccineCentre.setPrefWidth(400);
        GridPane.setHalignment(btnDeleteVaccineCentre, HPos.CENTER);
        gridPaneAdminEditVaccineCentres.add(btnDeleteVaccineCentre, 0, 7, 2, 1);
        btnDeleteVaccineCentre.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnDeleteVaccineCentre.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this vaccine centre?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.NO.equals(result)) {
                // consume event i.e. ignore close request 
                event.consume();
            } else {
                String nameToRemove = cboEditVaccineCentresName.getValue().toString();
                VaccinationCentre vcToRemove = null;
                for(int i = 0; i < vaccineCentresArray.size(); i++) {
                    if(vaccineCentresArray.get(i).getVcName().equals(nameToRemove)) {
                        vcToRemove = vaccineCentresArray.get(i);
                    }
                }
                vaccineCentresArray.remove(vcToRemove);
                VaccineManagement.refreshVaccineCentresFile();
                showAlert(Alert.AlertType.INFORMATION, gridPaneAdminEditVaccineCentres.getScene().getWindow(), "Success", "Vaccine Centre Deleted");
                txtEditVaccineCentresLocation.clear();
                cboEditVaccineCentresName.getItems().clear();
                vaccineCentresArray.forEach((VaccinationCentre vc) -> {
                    cboEditVaccineCentresName.getItems().add(vc.getVcName());
                });
                cboEditVaccineCentresName.getSelectionModel().selectFirst();
                for(int i = 0; i < vaccineNameSearchList; i++) {
                    if(vaccineCentresArray.get(i).getVcName().equals(cboEditVaccineCentresName.getValue())) {
                        txtEditVaccineCentresLocation.setText(vaccineCentresArray.get(i).getVcLocation());
                        if(vaccineCentresArray.get(i).getVcIsActive() == true) {
                            rbActiveTrue.setSelected(true);
                        } else {
                            rbActiveFalse.setSelected(true);
                        }
                    }
                }
            }
        });
        
        //Add create vaccine centre button
        Button btnAdminCreateNewVaccineCentre = new Button("Create a new vaccine centre");
        btnAdminCreateNewVaccineCentre.setPrefHeight(40);
        btnAdminCreateNewVaccineCentre.setPrefWidth(400);
        gridPaneAdminEditVaccineCentres.add(btnAdminCreateNewVaccineCentre, 0, 8, 2, 1);
        GridPane.setHalignment(btnAdminCreateNewVaccineCentre, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateNewVaccineCentre, new Insets(10, 0,0,0));
        btnAdminCreateNewVaccineCentre.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateNewVaccineCentre.setOnAction(event -> {
            primaryStage.setScene(sceneAdminCreateVaccineCentre);
        });
        
        //Add home button
        Button btnAdminEditVaccineCentresHome = new Button("Click here to return to the previous page");
        btnAdminEditVaccineCentresHome.setPrefHeight(40);
        btnAdminEditVaccineCentresHome.setPrefWidth(400);
        btnAdminEditVaccineCentresHome.setDefaultButton(true);
        gridPaneAdminEditVaccineCentres.add(btnAdminEditVaccineCentresHome, 0, 9, 2, 1);
        GridPane.setHalignment(btnAdminEditVaccineCentresHome, HPos.CENTER);
        GridPane.setMargin(btnAdminEditVaccineCentresHome, new Insets(10, 0,0,0));
        btnAdminEditVaccineCentresHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminEditVaccineCentresHome.setOnAction(event -> {
            primaryStage.setScene(sceneAdminHome);
        });
        
        // Creating a scene object 
        sceneAdminEditVaccineCentres = new Scene(gridPaneAdminEditVaccineCentres, 500, 550);
        
        /* ------------------------------------------------------------------------
                                CREATE VACCINE CENTRE PAGE
           ------------------------------------------------------------------------ */
        
        //Creating a Grid Pane 
        GridPane gridPaneAdminCreateVaccineCentre = new GridPane();    

        //Setting size for the pane 
        gridPaneAdminCreateVaccineCentre.setMinSize(400, 250);

        //Setting the padding  
        gridPaneAdminCreateVaccineCentre.setPadding(new Insets(40, 40, 40, 40)); 

        //Setting the vertical and horizontal gaps between the columns 
        gridPaneAdminCreateVaccineCentre.setVgap(10); 
        gridPaneAdminCreateVaccineCentre.setHgap(10);       

        //setting the alignment
        gridPaneAdminCreateVaccineCentre.setAlignment(Pos.CENTER);

        //Add header  
        Label lblAdminCreateVaccineCentre = new Label("Create a new vaccine record");
        lblAdminCreateVaccineCentre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPaneAdminCreateVaccineCentre.add(lblAdminCreateVaccineCentre, 0, 0, 2, 1);  
        lblAdminCreateVaccineCentre.setPadding(new Insets(0, 0, 20, 0)); 
        GridPane.setHalignment(lblAdminCreateVaccineCentre, HPos.CENTER);
        
        // Add name label
        Label lblCreateVaccineCentresName = new Label("Enter the centre's\nname:");
        
        // Textfield for vaccine centres
        TextField txtCreateVaccineCentresName = new TextField();
        
        // Names Hbox
        HBox hboxCreateVaccineCentresName = new HBox(20);
        hboxCreateVaccineCentresName.getChildren().addAll(lblCreateVaccineCentresName, txtCreateVaccineCentresName);
        gridPaneAdminCreateVaccineCentre.add(hboxCreateVaccineCentresName, 0, 1);
        
        // Add location label
        Label lblCreateVaccineCentresLocation = new Label("Centre's location:");
        
        // TextField for centre location
        TextField txtCreateVaccineCentresLocation = new TextField();
        
        // Location Hbox
        HBox hboxCreateVaccineCentresLocation = new HBox(20);
        hboxCreateVaccineCentresLocation.getChildren().addAll(lblCreateVaccineCentresLocation, txtCreateVaccineCentresLocation);
        gridPaneAdminCreateVaccineCentre.add(hboxCreateVaccineCentresLocation, 0, 2);
        
        // Add active label
        Label lblCreateVaccineCentresActive = new Label("Centre Active?:");
        
        // Togglegroup for actively vaccinating
        ToggleGroup tgCreateIsActivelyVaccinating = new ToggleGroup();
        
        // Radio Buttons for actively vaccinating
        RadioButton rbCreateActiveTrue = new RadioButton("Yes");
        rbCreateActiveTrue.setToggleGroup(tgCreateIsActivelyVaccinating);
        RadioButton rbCreateActiveFalse = new RadioButton("No");
        rbCreateActiveFalse.setToggleGroup(tgCreateIsActivelyVaccinating);
        rbCreateActiveFalse.setSelected(true);
        
        // Active vaccinating Hbox
        HBox hboxCreateVaccineCentresActivelyVaccinating = new HBox(40);
        hboxCreateVaccineCentresActivelyVaccinating.getChildren().addAll(lblCreateVaccineCentresActive, rbCreateActiveTrue, rbCreateActiveFalse);
        gridPaneAdminCreateVaccineCentre.add(hboxCreateVaccineCentresActivelyVaccinating, 0, 3);
        
        //Add create button
        Button btnAdminCreateVaccineCentre = new Button("Create vaccine centre record");
        btnAdminCreateVaccineCentre.setPrefHeight(40);
        btnAdminCreateVaccineCentre.setPrefWidth(250);
        gridPaneAdminCreateVaccineCentre.add(btnAdminCreateVaccineCentre, 0, 4, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccineCentre, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccineCentre, new Insets(5, 0,0,0));
        btnAdminCreateVaccineCentre.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccineCentre.setOnAction(event -> {
            // Validation
            if(txtCreateVaccineCentresName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccineCentre.getScene().getWindow(), "Form Error", "Please enter the centre's name");
            } else if(txtCreateVaccineCentresLocation.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, gridPaneAdminCreateVaccineCentre.getScene().getWindow(), "Form Error", "Please enter the centre's location");
            } else {
                Boolean isActive = null;
                if(rbCreateActiveTrue.isSelected()) {
                    isActive = true;
                } else {
                    isActive = false;
                }
                VaccineManagement.addVaccineCentre(txtCreateVaccineCentresName.getText(), txtCreateVaccineCentresLocation.getText(), isActive);
                showAlert(Alert.AlertType.INFORMATION, gridPaneAdminCreateVaccineCentre.getScene().getWindow(), "Success", "Vaccine centre created");
                txtCreateVaccineCentresName.setText(null);
                txtCreateVaccineCentresLocation.setText(null);
                rbCreateActiveFalse.setSelected(true);
                txtEditVaccineCentresLocation.clear();
                cboEditVaccineCentresName.getItems().clear();
                vaccineCentresArray.forEach((VaccinationCentre vc) -> {
                    cboEditVaccineCentresName.getItems().add(vc.getVcName());
                });
                cboEditVaccineCentresName.getSelectionModel().selectFirst();
                for(int i = 0; i < vaccineNameSearchList; i++) {
                    if(vaccineCentresArray.get(i).getVcName().equals(cboEditVaccineCentresName.getValue())) {
                        txtEditVaccineCentresLocation.setText(vaccineCentresArray.get(i).getVcLocation());
                        if(vaccineCentresArray.get(i).getVcIsActive() == true) {
                            rbActiveTrue.setSelected(true);
                        } else {
                            rbActiveFalse.setSelected(true);
                        }
                    }
                }
                primaryStage.setScene(sceneAdminEditVaccineCentres);
            }
        });
        
        //Add clear button
        Button btnAdminCreateVaccineCentreClear = new Button("Clear all fields");
        btnAdminCreateVaccineCentreClear.setPrefHeight(40);
        btnAdminCreateVaccineCentreClear.setPrefWidth(200);
        gridPaneAdminCreateVaccineCentre.add(btnAdminCreateVaccineCentreClear, 0, 5, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccineCentreClear, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccineCentreClear, new Insets(10, 0,0,0));
        btnAdminCreateVaccineCentreClear.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccineCentreClear.setOnAction(event -> {
            txtCreateVaccineName.setText(null);
            txtCreateVaccineDescription.setText(null);
            txtCreateVaccineConditions.setText(null);
            txtCreateVaccineExpiry.setText(null);
            txtCreateVaccineQuantity.setText(null);
        });
        
        //Add home button
        Button btnAdminCreateVaccineCentreHome = new Button("Click here to return to the previous page");
        btnAdminCreateVaccineCentreHome.setPrefHeight(40);
        btnAdminCreateVaccineCentreHome.setPrefWidth(360);
        btnAdminCreateVaccineCentreHome.setDefaultButton(true);
        gridPaneAdminCreateVaccineCentre.add(btnAdminCreateVaccineCentreHome, 0, 6, 2, 1);
        GridPane.setHalignment(btnAdminCreateVaccineCentreHome, HPos.CENTER);
        GridPane.setMargin(btnAdminCreateVaccineCentreHome, new Insets(10, 0,0,0));
        btnAdminCreateVaccineCentreHome.setStyle("-fx-font: 15px sans-serif; -fx-background-color: #87bdd8; -fx-text-fill: #000000; ");
        btnAdminCreateVaccineCentreHome.setOnAction(event -> {
            txtCreateVaccineName.setText(null);
            txtCreateVaccineDescription.setText(null);
            txtCreateVaccineConditions.setText(null);
            txtCreateVaccineExpiry.setText(null);
            txtCreateVaccineQuantity.setText(null);
            primaryStage.setScene(sceneAdminEditVaccineCentres);
        });
        
        // Creating a scene object 
        sceneAdminCreateVaccineCentre = new Scene(gridPaneAdminCreateVaccineCentre, 500, 550);
        
    }
    
    /* ------------------------------------------------------------------------
                        ADDITIONAL REQUIRED METHODS
        ------------------------------------------------------------------------ */
    
    // Alerts method
    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    
    // Populate user appointments table method
    private void populateUserAppointmentsTable(TableView table) {
        table.getItems().clear();
        appointmentsArray.forEach((Appointment appointments) -> {
            if(appointments.getAppointmentEmail().equals(userLoggedIn)) {
                table.getItems().add(appointments);
            }
        });
    }
    
    // Populate arrays method
    private static void populateArrays() {
        UserManagement.refreshPatients();
        UserManagement.refreshClinicians();
        UserManagement.refreshAdmins();
        UserManagement.refreshQuestionnaires();
        AppointmentManagement.refreshAppointments();
        VaccineManagement.refreshVaccineCentres();
        VaccineManagement.refreshVaccines();
    }

    // Populate Profile Method
    private void populateUserEditProfile(TextField name, TextField email, TextField password, TextField town, TextField eircode, TextField phoneNumber) {
        patientsArray.forEach((Patient patient) -> {
            if(patient.getEmail().equals(userLoggedIn)) {
                name.setText(patient.getName());
                email.setText(patient.getEmail());
                password.setText(patient.getPassword());
                town.setText(patient.getTown());
                eircode.setText(patient.getEircode());
                phoneNumber.setText(patient.getNumber());
            }
        });
    }
    
    // Create Questionnaire combobox population
    private Boolean populateCreateQuestionnaireCombo(ComboBox box) {
        int searchList = patientsArray.size();
        int count = 0;
        for(int i = 0; i < searchList; i++) {
            if(patientsArray.get(i).getQuestionnaireDone() == false) {
                box.getItems().add(patientsArray.get(i).getName());
                count++;
            }
        }
        box.getSelectionModel().selectFirst();
        return count != 0;
    }
    
    // Check is numeric boolean
    public static boolean isNumeric(String strNum) {
        boolean ret = true;
        try {
            Integer.parseInt(strNum); 
        } catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }
}

   
