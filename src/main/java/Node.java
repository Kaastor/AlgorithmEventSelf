
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.graphstream.graph.Edge;

import java.util.ArrayList;
import java.util.Collections;

@Getter @Setter
class Node extends Thread{

    int broadcastCounter = 0;

    private int nodeId;
    private ArrayList<Node> nodes;
    private DiagnosticStructure diagnosticStructure;
    private boolean failureFree = true;

    private static double testingPeriod = 5000; //ms
    private static long failureResponseTime = 500;  //ms
    private static long failureFreeResponseTime = 200; //ms
    private float internalTime;

    private ArrayList<Message> accusers;
    private ArrayList<Message> entry;
    private ArrayList<Message> buffer;
    private ArrayList<Message> checkedBuffer;

    private ArrayList<Integer> testerOf; //wychodzace
    private ArrayList<Integer> testedBy; //wchodzace

    Node(int id, DiagnosticStructure diagnosticStructure){
        this.nodeId = id;
        this.internalTime = 0;
        this.nodes = diagnosticStructure.getNodes();
        this.diagnosticStructure = diagnosticStructure;
        this.accusers = new ArrayList<>(Collections.nCopies(DiagnosticStructure.NODE_NUMBER, (Message) null));
        this.entry = new ArrayList<>(Collections.nCopies(DiagnosticStructure.NODE_NUMBER, (Message) null));
        this.buffer = new ArrayList<>();
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
        failureFreeResponseTime = responseTime;
    }

    void incrementTime(){
        internalTime+=1000;
        if(internalTime % testingPeriod == 0){
            work();
        }
    }

    void work(){
        for(Integer node : testerOf){
            performTest(node);
        }

        System.out.print("Id: " + nodeId + "| BroadcastCounter: " + broadcastCounter + "| Entry: ");
        for(Message message : entry){
            if(message != null)
                System.out.print(message.getTested().getNodeNumber() + ", ");
            else System.out.print(", null ");
        }
        System.out.println();
    }

    private boolean performTest(Integer node){
        boolean failureFree = true;
        if(isFailureFree(node)){
            checkMarkDiagnosticMessages(node);
            Message message = new Message(
                    new Information(this.nodeId, internalTime),
                    new Information(this.nodeId, internalTime),
                    new Information(node, 0));
            updateAndBroadcast(message);
        }
        else{
            failureFree = false;
            discardDiagnosticMessages(node);
            Message message = new Message(
                    null,
                    new Information(nodeId, internalTime),
                    new Information(node, 0));
            updateAndBroadcast(message);
        }

        return failureFree;
    }

    @SneakyThrows
    private boolean isFailureFree(Integer nodeId){
        long responseTime = nodes.get(nodeId).testNode();
        Thread.sleep(responseTime);

        return (responseTime < failureResponseTime);
    }

    private void checkMarkDiagnosticMessages(Integer node){
        for(Message message : new ArrayList<>(buffer)){
            if(message.getInformation(0) == null){
                    failureMessageProcess(message);
                    buffer.remove(message);
            }
            else if(message.getInformation(0).getNodeNumber() == node){
                entryMessageProcess(message);
                buffer.remove(message);
            }
        }
    }

    private void discardDiagnosticMessages(Integer node){
        for(Message message : new ArrayList<>(buffer)) {
            if (message.getInformation(0) == null) {
                if (message.getInformation(1).getNodeNumber() == node)
                    buffer.remove(message);
            } else if (message.getInformation(0).getNodeNumber() == node)
                buffer.remove(message);
        }
    }

    private void updateAndBroadcast(Message message){
        Integer nodeNumber = message.getInformation(2).getNodeNumber();
        if(message.getInformation(0) == null){
            Message previous = accusers.get(nodeNumber);
            accusers.set(nodeNumber, message);
            entry.set(nodeNumber, null);
            if(previous == null) broadcast(message);
        }
        else{
            Message previous = entry.get(nodeNumber);
            entry.set(nodeNumber, message);
            accusers.set(nodeNumber, null);
            if(previous == null) broadcast(message);
        }
    }

    private void broadcast(Message message){
        broadcastCounter++;
        for(Integer node : testedBy){
            nodes.get(node).getBuffer().add(message);
            nodes.get(node).performTest(this.getNodeId());
        }
    }

    private void failureMessageProcess(Message message){
        if(this.testerOf.contains(message.getTested().getNodeNumber())){
            if(performTest(message.getTested().getNodeNumber())){
                updateAndBroadcast(new Message(
                        new Information(this.nodeId, internalTime),
                        new Information(message.getTester().getNodeNumber(), message.getTester().getTime()),
                        new Information(message.getTested().getNodeNumber(), 0)));
            }
            else{
                updateAndBroadcast(message);
            }
        }
        else {
            updateAndBroadcast(message);
        }
    }

    private void entryMessageProcess(Message message){
        updateAndBroadcast(new Message(
                new Information(this.nodeId, internalTime),
                new Information(message.getTester().getNodeNumber(), message.getTester().getTime()),
                new Information(message.getTested().getNodeNumber(), 0)));
    }

    @SneakyThrows
    private long testNode(){
        if(failureFree)
            return  failureFreeResponseTime;
        else
            return failureResponseTime;
    }
}
