import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KioskClerkThread extends Thread {
    public static long time = System.currentTimeMillis();
    public static Vector<PassengerThread> c0Deque = new Vector<>(Main.counterNum);
    public static Vector<PassengerThread> c1Deque = new Vector<>(Main.counterNum);
    public static Vector<PassengerThread> waitDeque = new Vector<>();
    public static int totalNumberPassengersServed;
    private static int id;
    private static ArrayList<Integer> randomNumbersList = new ArrayList<>();
    public static AtomicInteger z1KioskCount = new AtomicInteger(0);
    public static AtomicInteger z2KioskCount = new AtomicInteger(0);
    public static AtomicInteger z3KioskCount = new AtomicInteger(0);

    public KioskClerkThread(int num) {
        setName("KioskClerk-" + num);
        id = num;
        totalNumberPassengersServed = 0;
        randomNumbersList = generateRandomNumbers();
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
                    }
                } else {
                    continue;
                }
            }
            if (currentThread().getName().equals("KioskClerk-1")) {
                if (!c1Deque.isEmpty()) {
                    PassengerThread servedPassenger = c1Deque.remove(0);
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;
                    if (!waitDeque.isEmpty()) {
                        c1Deque.add(waitDeque.remove(0));
                    }
                }

            }
        }
        msg("All passengers have been served. Check-in clerks done for the day");

    }

    private void assignTicket(PassengerThread servedPassenger) {
        int seatNum = randomNumbersList.remove(0);
        int zoneNum;
        if (seatNum >= 0 && seatNum <= 10) {
            zoneNum = 1;
            z1KioskCount.getAndAdd(1);
//            FlightAttendantThread.zone1Count.addAndGet(1);
//            FlightAttendantThread.zone1Queue.add(servedPassenger);
        }
        else if (seatNum >= 11 && seatNum <= 20) {
            zoneNum = 2;

            z2KioskCount.getAndAdd(1);
//            FlightAttendantThread.zone2Count.addAndGet(1);

//            FlightAttendantThread.zone2Queue.add(servedPassenger);
        }
        else {
            zoneNum = 3;
            z3KioskCount.getAndAdd(1);

//            FlightAttendantThread.zone3Count.addAndGet(1);

//            FlightAttendantThread.zone3Queue.add(servedPassenger);

        }
        servedPassenger.setZoneNum(zoneNum);
        servedPassenger.setTicketNum(seatNum);
        msg(servedPassenger.getName() + ": is in seat " + seatNum + " and zone " + zoneNum);
    }
}
