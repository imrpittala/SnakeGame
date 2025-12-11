# Code Explanation - Line by Line

## File 1: SnakeGame.java (Main Window)

```java
import javax.swing.JFrame;
```
**What**: Imports the JFrame class (creates windows)
**Why**: We need JFrame to create our game window

---

```java
public class SnakeGame extends JFrame {
```
**What**: SnakeGame IS A JFrame (inherits from JFrame)
**Why**: Allows us to create a window and customize it

---

```java
public SnakeGame() {
    GameBoard board = new GameBoard();
```
**What**: Constructor - runs when we create `new SnakeGame()`
**Why**: Creates the GameBoard object (where actual game happens)

---

```java
    add(board);
```
**What**: Adds the game board to the window
**Why**: Without this, the window would be empty!

---

```java
    setTitle("Multi-Threaded Snake Game");
    setSize(400, 420);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
```
**What**: Configures the window
**Breakdown**:
- `setTitle()` → Window title
- `setSize()` → Window size (400x420 pixels)
- `setDefaultCloseOperation()` → Close program when window closes
- `setResizable()` → Don't allow resizing
- `setLocationRelativeTo()` → Center window
- `setVisible()` → Show the window

---

```java
public static void main(String[] args) {
    new SnakeGame();
}
```
**What**: Entry point - program starts here
**Why**: Java requires a main() method to run
**What happens**: Creates a new SnakeGame object, which creates the window

---

## File 2: GameBoard.java (Game Logic & Threading)

### Part 1: Variable Declarations

```java
public class GameBoard extends JPanel implements Runnable, KeyListener {
```
**What**: GameBoard IS A JPanel, Runnable, AND KeyListener
**Why**:
- JPanel → Can draw graphics
- Runnable → Can run as a Thread
- KeyListener → Can detect keyboard input

---

```java
private final int TILE_SIZE = 25;
private final int GRID_WIDTH = 16;
private final int GRID_HEIGHT = 15;
```
**What**: Game board dimensions
**Why**: Makes it easy to change game size
- TILE_SIZE = 25 pixels per square
- GRID_WIDTH = 16 squares wide
- GRID_HEIGHT = 15 squares tall

---

```java
private ArrayList<Snake> snake;
private String direction = "RIGHT";
private String nextDirection = "RIGHT";
```
**What**: Snake data
**Explanation**:
- `snake` = List of all body segments (each is a Point)
- `direction` = Current direction moving
- `nextDirection` = Direction user pressed (prevents input lag)

---

```java
private Point food;
private boolean running = false;
private Thread gameThread;
```
**What**: Food and game state
**Explanation**:
- `food` = Food location
- `running` = Is game active?
- `gameThread` = The thread that runs the game loop

---

```java
private int score = 0;
private int level = 1;
private int foodEaten = 0;
```
**What**: Game statistics
**Why**: For scoring and difficulty progression

---

### Part 2: Constructor

```java
public GameBoard() {
    setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
```
**What**: Set the size of the game board
**Math**: 16 tiles × 25 pixels = 400 pixels wide
**Why**: Tells Swing how big to make the drawing area

---

```java
    setBackground(Color.BLACK);
    setFocusable(true);
    addKeyListener(this);
```
**What**: Setup and configuration
**Explanation**:
- `setBackground()` → Black background
- `setFocusable()` → This component can receive keyboard focus
- `addKeyListener()` → Listen for key presses

---

```java
    initGame();
}
```
**What**: Calls initGame() to start the game

---

### Part 3: Initialize Game

```java
private void initGame() {
    snake = new ArrayList<>();
    snake.add(new Point(8, 7));   // Head
    snake.add(new Point(7, 7));   // Body
    snake.add(new Point(6, 7));   // Body
    snake.add(new Point(5, 7));   // Body
```
**What**: Creates snake starting at center
**Why**: ArrayList is perfect for dynamic list of body segments
**Head is at index 0** (snake.get(0))

---

```java
    direction = "RIGHT";
    nextDirection = "RIGHT";
    placeFood();
    score = 0;
    foodEaten = 0;
    level = 1;
```
**What**: Reset all game variables

---

```java
    running = true;
    gameThread = new Thread(this);
    gameThread.start();
```
**What**: START THE GAME THREAD!
**Explanation**:
1. `new Thread(this)` → Create thread with this object as Runnable
2. `gameThread.start()` → Start the thread
3. This calls the `run()` method in a **separate thread**

---

### Part 4: Place Food

```java
private void placeFood() {
    int x = (int) (Math.random() * GRID_WIDTH);
    int y = (int) (Math.random() * GRID_HEIGHT);
    food = new Point(x, y);
```
**What**: Random position for food
**How**:
- `Math.random()` → Random decimal 0.0 to 1.0
- Multiply by GRID_WIDTH (16) → 0 to 16
- Cast to int → 0 to 15
- Create Point at that location

---

```java
    for (Point segment : snake) {
        if (segment.x == x && segment.y == y) {
            placeFood();  // Try again
            return;
        }
    }
```
**What**: Make sure food doesn't spawn on snake
**Why**: Don't want food stuck inside snake body

---

### Part 5: The Game Loop (Threading!)

```java
@Override
public void run() {
    while (running) {
        update();
```
**What**: THE HEART OF THE GAME!
**Explanation**:
- `run()` is called when thread starts
- Infinite while loop (as long as running = true)
- `update()` updates game logic

---

```java
        SwingUtilities.invokeLater(() -> repaint());
```
**What**: Safely redraw from background thread
**Why**: GUI updates must happen on Main UI Thread
**How**: `invokeLater()` queues repaint() for main thread to execute

---

```java
        try {
            int sleepTime = 200 - (level * 10);
            if (sleepTime < 80) sleepTime = 80;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
```
**What**: Control game speed
**Explanation**:
- Level 1: sleep 200 - 10 = 190ms per update
- Level 2: sleep 200 - 20 = 180ms per update
- Level increases = game gets faster!
- `Thread.sleep()` pauses the thread

---

### Part 6: Update Game Logic

```java
private void update() {
    if (!running) return;
    
    direction = nextDirection;
```
**What**: Update direction from user input
**Why**: Smooth movement (uses nextDirection set by keyboard)

---

```java
    Point head = snake.get(0);
    
    int newX = head.x;
    int newY = head.y;
    
    if (direction.equals("UP")) newY--;
    else if (direction.equals("DOWN")) newY++;
    else if (direction.equals("LEFT")) newX--;
    else if (direction.equals("RIGHT")) newX++;
```
**What**: Calculate new head position
**How**: Move in the direction of travel

---

```java
    Point newHead = new Point(newX, newY);
    
    if (newX < 0 || newX >= GRID_WIDTH || 
        newY < 0 || newY >= GRID_HEIGHT) {
        running = false;
        return;
    }
```
**What**: Check wall collision
**How**:
- X < 0 → Hit left wall
- X >= 16 → Hit right wall
- Y < 0 → Hit top wall
- Y >= 15 → Hit bottom wall

---

```java
    for (Point segment : snake) {
        if (newHead.x == segment.x && newHead.y == segment.y) {
            running = false;
            return;
        }
    }
```
**What**: Check self collision
**How**: Check if new head position matches any body segment

---

```java
    snake.add(0, newHead);
```
**What**: Add new head to front of snake
**Why**: Index 0 is always the head

---

```java
    if (newHead.x == food.x && newHead.y == food.y) {
        score += 10;
        foodEaten++;
        
        if (foodEaten % 5 == 0) {
            level++;
        }
        
        placeFood();
    } else {
        snake.remove(snake.size() - 1);
    }
```
**What**: Handle food eating
**Explanation**:
- If head == food position:
  - Add score
  - Count food eaten
  - Increase level every 5 foods
  - Place new food
  - **DON'T remove tail** → Snake grows!
- Else:
  - Remove tail
  - Snake length stays same

---

### Part 7: Drawing

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
```
**What**: Called whenever repaint() is called
**Why**: `super.paintComponent()` clears the screen first

---

```java
    if (running) {
        // Draw food (red square)
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, 
                   TILE_SIZE - 1, TILE_SIZE - 1);
```
**What**: Draw food
**How**:
- Set color to red
- `fillRect()` draws filled rectangle
- Position: food.x * 25 (convert grid to pixels)
- Size: 25x25 pixels

---

```java
        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point segment = snake.get(i);
            
            if (i == 0) {
                g.setColor(Color.GREEN);  // Head
            } else {
                g.setColor(new Color(50, 150, 50));  // Body
            }
            
            g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE,
                      TILE_SIZE - 1, TILE_SIZE - 1);
        }
```
**What**: Draw snake
**How**:
- Loop through all segments
- Head (index 0) = bright green
- Body = darker green
- Draw each as filled rectangle

---

```java
    } else {
        // Game over screen
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("GAME OVER!", 80, 100);
```
**What**: Draw game over message

---

### Part 8: Keyboard Input

```java
@Override
public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    
    if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
        nextDirection = "UP";
    }
```
**What**: Handle arrow key press
**Explanation**:
- Get key code from event
- If UP key pressed AND not going DOWN:
  - Set nextDirection to UP
  - (Prevents going backwards)

---

```java
    else if (key == KeyEvent.VK_SPACE && !running) {
        initGame();
    }
```
**What**: Restart game on SPACE press

---

```java
@Override
public void keyReleased(KeyEvent e) {}
@Override
public void keyTyped(KeyEvent e) {}
```
**What**: Required methods (but not used)
**Why**: KeyListener interface requires all three methods

---

## File 3: Point.java

```java
public class Point {
    public int x;
    public int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```
**What**: Simple data class
**Why**: Stores x,y coordinates for snake segments and food

---

## Threading Concepts Explained

### What is a Thread?

**Without Threading:**
```
Main Program Thread
  ├─ Create Window
  ├─ Start Game Loop (blocks here!)
  │   ├─ Update snake position
  │   ├─ Redraw screen
  │   └─ Repeat (locked!)
  └─ Can't accept keyboard input while looping!
     FROZEN! ❌
```

**With Threading:**
```
Main UI Thread              Game Thread
  ├─ Create Window              ├─ start()
  ├─ Wait for events            ├─ run() {
  ├─ Handle keyboard            │   while(running) {
  ├─ Redraw when asked          │     update()
  └─ Active!                     │     repaint()
                                 │     sleep()
                                 │   }
                                 │ }
                                 
Both run SIMULTANEOUSLY! ✓
Window responsive! ✓
```

### How Thread Works in Code

```java
// Step 1: Create Thread
gameThread = new Thread(this);
// 'this' = GameBoard object
// Thread will call GameBoard's run() method

// Step 2: Start Thread
gameThread.start();
// Calls run() in separate thread
// Main thread continues!

// Step 3: run() executes
public void run() {
    while(running) {
        // This loops in separate thread
        // Main thread can do other things
    }
}
```

### Thread.sleep()

```java
Thread.sleep(150);  // Pause for 150 milliseconds

Why?
- Controls game speed
- Without it: updates would be instant (too fast!)
- 150ms = ~6-7 updates per second (playable speed)
```

---

## Summary for Your BTech Report

**Main Points to Include:**

1. **Why Threading?**
   - Game loop must run continuously
   - But GUI must stay responsive
   - Solution: Separate thread for game logic

2. **How It Works?**
   - GameBoard implements Runnable
   - run() method contains infinite loop
   - Thread.start() launches it in background
   - Main thread handles GUI/input

3. **Thread Safety?**
   - SwingUtilities.invokeLater() for GUI updates
   - Simple variables are atomic (safe)
   - No complex synchronization needed

4. **Key Methods?**
   - run(): Game loop
   - update(): Game logic
   - paintComponent(): Drawing
   - keyPressed(): Input handling

**You should be able to explain all of this to your professor!**
