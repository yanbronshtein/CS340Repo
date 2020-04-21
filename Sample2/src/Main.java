import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//Edward Bradin - CS 340 Project 1
//implements full execution of the program, using methods defined in the FindMagicWord and Competitor classes
//number of competitors must be entered by user in the console upon being asked
public class Main {
    
    public static long startTime = System.currentTimeMillis();

    public static long age() {
        return (System.currentTimeMillis() - startTime);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        int i;
        System.out.println("Wizard Creates the Forest\n\n");
              //writes 5 letter words in file by passing two arguments. One is competitor name and number of 5 letters words to be write into file.
        //file name would be the name of the competitor while creating Thread in the main method.
        FindMagicWord.Write5LettersWordsToFile("forest", 5);
        System.out.println("Enter Number of Competitors:");
        int competitors=Integer.parseInt(br.readLine());
        
        Competitor Comptr[] = new Competitor[competitors];
        for (i= 0; i < competitors; i++) {
           //creates Competitor(Threads) as given by user input and start competition.
            Comptr[i] = new Competitor("Competitor-"+(i+1),competitors);
            //gets current priority of thread.
            int currentPriotiry=Comptr[i].getPriority();
            //get randomly generated priority using static method randInt of Competitor class by passing min and max priority in the argument.
            int priority=Competitor.randInt(0, 4);
            //the competitor will set its priority, increasing its current priority by a randomly generated integer between 0 - 4.
            Comptr[i].setPriority(currentPriotiry+priority);
            
        }
        //Waits for other thread to finish execution to get results.
        
        try
        {
            Thread.sleep(10000);
        }
        catch(Exception ae){}
        
        for(i=0;i<competitors;i++)
        {
            System.out.println("Competitor-"+(i+1)+" alive status after finishing all rounds :"+Comptr[i].isAlive());
        }
        //Get the lowest turn around time from the sorted map.
        System.out.println("all Competitors are now at home");
        System.out.println("" + Competitor.sortedhmWinnerResult.keySet().toArray()[0] + " is the winner of the competition, with a finish time of (ms):" + Competitor.sortedhmWinnerResult.get(Competitor.sortedhmWinnerResult.keySet().toArray()[0]));
        System.out.println(""+Competitor.sortedhmWinnerResult.keySet().toArray()[0]+" received 1000 Gold coins...!!!");
      

    }
}
