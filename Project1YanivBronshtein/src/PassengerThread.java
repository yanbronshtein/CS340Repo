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
//    final int id;
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
            sleep((long) (Math.random() * 6 * Main.THIRTY_MIN));
        } catch (InterruptedException e) {
        //todo: Figure out what to put here
        }
        msg("Arrived at airport");
        /* Passenger goes to the kiosk to print their pass */
        getBoardingPassAtKiosk();

    }


    /** This method simulates the process of the passenger getting on line at one of the two counters to get their
     * boarding pass */
    private void getBoardingPassAtKiosk() {
        /* Number of passengers at the first Kiosk Clerk counter */
        int c1Size;
        /* Number of passengers at the second Kiosk Clerk counter */
        int c2Size;

        /* Get the current number of passenger at the first counter the Kiosk Clerk  */
        synchronized (KioskClerkThread.c1Queue) {
            c1Size = KioskClerkThread.c1Queue.size();
        }
        /* Get the current number of passenger at the second counter the Kiosk Clerk  */
        synchronized (KioskClerkThread.c2Queue) {
            c2Size = KioskClerkThread.c2Queue.size();
        }

        /* If the number of passengers at both counters is below the threshold add new passengers as follows:
        * If the size of the line at counter 1 is less than the size of the line at counter 2, add to counter 1
        * If the size of the line at counter 1 is greater than that of counter 2, add to counter 2
        * If both lines have the same number of passengers, randomly pick a line to place the passenger*/
        if (c1Size < Main.counterNum && c2Size < Main.counterNum) {
            if (c1Size < c2Size) {
                msg("Added to first queue");
                KioskClerkThread.c1Queue.add(this);
            } else if (c1Size > c2Size) {
                msg("Added to second queue");
                KioskClerkThread.c2Queue.add(this);
            } else {
                int randNum = (int) Math.round( Math.random());

                if (randNum == 0) {
                    msg("Added to first queue randomly");
                    KioskClerkThread.c1Queue.add(this);
                } else {
                    msg("Added to second queue randomly");
                    KioskClerkThread.c2Queue.add(this);
                }
            }
        }
        /* If the second counter is full and the first is not, add the passenger to the first counter */
        else if (c1Size < Main.counterNum && c2Size == Main.counterNum) {
            msg("Adding to first queue because second queue is full ");
            KioskClerkThread.c1Queue.add(this);
        }
        /* If the first counter is full and the second is not, add the passenger to the second counter */
        else if (c1Size == Main.counterNum && c2Size < Main.counterNum) {
            msg("Adding to second queue because first queue is full ");
            KioskClerkThread.c2Queue.add(this);
        }
        /* If both counters are at max capacity, passenger enters busy loop  */
        else {
            while (c1Size == Main.counterNum && c2Size == Main.counterNum) {
                try {
                    msg("Busy wait to approach counter");
                    sleep(Main.THIRTY_MIN/10);
                } catch (InterruptedException e) {
                    //todo: Figure out what to put here
                }
            }
        }
        /* After having received the boarding pass, the passenger rushes to security */
        goThroughSecurity();

    }

    /** This method simulates the passenger going through security by simulating rushing as soon as
     * they receive their boarding pass
     */
    private void goThroughSecurity() {
        msg("Rushing to security");
        /* Increase the default priority of the passenger and have them sleep for a random amount of time
        * to simulate rushing and then restore to default priority */
        setPriority(getPriority() + 1);
        try {
            sleep((long) (Math.random() * Main.THIRTY_MIN));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setPriority(getPriority() - 1);

        msg("Left security and added to proper zone queue");
        /* Passenger enters is placed in one of 3 zone queues based on their passenger info  */
        switch (passengerInfo.get(1)) {
            case 1:
                FlightAttendantThread.z1Queue.add(this);
                FlightAttendantThread.atGateZ1Count.getAndAdd(1);
                break;
            case 2:
                FlightAttendantThread.z2Queue.add(this);
                FlightAttendantThread.atGateZ2Count.getAndAdd(1);
                break;
            default:
                FlightAttendantThread.z3Queue.add(this);
                FlightAttendantThread.atGateZ3Count.getAndAdd(1);
                break;
        }


        /* The passenger will now enter a busy wait at the gate */
        waitAtGate();

    }

    /** This method simulates the passenger waiting at the gate to be processed by the flight attendant
     * If the flight attendant has set the hasFinishedBoarding flag to true, and the passenger
     * has exited the busy wait for that reason, then they were late and the thread should terminate naturally*/
    private void waitAtGate() {
//        while (!FlightAttendantThread.hasFinishedBoarding.get() && passengerInfo.get(3) == -1) {
//        while (!FlightAttendantThread.hasFinishedBoarding.get() && (passengerInfo.get(3) == -1)) {
        while (!isInterrupted()) {

//            msg("I am in while loop in waitAtGateMethod() method");
            try {
                sleep(Main.THIRTY_MIN/4);
            }catch (InterruptedException e){
                msg("Called by flight attend to scan boarding pass");
                interrupt();
            }
        }

        scanBoardingPass();

        if (passengerInfo.get(3) == -1) {
            msg("Passenger " + passengerInfo.get(0) + " has boarded the plane with zone " +
                    passengerInfo.get(1) + " seat " + passengerInfo.get(2) +
                    " group ID " + passengerInfo.get(3));
        }

//        msg("I am in waitAtGateMethod() method");


        /* After getting interrupted by the flight attendant the passenger goes to scan their boarding pass
        * immediately prior to boarding the plane */

    }

    /** This method simulates the passenger scanning their boarding pass */
    private void scanBoardingPass() {
        /* Simulate scanning boarding pass by doing yield() twice */
        PassengerThread.yield();
        PassengerThread.yield();
        msg("Scanned boarding pass and boarded plane in group " + passengerInfo.get(3));


        /* Passenger sleeps during flight */
        sleepOnPlane();
    }

    /** This method simulates the passenger sleeping on the plane for two hours until
     * being woken by the flight attendant to signal preparation for landing */
    private void sleepOnPlane() {
        try {
            sleep(4 * Main.THIRTY_MIN);
        } catch (InterruptedException e) {
            //todo: Figure out what to put here
        }
        msg("I am in sleepOnPlane() method");
    }



}


