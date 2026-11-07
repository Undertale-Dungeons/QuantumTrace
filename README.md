# QuantumTrace

**A performance-focused visual overhaul for Minecraft.**

QuantumTrace is an ambitious shader project for Minecraft **26.2**, designed to make the game look better without losing the visual identity that makes Minecraft feel like Minecraft.

The project begins with **Vanilla Remade**: improved lighting, shadows, atmosphere, and water that preserve the original game's style. A separate **Realism** experience is planned for players who want a more physically inspired look.

> **Development status:** Experimental Fabric build. QuantumTrace 0.2.0 bundles the Caustica Vulkan hardware path-tracing renderer as a separate LGPL-licensed nested Fabric mod. Rendering is available only when Minecraft is running on the Vulkan backend and the selected GPU/driver exposes the required Vulkan ray-tracing features. DLSS/NGX binaries are not bundled.

---

## Current build

The current development build includes:

- Open the QuantumTrace control center with **O**.
- Interface themes: Vanilla, Metal Gear, 2D Minecraft, and Lemons Eats Limes.
- Potato, Low, Medium, High, and Ultra presets that update selected vanilla graphics options.
- Direct switches for ambient occlusion, cutout leaves, improved transparency, entity shadows, and vignette.
- An **Apply Vanilla Maxxing** action that enables those visual options and increases biome blending.
- A hidden Peabert mascot option, unlocked by clicking the menu title seven times.

The vanilla graphics controls change Minecraft's existing options. QuantumTrace 0.2.0 also bundles **Caustica 0.1.1**, a real Vulkan hardware path-tracing renderer. It builds acceleration structures from the loaded Minecraft scene and traces rays through the scene. The renderer is loaded by Fabric as a nested mod, so do not install a second copy of Caustica.

**Important:** The ray-tracing renderer requires Minecraft's experimental Vulkan backend and a GPU/driver exposing the required Vulkan ray-tracing extensions and feature bits. Having an NVIDIA RTX label alone does not guarantee that the backend or driver exposes the complete feature set. Other GPUs may work if the Vulkan requirements are present. The in-menu status reports whether the RT context actually initialized.

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

### Hardware ray tracing / path tracing

The current build uses Caustica 0.1.1 for hardware ray tracing on Minecraft 26.2's Vulkan backend. The renderer uses Vulkan acceleration structures and ray-tracing pipelines to trace world lighting. It checks the selected physical device and enables RT only when the required extensions and feature bits are available.

The renderer requires the device to support at least:

- `VK_KHR_acceleration_structure`
- `VK_KHR_ray_tracing_pipeline`
- `VK_KHR_deferred_host_operations`
- `VK_KHR_ray_tracing_position_fetch`
- `VK_KHR_ray_query`
- The required buffer-device-address and descriptor-indexing feature bits

Choose the **Vulkan** graphics backend in Minecraft's Video Settings (it is experimental in this Minecraft version), then restart the game after changing the QuantumTrace hardware-ray-tracing setting. The path tracer will only initialize if the backend, GPU and driver meet its runtime checks.

**DLSS note:** This QuantumTrace package deliberately excludes NVIDIA's proprietary NGX runtime files. DLSS Ray Reconstruction and DLSS Frame Generation are therefore unavailable in this bundled build; the Vulkan hardware path tracer does not depend on those optional features.

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
- **Graphics API:** Minecraft 26.2 Vulkan backend (experimental); OpenGL does not provide the required RT path
- **Path-tracing implementation:** Caustica 0.1.1 nested Fabric mod, LGPL-3.0-or-later
- **DLSS / NGX:** Not bundled; proprietary NVIDIA runtime files are excluded
- **Operating systems and GPU support:** Not yet tested across a hardware matrix

Compatibility will be stated only after the project has been built and tested against the target environment.

## Development roadmap

- [x] Establish the QuantumTrace project vision and feature direction
- [ ] Inspect and verify the supplied Minecraft 26.2 project/JAR
- [ ] Confirm mod loader, mappings, and rendering integration
- [x] Establish a working Fabric development build
- [x] Add an in-game settings screen and configurable interface themes
- [x] Connect quality presets and vanilla visual switches to Minecraft graphics options
- [x] Bundle a Vulkan hardware path-tracing renderer (Caustica 0.1.1)
- [ ] Implement the QuantumTrace-native Vanilla Remade lighting foundation
- [ ] Add configurable shadows and ambient occlusion
- [ ] Add atmospheric effects and improved water
- [x] Build the in-game settings and preset system
- [ ] Profile and optimise each rendering feature
- [ ] Explore the Realism rendering experience
- [x] Integrate optional Vulkan hardware ray tracing / path tracing
- [ ] Publish verified compatibility information and benchmarks

## Installation

Development artifacts are experimental and are not stable releases. Use the **QuantumTrace-Fabric-26.2** artifact from a successful GitHub Actions build. The artifact already contains the Caustica renderer as a nested Fabric mod. Configure Minecraft to use the experimental Vulkan backend and restart after changing the RT setting. See [Ray tracing setup](docs/RAY_TRACING.md) and [third-party notices](THIRD_PARTY_NOTICES.md).

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