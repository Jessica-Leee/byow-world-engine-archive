# BYOW — Legacy Course Snapshot

A tile-based world exploration project from **UC Berkeley CS 61B, Fall 2024**, Project 3: Build Your Own World. This repository preserves an earlier course implementation. It combines student project code with supplied rendering, utility, demonstration, and autograder-support code; individual contributions are not mapped here.

## Implemented features

- A seeded 40 × 40 world with rooms, connecting hallways, floors, and walls.
- Keyboard-controlled exploration and an English/Spanish menu.
- A switchable circular visibility radius. It is distance-based and does not account for wall occlusion.
- Save/load code for the seed and avatar position, with the known limitation below.

## Repository map

| Location | Purpose |
| --- | --- |
| [`src/core/`](src/core/) | World generation, geometry, movement, menu, and save/load code. |
| [`src/tileengine/`](src/tileengine/) | Retained course tile rendering and definitions. |
| [`src/utils/`](src/utils/), [`src/demo/`](src/demo/) | Retained course helpers and examples. |
| [`src/assets/`](src/assets/) | Project image asset. |
| [`tests/`](tests/) | Original visual checks and unfinished optional test stubs. |

## Build and run

Use JDK 17 or later and an existing copy of the course's Princeton `algs4.jar`; the dependency is not included here. Preserve its upstream license and notices. Run from the repository root on macOS or Linux:

```sh
export BYOW_ALGS4_JAR="/absolute/path/to/library-fa24/algs4.jar"
mkdir -p build/classes
find src -name '*.java' -print > build/sources.txt
javac --release 17 -cp "$BYOW_ALGS4_JAR" -d build/classes @build/sources.txt
java -cp "build/classes:$BYOW_ALGS4_JAR" core.Main
```

The entry point is `core.Main`, and the rendering library must be present on both classpaths. The game needs a graphical desktop. These steps were checked against the source and package layout, but compilation and graphical execution were not rerun for this documentation update.

## Controls

| Action | Keys |
| --- | --- |
| Start a new world | `N`, then a nonnegative integer seed within Java's `long` range, then `S` |
| Move | `W`, `A`, `S`, `D` |
| Toggle the visibility radius | `F` during exploration |
| Change menu language | `S` on the main menu |
| Load a saved world | `L` in English; `C` in Spanish |
| Quit from the English menu | `Q` |
| Save and quit during exploration | `:Q` |

The runtime writes `save.txt` in the working directory. Local saves are excluded from this archive.

## Known limits of this snapshot

- `World` uses the constructor seed for its random generator but does not retain it in the field returned by `getSeed()`. The save path therefore records the default value `0`; disk resume may reconstruct a different map. This source behavior has not been repaired in this legacy snapshot.
- The Spanish menu labels `S` as both quit and language switching. The language-switch branch takes precedence. Switch back to English and use `Q` to quit from the menu.
- The optional `AutograderBuddy.getWorldFromInput` interface is unimplemented. The original visual test depends on it, and two other course tests are placeholders. This is not a passing automated test suite.
- No new runtime, GUI, save/load, or test result is claimed by this documentation refresh.

## Course source and sharing

This project builds on the [Fall 2024 BYOW assignment](https://fa24.datastructur.es/projects/proj3/). Original course material and source notices remain applicable; retained comments in `World.java` also identify AI assistance for the visibility extension.

The [Fall 2024 course policy](https://fa24.datastructur.es/policies/#academic-misconduct) restricts publishing or distributing project solutions, including after the semester. No additional open-source license or redistribution permission is granted here. Repository visibility has not been changed by this documentation update, and no additional solution code or dependency archive has been added.
