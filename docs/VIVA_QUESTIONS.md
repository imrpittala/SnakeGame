# Viva Questions & Answers - Multi-Threaded Snake Game

## Frequently Asked Questions Your Professor Might Ask

---

### **1. What is Threading and Why Did You Use It?**

**Question**: "Explain why you used threading in your snake game. What would happen without it?"

**Answer**:

"Threading is using multiple threads to do multiple tasks simultaneously. In my game:

**Without Threading:**
- Game loop runs on main thread
- While loop is executing update/redraw
- Window becomes FROZEN
- Can't accept keyboard input
- Bad user experience ❌

**With Threading:**
- **Thread 1 (Game Thread)**: Runs game loop continuously
  - Updates snake position
  - Checks collisions
  - Spawns food
  - Updates score
  
- **Thread 2 (Main UI Thread)**: Handles GUI
  - Accepts keyboard input
  - Redraws graphics
  - Window stays responsive

Both threads run SIMULTANEOUSLY, so:
- Game updates smoothly ✓
- Window responds instantly to key presses ✓
- No freezing ✓"

---

### **2. How Did You Create and Start a Thread?**

**Question**: "Show me in your code how you created and started a thread."

**Answer**:

"In the `initGame()` method:

```java
gameThread = new Thread(this);  // Create thread
gameThread.start();              // Start the thread
```

This works because:
1. `GameBoard implements Runnable` - makes it runnable
2. `new Thread(this)` - creates thread with GameBoard as Runnable
3. `gameThread.start()` - starts thread, which calls run() method
4. `run()` method has the game loop that executes in background

The `run()` method has:
```java
@Override
public void run() {
    while (running) {
        update();                                    // Game logic
        SwingUtilities.invokeLater(() -> repaint()); // Draw
        Thread.sleep(150);                           // Control speed
    }
}
```

This loop runs approximately every 150 milliseconds, giving ~6-7 updates per second."

---

### **3. What is the Game Loop?**

**Question**: "Explain what the game loop does and why it needs to repeat."

**Answer**:

"The game loop is an infinite while loop that repeats these steps:

1. **update()** - Update game state
   - Read keyboard input (nextDirection)
   - Move snake head
   - Check collisions (walls, self, food)
   - Update score if food eaten
   - Spawn new food

2. **repaint()** - Redraw screen
   - Clear previous frame
   - Draw food (red square)
   - Draw snake (green squares)
   - Draw score/level text

3. **Thread.sleep(150)** - Control speed
   - Pause for 150 milliseconds
   - Without this: game would run too fast

Why repeat?
- Game is continuous animation
- Need to update position every frame
- Need to redraw every frame
- Creates illusion of movement (like movies are 24 FPS)

Speed = 1000ms / 150ms = ~6.7 updates per second"

---

### **4. How Does Collision Detection Work?**

**Question**: "Explain your collision detection logic."

**Answer**:

"I check for three types of collisions in the `update()` method:

1. **Wall Collision**:
   ```java
   if (newX < 0 || newX >= GRID_WIDTH || 
       newY < 0 || newY >= GRID_HEIGHT) {
       running = false;  // Game over
   }
   ```
   - newX < 0 → Hit left wall
   - newX >= 16 → Hit right wall (grid is 16 wide)
   - newY < 0 → Hit top wall
   - newY >= 15 → Hit bottom wall (grid is 15 tall)

2. **Self Collision**:
   ```java
   for (Point segment : snake) {
       if (newHead.x == segment.x && newHead.y == segment.y) {
           running = false;  // Game over
       }
   }
   ```
   - Loop through all body segments
   - Check if new head position matches any segment
   - If yes: snake hit itself → Game over

3. **Food Collision**:
   ```java
   if (newHead.x == food.x && newHead.y == food.y) {
       score += 10;
       foodEaten++;
       placeFood();
       // Don't remove tail → snake grows
   } else {
       snake.remove(snake.size() - 1);  // Remove tail
   }
   ```
   - If head position == food position
   - Increase score and count
   - Place new food
   - Keep all body segments (snake grows)
   - Else: remove tail to maintain length"

---

### **5. What Data Structure Did You Use for the Snake?**

**Question**: "Why did you use ArrayList for the snake? Why not an array?"

**Answer**:

"I used `ArrayList<Point> snake` because:

1. **Dynamic Size**: Snake grows when eating food
   - Array size is fixed
   - ArrayList grows automatically
   - Perfect for growing snake!

2. **Easy Operations**:
   ```java
   snake.add(0, newHead)  // Add head at front
   snake.remove(size-1)   // Remove tail at back
   ```
   - ArrayList has convenient methods
   - Array would require manual shifting

3. **Easy Iteration**:
   ```java
   for (Point segment : snake) {
       // Check collision with each segment
   }
   ```
   - Clean syntax for collision detection

4. **Performance**:
   - Snake is small (usually <20 segments)
   - ArrayList is fast enough
   - No performance issues

**If using Array:**
- Would need fixed size, e.g., `Point[] snake = new Point[100]`
- Would track actual length separately
- Would need manual shifting when adding/removing
- More complicated code"

---

### **6. How Did You Handle Keyboard Input?**

**Question**: "Explain how you handled keyboard input in your game."

**Answer**:

"I implemented `KeyListener` interface with three methods:

```java
public class GameBoard extends JPanel implements KeyListener {
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
            nextDirection = "UP";
        }
        // ... similar for other keys
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}  // Not needed
    
    @Override
    public void keyTyped(KeyEvent e) {}     // Not needed
}
```

Key points:

1. **Setup**:
   ```java
   setFocusable(true);      // This component can get focus
   addKeyListener(this);     // Register as listener
   ```

2. **Prevention of Backwards Movement**:
   ```java
   if (key == KeyEvent.VK_UP && !direction.equals("DOWN"))
   ```
   - Only allow UP if not currently going DOWN
   - Prevents snake from going backwards (kills player immediately)

3. **nextDirection Variable**:
   ```java
   nextDirection = "UP";    // Set when key pressed
   direction = nextDirection; // Used in update()
   ```
   - Handles rapid key presses smoothly
   - Prevents input lag

4. **Arrow Keys + WASD Support**:
   - Both arrow keys and WASD work
   - Better user experience

5. **Restart on SPACE**:
   ```java
   else if (key == KeyEvent.VK_SPACE && !running) {
       initGame();  // Restart
   }
   ```"

---

### **7. How Did You Manage Threads for GUI Safety?**

**Question**: "Why did you use SwingUtilities.invokeLater()?"

**Answer**:

"This is about **thread safety** in Swing.

**The Problem:**
```
Game Thread → repaint() ──X──→ CRASH!
Main UI Thread         (Multiple threads
                        touching GUI)
```

Swing is NOT thread-safe for multiple threads accessing GUI.
If game thread and UI thread both update graphics → CRASH!

**The Solution:**
```java
SwingUtilities.invokeLater(() -> repaint());
```

This:
1. Game thread creates a task: `() -> repaint()`
2. Adds it to Swing's event queue
3. Main UI thread picks it up and executes it
4. Guarantees only one thread modifies GUI ✓

**Result:**
```
Game Thread → SwingUtilities.invokeLater(repaint)
                     ↓
              Swing Event Queue
                     ↓
             Main UI Thread executes repaint()
             
Safe! ✓
```

Without this, the game would crash randomly due to:
- Race conditions
- Inconsistent graphics state
- Swing components not thread-safe"

---

### **8. How Did You Control Game Speed?**

**Question**: "How does your game speed work? Why does it increase with levels?"

**Answer**:

"Game speed is controlled by `Thread.sleep()`:

```java
int sleepTime = 200 - (level * 10);
if (sleepTime < 80) sleepTime = 80;
Thread.sleep(sleepTime);
```

**Explanation:**

- Level 1: sleep 200 - 10 = 190ms per update
- Level 2: sleep 200 - 20 = 180ms per update
- Level 3: sleep 200 - 30 = 170ms per update
- Level 9+: sleep 80ms (minimum, maximum speed)

**Updates Per Second:**
- Level 1: 1000ms / 190ms ≈ 5.3 updates/sec (slow)
- Level 5: 1000ms / 150ms ≈ 6.7 updates/sec (medium)
- Level 10: 1000ms / 80ms = 12.5 updates/sec (fast)

**Why Increase With Level?**
```java
if (foodEaten % 5 == 0) {
    level++;
}
```
- Every 5 foods eaten, level increases
- Game gets progressively harder
- More challenge for player
- Stays interesting

**Minimum Speed:**
```java
if (sleepTime < 80) sleepTime = 80;
```
- Game can't be too fast
- Below 80ms becomes unplayable
- Prevents infinite speedup"

---

### **9. Explain the Score and Level System**

**Question**: "How does scoring work in your game?"

**Answer**:

"The scoring system is simple but effective:

```java
// When food is eaten
if (newHead.x == food.x && newHead.y == food.y) {
    score += 10;        // Add 10 points
    foodEaten++;        // Count food
    
    if (foodEaten % 5 == 0) {
        level++;        // Level up every 5 foods
    }
    
    placeFood();        // Spawn new food
}
```

**Score Rules:**
- Each food eaten = 10 points
- Starting score = 0
- No multipliers or bonuses
- Simple and understandable

**Level System:**
- Starts at Level 1
- Increases every 5 foods eaten
  - After 5 foods → Level 2
  - After 10 foods → Level 3
  - After 15 foods → Level 4, etc.

**Display:**
```java
g.drawString(\"Score: \" + score + \"  |  Level: \" + level, 10, y);
```
- Shows in real-time at bottom of screen
- Updates immediately when food eaten

**Why This Design?**
- Easy to understand
- Provides progression
- Encourages longer playtime
- Difficulty increases naturally"

---

### **10. What Happens When Game Ends?**

**Question**: "How do you detect and display game over?"

**Answer**:

"Game over is triggered when collisions occur:

```java
if (newX < 0 || newX >= GRID_WIDTH || 
    newY < 0 || newY >= GRID_HEIGHT) {
    running = false;  // Hit wall
    return;
}

for (Point segment : snake) {
    if (newHead.x == segment.x && newHead.y == segment.y) {
        running = false;  // Hit self
        return;
    }
}
```

**When running becomes false:**

1. Game loop exits:
   ```java
   while (running) {  // Exit when false
       // ...
   }
   ```

2. In paintComponent(), displays game over screen:
   ```java
   if (running) {
       // Draw normal game
   } else {
       // Draw game over screen
       g.drawString(\"GAME OVER!\", ...);
       g.drawString(\"Final Score: \" + score, ...);
       g.drawString(\"Press SPACE to restart\", ...);
   }
   ```

3. Displays:
   - Game over message
   - Final score
   - Final level
   - Snake length achieved
   - Instructions to restart

4. **Restart:**
   ```java
   else if (key == KeyEvent.VK_SPACE && !running) {
       initGame();  // Reset everything
   }
   ```
   - Player presses SPACE
   - Calls initGame() to reset
   - Creates new snake, new food
   - Starts new thread
   - Game restarts fresh"

---

### **11. What Classes Did You Create and Why?**

**Question**: "Explain the relationship between your classes."

**Answer**:

"I created 3 classes:

1. **SnakeGame.java** (Window)
   - Extends JFrame
   - Creates the main window
   - Adds GameBoard to it
   - Entry point (main method)
   
2. **GameBoard.java** (Game Logic & Graphics)
   - Extends JPanel (drawing surface)
   - Implements Runnable (for threading)
   - Implements KeyListener (for input)
   - Contains:
     - All game logic
     - Game loop (run method)
     - Drawing code (paintComponent)
     - Input handling
     - Collision detection
     - Scoring

3. **Point.java** (Data Class)
   - Stores x, y coordinates
   - Used for:
     - Snake body segments
     - Food location

**Relationships:**
```
SnakeGame (JFrame)
    └─ contains
       GameBoard (JPanel + Runnable + KeyListener)
           └─ contains ArrayList of
              Point (coordinate data)
```

**Why This Structure?**
- **Separation of Concerns**: Each class has one job
- **SnakeGame**: Just creates window
- **GameBoard**: All game logic and drawing
- **Point**: Simple data container
- Easy to understand and maintain"

---

### **12. What Are the Key Methods?**

**Question**: "List and explain the key methods in your code."

**Answer**:

"Major methods in GameBoard:

1. **Constructor**: `GameBoard()`
   - Sets up the board
   - Adds keyboard listener
   - Calls initGame()

2. **initGame()**: Initialize game
   - Creates snake with 4 segments
   - Spawns food
   - Resets score, level, foodEaten
   - Creates and starts game thread
   - Called at start and restart

3. **run()**: THE GAME LOOP (runs on thread)
   ```java
   while(running) {
       update();
       repaint();
       Thread.sleep(150);
   }
   ```
   - Executed by game thread
   - Runs continuously until game over

4. **update()**: Game logic
   - Reads direction
   - Calculates new head position
   - Checks all collisions
   - Handles food eating
   - Updates score

5. **paintComponent(Graphics g)**: Drawing
   - Clears screen
   - Draws food
   - Draws snake
   - Draws score/level
   - Draws game over screen

6. **placeFood()**: Spawn food
   - Random position
   - Checks not on snake
   - Creates new food point

7. **keyPressed(KeyEvent e)**: Handle input
   - Detects arrow keys/WASD
   - Updates nextDirection
   - Detects SPACE for restart

8. **keyReleased(), keyTyped()**: Empty
   - Required by interface
   - Not needed for this game"

---

### **13. How Would You Improve the Game?**

**Question**: "What features could you add in the future?"

**Answer**:

"Possible enhancements:

1. **Power-ups**
   - Speed boost (temporary)
   - Invincibility (temporary)
   - Double points

2. **Obstacles**
   - Static walls on board
   - Moving obstacles
   - More challenging gameplay

3. **Sound Effects**
   - Food eaten sound
   - Game over sound
   - Background music

4. **Difficulty Modes**
   - Easy/Medium/Hard
   - Different starting speeds
   - Different grid sizes

5. **High Score Tracking**
   - Save to file
   - Display previous high scores
   - Leaderboard

6. **Better Graphics**
   - Images instead of colored squares
   - Smooth animation
   - Better colors/themes

7. **Multiplayer**
   - Two snakes competing
   - Network play using sockets
   - Turn-based gameplay

8. **Better AI** (for Pac-Man version)
   - Pathfinding algorithms
   - Intelligent ghost behavior
   - Difficulty progression"

---

### **14. What Problems Did You Face?**

**Question**: "What challenges did you encounter and how did you solve them?"

**Answer**:

"Key challenges and solutions:

1. **Thread Safety**
   - Problem: Game thread and UI thread both modifying GUI → crashes
   - Solution: Used SwingUtilities.invokeLater() for safe updates

2. **Input Lag**
   - Problem: Rapid key presses felt unresponsive
   - Solution: Used `nextDirection` variable to buffer input

3. **Snake Moving Too Fast**
   - Problem: Game updates faster than visible
   - Solution: Added Thread.sleep() to control speed

4. **Snake Going Backwards**
   - Problem: Can press opposite direction and die immediately
   - Solution: Checked current direction before allowing new direction

5. **Food Spawning on Snake**
   - Problem: Food could appear inside snake
   - Solution: Added loop to regenerate food if on snake

6. **Window Freezing**
   - Problem: Game logic blocking keyboard input
   - Solution: Used threading to separate concerns

7. **Screen Flicker**
   - Problem: Rapid redrawing causes flicker
   - Solution: Swing's paintComponent() handles double-buffering automatically"

---

### **15. What Did You Learn?**

**Question**: "What key concepts did this project teach you?"

**Answer**:

"This project taught me:

1. **Multi-threading**
   - Creating threads with Runnable
   - Starting threads with start()
   - Thread.sleep() for timing
   - Thread-safe GUI updates

2. **Swing GUI**
   - JFrame, JPanel components
   - Graphics drawing
   - Layout and sizing
   - Event handling

3. **Game Development**
   - Game loop concept
   - Collision detection
   - State management
   - Animation basics

4. **Data Structures**
   - ArrayList for dynamic lists
   - When to use ArrayList vs Array
   - Efficient operations on collections

5. **Event-Driven Programming**
   - KeyListener interface
   - Event handling patterns
   - Responsive UI design

6. **Object-Oriented Programming**
   - Class design
   - Interfaces and inheritance
   - Separation of concerns
   - Code organization

7. **Debugging & Problem-Solving**
   - Identifying threading issues
   - Testing collision logic
   - Optimizing performance

8. **Computer Architecture Concepts**
   - Concurrent execution
   - Race conditions
   - Thread synchronization
   - CPU scheduling"

---

## Quick Tips for Your Viva

✓ **Understand Threading Deeply**
- Why it's needed (responsive GUI)
- How it works (separate execution)
- How it's done (Thread, Runnable, start())

✓ **Know Your Code**
- Be able to explain every method
- Explain the game loop clearly
- Show collision detection logic

✓ **Speak Clearly**
- Don't rush
- Use examples
- Draw diagrams if needed

✓ **Be Ready to Modify**
- "Can you change X?"
- Show you understand the code
- Make small changes confidently

✓ **Admit Limitations**
- "I could improve X by..."
- Shows understanding
- Professors appreciate honesty

✓ **Answer Directly**
- Don't ramble
- Answer the question asked
- Then elaborate if needed

Good Luck! 🚀
