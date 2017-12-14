package activeobject;

import static java.lang.Thread.sleep;

public class Servant {
    public final int M;
    private int value;

    public Servant(int m) {
        M = m;
    }

    public void produce(int portion){
        value += portion;
        //System.out.println("Portion produced.");
    }

    public void consume(int portion){
        value -= portion;
        //System.out.println("Portion consumed.");
    }

    public int getValue(){
        return value;
    }
}
