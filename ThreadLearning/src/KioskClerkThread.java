import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KioskClerkThread extends Thread {
    public static long time = System.currentTimeMillis();
    //    private static final int maxAllowedOnLine = 3;
//    public static boolean[] countersAvailable = {true,true}; //Initially both counters are free

    public static Vector<PassengerThread> c0Deque = new Vector<>(Main.counterNum);
    public static AtomicInteger c0Size;
    public static AtomicInteger c1Size;

    public static Vector<PassengerThread> c1Deque = new Vector<>(Main.counterNum);
    public static Vector<PassengerThread> waitDeque = new Vector<>();
    public static int totalNumberPassengersServed;
    private static int id;
    private static ArrayList<Integer> randomNumbersList = new ArrayList<>();
//    public static boolean counter1IsFree;
//    public static boolean counter2IsFree;

    public static boolean c0Free() {
        return c0Deque.size() < Main.counterNum;
    }

    public static boolean c1Free() {
        return c1Deque.size() < Main.counterNum;
    }

    public KioskClerkThread(int num) {
        setName("KioskClerk-" + num);
        id = num;
        totalNumberPassengersServed = 0;
        randomNumbersList = generateRandomNumbers();
        c0Size = new AtomicInteger(0);
        c1Size = new AtomicInteger(0);
    }

    private ArrayList<Integer> generateRandomNumbers() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= Main.numPassengers; i++) {
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
            if (currentThread().getName().equals("KioskClerk-0")) {
                if (!c0Deque.isEmpty()) {
                    PassengerThread servedPassenger = c0Deque.remove(0);
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;

                    if (!waitDeque.isEmpty()) {
                        c0Deque.add(waitDeque.remove(0));
                        c0Size.addAndGet(1);
                    }
//                } else {
//                    continue;
//                }
                }
                if (currentThread().getName().equals("KioskClerk-1")) {
                    if (!c1Deque.isEmpty()) {
                        c1Size.addAndGet(-1);
                        PassengerThread servedPassenger = c1Deque.remove(0);
                        assignTicket(servedPassenger);
                        totalNumberPassengersServed++;
                        if (!waitDeque.isEmpty()) {
                            c1Deque.add(waitDeque.remove(0));
                            c1Size.addAndGet(1);
                        }
                    }
                }
            }
        }

        msg("All passengers have been served. Check-in clerks done for the day");
    }

    
    private void assignTicket(PassengerThread servedPassenger) {
        int seatNum = randomNumbersList.remove(0);
        int zoneNum;
        if (seatNum >= 0 && seatNum <= 10)
            zoneNum = 1;
        else if (seatNum >= 11 && seatNum <= 20)
            zoneNum = 2;
        else
            zoneNum = 3;
        servedPassenger.setZoneNum(zoneNum);
        servedPassenger.setTicketNum(seatNum);
        msg(servedPassenger.getName() + ": is in seat " + seatNum + " and zone " + zoneNum);
    }
}
