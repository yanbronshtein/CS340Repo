import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** This class simulates the behavior of the Flight Attendant in an airport
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class FlightAttendantThread extends Thread {
    /** Time recorded by each FlightAttendantThread upon creation */
    public static long time = System.currentTimeMillis();
    /** Variable used to count the number of passengers that passed security and were added to zone 1 queue */
    public static AtomicInteger atGateZ1Count = new AtomicInteger(0);
    /** Variable used to count the number of passengers that passed security and were added to zone 2 queue */
    public static AtomicInteger atGateZ2Count = new AtomicInteger(0);
    /** Variable used to count the number of passengers that passed security and were added to zone 3 queue */
    public static AtomicInteger atGateZ3Count = new AtomicInteger(0);
    /** Variable to hold the group ID assigned by the flight attendant to passengers boarding the plane */
    public static AtomicInteger groupID = new AtomicInteger(0);
    /** Variable is set to true by the flight attendant as soon as they have announced that the plane doors have closed  */
    public static AtomicBoolean hasFinishedBoarding = new AtomicBoolean(false);
    /** This vector is used to contain passengers in their groups waiting to board the plane */
    Vector<PassengerThread> atDoorQueue = new Vector<>();
    /** This vector is used to contain passengers that made it on the flight */
    public static Vector<PassengerThread> planeQueue = new Vector<>(Main.numPassengers / 3);
    /** This vector holds the passengers in zone 1 */
    public static Vector<PassengerThread> z1Queue = new Vector<>(Main.numPassengers /3);
    /** This vector holds the passengers in zone 2 */
    public static Vector<PassengerThread> z2Queue = new Vector<>(Main.numPassengers /3);
    /** This vector holds the passengers in zone 3 */
    public static Vector<PassengerThread> z3Queue = new Vector<>(Main.numPassengers /3);

    public static Vector<PassengerThread> onVacationQueue = new Vector<>();

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
        /* Flight Attendant busy waits until interrupted by the clock */
        while(!isInterrupted()){
            try{
                sleep(200);
            }catch(InterruptedException e){
                msg("Interrupted by clock to begin boarding process ");
                interrupt();
            }
        }
        //todo: DEAL WITH JOINS!!!!
//        try {
//            Main.clerks[0].join();
//        } catch (InterruptedException e) {
//        }
//        try {
//            Main.clerks[1].join();
//        } catch (InterruptedException e) {
//        }


        /*First boarding phase: The Flight Attendant spends a quarter of 30 minutes boarding each zone equally */
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/4);

        /* Second boarding phase: The Flight Attendant spends a 20th of 30 minutes to go through all the zones again*/
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/20);

        /*Flight Attendant has closed the doors */
        hasFinishedBoarding.set(true);
// TMP COMMENT:       msg("Doors of plane are closed. Please rebook your flight at this time");
        /* Flight attendant interrupts all passengers that have passed to security but missed boarding */
        sendLatePassengersHome();

        /* Flight attendant either wakes up on their own or most likely by the clock */
        try {
            sleep(4 * Main.THIRTY_MIN);
        } catch (InterruptedException e) {
// TMP COMMENT:           msg("All passengers aboard please ready yourself for landing");
            interrupt();
        }

        Vector<PassengerThread> disembarkPlaneQueue = new Vector<>();
        disembarkPlaneQueue.addAll(planeQueue);

        /* Sort the planeQueue by seat number for help with disembarking plane */
        disembarkPlaneQueue.sort((passenger1, passenger2) -> {
            if (passenger1.passengerInfo.get(2) < passenger2.passengerInfo.get(2)) {
                return -1;
            }
            else if (passenger1.passengerInfo.get(2) > passenger2.passengerInfo.get(2)) {
                return 1;
            }
            else {
                msg("Improper generation of unique tickets. Flight overbooked");
                return 0;
            }
        });

        passengersDisembark(disembarkPlaneQueue);

        msg("Flight Attendant terminating");
    }

    private void passengersDisembark(Vector<PassengerThread> disembarkPlaneQueue) {
        for (int i = 0; i < disembarkPlaneQueue.size(); i++) {
            PassengerThread p = disembarkPlaneQueue.remove(i);
            onVacationQueue.add(p);
            if (p.isAlive()) {
                try {
                    p.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        for (PassengerThread p: onVacationQueue) {
            p.interrupt();
        }
        msg("Passengers have disembarked. Cleaning Plane");

    }


    /** This method is used by the flight attendant to board passengers onto the plane
     * @param zoneQueue for zones 1,2 or 3
     * @param allowedTime long time allowed by the flight attendant for processing passengers in the zones
     * This method is called twice on the same queues with two different allowedTimes*/
    public void handleBoardingZone(Vector<PassengerThread> zoneQueue, long allowedTime) {
        long start = System.currentTimeMillis(); //Get current time
        long timeAllottedForCallingZone = start + allowedTime; //Calculate ending time

        /* Flight attendant removes passengers from their zone while the allotted time has no run out and the zoneQueue
         * is not empty and adds to the waiting at door queue   */
        while (System.currentTimeMillis() < timeAllottedForCallingZone && !zoneQueue.isEmpty()) {
            PassengerThread temp = zoneQueue.remove(0);
//            temp.interrupt();
            atDoorQueue.add(temp);
        }

        /* Empty out entire atDoorQueue */
        while (!atDoorQueue.isEmpty()) {
            int i = Main.groupNum; //Counter for number of passengers in group Can be at most 3
            groupID.getAndAdd(1); //Assign group to the next set of passengers

            /* Assign group id to passenger and put them in the boardingPlane queue and interrupt them to scan
            their ticket  */
            while (i > 0 && !atDoorQueue.isEmpty()) {
                PassengerThread boardingPassenger = atDoorQueue.remove(0);
                boardingPassenger.passengerInfo.set(3, groupID.get()); //Add groupID
                boardingPassenger.interrupt(); //Interrupt to have them scan their boarding pass
                planeQueue.add(boardingPassenger);
                msg("Passenger " + boardingPassenger.passengerInfo.get(0) + " has boarded the plane with zone " +
                        boardingPassenger.passengerInfo.get(1) + " seat " + boardingPassenger.passengerInfo.get(2) +
                        " group ID " + boardingPassenger.passengerInfo.get(3));
                i--;
            }
        }
    }

    /** This method goes through each of the queues removing any passengers and waking them up to go home and
     * rebook their tickets */
    public void sendLatePassengersHome() {
        while (!z1Queue.isEmpty()) {
            PassengerThread latePassenger = z1Queue.remove(0);
            latePassenger.interrupt();
        }

        while (!z2Queue.isEmpty()) {
            PassengerThread latePassenger = z2Queue.remove(0);
            latePassenger.interrupt();
        }

        while (!z3Queue.isEmpty()) {
            PassengerThread latePassenger = z3Queue.remove(0);
            latePassenger.interrupt();
        }
    }


}
