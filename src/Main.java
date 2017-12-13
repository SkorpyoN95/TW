import randomportion.Consumer;
import randomportion.Monitor;
import randomportion.Producer;

public class Main {
    private static final int M = 10;
    private static final int N = 3;
    private static final Monitor monitor = new Monitor(M);

    public static void main(String[] args){
        pcrandTest(N);
    }

    private static void pcrandTest(int nn){
        Thread[] producers = new Thread[nn];
        Thread[] consumers = new Thread[nn];

        for(int i = 0, k = nn - 1; i < k; i++) {
            producers[i] = new Thread(new Producer(monitor, i, 1));
            consumers[i] = new Thread(new Consumer(monitor, i, 2));
        }

        producers[nn-1] = new Thread(new Producer(monitor, nn-1, 1));
        consumers[nn-1] = new Thread(new Consumer(monitor, nn-1, monitor.M));

        for(int i=0; i<nn; i++) {
            producers[i].start();
            consumers[i].start();
        }
    }
}
