public class ClockThread extends Thread {


    public static long time = System.currentTimeMillis();
    public ClockThread(int id) {
        setName("Clock-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        // BabyGeese do stuff here
    }
}
