public class PassengerThread extends Thread {

    private int seatNum;
    private int zoneNum;
    private int groupNum;

    public int getTicketNum() {
        return seatNum;
    }

    public void setTicketNum(int ticketNum) {
        this.seatNum = ticketNum;
    }

    public int getZoneNum() {
        return zoneNum;
    }

    public void setZoneNum(int zoneNum) {
        this.zoneNum = zoneNum;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public static long time = System.currentTimeMillis();
    public PassengerThread(int id) {
        setName("Passenger-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }
//    @Override
//    public void run() {
//        try {
//            sleep((long) (Math.random() *1000));
//            msg("Arrived at airport");
////            while (!KioskClerkThread.isCounter0IsFree() && !KioskClerkThread.isCounter1IsFree()) {
////                KioskClerkThread.waitingDeque.add(this);
////                sleep(1000);
////            }
////
//            /* ******Critical Section ****/
//            if (KioskClerkThread.isCounter0IsFree()) {
//                msg("Entered first queue");
//                if (!KioskClerkThread.waitingDeque.contains(this))
//                    KioskClerkThread.counter0Deque.add(this);
//                else
//                    KioskClerkThread.counter0Deque.add(KioskClerkThread.waitingDeque.removeFirst());
//            }
//            else if (KioskClerkThread.isCounter1IsFree()){
//                msg("Entered second queue");
//                if (!KioskClerkThread.waitingDeque.contains(this))
//                    KioskClerkThread.counter1Deque.add(this);
//                else {
//                    KioskClerkThread.counter1Deque.add(KioskClerkThread.waitingDeque.removeFirst());
//                }
//            } else {
//                KioskClerkThread.waitingDeque.add(this);
//                while (true) {
//                    msg("In here yo");
//                    sleep(100);
//                }
//            }
//            //Have the customer sleep as soon as they have been put in the counter line
////            while (true) {
////                sleep(1000);
////            }
//
//            /* ****** EndCritical Section ****/
//
//        } catch (InterruptedException e) {
//        }
//    }



    @Override
    public void run() {
        try {
            sleep((long) (Math.random() *1000));
            msg("Arrived at airport");
//            while (!KioskClerkThread.isCounter0IsFree() && !KioskClerkThread.isCounter1IsFree()) {
//                KioskClerkThread.waitingDeque.add(this);
//                sleep(1000);
//            }
//
            /* ******Critical Section ****/
            if (KioskClerkThread.isCounter0IsFree()) {
                msg("Entered first queue");
                KioskClerkThread.counter0Deque.add(this);
            }
            else if (KioskClerkThread.isCounter1IsFree()) {
                msg("Entered second queue");
                KioskClerkThread.counter1Deque.add(this);
            }

//            } else {
//                while (!KioskClerkThread.isCounter0IsFree() && !KioskClerkThread.isCounter1IsFree()) {
//                    msg("Stuck in Passenger while loop");
//                    sleep(100);
//                }
//            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
