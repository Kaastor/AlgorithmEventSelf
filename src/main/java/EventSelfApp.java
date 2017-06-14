import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class EventSelfApp extends Application{

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        launch(args);
    }

    @SneakyThrows
    public void start(Stage theStage)
    {
        theStage.setTitle("EVENT_SELF");

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

        Button diagnosisButton = new Button();
        diagnosisButton.setText("Diagnozuj sieć");
        diagnosisButton.setOnAction( (event) -> {
            for(Node node : diagnosticStructure.getNodes()){
                node.diagnosis();
            }
        });
        Button failZeroNodeButton = new Button();
        failZeroNodeButton.setText("Uszkodź węzeł 0");
        failZeroNodeButton.setOnAction( (event) -> {
            if(diagnosticStructure.getNodes().get(0).isFailureFree()) {
                diagnosticStructure.getNodes().get(0).setFailureFree(false);
                System.out.println("\nWęzeł 0 został uszkodzony!");
            }
        });
        Button repairZeroNodeButton = new Button();
        repairZeroNodeButton.setText("Napraw węzeł 0");
        repairZeroNodeButton.setOnAction( (event) -> {
            if(!diagnosticStructure.getNodes().get(0).isFailureFree()) {
                diagnosticStructure.getNodes().get(0).nodeEntry();
                System.out.println("\nWęzeł 0 został naprawiony!");
            }
        } );

        Button failThirdNodeButton = new Button();
        failThirdNodeButton.setText("Uszkodź węzeł 3");
        failThirdNodeButton.setOnAction( (event) -> {
            if(diagnosticStructure.getNodes().get(3).isFailureFree()) {
                diagnosticStructure.getNodes().get(3).setFailureFree(false);
                System.out.println("\nWęzeł 3 został uszkodzony!\n");
            }
        });
        Button repairThirdNodeButton = new Button();
        repairThirdNodeButton.setText("Napraw węzeł 3");
        repairThirdNodeButton.setOnAction( (event) -> {
            if(!diagnosticStructure.getNodes().get(3).isFailureFree()) {
                diagnosticStructure.getNodes().get(3).nodeEntry();
                System.out.println("\nWęzeł 3 został naprawiony!\n");
            }
        });

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setMinSize(300, 300);
        root.setVgap(5);
        root.setHgap(5);
        root.add(diagnosisButton, 1, 1);
        root.add(failZeroNodeButton, 1, 2);
        root.add(repairZeroNodeButton, 1, 3);
        root.add(failThirdNodeButton, 1, 4);
        root.add(repairThirdNodeButton, 1, 5);
        theStage.setScene(new Scene(root));
        theStage.show();
    }

    private void graphVisualisation(DiagnosticStructure diagnosticStructure){
        String styleSheet="node {"+
                " fill-color: grey;"+
                " size: 50px;"+
                " stroke-mode: plain;"+
                " stroke-color: black;"+
                " stroke-width: 2px;"+
                "}"+
                "node.important {"+
                " fill-color: red;"+
                " size: 30px;"+
                "}";
        diagnosticStructure.addAttribute("ui.stylesheet", styleSheet);
        diagnosticStructure.addAttribute("ui.quality");
        diagnosticStructure.display();
    }
}
