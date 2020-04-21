import java.io.IOException;
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
//implementations of the Competitors. 
public class Competitor extends Thread {

    int restTime, activeThreadCount;
    static int points, counter, mcount, threadCount, threadCount1;
    HashMap hmforest = new HashMap();
    HashMap hmmountain = new HashMap();
    HashMap hmarrivalTime = new HashMap();
    HashMap hmcriticalThinking = new HashMap();
    HashMap<String, Long> hmRiverCrossingTime = new HashMap<String, Long>();
    static  HashMap hmWinnerResult = new HashMap();
    static HashMap sortedhmWinnerResult=new HashMap();
    int mountainPoints[] = {8, 6, 3};

    public Competitor(String s, int competitors) {
        super(s);
        this.points = competitors;
        Boolean[] readyArray = new Boolean[competitors];
        Arrays.fill(readyArray, Boolean.TRUE);
        start();
    }

    public Competitor() {
    }

    public void run() {
        //gets randomly generated restTime using static method randInt from Competitor class by passing min and max time in the argument. 
        restTime = randInt(40, 60);
        try {
            //competitor will start to find a map that contains a word.
            int point = enterForest();
            hmforest.put(getName(), point);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //competitors set back its priority to the norm value of 5 after finishing the forest
        this.setPriority(5);
        try {
            //rest time between forest and mountain
            Thread.sleep(restTime);
        } catch (Exception ae) {
            ae.printStackTrace();
        }

        enterMountains();
        restTime = randInt(40, 60);

        try {
            //rest time between mountains and river:
            Thread.sleep(restTime);
        } catch (Exception ae) {
            ae.printStackTrace();
        }
        try {
            enterRiver();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        restTime = randInt(40, 60);
        try {
            //rest time after finishing the river
            Thread.sleep(restTime);
        } catch (Exception ae) {
            ae.printStackTrace();
        }

        try {
            criticalThinking();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static int randInt(int min, int max) {

        Random rand = new Random();
        //nextInt is normally exclusive of the top value,
        //add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public int enterForest() throws IOException {
        int point = 0;
        System.out.println(" " + getName() + " has entered the forest");
        //gets count of random number to generate random words having 5 letters to write into file using the static method randInt of Competitor class by passing min and max time in the argument 
        //gets number of words to write into the file for competitor 
        int numberOfWords = Competitor.randInt(300, 600);
  
        //gets the magicWord(map) to be found from file(forest)
        String magicWord = FindMagicWord.generateMagicWord();
        //searches magicWord(map) from file(forest)
        boolean magicWordFound = FindMagicWord.searchMagicWord("forest", magicWord);

        if (magicWordFound) {
            System.out.println(getName() + " magic word found");
            point = points--;
        } else {//if magic word(map) not found in file then yield that competitor.   
            //yield will get the chance for execution, decided by the thread scheduler whose behavior is vendor dependent.
            this.yield();
            point = 0;
            System.out.println(getName() + " magic word not found ");
        }

        System.out.println(" " + getName() + " has finished the forest with finish time of (ms): " + Main.age());
        return point;
    }

    public synchronized void enterMountains() {
        System.out.println(" " + getName() + " has entered into the mountains");
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
            hmmountain.put(getName(), mountainPoints[mcount++]);
        } else {
            hmmountain.put(getName(), "0");
        }
        System.out.println(" " + getName() + " has finished the mountain with finish time of (ms): " + Main.age());
    }

    public void enterRiver() throws InterruptedException {

        System.out.println("waiting for other threads to start the river ");
        //get active thread count
        activeThreadCount = Thread.activeCount() - 1;
        threadCount++;
        //put thread name in map with his arrival time
        hmarrivalTime.put(getName(), Main.age());
        //The competitors will start crossing the river all at the same time so wait for them to come
        while (true) {
            if (threadCount < activeThreadCount) {
                continue;
            } else {
                break;
            }
        }

        long arrivalTime = (Long) hmarrivalTime.get(getName());
        long randomtime = Competitor.randInt(1000, 3000);
        try {
            //store river crossing time into Hashmap with competitor name as a key
            hmRiverCrossingTime.put(getName(), new Long((arrivalTime + randomtime)));
            //sleeps for a relatively long time.
            Thread.sleep(randomtime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        threadCount1++;

        while (true) {
            if (threadCount1 < activeThreadCount) {
                continue;
            } else {
                break;
            }
        }

        System.out.println(" " + getName() + " has finished the river and with finish time of (ms): " + Main.age());
    }

    public synchronized void criticalThinking() {
        int result = 0;
        //The wizard will ask each competitor three questions. For each question three integers will be generated between 1 and 9.
        int n1 = Competitor.randInt(1, 9);
        int n2 = Competitor.randInt(1, 9);
        int n3 = Competitor.randInt(1, 9);
        //If the generated number is between 1 and 3, it means that the competitor gave a wrong answer and he will receive 0 points.
        //If the number is between 4 and 6 then a partial answer was given and 2 points are awarded, if the number is between 7 and 9, 
        //the answer is correct and the competitor received 3 points for the answer.
        if (n1 >= 1 && n1 <= 3 || n2 >= 1 && n2 <= 3 || n3 >= 1 && n3 <= 3) {
            result = 0;
        }
        if (n1 >= 4 && n1 <= 6 || n2 >= 4 && n2 <= 6 || n3 >= 4 && n3 <= 6) {
            result = result + 2;
        }
        if (n1 >= 7 && n1 <= 9 || n2 >= 7 && n2 <= 9 || n3 >= 7 && n3 <= 9) {
            result = result + 3;
        }
        hmcriticalThinking.put(getName(), new Long(result));
        hmWinnerResult.put(getName(), Main.age());
        //sorts the result
        sortedhmWinnerResult = sortByValues(hmWinnerResult);
        
        System.out.println(" " + getName() + " has finished critical thinking with finish time of (ms): " + Main.age());
        System.out.println("" + getName() + " has taken time to finish entire race (ms):" + Main.age());

    }

    private static synchronized HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        //defines custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        //copying the sorted list in HashMap
        //using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
