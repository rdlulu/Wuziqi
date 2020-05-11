
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.awt.*;

public class Wuzi extends JFrame {
    public Wuzi() {
        setSize(800,800);
        setVisible(true);
        setTitle("My first plot");
        JButton b = new JButton("Reset");
        JButton a = new JButton("Start");
        JTextField status = new JTextField();
        status.setBounds(250,100,100,50);
        add(status);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("Game OVER");
            }
        });
        a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("Game START");
            }
        });
        add(a);
        add(b);

        Board board = new Board();
        a.setBounds(100,600,300,100);
        b.setBounds(400,600,300,100);

        setDefaultCloseOperation(3);

        setLayout(null);

    }

    public static void main(String[] args) {
        Wuzi w = new Wuzi();

    }
}
class Board {
    BufferedImage blackDot, whiteDot, empty;
    JButton[][] imageboard;
    int[][] board;

    Board() {
        try {
            blackDot = ImageIO.read(new File("/Users/rdlulu/Project/myface.png"));
            whiteDot = ImageIO.read(new File("/Users/rdlulu/Project/girl.jpg"));
            empty = ImageIO.read(new File("/Users/rdlulu/Project/534544_21.jpg"));
        } catch(Exception ex) {
            System.out.println("cannot get image");
        }
        imageboard = new JButton[10][10];
        board = new int[10][10];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                JButton jb = new JButton(new ImageIcon(empty));
                jb.setBounds(100 + i * 30, 15 + j * 30, 30,30);
                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jb.setIcon(new ImageIcon(blackDot));
                    }
                });
            }
        }
    }

}
