public class PassengerThread extends Thread {

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
    public PassengerThread(int id) {
        setName("Passenger-" + (id + 1));
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    @Override
    public void run() {
        try {
            sleep((long) (Math.random() *1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("Arrived at airport");
        getBoardingPass();

    }



    private void getBoardingPass() {
        int c0Size;
        int c1Size;
        synchronized (KioskClerkThread.c0Deque) {
            c0Size = KioskClerkThread.c0Deque.size();
        }
        synchronized (KioskClerkThread.c1Deque) {
            c1Size = KioskClerkThread.c1Deque.size();
        }
        System.out.println("c0Size: " + c0Size + "c1Size: " + c1Size);
        if (c0Size < Main.counterNum && c1Size < Main.counterNum) {
            if (c0Size < c1Size) {
                msg("Added zero queue");
                KioskClerkThread.c0Deque.add(this);
//                KioskClerkThread.c0Size.addAndGet(1);
            } else if (c0Size > c1Size) {
                msg("Added to one queue");
                KioskClerkThread.c1Deque.add(this);
//                KioskClerkThread.c1Size.addAndGet(1);

            } else {
                int randNum = (int) Math.round( Math.random());

                if (randNum == 0) {
                    msg("Added to zero queue randomly");
                    KioskClerkThread.c0Deque.add(this);
//                    KioskClerkThread.c0Size.addAndGet(1);
                } else {
                    msg("Added to one queue randomly");
                    KioskClerkThread.c1Deque.add(this);
//                    KioskClerkThread.c1Size.addAndGet(1);
                }
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
        goThroughSecurity();

    }


    private void goThroughSecurity() {
        msg("Rushing to security");
        setPriority(getPriority() + 1);
        try {
            sleep((long) (Math.random() * 3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setPriority(getPriority() -1);
        msg("Arrived At Gate");
        while (true) {
            try {
                sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


