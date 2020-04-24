import java.util.concurrent.ThreadLocalRandom;
/** This class simulates the behavior of the System clock that is meant to control the behavior of the flight attendant
 *  and interrupt any blocked threads at the end of the program
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class ClockThread extends Thread {
    /** Specifies the current time in the thread */
    public static long time = System.currentTimeMillis();
    /** Specifies the total time given for the program to run in milliseconds */
    private static long totalTime;


    public ClockThread(long time) {
        setName("Clock-");
        totalTime = time;
    }

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
        Main.flightAttendant.interrupt();

        //Sleep for the duration of the flight up till the flight attend is to announce that
        // the plane is preparing for landing
        try {
            sleep(4*Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        //todo: Add code to signal passengers terminating





        msg("All passengers have disembarked. Clock terminating");


    }
}
