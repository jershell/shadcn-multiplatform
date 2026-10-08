# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.3-dev] - 2026-10-08

### Added

- `Table`: rewritten on top of `com.ryinex.kotlin:compose-data-table` (replaces `lazytable`,
  which is removed as a dependency and exposed as `api` for its own types).
- `ScrollArea` / scrollbars: hover-reveal for the thumb, coalesced scrollbar state
  (`rememberCoalescedScrollbarState` — thumb drags applied once per frame, no lag on heavy
  lazy lists), configurable thumb visibility (`ThumbVisibility.AlwaysVisible`) and size
  (`thumbWidth`/`thumbHeight`), and `minThumbSize` so the thumb stays grabbable on long content.
- `Toast`: `selectable` option — title/description can be selected and copied.
- `Checkbox`: `accessibilityLabel` parameter for icon-only checkboxes (e.g. table row
  selection) without a visible label.
- Tests: `ScrollAreaLayoutTest`, `TableScreenshotTest` (JVM).

### Changed

- `Pagination`: the ellipsis icon is now tinted with the `foreground` token (was black outside
  a `Button`).
- `Sidebar`: the scrollbar now uses `rememberCoalescedScrollbarState`.
- `README`: added a `Compatibility` section (Kotlin 2.4.0 / Compose Multiplatform 1.11.1 /
  AGP 9.1.0 / JVM 17) to prevent consumption with older toolchains.

### Fixed

- Text selection highlight made the selected text invisible: `primary` and `foreground` have
  the same lightness in each theme (black-on-black in light, white-on-white in dark). The
  selection background is now a translucent `primary` (`alpha 0.3`); Compose cannot tint the
  selected text itself, so a solid fill would keep hiding it.

## [1.0.2-dev] - 2026-09-25

### Added

- `ExtraSmall` typography component; expanded the Typography demo (scale section).
