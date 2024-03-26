import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;;
public class SnakeGame extends JPanel implements ActionListener{
    private class Tile {
        int x;
        int y;
        
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize=25;

    Tile snakeHead;
    Tile food;

    Random random;

    Timer gameLoop;

    SnakeGame(int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;

        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.BLACK);

        snakeHead=new Tile(5, 5);
        food=new Tile(10,10);

        random=new Random();
        placeFood();

        gameLoop=new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

        g.setColor(Color.RED);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
    }

    public void placeFood(){
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHeight/tileSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
