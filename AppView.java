import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class AppView {
    private VBox view;
    private Button loginButton;
    private Button registerButton;

    private Button homeButton;
    private Button cartButton;
    private Button LanguageButton;
    private Button accountButton;

    private AppModel model;
    private AppController controller;
    private Stage primaryStage;
    private VBox contentBox;      // right-side panel that changes (Home / Cart / etc.)
    private User currentUser;     // the logged-in customer


    public AppView(AppController controller, AppModel model, Stage primaryStage) {

        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;

        createAndConfigurePane();
        createInitialLoginScreen();
        updateControllerFromListeners();
        observeModelAndUpdateControls();
    }

    public Parent asParent() {
        return view;
    }
    
    private void createAndConfigurePane() {
        view = new VBox(5);
        view.setAlignment(Pos.CENTER);
    }

    private void createInitialLoginScreen() {
        this.loginButton = new Button("Login");
        this.loginButton.setOnAction(event ->addLoginForm());

        this.registerButton = new Button("Register");
        this.registerButton.setOnAction(event ->addRegisterForm());

        HBox buttonRow = new HBox(5, loginButton, registerButton);
        buttonRow.setAlignment(Pos.CENTER);

        view.getChildren().add(buttonRow);
    }

    private void updateControllerFromListeners() {

    }

    private void observeModelAndUpdateControls() {

    }

    private VBox displayCustomerSidePanel() {
        homeButton = new Button("Home");
        homeButton.setAlignment(Pos.CENTER);

        cartButton = new Button("Cart");
        cartButton.setAlignment(Pos.CENTER);

        LanguageButton = new Button("Language");
        LanguageButton.setAlignment(Pos.CENTER);

        accountButton = new Button("Account");
        accountButton.setAlignment(Pos.CENTER);

        homeButton.setOnAction(e -> setContent(displayHomeScreen(currentUser)));
        cartButton.setOnAction(e -> setContent(displayCartScreen(currentUser)));
        LanguageButton.setOnAction(e -> setContent(displayLanguageScreen()));
        accountButton.setOnAction(e -> setContent(displayAccountScreen(currentUser)));

        VBox menuBox = new VBox(30, homeButton, cartButton, LanguageButton, accountButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPrefSize(120, 300);

        return menuBox;
    }

    
    private VBox displayHomeScreen(User user) {
        Label welcomeLabel = new Label("Welcome, " + user.getUserName() + "!");
        welcomeLabel.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Food Item");
        return new VBox(welcomeLabel, searchField);
    }

        private VBox displayCartScreen(User user) {
        Label title = new Label("Cart");
        title.setAlignment(Pos.CENTER);

        Label info = new Label("Your cart items will appear here.");
        info.setAlignment(Pos.CENTER);

        VBox box = new VBox(10, title, info);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox displayLanguageScreen() {
        Label title = new Label("Language");
        title.setAlignment(Pos.CENTER);

        RadioButton english = new RadioButton("English");
        RadioButton indonesian = new RadioButton("Indonesian");

        ToggleGroup group = new ToggleGroup();
        english.setToggleGroup(group);
        indonesian.setToggleGroup(group);
        english.setSelected(true);

        VBox box = new VBox(10, title, english, indonesian);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox displayAccountScreen(User user) {
        Label title = new Label("Account");
        title.setAlignment(Pos.CENTER);

        Label username = new Label("Username: " + user.getUserName());
        username.setAlignment(Pos.CENTER);

        // later you can add balance, edit profile, etc.
        VBox box = new VBox(10, title, username);
        box.setAlignment(Pos.CENTER);
        return box;
    }


    private void displayAlertScreen(String error) {
        Stage alert = new Stage();
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        VBox alertBox = new VBox(10, new Label(error));
        alertBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(alertBox, 200, 50);
        alert.setScene(scene);
        alert.show();
    }

        private void setContent(VBox newContent) {
        contentBox.getChildren().setAll(newContent.getChildren());
    }


    private void displayCustomerScreen(User user) {
        view.getChildren().clear();

        this.currentUser = user;

        VBox menuBox = displayCustomerSidePanel();

        contentBox = displayHomeScreen(user);

        HBox customerScreen = new HBox(5, menuBox, contentBox);

        view.getChildren().add(customerScreen);
    }


    private void addRegisterForm() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter In Username");
        HBox userNameRow = new HBox(5, new Label("Username:"), userNameField);
        userNameRow.setAlignment(Pos.CENTER);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter in Password");
        HBox passwordRow = new HBox(5, new Label("Password:"), passwordField);
        passwordRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(event -> {
            if (userNameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
                displayAlertScreen("Empty Fields!");
            } else if (passwordField.getText().length() < 7){
                displayAlertScreen("Password Minimum 8 letters!");
            }else if (userNameField.getText().trim().contains(" ")){
                displayAlertScreen("Username Can't Have Spaces!");
            }else {
                User t = new User(userNameField.getText(),passwordField.getText(), UserType.CUSTOMER, 1000);
                boolean valid = this.controller.register(t);
                if (valid){
                    stage.close();
                } else {
                    displayAlertScreen("Username already exists!");
                }
        }});

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox BtnRow = new HBox(submitBtn, cancelBtn);
        BtnRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, userNameRow, passwordRow, BtnRow);
        root.setAlignment(Pos.CENTER);

        Scene loginScene = new Scene(root, 300, 100);

        stage.setScene(loginScene);
        stage.show();
    }

    private void addLoginForm() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter In Username");
        HBox userNameRow = new HBox(5, new Label("Username:"), userNameField);
        userNameRow.setAlignment(Pos.CENTER);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter in Password");
        HBox passwordRow = new HBox(5, new Label("Password:"), passwordField);
        passwordRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(event -> {
            if (userNameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
                displayAlertScreen("Empty Fields!");
            } else if (passwordField.getText().length() < 8){
                displayAlertScreen("Password Minimum 8 letters!");
            }else {
                User t = new User(userNameField.getText().trim(),passwordField.getText().trim(), UserType.CUSTOMER, 1000);
                User valid = this.controller.login(t);
                if (valid != null) {
                    stage.close();
                    displayCustomerScreen(valid);
                } else {
                    displayAlertScreen("Invalid Credentials!");
                }           
            }
            });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox BtnRow = new HBox(submitBtn, cancelBtn);
        BtnRow.setAlignment(Pos.CENTER);
        

        VBox root = new VBox(10, userNameRow, passwordRow, BtnRow);
        root.setAlignment(Pos.CENTER);

        Scene loginScene = new Scene(root, 300, 100);

        stage.setScene(loginScene);
        stage.show();
    }
}
