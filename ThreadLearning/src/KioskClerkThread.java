import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

public class KioskClerkThread extends Thread {
    public static long time = System.currentTimeMillis();
    //    private static final int maxAllowedOnLine = 3;
//    public static boolean[] countersAvailable = {true,true}; //Initially both counters are free
    public static Deque<PassengerThread> counter0Deque = new LinkedList<>();
    public static Deque<PassengerThread> counter1Deque = new LinkedList<>();
    public static Deque<PassengerThread> waitingDeque = new LinkedList<>();
    public static int totalNumberPassengersServed;
    private static int id;
    private static ArrayList<Integer> randomNumbersList = new ArrayList<>();
//    public static boolean counter1IsFree;
//    public static boolean counter2IsFree;

    public static boolean isCounter0IsFree() {
        return counter0Deque.size() < 3;
    }

    public static boolean isCounter1IsFree() {
        return counter1Deque.size() < 3;
    }

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
//            while (counter0Deque.isEmpty() && counter1Deque.isEmpty()) {
//
//                try {
//                    msg("Sleeping because both counter queues are empty");
//                    sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            if (currentThread().getName().equals("KioskClerk-0")) {
                if (!counter0Deque.isEmpty()) {
                    PassengerThread servedPassenger = counter0Deque.removeFirst();
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;

                    if (!waitingDeque.isEmpty()) {
                        counter0Deque.add(waitingDeque.removeFirst());
                    }
                } else {
                    continue;
                }
            }
            if (currentThread().getName().equals("KioskClerk-1")) {
                if (!counter1Deque.isEmpty()) {
                    PassengerThread servedPassenger = counter1Deque.removeFirst();
                    assignTicket(servedPassenger);
                    totalNumberPassengersServed++;
                    if (!waitingDeque.isEmpty()) {
                        counter1Deque.add(waitingDeque.removeFirst());
                    }
                } else {
                    continue;
                }
            }
        }


    }

    public void assignTicket(PassengerThread servedPassenger) {
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
