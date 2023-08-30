package src.Gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class Chess extends JLayeredPane{

    // JLayeredPane content;
    // content = (JLayeredPane) getContentPane();

    int[] prime = {3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131, 137};
    public String firstplayer = "white", initposStr;
    public Boolean isButtonOn = false;
    public Rectangle[][] board = new Rectangle[10][10];
    public int focus = 20, prevRow, prevCol, newRow, newCol, whitemoves, blackmoves;
    public Boolean isDragging = false, whiteturn;
    int cur_row; int cur_col, wag = 0, totalMoves = 0, voldemort = 1;
    public char king = 'K';
    JLabel bg;
    JLayeredPane mainmenuPane;
    MainMenu mainMenu;
    JLabel winner = new JLabel(" ");

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void takeString(){
        try{
            Scanner sc = new Scanner(new File("Resources/Chess/initPosString.txt"));
            initposStr = sc.nextLine(); sc.close();
            } catch (Exception e){ System.out.println("Piece data input error"); }
    }
    JLabel fromBox = Jtext("  ?  ->", 400, 40);
    JLabel toBox = Jtext("?     .", 550, 40);


    boolean dontPrintFromToTo = false;




    public Chess() {
        newGame();
    }
    public void newGame(){
                // super();
        // super( "Chess" );
        // content = getLayeredPaneAbove(c);
        dontPrintFromToTo = false;
        takeString();
        firstplayer = "white";
        whitemoves = blackmoves = 0;
        whiteturn = true;
        // content = new JLayeredPane();
        setOpaque( true );
        
        // setContentPane( content );
        setSize( 1500, 1000 );
        // add(content);
        // setSize( 1500, 1000 );
        // setDefaultCloseOperation( EXIT_ON_CLOSE );
        setRectangle();
        winner.setText(" ");


        // Background
        if(firstplayer == "white")
        bg = this.setJlabel(null, 1, "Resources/Chess/white.png", 0, 0);
        else bg = this.setJlabel(null, 1, "Resources/Chess/black.png", 0, 0);



        setVisible(true);

        refreshBoard();



    }
    public JLabel Jtext(String str, int x, int y){
        JLabel typeLabel = new JLabel(str);
        typeLabel.setFont(new Font("Serif", Font.ITALIC, 50));
        typeLabel.setForeground(Color.BLACK);
        typeLabel.setBounds(x, y, typeLabel.getPreferredSize().width, typeLabel.getPreferredSize().height );
        setLayer(typeLabel, ++focus);

        add(typeLabel);

        return typeLabel;

    }
    /*
        jar turn, tar raja ki check e ache kina ta dekha lagbe
        so suppose white ekhn chaal dibe,
            tahole black er shob guti gulor moves count kora lagbe (check)
            
            then white er guti gular kongula moves dite parbe ta count kora lagbe

        mane shob e count kora lagbe


        jar moves 0, tar upor press korle kichu hobe na
        jar moves ache, se jdi thik jaygay release hoy tahole move hobe
    */

    public void directGoYX(int uu, int vv) {
        int r = cur_row + uu;
        int c = cur_col + vv;
        if(r<1 || r>8 || c<1 || c>8)return;
        if(whiteturn)king = 'K';
        else king = 'k';


        board[r][c].check = 'c';
        if(board[r][c].iconChar == king){wag++;voldemort*=board[cur_row][cur_col].iconSerial;}
    }

    public void goYX(int r, int c, int i, int j, int num) {
        if(whiteturn)king='K';
        else king='k';

        char c1='x',c2='x';
        int kr1, kc1, kr2, kc2;
        kr1 = kr2 = kc1 = kc2 = -100;
        for(;;){
            r+=i; c+=j;
            if(r<1 || r>8 || c<1 || c>8){r-=i;c-=j;break;}
            if(board[r][c].iconChar != 'x'){
                if(c1 == 'x'){ c1 = board[r][c].iconChar; kr1 = r; kc1 = c; }
                else { c2 = board[r][c].iconChar; kr2 = r; kc2 = c; break; }
            }
        }

        if(r == cur_row && c == cur_col)return;
        if(kr1 == -100)kr1 = r;
        if(kr2 == -100)kr2 = r;
        if(kc1 == -100)kc1 = c;
        if(kc2 == -100)kc2 = c;


        for(int cr=cur_row+i, cc=cur_col+j; cr!=kr1 || cc!=kc1; cr+=i, cc+=j){
            if(cr<1 || cr>8 || cc<1 || cc>8)break;
            board[cr][cc].check = 'c';
        }
        if(c1!='x' && board[kr1][kc1].iconSerial%2==board[cur_row][cur_col].iconSerial%2){
            board[kr1][kc1].check = 'c';
            return;
        }

        if(c2 == king){
            for(int cr=cur_row+i, cc=cur_col+j; cr!=kr2 || cc!=kc2; cr+=i, cc+=j){
                if(cr<1 || cr>8 || cc<1 || cc>8)break;
                board[cr][cc].members *= board[cur_row][cur_col].iconSerial;
            }
        }

        if(c1 == king){  
            for(int cr=cur_row, cc=cur_col; cr!=kr1 || cc!=kc1; cr+=i, cc+=j){
                board[cr][cc].members *= board[cur_row][cur_col].iconSerial;
            }
            
            for(int cr=kr1, cc=kc1; cr!=kr2+i || cc!=kc2+j; cr+=i, cc+=j){
                if(cr<1 || cr>8 || cc<1 || cc>8)break;
                board[cr][cc].check = 'c';
            }

            voldemort *= board[cur_row][cur_col].iconSerial;
        }



        
    }



    public void refreshBoard() {
        pawnPromotion();

        eraseBoard();


        for(cur_row = 1; cur_row<=8; cur_row++) {
            for(cur_col = 1; cur_col<=8; cur_col++) {

                char ch = board[cur_row][cur_col].iconChar;
                if(ch == 'x')continue;
                if(!(whiteturn ^ (ch<'Z')))continue;

                if(board[cur_row][cur_col].check != 'c')board[cur_row][cur_col].check = 'C';

                if(ch == 'k' || ch == 'K'){
                    for(int i=-1; i<=1; i++){
                        for(int j=-1; j<=1; j++){
                            if(i==0 && j==0)continue;
                            directGoYX(i, j);
                        }
                    }
                }

                if(ch == 'b' || ch == 'B' || ch == 'q' || ch == 'Q'){
                    goYX(cur_row, cur_col, 1, 1, 0);
                    goYX(cur_row, cur_col, 1, -1, 0);
                    goYX(cur_row, cur_col, -1, 1, 0);
                    goYX(cur_row, cur_col, -1, -1, 0);
                }
                if(ch == 'r' || ch == 'R' || ch == 'q' || ch == 'Q'){
                    goYX(cur_row, cur_col, 0, 1, 0);
                    goYX(cur_row, cur_col, 0, -1, 0);
                    goYX(cur_row, cur_col, 1, 0, 0);
                    goYX(cur_row, cur_col, -1, 0, 0);
                }
                if(ch == 'h' || ch == 'H'){
                    for(int uu=-2; uu<=2; uu+=4)
                        for(int vv = -1; vv<=1; vv+=2){
                            directGoYX(uu, vv);
                            directGoYX(vv, uu);
                        }
                }
                if(ch == 'p'){
                    directGoYX(-1, 1); directGoYX(-1, -1);
                }
                if(ch == 'P'){
                    directGoYX(1, 1); directGoYX(1, -1);
                }

            }
        }

        // whose turn is now, we should count their moves
        for(cur_row = 1; cur_row<=8; cur_row++) {
            for(cur_col = 1; cur_col<=8; cur_col++) {

                char ch = board[cur_row][cur_col].iconChar;
                if(ch == 'x')continue;
                if(whiteturn ^ (ch<'Z'))continue;

                if(ch == 'k' || ch == 'K')board[cur_row][cur_col].countMoves(0,0);

                if(ch == 'b' || ch == 'B' || ch == 'q' || ch == 'Q'){
                    board[cur_row][cur_col].countMoves(1, 1);
                    board[cur_row][cur_col].countMoves(1, -1);
                    board[cur_row][cur_col].countMoves(-1, 1);
                    board[cur_row][cur_col].countMoves(-1, -1);
                }
                if(ch == 'r' || ch == 'R' || ch == 'q' || ch == 'Q'){
                    board[cur_row][cur_col].countMoves(0, 1);
                    board[cur_row][cur_col].countMoves(0, -1);
                    board[cur_row][cur_col].countMoves(1, 0);
                    board[cur_row][cur_col].countMoves(-1, 0);
                }
                if(ch == 'h' || ch == 'H'){
                    for(int uu=-2; uu<=2; uu+=4)
                        for(int vv = -1; vv<=1; vv+=2){
                            board[cur_row][cur_col].countMoves(uu, vv);
                            board[cur_row][cur_col].countMoves(vv, uu);
                        }
                }
                if(ch == 'p'){
                    if(cur_row==1)continue;
                    if(board[cur_row-1][cur_col].iconChar == 'x'){board[cur_row][cur_col].countMoves(-1, 0);
                    if(cur_row == 7 && board[cur_row-2][cur_col].iconChar == 'x')board[cur_row][cur_col].countMoves(-2, 0);}
                    if(cur_col-1>=1 && board[cur_row-1][cur_col-1].iconChar != 'x'){
                        board[cur_row][cur_col].countMoves(-1, -1);}
                    if(cur_col+1<=8 && board[cur_row-1][cur_col+1].iconChar != 'x'){
                        board[cur_row][cur_col].countMoves(-1, 1);}
                }
                if(ch == 'P'){
                    if(cur_row==8)continue;
                    if(board[cur_row+1][cur_col].iconChar == 'x'){board[cur_row][cur_col].countMoves(1, 0);
                    if(cur_row == 2 && board[cur_row+2][cur_col].iconChar == 'x')board[cur_row][cur_col].countMoves(2, 0);}
                    if(cur_col-1>=1 && board[cur_row+1][cur_col-1].iconChar != 'x'){
                        board[cur_row][cur_col].countMoves(1, -1);}
                    if(cur_col+1<=8 && board[cur_row+1][cur_col+1].iconChar != 'x'){
                        board[cur_row][cur_col].countMoves(1, 1);}
                }


            }
        }
        
        // drawBoard();
    }

    public class LabelDragger implements MouseListener, MouseMotionListener {

        public void mousePressed(MouseEvent e) {
            if(isButtonOn)return;
            // drawBoard();
            isDragging = true;
            JLabel label = (JLabel) e.getSource();
            setLayer(label, ++focus);
            int x = label.getX() + e.getX(), y = label.getY() + e.getY();
            prevRow = getrowy(y); prevCol = x/100;
                    System.out.println(prevCol + " " + prevRow + " " );
            if(board[prevRow][prevCol].moves == 0)isDragging = false;
            if(whiteturn && (board[prevRow][prevCol].iconChar>'Z'))isDragging = false;
        }
        public void mouseDragged(MouseEvent e) {
            if(isButtonOn)return;
            JLabel label = (JLabel)e.getSource();
            int x = label.getX() + e.getX();
            int y = label.getY() + e.getY();

            if(!isDragging)
            {
                int ttx = board[prevRow][prevCol].posx, tty = board[prevRow][prevCol].posy;
                if(x<=ttx)x = ttx; else if(x>=ttx+100)x = ttx+100;
                if(y<=tty)y = tty; else if(y>=tty+100)y = tty+100;
            }
            else{ 
                if(x<=100)x = 100; else if(x>=900)x = 900;
                if(y<=100)y = 100; else if(y>=900)y = 900;     
            }

            label.setLocation(x - 40, y - 40);
            SwingUtilities.invokeLater( new RefreshThread() );      

        }
        public void mouseReleased(MouseEvent e) {
            if(isButtonOn)return;
            JLabel label = (JLabel) e.getSource();
            setLayer(label, focus++);
            int x = label.getX() + e.getX(), y = label.getY() + e.getY();
            newRow = getrowy(y); newCol = x/100;
            Boolean setToPrevPosition = false;

            if(!isDragging || newRow <1 || newRow > 8 || newCol <1 || newCol >8)setToPrevPosition = true;
            else if(board[newRow][newCol].members % board[prevRow][prevCol].iconSerial != 0)setToPrevPosition = true;
            else if(prevRow==newRow && prevCol==newCol)setToPrevPosition = true;
            if(setToPrevPosition){
                label.setLocation(board[prevRow][prevCol].posx+10, board[prevRow][prevCol].posy+10);
            }
            else{
                if(board[newRow][newCol].icon != null)
                    remove(board[newRow][newCol].icon);
                setLayer(label, --focus);
                board[newRow][newCol].icon = board[prevRow][prevCol].icon;
                board[newRow][newCol].iconChar = board[prevRow][prevCol].iconChar;
                board[newRow][newCol].iconSerial = board[prevRow][prevCol].iconSerial;
                board[newRow][newCol].icon = board[prevRow][prevCol].icon;
                
                board[prevRow][prevCol].iconChar = 'x';
                board[prevRow][prevCol].iconSerial = 1;
                board[prevRow][prevCol].icon = null;
                label.setLocation(board[newRow][newCol].posx+10, board[newRow][newCol].posy+10);
                whiteturn = !whiteturn;
            }

            refreshBoard();
            // drawBoard(); // comment

            if(totalMoves == 0) {
                dontPrintFromToTo = true;
                endTheGame();
            }


            

        }


        public void mouseMoved(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    
    }

    public void endTheGame() {
        // JLabel winner;
        if(whiteturn) 
        {
            winner.setText("Black Wins!");
            // winner = new JLabel("Black wins!\n\n");                    
        }
        else {
            winner.setText("White Wins!");
            // winner = new JLabel("White wins!\n\n");
        }
        fromBox.setText("");
        toBox.setText("");
        winner.setFont(new Font("Serif", Font.ITALIC, 50));
        winner.setForeground(Color.BLACK);
        winner.setBounds(300, 20, winner.getPreferredSize().width, winner.getPreferredSize().height );
        setLayer(winner, ++focus);

        add(winner);

        setLayer(getButton("New Game", 1000, 850), ++focus);
        setLayer(getButton("Exit", 1200, 850), ++focus);
    }

    public JLabel setJlabel( JLabel label, int pos, String str, int posX, int posY) {
        label = new JLabel(new ImageIcon(str));
        label.setBounds( posX, posY, label.getPreferredSize().width, label.getPreferredSize().height );
        if(str != "Resources/Chess/black.png" && str != "Resources/Chess/white.png" && str != "Resources/Chess/Chessboard.png")
            {
                LabelDragger labelDragger = new LabelDragger();
                label.addMouseMotionListener(labelDragger);
                label.addMouseListener(labelDragger);
            }

        label.setOpaque( false );
        setLayer( label, pos );

        add( label );

        return label;
    }
 
    public int getrowy(int y){ if(firstplayer == "black")return y/100; else return 9-y/100; }

    private class RefreshThread extends Thread { public void run() { revalidate(); repaint();  } }

    JButton[] pawnPromotionArr = new JButton[4];

    public void setRectangle(){
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                if(board[i][j] != null){
                    board[i][j].clean();
                }
                board[i][j] = new Rectangle(i, j, 100*j, 100*i);
                if(firstplayer == "white"){
                    board[i][j].posy = 100*(9-i);
                }
            }
        }
        String str = initposStr;
        for(int i=0; i<str.length(); i+=3){
            int x = str.charAt(i+1)-'A'+1, y = str.charAt(i+2)-'0';
            board[y][x].iconChar = str.charAt(i);
            String iconStr = "Resources/Chess/ChessPieces/"+board[y][x].iconChar+".png";
            board[y][x].icon = setJlabel(null, 2, iconStr ,board[y][x].posx+10, board[y][x].posy+10);
            
            if(str.charAt(i) > 'Z')board[y][x].iconSerial = board[y][x].members = prime[i/3]*2;
            else board[y][x].iconSerial = board[y][x].members = prime[i/3];

        }

        pawnPromotionArr[0] = getButton("Queen", 400, 300);
        pawnPromotionArr[3] = getButton("Knight", 400, 400);
        pawnPromotionArr[1] = getButton("Rook", 400, 500);
        pawnPromotionArr[2] = getButton("Bishop", 400, 600);
        // for(int i=0; i<4; i++)
        //     add(pawnPromotionArr[i]);

    }


    public class Rectangle{
        int moves = 0, posx=1, posy = 1, row=1, col=1;
        long members = 1, iconSerial = 1;
        JLabel icon = null;
        char iconChar='x', check='x';
        int checkMembers=1;

        public void clean(){
            moves = posx = posy = row = col = 0;
            members = iconSerial = checkMembers = 0;
            if(icon != null){
                remove(icon);
                icon = null;
            }
        }

        public Rectangle(int r, int c, int pX, int pY){ row = r;col = c; posx = pX; posy = pY; }

        public void countMoves(int i, int j){
            int r = row;
            int c = col;
            if(whiteturn)king = 'K';
            else king = 'k';
            if(iconChar == king){
                for(i=-1; i<=1; i++){
                    for(j=-1; j<=1; j++){
                        if(i==0 && j==0)continue;

                        if(row+i < 1 || row+i > 8 || col+j < 1 || col+j > 8)continue;

                        if(board[row+i][col+j].check == 'c')continue;

                        if(board[row+i][col+j].iconChar == 'x' || board[row+i][col+j].iconSerial%2 != iconSerial%2){
                            moves++; totalMoves++;
                            board[row+i][col+j].members *= iconSerial;
                        }
                    }
                }


                return;
            }
            if(wag >= 2){moves=0;return;}

            long res = voldemort*(members/iconSerial); //////// check if res = 0
            if(res == 0){res=1;}

            if(iconChar == 'p' || iconChar == 'P' || iconChar == 'h' || iconChar == 'H'){
                if(r+i < 1 || r+i > 8 || c+j < 1 || c+j > 8)return;
                if(board[r+i][c+j].iconChar=='x' || iconSerial%2!=board[r+i][c+j].iconSerial%2){
                    if(board[r+i][c+j].members % res == 0){
                        if(board[r+i][c+j].members % iconSerial != 0){
                            board[r+i][c+j].members *= iconSerial; moves++; totalMoves++;
                        }
                    }
                }
                return;
            }



            for(;;){
                r+=i; c+=j;
                if(r<1 || r>8 || c<1 || c>8)return;

                if(board[r][c].iconChar=='x' || iconSerial%2!=board[r][c].iconSerial%2){
                    if(board[r][c].members % res == 0){
                        if(board[r][c].members % iconSerial != 0){
                            board[r][c].members *= iconSerial; moves++; totalMoves++;
                        }
                    }

                    if(board[r][c].iconChar != 'x')return;
                }
                else return;
            }
            
            

        }

    }

    public void print(String str){ System.out.print(str); }

    public void eraseBoard() {
        wag = 0;
        totalMoves = 0;
        voldemort = 1;
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                board[i][j].check = 'x';
                board[i][j].moves = 0;
                // if(board[i][j].check == 'x')
                board[i][j].members = board[i][j].iconSerial;
            }
        }
    }

    public void drawBoard(){
        System.out.print("new instance: "+whiteturn+"\n\n");
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                print(board[i][j].iconSerial+"\t");
            }
            System.out.print("\n");
        }
        print("\n\n");
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                print(board[i][j].members+"\t");
            }
            System.out.print("\n");
        }
        print("\n\n");
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                print(board[i][j].moves+"\t");
            }
            System.out.print("\n");
        }
        print("\n\n");
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                print(board[i][j].iconChar+"\t");
            }
            System.out.print("\n");
        }
        print("\n\n");
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                print(board[i][j].check+"\t");
            }
            System.out.print("\n");
        }
        print("\n\n"+wag);

        print("\n\n\n\n\n\n");
    }

    int bx, by;
    public void pawnPromotion(){
        // Boolean promoteBool = false;
        for(int j=1; j<=8; j*=8){
            for(int i=1; i<=8; i++){
                if(board[j][i].iconChar =='p' || board[j][i].iconChar =='P'){
                    isButtonOn = true;
                    pawnPromotionLayer(focus+2);
                    bx = i;
                    by = j;
                    j=9;
                    break;
                
                }
            }
        }
    }

    public JButton getButton(String buttonStr, int x, int y){
        JButton b = new JButton(buttonStr);
        b.setOpaque(false);
        b.setVisible(true);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 50));
        if(buttonStr == "New Game" || buttonStr == "Exit"){
            b.setFont(new Font("Arial", Font.BOLD, 30));
            b.setBounds(x,y,200,50);
        }
        else
            b.setBounds(x,y, 400, 100);
        b.addMouseListener(new ButtonClicker());
        add(b);
        return b;
    }
    public void pawnPromotionLayer(int k){
        for(int i=0; i<4; i++){
            setLayer(pawnPromotionArr[i], k);
        }
    }

    public class ButtonClicker implements MouseListener{

        public void mouseClicked(MouseEvent e) {
            isButtonOn = false;

            JButton b = (JButton) e.getSource();
            String bname = b.getText();

            if(bname.equals("Queen")){
                board[by][bx].iconChar = (board[by][bx].iconChar == 'p')? 'q':'Q';
            }
            else if(bname.equals("Rook")){
                board[by][bx].iconChar = (board[by][bx].iconChar == 'p')? 'r':'R';
            }
            else if(bname.equals("Knight")){
                board[by][bx].iconChar = (board[by][bx].iconChar == 'p')? 'h':'H';
            }
            else if(bname.equals("Bishop")){
                board[by][bx].iconChar = (board[by][bx].iconChar == 'p')? 'b':'B';
            }

            else if(bname.equals("New Game")){
                // setVisible(false);
                getRootPane().setContentPane(mainMenu);
                
                return;
            }
            else if(bname.equals("Exit")){
                System.exit(0);
            }

            remove(board[by][bx].icon);

            board[by][bx].icon = setJlabel(null, focus, "Resources/Chess/ChessPieces/"+board[by][bx].iconChar+".png", board[by][bx].posx+10, board[by][bx].posy+10);



            pawnPromotionLayer(0);
            
        }

        public void mousePressed(MouseEvent e) { } 
        public void mouseReleased(MouseEvent e) { } 
        public void mouseEntered(MouseEvent e) { } 
        public void mouseExited(MouseEvent e) { }

    }











    public boolean selected(String court) {
        // System.out.println(court+" bujhso?");
        if(isButtonOn)return false;
        // drawBoard();
        isDragging = true;
        // JLabel label = (JLabel) e.getSource();
        // setLayer(label, ++focus);
        // int x = label.getX() + e.getX(), y = label.getY() + e.getY();
        int x = court.charAt(0)-'a'+1, y = court.charAt(1)-'0';
        prevRow = y; prevCol = x;
                // System.out.println(prevCol + " " + prevRow + " " );
        // return true; // comment this line

        if(board[prevRow][prevCol].icon == null)return false;
        if(board[prevRow][prevCol].moves == 0){
            isDragging = false;
            return false;
        }
        if(whiteturn && (board[prevRow][prevCol].iconChar>'Z')){
            isDragging = false;
            return false;
        }
        return true;
    }
    public boolean moveTo(String dest) {
        try{
            if(isButtonOn)return false;
            int x = dest.charAt(0)-'a'+1, y = dest.charAt(1)-'0';
            newRow = y; newCol = x;
            Boolean setToPrevPosition = false;
            JLabel label = board[prevRow][prevCol].icon;

            if(!isDragging || newRow <1 || newRow > 8 || newCol <1 || newCol >8)setToPrevPosition = true;
            else if(board[newRow][newCol].members % board[prevRow][prevCol].iconSerial != 0)setToPrevPosition = true;
            else if(prevRow==newRow && prevCol==newCol)setToPrevPosition = true;
            if(setToPrevPosition){
                label.setLocation(board[prevRow][prevCol].posx+10, board[prevRow][prevCol].posy+10);
                return false;
            }
            else{
                if(board[newRow][newCol].icon != null)
                    remove(board[newRow][newCol].icon);
                board[newRow][newCol].icon = board[prevRow][prevCol].icon;
                board[newRow][newCol].iconChar = board[prevRow][prevCol].iconChar;
                board[newRow][newCol].iconSerial = board[prevRow][prevCol].iconSerial;
                board[newRow][newCol].icon = board[prevRow][prevCol].icon;
                
                board[prevRow][prevCol].iconChar = 'x';
                board[prevRow][prevCol].iconSerial = 1;
                board[prevRow][prevCol].icon = null;
                label.setLocation(board[newRow][newCol].posx+10, board[newRow][newCol].posy+10);
                whiteturn = !whiteturn;
            }


            refreshBoard();
            // drawBoard(); // comment

            if(totalMoves == 0) {
                dontPrintFromToTo = true;
                endTheGame();
            }
            return true;
        }catch(NullPointerException e){return false;}

    }

    public int toMove(ArrayList<String> moveList){
        if(dontPrintFromToTo)return 0;
        if(moveList.size() == 0)
        {
            fromBox.setText("?  ->");
            toBox.setText("?");
            return 0;
        }
        if(moveList.size()>=1){
            if(!selected(moveList.get(0))){
                fromBox.setText("X  ->");
                toBox.setText("?");
                return 0;
            }
            fromBox.setText(moveList.get(0)+" ->");
            toBox.setText("?");
        }
        if(moveList.size()>=2){
            if(moveList.size()>=3){
                if(moveList.get(2) == "yes"){
                    if(!moveTo(moveList.get(1))){
                        return 1;
                    }
                    else{
                        return 0;
                    }
                }
            }
            else{
                toBox.setText(moveList.get(1)+"?");

                return 3; // wait for confirmation
            } 
        }
        return 4;
    }



}
