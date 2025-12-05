import java.util.Collections;
import java.util.Comparator;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableRow;

import javafx.beans.binding.Bindings;           
import javafx.beans.value.ObservableValue;      
import javafx.collections.ObservableMap;        
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;



public class AppView {
    private TableView<Product> productTable;
    private VBox view;
    private Button loginButton;
    private Button registerButton;

    private Button homeButton;
    private Button cartButton;
    private Button LanguageButton;
    private Button accountButton;

    private TextField searchField;
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
         if (searchField != null) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        updateIfNeeded(newValue);
        });
        }
    }

    class SortByName implements Comparator<Product> {
        public int compare(Product a, Product b){
            return a.getName().get().compareTo(b.getName().get());
        }
    }
    
    private void updateIfNeeded(String search){
         if (search == null || search.trim().isEmpty()) {
        ObservableList<Product> searched = this.controller.getProductList();
        FXCollections.sort(searched);
        productTable.setItems(searched);
    } else {
        ObservableList<Product> searched = controller.searchProduct(search);
        
        FXCollections.sort(searched);
        productTable.setItems(searched);
    }
    }

    private void observeModelAndUpdateControls() {

    }

    private VBox displayCustomerSidePanel() {
        // use the fields so other methods can see them
        homeButton = new Button("Home");
        homeButton.setAlignment(Pos.CENTER);

        cartButton = new Button("Cart");
        cartButton.setAlignment(Pos.CENTER);

        LanguageButton = new Button("Language");
        LanguageButton.setAlignment(Pos.CENTER);

        accountButton = new Button("Account");
        accountButton.setAlignment(Pos.CENTER);

        // when side buttons are clicked, change the right-side content
        homeButton.setOnAction(e -> setContent(displayHomeScreen(currentUser)));
        cartButton.setOnAction(e -> displayCartScreen(currentUser));
        LanguageButton.setOnAction(e -> setContent(displayLanguageScreen()));
        accountButton.setOnAction(e -> setContent(displayAccountScreen(currentUser)));

        VBox menuBox = new VBox(30, homeButton, cartButton, LanguageButton, accountButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPrefSize(120, 300);
        return menuBox;
    }

    private void setContent(VBox newContent) {
        // replace children in the existing contentBox
        contentBox.getChildren().setAll(newContent.getChildren());
    }


    
    private VBox displayHomeScreen(User user) {
        Label welcomeLabel = new Label("Welcome, " + user.getUserName() + "!");
        welcomeLabel.setAlignment(Pos.CENTER);
        searchField = new TextField();
        searchField.setPromptText("Enter Food Item");

        productTable = new TableView<>();
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().getName());

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> cellData.getValue().getPriceLabel());

        TableColumn<Product, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().toString()));

        TableColumn<Product, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            SimpleIntegerProperty qty = new SimpleIntegerProperty(this.model.getItemAmt(p));

             this.model.itemCart.addListener((MapChangeListener<Product, Integer>) change -> {
                 qty.set(this.model.getItemAmt(p));
             });
            return qty.asString();
        });

        TableColumn<Product, Void> plusCol = new TableColumn<>("+");
        plusCol.setCellFactory(Col -> { return new TableCell<Product, Void>(){ 
            
            final Button addBtn = new Button("+");
            
        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                addBtn.setOnAction(null);
            } else {
                setGraphic(addBtn);
                addBtn.setOnAction(event -> {
                    Product productAdded = getTableView().getItems().get(getIndex());
                    AppView.this.controller.addCartItem(productAdded);          
                    System.out.println("Adding to cart: " + productAdded.getName().get());
                    getTableView().refresh();
                    getTableView().requestLayout();
                }); 
            }
            }
        };
        });

        TableColumn<Product, Void> minCol = new TableColumn<>("-");
        minCol.setCellFactory(Col -> { return new TableCell<Product, Void>(){ 
            
            final Button addBtn = new Button("-");
            
        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                addBtn.setOnAction(null);
            } else {
                setGraphic(addBtn);
                addBtn.setOnAction(event -> {
                    Product productAdded = getTableView().getItems().get(getIndex());
                    AppView.this.controller.deduceCartItem(productAdded);          
                    System.out.println("Reducing Item from Cart: " + productAdded.getName().get());
                    getTableView().refresh();
                    getTableView().requestLayout();
                }); 
            }
            }
        };
        });
        

        productTable.setPrefHeight(100);
        productTable.setPrefWidth(500);

        productTable.getColumns().addAll(nameCol, priceCol, catCol, plusCol, qtyCol, minCol);
        
        ObservableList<Product> a = this.controller.getProductList();
        FXCollections.sort(a);
        productTable.setItems(FXCollections.observableArrayList(a));

        updateControllerFromListeners();

        return new VBox(10, welcomeLabel, searchField, productTable);
    }



    private void displayCartScreen(User user) {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.NONE);
        stage.setTitle("Cart");

        ObservableMap<Product, Integer> cartMap = this.model.itemCartProperty();
        ObservableList<Product> liveCartMap = FXCollections.observableArrayList(cartMap.keySet());

        cartMap.addListener((MapChangeListener<Product, Integer>) change -> {
            if (change.wasAdded()) {
                if (!liveCartMap.contains(change.getKey())) {
                    liveCartMap.add(change.getKey());
                }
            }
            if (change.wasRemoved()) {
                if (!liveCartMap.contains(change.getKey())) {
                    liveCartMap.remove(change.getKey());
                }
                    
                //bro this does NOT work :broken:
            }
        });

        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(liveCartMap);
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().getName());        

        TableColumn<Product, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            SimpleIntegerProperty qty = new SimpleIntegerProperty(this.model.getItemAmt(p));

             this.controller.itemCartProperty().addListener((MapChangeListener<Product, Integer>) change -> {
                 qty.set(this.model.getItemAmt(p));
             });
            return qty.asString();
        });

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");


        priceCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();

            ObservableValue<Integer> qtyInMap = Bindings.valueAt(cartMap, p);

            SimpleIntegerProperty qty = new SimpleIntegerProperty(this.model.getItemAmt(p));



            if (qtyInMap.getValue() == null) {
                qty.set(0); 
            }  else {
                qty.set(qtyInMap.getValue());
            }

            float unitPrice = p.getPrice().get();

            float total = unitPrice * qty.get();

            this.controller.itemCartProperty().addListener((MapChangeListener<Product, Integer>) change -> {
                 qty.set(this.model.getItemAmt(p));
                 
             });

            return new SimpleStringProperty("$"+total);
        });

        cartTable.getColumns().addAll(nameCol, priceCol, qtyCol);

        VBox cartBox = new VBox(5, cartTable);
        
        Scene scene = new Scene(cartBox, 250, 400);
        stage.setScene(scene);
        stage.show();
    }

    private VBox displayLanguageScreen() {
        Label title = new Label("Language");
        title.setAlignment(Pos.CENTER);

        RadioButton english = new RadioButton("English(US)");
        RadioButton indonesian = new RadioButton("English(SG)");

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

        // later you can add: balance, edit account, etc.
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


    private void displayCustomerScreen(User user) {
        view.getChildren().clear();

        // remember who is logged in
        this.currentUser = user;

        VBox menuBox = displayCustomerSidePanel();

        // default right panel is Home
        contentBox = displayHomeScreen(user);

        HBox customerScreen = new HBox(5, menuBox, contentBox);

        view.getChildren().add(customerScreen);
    }

    private VBox displayAdminPanel() {
        Button homeButton = new Button("Home");
        homeButton.setAlignment(Pos.CENTER);

        Button stockButton = new Button("Cart");
        stockButton.setAlignment(Pos.CENTER);

        Button disableButton = new Button("Language");
        disableButton.setAlignment(Pos.CENTER);

        Button accountButton = new Button("Account");
        accountButton.setAlignment(Pos.CENTER);

        VBox menuBox = new VBox(30, homeButton, stockButton, disableButton, accountButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPrefSize(120, 300);
        return menuBox;
    }

    private void displayAdminScreen(User user) {
        view.getChildren().clear();
        VBox menuBox = displayAdminPanel();

        VBox contentBox = new VBox(); 
        

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
                User t = new User(userNameField.getText().trim(),passwordField.getText().trim(),UserType.CUSTOMER,1000);

            User valid = this.controller.login(t);

            if (valid != null) {
             stage.close();

             // If admin, open admin screen. Else, open customer screen.
                if (valid.getUserType() == UserType.ADMIN) {
                displayAdminScreen(valid);
             } else {
                displayCustomerScreen(valid);
                }

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
