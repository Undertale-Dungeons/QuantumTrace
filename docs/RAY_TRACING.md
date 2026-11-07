# Hardware ray tracing setup

## What is included

QuantumTrace 0.2.0 bundles **Caustica 0.1.1** as a separate Fabric nested mod. Caustica supplies the actual Vulkan hardware path tracer: device feature negotiation, ray-tracing pipelines, acceleration-structure builds, terrain scene extraction, material data, and world-lighting shaders.

This is a real hardware ray-tracing path, not a raster preset with an RTX name. The renderer only reports itself active after its Vulkan RT device/context has initialized.

Upstream engine: https://github.com/ComfyFluffy/Caustica  
Pinned release source: https://github.com/ComfyFluffy/Caustica/tree/ddf36c0e073b53ffdeecf411ea92f0ac5170f358  
License: LGPL-3.0-or-later. See [the included license texts](../third_party/caustica/).

## Enable it

1. Use Minecraft Java Edition 26.2 with Fabric Loader 0.19.3 or newer and Java 25.
2. In Minecraft's Video Settings, select the experimental **Vulkan** graphics backend if it is not already selected.
3. Start the game with QuantumTrace installed. Caustica is already bundled inside the QuantumTrace jar; do not install a second Caustica jar.
4. Open the QuantumTrace menu with **O**.
5. Turn on **HARDWARE RAY TRACING**. If the Vulkan device was created without RT features, restart Minecraft after changing this setting.
6. Check the status line in the QuantumTrace menu. It reports whether the nested engine is loaded and whether its RT context is active.

The **RAYS PER PIXEL** control cycles between 1, 2, 4 and 8 samples. **MAX PATH BOUNCES** cycles between 2, 4, 6 and 8. Higher values can increase GPU load substantially.

## Hardware and driver support

A GPU must expose all required features through the active Vulkan driver. The upstream runtime checks the actual physical device for:

- `VK_KHR_acceleration_structure`
- `VK_KHR_ray_tracing_pipeline`
- `VK_KHR_deferred_host_operations`
- `VK_KHR_ray_tracing_position_fetch`
- `VK_KHR_ray_query`
- Buffer device address, runtime descriptor arrays, non-uniform sampled-image indexing, partially-bound descriptors, update-after-bind sampled-image descriptors, and shader int64 feature bits

A GPU marketed as an NVIDIA RTX card is not sufficient evidence by itself: the installed driver and active Vulkan backend must expose the full set of required extensions and feature bits. A non-NVIDIA card may work if it exposes the same Vulkan capabilities.

Minecraft's Vulkan backend is experimental in 26.2. If Vulkan is missing, the backend falls back, or the driver does not expose all the RT capabilities, Caustica does not create the hardware ray-tracing context. QuantumTrace then remains on the normal Minecraft rendering path.

## DLSS / NVIDIA NGX

This distribution intentionally removes optional proprietary NVIDIA NGX native binaries from the upstream Caustica release. DLSS Ray Reconstruction and DLSS Frame Generation are not included or enabled. The path-tracing renderer itself uses Vulkan RT and does not require DLSS.

NVIDIA NGX is governed by separate NVIDIA SDK terms, not by Caustica's LGPL license. No NVIDIA NGX runtime binaries are redistributed in this QuantumTrace build. See the [NVIDIA SDK license](https://github.com/NVIDIA/DLSS/blob/main/LICENSE.txt).

## Compatibility notes

- This is an experimental renderer. Use a separate Minecraft profile and back up important worlds before testing.
- Other mods that heavily modify world rendering, post-processing, the Vulkan backend, or chunk/model extraction may conflict with the path tracer.
- Performance depends on scene complexity, render resolution, sample count, bounce count, GPU and driver.
- If RT status remains inactive, check the game log for Caustica's Vulkan device capability report and verify Vulkan is selected.
- The build pipeline verifies the nested engine and its SPIR-V ray-generation shader are present, but hardware compatibility still depends on the player's machine.

## Build and licensing

The QuantumTrace Gradle build downloads the pinned Caustica 0.1.1 release and verifies its SHA-256 before bundling it as a nested mod. The build omits native files whose names begin with `nvngx_` or `libnvidia-ngx-`, because those optional runtime files have a separate proprietary NVIDIA license. All Caustica Java classes, mixins, and ray-tracing shader resources remain in the nested jar.

The upstream source, original license, third-party notices, and a description of the packaging change are included in this repository under `third_party/caustica/`. The nested Caustica component remains LGPL-3.0-or-later.
