
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
class Node {

    private ArrayList<Message> accusers;
    private ArrayList<Message> entry;
    private ArrayList<Message> buffer;
    private ArrayList<Message> checkedBuffer;
    public Node(){

    }
}
