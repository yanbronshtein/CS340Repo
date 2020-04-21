public class Main {
    public static PassengerThread[] passengers;
    public static KioskClerkThread[] clerks;
    public static int numPassengers = 30;
    public static int numClerks = 2;
    public static void main(String[] args) {
        passengers = new PassengerThread[numPassengers]; //4 passengers
        clerks = new KioskClerkThread[numClerks]; //only two clerks

        // Create clerk threads
        for (int i = 0; i < numClerks; i++) {
            clerks[i] = new KioskClerkThread(i);
        }
        //Create passenger threads
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new PassengerThread(i);
        }

        //Start clerk threads
        for (KioskClerkThread clerk : clerks) {
            clerk.start();
        }

        //Start passenger threads
        for (PassengerThread passenger : passengers) {
            passenger.start();
        }










    }
}
