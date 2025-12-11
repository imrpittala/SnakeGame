# Architecture & Threading Diagram

## File Structure
```
SnakeGame Project/
├── SnakeGame.java          (Creates the window)
├── GameBoard.java          (Game logic & threading)
├── Point.java              (Data class)
└── README.md               (Instructions)
```

## Class Relationships
```
┌─────────────────────────┐
│      SnakeGame          │
│    (extends JFrame)     │
└────────────┬────────────┘
             │
             │ contains
             │
┌────────────▼────────────────┐
│       GameBoard             │
│  (extends JPanel            │
│   implements Runnable       │
│   implements KeyListener)   │
└──────────────┬──────────────┘
               │
               │ contains ArrayList of
               │
       ┌───────▼────────┐
       │    Point       │
       │   (x, y)       │
       └────────────────┘
```

## Threading Flow
```
Program Start
    │
    ▼
main() in SnakeGame.java
    │
    ▼
new SnakeGame() - Creates Window
    │
    ▼
new GameBoard() - Creates Game Board
    │
    ▼
initGame() - Initializes game
    │
    ┌─────────────────────────────────────┐
    │                                     │
    ▼                                     ▼
[GAME THREAD]                      [MAIN UI THREAD]
    │                                     │
    ├─ gameThread.start()                │
    │                                     │
    ▼ (Runs in Background)       (Handles User Input)
    │                                     │
    ├─ run() method loop                 │
    │   - update()                        │
    │   - repaint()                       │
    │   - Thread.sleep()                 │
    │   (Every 150ms)                     │
    │                                     │
    └─────────────────┬───────────────────┘
                      │
                Both threads update
                GameBoard variables
                (SYNCHRONIZED!)
```

## What Happens Each Game Cycle

```
┌──────────────────────────────────────────────┐
│         GAME THREAD - Game Loop              │
└──────────────────────────────────────────────┘

1. update()
   ├─ Check direction from keyboard
   ├─ Calculate new head position
   ├─ Check collision with walls
   ├─ Check collision with snake body
   ├─ Add new head to snake
   ├─ Check if food eaten
   │  ├─ YES: score += 10, foodEaten++, placeFood()
   │  └─ NO: remove tail
   └─ If collision: running = false

2. repaint()
   └─ Calls paintComponent(Graphics g)
      ├─ Draw food (red square)
      ├─ Draw snake (green squares)
      └─ Draw score/level text

3. Thread.sleep(150)
   └─ Wait 150 milliseconds
      (Controls game speed)

4. Repeat (back to step 1)

┌──────────────────────────────────────────────┐
│    MAIN UI THREAD - Keyboard Input           │
└──────────────────────────────────────────────┘

- keyPressed(KeyEvent e)
  ├─ If UP/W key: nextDirection = "UP"
  ├─ If DOWN/S key: nextDirection = "DOWN"
  ├─ If LEFT/A key: nextDirection = "LEFT"
  ├─ If RIGHT/D key: nextDirection = "RIGHT"
  └─ If SPACE: initGame() (restart)
  
  (This updates nextDirection variable
   that game thread reads!)
```

## Data Flow Diagram
```
Snake Game Execution Flow:

START
  │
  ▼
Create SnakeGame Window (JFrame)
  │
  ▼
Create GameBoard (JPanel)
  │
  ▼
Initialize Game (initGame)
  │  ├─ Create snake ArrayList
  │  ├─ Create food at random
  │  └─ Start game thread
  │
  ▼
GAME RUNNING
  │
  ├─ [GAME THREAD]                    [MAIN UI THREAD]
  │   │                                    │
  │   ├─ Loop: while(running)              ├─ Wait for key press
  │   │   ├─ update()                      │
  │   │   │   ├─ Move snake                ├─ keyPressed event
  │   │   │   ├─ Check collisions          │   └─ Update direction
  │   │   │   └─ Update score              │
  │   │   │                                │
  │   │   ├─ repaint()                     │
  │   │   │   └─ Call paintComponent       │
  │   │   │       └─ Draw graphics         │
  │   │   │                                │
  │   │   └─ Thread.sleep(150ms)           │
  │   │                                    │
  │   └─ If collision: running = false     │
  │                                        │
  └─ Show Game Over Screen
       ├─ Wait for SPACE key press
       └─ Call initGame() to restart
```

## Threading Safety - SwingUtilities.invokeLater()
```
Why do we use SwingUtilities.invokeLater()?

Problem:
  Game Thread (background)
         │
         ▼
    repaint() ──────X──── NOT SAFE!
         │
  Main UI Thread (GUI)
  
  Multiple threads accessing GUI = CRASH!

Solution:
  Game Thread (background)
         │
         ▼
    SwingUtilities.invokeLater(() -> repaint())
         │
         ▼
    Queue repaint() to Main UI Thread
         │
  Main UI Thread executes repaint() safely
  
  Only ONE thread updates GUI = SAFE!
```

## Key Methods Breakdown

```
SnakeGame.java:
├─ Constructor: Creates window, adds GameBoard
└─ main(): Entry point

GameBoard.java:
├─ Constructor: Initialize board, start thread
├─ initGame(): Reset game state, start thread
├─ run(): GAME LOOP (runs on separate thread)
├─ update(): Game logic (move, collisions, score)
├─ paintComponent(): Draw everything
├─ placeFood(): Random food location
└─ Key handlers: keyPressed(), keyReleased(), keyTyped()

Point.java:
└─ Constructor: Store x, y coordinates
```

## Thread Lifecycle
```
Program Start
    │
    ▼
new Thread(gameBoard) → gameThread created (state: NEW)
    │
    ▼
gameThread.start() → Transitions to RUNNABLE state
    │
    ▼
JVM scheduler picks it → Transitions to RUNNING state
    │
    ▼
run() method executes (infinite while loop)
    │
    ├─ while(running) { ... }
    │
    ▼
Collision detected → running = false
    │
    ▼
while loop ends → run() returns
    │
    ▼
Thread terminates → state: TERMINATED
    │
    ▼
User presses SPACE → initGame() called again
    │
    ▼
new Thread created and started (cycle repeats)
```

## How Multiple Threads Work

```
TIME ──────────────────────────────────────────>

GAME THREAD:    [update] [repaint] [sleep] [update] [repaint] [sleep]
                   ▲         ▲       ▲        ▲        ▲       ▲
                   └────────────────────────────────────────────┘
                          150 milliseconds

MAIN UI THREAD:  [idle] [keyPress] [idle] [keyPress] [idle] ...
                        ▲                 ▲
                        └─────────────────┘
                     User presses key


RESULT: Both threads run CONCURRENTLY!
        Game updates smoothly while accepting input!
```

## Collision Detection Logic
```
Check Collisions in update():

1. Get head position
   head = snake.get(0)

2. Calculate new head position
   newX = head.x + direction
   newY = head.y + direction

3. Check WALL collision
   if (newX < 0 || newX >= GRID_WIDTH || 
       newY < 0 || newY >= GRID_HEIGHT)
       running = false  // Hit wall!

4. Check SELF collision
   for (Point segment : snake)
       if (segment == newHead)
           running = false  // Hit self!

5. If no collision:
   snake.add(0, newHead)  // Add new head
   
   If food at newHead:
       score += 10
       placeFood()
       // Don't remove tail (snake grows)
   Else:
       snake.remove(last)  // Remove tail
       // Maintain length
```

## Variable Scope & Thread Safety

```
SHARED VARIABLES (accessed by both threads):
- snake (ArrayList)           ← Game thread modifies, UI thread reads
- direction (String)          ← UI thread sets, game thread reads
- nextDirection (String)      ← UI thread sets, game thread reads
- food (Point)                ← Game thread sets, UI thread reads
- score (int)                 ← Game thread modifies, UI thread reads
- running (boolean)           ← Game thread sets/checks

IMPORTANT:
✓ We use volatile keyword where appropriate
✓ We use SwingUtilities.invokeLater() for GUI updates
✓ No locks needed because updates are simple assignments
✓ Game thread reads direction, UI thread writes it
  (Works fine because String is thread-safe in this context)
```

This architecture demonstrates your understanding of:
✓ Object-Oriented Programming (Classes, inheritance)
✓ Multi-threading (Separate game loop)
✓ GUI Programming (Swing components)
✓ Event Handling (Keyboard input)
✓ Game Logic (Collision detection, scoring)
