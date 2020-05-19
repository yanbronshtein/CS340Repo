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


        callPassengersByZone(Main.zone1Queue);
        callPassengersByZone(Main.zone2Queue);
        callPassengersByZone(Main.zone3Queue);


        Main.isGateClosed = true;
        while(Main.zone1Queue.hasQueuedThreads())
        {
            Main.zone1Queue.release();
        }
        while(Main.zone2Queue.hasQueuedThreads())
        {
            Main.zone2Queue.release();
        }
        while(Main.zone3Queue.hasQueuedThreads())
        {
            Main.zone3Queue.release();
        }
        msg("The plane door has closed. All remaining passengers please rebook your flights");

        /* Sleep on plane and wait to be woken up by the clock for the landing procedure */
        try {
            Main.timeToLand.acquire();
            sleep(4000);
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

    private static void callPassengersByZone(Semaphore zoneQueue) {

        while(zoneQueue.hasQueuedThreads())
            zoneQueue.release();

        try{
            sleep(4000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }


        while (Main.boardingPlaneQueue.hasQueuedThreads()) {
            int boardingQueueLength = Main.boardingPlaneQueue.getQueueLength();
            if (boardingQueueLength % Main.groupNum == 0 || boardingQueueLength < Main.groupNum) {
                int counter = (boardingQueueLength % Main.groupNum == 0) ? Main.groupNum : boardingQueueLength;

                for (int i = 0; i < counter; i++)
                    Main.boardingPlaneQueue.release();
            }
        }
    }

    private void passengersDisembark() {
        while (!Main.inOrderExiting.isEmpty()) {
            Map.Entry<Integer, Semaphore> entry = Main.inOrderExiting.pollFirstEntry();
//            msg("Passenger in seat" + entry.getKey() + "has departed the plane");
            try {
                sleep(200);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            entry.getValue().release();
        }
        /*for(Integer i=1; i<=30; i++)
        {
            if(Main.inOrderExiting.containsKey(i))
            {
                Main.inOrderExiting.get(i).release();
            }
        }*/

    }
}

