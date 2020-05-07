import java.util.Vector;

/** This class simulates the behavior of the passenger thread
 * @author Yaniv Bronshtein
 * @version 1.0*/
public class PassengerThread extends Thread {
    /** This vector contains the four identifying pieces of information for a passenger:
     * passengerInfo.get(0): passenger id upon thread creation
     * passengerInfo.get(1): zone number on boarding pass
     * passengerInfo.get(2): seat number on boarding pass
     * passengerInfo.get(3): group id when boarding the plane
     * */
    public final Vector<Integer> passengerInfo = new Vector<>(3);
    /** Time in thread upon creation */
    public static long time = System.currentTimeMillis();

    /** Constructor creates thread with unique name and id  */
    public PassengerThread(int num) {
        int id = num + 1;
        setName("Passenger-" + id);
        passengerInfo.add(0, id); //id of passenger
        passengerInfo.add(1, -1);
        passengerInfo.add(2, -1);
        passengerInfo.add(3, -1);

    }

    /** This method is used to display messages by the thread onto the console including the current
     * time and thread name followed by the message
     * @param m message by the thread */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    /** This method is executed by the Passenger Thread as soon as it is started */
    @Override
    public void run() {
        /* Each passenger arrives approximately three hours before the flight */
        try {
            sleep((long) (Math.random() * Main.THIRTY_MIN));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("Arrived at airport");
        /* Passenger goes to the kiosk to print their pass */
        getBoardingPassAtKiosk();



    }


    /** This method simulates the process of the passenger getting on line at one of the two counters to get their
     * boarding pass */
    private void getBoardingPassAtKiosk() {
        Main.



    }








    /** This method simulates the passenger waiting at the gate to be processed by the flight attendant
     * If the flight attendant has set the hasFinishedBoarding flag to true, and the passenger
     * has exited the busy wait for that reason, then they were late and the thread should terminate naturally*/
    private void waitAtGate() {
        while (!isInterrupted()) {
            try {
                sleep(Main.THIRTY_MIN/10);
            }catch (InterruptedException e){
// TMP COMMENT:               msg("Called by flight attend to scan boarding pass");
                interrupt();
            }
        }
        /* After getting interrupted by the flight attendant the passenger goes to scan their boarding pass
         * immediately prior to boarding the plane */
        scanBoardingPass();

//        if (passengerInfo.get(3) == -1) {
//            msg("Passenger " + passengerInfo.get(0) + " has boarded the plane with zone " +
//                    passengerInfo.get(1) + " seat " + passengerInfo.get(2) +
//                    " group ID " + passengerInfo.get(3));
//        }
    }



    /** This method simulates the passenger sleeping on the plane for two hours until
     * being woken by the flight attendant to signal preparation for landing */
    private void sleepOnPlane() {
        try {
            sleep(5 * Main.THIRTY_MIN);
        } catch (InterruptedException e) {
//  TMP COMMENT:          msg("Woken up by flight attendant for landing procedure");
            interrupt();
        }

        waitToDepartPlane();
    }

    private void waitToDepartPlane() {
        while (!isInterrupted()) {
            try {
                sleep(Main.THIRTY_MIN/10);
            } catch (InterruptedException e) {
                msg("Leaving plane");
                interrupt();
            }
        }
    }




}


