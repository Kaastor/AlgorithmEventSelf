
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.graphstream.graph.Edge;

import java.util.ArrayList;

@Getter @Setter
class Node extends Thread{

    private int nodeId;
    private ArrayList<Node> nodes;
    private DiagnosticStructure diagnosticStructure;
    private boolean failureFree = true;

    private static double testingPeriod = 5000; //ms
    private static long testingTime = 500;  //ms
    private static long testResponseTime = 200; //ms
    private float internalTime;

    private ArrayList<Message> accusers;
    private ArrayList<Message> entry;
    private ArrayList<ArrayList<Message>> buffer;
    private ArrayList<Message> checkedBuffer;

    private ArrayList<Integer> testerOf; //wychodzace
    private ArrayList<Integer> testedBy; //wchodzace

    Node(int id, DiagnosticStructure diagnosticStructure){
        this.nodeId = id;
        this.internalTime = 0;
        this.nodes = diagnosticStructure.getNodes();
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
        for (Edge edge : diagnosticStructure.getNode(nodeId).getEachEnteringEdge()){
            if(edge.getSourceNode().getIndex() != nodeId)
                testedBy.add(edge.getSourceNode().getIndex());
            else
                testedBy.add(edge.getTargetNode().getIndex());
        }
        for (Edge edge : diagnosticStructure.getNode(nodeId).getEachLeavingEdge()){
            if(edge.getSourceNode().getIndex() != nodeId)
                testerOf.add(edge.getSourceNode().getIndex());
            else
                testerOf.add(edge.getTargetNode().getIndex());
        }
    }

    void damaged(long responseTime){
        testResponseTime = responseTime;
    }

    void incrementTime(){
        internalTime+=1000;
        if(internalTime % testingPeriod == 0)
            work();
    }


    void work(){
        for(Integer node : testerOf)
            performTest(node);
    }


    boolean performTest(Integer node){
        boolean failureFree = true;
        if(checkNodeCondition(node)){
            System.out.println(nodeId + " Ztestowalem: " + node + " i on działa.");
        }

        return failureFree;
    }

    @SneakyThrows
    boolean checkNodeCondition(Integer node){
        boolean response = nodes.get(node).isFailureFree();
        if(response) Thread.sleep(testResponseTime);
        else Thread.sleep(testingTime);

        return response;
    }


}
