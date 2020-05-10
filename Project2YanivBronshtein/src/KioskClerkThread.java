import java.util.Vector;

/** This class simulates the behavior of the Kiosk Clerk in an airport
 * @author Yaniv Bronshtein
 * @version 1.0
 * */
public class KioskClerkThread extends Thread {
    /** Time in thread at creation */
    public static long time = System.currentTimeMillis();

    public static int numPassengersServed = 0;
    /* Used by KioskClerk to access passengers served counter */
    /** ID of KioskClerkThread */
    final int id;


    /** Constructor creates a KioskClerkThread. the name of the thread is initialized as well the totalNumPassengersServed.
     * The generateRandomNumbers() function is called to generateRandomNumbers for boarding pass seat numbers
     * @param num ID -1*/
    public KioskClerkThread(int num) {
        id = num + 1;
        setName("KioskClerk-" + id);
    }





    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    /** This method is executed when the thread is started in the main method in the Main class
     * Here the Kiosk Clerks deque passengers from their counters and assign them boarding passes  */
    @Override
    public void run() {
        while(true) {
            try {
                Main.mutexClerk.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(numPassengersServed== Main.numPassengers) {
                Main.mutexClerk.release();
                break;
            }
            numPassengersServed++;
            Main.mutexClerk.release();


            try {
                Main.passengersAtKiosk.acquire();
                msg("Currently Serving a Passenger");
                sleep(5000); //todo:Sleep inside the clerk???
                msg("Passenger is Served");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Main.clerksAvailable.release();
        }




        msg("All passengers at counter " + id + " have been served. Thread Terminating");
    }


}
