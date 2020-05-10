import java.util.Vector;
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

        //todo: Do this timed: Controlled by clock
        while (!Main.timeToCloseGate) {
            while (Main.zone1Queue.hasQueuedThreads()) {
                Main.zone1Queue.release();
            }
            while (Main.zone2Queue.hasQueuedThreads()) {
                Main.zone2Queue.release();
            }
            while (Main.zone3Queue.hasQueuedThreads()) {
                Main.zone3Queue.release();
            }
            try {
                sleep(Main.THIRTY_MIN/2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Main.isGateClosed = true;

        //todo: Tell Passengers to fuck off????
        //notify the clock-thread, hey its good to go, take off
        Main.gateClosed.release();
        //Announce to Passenger who are late
        msg("Gates to enter the plane has closed");

        try {
            Main.timeToLand.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passengersDisembark();
        msg("Flight Attendant terminating");
    }

    /** This method is used by the flight attendant to board passengers onto the plane
     * @param zoneQueue for zones 1,2 or 3
     * @param allowedTime long time allowed by the flight attendant for processing passengers in the zones
     * This method is called twice on the same queues with two different allowedTimes*/
    public void handleBoardingZone(Vector<PassengerThread> zoneQueue, long allowedTime) {
        try {
            Main.boardingPlaneQueue.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Empty out entire atDoorQueue */
        while (Main.boardingPlaneQueue.hasQueuedThreads()) {
            int i = Main.groupNum; //Counter for number of passengers in group Can be at most 3
            groupID.getAndIncrement(); //Assign group to the next set of passengers

        }
    }


    private void passengersDisembark() {

        int firstPassengerSeat = 1;
        while (!Main.inOrderExiting.get(firstPassengerSeat).hasQueuedThreads()) {
            firstPassengerSeat++;
            if (firstPassengerSeat > 30)
                break;
        }
        if(firstPassengerSeat <= 30)
        {
            Main.inOrderExiting.get(firstPassengerSeat).release();
        }

        Main.landing.release();
        msg("Passengers have disembarked. Cleaning Plane");

    }
}

