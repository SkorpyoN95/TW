package activeobject;

import java.util.Random;

public class Producer implements Runnable {
    private final Proxy proxy;
    private final int portion;

    public Producer(Proxy proxy, int portion) {
        this.proxy = proxy;
        this.portion = portion;
    }

    public Producer(Proxy proxy) {
        this.proxy = proxy;
        Random generator = new Random();
        portion = generator.nextInt(proxy.getServantSize());
    }

    @Override
    public void run() {
        while(true){
            proxy.produce(portion);
        }
    }
}
