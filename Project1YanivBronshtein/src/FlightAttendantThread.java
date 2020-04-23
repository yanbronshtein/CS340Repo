import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


public class FlightAttendantThread extends Thread {

    public static AtomicInteger atGateZ1Count = new AtomicInteger(0);
    public static AtomicInteger atGateZ2Count = new AtomicInteger(0);
    public static AtomicInteger atGateZ3Count = new AtomicInteger(0);
    public static long THRESHOLD_TIME = 9*Main.THIRTY_MIN / 10; //Time after which we close the gates(3 min till departure)

    Vector<PassengerThread> atDoorQueue = new Vector<>();

    public static Vector<PassengerThread> boardingPlaneQueue = new Vector<>(Main.numPassengers / 3);
    //    public static Vector<PassengerThread> othersQueue = new Vector<>(Main.numPassengers);
//    public static Vector<PassengerThread> waitingAtGateQueue = new Vector<>(Main.numPassengers);
    public static Vector<PassengerThread> z1Queue = new Vector<>(Main.numPassengers /3);
    public static Vector<PassengerThread> z2Queue = new Vector<>(Main.numPassengers /3);
    public static Vector<PassengerThread> z3Queue = new Vector<>(Main.numPassengers /3);

    public static long time = System.currentTimeMillis();


    public FlightAttendantThread(int id) {
        setName("FlightAttendant-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                //todo: Figure out what to put here
            }
        }
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/4);
        handleBoardingZone(z1Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z2Queue, Main.THIRTY_MIN/20);
        handleBoardingZone(z3Queue, Main.THIRTY_MIN/20);
        msg("Doors of plane are closed. Please rebook your flight at this time");
    }



    public void handleBoardingZone(Vector<PassengerThread> queue, long allowedTime) {
        long start = System.currentTimeMillis(); //Get current time
        long timeForCallingZone = start + allowedTime; // 60 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < timeForCallingZone && !queue.isEmpty()) {
            PassengerThread temp = queue.remove(0);
            atDoorQueue.add(temp);
        }
        while (!atDoorQueue.isEmpty()) {
            int i = Main.counterNum;
            while (i >= 0 && !atDoorQueue.isEmpty()) {
                PassengerThread boardingPassenger = atDoorQueue.remove(0);
                boardingPassenger.interrupt(); //Interrupt to have them scan their ticket
                boardingPlaneQueue.add(boardingPassenger);
                i--;
            }
        }
    }
}

