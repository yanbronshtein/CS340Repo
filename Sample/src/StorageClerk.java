import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class StorageClerk implements Runnable{
	
	private String name;
	private int id;
	private static boolean waitForClose = false;
	public static long time = System.currentTimeMillis();
	private static List<Boolean> storageHelp = Collections.synchronizedList(new ArrayList<Boolean>());

	public StorageClerk(int id){
		this.id = id;
		setName("StorageClerk-" + id);
		storageHelp.add(false);
	}

	public int getStorageId(){
		return this.id;
	}
	
	
	public void setName(String name){
		this.name = name;
	}
	

	public String getName(){
		return this.name;
	}
	

	public void msg(String m){
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	
	@Override
	public void run() {
		Customer beingHelped;
		Random rand = new Random(System.currentTimeMillis());
		boolean assistedClerk = false;
		while(true){
			// Check if coworkers need help
			// Help first coworker, then sleep
			synchronized(storageHelp){
				for(int i = 0; i < storageHelp.size(); i++){
					if(storageHelp.get(i)){
						helpStorageClerk(i);
						msg(" assists StorageClerk-" + i);
						try {
							Thread.sleep(rand.nextInt(6000 - 3000) + 3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						assistedClerk = true;
						break;
					}
				}
			}

			if (assistedClerk){
				assistedClerk = false;
				continue;
			}

			while (Customer.storageLine.size() == 0 && !waitForClose) {
				// Busy wait
			}

			if(waitForClose){
				break;
			}

			try{
				beingHelped = getNextStorageCustomer();
			} catch(IndexOutOfBoundsException e){
				continue;
			}
			msg("will help " + beingHelped.getName());
			if(beingHelped.isTwoManJob){
				msg("needs assistance from another StorageClerk.");
				askForHelp();
				while (storageHelp.get(this.getStorageId())) {
					// Wait for help
				}
			}
			try {
				Thread.sleep(rand.nextInt(6000 - 3000) + 3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			beingHelped.helpedByStorage = true;
			msg("has helped " + beingHelped.getName());
		}

		msg("is waiting for BALA to close.");
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			msg("has been woken up.");
		}

		for(Thread cust: RunProject.customerArr){ //Wait for all customers to leave
			try {
				cust.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		msg("Clocks out");

	}


	private synchronized Customer getNextStorageCustomer(){
		Customer cust = Customer.storageLine.remove(0);
		return cust; 
	}


	private synchronized void askForHelp(){
		storageHelp.set(this.getStorageId(), true);
	}


	private synchronized void helpStorageClerk(int id){
		storageHelp.set(id, false);
	}

	public synchronized static void prepForClosing(){
		waitForClose = true;
	}

	public synchronized static boolean finishedWorking() {
		return waitForClose;
	}


}
