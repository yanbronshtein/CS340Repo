import java.util.*;
import java.util.concurrent.Semaphore;

/** This class contains the main method which creates a thread that in turn starts Clock, Kiosk Clerk, Flight Attendant,
 * and passenger threads
 * @author Yaniv Bronshtein
 * @version 2.0*/
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
    public static int numPassengers = 30;
    /** Number of check-in counters */
    public static int numClerks = 2;
    /** Max number of allowed passengers in a group boarding the plane */
    public static int groupNum = 4;
    /** Constant specifying the time unit of 30 min as 2000 milliseconds  */
    public static final long THIRTY_MIN = 2000;
    /** Clerk counting semaphore */
    public static Semaphore clerksAvailable = new Semaphore(0, true);
    /** Passenger Counting semaphore */
    public static Semaphore passengersAtKiosk = new Semaphore(0, true);
    /** Zone1 Queue */
    public static Semaphore zone1Queue = new Semaphore(0, true);
    /** Zone2 Queue */
    public static Semaphore zone2Queue = new Semaphore(0, true);
    /** Zone3 Queue */
    public static Semaphore zone3Queue = new Semaphore(0, true);
    /* Queue for passengers waiting at the door to enter in groups of groupNum */
    public static Semaphore boardingPlaneQueue = new Semaphore(0, true);
    /** Blocking semaphore controlled by Clock letting Flight attendant know to start boarding process */
    public static Semaphore timeToBoard = new Semaphore(0, true);
    /** Blocking semaphore controlled by Clock letting Flight attendant know to start landing process */
    public static Semaphore timeToLand = new Semaphore(0, true);
    /** Vector or random numbers for seats between 1 and 30 */
    public static Vector<Integer> listOfSeatNumbers = generateRandomNumbers();
    /** Mutex used by the passenger to select their boarding pass from the vector and to access the inOrderExiting TreeMap  */
    public static Semaphore mutexPassenger = new Semaphore(1, true);
    /** Mutex used by the clerk to check the number of passengers served and increment that number */
    public static Semaphore mutexClerk = new Semaphore(1, true);
    /** Boolean variable set by flight attendant and checked by all the passengers to know
     * whether or not to enter the zoneQueue or boardingPlaneQueue */
    public static volatile boolean isGateClosed = false;
    /** Blocking semaphore instance variable */
    public static Semaphore passengerEnteringPlane = new Semaphore(0, true);
    /* Semaphore used by flight attendant to let clock know to terminate */
    public static Semaphore flightAttendantDoneCleaning = new Semaphore(0, true);
    /** TreeMap for passengers to leave plane in order of their seat number */
    public static TreeMap<Integer, Semaphore> inOrderExiting = new TreeMap<>();

    /**main() method
     * @param args Single command line argument for the number of passengers */
    public static void main(String[] args) {
        /* Command line argument validation */
        if (args.length == 1) {
            System.out.println("args provided: " + args[0]);
            int tempVal;
            try {
                tempVal = Integer.parseInt(args[0]);
            }catch (NumberFormatException e) {
                throw new IllegalArgumentException("The number entered is an invalid integer");
            }
            if (tempVal < 1 || tempVal > 30)
                throw new IllegalArgumentException("The number entered is not between 1 and 30");
            numPassengers = tempVal;
        }
        /*Create clock thread */
        clock = new ClockThread();

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

    /** This function creates a vector of all the numbers between 1 and 30 and shuffles them. */
    private static Vector<Integer> generateRandomNumbers() {
        Vector<Integer> vec = new Vector<>();
        for (int i = 1; i <= 30; i++) {
            vec.add(i);
        }
        Collections.shuffle(vec);
        return vec;
    }
}
