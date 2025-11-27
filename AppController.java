public class AppController {
    private final AppModel model;

    public AppController(AppModel model) {
        this.model = model;
    }

    public boolean register(User user) {
        return this.model.register(user);
    }
}
