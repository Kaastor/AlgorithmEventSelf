import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.graphstream.ui.view.Viewer;

public class EventSelfApp extends Application{

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        launch(args);
    }

    @SneakyThrows
    public void start(Stage theStage)
    {
        DiagnosticStructure diagnosticStructure = new DiagnosticStructure();

        graphVisualisation(diagnosticStructure);

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
    }

    private void graphVisualisation(DiagnosticStructure diagnosticStructure){
        String styleSheet="node {"+
                " fill-color: grey;"+
                " size: 30px;"+
                " stroke-mode: plain;"+
                " stroke-color: black;"+
                " stroke-width: 1px;"+
                "}"+
                "node.important {"+
                " fill-color: red;"+
                " size: 30px;"+
                "}";
        diagnosticStructure.addAttribute("ui.stylesheet", styleSheet);
        diagnosticStructure.addAttribute("ui.quality");
        Viewer viewer = diagnosticStructure.display();
    }
}
