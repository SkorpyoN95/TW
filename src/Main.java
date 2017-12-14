import randomportion.*;
import asynchronized.*;
import activeobject.*;

import java.util.Random;

import static java.lang.Thread.sleep;


public class Main {
    private static final int M = 10;
    private static final int N = 3;
    private static final RMonitor monitor = new RMonitor(M);
    private static Buffer buffer = new Buffer(M);
    private static final AMonitor monitor2 = new AMonitor(buffer);
    private static final Servant servant = new Servant(M);
    private static final Scheduler scheduler = new Scheduler();
    private static final Proxy proxy = new Proxy(scheduler, servant);

    public static void main(String[] args){
        //pcrandTest(N);
        //pcasyncTest(N);
        //pcaoTest(N);
        aoTest(1);
    }

    /*
    private static void pcrandTest(int nn){
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0, k = nn - 1; i < k; i++) {
            producers[i] = new Thread(new RProducer(monitor, i, 1));
            consumers[i] = new Thread(new RConsumer(monitor, i, 2));
        }

        producers[nn-1] = new Thread(new RProducer(monitor, nn-1, 1));
        consumers[nn-1] = new Thread(new RConsumer(monitor, nn-1, monitor.M));

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }
    /*
    private static void pcasyncTest(int nn){
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0, k = nn - 1; i < k; i++) {
            producers[i] = new Thread(new AProducer(monitor2, buffer, i, 1));
            consumers[i] = new Thread(new AConsumer(monitor2, buffer, i, 2));
        }

        producers[nn-1] = new Thread(new AProducer(monitor2, buffer, nn-1, 1));
        consumers[nn-1] = new Thread(new AConsumer(monitor2, buffer, nn-1, buffer.getSize()/2));

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }
    /*
    private static void pcaoTest(int nn){
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];
        Random generator = new Random();

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AOProducer(proxy));
            consumers[i] = new Thread(new AOConsumer(proxy));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }
    /**/

    private static void randTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new RProducer(monitor, counter, i));
            consumers[i] = new Thread(new RConsumer(monitor, counter, i));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter is now: " + counter[0]);
    }

    private static void asyncTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AProducer(monitor2, buffer, counter, i));
            consumers[i] = new Thread(new AConsumer(monitor2, buffer, counter, i));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter is now: " + counter[0]);
    }

    private static void aoTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AOProducer(proxy, counter));
            consumers[i] = new Thread(new AOConsumer(proxy, counter));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter is now: " + counter[0]);
    }
}
