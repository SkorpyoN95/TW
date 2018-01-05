package activeobject;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Future {
    private boolean availaible = false;
    private int value;
    private final Lock lock = new ReentrantLock(true);
    private final Condition waiting = lock.newCondition();
    private int[] counter;

    public Future(int[] counter){
        this.counter = counter;
    }

    public boolean isAvailaible(){
        return availaible;
    }

    public int blockingGet(){
        lock.lock();
        try{
            while(!availaible){
                waiting.await();
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return value;
    }

    public Integer nonblockingGet(){
        if(availaible)  return value;
        else            return null;
    }

    public void update(int v){
        lock.lock();
        try {
            value = v;
            availaible = true;
            if(System.currentTimeMillis() - AOConsumer.getTimer() < 10000) counter[0]++;
            waiting.signal();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
