import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static boolean isLate = false;

    /** Constructor creates thread with unique name and id  */
    public PassengerThread(int num) {
        int id = num + 1;
        setName("Passenger-" + id);
        passengerInfo.add(0, id); //id of passenger
        passengerInfo.add(1, -1); //zonenum
        passengerInfo.add(2, -1); //seatnum
        passengerInfo.add(3, -1); //groupnum

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
        Main.customers.release();
        try {
            Main.clerksAvailable.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Main.mutexPassenger.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* Chose a number from the front of the previously populated randomNumbersVec and remove it.
        Assign a zone based on the seat number range  */
        int seatNum = Main.randomNumbers.remove(0);
        Main.mutexPassenger.release();
        int zoneNum;
        if (seatNum >= 0 && seatNum <= 10) {
            zoneNum = 1;
        }
        else if (seatNum >= 11 && seatNum <= 20) {
            zoneNum = 2;
        }
        else {
            zoneNum = 3;
        }
        passengerInfo.set(1,zoneNum);
        passengerInfo.set(2,seatNum);
        msg("seat number is: " + seatNum);

        if(!isLate) {
            if (seatNum >= 0 && seatNum <= 10) {
                zoneNum = 1;
                try {
                    Main.zone1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (seatNum >= 11 && seatNum <= 20) {
                zoneNum = 2;
                try {
                    Main.zone2.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                zoneNum = 3;
                try {
                    Main.zone3.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            msg("is in seat: " + seatNum + " and zone: " + zoneNum);
            sleepOnPlane();
        }
        else
            msg("Couldn't get on the plane");
    }


    /** This method simulates the passenger sleeping on the plane for two hours until
     * being woken by the flight attendant to signal preparation for landing */
    private void sleepOnPlane() {
//        try {
//            Main.landing.acquire();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //todo: Might need to add mutex here if all goes to shit
//        AtomicInteger seatNum = new AtomicInteger(passengerInfo.get(2));
        int seatNum = passengerInfo.get(2);
        try {
            Main.inOrderExiting.get(seatNum).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (seatNum != 30) {
            int nextSeat = seatNum + 1;
            while (!Main.inOrderExiting.get(nextSeat).hasQueuedThreads()) {
                nextSeat++;
                if (nextSeat > 30)
                    break;
            }
            if (nextSeat <= 30)
                Main.inOrderExiting.get(nextSeat).release();
        }
        msg("Exited the plane");

    }

}


