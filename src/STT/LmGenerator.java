package src.STT;

import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;

public class LmGenerator{
    public LmGenerator() throws Exception {
        Scanner sc = new Scanner(new File("Resources/ChessCommand.dic"));
        // Scanner sc = new Scanner(new File("Resources/dict.dict"));
        FileWriter fw = new FileWriter("Resources/ChessCommand.lm");

        

        int lineCnt = 2;
        String prevWord = "";
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] words = line.split(" ");
            if(words[0].equals(prevWord) || words[0].length() == 0){continue;}
            prevWord = words[0];
            lineCnt++;
        }
        fw.write("\\data\\"+"\n");
        fw.write("ngram 1="+lineCnt+"\n");
        for(int i=0; i<7; i++)fw.write("\n");
        fw.write("\\1-grams:\n-1 </s> -0.3\n-1 <s> -0.25\n");
        sc.close();



        
        sc = new Scanner(new File("Resources/ChessCommand.dic"));
        prevWord = "";
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] words = line.split(" ");
            if(words[0].equals(prevWord) || words[0].length() == 0){continue;}
            fw.write("-1 ");

            fw.write(words[0]);
            fw.write(" -0.25\n");
            prevWord = words[0];
        }
        fw.write("\n\n\\end\\");
        fw.close();
        sc.close();
    }
}