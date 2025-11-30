import java.time.LocalDate;
import java.time.YearMonth;

public class AppController {
    private final AppModel model;

    public AppController(AppModel model) {
        this.model = model;
    }

    public boolean register(User user) {
        return this.model.register(user);
    }

    public User login(User user) {
        return this.model.login(user);
    }

    public boolean chargeBalance(User user, float amount) {
        return this.model.chargeUser(user, amount);
    }

    public void setExpDate(int[] expDate) {
        this.model.setExpDate(expDate);
        //me do this later :3 
    }

    public boolean isExp(Perishable product) {
        int[] expDate = product.getExpDate();
        LocalDate expiryDate = LocalDate.of(expDate[2], expDate[1], expDate[0]);
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(expiryDate);
        
    }
}
