import java.util.LinkedList;
import java.util.Queue;

public class FlightAttendantThread extends Thread {

    public Queue<PassengerThread> boardingPlaneQueue = new LinkedList<>();
    public static long time = System.currentTimeMillis();


    public FlightAttendantThread(int id) {
        setName("FlightAttendant-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        if (time < Main.THIRTY_MINUTES) {

        }


        while(!KioskClerkThread.zone1Queue.isEmpty()) {
            boardingPlaneQueue.add(KioskClerkThread.zone1Queue.remove());
        }

        while(!KioskClerkThread.zone2Queue.isEmpty()) {
            boardingPlaneQueue.add(KioskClerkThread.zone2Queue.remove());
        }

        while(!KioskClerkThread.zone3Queue.isEmpty()) {
            boardingPlaneQueue.add(KioskClerkThread.zone3Queue.remove());
        }








    }
}
