package src.Gui;

import javax.swing.JFrame;



public class StartChess extends JFrame{
    MainMenu mainMenu = new MainMenu();
    public static void main(String[] args) {
        new StartChess();
    }
    public StartChess() {
        super( "Chess" );
        setSize( 1000, 1000 );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setVisible(true);
        setContentPane(mainMenu);
    }
    public void setNewMenuAsContentPane(){
        setContentPane(mainMenu);
    }

    
}
