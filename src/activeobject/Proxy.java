package activeobject;

public class Proxy {
    private final Scheduler scheduler;
    private final Servant servant;

    public Proxy(Scheduler scheduler, Servant servant) {
        this.scheduler = scheduler;
        this.servant = servant;
    }

    public Future produce(int portion, int[] counter){
        Future future = new Future(counter);
        MethodRequest mr = new ProduceRequest(servant, future, portion);
        scheduler.enqueueProducer(mr);
        return future;
    }
    
    public Future consume(int portion, int[] counter){
        Future future = new Future(counter);
        MethodRequest mr = new ConsumeRequest(servant, future, portion);
        scheduler.enqueueConsumer(mr);
        return future;
    }

    public int getServantSize(){
        return servant.M;
    }
}
