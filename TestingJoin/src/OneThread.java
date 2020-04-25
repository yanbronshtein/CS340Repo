import java.util.Vector;

public class OneThread extends Thread {
    public OneThread(int id) {
        setName("OneThread-" + id);
    }
    public void run()
    {
        System.out.println(getName());
    }

} 