import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString(exclude = "message")
@Getter @Setter
class Message {

    private ArrayList<Information> message;

    private Information sender;
    private Information tester;
    private Information tested;

    Message(Information sender, Information tester, Information tested){
        this.message = new ArrayList<>(3);
        this.sender = sender;
        this.message.add(sender);
        this.tester = tester;
        this.message.add(tester);
        this.tested = tested;
        this.message.add(tested);
    }

    Information getInformation(int index){
        return message.get(index);
    }
}
