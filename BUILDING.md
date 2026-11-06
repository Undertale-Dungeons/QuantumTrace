# Building QuantumTrace

QuantumTrace currently targets Minecraft 26.2 on Fabric.

## Requirements

- Java 25
- Gradle 9.5.1
- Fabric Loader 0.19.3 or newer
- Fabric API 0.161.0+26.2

## Build

From the repository root, run:

```sh
gradle --no-daemon clean build
```

The mod JAR is written to `build/libs/quantumtrace-0.1.0.jar`.

A GitHub Actions build also runs on every push to `main`. Its JAR is uploaded as a workflow artifact.

## In-game

Install the JAR in the Fabric instance's `mods` folder. Press **O** to open the QuantumTrace settings screen.

This first build is a settings prototype. It persists the selected visual mode and preset, but does not yet change Minecraft's rendering pipeline. The RTX-style toggle is a placeholder and does not enable ray tracing.
