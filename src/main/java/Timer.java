import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

class Timer{

    private static final int second = 1000;
    private ArrayList<Node> nodes;
    private float internalTime;

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
        internalTime+=1000;

        if(internalTime == 10000){
            System.out.println("\nNode 0 broke!");
            nodes.get(0).setFailureFree(false);
        }
        if(internalTime == 15000){
            System.out.println("\nNode 1 broke!");
            nodes.get(1).setFailureFree(false);
        }

        if(internalTime == 25000){
            System.out.println("\nNode 1 comeback!");
            nodes.get(1).nodeEntry();
        }
        if(internalTime == 35000){
            System.out.println("\nNode 0 comeback!");
            nodes.get(0).nodeEntry();
        }
    }
}
