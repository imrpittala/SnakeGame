import javax.swing.JFrame;

public class SnakeGame extends JFrame {
    
    public SnakeGame() {
        // Create the game board
        GameBoard board = new GameBoard();
        
        // Add it to the window
        add(board);
        
        // Set window properties
        setTitle("Multi-Threaded Snake Game");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);  // Center the window
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new SnakeGame();
    }
}
