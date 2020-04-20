public class KioskClerkThread extends Thread {


    public static long time = System.currentTimeMillis();
    public KioskClerkThread(int id) {
        setName("KioskClerk-" + id);
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + getName() + ":" + m);
    }



    @Override
    public void run() {
        //Once customer comes, announce ticket and give to customer
        //Anounce: Seat+Zone+Customer_Name
        //Have to have some way to know if all passengers have received their ticket
        //terminate
    }
}
