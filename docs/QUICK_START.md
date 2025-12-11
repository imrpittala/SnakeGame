# Quick Start Guide - Snake Game

## 3-Minute Setup

### What You Have
- SnakeGame.java (Window)
- GameBoard.java (Game Logic & Threading)
- Point.java (Data class)

### Step 1: Create a Folder
Create a new folder called `SnakeGame` on your desktop

### Step 2: Save the 3 Files
Copy and paste the 3 Java files into this folder

### Step 3: Open Command Prompt
- **Windows**: Press Windows key + R, type `cmd`, hit Enter
- **Mac/Linux**: Open Terminal

### Step 4: Navigate to Folder
```
cd Desktop/SnakeGame
```

### Step 5: Compile (One-time)
```
javac SnakeGame.java GameBoard.java Point.java
```

### Step 6: Run the Game
```
java SnakeGame
```

**That's it! Your game is now running!**

---

## During Gameplay

- **Arrow Keys or WASD** = Move snake
- **SPACE** = Restart when game over

---

## What Each File Does

| File | Purpose |
|------|---------|
| `SnakeGame.java` | Creates the window (JFrame) |
| `GameBoard.java` | Contains game logic, threading, and drawing |
| `Point.java` | Simple data class for x,y positions |

---

## Key Threading Concepts in Your Game

### Without Threading (BAD)
```
Game Window Freezes While Snake Moves ❌
User Can't Control Snake While Game Updates ❌
```

### With Threading (GOOD)
```
Thread 1: Game Loop (Update snake, check collisions)
Thread 2: Main UI (Handle keyboard, draw graphics)
Both run SIMULTANEOUSLY ✓ Window stays responsive ✓
```

**In your code:**
```java
gameThread = new Thread(this);    // Create thread
gameThread.start();                // Start it
```

This runs the `run()` method in a separate thread!

---

## During Viva/Presentation - Key Points to Explain

1. **Why threading?**
   - "Game loop needs to run continuously"
   - "But window must stay responsive to keyboard input"
   - "So I used separate thread for game, main thread for GUI"

2. **How it works?**
   - "GameBoard implements Runnable"
   - "`run()` method contains game loop"
   - "`Thread.sleep(150)` controls speed"
   - "While loop updates snake, checks collisions, redraws"

3. **Key methods to point out:**
   - `run()` - The game loop
   - `update()` - Game logic (movement, collision)
   - `paintComponent()` - Drawing everything
   - `keyPressed()` - Handling input

---

## Compilation Errors? Try This

### Error: "class GameBoard not found"
- Make sure all 3 files are in the SAME folder
- Use `javac *.java` to compile all at once

### Error: "Exception in thread"
- Make sure you're running `java SnakeGame` (not GameBoard.java)

### Window opens but no game visible
- Click inside the window
- Press an arrow key to start playing

---

## Ready to Submit?

Before showing your professor:

✓ Game runs without errors
✓ Snake moves smoothly
✓ Keyboard controls work
✓ Score increases when eating food
✓ Game over happens correctly
✓ Can restart with SPACE

**You're all set!**
