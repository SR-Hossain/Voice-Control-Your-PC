package src.header;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Color;

public class Jframe extends JFrame{
    public Jframe(){
        // setSize(1000,1000);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
    }
    public void addCloseConfirmation(){
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to close this window?", "Close Window?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                    }
            }
        });
    }
}

