package activeobject;

import java.util.Random;

public class Consumer implements Runnable {
    private final Proxy proxy;
    private final int portion;

    public Consumer(Proxy proxy, int portion) {
        this.proxy = proxy;
        this.portion = portion;
    }

    public Consumer(Proxy proxy) {
        this.proxy = proxy;
        Random generator = new Random();
        portion = generator.nextInt(proxy.getServantSize());
    }

    @Override
    public void run() {
        while(true){
            proxy.consume(portion);
        }
    }
}
