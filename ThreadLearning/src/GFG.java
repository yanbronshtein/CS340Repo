// Java Program to demonstrates 
// the compareAndSet() function 

import java.util.concurrent.atomic.AtomicBoolean;

public class GFG {
    public static void main(String[] args)
    {

        // Initially value as false 
        AtomicBoolean val = new AtomicBoolean(false);

        // Prints the updated value 
        System.out.println("Previous value: "
                + val);

        // Checks if previous value was false 
        // and then updates it 
        boolean res = val.compareAndSet(false, true);

        // Checks if the value was updated. 
        if (res)
            System.out.println("The value was"
                    + " updated and it is "
                    + val);
        else
            System.out.println("The value was "
                    + "not updated");
    }
} 