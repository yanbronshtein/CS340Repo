import java.util.ArrayList;
import java.util.Collections;

public class TestTickets {
    public static void main(String[] args) {
        ArrayList<Integer> myList = generateRandomNumbers();
        System.out.println("Hi");
    }

    private static ArrayList<Integer> generateRandomNumbers() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= Main.numPassengers; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        Collections.shuffle(list);

        return list;
    }
}
