package randomportion;

import java.util.Random;

import static java.lang.Thread.sleep;

public class RProducer implements Runnable {
    private RMonitor monitor;
    private final int portion;
    private final int number;
    private int counter[];

    public RProducer(RMonitor monitor, int[] counter, int number, int portion) {
        this.monitor = monitor;
        this.portion = portion;
        this.number = number;
        this.counter = counter;
    }

    public RProducer(RMonitor monitor, int[] counter, int number) {
        this.monitor = monitor;
        Random generator = new Random();
        portion = generator.nextInt(monitor.M-1) + 1;
        this.number = number;
        this.counter = counter;
    }

    @Override
    public void run() {
        while(true){
            mainJob();
            doSomeJob();
        }
    }

    private void mainJob(){
        monitor.produce(portion, this);
        synchronized (this){
            counter[0]++;
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

    public void prompt(){
        System.out.println("I am producer #" + number + " and I just produced " + portion + ".");
    }
}
