import java.lang.Thread;
import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Customer implements Runnable {
	
	private String name;
	private int myStorageNumber;
	private int id;
	public volatile boolean helpedByStorage = false;
	public volatile boolean isTwoManJob = false;

	private static List<Thread> customerThreadList = Collections.synchronizedList(new ArrayList<Thread>());
	private static int numCustomers = 0;
	private static int storageNumber = 0;
	private static int doneWithFloor = 0;
	private static int doneWithStorage = 0;
	public static long time = System.currentTimeMillis();
	public static Vector<Customer> CustomerLine = new Vector<Customer>();
	public static List<Customer> storageLine = Collections.synchronizedList(new ArrayList<Customer>());


	public Customer(int id) {
		this.id = id;
		setName("Customer-" + id);
		customerThreadList.add((new Thread(this)));
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public int getCustomerId(){
		return this.id;
	}
	
	
	public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	
	@Override
	public void run() {
		Random rand = new Random();
		msg("has arrived to BALA.");
		enterStore();

		msg("is browsing the store.");
		try {
			Thread.sleep(rand.nextInt(7000-4000)+4000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}

		msg("goes on line for a FloorClerk.");
		CustomerLine.add(this);
		while(CustomerLine.contains(this)){
			//Busy Wait until Customer is helped
		}
		doneWithFloor();
		
		if(allDoneWithFloor()){
			FloorClerk.prepForClosing();
		}

		msg("is rushing to a cashier.");
		int currPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(currPriority + 1);

		try {
			Thread.sleep(rand.nextInt(6000 - 3000) + 3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("has paid the cashier.");

		if(isItemHeavy()){
			// Take break
			msg("buys a heavy item. Takes a break.");
			Thread.yield();
			Thread.yield();
			try{
				Thread.sleep(rand.nextInt(6000 - 3000) + 3000);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			takeNumber();
			msg("in storage room. Takes number " + myStorageNumber);

			VeryHeavyOrNot();
			while(!helpedByStorage){
				// Busy Wait
			}
			doneWithStorage();
			msg("Leaves the storage room.");

			if(allDoneWithStorage()){
				msg("has alerted StorageClerks to sleep");
				StorageClerk.prepForClosing();
			}
		}
		else{
			msg("bought a light item.");
		}

		msg("Looks for the exit");

		try {
			Thread.sleep(rand.nextInt(8000 - 4000) + 4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(getCustomerId() == 0){
			while(!StorageClerk.finishedWorking() || !FloorClerk.finishedWorking()){
				//Busy Wait
			}

			for(Thread fc: RunProject.floorClerkArr){	//Wake up FloorClerks
				fc.interrupt();
			}
			for (Thread sc : RunProject.storageClerkArr) {	//Wake up FloorClerks
				sc.interrupt();
			}
		}
		else{
			waitForPartner();
		}
		msg("Leaves BALA store.");
	}	

	/* Generate a random number between 1 to 10.
	If greater than or equal to 6, return true, else
	return false.
	*/
	private boolean isItemHeavy(){
		Random rand = new Random(System.currentTimeMillis());
		int num = rand.nextInt(10);
		if(num >= 6){
			return true;
		} else{
			return false;
		}
	}

	// If item is heavy, set twoManJob flag to true
	private void VeryHeavyOrNot() {
		Random rand = new Random(System.currentTimeMillis());
		int num = rand.nextInt(10);
		if (num >= 8) {
			msg("has very heavy item.");
			this.isTwoManJob = true;
		}
	}

	//Assign customer in storage room a number
	//Increase storageNumber in a mutually exclusive way
	private synchronized void takeNumber(){
		myStorageNumber = storageNumber;
		storageLine.add(this);
		storageNumber += 1;
	}


	private synchronized void enterStore(){
		numCustomers += 1;
	}


	// Increment the number of Customers who have been helped
	// by StorageClerks
	private synchronized void doneWithStorage(){
		doneWithStorage += 1;
	}

	// Increment the number of Customers who have been helped
	// by FloorClerks
	private synchronized void doneWithFloor(){
		doneWithFloor += 1;
	}

	//Check if all customers have been helped by FloorClerks
	private synchronized boolean allDoneWithFloor(){
		return doneWithFloor == numCustomers;
	}

	//Check if all customers have been helped by StorageClerks
	private synchronized boolean allDoneWithStorage(){
		return doneWithStorage == storageNumber;
	}

	//Customer N waits for Customer N-1 to finish
	private void waitForPartner(){
		if(RunProject.customerArr[getCustomerId()-1].isAlive()){
			try{
				RunProject.customerArr[getCustomerId() - 1].join();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}


}
