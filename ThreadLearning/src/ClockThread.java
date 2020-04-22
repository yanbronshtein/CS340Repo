public class ClockThread extends Thread {


    public static long time = System.currentTimeMillis();
    private int totalTime;
    public ClockThread(int totalTime) {
        setName("Clock-");
        this.totalTime = totalTime;
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        try {
//            sleep(flightTime);
//            for(int i=0; i<Main.cashier.length;i ++)
//            {
//                if(Main.clerks[i].isAlive())
//                    Main.clerks[i].interrupt();
//            }
//
//            for(int i=0; i<Main..length; i++)
//            {
//                if(Main.customer[i].isAlive())
//                    Main.customer[i].interrupt();
//            }
//            for(int i=0; i<Main.floorClerk.length; i++)
//            {
//                if(Main.floorClerk[i].isAlive())
//                    Main.floorClerk[i].interrupt();
//            }

            if (!Main.flightAttendant.isInterrupted()) {
                Main.flightAttendant.interrupt();
            }
        } catch (InterruptedException e) {
            msg("Gates are closed. Plane is departing");
        }


    }
}
