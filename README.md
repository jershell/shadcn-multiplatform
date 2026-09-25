# shadcn-multiplatform

[![Maven Central](https://img.shields.io/maven-central/v/com.github.jershell/shadcn-multiplatform)](https://central.sonatype.com/artifact/com.github.jershell/shadcn-multiplatform)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

shadcn/ui components for Compose Multiplatform, built on top of [compose-unstyled](https://composables.com/compose-unstyled) primitives and styled according to the shadcn/ui design system. All colors, dimensions and typography come from Figma design tokens, with light/dark theme support.

**Live web demo: <https://jershell.github.io/shadcn-multiplatform/>**

## Features

- 50+ shadcn/ui components implemented for Compose Multiplatform (see [COMPONENTS.md](COMPONENTS.md) for the full plan and status)
- Built on unstyled compose-unstyled primitives — no Material components underneath
- Theming generated from Figma design tokens (`imports/design-tokens.json`): light/dark palettes, semantic colors, dimensions, typography, shadows
- Icon set with a generated catalog (`ShadcnIcon`)
- Targets: Android, JVM (desktop), wasmJs, iOS. There is no JS target (the `Table` component is based on lazytable, which does not support it)

## Modules

- `:uikit` — the library (targets: android, jvm, wasmJs, iosArm64/iosSimulatorArm64)
- `:demoApp:commonApp` — shared demo code (+ platform sources with expect/actual)
- `:demoApp:androidApp`, `:demoApp:desktopApp`, `:demoApp:webApp` — demo entry points; iOS is opened from `demoApp/iosApp` in Xcode

## Getting started

Add the dependency:

```kotlin
dependencies {
    implementation("com.github.jershell:shadcn-multiplatform:1.0.2-dev")
}
```

Wrap your UI in `ShadcnUI`:

```kotlin
import com.github.jershell.shadcn.containers.ShadcnUI

@Composable
fun App() {
    ShadcnUI {
        // your UI, components from com.github.jershell.shadcn.components.*
    }
}
```
or ShadcnTheme {}

## Demo

### Desktop

Run the desktop application: `./gradlew :demoApp:desktopApp:run`

### Android

To build the application bundle:

- run `./gradlew :demoApp:androidApp:assembleDebug`
- find the `.apk` file in `androidApp/build/outputs/apk/debug/androidApp-debug.apk`

Or open the project in Android Studio and run the imported android run configuration.

### iOS

- open `demoApp/iosApp/iosApp.xcodeproj` in Xcode and run the standard configuration

### Web (wasm)

Live demo is hosted on GitHub Pages: <https://jershell.github.io/shadcn-multiplatform/> (deployed by the `pages.yml` workflow).

Run the browser application: `./gradlew :demoApp:webApp:wasmJsBrowserDevelopmentRun`

Build the web distribution: `./gradlew :demoApp:webApp:wasmJsBrowserDistribution` — the output goes to `webApp/build/dist/wasmJs/productionExecutable`, deploy that directory to a web server.

## Theme tokens regeneration

The theme code (`uikit/.../shadcn/theme/Generated*.kt`, `Theme.kt`, `TokenShadow.kt`) is generated — do not edit it manually. To regenerate it from the Figma export `imports/design-tokens.json`:

```bash
./gradlew importThemeFromFigmaTokens
```

## shadcn/ui reference sync

To refresh the local snapshot of the shadcn/ui reference in `imports/shadcn-reference/` (component sources `*.tsx` and `theme-globals.css`):

```bash
./gradlew syncShadcnReference
```

The reference version is pinned via the `REF` constant in `build-plugin/.../SyncShadcnReference.kt`; after updating it, `git diff` shows what changed in the reference.

## Documentation and design sources

- [shadcn/ui components](https://www.figma.com/community/file/1342715840824755935) by Sitsiilia Bergmann - A well-structured component library aligned with the shadcn component system, regularly maintained.
- [shadcn/ui design system](https://www.figma.com/community/file/1203061493325953101) by Pietro Schirano - A design companion for shadcn/ui. Each component was painstakingly crafted to perfectly match the code implementation.
- [compose-unstyled docs](https://composables.com/compose-unstyled/docs/overview)
- [compose-unstyled custom themes](https://composables.com/compose-unstyled/docs/custom-themes)
- [composables.com/icons](https://composables.com/icons)

## Components

This project implements the [shadcn/ui components](https://ui.shadcn.com/docs/components).

The component plan and status are tracked in [COMPONENTS.md](COMPONENTS.md).

## License

MIT — see [LICENSE](LICENSE).
