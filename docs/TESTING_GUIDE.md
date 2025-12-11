# Testing & Debugging Guide

## ✅ Comprehensive Testing Checklist

### Before Submission, Test All of These:

---

## **1. Compilation Testing**

### ✓ Test 1.1: All Files Compile
```bash
javac SnakeGame.java
javac GameBoard.java  
javac Point.java
```
**Expected**: No errors, 3 .class files created
**If fails**: Check imports and syntax

---

### ✓ Test 1.2: Compile All Together
```bash
javac *.java
```
**Expected**: No errors, all compile together

---

## **2. Startup Testing**

### ✓ Test 2.1: Program Launches
```bash
java SnakeGame
```
**Expected**: Window opens with title "Multi-Threaded Snake Game"
**If fails**: Check main() method and constructor

---

### ✓ Test 2.2: Window Properties
- Window is 400×420 pixels ✓
- Title is visible ✓
- Close button works ✓
- Window doesn't resize ✓
- Window centered on screen ✓

---

### ✓ Test 2.3: Initial State
- Black game board visible ✓
- Green snake visible (4 segments) ✓
- Red food visible ✓
- Score shows 0 ✓
- Level shows 1 ✓

---

## **3. Movement Testing**

### ✓ Test 3.1: Arrow Key Controls
| Key | Expected | Check |
|-----|----------|-------|
| ↑ (UP) | Snake moves up | ✓ |
| ↓ (DOWN) | Snake moves down | ✓ |
| ← (LEFT) | Snake moves left | ✓ |
| → (RIGHT) | Snake moves right | ✓ |

---

### ✓ Test 3.2: WASD Controls
| Key | Expected | Check |
|-----|----------|-------|
| W | Snake moves up | ✓ |
| A | Snake moves left | ✓ |
| S | Snake moves down | ✓ |
| D | Snake moves right | ✓ |

---

### ✓ Test 3.3: Smooth Movement
- Snake moves continuously without stopping ✓
- Movement is smooth (no jitter) ✓
- Snake doesn't skip positions ✓
- Speed is consistent ✓

---

### ✓ Test 3.4: Prevent Backward Movement
1. Move right (→)
2. Immediately press left (←)
3. Expected: Snake continues right, doesn't go left ✓

**Repeat for all directions** - snake shouldn't reverse into itself

---

## **4. Collision Testing**

### ✓ Test 4.1: Wall Collision
1. Move snake toward left wall
2. Let it hit the wall
3. Expected: Game over ✓

**Repeat for**: right wall, top wall, bottom wall

---

### ✓ Test 4.2: Self Collision
1. Create a small loop (snake hits itself)
2. Expected: Game over ✓

**Test case**: 
- Move right 3 times, up 1, left 4, down 1
- Snake should hit itself

---

### ✓ Test 4.3: Food Collision
1. Move snake head to food position
2. Expected:
   - Score increases by 10 ✓
   - New food appears ✓
   - Snake length increases ✓
   - No game over ✓

---

## **5. Scoring Testing**

### ✓ Test 5.1: Score Calculation
- Start with score = 0
- Eat 1 food → score = 10 ✓
- Eat 2nd food → score = 20 ✓
- Eat 3rd food → score = 30 ✓

**Pattern**: Each food = 10 points

---

### ✓ Test 5.2: Food Counter
- Eat 1 food → foodEaten = 1 ✓
- Eat 5 foods → foodEaten = 5 ✓
- Eat 10 foods → foodEaten = 10 ✓

---

## **6. Level Testing**

### ✓ Test 6.1: Level Progression
- Start at Level 1 ✓
- Eat 5 foods → Level 2 ✓
- Eat 10 foods → Level 3 ✓
- Eat 15 foods → Level 4 ✓

**Pattern**: Level = 1 + (foodEaten / 5)

---

### ✓ Test 6.2: Speed Increase
1. Note speed with Level 1
2. Eat 5 foods (Level 2)
3. Snake should move noticeably faster ✓
4. Repeat for Level 3, 4, etc.

---

## **7. Game Over Testing**

### ✓ Test 7.1: Game Over Screen
When game ends:
- Game loop stops ✓
- "GAME OVER!" message displays ✓
- Final score shows ✓
- Final level shows ✓
- "Press SPACE to restart" message shows ✓

---

### ✓ Test 7.2: Game Over Accuracy
- Crash into wall → Game over ✓
- Hit yourself → Game over ✓
- No food eaten → Game over ✓

---

## **8. Restart Testing**

### ✓ Test 8.1: Restart Functionality
1. Play until game over
2. Press SPACE
3. Expected:
   - Snake resets to starting position ✓
   - Score resets to 0 ✓
   - Level resets to 1 ✓
   - Food spawns randomly ✓
   - Game loop restarts ✓

---

### ✓ Test 8.2: Multiple Restarts
1. Play game
2. Game over
3. Press SPACE to restart
4. Play again
5. Game over
6. Press SPACE again
7. Should restart successfully each time ✓

---

## **9. Food Spawning Testing**

### ✓ Test 9.1: Random Food Location
- Play several games
- Food should spawn at different locations each time ✓
- Food should NOT spawn on snake ✓

---

### ✓ Test 9.2: Food After Eating
1. Eat a food
2. New food should appear immediately ✓
3. New food at random location ✓
4. New food not on snake ✓

---

## **10. Performance Testing**

### ✓ Test 10.1: No Lag
- Window stays responsive ✓
- Keyboard input is instant ✓
- No delays in movement ✓
- Smooth animation ✓

---

### ✓ Test 10.2: CPU Usage
- Game runs smoothly without high CPU usage
- No unnecessary loops or operations

---

### ✓ Test 10.3: Memory
- Game doesn't use excessive memory
- No memory leaks after extended play

---

## **11. Thread Safety Testing**

### ✓ Test 11.1: No Crashes
- Play for extended period (5+ minutes)
- No crashes or exceptions ✓
- No flickering or visual glitches ✓

---

### ✓ Test 11.2: GUI Responsiveness
- While game is running, window responds instantly ✓
- Can move window around screen ✓
- Can minimize and restore ✓

---

## **12. Edge Cases**

### ✓ Test 12.1: Snake at Grid Boundary
```
Snake at (0,0)
Move LEFT → Wall collision ✓
Move UP → Wall collision ✓
```

---

### ✓ Test 12.2: Food at Boundary
- Food should spawn on boundary squares ✓
- Food should be reachable ✓

---

### ✓ Test 12.3: Very Long Snake
1. Eat 30+ foods (get very long snake)
2. Game should still work correctly ✓
3. No slowdown ✓
4. Collision detection still accurate ✓

---

### ✓ Test 12.4: High Score
1. Eat many foods, reach high score (100+)
2. Score display should show correctly ✓
3. High level should work ✓
4. Game speed maxes out ✓

---

## **Debugging Tips**

### If Game Doesn't Compile:

```java
// Check 1: Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Check 2: Class declarations
public class GameBoard extends JPanel implements Runnable, KeyListener

// Check 3: Method signatures
@Override
public void run() { ... }

// Check 4: Bracket matching
// Every { must have corresponding }
```

---

### If Game Doesn't Run:

```java
// Issue: "NoClassDefFoundError"
// Solution: Make sure all 3 .class files are in same directory

// Issue: Window appears but nothing happens
// Solution: 
// 1. Click inside window
// 2. Press arrow key
// 3. Game should start

// Issue: NullPointerException
// Solution: Check that all objects are initialized in constructor
```

---

### If Movement is Slow:

```java
// Check Thread.sleep() value
Thread.sleep(150);  // Should be around 150-200ms

// Too high (>300): Game too slow
// Too low (<50): Game too fast, might freeze

// Increase level speed calculation:
int sleepTime = 200 - (level * 10);
if (sleepTime < 80) sleepTime = 80;
```

---

### If Snake Doesn't Respond to Keys:

```java
// Check 1: setFocusable(true)
setFocusable(true);  // Must be true

// Check 2: addKeyListener(this)
addKeyListener(this);  // Must be added

// Check 3: Click window first
// Must click window to give it focus

// Check 4: keyPressed() method
public void keyPressed(KeyEvent e) {
    // Check if method is called
    System.out.println("Key pressed: " + e.getKeyCode());
}
```

---

### If Collisions Not Detected:

```java
// Check wall collision logic
if (newX < 0 || newX >= GRID_WIDTH || 
    newY < 0 || newY >= GRID_HEIGHT) {
    running = false;
}

// Check self collision logic
for (Point segment : snake) {
    if (newHead.x == segment.x && newHead.y == segment.y) {
        running = false;
    }
}

// Check food collision logic
if (newHead.x == food.x && newHead.y == food.y) {
    score += 10;
}
```

---

### If Threads Crash:

```java
// Issue: Random crashes, flickering
// Solution: Check SwingUtilities.invokeLater()

SwingUtilities.invokeLater(() -> repaint());
// NOT: repaint();  // Direct call can crash!

// Issue: Game freezes suddenly
// Solution: Check Thread.sleep() isn't too high
// Keep it 80-200ms range
```

---

## **System Test Cases**

### Test Case 1: Happy Path
```
1. Start game
2. Move right 5 times
3. Move up 3 times
4. Move left to eat food
5. Score increases
6. Move around
7. Eat 5 foods (level up)
8. Hit wall
9. Game over
10. Press SPACE, restart
Result: All steps work ✓
```

---

### Test Case 2: Stress Test
```
1. Start game
2. Play for 10 minutes continuously
3. Eat 50+ foods
4. Reach Level 10+
5. Game should not crash
6. No memory issues
7. Still responsive to input
Result: Game handles stress ✓
```

---

### Test Case 3: Edge Case
```
1. Immediately move into wall
2. Try backward movement
3. Eat food at boundary
4. Very long snake collision
5. Rapid key presses
6. Window resizing attempts
Result: All edge cases handled ✓
```

---

## **Final Submission Checklist**

Before submitting to professor:

- [ ] All 3 Java files compile without errors
- [ ] Program runs with `java SnakeGame`
- [ ] All movement keys work (arrow + WASD)
- [ ] Snake moves smoothly
- [ ] Collisions detected correctly
- [ ] Score increases when eating food
- [ ] Level increases every 5 foods
- [ ] Speed increases with level
- [ ] Game over screen displays
- [ ] Can restart with SPACE
- [ ] No crashes or exceptions
- [ ] Window stays responsive
- [ ] Can play for extended time
- [ ] All edge cases handled

---

## **Common Issues & Quick Fixes**

| Issue | Quick Fix |
|-------|-----------|
| Files not found | All 3 .java in same folder |
| Compilation error | Check class declarations and imports |
| Game won't start | Run from correct directory |
| Snake doesn't move | Click window first, then press key |
| Slow movement | Reduce Thread.sleep() value |
| Crashes | Check SwingUtilities.invokeLater() usage |
| Collision not working | Review collision detection logic |
| Game freezes | Check Thread.sleep() timeout |
| Food stuck | Check placeFood() logic |
| Level doesn't increase | Check foodEaten counter |

---

## **Performance Benchmarks**

Good performance targets:

- **FPS**: 6-7 updates per second (smooth)
- **Input Lag**: <100ms (instant)
- **CPU**: <5% usage (efficient)
- **Memory**: <50MB (lean)
- **Responsiveness**: Instant window response
- **Stability**: 0 crashes over 30 minutes

---

If all these tests pass, your game is ready for submission! ✅
