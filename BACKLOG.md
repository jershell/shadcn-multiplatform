# Backlog — other tasks

Tasks outside the component plan. Misc decisions are parked here so they don't get lost.

## Field — deferred features

- [ ] `FieldOrientation.Responsive`: the reference uses the container query `@md/field-group`
      (28rem from the FieldGroup container) — vertical on narrow, horizontal on wide.
      Compose has no container queries; implementation = a BoxWithConstraints wrapper for FieldGroup
      that exposes the "width" via CompositionLocal.
- [ ] The "FieldLabel contains a Field" pattern (checkbox/radio cards): in the reference, a label
      with a field inside gets `w-full flex-col rounded-md border p-4` and, when checked,
      `border-primary bg-primary/5`. Compose cannot detect children — an explicit
      FieldLabel parameterization is needed (e.g. `bordered: Boolean`).

## Flip for floating elements (compose-unstyled)

**Status:** partially solved with a local workaround; the full fix is an MR upstream.

**Problem.** All compose-unstyled primitives are positioned via `calculateFloatingPlacement`
(`composeunstyled-internal-anchored`), which only does shift (clamping into the window) and no flip.
A panel that does not fit on the requested side gets clamped back and **overlaps its own trigger**.
Affected: Popover, Tooltip, DropdownMenu (→ Select, Calendar, Combobox, all future menus).
Verified up to version 2.9.2 inclusive — behavior unchanged.

**Upstream issue:** https://github.com/composablehorizons/compose-unstyled/issues/427

**Proper solution:** MR to compose-unstyled with flip logic in `calculateFloatingPlacement`
(or a collision-strategy parameter in `AnchoredFloatingContent`). Tedious — deferred.

**Local workaround (done):** `uikit/.../shadcn/anchored/FlipAnchored.kt` —
`FlipAnchoredFloatingContent`, a clone of the library primitive on top of public APIs
(`AnchoredLayout` + `calculateFloatingPlacement`). Side selection: requested → opposite →
smallest overlap.

- [x] Popover moved to FlipAnchoredFloatingContent (Combobox is fixed automatically)
- [ ] Tooltip — still on `UnstyledTooltip` (no flip); migration = porting the trigger logic
      (hover-delay/focus/long-press/Escape, ~150 lines per the library sources)
- [ ] Select / Calendar — on `UnstyledDropdownMenu` (no flip); heavy migration: modal,
      focus traversal, keyboard menu navigation
- [ ] Remove the workaround once flip lands upstream

## Toast — deferred features (baseline: Base UI toast, see `imports/api-reference/toast.md`)

- [ ] success / warning / info variants — no tokens in the Figma export
      (`imports/design-tokens.json`); add tokens → regenerate → extend `ToastVariant`
- [ ] Promise toasts (`toastManager.promise(...)` from Base UI: loading → success/error)
- [ ] Swipe dismiss (base-ui `swipeDirection`; primarily relevant for Android/iOS)
- [ ] Anchored toasts (`Toast.Positioner` — a toast anchored to an element, the "Copied" example)
- [ ] A11y: liveRegion on toasts, F6 focus in the viewport landmark
- [ ] `update()` of the manager (base-ui: upsert a toast by id)

## Drawer — deferred features

- [ ] `DrawerSide.Top`: the `UnstyledModalBottomSheet` primitive is bottom-anchored only
      (vertical offset from the bottom); for the top we need either an inverted detent container
      or a local implementation on FlipAnchored/Modal.
- [ ] Pinned footer (the reference `mt-auto`): currently DrawerFooter follows the content;
      pinning to the bottom is a manual `Spacer(weight)` or a component parameter.
- [ ] Horizontal drag for `Left`/`Right` (Sheet): the primitive's vertical `anchoredDraggable`
      does not fit; a custom horizontal anchored-drag is needed
      (currently SideDrawer only closes on click/Escape — like Sheet in the reference).

## Context Menu — deferred features

- [ ] Long-press on touch devices (Android/iOS): currently right-click only.
      On long-press-down the menu opens, and the subsequent release lands in the
      modal scrim and closes it — need a consume-release approach or custom delay logic.

## Dropdown Menu — deferred features

- [ ] Sub-menu: implemented locally in a basic form (`DropdownMenuSub` on FlipAnchoredFloatingContent + Portal),
      but it is NOT radix semantics: hover-open is not done (click/ArrowRight... there is no
      keyboard ArrowRight-open), focus management between levels is coarse, sub-in-sub nesting is untested.
      When a sub-primitive appears in compose-unstyled — migrate.
- [ ] Panel flip — covered by the general "Flip for floating elements" item above;
      closed by a single upstream MR.

## Dialog — deferred niceties

- [ ] scrim/overlay token in the Figma export — currently `Color.Black.copy(alpha = 0.5f)`
      is hardcoded in `DialogColors.kt` (reference `bg-black/50`); add a token → regenerate → replace
- [ ] Nested/stacked dialogs (radix style, several at once) — currently FIFO
- [ ] Focus trap on the first interactive element of the panel (currently focus goes to the panel as a whole)

## Slider — deferred niceties

- [ ] A11y for `RangeSlider`: progress semantics over two values (the single Slider
      has semantics via the primitive)
- [ ] Verify the color difference reported by the master (all colors from tokens,
      matching shadcn neutral: track=muted=neutral-100, range/thumb-border=primary=neutral-900)

## Button — deferred niceties (parity with the reference)

- [ ] `has-[>svg]` paddings: with an icon present, default→px-3, xs→px-1.5, sm→px-2.5,
      lg→px-4. Compose cannot cheaply detect an icon inside the content slot
      (needs SubcomposeLayout or an explicit flag) — deferred, the difference is 4px
- [ ] `link`: `underline-offset-4` — BasicText does not control underline offset

## HDCharts — MR candidates upstream

Library: io.github.dautovicharis:charts 2.4.0 (https://github.com/HDCharts/charts)

- [ ] Unify the color-list semantics (lineColors/barColors/areaColors).
      Problem: semantics are inconsistent across chart types — Bar/Histogram/
      StackedBar/Radar/Pie expect color PER POINT/CATEGORY (list.size == points.size),
      while Line (multi-series) and StackedArea expect color PER SERIES (list.size == items.size).
      The user gets confusing "Colors size N does not match expected M" errors and has to
      read the validation sources. Solution: unified "color per series" semantics
      (+ an optional per-point override) or an explicit flag field with a documented error.
- [ ] Move the title and legend TextStyle into ChartViewDefaults.style — currently they are
      rendered from MaterialTheme.colorScheme inside the factory, so a chart cannot be fully
      themed outside Material (our case: shadcn tokens).

## Compose shadows

- [x] Remove `shadow-xs` from the outline Button (done)
- [x] Remove the shadow from the outline Toggle and the joined ToggleGroup container (done)
- [ ] Restore the removed shadows if a proper renderer appears or an MR lands upstream.
      Shadows with explicit token colors (PopoverContent, ComboboxPanel, Dialog, Slider thumb)
      look acceptable — those were left untouched.