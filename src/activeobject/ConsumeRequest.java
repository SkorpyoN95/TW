package activeobject;

public class ConsumeRequest implements MethodRequest {
    private Servant servant;
    final private int portion;
    final private Future future;

    public ConsumeRequest(Servant servant, Future future, int portion) {
        this.servant = servant;
        this.portion = portion;
        this.future = future;
    }

    @Override
    public boolean guard() {
        return servant.getValue() >= portion;
    }

    @Override
    public void call() {
        servant.consume(portion);
        future.update(portion);
    }
}
