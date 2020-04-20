public class PassengerThread extends Thread {


    public static long time = System.currentTimeMillis();
    public PassengerThread(int id) {
        setName("Passenger-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    public void run() {

        //1.sleep(rand) each pass. arrives at random time
        //2.Go straight to clerk to check in
        //Once received ticket, to simulate rushing, elevate priority by 1 priority+1
        //Use thread message "Customer has arrived
        //sleep(rand) WILL simulate at security line doing security shit
        //priority-1

        //sleep() passenger walking in door


    }
}