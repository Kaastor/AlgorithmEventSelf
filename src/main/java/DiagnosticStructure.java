import org.graphstream.graph.implementations.SingleGraph;

class DiagnosticStructure extends SingleGraph{

    private Integer nodeNumber;

    DiagnosticStructure(int nodeNumber){
        super("Diagnostic Structure");
        this.nodeNumber = nodeNumber;
        initialization();
    }

    private void initialization(){
        addNodes();
        addEdges();
    }

    private void addNodes(){
        for(Integer i = 0 ; i < nodeNumber ; i++){
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
