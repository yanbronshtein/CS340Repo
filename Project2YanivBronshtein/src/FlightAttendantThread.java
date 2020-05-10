import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/** This class simulates the behavior of the Flight Attendant in an airport
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class FlightAttendantThread extends Thread {
    /** Time recorded by each FlightAttendantThread upon creation */
    public static long time = System.currentTimeMillis();
    /** Variable to hold the group ID assigned by the flight attendant to passengers boarding the plane */
    //todo: Figure out if I really need a group ID here that is atomic!!!
    public static AtomicInteger groupID = new AtomicInteger(0);

    /** This vector is used to contain passengers that made it on the flight */
    public static Vector<PassengerThread> planeQueue = new Vector<>(Main.numPassengers / 3);

    /** bloooooop */
    /** Constructs a FlightAttendantThread and sets its name */
    public FlightAttendantThread() {
        setName("FlightAttendant-");
    }

    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    /** This method is executed when the thread is started in the main method in the Main class
     * and simulates the behavior of the Flight Attendant including boarding closing the door, announcing to
     * passengers that the plane is landing and cleaning up after the passengers*/
    @Override
    public void run() {
        /* Wait for clock to signal boarding time */
        try {
            Main.timeToBoard.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int calledPassengersAtDoor = 0;

        //todo: Do this timed: Controlled by clock
        while (!Main.timeToCloseGate) {
            handleBoarding(Main.zone1Queue);
            handleBoarding(Main.zone2Queue);
            handleBoarding(Main.zone3Queue);
        }

        Main.isGateClosed = true;
        msg("The plane door has closed. All remaining passengers please rebook your flights");

//        //notify the clock-thread, hey its good to go, take off
//        Main.gateClosed.release();
        //Announce to Passenger who are late

        /* Sleep on plane and wait to be woken up by the clock for the landing procedure */
        try {
            Main.timeToLand.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("All passengers aboard please prepare for landing");

        passengersDisembark();
        msg("Passengers have left plane. Cleaning");
        try {
            sleep(Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        msg("Done cleaning. Flight attendant terminating");
        Main.flightAttendantDoneCleaning.release();

    }

    private static void handleBoarding(Semaphore sem) {

        while (sem.hasQueuedThreads()) {
            sem.release(); //Release passenger to call them to the door
            //todo: Should flight attendant use the same mutex as the passenger?
            try {
                Main.mutexPassenger.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Main.boardingPassengerCount % 4 == 0 || !sem.hasQueuedThreads()) {
                Main.mutexPassenger.release();
                int i = 4;
                /* Deque exactly 4 passengers or whatever is left */
                while (i > 0 && !sem.hasQueuedThreads()) {
                    Main.boardingPlaneQueue.release();
                    i--;
                }
            }else {
                Main.mutexPassenger.release();
            }
        }
    }


    private void passengersDisembark() {
        while (!Main.inOrderExiting.isEmpty()) {
            Main.inOrderExiting.pollFirstEntry().getValue().release();
        }

    }
}

