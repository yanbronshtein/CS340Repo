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
    /** This vector is used to contain passengers boarding plane */
    public static Vector<PassengerThread> boardingPlaneQueue = new Vector<>(Main.numPassengers / 3);
    /** This vector holds the passengers in zone 1 */
    public static Vector<PassengerThread> z1Queue = new Vector<>(Main.numPassengers /3);
    /** This vector holds the passengers in zone 2 */
    public static Vector<PassengerThread> z2Queue = new Vector<>(Main.numPassengers /3);
    /** This vector holds the passengers in zone 3 */
    public static Vector<PassengerThread> z3Queue = new Vector<>(Main.numPassengers /3);





    public FlightAttendantThread() {
        setName("FlightAttendant-");
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    @Override
    public void run() {
        msg("Started run method ");
//        while (!ClockThread.isBoardingTime.get()) {
//            try {
//                sleep(200);
//            } catch (InterruptedException e) {
//                msg("Interrupted by the clock. Time for boarding");
//            }
//        }


        while(!isInterrupted()){
            try{
                sleep(200);
            }catch(InterruptedException e){
                interrupt(); // propagate interrupt
            }
        }
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/20);

        hasFinishedBoarding.set(true);
        msg("Doors of plane are closed. Please rebook your flight at this time");

    }



    public void handleBoardingZone(Vector<PassengerThread> zoneQueue, long allowedTime) {

        msg("Size of zoneQueue" + zoneQueue.size());
        long start = System.currentTimeMillis(); //Get current time
        long timeAllottedForCallingZone = start + allowedTime; // 60 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < timeAllottedForCallingZone && !zoneQueue.isEmpty()) {
            PassengerThread temp = zoneQueue.remove(0);
            temp.interrupt();
            atDoorQueue.add(temp);
        }
        while (!atDoorQueue.isEmpty()) {
            int i = Main.groupNum;
            groupID.getAndAdd(1);
            while (i > 0 && !atDoorQueue.isEmpty()) {
                PassengerThread boardingPassenger = atDoorQueue.remove(0);
                boardingPassenger.passengerInfo.set(3, groupID.get()); //Add group
                boardingPassenger.interrupt(); //Interrupt to have them scan their boarding pass
                boardingPlaneQueue.add(boardingPassenger);
                msg("Passenger " + boardingPassenger.passengerInfo.get(0) + " has boarded the plane with zone " +
                        boardingPassenger.passengerInfo.get(1) + " seat " + boardingPassenger.passengerInfo.get(2) +
                        " group ID " + boardingPassenger.passengerInfo.get(3));
                i--;
            }
        }
    }
}

