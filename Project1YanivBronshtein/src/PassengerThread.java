import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PassengerThread extends Thread {


    public static long time = System.currentTimeMillis();
    public static int seatNum = 0;
    public static int zoneNum = 0;
    public static int groupNum = 0;

    public PassengerThread(int num) {
        setName("Passenger-" + (num+1));
    }
    /** Passenger line at check-in counter */
    public static Queue<PassengerThread> passengersAtCheckInLine = new LinkedList<>();


    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    public void run() {

        arriveAtAirport();




        //1.sleep(rand) each pass. arrives at random time
        //2.Go straight to clerk to check in
        //Once received ticket, to simulate rushing, elevate priority by 1 priority+1
        //Use thread message "Customer has arrived
        //sleep(rand) WILL simulate at security line doing security shit
        //priority-1

        //sleep() passenger walking in door


    }


    public void arriveAtAirport() {
        /* Customer arrives between 3 and 5 hours before the flight */
        try {
            sleep(ThreadLocalRandom.current().nextLong(6 * Main.THIRTY_MINUTES, 10 * Main.THIRTY_MINUTES));
            msg("Passenger Arrives at the airport and goes to check-in counter");

        } catch (InterruptedException e) {
            msg("Interrupted!");
        }

        //Busy wait if the number of passengers on a line is 3
         while (KioskClerkThread.counter1Deque.size() ==3 && KioskClerkThread.counter2Deque.size() == 3) {
             //No-op
         }

         printBoardingPass();
    }

    private void printBoardingPass() {
        goToSecurityLine();

    }

    private void goToSecurityLine() {
        msg("Rushing to security line");
        //Simulate rushing by using getPriority() and setPriority()
        //Increase priority of passenger
        //After sleep of random time, set priority back to default val


        /*Go to gate */
        //Simulate taking seat and waiting for flight attendant using BW

    }




}