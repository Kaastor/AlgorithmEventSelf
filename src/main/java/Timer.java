import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

class Timer{

    private static final int second = 1000;
    private ArrayList<Node> nodes;

    Timer(DiagnosticStructure diagnosticStructure) {
        this.nodes = diagnosticStructure.getNodes();
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(second), (event) -> handle()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void handle() {
        for(Node node : nodes){
            node.incrementTime();
        }
    }
}
