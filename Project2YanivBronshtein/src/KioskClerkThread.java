import java.util.Vector;

/** This class simulates the behavior of the Kiosk Clerk in an airport
 * @author Yaniv Bronshtein
 * @version 2.0
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
            /* Put a wait on the mutex to check the number of passengers served */
            try {
                Main.mutexClerk.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* If the number of passengers served is equal to the variable numPassengers,
            * break out of the while loop and terminate(make sure to release the mutex first so the other
            * clerk thread has a chance to perform this check */
            if(numPassengersServed== Main.numPassengers) {
                Main.mutexClerk.release();
                break;
            }
            numPassengersServed++; //Increment the number of passengers served
            Main.mutexClerk.release(); //Release the mutex

            /* Wait for passengers to arrive at the kiosk clerk
            * Simulate serving passenger through a brief sleep */
            try {
                Main.passengersAtKiosk.acquire();
                msg("Currently Serving a Passenger");
                sleep(20);
                msg("Passenger is Served");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Main.clerksAvailable.release(); //Let the customer know that the clerk is available
        }

        msg("All passengers at counter " + id + " have been served. Thread Terminating");
    }


}
