import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MVCExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ASTRO");
        AppModel model = new AppModel();
        AppController controller = new AppController(model);
        AppView view = new AppView(controller, model, primaryStage);

        Scene scene = new Scene(view.asParent(), 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}