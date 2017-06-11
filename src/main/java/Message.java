import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
class Message {

    private ArrayList message;
    
    public Message(){
        message = new ArrayList(3);
    }

}
