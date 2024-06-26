import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;
        Image image;

        Tile(int x, int y, String imagePath) {
            this.x = x;
            this.y = y;
            // this.color = null;
            try {
                this.image = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
                this.image = null;
            }
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile wallTile;
    Tile grassTile;

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

        wallTile = new Tile(0, 0, "Images/WallTile.png");
        grassTile = new Tile(1, 1, "Images/GrassTile.png");
        snakeHead = new Tile(5, 5, "Images/SnakeHead.png");
        food = new Tile(10, 10, "Images/Apple.png");
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
        for (int x = 1; x < boardWidth / tileSize - 1; x++) {
            for (int y = 1; y < boardHeight / tileSize - 1; y++) {
                g.drawImage(grassTile.image, x * tileSize, y * tileSize, tileSize, tileSize, null);
            }
        }

        g.drawImage(snakeHead.image, snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, null);

        if (food.image != null) {
            g.drawImage(food.image, food.x * tileSize, food.y * tileSize, tileSize, tileSize, null);
        }

        for (Tile bodyPart : snakeBody) {
            g.drawImage(bodyPart.image, bodyPart.x * tileSize, bodyPart.y * tileSize, tileSize, tileSize, null);
        }

        drawWall(g);
    }

    public void drawWall(Graphics g) {
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawImage(wallTile.image, i * tileSize, 0, tileSize, tileSize, null);
            g.drawImage(wallTile.image, i * tileSize, (boardHeight / tileSize - 1) * tileSize, tileSize, tileSize,null);
        }

        for (int i = 1; i < boardHeight / tileSize - 1; i++) {
            g.drawImage(wallTile.image, 0, i * tileSize, tileSize, tileSize, null);
            g.drawImage(wallTile.image, (boardWidth / tileSize - 1) * tileSize, i * tileSize, tileSize, tileSize, null);
        }
    }

    public int getScore() {
        return snakeBody.size();
    }

    public void placeFood() {
        food.x = random.nextInt((boardWidth / tileSize) - 2) + 1;
        food.y = random.nextInt((boardHeight / tileSize) - 2) + 1;
    }

    public boolean isCollided(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    public boolean isCollidedWithWall() {
        return snakeHead.x * tileSize < 1 || snakeHead.x * tileSize >= boardWidth - (1 * tileSize) ||
                snakeHead.y * tileSize < 1 || snakeHead.y * tileSize >= boardHeight - (1 * tileSize);
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
            snakeBody.add(new Tile(food.x, food.y, "Images/SnakeBody.png"));
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

    void restart() {
        snakeHead.x = 5;
        snakeHead.y = 5;
        food.x = 10;
        food.y = 10;
        velocityX = 0;
        velocityY = 0;
        snakeBody.clear();
        isGameOver = false;
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    private void showGameOverDialog() {
        int currentScore = snakeBody.size();
        int highScore = FileHelper.getHighScoreFromFile("HighScore.txt");
        String message = "Game Over!\nScore: " + currentScore + "\nHigh Score: " + highScore + "\nPress Space to Restart";

        JOptionPane optionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                new Object[] { "Restart" }, "Restart");

        JDialog dialog = optionPane.createDialog("Game Over");
        dialog.setSize(400, 200);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        
        Object selectedValue = optionPane.getValue();
        if (selectedValue!=null && selectedValue.equals("Restart")) {
            restart();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (isGameOver) {
            gameLoop.stop();
            showGameOverDialog();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isGameOver && e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (!isGameOver && e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (!isGameOver && e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (!isGameOver && e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        } else if (isGameOver && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
            restart();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
