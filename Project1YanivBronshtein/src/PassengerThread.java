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
        /* Each passenger arrives approximately three hours before the flight */
        try {
            sleep((long) (Math.random() * 6 * Main.THIRTY_MIN));
        } catch (InterruptedException e) {
        //todo: Figure out what to put here
        }
        msg("Arrived at airport");
        getBoardingPassAtKiosk();

    }



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

        /* If the number of passengers at both counters is below the threshold */
        if (c1Size < Main.counterNum && c2Size < Main.counterNum) {
            if (c1Size < c2Size) {
                msg("Added zero queue");
                KioskClerkThread.c1Queue.add(this);
            } else if (c1Size > c2Size) {
                msg("Added to one queue");
                KioskClerkThread.c2Queue.add(this);
            } else {
                int randNum = (int) Math.round( Math.random());

                if (randNum == 0) {
                    msg("Added to zero queue randomly");
                    KioskClerkThread.c1Queue.add(this);
                } else {
                    msg("Added to one queue randomly");
                    KioskClerkThread.c2Queue.add(this);
                }
            }
        } else {
            while (c1Size == Main.counterNum && c2Size == Main.counterNum) {
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


