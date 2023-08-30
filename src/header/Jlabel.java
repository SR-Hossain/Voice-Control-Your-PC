package src.header;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Jlabel extends JLabel{
    public Jlabel(String str){
        super(str);
        if(new File(str).exists())setIcon(str);
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        setVisible(true);
    }
    public void refresh(){
        this.setBounds(this.getX(), this.getY(), this.getPreferredSize().width, this.getPreferredSize().height);
    }
    public void setIcon(String str){
        setIcon(new ImageIcon(str));
        setBounds( 0, 0, Math.min(500, getPreferredSize().width), Math.min(757, getPreferredSize().height ));
        setOpaque(false);
        setText("");
    }
}
