package randomportion;

import java.util.Random;

import static java.lang.Thread.sleep;

public class RConsumer implements Runnable {
    private RMonitor monitor;
    private final int portion;
    private final int number;
    private int[] counter;
    private int weight;
    static private long start_time = 0;

    public RConsumer(RMonitor monitor, int[] counter, int number, int weight) {
        this.monitor = monitor;
        Random generator = new Random();
        portion = generator.nextInt(monitor.M-1) + 1;
        this.number = number;
        this.counter = counter;
        this.weight = weight;
    }

    public RConsumer(RMonitor monitor, int[] counter, int number, int portion, int weight) {
        this.monitor = monitor;
        this.portion = portion;
        this.number = number;
        this.counter = counter;
        this.weight = weight;
    }

    @Override
    public void run() {
        while(System.currentTimeMillis() - start_time < 10000){
            mainJob();
            doSomeJob();
        }
    }

    private void mainJob(){
        monitor.consume(portion, this);
        synchronized (this){
            if(System.currentTimeMillis() - start_time < 10000) counter[0]++;
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

    public void prompt(){
        System.out.println("I am consumer #" + number + " and I just consumed " + portion + ".");
    }

    static public void startCounting(){
        start_time = System.currentTimeMillis();
    }

    static public long getTimer(){
        return start_time;
    }
}
