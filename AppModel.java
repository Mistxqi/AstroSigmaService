import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

    public boolean login(User user) {
        for (User m : loginList){
            if (user.getUserName().equalsIgnoreCase(m.getUserName()) && user.getPassword().equals(m.getUserName())||user.getUserType().equals(m.getUserType())) {
                return true;
            }
        } 
        return false;
    }

    public void removeUser(User user) {
        this.loginList.remove(user);
    }
}

class User {
    private final SimpleStringProperty userName;
    private final SimpleStringProperty password;
    private UserType userType;

    public User(String userName,String password, UserType userType) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.userType = userType;
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
}

enum UserType {
    CUSTOMER,ADMIN;

    public String toString() {
        if (this.equals(ADMIN)) {
            return "Administrator";
        } else {
            return "Customer";
        }
    }
}