package randomportion;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    public final int M;
    private int value = 0;
    private final Lock lock = new ReentrantLock(true);
    private final Condition firstProducing = lock.newCondition(),
                            restProducing = lock.newCondition(),
                            firstConsuming = lock.newCondition(),
                            restConsuming = lock.newCondition();
    private int waitingProducers = 0, waitingConsumers = 0;
    private boolean prodIsWaiting = false, consIsWaiting = false;

    public Monitor(int m) {
        M = m;
    }

    public void produce(int portion, Producer producer){
        lock.lock();
        try{
            if(prodIsWaiting){
                waitingProducers++;
                restProducing.await();
                waitingProducers--;
            }
            prodIsWaiting = true;
            while(2*M < value + portion){
                firstProducing.await();
            }
            prodIsWaiting = false;
            value += portion;
            producer.prompt();
            if(waitingProducers > 0) restProducing.signal();
            firstConsuming.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void consume(int portion, Consumer consumer){
        lock.lock();
        try{
            if(consIsWaiting){
                waitingConsumers++;
                restConsuming.await();
                waitingConsumers--;
            }
            consIsWaiting = true;
            while(value < portion){
                firstConsuming.await();
            }
            consIsWaiting = false;
            value -= portion;
            consumer.prompt();
            if(waitingConsumers > 0) restConsuming.signal();
            firstProducing.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }
}
