package Task1;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class AutomaticPhoneStation {

    ArrayBlockingQueue<String> calls;
    private int callsNumToGenerate;
    private int timeout = 1000;
    private static Random random = new Random();

    public AutomaticPhoneStation(int callsNumToGenerate) {
        this.calls = new ArrayBlockingQueue<>(callsNumToGenerate);
        this.callsNumToGenerate = callsNumToGenerate;
    }

    public void generateCalls() {
        try {
            while (callsNumToGenerate-- > 0) {
                Thread.sleep(timeout);
                this.calls.add(
                        String.valueOf(rand(1, 998)) +
                                String.valueOf(rand(100, 999)) +
                                String.valueOf(rand(100, 999)) +
                                String.valueOf(rand(1000, 9999))
                );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int rand(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }
}
