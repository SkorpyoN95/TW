import randomportion.*;
//import asynchronized.*;
//import activeobject.*;

import java.util.Random;

public class Main {
    private static final int M = 10;
    private static final int N = 3;
    private static final randomportion.Monitor monitor = new randomportion.Monitor(M);
    //private static Buffer buffer = new Buffer(M);
    //private static final asynchronized.Monitor monitor2 = new asynchronized.Monitor(buffer);
    //private static final Servant servant = new Servant(M);
    //private static final Scheduler scheduler = new Scheduler();
    //private static final Proxy proxy = new Proxy(scheduler, servant);

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
            producers[i] = new Thread(new Producer(monitor, i, 1));
            consumers[i] = new Thread(new Consumer(monitor, i, 2));
        }

        producers[nn-1] = new Thread(new Producer(monitor, nn-1, 1));
        consumers[nn-1] = new Thread(new Consumer(monitor, nn-1, monitor.M));

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
            producers[i] = new Thread(new Producer(monitor, buffer, i, 1));
            consumers[i] = new Thread(new Consumer(monitor, buffer, i, 2));
        }

        producers[nn-1] = new Thread(new Producer(monitor, buffer, nn-1, 1));
        consumers[nn-1] = new Thread(new Consumer(monitor, buffer, nn-1, buffer.getSize()/2));

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }

    private static void pcaoTest(int nn){
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];
        Random generator = new Random();

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new Producer(proxy));
            consumers[i] = new Thread(new Consumer(proxy));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }
    */

}
