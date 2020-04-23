import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ToDo {
    Timer timer;
    public static long THIRTY_MINUTES = 1000;

    public ToDo() {
        timer = new Timer();

        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Let's do shit bitch");
                timer.cancel();
            }
        };
        timer.schedule(myTask,new Date(), THIRTY_MINUTES/4 );
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            ToDo myTodo = new ToDo();

        }
    }
}
