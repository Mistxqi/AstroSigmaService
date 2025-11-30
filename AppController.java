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
        return null;
        //me do this later :3 
    }
}
