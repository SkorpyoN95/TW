package asynchronized;

import java.util.LinkedList;
import java.util.Random;

public class Producer implements Runnable {
    private Monitor monitor;
    private Buffer buffer;
    private final int portion;
    private final int number;

    public Producer(Monitor monitor, Buffer buffer, int number) {
        this.monitor = monitor;
        this.buffer = buffer;
        Random generator = new Random();
        portion = generator.nextInt(buffer.getSize() / 2) + 1;
        this.number = number;
    }

    public Producer(Monitor monitor, Buffer buffer, int number, int portion) {

        this.monitor = monitor;
        this.buffer = buffer;
        this.portion = portion;
        this.number = number;
    }

    @Override
    public void run() {
        Random generator = new Random();
        while(true){
            LinkedList<Integer> res =  monitor.startProduce(portion);
            for(int i : res){
                buffer.setElem(i, generator.nextInt(10000));
            }
            monitor.endProduce(res, this);
        }
    }

    public void prompt(){
        System.out.println("I am producer #" + number + " and I just produced " + portion + " unit(s).");
    }
}
