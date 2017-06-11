import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class EventSelfApp extends Application{

    public static void main(String args[]) {
        launch(args);
    }

    @SneakyThrows
    public void start(Stage theStage)
    {
        DiagnosticStructure diagnosticStructure = new DiagnosticStructure();

        Task task = new Task<Void>() {
            @SneakyThrows
            @Override public Void call() {
                new Timer(diagnosticStructure);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

//        diagnosticStructure.display();

    }
}
