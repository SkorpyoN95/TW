package activeobject;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Scheduler {
    private BlockingDeque<MethodRequest> producers = new LinkedBlockingDeque<>(),
                                        consumers = new LinkedBlockingDeque<>();

    public Scheduler(){
        Thread producer = new Thread(()->dispatch(producers));
        Thread consumer = new Thread(()->dispatch(consumers));
        producer.start();
        consumer.start();
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

    private void dispatch(BlockingDeque<MethodRequest> queue){
        try {
            while(true) {
                MethodRequest mr = queue.takeFirst();
                if(!mr.guard()){
                    queue.putFirst(mr);
                }
                else {
                    mr.call();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
