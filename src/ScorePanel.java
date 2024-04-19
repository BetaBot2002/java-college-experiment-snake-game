import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ScorePanel extends JPanel implements ActionListener{
    SnakeGame snakeGame;
    int currentScore;
    int highestScore;
    Timer scoreLoop;

    ScorePanel(SnakeGame snakeGame, int panelWidth, int panelHeight) {
        this.snakeGame=snakeGame;
        this.currentScore = 0;
        this.highestScore = FileHelper.getHighScoreFromFile("HighScore.txt");

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.WHITE);

        scoreLoop = new Timer(100, this);
        scoreLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("Current Score: " + currentScore, 10, 20);
        g.drawString("Highest Score: " + highestScore, 10, 40);
    }

    public void updateScore() {
        currentScore = snakeGame.getScore();
        if (currentScore > highestScore) {
            highestScore = currentScore;
            FileHelper.writeLineToFile("HighScore.txt", currentScore);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateScore();
        repaint();
    }
}
