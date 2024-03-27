import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;
        Color color;

        Tile(int x, int y,Color color) {
            this.x = x;
            this.y = y;
            this.color=color;
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
    boolean isGameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5,Color.GREEN);
        food = new Tile(10, 10,Color.RED);
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
        g.setFont(new Font("Helvetica", Font.BOLD, 18));
        if (isGameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over!! Score: " + String.valueOf(snakeBody.size()), tileSize - 18, tileSize);
        } else {
            g.setColor(Color.GREEN);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 18, tileSize);
        }

        g.setColor(snakeHead.color);
        g.fillRoundRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, 20, 20);

        g.setColor(food.color);
        g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        for (Tile bodyPart : snakeBody) {
            g.setColor(bodyPart.color);
            g.fill3DRect(bodyPart.x * tileSize, bodyPart.y * tileSize, tileSize, tileSize, true);
        }

    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean isCollided(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    public boolean isCollidedWithWall() {
        return snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight;
    }

    public void move() {
        for (Tile bodyPart : snakeBody) {
            if (isCollided(bodyPart, snakeHead)) {
                isGameOver = true;
            }
        }

        if (isCollidedWithWall()) {
            isGameOver = true;
        }

        if (isCollided(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y,ColorPicker.getNextColor()));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (isGameOver)
            gameLoop.stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
            // System.out.println(e.getKeyCode());
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
            // System.out.println(e.getKeyCode());
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
            // System.out.println(e.getKeyCode());
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
            // System.out.println(e.getKeyCode());
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
