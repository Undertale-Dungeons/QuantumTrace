# Third-party notices

## Caustica hardware ray-tracing renderer

QuantumTrace bundles **Caustica 0.1.1** by ComfyFluffy and contributors as a separate Fabric nested mod. Caustica supplies the Vulkan hardware ray-tracing and path-tracing renderer used by QuantumTrace.

- Upstream project: https://github.com/ComfyFluffy/Caustica
- Pinned source release: https://github.com/ComfyFluffy/Caustica/tree/ddf36c0e073b53ffdeecf411ea92f0ac5170f358
- Upstream release asset SHA-256: `785b4b3b28a9b13e30a28e846a73050b87b4009af28125e3a262cd3365afdef0`
- License: GNU Lesser General Public License v3.0 or later (`LGPL-3.0-or-later`)
- License texts: [COPYING](third_party/caustica/COPYING), [COPYING.LESSER](third_party/caustica/COPYING.LESSER), and [LICENSE.md](third_party/caustica/LICENSE.md)

The upstream project is distributed as a distinct nested Fabric mod. QuantumTrace does not copy Caustica's Java classes into its own package. The build repackages the pinned upstream JAR only to omit optional NVIDIA NGX native binaries whose license is separate from Caustica's LGPL license. The Caustica Java code and ray-tracing shader resources are otherwise retained. A repackaging notice is included at [third_party/caustica/REPACKAGING.md](third_party/caustica/REPACKAGING.md).

The corresponding upstream source is available at the pinned URL above. The upstream license and third-party notices are included in this repository and in the distributable project documentation.

## NVIDIA DLSS / NGX

The bundled QuantumTrace artifact does **not** include NVIDIA's proprietary DLSS/NGX runtime binaries. DLSS Ray Reconstruction and DLSS Frame Generation are therefore not enabled in this package. This does not disable Caustica's Vulkan hardware ray-tracing renderer.

NVIDIA SDK components have separate terms and are not covered by Caustica's LGPL license. See NVIDIA's official license: https://github.com/NVIDIA/DLSS/blob/main/LICENSE.txt.

QuantumTrace is not endorsed by or affiliated with NVIDIA.
