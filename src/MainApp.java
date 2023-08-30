package src;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import src.STT.SpeechToText;
import src.header.*;

import java.awt.Font;
import java.awt.Color;


public class MainApp{
    public static void main(String[] args) throws Exception{
        new MainApp();  
    }
    public int x;
    Jlabel go, running, command; 
    SpeechToText stt;
    Jframe frame;


    public MainApp() throws Exception {
        frame = new Jframe();
        JlayeredPane lpane = new JlayeredPane();

        frame.setTitle("Sophia");
        frame.setSize(500,757);
        addCloseConfirmations();

        frame.add(lpane);

        createPages(lpane);
        addThreeDot(lpane);
        theFont(lpane);

        // Thread stt = new Thread(new SpeechToText());
        stt = new SpeechToText();
        stt.pageToModify(command);
        stt.run();
        

        frame.setSize(500,757);
        frame.setSize(500,757);
        frame.setSize(500,757);

        
                
    }
    void theFont(JlayeredPane lpane) throws Exception {
        command = new Jlabel("facebook twitter google you know wha");
        Font shuher = new Font_().shuher;
        command.setFont(shuher.deriveFont(Font.PLAIN, 60));
        command.setForeground(Color.decode("#26C59B"));
        
        lpane.add(command);
        command.setLocation(200, 630);

        
        lpane.setLayer(command, 500);
    
        command.refresh();
        command.setText(" ");

    }

    private void addCloseConfirmations() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to close this window?", "Close Window?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        stt.end();
                    }
            }
        });
    }

    private void addThreeDot(JlayeredPane lpane) {
        Jlabel threeDot = new Jlabel("src/resources/3dots.png");
        lpane.add(threeDot);
        lpane.setLayer(threeDot, 100);

        
        
    }

    void createPages(JlayeredPane lpane){
        go = new Jlabel("src/resources/go.png");
        lpane.add(go);
        lpane.setLayer(go, 1);



        running = new Jlabel("src/resources/Running.png");
        lpane.add(running);
        lpane.setLayer(running, 0);



        running.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                stt.pause();
                lpane.setLayer(running, 0);
                lpane.setLayer(go, 1);
                command.setText("");
            } 
            public void mouseEntered(MouseEvent e) { } 
            public void mouseExited(MouseEvent e) { } 
            public void mousePressed(MouseEvent e) { } 
            public void mouseReleased(MouseEvent e) { } 
            public void mouseDragged(MouseEvent e) { } 
            public void mouseMoved(MouseEvent e) { }

        });





        go.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // try {
                // stt.thread.notify();
                // } catch(Exception ee) {System.out.println("notify hoy nai");}
                stt.resume();
                lpane.setLayer(go, 0);
                lpane.setLayer(running, 1);
            } 
            public void mouseEntered(MouseEvent e) { } 
            public void mouseExited(MouseEvent e) { } 
            public void mousePressed(MouseEvent e) { } 
            public void mouseReleased(MouseEvent e) { } 
            public void mouseDragged(MouseEvent e) { } 
            public void mouseMoved(MouseEvent e) { }

        });
    }

}





























