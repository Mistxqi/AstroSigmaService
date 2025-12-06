import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Comparator;
import java.util.HashMap;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn;
import javafx.collections.MapChangeListener;

public class AppModel {
    private final ObservableList<User> loginList;
    private final ObservableList<Product> productList;

    AppModel() {
        this.loginList = MVCExample.getLoginList();
        this.productList = MVCExample.getProductList();
    }

    public boolean register(User user) {

        for (User m : loginList){
            if (user.getUserName().equalsIgnoreCase(m.getUserName())) {
                System.out.println("Duplicate username: " + user.getUserName());
                return false;
            }
        }
        this.loginList.add(user);
        return true;
    }

    public User login(User user) {
        for (User m : loginList){
            if (user.getUserName().equalsIgnoreCase(m.getUserName()) && user.getPassword().equals(m.getPassword())) {
                return m;
            }
        } 
        return null;
    }

    public User getUser(User user) {
        for (User m : loginList){
            if (user.getUserName().equalsIgnoreCase(m.getUserName()) && user.getPassword().equals(m.getPassword())) {
                return m;
            }
        } 
        return null;
    }

    public void removeUser(User user) {
        this.loginList.remove(user);
    }

    public boolean chargeUser(User user, float amount) {
        return user.chargeBalance(amount);
    }

    public ObservableList<Product> searchProduct(String product) {
        ObservableList<Product> results = FXCollections.observableArrayList();

        for (Product m : productList){
            String mname = m.getName().get().toLowerCase();
            String mcat = m.getCategory().toString().toLowerCase();
            if (mname.contains(product.toLowerCase())||mcat.contains(product.toLowerCase())){
                results.add(m);
            }
        }

        return results;
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    //cart
    public ObservableMap<Product, Integer> itemCart = FXCollections.observableHashMap();
    
    
    public void addCartItem(Product product) {

        if (itemCart.containsKey(product)){
            int a = itemCart.get(product);
            itemCart.put(product, a+1);
        } else {
            itemCart.put(product, 1);
        }
    }

    public void removeCartItem(Product product) {
        if (itemCart.containsKey(product)){
            itemCart.remove(product);
        }
    }

    public void deduceCartItem(Product product) {
        if(itemCart.containsKey(product)){
            if (itemCart.get(product)>1){
                int a = itemCart.get(product);
                itemCart.put(product, a-1);
            } else {
                itemCart.remove(product);
            }
        }
    }

    public Integer getItemAmt(Product product) {
        if (itemCart.containsKey(product)){
            return itemCart.get(product);
        } 
            return 0;
    }

    public ObservableMap<Product, Integer> itemCartProperty() {
        return itemCart;
    }

    public ObservableList<Product> itemInCart() {
        ObservableList<Product> itemsinCart = FXCollections.observableArrayList(); 

        for (Product m : itemCart.keySet()) {
            itemsinCart.add(m);
        }

        return itemsinCart;
    }
    
    
    }


class User {
    private String userName;
    private String password;
    private UserType userType;
    private float balance;

    public User(String userName,String password, UserType userType, float balance) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public float getBalance() {
        return balance;
    }

    public boolean chargeBalance(float amount) {

        if (this.balance >= amount) {
            balance= this.balance-amount;
            return true;
        } else {
            return false;
        }
    }
}

class Product implements Comparable<Product>{
    private  String name;
    private  float price;
    private  int stock;
    private  ItemCategory category;

    public Product(String name, int stock, float price, ItemCategory category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    @Override
    public int compareTo(Product other) {
        return this.name.compareTo(other.name);
    }


    public SimpleStringProperty getName() {
        return new SimpleStringProperty(this.name);
    }

    public void setName(SimpleStringProperty name) {
        this.name = name.toString();
    }

    public SimpleFloatProperty getPrice() {
        return new SimpleFloatProperty(this.price);
    }

    public SimpleStringProperty getPriceLabel() {
        return new SimpleStringProperty("$" +String.valueOf(this.price));
    }

    public void setPrice(SimpleFloatProperty price) {
        this.price = price.get();
    }

    public SimpleIntegerProperty getStock() {
        return new SimpleIntegerProperty(this.stock);
    }

    public void setStock(SimpleIntegerProperty stock) {
        this.stock = stock.get();
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }
    
}

class Perishable extends Product {
    private int[] expDate;
    private int bad;

    public Perishable(String name, int stock, float price, ItemCategory itemCategory, int[] expDate, int bad) {
        super(name, stock, price, itemCategory);
        this.expDate = expDate;
        this.bad = bad;
    }

    public int[] getExpDate() {
        return expDate;
    }

    public void setExpDate(int[] expDate) {
        this.expDate = expDate;
    }

    public SimpleIntegerProperty getBad() {
        return new SimpleIntegerProperty(this.bad);
    }

    public void setBad(SimpleIntegerProperty bad) {
        this.bad = bad.get();
    }

    
}




enum ItemCategory{
    RECOMMENDATIONS,
    FRUITS_N_VEGETABLES,
    ASTROBASIC,
    ASTROGOODS,
    MEATS,
    DAIRY,
    SEAFOOD,
    SNACKS,
    SEASONINGS;
}


enum UserType {
    CUSTOMER,ADMIN,DISABLED;

    public String toString() {
        if (this.equals(ADMIN)) {
            return "Administrator";
        } else if (this.equals(CUSTOMER)) {
            return "Customer";
        } else {
            return "Disabled";
        }
    }
}