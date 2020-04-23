import java.util.concurrent.ThreadLocalRandom;

public class ClockThread extends Thread {


    public static long time = System.currentTimeMillis();
    private long totalTime;
    public ClockThread(long totalTime) {
        setName("Clock-");
        this.totalTime = totalTime;
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        // Sleep for 2.5 hours
        try {
            sleep(5*Main.THIRTY_MIN);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Notify flight attendant that it is time to board
        Main.flightAttendant.interrupt();

        //Sleep for the duration of the flight up till the flight attend is to announce that
        // the plane is preparing for landing
        try {
            sleep(4*Main.THIRTY_MIN);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        //todo: Add code to signal passengers terminating





        msg("All passengers have disembarked. Clock terminating");


    }
}
