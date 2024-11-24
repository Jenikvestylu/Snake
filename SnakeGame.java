import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int WIDTH = 30;
    private final int HEIGHT = 20;
    private final int DELAY = 150;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private char nextDirection;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        setResizable(false);
        initGame();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if (direction != 'D') nextDirection = 'U';
                        break;
                    case KeyEvent.VK_S:
                        if (direction != 'U') nextDirection = 'D';
                        break;
                    case KeyEvent.VK_A:
                        if (direction != 'R') nextDirection = 'L';
                        break;
                    case KeyEvent.VK_D:
                        if (direction != 'L') nextDirection = 'R';
                        break;
                }
            }
        });
        setVisible(true);
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        direction = 'R';
        nextDirection = 'R';
        spawnFood();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        do {
            food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (snake.contains(food));
    }

    private void moveSnake() {
        direction = nextDirection;
        Point head = snake.get(0);
        Point newHead;
        switch (direction) {
            case 'U': newHead = new Point(head.x, head.y - 1); break;
            case 'D': newHead = new Point(head.x, head.y + 1); break;
            case 'L': newHead = new Point(head.x - 1, head.y); break;
            case 'R': newHead = new Point(head.x + 1, head.y); break;
            default: return;
        }
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= WIDTH || newHead.y >= HEIGHT || snake.contains(newHead)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.size() - 1));
            System.exit(0);
        }
        snake.add(0, newHead);
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveSnake();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Image buffer = createImage(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        Graphics bufferGraphics = buffer.getGraphics();
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        bufferGraphics.setColor(Color.RED);
        bufferGraphics.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        bufferGraphics.setColor(Color.GREEN);
        for (Point p : snake) {
            bufferGraphics.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.drawImage(buffer, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}
