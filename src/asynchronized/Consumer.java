package asynchronized;

import java.util.LinkedList;
import java.util.Random;

public class Consumer implements Runnable {
    private AMonitor AMonitor;
    private Buffer buffer;
    private final int portion;
    private final int number;

    public Consumer(AMonitor AMonitor, Buffer buffer, int number) {
        this.AMonitor = AMonitor;
        this.buffer = buffer;
        Random generator = new Random();
        portion = generator.nextInt(buffer.getSize() / 2) + 1;
        this.number = number;
    }

    public Consumer(AMonitor AMonitor, Buffer buffer, int number, int portion) {

        this.AMonitor = AMonitor;
        this.buffer = buffer;
        this.portion = portion;
        this.number = number;
    }

    @Override
    public void run() {
        while(true){
            LinkedList<Integer> res =  AMonitor.startConsume(portion);
            for(int i : res){
                buffer.getElem(i);
            }
            AMonitor.endConsume(res, this);
        }
    }

    public void prompt(){
        System.out.println("I am consumer #" + number + " and I just consumed " + portion + " unit(s).");
    }
}
