
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Main {
    
    public static long startTime = System.currentTimeMillis();
	

    public static long age() {
        return (System.currentTimeMillis() - startTime);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
	
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Wizard Creates the Forest\n\n");
        System.out.println("Enter Number of Competitors:");
        int competitors=Integer.parseInt(br.readLine());
         final Competitor Comptr = new Competitor();		
		System.out.println("Wizard creates the forest");		
         for(int i=1;i<=competitors;i++)
        {
            //start new thread 
     new Thread("Competitor-"+i){
            @Override
            public void run(){
              Comptr.mutualExclusion(); 
            }
        }.start();
        }
        
        //wait other thread to finish their execution to get result        
        try
		{
		Thread.sleep(50000);
		System.out.println("All Competitors are now at home");
	
		System.out.println("\n\nWizard Generated the First report");
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Report 1: Time taken by each Competitor to finish Entire Race");
		System.out.println("-----------------------------------------------------------------------\n");
		for(int i=1;i<=competitors;i++)
        {
		System.out.println("" + Competitor.sortedhmWinnerResult.keySet().toArray()[i-1] + " has finished Entire Race (ms):" + Competitor.sortedhmWinnerResult.get(Competitor.sortedhmWinnerResult.keySet().toArray()[i-1]));
		}
		System.out.println("\n"+Competitor.sortedMountainResult.keySet().toArray()[competitors-1]+" came last "+Competitor.sortedMountainResult.get(Competitor.sortedMountainResult.keySet().toArray()[competitors-1])+" (ms) and signaled Wizard to release group of Compititors to start crossing river");	
		System.out.println("\n\nWizard Generated the second report");
		System.out.println("------------------------------------------------------------------------------------------------------");
		System.out.println("Report 2:Time taken by each competitor to pass each of the obstacles (Excluding Rest Time)");
		System.out.println("------------------------------------------------------------------------------------------------------\n");	
		System.out.println("Forest Result:");
		for(int i=1;i<=competitors;i++)
        {
		System.out.println("" + Competitor.forestFinishTime.keySet().toArray()[i-1] + " has finished forest (ms): " + Competitor.forestFinishTime.get(Competitor.forestFinishTime.keySet().toArray()[i-1]));
		}
		System.out.println("\nMountain Result:");
		for(int i=1;i<=competitors;i++)
        {
		System.out.println("" + Competitor.mountainFinishTime.keySet().toArray()[i-1] + " has finished Mountain (ms): " + Competitor.mountainFinishTime.get(Competitor.mountainFinishTime.keySet().toArray()[i-1]));
		}
		System.out.println("\nRiver crossing Result:");
		for(int i=1;i<=competitors;i++)
        {
		System.out.println("" + Competitor.riverFinishTime.keySet().toArray()[i-1] + " has finished river (ms): " + Competitor.riverFinishTime.get(Competitor.riverFinishTime.keySet().toArray()[i-1]));
		}
		System.out.println("\n\n-----------------------------------------------------------------------");
		System.out.println("Wizard Announced the Winner of the Competion : ");
		System.out.println("-----------------------------------------------------------------------");
        System.out.println("\n" + Competitor.sortedhmWinnerResult.keySet().toArray()[0] + " is Winner of the competation having finish time (ms):" + Competitor.sortedhmWinnerResult.get(Competitor.sortedhmWinnerResult.keySet().toArray()[0]));
        System.out.println(""+Competitor.sortedhmWinnerResult.keySet().toArray()[0]+" received 1000 Gold coins...!!!");
		}
		catch (Exception ae) {
            ae.printStackTrace();
        }
		        	        
          }
}
