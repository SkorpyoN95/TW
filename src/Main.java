import randomportion.*;
import asynchronized.*;
import activeobject.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static java.lang.Thread.sleep;


public class Main {

    public static void main(String[] args) {
        //pcrandTest(N);
        //pcasyncTest(N);
        //pcaoTest(N);
        try {
            PrintWriter writer = new PrintWriter("results3.csv", "UTF-8");
            writer.println("threads,random,async,activeobject");
            runTests(50, 5, writer);
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

    private static int randTest(int nn, int ww, int mm){
        final RMonitor monitor = new RMonitor(mm);
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new RProducer(monitor, counter, i, i%mm+1, ww));
            consumers[i] = new Thread(new RConsumer(monitor, counter, i, i%mm+1, ww));
        }

        RProducer.startCounting();
        RConsumer.startCounting();

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter[0];
    }

    private static int asyncTest(int nn, int ww, int mm){
        Buffer buffer = new Buffer(2*mm);
        final AMonitor monitor2 = new AMonitor(buffer);
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AProducer(monitor2, buffer, counter, i, i%mm+1, ww));
            consumers[i] = new Thread(new AConsumer(monitor2, buffer, counter, i, i%mm+1, ww));
        }

        AProducer.startCounting();
        AConsumer.startCounting();

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter[0];
    }

    private static int aoTest(int nn, int ww, int mm){
        final Servant servant = new Servant(mm);
        final Scheduler scheduler = new Scheduler();
        final Proxy proxy = new Proxy(scheduler, servant);
        final int[] counter = {0};
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0; i < nn; i++) {
            producers[i] = new Thread(new AOProducer(proxy, counter, i%mm+1, ww));
            consumers[i] = new Thread(new AOConsumer(proxy, counter, i%mm+1, ww));
        }

        AOProducer.startCounting();
        AOConsumer.startCounting();

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter[0];
    }

    private static void runTests(int nn, int ww, PrintWriter pw){
        for (int i = 1; i <= nn; i++) {
            String line = String.format("%d,%d,%d,%d", i, randTest(i, 2500, 10), asyncTest(i, 2500, 10), aoTest(i, 2500, 10));
            System.out.println(line);
            pw.println(line);
        }
        System.out.println("Finished");
    }
}
