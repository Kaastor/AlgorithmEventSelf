import lombok.Getter;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;

@Getter
class DiagnosticStructure extends SingleGraph{

    static final Integer NODE_NUMBER = 5;

    private ArrayList<Node> nodes;

    DiagnosticStructure(){
        super("Diagnostic Structure");
        this.nodes = new ArrayList<>();
        initialization();
    }

    private void initialization(){
        addNodes();
        addEdges();
        for(Integer i = 0; i < NODE_NUMBER; i++){
            Node node = new Node(i, this);
            if(i==0) node.setFailureFree(false);
            nodes.add(node);
        }
        for(Integer i = 0; i < NODE_NUMBER; i++){
            nodes.get(i).work();
        }
    }

    private void addNodes(){
        for(Integer i = 0; i < NODE_NUMBER; i++){
            addNode(i.toString());
        }
    }

    private void addEdges(){
        addEdge("1", 0,3, true);
        addEdge("2",0,1);
        addEdge("3",1,3);
        addEdge("4",1,2, true);
        addEdge("5",2,4, true);
        addEdge("6",3,2, true);
        addEdge("7",3,4);
        addEdge("8",4,0, true);
    }
}
