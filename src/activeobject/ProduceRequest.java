package activeobject;

public class ProduceRequest implements MethodRequest {
    private Servant servant;
    final private int portion;
    final private Future future;

    public ProduceRequest(Servant servant, Future future, int portion) {
        this.servant = servant;
        this.portion = portion;
        this.future = future;
    }

    @Override
    public boolean guard() {
        return 2*servant.M >= servant.getValue() + portion;
    }

    @Override
    public void call() {
        servant.produce(portion);
        future.update(portion);
    }
}
