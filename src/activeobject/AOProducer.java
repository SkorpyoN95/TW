package activeobject;

import java.util.Random;

import static java.lang.Thread.sleep;

public class AOProducer implements Runnable {
    private final Proxy proxy;
    private final int portion;
    private int[] counter;
    private int weight;
    static private long start_time = 0;

    public AOProducer(Proxy proxy, int[] counter, int portion, int weight) {
        this.proxy = proxy;
        this.portion = portion;
        this.counter = counter;
        this.weight = weight;
    }

    public AOProducer(Proxy proxy, int[] counter, int weight) {
        this.proxy = proxy;
        Random generator = new Random();
        portion = generator.nextInt(proxy.getServantSize());
        this.counter = counter;
        this.weight = weight;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            proxy.produce(portion, counter);
            doSomeJob();
        }
    }

    private synchronized void doSomeJob(){
        try {
            sleep(weight);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        if(System.currentTimeMillis() - start_time < 10000) counter[0]++;
    }

    static public void startCounting(){
        start_time = System.currentTimeMillis();
    }

    static public long getTimer(){
        return start_time;
    }
}
