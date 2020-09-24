package Task1;

public class Main {

    public static void main(String[] args) {
        int operatorsNum = 10;
        int callsNum = 60;

        AutomaticPhoneStation station = new AutomaticPhoneStation(callsNum);
        PhoneOperator operator = new PhoneOperator(station);

        while (operatorsNum-- > 0) {
            new Thread(null, operator::waitForCall, "Operator" + operatorsNum).start();
        }
        Thread stationThr = new Thread(null, station::generateCalls, "Station");
        stationThr.start();
    }
}
