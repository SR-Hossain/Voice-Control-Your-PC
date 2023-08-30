package src.Gui;
import src.STT.*;
import javax.swing.JLayeredPane;
import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


import java.util.ArrayList;
import java.util.logging.Logger;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;


public class MainMenu extends JLayeredPane {
    Chess chess = new Chess();
    public JLabel setJlabel( JLabel label, int pos, String str, int posX, int posY) {
        label = new JLabel(new ImageIcon(str));
        label.setBounds( posX, posY, label.getPreferredSize().width, label.getPreferredSize().height );
        
        label.setOpaque( false );
        setLayer( label, 50 );

        add( label );

        return label;
    }
    public MainMenu() {
        Thread stt = new Thread(new SpToTxt());
        stt.start();
        setSize( 2000, 2000 );
        setJlabel(null, 0, "Resources/Chess/Background.png", 0, 0);
        getButton("New Game", 100, 300);
        getButton("Exit", 100, 500);

        setVisible(true);
    }
    public JButton getButton(String buttonStr, int x, int y){
        JButton b = new JButton(buttonStr);
        b.setOpaque(false);
        b.setVisible(true);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.ITALIC, 60));
        b.setBounds(x,y, 600, 200);
        b.addMouseListener(new ButtonClicker());
        add(b);
        setLayer(b, 100);
        return b;
    }

    public class ButtonClicker implements MouseListener{

        public void mouseClicked(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            String name = b.getText();
            if(name.equals("New Game")){
                startNewGame();
            }
            else{
                System.exit(0);
            }

            
        }

        public void mousePressed(MouseEvent e) { } 
        public void mouseReleased(MouseEvent e) { } 
        public void mouseEntered(MouseEvent e) { } 
        public void mouseExited(MouseEvent e) { }

    }
    public void startNewGame(){
        chess.newGame();
        chess.setMainMenu(this);
        try{
        getRootPane().setContentPane(chess);
        }catch(Exception e){System.out.println(chess + " Trouble "+e);}
    }


































































    class SpToTxt implements Runnable {
        ArrayList<String> list = new ArrayList<String>();


        public void run() {
            turnOffLogComments();
            sttWork();

        }

        public void sttWork(){
            try {
                // turnOffLogComments();
                startRec(getConfig());
            } catch (Exception e) {
                System.out.println("ami ke " + e);
                sttWork();
            }

        }
        
        public void turnOffLogComments(){
            Logger cmRootLogger = Logger.getLogger("default.config");
            cmRootLogger.setLevel(java.util.logging.Level.OFF);
            String conFile = System.getProperty("java.util.logging.config.file");
            try {
                System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
            } catch(Exception e){}
        }
        public Configuration getConfig(){
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("Resources/ChessCommand.dic");
            configuration.setLanguageModelPath("Resources/ChessCommand.lm");
            return configuration;
        }
            boolean commandSelectSpokenJustNow = false;
        boolean finalCommandToSelect = false;
        boolean commandMoveToSpokenJustNow = false;
        String prev = "";

        public void startRec(Configuration config) throws Exception {
            SpeechResult result;
            new LmGenerator();

            LiveSpeechRecognizer rec = new LiveSpeechRecognizer(config);
            rec.startRecognition(true);

            // String twoLine = "";
            // int count = 0;
            System.out.println("Talk.......");

            while ((result = rec.getResult()) != null) {
                String line = result.getHypothesis();
                if (line.length() == 0)
                    continue;
                // twoLine+=line;

                if(line.contains("newgame")){
                    startNewGame();
                }
                else if(line.contains("exit game")||line.contains("exitgame")){
                    System.exit(0);
                }

                directCommand(line);








                // if(finalCommandToSelect){
                //     for(int i = 0; i < line.length(); i++){
                //         char x = line.charAt(i);
                //         if('1'<= x && x <= '8' && list.size()>='x'-'1'){
                //             list.set(0, list.get(x-'1'));
                //             finalCommandToSelect = false;
                //             finalCommand();
                //         }
                //     }
                // }
                // else if(line.contains("select") || line.contains("move ") || line.contains("z")){
                //     commandSelectSpokenJustNow = true;
                //     commandMoveToSpokenJustNow = false;
                // }
                // else if(line.contains("moveTo")){
                //     commandMoveToSpokenJustNow = true;
                // }
                // else if(line.contains("yes")){
                //     if(list.size() != 0){
                //         if(chess.moveTo(list.get(0))){
                //             System.out.println("Moving "+prev+" to "+list.get(0));
                //             commandMoveToSpokenJustNow = false;
                //         }
                //         else System.out.println("Failed moving from "+prev+" to "+list.get(0));
                //     }
                //     else System.out.println("Failed!");
                // }
                // else if(line.contains("no")){
                //     System.out.println("So then where to move ?");
                //     commandMoveToSpokenJustNow = true;
                // }
                // getCommand(line);
                

            }

            try {
                rec.stopRecognition();
                sttWork();
            } catch (Exception E) {
                System.out.println("main loop e rec stop hoy nai, catch e ashse");
            }
            System.out.println("main loop shesh");
        }

        // public void getCommand(String str) {
        //     str = str.replaceAll("\\s", "");
        //     System.out.println(str+ "----");
        //     str = str.replaceAll("move", "");
        //     list.clear();

        //     String fin = "";
        //     int i = 0;
        //     for (i = 1; i < str.length(); i++) {
        //         char x = str.charAt(i - 1);
        //         char y = str.charAt(i);
        //         if (x == '8' && '1' <= y && y <= '8')
        //             x = 'h';
        //         if(x == 'z' || ('1'<=x && x<='8' && y == '2') ){
        //             y = 'z';
        //             sendCommand(list);
        //             list.clear();
        //             commandMoveToSpokenJustNow = true;
        //         }
        //         if ('1' <= y && y <= '8') {
        //             if ('a' <= x && x <= 'h') {

        //                 String z = "";
        //                 z += x;
        //                 z += y;
        //                 if (!list.contains(z))
        //                     list.add(z);
        //                 // System.out.println("()() "+z);
        //             }
        //         }
        //     }
        //     sendCommand(list);
        // }

        // public void sendCommand(ArrayList<String> list) {
        //     if(list.size() == 0) return;
        //     if (list.size() > 1) {
        //         System.out.println("sorry for the trouble. which one did you mean?");
        //         finalCommandToSelect = true;
        //         System.out.println(list);
        //     } else if (list.size() == 1) {
        //         finalCommand();
        //     }

        // }

        // public void finalCommand() {
        //     if(list.size() == 0) {return;}
        //     // System.out.println(list.get(0) + " final porjonto thik e ashse");
        //     if (commandMoveToSpokenJustNow) {
        //         if(prev.length()==2 && chess.selected(prev))
        //         System.out.println("Confirm moving from "+prev+" to "+list.get(0));
        //         commandSelectSpokenJustNow = false;
                
        //         return;
        //     }
        //     else if(commandSelectSpokenJustNow){
        //         if( !chess.selected(list.get(0)) )System.out.println("Can't select "+list.get(0));
        //         else prev = list.get(0);
        //     }
        //     commandSelectSpokenJustNow = false;
        //     finalCommandToSelect = false;
        //     commandMoveToSpokenJustNow = false;

        // }

        ArrayList<String> totalList = new ArrayList<String>();

        public void directCommand(String str){
            // System.out.print(str+" -> ");
            str = str.replaceAll("\\s", "");
            System.out.print(str+" -> ");
            if(str.contains("no")){
                if(totalList.size() > 0)
                    totalList.remove(totalList.size() - 1);
            }
            else if(str.contains("yes") && totalList.size() == 2){
                totalList.add("yes");
            }
            else{
                for(int i=1; i<str.length(); i++){
                    char x = str.charAt(i-1);
                    char y = str.charAt(i);
                    if (x == '8' && '1' <= y && y <= '8')
                        x = 'h';
                    if('1'<= y && y <= '8'){
                        if('a' <= x && x <= 'h'){
                            String fin = "";
                            fin += x;
                            fin += y;
                            if(!totalList.contains(fin) && totalList.size() < 2){
                                totalList.add(fin);
                            }
                        }
                    }
                }
            }
            System.out.println(totalList);
            int elementToKeep = chess.toMove(totalList);
            for(int i=totalList.size(); i>elementToKeep; i--){
                totalList.remove(i-1); // pop last element
            }
            chess.toMove(totalList);
        }

        
    }














}




















