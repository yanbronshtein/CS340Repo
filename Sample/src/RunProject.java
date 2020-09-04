public class RunProject {
	
	public static Thread[] floorClerkArr;
	public static Thread[] storageClerkArr;
	public static Thread[] customerArr;


	public static void main(String[] args){
		
		int nCustomer = 18;
		int nFloorClerk= 2;
		int nStorageClerk= 4;
		
		switch(args.length) {
			case 1: nCustomer = Integer.parseInt(args[0]);
					break;
					
			case 2: nCustomer = Integer.parseInt(args[0]);
					nFloorClerk=  Integer.parseInt(args[1]);
					break;

			case 3: nCustomer = Integer.parseInt(args[0]);
					nFloorClerk=  Integer.parseInt(args[1]);
					nStorageClerk= Integer.parseInt(args[2]);
					break;
		}

		// initialize time

		floorClerkArr = new Thread[nFloorClerk];
		for(int i=0; i < nFloorClerk; i++){
			floorClerkArr[i] = (new Thread(new FloorClerk(i)));
			floorClerkArr[i].start();
		}

		storageClerkArr = new Thread[nStorageClerk];
		for(int j=0; j < nStorageClerk; j++){
			storageClerkArr[j] = (new Thread(new StorageClerk(j)));
			storageClerkArr[j].start();
		}

		customerArr = new Thread[nCustomer];
		for(int k = nCustomer-1; k >= 0; k--){
			customerArr[k] = (new Thread(new Customer(k)));
			customerArr[k].start();
		}
	}
}
