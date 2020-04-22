import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KioskClerkThread1 extends Thread {
    public static long time = System.currentTimeMillis();
    //    private static final int maxAllowedOnLine = 3;
//    public static boolean[] countersAvailable = {true,true}; //Initially both counters are free

    public static Vector<PassengerThread1> c0Deque = new Vector<>(Main.counterNum);
    public static AtomicInteger c0Size;
    public static AtomicInteger c1Size;

    public static Vector<PassengerThread1> c1Deque = new Vector<>(Main.counterNum);
    public static Vector<PassengerThread1> waitDeque = new Vector<>();
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

    public KioskClerkThread1(int num) {
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
//    @Override
//    public void run() {
//
//        while (totalNumberPassengersServed < Main.numPassengers )
//
//            while (counter0Deque.isEmpty() && counter1Deque.isEmpty()) {
//            try {
//                sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        /* ******Critical Section ****/
//        if (!counter0Deque.isEmpty()) {
//            PassengerThread servicedPassenger = counter0Deque.removeFirst();
//
//            msg("Serviced customer first customer" +servicedPassenger.getName() );
//
//        }
//        if (!counter1Deque.isEmpty()){
//            PassengerThread servicedPassenger = counter1Deque.removeFirst();
//
//            msg("Serviced customer second customer" +servicedPassenger.getName() );
//        }
//
//        /* ****** End Critical Section ****/
//
//
//    }


    @Override
//    public void run() {
//
//        while (totalNumberPassengersServed < Main.numPassengers ) {
//            if (id == 0) {
//                if (!counter0Deque.isEmpty()) {
//                    PassengerThread servedPassenger = counter0Deque.removeFirst();
//                    int seatNum = randomNumbersList.remove(0);
//                    int zoneNum;
//                    if (seatNum >= 0 && seatNum <= 10)
//                        zoneNum = 1;
//                    else if (seatNum >= 11 && seatNum <= 20)
//                        zoneNum = 2;
//                    else
//                        zoneNum = 3;
//                    servedPassenger.setGroupNum(seatNum);
//                    servedPassenger.setZoneNum(zoneNum);
//                    msg(servedPassenger.getName() + ": is in seat " + seatNum +" and zone " + zoneNum);
//                    assignTicket(servedPassenger);
////                    servedPassenger.interrupt(); //interrupt served passenger so they go to security
//                    totalNumberPassengersServed++;
//                } else {
//                    waitingDeque.getFirst().interrupt();
//                }
////                else if (!waitingDeque.isEmpty())
////                    waitingDeque.getFirst().interrupt(); //interrupt passenger in front of waiting queue
//            } else {
//                if (!counter1Deque.isEmpty()) {
//                    PassengerThread servedPassenger = counter1Deque.removeFirst(); //interrupt served passenger so they go to security
//                    int seatNum = randomNumbersList.remove(0);
//                    int zoneNum;
//                    if (seatNum >= 0 && seatNum <= 10)
//                        zoneNum = 1;
//                    else if (seatNum >= 11 && seatNum <= 20)
//                        zoneNum = 2;
//                    else
//                        zoneNum = 3;
//                    servedPassenger.setZoneNum(zoneNum);
//                    servedPassenger.setTicketNum(seatNum);
////        passenger.setGroupNum(seatNum);
////        passenger.setZoneNum(zoneNum);
//                    msg(servedPassenger.getName() + ": is in seat " + seatNum +" and zone " + zoneNum);
////                    assignTicket(servedPassenger);
////                    servedPassenger.interrupt(); //interrupt passenger in front of waiting queue
//                    totalNumberPassengersServed++;
//                }
//                else {
//                    waitingDeque.getFirst().interrupt();
//                }
//            }
//        }
//    }


//    public void run() {
//
//        while (totalNumberPassengersServed < Main.numPassengers ) {
//            while (counter0Deque.isEmpty() && counter1Deque.isEmpty()) {
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            msg("Stuck in clerk while looop");
//            if (id == 0 && !counter0Deque.isEmpty()) {
//                PassengerThread servedPassenger = counter0Deque.removeFirst();
//                int seatNum = randomNumbersList.remove(0);
//                int zoneNum;
//                if (seatNum >= 0 && seatNum <= 10)
//                    zoneNum = 1;
//                else if (seatNum >= 11 && seatNum <= 20)
//                    zoneNum = 2;
//                else
//                    zoneNum = 3;
//                servedPassenger.setGroupNum(seatNum);
//                servedPassenger.setZoneNum(zoneNum);
//                msg(servedPassenger.getName() + ": is in seat " + seatNum + " and zone " + zoneNum);
////                    servedPassenger.interrupt(); //interrupt served passenger so they go to security
//                totalNumberPassengersServed++;
//            } if (id == 1 && !counter1Deque.isEmpty()) {
//                PassengerThread servedPassenger = counter1Deque.removeFirst(); //interrupt served passenger so they go to security
//                int seatNum = randomNumbersList.remove(0);
//                int zoneNum;
//                if (seatNum >= 0 && seatNum <= 10)
//                    zoneNum = 1;
//                else if (seatNum >= 11 && seatNum <= 20)
//                    zoneNum = 2;
//                else
//                    zoneNum = 3;
//                servedPassenger.setZoneNum(zoneNum);
//                servedPassenger.setTicketNum(seatNum);
//                msg(servedPassenger.getName() + ": is in seat " + seatNum +" and zone " + zoneNum);
//                totalNumberPassengersServed++;
//            }
//        }
//    }


    public void run() {
        while (totalNumberPassengersServed < Main.numPassengers) {
            if (currentThread().getName().equals("KioskClerk-0")) {
                if (!c0Deque.isEmpty()) {
                    PassengerThread1 servedPassenger = c0Deque.remove(0);
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;

                    if (!waitDeque.isEmpty()) {
                        c0Deque.add(waitDeque.remove(0));
                        c0Size.addAndGet(1);
                    }
                } else {
                    continue;
                }
            }
            if (currentThread().getName().equals("KioskClerk-1")) {
                if (!c1Deque.isEmpty()) {
                    c1Size.addAndGet(-1);
                    PassengerThread1 servedPassenger = c1Deque.remove(0);
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

    public void assignTicket(PassengerThread1 servedPassenger) {
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
