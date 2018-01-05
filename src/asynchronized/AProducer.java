package asynchronized;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class AProducer implements Runnable {
    private AMonitor AMonitor;
    private Buffer buffer;
    private final int portion;
    private final int number;
    private int[] counter;
    private int weight;
    static private long start_time = 0;
    private Random generator = new Random();

    public AProducer(AMonitor AMonitor, Buffer buffer, int[] counter, int number, int weight) {
        this.AMonitor = AMonitor;
        this.buffer = buffer;
        Random generator = new Random();
        portion = generator.nextInt(buffer.getSize() / 2) + 1;
        this.number = number;
        this.counter = counter;
        this.weight = weight;
    }

    public AProducer(AMonitor AMonitor, Buffer buffer, int[] counter, int number, int portion, int weight) {

        this.AMonitor = AMonitor;
        this.buffer = buffer;
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
        LinkedList<Integer> res =  AMonitor.startProduce(portion);
        for(int i : res){
            buffer.setElem(i);
        }
        AMonitor.endProduce(res, this);
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
        System.out.println("I am producer #" + number + " and I just produced " + portion + " unit(s).");
    }

    static public void startCounting(){
        start_time = System.currentTimeMillis();
    }

    static public long getTimer(){
        return start_time;
    }
}
