package asynchronized;

import java.util.LinkedList;
import java.util.Random;

public class Producer implements Runnable {
    private AMonitor AMonitor;
    private Buffer buffer;
    private final int portion;
    private final int number;

    public Producer(AMonitor AMonitor, Buffer buffer, int number) {
        this.AMonitor = AMonitor;
        this.buffer = buffer;
        Random generator = new Random();
        portion = generator.nextInt(buffer.getSize() / 2) + 1;
        this.number = number;
    }

    public Producer(AMonitor AMonitor, Buffer buffer, int number, int portion) {

        this.AMonitor = AMonitor;
        this.buffer = buffer;
        this.portion = portion;
        this.number = number;
    }

    @Override
    public void run() {
        Random generator = new Random();
        while(true){
            LinkedList<Integer> res =  AMonitor.startProduce(portion);
            for(int i : res){
                buffer.setElem(i, generator.nextInt(10000));
            }
            AMonitor.endProduce(res, this);
        }
    }

    public void prompt(){
        System.out.println("I am producer #" + number + " and I just produced " + portion + " unit(s).");
    }
}
