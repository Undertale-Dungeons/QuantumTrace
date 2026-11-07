# QuantumTrace

**A performance-focused visual overhaul for Minecraft.**

QuantumTrace is an ambitious shader project for Minecraft **26.2**, designed to make the game look better without losing the visual identity that makes Minecraft feel like Minecraft.

The project begins with **Vanilla Remade**: improved lighting, shadows, atmosphere, and water that preserve the original game's style. A separate **Realism** experience is planned for players who want a more physically inspired look.

> **Development status:** Early Fabric client build. The settings screen and several built-in Minecraft graphics controls are implemented. QuantumTrace's custom post-processing shaders, custom glint renderer, and ray-tracing features are still planned work.

---

## Current build

The current development build includes:

- Open the QuantumTrace control center with **O**.
- Interface themes: Vanilla, Metal Gear, 2D Minecraft, and Lemons Eats Limes.
- Potato, Low, Medium, High, and Ultra presets that update selected vanilla graphics options.
- Direct switches for ambient occlusion, cutout leaves, improved transparency, entity shadows, and vignette.
- An **Apply Vanilla Maxxing** action that enables those visual options and increases biome blending.
- A hidden Peabert mascot option, unlocked by clicking the menu title seven times.

These controls change Minecraft's existing graphics options. They are not custom QuantumTrace shaders. The New Glint control is visible but disabled until its renderer exists.

## Vision

QuantumTrace aims to bring together three priorities:

- **Visual quality** — richer lighting, convincing shadows, and more atmospheric worlds.
- **Performance** — scalable effects, sensible defaults, and quality controls for a wide range of hardware.
- **Player choice** — from lightweight Potato settings to demanding Ultra settings.

The goal is not to make every system run the most expensive effects. It is to give players control over the visual features that matter most to them.

## Rendering experiences

### Vanilla Remade

An enhanced take on Minecraft's familiar visual style. The intention is to preserve vanilla colours, materials, and readability while improving:

- Directional sunlight and ambient lighting
- Soft, configurable shadows
- Ambient occlusion
- Atmospheric fog and distance haze
- Water appearance and reflections
- More consistent block and entity lighting

### Realism

A separate visual direction for players who prefer a more realistic presentation. Planned areas of exploration include:

- More physically inspired lighting and material response
- Enhanced reflections and water
- Higher-quality shadowing
- More advanced indirect-lighting techniques
- Optional ray-tracing features, where supported by the eventual rendering backend and hardware

### RTX / ray-tracing mode

QuantumTrace plans to offer an optional advanced rendering mode within its shader experience. The exact implementation depends on the graphics API, Minecraft version, mod loader, and supported GPU features.

**Important:** “RTX-style” describes a visual goal; it does not mean that NVIDIA RTX hardware is required or that hardware-accelerated ray tracing is already implemented. Supported devices and the final feature set will be listed once they have been tested.

## Quality presets

QuantumTrace is planned to include five scalable presets:

| Preset | Design goal |
| --- | --- |
| **Potato** | Prioritise frame rate and lower-end hardware |
| **Low** | Lightweight visual improvements |
| **Medium** | Balanced image quality and performance |
| **High** | Richer lighting and higher-quality effects |
| **Ultra** | Maximum available visual quality |

Presets are starting points. Individual settings will remain configurable so players can tune quality to their own system.

## Planned settings

The settings menu is intended to expose controls for:

- Lighting intensity and ambient lighting
- Shadow quality, distance, and softness
- Ambient occlusion
- Reflection quality
- Water quality
- Fog and atmospheric effects
- Render scale and effect quality
- Advanced ray-tracing options, when available
- Preset selection and per-effect overrides

The final settings list will depend on the features that can be implemented reliably and efficiently.

## Performance principles

Performance is a core design requirement, not a final optimisation pass. Development will prioritise:

1. Scalable shader quality and sensible defaults.
2. Avoiding unnecessary per-pixel and per-frame work.
3. Resolution-aware effects and configurable render scale.
4. Efficient shadow and reflection update strategies.
5. Graceful fallback paths when advanced features are unavailable.
6. Testing across different GPU classes and graphics settings.

No FPS gains or hardware requirements are promised until repeatable benchmarks are available.

## Compatibility

- **Target Minecraft version:** 26.2
- **Mod loader:** Fabric Loader 0.19.3 or newer
- **Fabric API:** 0.161.0+26.2
- **Java version:** 25
- **Build toolchain:** Fabric Loom 1.17.21 and Gradle 9.5.1
- **Graphics API and custom shader backend:** Not implemented yet
- **Operating systems and GPU support:** Not yet tested across a hardware matrix

Compatibility will be stated only after the project has been built and tested against the target environment.

## Development roadmap

- [x] Establish the QuantumTrace project vision and feature direction
- [ ] Inspect and verify the supplied Minecraft 26.2 project/JAR
- [ ] Confirm mod loader, mappings, and rendering integration
- [x] Establish a working Fabric development build
- [x] Add an in-game settings screen and configurable interface themes
- [x] Connect quality presets and vanilla visual switches to Minecraft graphics options
- [ ] Implement the Vanilla Remade lighting foundation
- [ ] Add configurable shadows and ambient occlusion
- [ ] Add atmospheric effects and improved water
- [x] Build the in-game settings and preset system
- [ ] Profile and optimise each rendering feature
- [ ] Explore the Realism rendering experience
- [ ] Investigate optional ray-tracing support
- [ ] Publish verified compatibility information and benchmarks

## Installation

Development artifacts are experimental and are not stable releases. Use the QuantumTrace-Fabric-26.2 build artifact from the repository's successful GitHub Actions build. A full shader pack is not included yet.

## Contributing

QuantumTrace is at an early stage. Once the project structure and development workflow are established, contribution guidelines will be added here.

For now, bug reports should include, where possible:

- Minecraft version
- Mod loader and loader version
- Java version
- GPU model and driver version
- Operating system
- Steps to reproduce the issue
- Relevant logs or crash reports

## Project status

QuantumTrace is currently a work in progress. The feature descriptions above communicate the intended direction and should not be interpreted as a list of released features.

More technical documentation will be added as the rendering architecture is confirmed.

## License

A license has not yet been specified. Until one is added to this repository, all rights are reserved by the respective copyright holder(s).