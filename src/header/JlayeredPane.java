package src.header;

import javax.swing.JLayeredPane;
import java.awt.Color;

public class JlayeredPane extends JLayeredPane{
    public JlayeredPane(){
        super();
        setOpaque(true);
        setSize(500, 500);
        setBackground(Color.BLACK);
        setVisible(true);
    }
}

