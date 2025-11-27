import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;

public class AppModel {
    private final ObservableList<User> loginMap;

    AppModel() {
        this.loginMap = FXCollections.observableArrayList();
    }

    public ObservableList<String, String> loginProperty(){

    }

    public boolean register(User user) {

        for (User m : loginMap){
            if (user.getUserName().equalsIgnoreCase(m.getUserName())) {
                System.out.println("Duplicate username: " + user.getUserName());
                return false;
            }
        }
        this.loginMap.add(user);
        return true;
    }

    public void removeUser(User user) {
        this.loginMap.remove(user);
    }
}

class User {
    private final SimpleStringProperty userName;
    private final SimpleStringProperty password;

    public User(String userName,String password) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
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