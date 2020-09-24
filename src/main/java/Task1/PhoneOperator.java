package Task1;

public class PhoneOperator {

    AutomaticPhoneStation station;
    int maxWaitCount = 4;
    int waitTimeout = 500;
    int busyTimeout = 4000;

    public PhoneOperator(AutomaticPhoneStation station) {
        this.station = station;
    }

    public void waitForCall() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) return;
            try {
                int waitCount = 0;
                while (station.calls.size() == 0 && waitCount < maxWaitCount) {
                    Thread.sleep(waitTimeout);
                    waitCount++;
                }
                if (waitCount == maxWaitCount) return;
                String callNumber = station.calls.take();
                System.out.println(Thread.currentThread().getName() + " звонит по " + callNumber);
                Thread.sleep(busyTimeout);
                System.out.println(Thread.currentThread().getName() + " закончил ");
            } catch (InterruptedException e) {
            }
        }
    }
}
