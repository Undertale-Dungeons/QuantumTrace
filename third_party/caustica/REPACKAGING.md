# Caustica repackaging notice

Date: 2026-11-07

This QuantumTrace distribution contains Caustica 0.1.1, copyright (c) 2026 ComfyFluffy and contributors, licensed under LGPL-3.0-or-later.

The Caustica release JAR is used as a separate nested Fabric mod. The build performs one packaging change: it omits optional native files whose names begin with `nvngx_` or `libnvidia-ngx-`. These are proprietary NVIDIA NGX runtime components covered by separate NVIDIA terms. They are not needed for Caustica's Vulkan acceleration-structure and ray-tracing-pipeline implementation.

No Caustica Java source files or ray-tracing shader resources are modified by this repackaging. The corresponding upstream source is available at:

https://github.com/ComfyFluffy/Caustica/tree/ddf36c0e073b53ffdeecf411ea92f0ac5170f358

The included `COPYING`, `COPYING.LESSER`, and `LICENSE.md` files preserve the upstream licensing information.
