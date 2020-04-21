import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    /* ********Initial Parameters ********/
    /** This is going to be our basic time unit in milliseconds */
    public static final long THIRTY_MINUTES = 750;
    /** Total number of passengers boarding the plane (should be set using command-line argument*/
    public static int numPassengers = 30;
    /** Size of groups entering the plane  */
    public static int groupNum = 4;
    /** Max allowed customers at one of the two kiosks */
    public static int counterNum = 3;
    /** Number of check-inCounters  */
    public static int numKiosks = 2;


    /* ********Threads ********/
    /** Array of Kiosk Clerks */
    public static KioskClerkThread[] clerks;
//    public static KioskClerkThread clerk1;
//    public static KioskClerkThread clerk2;

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

            if (!(tempVal > 0 && tempVal <= 30))
                throw new IllegalArgumentException("The argument<" + args[0] + "> is not a positive integer");
            else
                numPassengers = tempVal;
        }
        //Start timer
        int totalTime = 100000;


        /* Initialize kiosk clerk threads */
        clerks = new KioskClerkThread[numKiosks];
        for (int i = 0; i < numKiosks; i++)
            clerks[i] = new KioskClerkThread(i);



        /* Initialize Flight Attendant Thread */
        flightAttendant = new FlightAttendantThread(0);
        
        /* Initialize passenger threads */
        passengers = new PassengerThread[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            passengers[i] = new PassengerThread(i);

        /* Initialize Clock Thread */
        clock = new ClockThread(totalTime);

        /* Start kiosk clerk threads */
//        clerk1.start();
//        clerk2.start();
        for (KioskClerkThread clerk : clerks) {
            clerk.start();
        }


        /* Start Flight Attendant thread */
        flightAttendant.start();

        /* Start the Passenger Thread */
        for (PassengerThread passenger: passengers)
            passenger.start();

        /* Start clock thread */
        clock.start();




        
        /* To avoid crowding at the check-in counters, the airline asks passengers to form lines of no more than a
        few passengers at each counter(determined by counterNum */



        System.out.println(numPassengers);





    }
}
