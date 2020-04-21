import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KioskClerkThread extends Thread {

    public volatile int numPassengersOnLine;
    public volatile int numPassengersServed;
    public static Deque<PassengerThread> counter1Deque = new LinkedList<>();
    public static Deque<PassengerThread> counter2Deque = new LinkedList<>();
    public static Deque<PassengerThread> zone1Queue = new LinkedList<>();
    public static Deque<PassengerThread> zone2Queue = new LinkedList<>();
    public static Deque<PassengerThread> zone3Queue = new LinkedList<>();
    private String lineNumStr;

    private static ArrayList<Integer> randomNums;
//    public static int getNumPassengersOnLine() {
//        return numPassengersOnLine;
//    }
//
//    public static void setNumPassengersOnLine(int numPassengersOnLine) {
//        KioskClerkThread.numPassengersOnLine = numPassengersOnLine;
//    }

    public static long time = System.currentTimeMillis();
    public KioskClerkThread(int id) {

        setName("KioskClerk-" + id);
        numPassengersOnLine = 0;
        numPassengersServed = 0;

    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        //Once customer comes, announce ticket and give to customer
        //Anounce: Seat+Zone+Customer_Name
        //Have to have some way to know if all passengers have received their ticket
        //terminate
        msg("Waiting for passengers");
        randomNums = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            randomNums.add(i);
        }
        Collections.shuffle(randomNums);
        assignTickets();
        msg("Done for the day");



    }

    private void assignTickets() {
        msg("Assigning tickets");
        //Generate a number between 1 and 30 with a corresponding zone#
        int ticketNum = randomNums.get(0);
        randomNums.remove(0);


        //1-10: Zone1 (Add to zone1 queue)
        //11-20: Zone2 (Add to zone2 queue)
        //21-30: Zone3 (Add to zone3 queue)

        //when all passengers got their tickets, check-in clerks are done for the day(terminate)




    }

    private void generateRandom(ArrayList<Integer> randomNums) {

    }








}
