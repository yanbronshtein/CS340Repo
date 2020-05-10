import java.util.Vector;
import java.util.concurrent.Semaphore;

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
    public Semaphore canLeavePlane = new Semaphore(0, true);
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

        Main.passengersAtKiosk.release(); //Let clerk know that ready to receive ticket
        /* Get on line with the clerk */
        try {
            Main.clerksAvailable.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* Choose ticket from the pool */
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

        /* The passenger goes to wait in their zone queue semaphore  */
        if(!Main.isGateClosed) {
            if (seatNum >= 0 && seatNum <= 10) {
                zoneNum = 1;
                try {
                    Main.zone1Queue.acquire();
                    /* Once they have been called by the flight attendant, they have  */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if (seatNum >= 11 && seatNum <= 20) {
                zoneNum = 2;
                try {
                    Main.zone2Queue.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                zoneNum = 3;
                try {
                    Main.zone3Queue.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            msg("is in seat: " + seatNum + " and zone: " + zoneNum);
//            sleepOnPlane();
            /* Waiting to get in plane by group */


            /* Acquire mutex to increment passenger counter */
            try {
                Main.mutexPassenger.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.boardingPassengerCount++;
            Main.mutexPassenger.release();

            /* Wait to leave with the group */
            try {
                Main.boardingPlaneQueue.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Now that they have been released from the boardingPlane Queue, they can be put into the treemap for exiting */
            Main.inOrderExiting.put(seatNum, this.canLeavePlane);
            /* Waiting to leave */
            try {
                Main.passengerCanLeave.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* If they have been released they can enjoy their vacation */


            try {
                this.canLeavePlane.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
            msg("Couldn't get on the plane");
    }






    private void sleepOnPlane() {

    }

}


