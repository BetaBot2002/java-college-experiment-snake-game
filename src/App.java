
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=600;
        int scorePanelHeight=50;
        int boardHeight=boardWidth+scorePanelHeight;

        JFrame frame=new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.RED);

        SnakeGame snakeGame=new SnakeGame(boardWidth, boardHeight);
        ScorePanel scorePanel=new ScorePanel(snakeGame, boardWidth, scorePanelHeight);
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(snakeGame, BorderLayout.CENTER);
        frame.pack();
        snakeGame.requestFocus();
    }
}
