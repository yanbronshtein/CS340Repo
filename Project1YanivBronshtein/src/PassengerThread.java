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
        setName("Passenger-" + (id + 1));
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }

    @Override
    public void run() {
        try {
            sleep((long) (Math.random() * 6 * Main.THIRTY_MIN));
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
            } else if (c0Size > c1Size) {
                msg("Added to one queue");
                KioskClerkThread.c1Deque.add(this);
            } else {
                int randNum = (int) Math.round( Math.random());

                if (randNum == 0) {
                    msg("Added to zero queue randomly");
                    KioskClerkThread.c0Deque.add(this);
                } else {
                    msg("Added to one queue randomly");
                    KioskClerkThread.c1Deque.add(this);
                }
            }
        } else {
            while (c0Size == Main.counterNum && c1Size == Main.counterNum) {
                try {
                    msg("Busy wait to approach counter");
                    sleep(Main.THIRTY_MIN/10);
                } catch (InterruptedException e) {
                    //todo: Figure out what to put here
                }
            }
        }
        goThroughSecurity();

    }

    private void goThroughSecurity() {
        msg("Rushing to security");
        int originalPriority = getPriority();
        int higherPriority = originalPriority + 1;
        setPriority(higherPriority);
        try {
            sleep((long) (Math.random() * Main.THIRTY_MIN));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setPriority(originalPriority);
        msg("Arrived At Gate and adding to proper zone queues");
        switch (this.getZoneNum()) {
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

        waitAtGate();

    }

    private void waitAtGate() {
        while (!isInterrupted()) {
            try {
                sleep(Main.THIRTY_MIN/10);
            }catch (InterruptedException e){
                //todo: figure out what to put here
            }
        }
        scanBoardingPass();

    }
    private void scanBoardingPass() {
        msg("Scanned boarding pass");
        PassengerThread.yield();
        PassengerThread.yield();
        sleepOnPlane();
    }

    private void sleepOnPlane() {
        try {
            sleep(4 * Main.THIRTY_MIN);
        } catch (InterruptedException e) {
            //todo: Figure out what to put here
        }
    }



}


