import java.util.Date;
import java.util.TimerTask;

public class BoardPassengersTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Task performed on: " + new Date() + "n"  + "Thread's name:" + Thread.currentThread().getName());

    }
}
