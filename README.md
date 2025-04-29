# CS61B Project 3: BYOW (Build Your Own World)

This project implements a tile-based world generator and game engine. It allows users to create and explore procedurally generated worlds with rooms, hallways, and various tile types.

## Features

- Procedural world generation with rooms and hallways
- Tile-based rendering system
- Interactive world exploration
- Save and load game states
- Customizable world parameters

## Project Structure

- `src/` - Source code directory
  - `core/` - Core game logic and world generation
    - `Main.java` - Entry point and game loop
    - `World.java` - World generation and management
    - `Room.java` - Room generation logic
    - `Hallway.java` - Hallway generation logic
    - `Point.java` - 2D point representation
    - `AutograderBuddy.java` - Autograder interface
  - `tileengine/` - Tile-based rendering system
    - `TETile.java` - Tile representation
    - `TERenderer.java` - Tile rendering engine
    - `Tileset.java` - Tile definitions
  - `utils/` - Utility classes
    - `FileUtils.java` - File I/O utilities
    - `RandomUtils.java` - Random number generation utilities
  - `assets/` - Game assets (images, etc.)
  - `demo/` - Demo implementations

## Building and Running

1. Compile the project:
```bash
javac -d out src/**/*.java
```

2. Run the game:
```bash
java -cp out Main
```

## Game Controls

- `N` - Start a new game
- `L` - Load a saved game
- `Q` - Quit the game
- Arrow keys - Move around the world
- `:Q` - Save and quit

## World Generation

The world is generated using a procedural algorithm that:
1. Creates rooms of random sizes
2. Connects rooms with hallways
3. Ensures all rooms are accessible
4. Places walls and floors appropriately


## License

This project is part of CS61B at UC Berkeley and is subject to the course's academic integrity policies. 