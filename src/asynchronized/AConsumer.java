package asynchronized;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class AConsumer implements Runnable {
    private AMonitor AMonitor;
    private Buffer buffer;
    private final int portion;
    private final int number;
    private int[] counter;

    public AConsumer(AMonitor AMonitor, Buffer buffer, int[] counter, int number) {
        this.AMonitor = AMonitor;
        this.buffer = buffer;
        Random generator = new Random();
        portion = generator.nextInt(buffer.getSize() / 2) + 1;
        this.number = number;
        this.counter = counter;
    }

    public AConsumer(AMonitor AMonitor, Buffer buffer, int[] counter, int number, int portion) {

        this.AMonitor = AMonitor;
        this.buffer = buffer;
        this.portion = portion;
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
        LinkedList<Integer> res =  AMonitor.startConsume(portion);
        for(int i : res){
            buffer.getElem(i);
        }
        AMonitor.endConsume(res, this);
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
        System.out.println("I am consumer #" + number + " and I just consumed " + portion + " unit(s).");
    }
}
