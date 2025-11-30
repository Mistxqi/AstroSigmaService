import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.MapChangeListener;

public class AppUserList {

    private static ObservableList<User> loginList = FXCollections.observableArrayList();

    static {
        loginList.add(new User("Admin", "12345678", UserType.ADMIN, 10000));
        loginList.add(new User("User", "12345678", UserType.CUSTOMER, 10000));
    }

    public static ObservableList<User> getLoginList() {
        return loginList;
    }
}

// public class AppProductList {

//     private static ObservableList<Product> productList = 
// }