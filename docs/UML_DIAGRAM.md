# UML Class Diagram - For Your Project Report

## ASCII UML Diagram

```
┌─────────────────────────────────────┐
│          <<interface>>              │
│          Runnable                   │
│         (java.lang)                 │
├─────────────────────────────────────┤
│ + run(): void                       │
└──────────────────▲──────────────────┘
                   │
                   │ implements
                   │
┌───────────────────────────────────────────────────────────────────────┐
│          <<interface>>                                                │
│          KeyListener                                                  │
│         (java.awt.event)                                              │
├───────────────────────────────────────────────────────────────────────┤
│ + keyPressed(KeyEvent): void                                          │
│ + keyReleased(KeyEvent): void                                         │
│ + keyTyped(KeyEvent): void                                            │
└─────────────────────────────────────┬─────────────────────────────────┘
                                      │ implements
                                      │
                ┌─────────────────────┴──────────────────────┐
                │                                            │
                │                                            │
┌───────────────▼──────────────────┐              ┌──────────▼──────────────┐
│      SnakeGame                   │              │     GameBoard           │
│  (extends JFrame)                │              │  (extends JPanel)       │
├──────────────────────────────────┤              ├─────────────────────────┤
│ - board: GameBoard               │              │ - TILE_SIZE: int        │
├──────────────────────────────────┤              │ - GRID_WIDTH: int       │
│ + SnakeGame()                    │              │ - GRID_HEIGHT: int      │
│ + main(String[]): void           │──contains──→│ - snake: ArrayList      │
│                                  │              │ - food: Point           │
│                                  │              │ - direction: String     │
│                                  │              │ - nextDirection: String │
│                                  │              │ - gameThread: Thread    │
│                                  │              │ - running: boolean      │
└──────────────────────────────────┘              │ - score: int            │
                                                  │ - level: int            │
                                                  │ - foodEaten: int        │
                                                  ├─────────────────────────┤
                                                  │ + GameBoard()           │
                                                  │ + run(): void           │
                                                  │ + initGame(): void      │
                                                  │ + update(): void        │
                                                  │ + placeFood(): void     │
                                                  │ + paintComponent(): void│
                                                  │ + keyPressed(): void    │
                                                  │ + keyReleased(): void   │
                                                  │ + keyTyped(): void      │
                                                  └────────┬────────────────┘
                                                           │
                                                  contains ArrayList of
                                                           │
                                                  ┌────────▼────────────────┐
                                                  │       Point             │
                                                  ├─────────────────────────┤
                                                  │ + x: int                │
                                                  │ + y: int                │
                                                  ├─────────────────────────┤
                                                  │ + Point(int, int)       │
                                                  │ + toString(): String    │
                                                  └─────────────────────────┘
```

---

## Class Descriptions

### 1. SnakeGame (extends JFrame)

**Purpose**: Main window container for the game

**Attributes**:
- `board: GameBoard` - The actual game panel

**Methods**:
- `SnakeGame()` - Constructor that sets up window
  - Creates GameBoard
  - Adds to window
  - Configures JFrame properties
  - Makes visible
  
- `main(String[])` - Entry point
  - Creates new SnakeGame instance
  - Starts the application

**Key Configuration**:
- Window size: 400×420 pixels
- Not resizable
- Centered on screen
- Closes on window close

---

### 2. GameBoard (extends JPanel, implements Runnable, KeyListener)

**Purpose**: Core game logic, rendering, and threading

**Attributes**:

*Game Configuration*:
- `TILE_SIZE: int` = 25 (pixel size per grid square)
- `GRID_WIDTH: int` = 16 (grid width)
- `GRID_HEIGHT: int` = 15 (grid height)

*Game State*:
- `snake: ArrayList<Point>` - Snake body segments
- `food: Point` - Food location
- `direction: String` - Current movement direction
- `nextDirection: String` - Buffered next direction
- `running: boolean` - Is game active?
- `gameThread: Thread` - Background game loop thread

*Statistics*:
- `score: int` - Player score (10 per food)
- `level: int` - Current difficulty level
- `foodEaten: int` - Total foods eaten

**Methods**:

- `GameBoard()` - Constructor
  - Sets preferred size
  - Sets background color
  - Makes focusable
  - Adds key listener
  - Calls initGame()

- `initGame()` - Initialize/reset game
  - Creates snake with 4 segments
  - Places food randomly
  - Resets all statistics
  - Creates and starts game thread

- `run()` - THE GAME LOOP (executed by thread)
  ```
  while(running) {
    update()
    repaint()
    sleep(150ms)
  }
  ```

- `update()` - Game logic (called ~6-7 times/second)
  - Read keyboard input
  - Move snake
  - Check collisions (walls, self, food)
  - Update score and level
  - Spawn food

- `paintComponent(Graphics g)` - Drawing/rendering
  - Clear screen
  - Draw food (red)
  - Draw snake (green)
  - Draw score/level
  - Draw game over screen

- `placeFood()` - Spawn new food
  - Random position
  - Ensure not on snake

- `keyPressed(KeyEvent)` - Handle key events
  - Arrow keys/WASD for movement
  - SPACE for restart

- `keyReleased(KeyEvent)` - Required by interface (empty)
- `keyTyped(KeyEvent)` - Required by interface (empty)

---

### 3. Point (Data Class)

**Purpose**: Store x,y coordinates

**Attributes**:
- `x: int` - X coordinate
- `y: int` - Y coordinate

**Methods**:
- `Point(int x, int y)` - Constructor
- `toString()` - String representation

**Usage**:
- Represents snake body segments
- Represents food location
- Stored in ArrayList for flexible list management

---

## Relationships

### Inheritance
```
JFrame
  ↑
  │ extends
  │
SnakeGame
```

```
JPanel
  ↑
  │ extends
  │
GameBoard
```

### Interfaces
```
Runnable
  ↑
  │ implements
  │
GameBoard ────────────── KeyListener
  │ implements                ↑
  │────────────────────────────
```

### Composition
```
SnakeGame
  │ contains
  └─→ GameBoard
        │ contains ArrayList of
        └─→ Point
```

---

## Design Patterns Used

### 1. **Runnable Pattern**
```java
class GameBoard implements Runnable {
    public void run() {
        // Game loop
    }
}

// Usage
gameThread = new Thread(this);
gameThread.start();
```
Allows GameBoard to run in separate thread.

### 2. **Observer Pattern** (KeyListener)
```java
class GameBoard implements KeyListener {
    public void keyPressed(KeyEvent e) {
        // Handle input
    }
}

// Usage
addKeyListener(this);
```
Subscribes to keyboard events from JPanel.

### 3. **MVC Pattern** (Partial)
- **Model**: Game state (snake, food, score)
- **View**: paintComponent() rendering
- **Controller**: keyPressed() input handling

---

## Thread Interaction Diagram

```
Thread 1: Main UI Thread
  ├─ JVM Event Dispatcher
  ├─ KeyListener.keyPressed()
  ├─ repaint() queue
  ├─ paintComponent() execution
  └─ Window refresh

        ↑↓ (Communication)

Thread 2: Game Thread
  ├─ run() method
  ├─ update() execution
  ├─ Collision detection
  ├─ Score calculation
  └─ Thread.sleep()

        ↑↓ (Shared Data)
        
Shared Variables:
  - snake (ArrayList)
  - food (Point)
  - direction (String)
  - nextDirection (String)
  - running (boolean)
  - score (int)
```

---

## Class Responsibility Matrix

| Responsibility | SnakeGame | GameBoard | Point |
|---|---|---|---|
| Create window | ✓ | - | - |
| Game logic | - | ✓ | - |
| Rendering | - | ✓ | - |
| Input handling | - | ✓ | - |
| Threading | - | ✓ | - |
| Store coordinates | - | - | ✓ |

---

## Method Call Sequence Diagram

```
Program Start:
    ├─ main()
    │   └─ new SnakeGame()
    │       ├─ new GameBoard()
    │       │   ├─ setPreferredSize()
    │       │   ├─ addKeyListener()
    │       │   └─ initGame()
    │       │       ├─ Create snake
    │       │       ├─ placeFood()
    │       │       └─ gameThread.start()
    │       │           └─ calls run()
    │       └─ add(GameBoard)
    │
    ├─ setSize()
    ├─ setDefaultCloseOperation()
    └─ setVisible(true)

Game Loop (in gameThread):
    while(running) {
        ├─ update()
        │   ├─ Check direction
        │   ├─ Move snake
        │   ├─ Check collisions
        │   └─ Update score
        │
        ├─ SwingUtilities.invokeLater(
        │       () -> repaint()
        │   )
        │
        └─ Thread.sleep(150)

Rendering (on Main UI Thread):
    repaint()
    └─ paintComponent(Graphics)
        ├─ super.paintComponent()
        ├─ Draw food
        ├─ Draw snake
        └─ Draw score

Input (on Main UI Thread):
    keyPressed(KeyEvent)
    └─ Update nextDirection
```

---

## For Your Report - How to Present This

### Suggested Presentation:

1. **Class Overview**
   - "I created 3 classes with clear separation of concerns"
   - Present the diagram
   - Explain each class's role

2. **Relationships**
   - "GameBoard implements Runnable to enable threading"
   - "GameBoard implements KeyListener to handle input"
   - "SnakeGame is the main window container"

3. **Threading**
   - Point to GameBoard extending JPanel and implementing Runnable
   - Explain how run() creates the game loop
   - Show how gameThread.start() launches it

4. **Data Flow**
   - Snake as ArrayList for dynamic size
   - Point class for coordinates
   - Shared variables between threads

5. **Design Quality**
   - Single responsibility per class
   - Clear method names
   - Proper use of interfaces
   - Thread-safe design

---

## Quick Reference for Your Viva

**Q: What does GameBoard extend?**
A: JPanel (for drawing), implements Runnable (for threading), implements KeyListener (for input)

**Q: How are snake segments stored?**
A: ArrayList<Point> - allows dynamic growth

**Q: Why is Point a separate class?**
A: Cleaner data representation, reusable for both snake and food

**Q: How do threads interact?**
A: Game thread updates state, main UI thread renders and handles input

**Q: What's the relationship between SnakeGame and GameBoard?**
A: SnakeGame (JFrame) contains GameBoard (JPanel) - composition relationship

---

This UML structure demonstrates:
✓ Good object-oriented design
✓ Proper use of inheritance and interfaces
✓ Clear separation of concerns
✓ Understanding of threading
✓ Professional code organization
