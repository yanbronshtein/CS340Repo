
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

 class Competitor extends Thread {

    int restTime, activeThreadCount;
    static int points, counter, mcount, threadCount, threadCount1;
    HashMap hmmountain = new HashMap();
    HashMap hmarrivalTime = new HashMap();
    HashMap hmcriticalThinking = new HashMap();
    static  HashMap hmWinnerResult = new HashMap();
    static HashMap sortedhmWinnerResult=new HashMap();
	static HashMap sortedMountainResult=new HashMap();
	
	static HashMap forestFinishTime = new HashMap();
	static HashMap mountainFinishTime = new HashMap();
	static HashMap riverFinishTime = new HashMap();
	static HashMap CriticalThinkingFinishTime = new HashMap();
    int mountainPoints[] = {8, 6, 3};
     Semaphore sema = new Semaphore(1);
      static boolean arrived=true;
    public void mutualExclusion() {
         
        
        //get randomly generated restTime using static method randInt of Competitor class by passing min and max time in argument 
       
        try {
            //competitor will start to find in the forest a map that contains a magic word.

            enterForest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //competitors sets back its priority to the norm value of 5 after finish forest
        this.setPriority(5);
		restTime = randInt(40, 60);
        try {
            //rest time between forest and mountain
            Thread.sleep(restTime);
        } catch (Exception ae) {
            ae.printStackTrace();
        }
        try{
        //Thread will wait on acquire() until Thread inside critical section release permit by calling release() on semaphore.       
        sema.acquire();
        System.out.println(Thread.currentThread().getName() + " inside mutual exclusive region of enterMountains");
        enterMountains();        
        }
       catch (Exception ie) {
            ie.printStackTrace();
        } finally {
            
            sema.release();
            System.out.println(Thread.currentThread().getName() + " outside of mutual exclusive region of enterMountains");
        }
        restTime = randInt(40, 60);

        try {
            //RestTime between mountains and river:
            Thread.sleep(restTime);
        } catch (Exception ae) {
            ae.printStackTrace();
        }
        try {
            enterRiver();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    } 
 
    public static int randInt(int min, int max) {

        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void enterForest() throws IOException {
        long foreststartTime = System.currentTimeMillis();
        System.out.println("["+foreststartTime+" ms]" + Thread.currentThread().getName() + " enters the Forest");
        //get count of random number to generate random words having 5 letters to write into file using static method randInt of Competitor class by passing min and max time in argument 
        //get number of words to write into file for competitor 
        int numberOfWords = Competitor.randInt(300, 600);
        //Write 5 letters words in file by passing two arguments.one is competitor name and number of 5 letters words to be write into file.
        //file name would be the name of the competitor while creating Thread in main method
        FindMagicWord.Write5LettersWordsToFile(Thread.currentThread().getName(), numberOfWords);
        //get the magicWord(map) to be find from file(forest)
        String magicWord = FindMagicWord.generateMagicWord();
        //search magicWord(map) from file(forest)
        boolean magicWordFound = FindMagicWord.searchMagicWord(Thread.currentThread().getName(), magicWord);

        if (magicWordFound) {
            System.out.println(Thread.currentThread().getName() + " Magic word Found");
        } else {//if magic word(map) not found in file then yield that competitor   
            //yielded will get the chance for execution is decided by the thread scheduler whose behavior is vendor dependent.
            this.yield();
            System.out.println(Thread.currentThread().getName() + " Magic word Not Found ");
        }

		long forestEndTime = System.currentTimeMillis();
		long forestTime=forestEndTime-foreststartTime;
		forestFinishTime.put(Thread.currentThread().getName(),forestTime);
		
    }

    private  void enterMountains() {
        long mountainstartTime = System.currentTimeMillis();
       
        System.out.println("["+mountainstartTime+" ms]"  + Thread.currentThread().getName() + " enters the Mountains");
        
        int randomsleeptime = Competitor.randInt(1000, 3000);
        try {
            Thread.sleep(randomsleeptime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //The competitor with the shortest traversal time will receive 8 points, the second will receive 6 points, and the third will receive 3 points.
        //The last ones will get no points.
        if (mcount < 3) {
            //store mountain points in hashmap
            hmmountain.put(Thread.currentThread().getName(), mountainPoints[mcount++]);
        } else {
            hmmountain.put(Thread.currentThread().getName(), "0");
        }
		
	long mountainEndTime = System.currentTimeMillis();
	long mountainTime=mountainEndTime-mountainstartTime;
		mountainFinishTime.put(Thread.currentThread().getName(),mountainTime);
        		 
        //sort the result
        sortedMountainResult = sortByValues(mountainFinishTime);		
    }

    public void enterRiver() throws InterruptedException {
	
        System.out.println("Waiting for other threads to come to start for river ");
        //get Active thread count
        activeThreadCount = Thread.activeCount() - 1;
        threadCount++;
        //put thread name in map with his arrival time
        long riverstartTime = System.currentTimeMillis();
        hmarrivalTime.put(Thread.currentThread().getName(), riverstartTime);
        
        
        
        //The competitors will start crossing the river all at the same time so wait for them to come
        while (true) {
            if (threadCount < activeThreadCount) {
                continue;
            } else {
			
            break;
				
            }
        }
        System.out.println("["+riverstartTime+" ms]"  + Thread.currentThread().getName() + " starts crossing the river");
        long randomtime = Competitor.randInt(1000, 3000);
        try {
            
            //Have them sleep for a relatively long random time.
            Thread.sleep(randomtime);

        } catch (Exception e) {
            e.printStackTrace();
        }
		long riverEndTime = System.currentTimeMillis();
		long riverTime = riverEndTime-((long) hmarrivalTime.get(Thread.currentThread().getName()));
		riverFinishTime.put(Thread.currentThread().getName(),riverTime);		
        hmWinnerResult.put(Thread.currentThread().getName(), Main.age());
        //sort the result
        sortedhmWinnerResult = sortByValues(hmWinnerResult);	
		
    }

    private static synchronized HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
