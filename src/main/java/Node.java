
import lombok.Getter;
import lombok.Setter;
import org.graphstream.graph.Edge;

import java.util.ArrayList;

@Getter @Setter
class Node {

    private int id;
    private DiagnosticStructure diagnosticStructure;

    private static float TESTING_PERIOD = 5000; //ms
    private static float testingTime = 1000;   //ms
    private float internalTime;

    private ArrayList<Message> accusers;
    private ArrayList<Message> entry;
    private ArrayList<ArrayList<Message>> buffer;
    private ArrayList<Message> checkedBuffer;

    private ArrayList<Integer> testerOf; //wychodzace
    private ArrayList<Integer> testedBy; //wchodzace

    Node(int id, DiagnosticStructure diagnosticStructure){
        this.id = id;
        this.internalTime = 0;
        this.diagnosticStructure = diagnosticStructure;
        this.accusers = new ArrayList<>(DiagnosticStructure.NODE_NUMBER);
        this.entry = new ArrayList<>(DiagnosticStructure.NODE_NUMBER);
        this. buffer = new ArrayList<>(DiagnosticStructure.NODE_NUMBER);
        for(int i = 0 ; i < buffer.size() ; i++)
            buffer.add(new ArrayList<>(DiagnosticStructure.NODE_NUMBER));
        this.checkedBuffer = new ArrayList<>();

        this.testerOf = new ArrayList<>();
        this.testedBy = new ArrayList<>();
        initializeTestingStructure();
    }

    private void initializeTestingStructure(){
        for (Edge edge : diagnosticStructure.getNode(id).getEachEnteringEdge()){
            if(edge.getSourceNode().getIndex() != id)
                testedBy.add(edge.getSourceNode().getIndex());
            else
                testedBy.add(edge.getTargetNode().getIndex());
        }
        for (Edge edge : diagnosticStructure.getNode(id).getEachLeavingEdge()){
            if(edge.getSourceNode().getIndex() != id)
                testerOf.add(edge.getSourceNode().getIndex());
            else
                testerOf.add(edge.getTargetNode().getIndex());
        }
    }

    void incrementTime(){
        internalTime++;
        if(internalTime % 5 == 0)
            System.out.println(id + " Testuje.");
    }


}
