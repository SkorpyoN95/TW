package asynchronized;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AMonitor {
    private LinkedList<Integer> emptyElems = new LinkedList<>(),
                                fullElems = new LinkedList<>();
    private final Lock lock = new ReentrantLock(true);
    private final Condition firstProducing = lock.newCondition(),
            restProducing = lock.newCondition(),
            firstConsuming = lock.newCondition(),
            restConsuming = lock.newCondition();
    private int waitingProducers = 0, waitingConsumers = 0;
    private boolean prodIsWaiting = false, consIsWaiting = false;

    public AMonitor(Buffer buffer) {
        emptyElems.addAll(IntStream.range(0, buffer.getSize()).boxed().collect(Collectors.toList()));
    }

    public LinkedList<Integer> startProduce(int portion){
        lock.lock();
        LinkedList<Integer> result = new LinkedList<>();
        try{
            if(prodIsWaiting){
                waitingProducers++;
                restProducing.await();
                waitingProducers--;
            }
            while(emptyElems.size() < portion){
                prodIsWaiting = true;
                firstProducing.await();
            }
            while(result.size() != portion) {
                result.add(emptyElems.removeFirst());
            }
            prodIsWaiting = false;
            if(waitingProducers > 0) restProducing.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
        return result;
    }

    public void endProduce(LinkedList<Integer> elems, Producer producer){
        lock.lock();
        try{
            fullElems.addAll(elems);
            producer.prompt();
            firstConsuming.signal();
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public LinkedList<Integer> startConsume(int portion){
        lock.lock();
        LinkedList<Integer> result = new LinkedList<>();
        try{
            if(consIsWaiting){
                waitingConsumers++;
                restConsuming.await();
                waitingConsumers--;
            }
            while(fullElems.size() < portion){
                consIsWaiting = true;
                firstConsuming.await();
            }
            while(result.size() != portion) {
                result.add(fullElems.removeFirst());
            }
            consIsWaiting = false;
            if(waitingConsumers > 0) restConsuming.signal();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
        return result;
    }

    public void endConsume(LinkedList<Integer> elems, Consumer consumer){
        lock.lock();
        try{
            emptyElems.addAll(elems);
            consumer.prompt();
            firstProducing.signal();
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
