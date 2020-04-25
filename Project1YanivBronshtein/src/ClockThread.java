import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/** This class simulates the behavior of the System clock that is meant to control the behavior of the flight attendant
 *  and interrupt any blocked threads at the end of the program
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class ClockThread extends Thread {
    /** Specifies the current time in the thread */
    public static long time = System.currentTimeMillis();
    /** Specifies the total time given for the program to run in milliseconds */
    private static long totalTime;

    public static AtomicBoolean isBoardingTimeOver = new AtomicBoolean(false);
    public static AtomicBoolean isTimeToDisembarkPlane = new AtomicBoolean(false);
    /** Constructs the ClockThread
     * @param time total time given for this program in milliseconds */
    public ClockThread(long time) {
        setName("Clock-");
        totalTime = time;
    }

    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        // Sleep for 2.5 hours
        try {
            sleep(5*Main.THIRTY_MIN);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //Notify flight attendant that it is time to board
        if (Main.flightAttendant.isAlive()) {
            Main.flightAttendant.interrupt();
        }

        //Sleep for boarding time
        try {
            sleep(Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Time to interrupt late passengers */
        isBoardingTimeOver.set(true);
        //Sleep 2 hours until it is time for landing
        try {
            sleep(4*Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            interrupt();
        }

        /* Time to interrupt flight attendant for disembarking plane */
        isTimeToDisembarkPlane.set(true);
        Main.flightAttendant.interrupt();


        //todo: Add code to signal passengers terminating


        /* Terminate all passengers who have not been given a group id */
//        for (PassengerThread passenger : Main.passengers) {
//
//            passenger.interrupt();
//        }

        while (!isInterrupted()) {
            try {
                sleep(20);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
        msg("Clock terminating");


    }
}