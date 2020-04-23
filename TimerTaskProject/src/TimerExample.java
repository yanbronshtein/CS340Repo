import java.util.Timer;

public class TimerExample {
    public static void main(String[] args) {
        Timer myTimer = new Timer("Boarding Timer");
        long delay = 1000;
        myTimer.schedule(new BoardPassengersTask(), delay);

    }

}
