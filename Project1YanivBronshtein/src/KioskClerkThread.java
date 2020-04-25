import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
/** This class simulates the behavior of the Kiosk Clerk in an airport
 * @author Yaniv Bronshtein
 * @version 1.0
 * */
public class KioskClerkThread extends Thread {
    /** Time in thread at creation */
    public static long time = System.currentTimeMillis();
    /** Vector that holds the passengers for the first kiosk counter */
    public static final Vector<PassengerThread> c1Queue = new Vector<>(Main.counterNum);
    /** Vector that holds the passengers for the second kiosk counter */
    public static final Vector<PassengerThread> c2Queue = new Vector<>(Main.counterNum);
    /** Total number of passengers served by both clerks */
//    public static int totalNumberPassengersServed;
    public static AtomicInteger totalNumPassengersServed = new AtomicInteger(0);
    /** ID of KioskClerkThread */
    final int id;
    /** Vector that contains a list of randomized seat numbers */
    private static Vector<Integer> randomNumbersVec = new Vector<>(30);
    /** Number of passengers belonging to zone 1 processed by kiosk clerk */
    public static AtomicInteger z1KioskCount = new AtomicInteger(0);
    /** Number of passengers belonging to zone 2 processed by kiosk clerk */
    public static AtomicInteger z2KioskCount = new AtomicInteger(0);
    /** Number of passengers belonging to zone 3 processed by kiosk clerk */
    public static AtomicInteger z3KioskCount = new AtomicInteger(0);
    /** Counter of passengers waiting on line at the first counter */
    public static AtomicInteger c1Size = new AtomicInteger(0);
    /** Counter of passengers waiting on line at the second counter */
    public static AtomicInteger c2Size = new AtomicInteger(0);

    /** Constructor creates a KioskClerkThread. the name of the thread is initialized as well the totalNumPassengersServed.
     * The generateRandomNumbers() function is called to generateRandomNumbers for boarding pass seat numbers
     * @param num ID -1*/
    public KioskClerkThread(int num) {
        id = num + 1;
        setName("KioskClerk-" + id);
        randomNumbersVec = generateRandomNumbers();
    }

    /** This function creates a vector of all the numbers between 1 and 30 and shuffles them. */
    private Vector<Integer> generateRandomNumbers() {
        Vector<Integer> vec = new Vector<>();
        for (int i = 1; i <= 30; i++) {
            vec.add(i);
        }
        Collections.shuffle(vec);
        return vec;
    }


    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    /** This method is executed when the thread is started in the main method in the Main class
     * Here the Kiosk Clerks deque passengers from their counters and assign them boarding passes  */
    @Override
    public void run() {
        /* While the two Kiosk Clerks combined have not served all the passengers that arrived,
         * we first check the id of the thread. If the id is 1 and the first queue is not empty, passengers
         * are removed there
         * if the id is 2 and the second queue is not empty passengers are removed from there
         * In both cases, the assignBoardingPass() method is called on the passengers
         * and the totalNumberPassengersServed is incremented   */
        while (totalNumPassengersServed.get() < Main.numPassengers) {
            if (id == 1 && !c1Queue.isEmpty()) {
                PassengerThread servedPassenger = c1Queue.remove(0);
                c1Size.getAndDecrement();
                assignBoardingPass(servedPassenger);
                totalNumPassengersServed.getAndIncrement();
            }
            if (id == 2 && !c2Queue.isEmpty()) {
                PassengerThread servedPassenger = c2Queue.remove(0);
                c2Size.getAndDecrement();
                assignBoardingPass(servedPassenger);
                totalNumPassengersServed.getAndIncrement();
            }
        }
        /* As soon as the while loop is exited, the clerks are done for the day */
// TMP COMMENT       msg("All passengers at counter " + id + " have been served. Thread Terminating");


        msg("All passengers at counter " + id + " have been served. Thread Terminating");
    }

    /** This method is used by the KioskClerkThread to assign the passenger a boarding pass(seat and zone num)
     * @param servedPassenger passenger serviced by KioskClerk */
    private void assignBoardingPass(PassengerThread servedPassenger) {
        /* Chose a number from the front of the previously populated randomNumbersVec and remove it.
        Assign a zone based on the seat number range  */
        int seatNum = randomNumbersVec.remove(0);
        int zoneNum;
        if (seatNum >= 0 && seatNum <= 10) {
            zoneNum = 1;
            z1KioskCount.getAndIncrement();
        }
        else if (seatNum >= 11 && seatNum <= 20) {
            zoneNum = 2;
            z2KioskCount.getAndIncrement();
        }
        else {
            zoneNum = 3;
            z3KioskCount.getAndIncrement();
        }

        /* Update the passenger info vector in the PassengerThread (position 1: zoneNum, position 2: seatNum) */
        servedPassenger.passengerInfo.set(1, zoneNum);
        servedPassenger.passengerInfo.set(2, seatNum);
        msg(servedPassenger.getName() + ": is in seat " + seatNum + " and zone " + zoneNum);
    }
}
