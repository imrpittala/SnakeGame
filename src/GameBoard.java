import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameBoard extends JPanel implements Runnable, KeyListener {
    
    // Game settings
    private final int TILE_SIZE = 25;      // Size of each square
    private final int GRID_WIDTH = 16;     // 16 tiles wide
    private final int GRID_HEIGHT = 15;    // 15 tiles tall
    
    // Snake data
    private ArrayList<Point> snake;        // Snake body segments
    private String direction = "RIGHT";    // Current direction
    private String nextDirection = "RIGHT"; // Next direction (to handle rapid key presses)
    
    // Food position
    private Point food;
    
    // Game state
    private boolean running = false;
    private Thread gameThread;             // Our game loop thread
    private int score = 0;
    private int level = 1;
    private int foodEaten = 0;
    
    // Constructor
    public GameBoard() {
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, 
                                       GRID_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);               // Listen for keyboard input
        
        initGame();
    }
    
    // Initialize the game
    private void initGame() {
        // Create snake starting at center
        snake = new ArrayList<>();
        snake.add(new Point(8, 7));   // Head
        snake.add(new Point(7, 7));   // Body segment
        snake.add(new Point(6, 7));   // Body segment
        snake.add(new Point(5, 7));   // Body segment
        
        direction = "RIGHT";
        nextDirection = "RIGHT";
        
        // Place food randomly
        placeFood();
        
        score = 0;
        foodEaten = 0;
        level = 1;
        
        // Start the game thread
        running = true;
        gameThread = new Thread(this);
        gameThread.start();  // This calls the run() method
    }
    
    // Place food at random position
    private void placeFood() {
        int x = (int) (Math.random() * GRID_WIDTH);
        int y = (int) (Math.random() * GRID_HEIGHT);
        food = new Point(x, y);
        
        // Make sure food doesn't spawn on snake
        for (Point segment : snake) {
            if (segment.x == x && segment.y == y) {
                placeFood();  // Try again
                return;
            }
        }
    }
    
    // The main game loop (runs on separate thread)
    @Override
    public void run() {
        while (running) {
            update();                   // Update game logic
            
            // Use SwingUtilities to safely update GUI from thread
            SwingUtilities.invokeLater(() -> repaint());
            
            try {
                // Game speed - decrease sleep time as level increases
                int sleepTime = 200 - (level * 10);
                if (sleepTime < 80) sleepTime = 80;
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Update game logic
    private void update() {
        if (!running) return;
        
        // Update direction (this handles rapid key presses smoothly)
        direction = nextDirection;
        
        // Get current head position
        Point head = snake.get(0);
        
        // Calculate new head position based on direction
        int newX = head.x;
        int newY = head.y;
        
        if (direction.equals("UP")) newY--;
        else if (direction.equals("DOWN")) newY++;
        else if (direction.equals("LEFT")) newX--;
        else if (direction.equals("RIGHT")) newX++;
        
        // Create new head
        Point newHead = new Point(newX, newY);
        
        // Check collision with walls
        if (newX < 0 || newX >= GRID_WIDTH || 
            newY < 0 || newY >= GRID_HEIGHT) {
            running = false;  // Game over
            return;
        }
        
        // Check collision with itself
        for (Point segment : snake) {
            if (newHead.x == segment.x && newHead.y == segment.y) {
                running = false;  // Game over
                return;
            }
        }
        
        // Add new head to front
        snake.add(0, newHead);
        
        // Check if food eaten
        if (newHead.x == food.x && newHead.y == food.y) {
            score += 10;
            foodEaten++;
            
            // Increase level every 5 food eaten
            if (foodEaten % 5 == 0) {
                level++;
            }
            
            placeFood();  // Place new food (snake grows because we don't remove tail)
        } else {
            snake.remove(snake.size() - 1);  // Remove tail (maintains length)
        }
    }
    
    // Draw everything on screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (running) {
            // Draw food (red square)
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, 
                       TILE_SIZE - 1, TILE_SIZE - 1);
            
            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                Point segment = snake.get(i);
                
                // Head is bright green, body is darker green
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(50, 150, 50));
                }
                
                g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE,
                          TILE_SIZE - 1, TILE_SIZE - 1);
            }
            
            // Draw score and level at bottom
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score + "  |  Level: " + level + "  |  Length: " + snake.size(), 10, GRID_HEIGHT * TILE_SIZE + 20);
            
        } else {
            // Game over screen
            g.setColor(new Color(0, 0, 0, 150));  // Semi-transparent black
            g.fillRect(0, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER!", 80, 100);
            
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Final Score: " + score, 100, 150);
            g.drawString("Level: " + level, 100, 180);
            g.drawString("Length: " + snake.size(), 100, 210);
            
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press SPACE to restart", 90, 250);
        }
    }
    
    // Handle key press events
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Arrow keys change direction (but can't go backwards)
        if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
            nextDirection = "UP";
        }
        else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) {
            nextDirection = "DOWN";
        }
        else if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
            nextDirection = "LEFT";
        }
        else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
            nextDirection = "RIGHT";
        }
        
        // W, A, S, D keys also work
        else if (key == KeyEvent.VK_W && !direction.equals("DOWN")) {
            nextDirection = "UP";
        }
        else if (key == KeyEvent.VK_S && !direction.equals("UP")) {
            nextDirection = "DOWN";
        }
        else if (key == KeyEvent.VK_A && !direction.equals("RIGHT")) {
            nextDirection = "LEFT";
        }
        else if (key == KeyEvent.VK_D && !direction.equals("LEFT")) {
            nextDirection = "RIGHT";
        }
        
        // Space bar to restart after game over
        else if (key == KeyEvent.VK_SPACE && !running) {
            initGame();  // Restart the game
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}  // Not needed
    
    @Override
    public void keyTyped(KeyEvent e) {}  // Not needed
}
