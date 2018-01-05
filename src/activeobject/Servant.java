package activeobject;

import static java.lang.Thread.sleep;

public class Servant {
    public final int M;
    private int value = 0;
    private boolean[] elems;

    public Servant(int m) {
        M = m;
        elems = new boolean[2*m];
    }

    public void produce(int portion){
        for(int i = value, j = value + portion; i < j; ++i)
            elems[i] = true;
        value += portion;
        //System.out.println("Portion produced.");
    }

    public void consume(int portion){
        for(int i = value, j = value - portion; i > j; --i)
            elems[i] = false;
        value -= portion;
        //System.out.println("Portion consumed.");
    }

    public int getValue(){
        return value;
    }
}
