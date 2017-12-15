package activeobject;

import java.util.Random;

import static java.lang.Thread.sleep;

public class AOProducer implements Runnable {
    private final Proxy proxy;
    private final int portion;
    private int[] counter;

    public AOProducer(Proxy proxy, int[] counter, int portion) {
        this.proxy = proxy;
        this.portion = portion;
        this.counter = counter;
    }

    public AOProducer(Proxy proxy, int[] counter) {
        this.proxy = proxy;
        Random generator = new Random();
        portion = generator.nextInt(proxy.getServantSize());
        this.counter = counter;
    }

    @Override
    public void run() {
        while(true){
            proxy.produce(portion, counter);
            doSomeJob();
        }
    }

    private synchronized void doSomeJob(){
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        counter[0]++;
    }
}
