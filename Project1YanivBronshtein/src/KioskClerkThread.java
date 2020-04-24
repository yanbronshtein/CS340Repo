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
    public static int totalNumberPassengersServed;
    /** ID of KioskClerkThread */
    final int id;
    /** Vector that contains a list of randomized seat numbers */
    private static Vector<Integer> randomNumbersList = new Vector<>(30);
    /** Number of passengers belonging to zone 1 processed by kiosk clerk */
    public static AtomicInteger z1KioskCount = new AtomicInteger(0);
    /** Number of passengers belonging to zone 2 processed by kiosk clerk */
    public static AtomicInteger z2KioskCount = new AtomicInteger(0);
    /** Number of passengers belonging to zone 3 processed by kiosk clerk */
    public static AtomicInteger z3KioskCount = new AtomicInteger(0);

    /**  */
    public KioskClerkThread(int num) {
        id = num + 1;
        setName("KioskClerk-" + id);
        totalNumberPassengersServed = 0;
        randomNumbersList = generateRandomNumbers();
    }

    private Vector<Integer> generateRandomNumbers() {
        Vector<Integer> list = new Vector<>();
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    @Override
    public void run() {
        while (totalNumberPassengersServed < Main.numPassengers) {
//            if (currentThread().getName().equals("KioskClerk-1")) {
            if (id == 1) {

                if (!c1Queue.isEmpty()) {
                    PassengerThread servedPassenger = c1Queue.remove(0);
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;
                } else {
                    continue;
                }
            }
//            if (currentThread().getName().equals("KioskClerk-2")) {
            if (id == 2) {
                if (!c2Queue.isEmpty()) {
                    PassengerThread servedPassenger = c2Queue.remove(0);
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;
                }
            }
        }
        msg("All passengers at counter " + id + " have been served. Thread Terminating");
    }

    private void assignTicket(PassengerThread servedPassenger) {
        int seatNum = randomNumbersList.remove(0);
        int zoneNum;
        if (seatNum >= 0 && seatNum <= 10) {
            zoneNum = 1;
            z1KioskCount.getAndAdd(1);
        }
        else if (seatNum >= 11 && seatNum <= 20) {
            zoneNum = 2;
            z2KioskCount.getAndAdd(1);
        }
        else {
            zoneNum = 3;
            z3KioskCount.getAndAdd(1);
        }
        servedPassenger.passengerInfo.set(1, zoneNum);
        servedPassenger.passengerInfo.set(2, seatNum);
        msg(servedPassenger.getName() + ": is in seat " + seatNum + " and zone " + zoneNum);
    }
}
