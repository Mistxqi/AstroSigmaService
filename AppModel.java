import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;

public class AppModel {
    private final ObservableList<User> loginList;


    AppModel() {
        this.loginList = AppUserList.getLoginList();
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