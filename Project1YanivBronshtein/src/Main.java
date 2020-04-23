public class Main {
    public static ClockThread clock;
    public static KioskClerkThread[] clerks;
    public static PassengerThread[] passengers;
    public static FlightAttendantThread flightAttendant;
    public static int numPassengers = 30;
    public static int numClerks = 2;
    public static int counterNum = 3;
    public static int groupNum = 4;
    public static final long THIRTY_MIN = 1000;
    public static void main(String[] args) {
        //Create clock thread
        clock = new ClockThread(12*THIRTY_MIN);

        // Create clerk threads
        clerks = new KioskClerkThread[numClerks]; //only two clerks
        for (int i = 0; i < numClerks; i++) {
            clerks[i] = new KioskClerkThread(i);
        }
        //Create passenger threads
        passengers = new PassengerThread[numPassengers]; //4 passengers
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new PassengerThread(i);
        }
        //Create Flight attendant thread
        flightAttendant = new FlightAttendantThread(0);

        //Start clock thread
        clock.start();

        //Start clerk threads
        for (KioskClerkThread clerk : clerks) {
            clerk.start();
        }

        //Start passenger threads
        for (PassengerThread passenger : passengers) {
            passenger.start();
        }

        //Start flight attendant thread
        flightAttendant.start();










    }
}
