import randomportion.*;
import asynchronized.*;
import activeobject.*;

import java.util.Random;


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
    }

    /**/
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
    /**/
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
    /**/
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

}
