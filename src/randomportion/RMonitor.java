package randomportion;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RMonitor {
    public final int M;
    private int value = 0;
    private final Lock lock = new ReentrantLock(true);
    private final Condition firstProducing = lock.newCondition(),
                            restProducing = lock.newCondition(),
                            firstConsuming = lock.newCondition(),
                            restConsuming = lock.newCondition();
    private int waitingProducers = 0, waitingConsumers = 0;
    private boolean prodIsWaiting = false, consIsWaiting = false;

    public RMonitor(int m) {
        M = m;
    }

    public void produce(int portion, RProducer producer){
        lock.lock();
        try{
            if(prodIsWaiting){
                waitingProducers++;
                restProducing.await();
                waitingProducers--;
            }
            while(2*M < value + portion){
                prodIsWaiting = true;
                firstProducing.await();
            }
            value += portion;
            producer.prompt();
            prodIsWaiting = false;
            if(waitingProducers > 0) restProducing.signal();
            firstConsuming.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void consume(int portion, RConsumer consumer){
        lock.lock();
        try{
            if(consIsWaiting){
                waitingConsumers++;
                restConsuming.await();
                waitingConsumers--;
            }
            while(value < portion){
                consIsWaiting = true;
                firstConsuming.await();
            }
            value -= portion;
            consumer.prompt();
            consIsWaiting = false;
            if(waitingConsumers > 0) restConsuming.signal();
            firstProducing.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }
}
