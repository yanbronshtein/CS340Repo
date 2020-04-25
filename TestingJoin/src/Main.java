import java.util.Vector;

public class Main {

    public static void main(String[] args) {

        Vector<OneThread> myVec = new Vector<>(3);
        for (int i = 0; i < 10; i++) {
            myVec.add(new OneThread(i));
        }

        for (OneThread thread: myVec) {
            thread.start();
        }

//        while (!myVec.isEmpty()) {
//            OneThread removedThread = myVec.remove(0);
//            if (removedThread.isAlive()) {
//                try {
//                    removedThread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        for (int i = 0; i < myVec.size() - 1; i++) {
            OneThread removedThread1 = myVec.remove(i);
            OneThread removedThread2 = myVec.remove(i+ 1);
            if (removedThread2.isAlive()) {
                try {
                    removedThread1.join();

                }catch (InterruptedException e) {

                }
            }
        }

    }
}
