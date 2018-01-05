package randomportion;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class RMonitor {
    public final int M;
    private int value = 0;
    private boolean[] elems;
    private final Lock lock = new ReentrantLock(true);
    private final Condition firstProducing = lock.newCondition(),
                            restProducing = lock.newCondition(),
                            firstConsuming = lock.newCondition(),
                            restConsuming = lock.newCondition();
    private int waitingProducers = 0, waitingConsumers = 0;
    private boolean prodIsWaiting = false, consIsWaiting = false;

    public RMonitor(int m) {
        M = m;
        elems = new boolean[2*m];
    }

    public void produce(int portion, RProducer producer){
        lock.lock();
        try{
            if(prodIsWaiting){
                waitingProducers++;
                restProducing.await();
                waitingProducers--;
            }
            while(2*M - 1 < value + portion){
                prodIsWaiting = true;
                firstProducing.await();
            }
            for(int i = value, j = value + portion; i < j; ++i)
                elems[i] = true;
            value += portion;
            //producer.prompt();
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
            for(int i = value, j = value - portion; i > j; --i)
                elems[i] = false;
            value -= portion;
            //consumer.prompt();
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
