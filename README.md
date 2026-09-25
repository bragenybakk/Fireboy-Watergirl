# Fireboy & Watergirl

A two-player co-op puzzle platformer written in Java. Two players share one keyboard and have to work together to get through each level: Fireboy can't touch water, Watergirl can't touch fire, and both have to reach the exit.

Built by a team of four over one semester (spring 2026) as the software engineering project in INF112 at the University of Bergen. Inspired by the original Flash game from 2009.

<!-- Add a gameplay GIF or screenshot here, for example:
![Gameplay](doc/gameplay.gif)
-->

## Features

- **Local co-op:** two players on one keyboard with separate controls
- **Three levels** loaded from plain text files, so new levels can be made without touching the code
- **Puzzle elements:** buttons, moving platforms, boost platforms, laser walls, pushable boxes (including chains of boxes) and fire/water/poison pools
- **Enemies** with simple patrol AI
- **Full menu system:** main menu, level select, settings, pause, game over and a How to Play screen
- **Music and sound effects** that can be toggled in settings
- **Swappable visual themes** through a `Theme` interface

## Tech stack

| | |
|---|---|
| Language | Java 25 |
| Rendering | Swing / `Graphics2D`, 60 FPS game loop |
| Build | Maven |
| Testing | JUnit 5, 171 tests that run headless (no display needed), JaCoCo for coverage |
| Workflow | Git, GitLab issues and merge requests with code review, Kanban |

## Architecture

The code follows a strict **Model–View–Controller** split. The layers never import each other directly and only talk through two interfaces:

- `ControllableGameModel`: what the controller is allowed to do (move, jump, navigate menus)
- `ViewableGameModel`: read-only state the view uses to draw the game

`GameModel` implements both. This made the game logic easy to unit test without a window, and let us change rendering and input handling without touching the physics.

```
inf112.fireboys
├── app                 Entry point
├── controller          Keyboard input and game loop
├── model               Game state, physics, collisions, level loading
│   ├── entity          Walls, doors, boxes, gems, pools, platforms
│   ├── enemy           Enemy AI
│   └── player          Player logic
├── coordinateSystem    Value types (Position, Board)
└── view                Rendering, sprites, audio
    └── theme           Visual themes
```

More detail (in Norwegian) in [`doc/arkitektur.md`](doc/arkitektur.md).

## My contributions

I was the team's **test lead**, responsible for test coverage, making sure the tests actually caught bugs, and keeping the whole suite runnable without a display.

On the code side I built:

- **Player 2 (Fireboy)** with its own controls, and the win condition that requires both players at the door
- **Audio:** background music and sound effects through an `AudioManager`
- **Settings screen** and the **How to Play** screen, and a redesign of all menus into one consistent style
- **Character sprites:** full-body sprites for both characters and flipping based on movement direction
- **Codebase cleanup:** Java 25 upgrade, Javadoc for all public classes and methods, and removal of dead code

## Getting started

**Requirements:** Java 25 and Maven 3.6.3+

```bash
git clone https://github.com/bragenybakk/Fireboy-Watergirl.git
cd Fireboy-Watergirl
mvn clean compile
mvn exec:java
```

Run the tests:

```bash
mvn test
```

## Controls

| Action | Watergirl | Fireboy |
|---|---|---|
| Move left | `←` | `A` |
| Move right | `→` | `D` |
| Jump | `↑` | `W` |
| Pause | `Esc` | `Esc` |

In menus: `W`/`S` or arrow keys to navigate, `Enter` or `Space` to select, `Esc` to go back.

## Team

- Brage Nybakk
- Petter Hoff
- Oscar Ruud Alvseike
- Magnus Ødegård

## Credits

Music, sprites and other assets are listed in [`doc/kilder.md`](doc/kilder.md).
