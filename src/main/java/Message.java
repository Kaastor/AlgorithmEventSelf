import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
class Message {

    private ArrayList<Information> message;

    public Message(Information sender, Information tester, Information tested){
        this.message = new ArrayList<>(3);
        this.message.add(sender);
        this.message.add(tester);
        this.message.add(tested);
    }
}
