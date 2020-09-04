
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FindMagicWord {

    public static void Write5LettersWordsToFile(String competitorName, int numberOfWords) throws IOException {
        Random r = new Random();
        List<Character> l = Arrays.asList('a', 'b', 'c', 'd');
        String str;
        File file = new File(competitorName+ ".txt");
        FileWriter fw = new FileWriter(file);
        //generate file letter words
        for(int k=300;k<=600;k++)
       {
        for (int row = 0; row < numberOfWords; row++) {
            str = "";
            for (int i = 0; i < 5; i++) {
                char c = l.get(r.nextInt(l.size()));
                str = str + Character.toString(c);
            }
            fw.write(str + "\n");
        }
       }
        fw.close();
    }

    public static String generateMagicWord() {
        String magicWord = "";
        Random r = new Random();
        List<Character> l = Arrays.asList('a', 'b', 'c', 'd');
        for (int i = 0; i < 5; i++) {
            char c = l.get(r.nextInt(l.size()));
            magicWord = magicWord + Character.toString(c);
        }
        
        return magicWord;
    }
    
    public static boolean searchMagicWord(String competitorName,String magicWord ) throws FileNotFoundException, IOException
    {
        FileReader fr=new FileReader(competitorName+".txt");
        BufferedReader br=new BufferedReader(fr);
        int line=0;
        String word="";
        boolean magicWordFound=false;
		System.out.println(""+competitorName+" searches for the magic word");
        while((word=br.readLine())!=null)
        {
            if(word.equals(magicWord))
            {
                magicWordFound=true;
                break;
            }
            line++;
        }       
        br.close();
        fr.close();
        return magicWordFound;
    }
}
