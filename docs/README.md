# Multi-Threaded Snake Game for BTech Project

## How to Compile and Run

### Step 1: Save All Files
Save these 3 Java files in the **same folder**:
- `SnakeGame.java`
- `GameBoard.java`
- `Point.java`

### Step 2: Open Command Prompt/Terminal
Navigate to the folder where you saved the files.

### Step 3: Compile All Files
Type this command:
```
javac SnakeGame.java GameBoard.java Point.java
```

You should see no errors. If successful, 3 new `.class` files will be created.

### Step 4: Run the Game
Type this command:
```
java SnakeGame
```

A window will open with your Snake game!

## How to Play

### Controls
- **Arrow Keys** - Move the snake (UP, DOWN, LEFT, RIGHT)
- **W, A, S, D Keys** - Alternative movement controls
- **SPACE** - Restart game after game over

### Game Rules
- Eat red food squares to grow and gain points
- Don't hit the walls or yourself
- Each food eaten = 10 points
- Level increases every 5 foods eaten
- Game gets faster as you level up
- Score, level, and snake length display at bottom

## Game Features

✓ **Multi-Threading**: Game loop runs on separate thread from GUI
✓ **Smooth Animation**: 60+ FPS with thread-based updates
✓ **Score System**: Points increase as you eat food
✓ **Level System**: Difficulty increases with levels
✓ **Smooth Controls**: Responsive keyboard input
✓ **Game Over Screen**: Shows final score and stats
✓ **Restart Ability**: Press SPACE to play again

## How It Uses Threading

The game uses **multi-threading** in these ways:

1. **Game Loop Thread**: 
   - Runs continuously in `run()` method
   - Updates snake position
   - Checks collisions
   - Spawns food
   - All happens without freezing the window

2. **Main UI Thread**:
   - Handles window events
   - Processes keyboard input
   - Redraws the screen
   - These work **simultaneously** with game logic!

3. **Thread Safety**:
   - Uses `SwingUtilities.invokeLater()` to safely update GUI from game thread
   - This prevents crashes when multiple threads access graphics

## Code Structure

```
SnakeGame.java (Main Window)
    ├─ Creates JFrame window
    └─ Adds GameBoard to it

GameBoard.java (Game Logic & Graphics)
    ├─ Implements Runnable (for threading)
    ├─ Implements KeyListener (for keyboard input)
    ├─ run() method - Game loop thread
    ├─ update() method - Game logic
    ├─ paintComponent() method - Drawing
    └─ Key event methods - Handle arrow keys

Point.java (Data Class)
    └─ Stores x, y coordinates for snake segments
```

## For Your BTech Project Report

### What to Document

1. **System Design**
   - Class diagram showing relationships
   - Thread diagram showing game thread + UI thread
   - Game state flow diagram

2. **Threading Explanation**
   - Why threading is used (responsive GUI)
   - How the game loop thread works
   - How keyboard input and game updates work concurrently
   - Thread synchronization methods used

3. **Game Logic**
   - Collision detection algorithm
   - Food placement logic
   - Score and level system
   - Snake movement mechanism

4. **Testing Results**
   - Screenshots of gameplay
   - Game over screen
   - Performance metrics (FPS, response time)

5. **Implementation Details**
   - Key methods explained
   - ArrayList usage for snake body
   - Swing components used
   - Event handling mechanism

## Troubleshooting

**Problem**: "Command not found: javac"
- **Solution**: Java JDK not installed. Download from oracle.com

**Problem**: Window opens but nothing appears
- **Solution**: Try clicking inside the window first to give it focus

**Problem**: Snake doesn't move
- **Solution**: Click inside the game window, then press arrow keys

**Problem**: "FileNotFoundException" when running
- **Solution**: Make sure all 3 .java files are in the same folder

## Features You Can Add (Optional)

1. **High Score Storage**
   - Save best scores to a file
   - Display high scores at game over

2. **Obstacles**
   - Add walls/obstacles on the board
   - Make game more challenging

3. **Power-ups**
   - Speed boost
   - Temporary invincibility
   - Freeze time

4. **Difficulty Modes**
   - Easy/Medium/Hard selection
   - Different grid sizes
   - Different starting speeds

5. **Sound Effects**
   - Sound when eating food
   - Background music
   - Game over sound

## Good Luck!

This is a complete, working game ready for your BTech project submission.
Make sure to understand each part and be able to explain the threading concepts during your viva!
