
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.PriorityQueue;
//import java.awt.*;

public class WuziGame extends JFrame{
    Wuzi wuzi;
    boolean gamefinish;
    public WuziGame(int size) {
        //getContentPane().add(new DrawRect());
        this.gamefinish = false;

        JButton b = new JButton("Reset");
        //JButton a = new JButton("Start");
        //this.add(a);
        this.add(b);
        //a.setBounds(100,50 + size * 50,300,100);
        b.setBounds(size * 25,50 + size * 50,300,100);
        JTextField status = new JTextField();
        add(status);
        status.setBounds(size * 25,10,200,30);
        status.setBackground(Color.white);
        wuzi = new Wuzi(status, size, false);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j <size; j++) {
                add(wuzi.board[i][j]);
            }
        }
//        a.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                status.setText("Game START");
//            }
//        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("Game START!!!!");
                status.setBackground(Color.white);
                gamefinish = false;
                wuzi.clearBoard();
            }
        });

        setSize(size * 50 + 400,size * 50 + 400);
        setDefaultCloseOperation(3);
        setTitle("无敌五子棋");
        JScrollBar scrollBar=new JScrollBar(JScrollBar.VERTICAL, 30,40, 0, 500);
        this.getContentPane().add(scrollBar, BorderLayout.EAST);
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        WuziGame w = new WuziGame(13);

    }
}

class Wuzi extends JPanel{
    boolean gamefinish;
    int size;
    JButton[][] board;
    int[][] matrix;
    ImageIcon whiteDot, blackDot, empty, yellowDot;
    BufferedImage pic1, pic2, pic3, yellowDotImage;
    JTextField status;

    Wuzi(JTextField status, int size, boolean gamefinish) {
        this.gamefinish = false;
        this.size = size;
        this.status = status;
        try {
            pic1 = ImageIO.read(new File(Wuzi.class.getResource("blackDot.png").getFile()));
            pic2 = ImageIO.read(new File(Wuzi.class.getResource("whiteDot.jpg").getFile()));
            pic3 = ImageIO.read(new File(Wuzi.class.getResource("empty.jpg").getFile()));
            yellowDotImage = ImageIO.read(new File(Wuzi.class.getResource("yellowDot.jpg").getFile()));
        } catch(Exception ex) {
            System.out.println("cannot get image");
        }
        board = new JButton[size][size];
        matrix = new int[size][size];
        empty = new ImageIcon(pic3);
        whiteDot = new ImageIcon(pic2);
        blackDot = new ImageIcon(pic1);
        yellowDot = new ImageIcon(yellowDotImage);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                JButton jb = new JButton(empty);
                board[i][j] = jb;
                jb.setBounds(100 + i * 50, 50 + j * 50, 49,49);
                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!gamefinish && jb.getIcon() == empty) {
                            jb.setIcon(whiteDot);
                            updateBoard();
                            if (checkWhoWin(matrix) == 0) {
                                fillNextMove(matrix, board);
                                updateBoard();
                                checkWhoWin(matrix);
                            }
                        }
                    }
                });
            }
        }
    }

    void clearBoard() {
        for(int i = 0; i < size ;i++) {
            for(int j = 0; j < size; j++) {
                board[i][j].setIcon(empty);
                matrix[i][j] = 0;
            }
        }
    }
    void updateBoard() {
        //update matrix table and check for win
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(board[i][j].getIcon() == whiteDot) {
                    matrix[i][j] = 1;
                    //System.out.println("find one white dot");
                }
                else if(board[i][j].getIcon() == blackDot) {
                    matrix[i][j] = -1;
                    //System.out.println("find one black dot");
                }
            }
        }

    }

    int checkWhoWin(int[][] matrix) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                int temp = matrix[i][j];
                if(temp == 0) continue;
                if(temp == 1) {
                    if(reachFive(matrix, i, j)) {
                        status.setText("哇塞！！！你真的可以的！甘拜下风！");
                        status.setBackground(Color.RED);
                        gamefinish = true;
                        return 1;
                    }
                }
                else {
                    if(reachFive(matrix, i, j)) {
                        status.setText("哈哈哈哈你还是要再练习");
                        status.setBackground(Color.CYAN);
                        gamefinish = true;
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

    int[][] dir = new int[][]{{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,1},{-1,-1},{1,-1}};

    boolean reachFive(int[][] matrix, int i, int j) {
        int cur = matrix[i][j];
        for(int k = 0; k < dir.length; k++) {
            for(int m = 1; m < 5; m++) {
                int ni = i + dir[k][0] * m;
                int nj = j + dir[k][1] * m;
                if(ni < 0 || nj < 0 || ni >= size || nj >= size || matrix[ni][nj] != cur) {
                    break;
                }
                if(m == 4) {
                    //System.out.println("got 5");
                    for(int t = 0; t < 5; t++) {
                        ni = i + dir[k][0] * t;
                        nj = j + dir[k][1] * t;
                        board[ni][nj].setIcon(yellowDot);
                    }
                    return true;
                }
            }
        }
        return false;

    }
    void fillNextMove(int[][] matrix, JButton[][] c) {
        int max_pos = 0;
        int max_neg = 0;
        int mp_i = 0;
        int mp_j = 0;
        int mn_i = 0;
        int mn_j = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                int x = matrix[i][j];
                if(x == 1 || x == -1) continue;
                int temp1 = getScore(matrix, i, j, -1);
                int temp2 = getScore(matrix, i, j, 1);
                if(temp1 > max_pos) {
                    max_pos = temp1;
                    mp_i = i;
                    mp_j = j;
                }
                if(temp2 > max_neg) {
                    max_neg = temp2;
                    mn_i = i;
                    mn_j = j;
                }
            }
        }
        int fi = (max_pos < max_neg) ? mn_i : mp_i;
        int fj = (max_pos < max_neg) ? mn_j : mp_j;
        matrix[fi][fj] = -1;
        c[fi][fj].setIcon(blackDot);
        if(max_neg > 500) {
            status.setText("哎呀危险");
            status.setBackground(Color.YELLOW);
        }
        else if(max_pos > 500) {
            status.setText("冲压！！！！！");
            status.setBackground(Color.YELLOW);
        }
        else {
            status.setText("慢慢来哦，不急");
            status.setBackground(Color.white);
        }
        //System.out.println("successfully here");
    }
    int[][] dir4 = new int[][]{{1,0},{0,1},{1,1},{-1,1}};
    int getScore(int[][] m, int si, int sj, int type) {
        int score = 0;
        for(int k = 0; k < 4; k++) {
            int lineScore = 1;
            for(int i = 0; i < 5; i++) {
                int ni = si + dir4[k][0] * (i - 4);
                int nj = sj + dir4[k][1] * (i - 4);
                int tempScore = 0;
                for(int j = 0; j < 5; j++) {
                    int nni = ni + dir4[k][0] * j;
                    int nnj = nj + dir4[k][1] * j;
                    if(nni == si && nnj == sj) continue;
                    if(nni < 0 || nnj < 0 || nni >= size || nnj >= size || m[nni][nnj] == -type) {
                        tempScore = 0;
                        break;
                    }
                    if(m[nni][nnj] == type) tempScore++;

                }
                //most important part
                switch(tempScore) {
                    case 0:
                        lineScore += 0;
                        break;
                    case 1:
                        lineScore += 2;
                        break;
                    case 2:
                        lineScore += 5;
                        break;
                    case 3:
                        lineScore += 100;
                        break;
                    case 4:
                        lineScore += 5000;
                        break;
                    default:
                        break;
                }
            }
            score += lineScore;
        }
        return score;
    }

}
//class DrawRect extends JPanel {
//    DrawRect() {
//        super();
//    }
//    @Override
//    public void paint(Graphics g) {
//        super.paintComponent(g);
//        for(int i = 0; i <= 10; i++) {
//            for(int j = 0; j <= 10; j++) {
//                g.setColor(Color.BLACK);
//                g.drawRect(150 + i * 50, 100 + j * 50, 50, 50);
//            }
//        }
//    }
//}
