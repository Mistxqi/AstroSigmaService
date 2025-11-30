import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;

public class AppModel {
    private final ObservableList<User> loginList;
    private final ObservableList<Product> productList;

    AppModel() {
        this.loginList = AppUserList.getLoginList();
        this.productList = AppProductList.getProductList();
    }

    public ObservableList<User> loginProperty(){
        return null;
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
}

class User {
    private final SimpleStringProperty userName;
    private final SimpleStringProperty password;
    private UserType userType;
    private SimpleFloatProperty balance;

    public User(String userName,String password, UserType userType, float balance) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.userType = userType;
        this.balance = new SimpleFloatProperty(balance);
    }

    public String getUserName() {
        return userName.get();
    }

    public String getPassword() {
        return password.get();
    }

    public void setUserName(String newUserName) {
        this.userName.set(newUserName);
    }

    public void setPassword(String newPassword) {
        this.password.set(newPassword);
    }

    public UserType getUserType() {
        return this.userType;
    }

    public float getBalance() {
        return balance.get();
    }

    public boolean chargeBalance(float amount) {

        if (this.balance.get() >= amount) {
            balance.set(this.balance.get()-amount);
            return true;
        } else {
            return false;
        }
    }
}

class Product {
    private SimpleStringProperty name;
    private SimpleFloatProperty price;
    private SimpleIntegerProperty stock;
    private ItemCategory category;

    public Product(String name, int stock, float price, ItemCategory category) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleFloatProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.category = category;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public SimpleFloatProperty getPrice() {
        return price;
    }

    public void setPrice(SimpleFloatProperty price) {
        this.price = price;
    }

    public SimpleIntegerProperty getStock() {
        return stock;
    }

    public void setStock(SimpleIntegerProperty stock) {
        this.stock = stock;
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
    private SimpleIntegerProperty bad;

    public Perishable(String name, int stock, float price, ItemCategory itemCategory, int[] expDate, int bad) {
        super(name, stock, price, category);
        this.expDate = expDate;
        this.bad = new SimpleIntegerProperty(bananasExp);
    }

    public int[] getExpDate() {
        return expDate;
    }

    public void setExpDate(int[] expDate) {
        this.expDate = expDate;
    }

    public SimpleIntegerProperty getBad() {
        return bad;
    }

    public void setBad(SimpleIntegerProperty bad) {
        this.bad = bad;
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