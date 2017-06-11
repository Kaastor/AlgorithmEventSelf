import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Information{
    int nodeNumber;
    float time;

    Information(int nodeNumber, float time){
        this.nodeNumber = nodeNumber;
        this.time = time;
    }
}