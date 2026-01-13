# Monopoly-Style Game (Academic Project)

This project is a console-based Monopoly-style board game implemented as part of my university studies in Software Engineering.  
The goal of the project was to apply data structures, object-oriented programming principles, and file-based configuration in a practical program.

---

## Project Description

The game simulates a simplified Monopoly-style board game for 2–3 players.  
Players take turns rolling a dice, moving across the board, buying fields, paying fees, and building additional structures on owned fields.  
The game ends when one player reaches the target amount of money.

The implementation strictly follows the academic requirements provided in the assignment.

---

## Implemented Features

 Turn-based gameplay for 2–3 players  
- Dice rolling and movement across the board  
- Buying fields and paying fees when landing on owned fields  
- Additional buildings on owned fields using a stack structure  
- Money management and winner calculation  
- Clear console output showing:
- current player  
- dice result  
- player position  
- money gained or lost  
- game status after each turn  

---

## Data Structures Used

This project was implemented using the required data structures:

- **Circular singly linked list** – for representing the game board  
- **Queue** – for managing player turn order  
- **Stack** – for storing additional buildings on a field  
- **Iterators** – for moving through the board instead of using indexes  

---

## Project Structure
- `src/main/java/` — main source code (game logic)
- `data/lenta.json` — board configuration
- `data/rules.txt` — game rules / notes
- `pom.xml` — Maven project configuration

This allows easy modification of the board without changing the source code.

---

## Technologies

-Java
-Maven
-JSON for data input
-Object-Oriented Programming

## Author

Gabija Šimkutė 
