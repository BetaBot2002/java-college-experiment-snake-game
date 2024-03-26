import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    Tile food;
    ArrayList<Tile> snakeBody;

    Random random;

    Timer gameLoop;
    int velocityX;
    int velocityY;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        food = new Tile(10, 10);
        snakeBody = new ArrayList<Tile>();

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        for (Tile bodyPart : snakeBody) {
            g.setColor(Color.GREEN);
            g.fillRect(bodyPart.x * tileSize, bodyPart.y * tileSize, tileSize, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public void move() {
        if (isCollided(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile bodyPart = snakeBody.get(i);
            if (i == 0) {
                bodyPart.x = snakeHead.x;
                bodyPart.y = snakeHead.y;
            } else {
                Tile prevBodyPart = snakeBody.get(i - 1);
                bodyPart.x = prevBodyPart.x;
                bodyPart.y = prevBodyPart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }

    public boolean isCollided(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
