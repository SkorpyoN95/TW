package randomportion;

import java.util.Random;

public class Producer implements Runnable {
    private Monitor monitor;
    private final int portion;
    private final int number;

    public Producer(Monitor monitor, int number, int portion) {
        this.monitor = monitor;
        this.portion = portion;
        this.number = number;
    }

    public Producer(Monitor monitor, int number) {
        this.monitor = monitor;
        Random generator = new Random();
        portion = generator.nextInt(monitor.M-1) + 1;
        this.number = number;
    }

    @Override
    public void run() {
        while(true){
            monitor.produce(portion, this);
        }
    }

    public void prompt(){
        System.out.println("I am producer #" + number + " and I just produced " + portion + ".");
    }
}
