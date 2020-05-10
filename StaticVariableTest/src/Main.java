public class Main {

    public static void main(String[] args) {
        OtherClass[] arr = new OtherClass[600];
        for (OtherClass obj : arr) {
            obj = new OtherClass(false);
            obj.specialMethod();
        }
//        OtherClass.specialMethod();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OtherClass.specialVariable = true;

    }
}
