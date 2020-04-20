import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    /* ********Initial Parameters ********/
    /** Total number of passengers boarding the plane (should be set using command-line argument*/
    public static int numPassengers = 30;
    /** Number of zones  */
    public static int groupNum = 4;
    /** Max allowed customers at a counter */
    public static int counterNum = 3;
    /** Number of check-inCounters  */
    public static int numKiosks = 2;

    /* ********Threads ********/
    /** Array of Kiosk Clerks */
    public static KioskClerkThread[] clerks;
    /** Single Flight Attendant */
    public static FlightAttendantThread flightAttendant;
    /** Array of Passengers */
    public static PassengerThread[] passengers;
    /** Clock Thread */
    public static ClockThread clock;

    /** Main Method creates the first thread object
     * @param args Single numPassenger argument */
    public static void main(String[] args) {
        /* Input validation for numPassengers */
        if (args.length == 1) {
            int tempVal;
            try {
                tempVal = Integer.parseInt(args[0]);
            }catch (NumberFormatException e) {
                throw new NumberFormatException("The argument<" + args[0] + "> is not a valid number");
            }
            if (tempVal > 0)
                numPassengers = tempVal;
            else
                throw new IllegalArgumentException("The argument<" + args[0] + "> is not a positive integer");
        }
        //Start timer
        int setTimer = 100000;

        /* Initialize kiosk clerk threads */
        clerks = new KioskClerkThread[numKiosks];
        for (int i = 0; i < numKiosks; i++) {
            clerks[i] = new KioskClerkThread(i);
        }

        /* Initialize Flight Attendant Thread */
        flightAttendant = new FlightAttendantThread(0);
        
        /* Initialize passenger threads */
        passengers = new PassengerThread[numPassengers];
        for (int i = 0; i < numPassengers; i++) {
            passengers[i] = new PassengerThread(i);
        }

        /* Initialize Clock Thread */
        clock = new ClockThread(0);





        
        /* To avoid crowding at the check-in counters, the airline asks passengers to form lines of no more than a
        few passengers at each counter(determined by counterNum */



        System.out.println(numPassengers);





    }
}
