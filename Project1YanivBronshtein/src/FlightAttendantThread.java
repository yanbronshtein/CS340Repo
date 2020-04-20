public class FlightAttendantThread extends Thread {


    public static long time = System.currentTimeMillis();
    public FlightAttendantThread(int id) {
        setName("FlightAttendant-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        //if <= T-30min, start calling out names
        //else if line.isempty() BW
        //else BW

        //Bw until customers come (Flight attendant will have line)
        //if  busywait. Else serve



    }
}
