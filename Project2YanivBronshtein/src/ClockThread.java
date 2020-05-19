
/** This class simulates the behavior of the System clock that is meant to control the behavior of the flight attendant
 *  and interrupt any blocked threads at the end of the program
 * @author Yaniv Bronshtein
 * @version 2.0*/
public class ClockThread extends Thread {
    /** Specifies the current time in the thread */
    public static long time = System.currentTimeMillis();

    /** Constructs the ClockThread
     *  */
    public ClockThread() {
        setName("Clock-");
    }

    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    /* Run method of the Clock thread */
    @Override
    public void run() {
        /* Sleep for 2.5 hours needs to notify flight attendant that it is time to start the
        boarding process */
        try {
            sleep(5*Main.THIRTY_MIN);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Main.timeToBoard.release();

        /*Sleep 2.5 hours( half an hour for boarding process and 2 hours until it is time for landing) */
        try {
            sleep(5*Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        Main.timeToLand.release(); //Used to wakeup flight attendant to begin landing procedure
        /* Wait for signal from flight attendant that they are done cleaning to terminate */
        try {
            Main.flightAttendantDoneCleaning.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("Clock terminating");

    }
}
