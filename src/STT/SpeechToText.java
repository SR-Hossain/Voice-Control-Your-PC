package src.STT;
import src.header.*;
import java.awt.Desktop;
import java.awt.Font;
import java.net.URI;
import java.awt.Robot;
import java.util.Map;
import java.util.logging.Logger;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

// @SuppressWarnings("unused")
public class SpeechToText implements Runnable {
    int ctrl = KeyEvent.VK_CONTROL, alt = KeyEvent.VK_ALT, enter = KeyEvent.VK_ENTER, left = KeyEvent.VK_LEFT, right = KeyEvent.VK_RIGHT, up = KeyEvent.VK_UP, down = KeyEvent.VK_DOWN, tab = KeyEvent.VK_TAB, pgup = KeyEvent.VK_PAGE_UP, pgdown = KeyEvent.VK_PAGE_DOWN, home = KeyEvent.VK_HOME, end = KeyEvent.VK_END, esc = KeyEvent.VK_ESCAPE, space = KeyEvent.VK_SPACE, backspace = KeyEvent.VK_BACK_SPACE, delete = KeyEvent.VK_DELETE, f1 = KeyEvent.VK_F1, f2 = KeyEvent.VK_F2, f3 = KeyEvent.VK_F3, f4 = KeyEvent.VK_F4, f5 = KeyEvent.VK_F5, f6 = KeyEvent.VK_F6, f7 = KeyEvent.VK_F7, f8 = KeyEvent.VK_F8, f9 = KeyEvent.VK_F9, f10 = KeyEvent.VK_F10, f11 = KeyEvent.VK_F11, f12 = KeyEvent.VK_F12;
    Font shuher;
    int waitTime = 0;
    int windows = 524;
    String siteNames = "google facebook youtube twitter";
    Desktop desk = Desktop.getDesktop();
    Map<String, Triplet> commands = new HashMap<String,Triplet>();
    String prevWord = "";
    boolean exit = false;
    LiveSpeechRecognizer recognizer;
    SpeechResult result;
    Robot robot;
    public Jlabel wordToShow;
    public Thread thread;

    private boolean active;

    private boolean stop;

    public void pageToModify(Jlabel wordToShow) {
        this.wordToShow = wordToShow;
        thread = new Thread(this, "VRec");
        thread.start();
    }


    public void run(){
        active = true;
        try {
            shuher = new Font_().shuher;
            work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void work() throws Exception
    {

        Logger cmRootLogger = Logger.getLogger("default.config");
        cmRootLogger.setLevel(java.util.logging.Level.OFF);
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null) {
                System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
        }

        robot = new Robot();
        
        new LmGenerator();

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("Resources/dic.dic");
        configuration.setLanguageModelPath("Resources/lm.lm");
        

        recognizer = new LiveSpeechRecognizer(configuration);
        recognizer.startRecognition(true);


        

        while(!stop && (result = recognizer.getResult()) != null){
            synchronized(this){
                while(!active)
                wait();
            }
            String[] words = result.getHypothesis().split(" ");

            for(String word: words){
                System.out.println(word);

                if(word.length() == 0)continue;
                displayCommand(word);
                if(siteNames.contains(word))gotoSite(word);
                else if(word.contains("chess")){
                    recognizer.stopRecognition();
                    new src.Gui.StartChess();
                }
                else followCommand(word);
            }
        }

        try{
        recognizer.stopRecognition();
        } catch (Exception E){System.out.println("main loop e rec stop hoy nai, catch e ashse");}
        System.out.println("main loop shesh");

        
    }
    void displayCommand(String command){
        wordToShow.setText(command);
    }

    public synchronized void end(){
        System.out.println("force stopping...");
        this.stop = true;
        try{
        recognizer.stopRecognition();
        } catch(Exception e){System.out.println("end method e rec off hoy nai catch e ashse");}
        System.exit(0);

    }
    public synchronized void pause(){
        active = false;
    }
    public synchronized void resume(){
        active = true;
        
        notify();
    }
    void gotoSite(String word) throws Exception {
        
        desk.browse(new URI("http://www."+word+".com"));
    }


    void followCommand(String command){
        
        switch(command){
            case "turnoff": System.exit(0); break;
            case "back" :  pressKeys(alt, left, 0); break;
            case "forward":  pressKeys(alt, right, 0); break;
            case "left":  pressKeys(left, 0, 0); break;
            case "right":  pressKeys(right, 0, 0); break;
            case "up":  pressKeys(up, 0, 0); break;
            case "down":  pressKeys(down, 0, 0); break;
            case "enter":  pressKeys(enter, 0, 0); break;
            case "close":  pressKeys(ctrl, KeyEvent.VK_W, 0); break;
            case "exit":  pressKeys(alt, KeyEvent.VK_F4, 0); break;
            case "newtab":  pressKeys(ctrl, KeyEvent.VK_T, 0); break;
            case "reload":  pressKeys(ctrl, KeyEvent.VK_R, 0); break;
            case "refresh":  pressKeys(ctrl, KeyEvent.VK_R, 0); break;
            case "autoscroll":  pressKeys(down, 0, -1); break;
            case "stopscroll":  robot.keyRelease(down); break;
            case "allTab":  pressKeys(alt, tab, 0); break;
            case "recentWindow":  pressKeys(alt, tab, 0); break;
            case "rightTab":  pressKeys(ctrl, pgdown, 0); break;
            case "leftTab":  pressKeys(ctrl, pgup, 0); break;
            case "recentTab":  pressKeys(ctrl, tab, 0); break;
            case "explorer":  pressKeys(524, KeyEvent.VK_E, 0); break;
        }
    }

    private void pressKeys(int first, int second, int third) {
        robot.keyRelease(down);

        if(waitTime > 0)waitTime--;

        System.out.print("this is "+first+" and "+second+" and "+third+"");

        robot.keyPress(first);
        
        if(second != 0){
            robot.keyPress(second);
            if(third != 0){robot.keyPress(third);robot.keyRelease(third);}
            robot.keyRelease(second);
        }
        
        if(third == -1)return;
        robot.keyRelease(first);
    }


    
}
