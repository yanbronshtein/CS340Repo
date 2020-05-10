public class OtherClass {

    public static boolean specialVariable = false;
    public boolean instanceVar;
    public OtherClass(boolean instanceVar) {
        this.instanceVar = instanceVar;
    }
    public void specialMethod() {
        if (!specialVariable) {
            do {
                System.out.println("Hello World");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!specialVariable);
        }
    }
}
