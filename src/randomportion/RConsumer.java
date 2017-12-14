package randomportion;

import java.util.Random;

public class RConsumer implements Runnable {
    private RMonitor monitor;
    private final int portion;
    private final int number;

    public RConsumer(RMonitor monitor, int number) {
        this.monitor = monitor;
        Random generator = new Random();
        portion = generator.nextInt(monitor.M-1) + 1;
        this.number = number;
    }

    public RConsumer(RMonitor monitor, int number, int portion) {

        this.monitor = monitor;
        this.portion = portion;
        this.number = number;
    }

    @Override
    public void run() {
        while(true){
            monitor.consume(portion, this);
        }
    }

    public void prompt(){
        System.out.println("I am consumer #" + number + " and I just consumed " + portion + ".");
    }
}
