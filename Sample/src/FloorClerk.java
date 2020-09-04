public class FloorClerk implements Runnable{
	
	private String name;
	private static boolean waitForClose = false;
	public static long time = System.currentTimeMillis();
	
	
	public FloorClerk(int id){
		setName("FloorClerk-" + id);
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
	public void run(){
		Customer beingHelped;
		while(true){

			msg("waiting for Customers.");
			while(isLineEmpty() && !waitForClose){
				// Busy wait while there are no customers
			}

			if(waitForClose){	// No more customers to help
				break;
			}

			try{
				beingHelped = helpCustomer();
				msg("has helped " + beingHelped.getName());
			} catch(ArrayIndexOutOfBoundsException e){
				//continue
			}
		}
		msg("Will sleep until close");
		try{
			Thread.sleep(200000);
		} catch(InterruptedException e){
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


	private Customer helpCustomer(){
		synchronized(Customer.CustomerLine){
			return Customer.CustomerLine.remove(0);
		}
	}

	private boolean isLineEmpty(){
		synchronized(Customer.CustomerLine){
			return Customer.CustomerLine.size() == 0;
		}
	}

	public synchronized static void prepForClosing(){
		waitForClose = true;
	}

	public synchronized static boolean finishedWorking(){
		return waitForClose;
	}

}

