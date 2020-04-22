public class Main1 {
    public static PassengerThread1[] passengers;
    public static KioskClerkThread1[] clerks;
    public static int numPassengers = 3;
    public static int numClerks = 2;
    public static int counterNum = 3;
    public static void main(String[] args) {
        passengers = new PassengerThread1[numPassengers]; //4 passengers
        clerks = new KioskClerkThread1[numClerks]; //only two clerks

        // Create clerk threads
        for (int i = 0; i < numClerks; i++) {
            clerks[i] = new KioskClerkThread1(i);
        }
        //Create passenger threads
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new PassengerThread1(i);
        }

        //Start clerk threads
        for (KioskClerkThread1 clerk : clerks) {
            clerk.start();
        }

        //Start passenger threads
        for (PassengerThread1 passenger : passengers) {
            passenger.start();
        }










    }
}
