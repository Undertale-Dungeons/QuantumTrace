# QuantumTrace hardware ray tracing plan

## Current status

QuantumTrace does **not** contain a ray-tracing renderer or an RTX implementation yet. The in-game control center reports this honestly and keeps the hardware ray-tracing control disabled until a renderer backend is integrated.

This document records the technical requirements for a real implementation. It is not a claim that those features are already available.

## Target architecture

QuantumTrace targets Minecraft Java Edition 26.2 on Fabric. Minecraft 26.2 offers an experimental Vulkan graphics backend alongside OpenGL. Rendering code must use the Minecraft/Blaze3D rendering abstraction where possible; raw OpenGL calls are not portable to the Vulkan backend.

A hardware ray-tracing backend would need to:

1. Detect the active Minecraft graphics backend and query the actual Vulkan physical device.
2. Check the Vulkan ray-tracing extensions and required device features before enabling the renderer.
3. Extract chunk, block, foliage, fluid, and entity geometry into GPU-friendly scene data.
4. Build and update bottom-level and top-level acceleration structures (BLAS/TLAS).
5. Trace direct-light shadow rays, reflection rays, and indirect-lighting rays.
6. Denoise temporally and composite the result into the Minecraft frame.
7. Release GPU resources safely on world changes, device loss, and shutdown.

## Vulkan requirements to investigate

Hardware support is not established by the GPU name alone. The backend must query the driver's enabled extensions and feature bits. The minimum capability set to investigate includes:

- `VK_KHR_acceleration_structure`
- `VK_KHR_deferred_host_operations`
- At least one traversal path: `VK_KHR_ray_query` or `VK_KHR_ray_tracing_pipeline`
- Buffer device address and acceleration-structure feature support
- Descriptor indexing features required by the renderer's material system

A ray-tracing-pipeline path uses ray-generation, hit, and miss shaders plus a shader binding table. A ray-query path can run traversal from compute shaders. The exact minimum must be validated against the shaders and device features QuantumTrace eventually ships.

## Rendering quality controls planned

Once a backend exists, the settings UI can expose real controls for:

- Hardware ray tracing: Off / Auto / On
- Ray-traced shadows
- Ray-traced ambient occlusion
- Ray-traced reflections
- Indirect lighting / global illumination
- Samples per pixel and maximum bounce depth
- Denoiser and temporal accumulation
- Render scale and ray-tracing resolution

The controls must remain unavailable when the renderer is not initialized or the device does not meet its requirements. Selecting a setting must never silently claim that ray tracing is active when the engine has fallen back to rasterization.

## Compatibility and fallback

- Vulkan is experimental in Minecraft 26.2 and may fall back to another graphics backend.
- Vulkan support alone does not imply ray-tracing support.
- NVIDIA RTX branding is not a reliable hardware check. Vulkan ray-tracing support is capability-based and may be exposed by supported GPUs from different vendors.
- When hardware ray tracing is unavailable, QuantumTrace should retain the standard Minecraft/Vanilla Remade rendering path.
- The initial renderer must avoid raw OpenGL interop so it can work with Minecraft's backend abstraction.

## References

- [Minecraft 26.2 release notes](https://feedback.minecraft.net/hc/en-us/articles/46690753273997-Minecraft-Java-Edition-26-2)
- [Fabric: Minecraft 26.2 rendering changes](https://www.fabricmc.net/2026/06/15/262.html)
- [Fabric 26.2 rendering concepts](https://docs.fabricmc.net/develop/rendering/basic-concepts)
- [Fabric 26.2 world rendering](https://docs.fabricmc.net/develop/rendering/world)
- [Vulkan ray-tracing extensions overview](https://docs.vulkan.org/guide/latest/extensions/ray_tracing.html)

---

**Next engineering milestone:** build a Vulkan device/capability bridge that is compatible with Minecraft's 26.2 renderer lifecycle. The ray tracer should only be enabled after this bridge and the scene extraction path pass hardware and fallback tests.
