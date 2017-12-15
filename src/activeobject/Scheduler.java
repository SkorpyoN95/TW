package activeobject;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {
    private BlockingDeque<MethodRequest>    producers = new LinkedBlockingDeque<>(),
                                            consumers = new LinkedBlockingDeque<>();
    private final Lock lock = new ReentrantLock(true);
    private final Condition producer = lock.newCondition(),
                            consumer = lock.newCondition();

    public Scheduler(){
        Thread scheduler = new Thread(()->dispatch());
        scheduler.start();
    }

    public void enqueueProducer(MethodRequest mr){
        try {
            producers.putLast(mr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enqueueConsumer(MethodRequest mr){
        try {
            consumers.putLast(mr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(){
        MethodRequest mrp, mrc;
        try {
            while(true) {
                mrp = producers.takeFirst();
                while(!mrp.guard()){
                    mrc = consumers.takeFirst();
                    mrc.call();
                }
                mrp.call();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
