import java.util.concurrent.Semaphore;

/** This class contains the main method which creates a thread that in turn starts Clock, Kiosk Clerk, Flight Attendant,
 * and passenger threads
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class Main {
    /**Single clock thread */
    public static ClockThread clock;
    /** Array of KioskClerkThreads */
    public static KioskClerkThread[] clerks;
    /** Array of PassengerThreads */
    public static PassengerThread[] passengers;
    /** Single flight attendant thread */
    public static FlightAttendantThread flightAttendant;
    /** Number of passengers boarding plane. The max is 30 and the default is 30 but a number between 1 and 30 can be
     * provided by the user */
    public static int numPassengers =4;
    /** Number of check-in counters */
    public static int numClerks = 2;
    /** Max number of people allowed at a time at the kiosk counters */
    public static int counterNum = 3;
    /** Max number of allowed passengers in a group boarding the plane */
    public static int groupNum = 4;
    /** Constant specifying the time unit of 30 min as 2000 milliseconds  */
    public static final long THIRTY_MIN = 2000;
    /** Clerk counting semaphore */
    public static Semaphore clerksAvailable = new Semaphore(2, true);
    /** Customer Counting semaphore */
    public static Semaphore customers = new Semaphore(0, true);
    /** Zone1 Queue */
    public static Semaphore zone1 = new Semaphore(0, true);
    /** Zone2 Queue */
    public static Semaphore zone2 = new Semaphore(0, true);
    /** Zone3 Queue */
    public static Semaphore zone3 = new Semaphore(0, true);
    /** Binary semaphore released by the flight attendant letting the passengers know the plane is landing */
    public static Semaphore landing = new Semaphore(0, true);
    /** Blocking semaphore controlled by Clock letting Flight attendant know to start boarding process */
    public static Semaphore timeToBoard = new Semaphore(0, true);
    /** Blocking semaphore controlled by Clock letting Flight attendant know to start landing process */
    public static Semaphore timeToLand = new Semaphore(0, true);

    /**main() method
     * @param args Single command line argument for the number of passengers */
    public static void main(String[] args) {
        /*Create clock thread */
        clock = new ClockThread(12*THIRTY_MIN);

        /* Create clerk threads */
        clerks = new KioskClerkThread[numClerks]; //only two clerks
        for (int i = 0; i < numClerks; i++) {
            clerks[i] = new KioskClerkThread(i);
        }
        /*Create passenger threads */
        passengers = new PassengerThread[numPassengers]; //4 passengers
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new PassengerThread(i);
        }
        /*Create Flight attendant thread */
        flightAttendant = new FlightAttendantThread();

        /*Start clock thread */
        clock.start();

        /* Start clerk threads */
        for (KioskClerkThread clerk : clerks) {
            clerk.start();
        }

        /* Start passenger threads */
        for (PassengerThread passenger : passengers) {
            passenger.start();
        }

        /* Start flight attendant thread */
        flightAttendant.start();

    }
}
