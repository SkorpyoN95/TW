import randomportion.*;
import asynchronized.*;
import activeobject.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static java.lang.Thread.sleep;


public class Main {
    private static final int M = 10;
    private static final int N = 3;
    private static final RMonitor monitor = new RMonitor(M);
    private static Buffer buffer = new Buffer(2*M);
    private static final AMonitor monitor2 = new AMonitor(buffer);
    private static final Servant servant = new Servant(M);
    private static final Scheduler scheduler = new Scheduler();
    private static final Proxy proxy = new Proxy(scheduler, servant);

    public static void main(String[] args) {
        //pcrandTest(N);
        //pcasyncTest(N);
        //pcaoTest(N);
        try {
            PrintWriter writer = new PrintWriter("results.csv", "UTF-8");
            writer.println("threads,counter1,counter2,counter3");
            runTests(100, writer);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    private static int randTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new RProducer(monitor, counter, i, i%M+1));
            consumers[i] = new Thread(new RConsumer(monitor, counter, i, i%M+1));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer result = counter[0];

        for(int i=0; i<nn; i++) {
            producers[i].stop();
            consumers[i].stop();
        }

        return result;
    }

    private static int asyncTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AProducer(monitor2, buffer, counter, i, i%M+1));
            consumers[i] = new Thread(new AConsumer(monitor2, buffer, counter, i, i%M+1));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer result = counter[0];

        for(int i=0; i<nn; i++) {
            producers[i].stop();
            consumers[i].stop();
        }

        return result;
    }

    private static int aoTest(int nn){
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AOProducer(proxy, counter, i%M+1));
            consumers[i] = new Thread(new AOConsumer(proxy, counter, i%M+1));
        }

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer result = counter[0];

        for(int i=0; i<nn; i++) {
            producers[i].stop();
            consumers[i].stop();
        }

        return result;
    }

    private static void runTests(int nn, PrintWriter pw){
        for(int i = 1; i <= nn; i++) {
            String line = String.format("%s,%s,%s,%s", i, randTest(i), asyncTest(i), aoTest(i));
            System.out.println(line);
            pw.println(line);
        }
        System.out.println("Finished");
    }
}
