public class PassengerThread1 extends Thread {

    private int seatNum;
    private int zoneNum;
    private int groupNum;
    //    public static int c0Size = KioskClerkThread.c0Deque.size();
//    public static int c1Size = KioskClerkThread.c1Deque.size();
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
    public PassengerThread1(int id) {
        setName("Passenger-" + (id + 1));
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("Arrived at airport");


        int c0Size = KioskClerkThread1.c0Size.get();
        int c1Size = KioskClerkThread1.c1Size.get();
        System.out.println("c0Size:" + c0Size + "c1Size:" + c1Size);
        if (c0Size < Main.counterNum && c1Size < Main.counterNum) {
            if (c0Size < c1Size) {
                msg("Added zero queue");
                KioskClerkThread1.c0Deque.add(this);
                KioskClerkThread1.c0Size.addAndGet(1);
                System.out.println("After add");
                System.out.println("c0Size:" + KioskClerkThread.c0Deque.size() +
                        "c1Size:" + KioskClerkThread.c1Deque.size());
            } else if (c0Size > c1Size) {
                msg("Added to one queue");
                KioskClerkThread1.c1Deque.add(this);
                KioskClerkThread1.c1Size.addAndGet(1);

                System.out.println("After add");
                System.out.println("c0Size:" + KioskClerkThread1.c0Deque.size() +
                        "c1Size:" + KioskClerkThread1.c1Deque.size());
            } else {
                int randNum = (int) Math.round( Math.random());

                if (randNum == 0) {
                    msg("Added to zero queue randomly");
                    KioskClerkThread1.c0Deque.add(this);
                    KioskClerkThread1.c0Size.addAndGet(1);


                } else {
                    msg("Added to one queue randomly");
                    KioskClerkThread1.c1Deque.add(this);
                    KioskClerkThread1.c1Size.addAndGet(1);

                }
                System.out.println("After add");
                System.out.println("c0Size:" + KioskClerkThread1.c0Deque.size() +
                        "c1Size:" + KioskClerkThread1.c1Deque.size());
            }

        } else {
            while (c0Size == Main.counterNum && c1Size == Main.counterNum) {
                try {
                    msg("Busy wait to approach counter");
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


