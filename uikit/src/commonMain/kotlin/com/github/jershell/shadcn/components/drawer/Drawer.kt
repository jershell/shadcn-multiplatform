package com.github.jershell.shadcn.components.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.DialogProperties
import com.composeunstyled.ModalBottomSheetProperties
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledDialog
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.UnstyledModalBottomSheet
import com.composeunstyled.rememberModalBottomSheetState
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.drawer_close

/**
 * The edge a [Drawer] slides from.
 *
 * `Top` is not implemented yet: the compose-unstyled bottom-sheet primitive only
 * anchors vertically from the bottom edge (BACKLOG).
 */
enum class DrawerSide { Bottom, Left, Right }

/**
 * Propagated by [Drawer] to its pieces: [DrawerHeader] centers its text for the
 * bottom side, like the reference
 * `group-data-[vaul-drawer-direction=bottom]:text-center`.
 */
internal val LocalDrawerSide = compositionLocalOf { DrawerSide.Bottom }

internal val LocalDrawerScope = compositionLocalOf<DrawerScope?> { null }

/**
 * Scope of a [Drawer]; provides [DrawerScope.close] to the pieces.
 */
class DrawerScope internal constructor(
    private val onClose: () -> Unit,
) {
    /** Closes the drawer, e.g. from a footer button. */
    fun close() {
        onClose()
    }
}

/**
 * A drawer styled after the shadcn/ui Drawer: a modal panel sliding from an edge of
 * the screen with a `bg-black/50` scrim, Escape and click-outside dismissal.
 *
 * The API is designed so that the shadcn/ui `Sheet` is a configuration of this
 * component:
 *  - **Drawer** (the base scenario): `side = Bottom`, `draggable = true` (drag to
 *    dismiss, powered by the modal bottom-sheet primitive) and the drag-handle pill.
 *  - **Sheet** (the customization): `side = Right` (or `Left`), `draggable = false`,
 *    `showDragHandle = false`, `showCloseButton = true` — a plain slide-in panel with
 *    the close (X) button, like the reference `sheet.tsx` built on Radix Dialog.
 *
 * The application root must be wrapped in [com.composeunstyled.ModalHost].
 *
 * @param open Whether the drawer is visible.
 * @param onOpenChange Called when the user dismisses the drawer (scrim click,
 *   Escape, drag to dismiss).
 * @param side The edge the panel slides from; `Bottom` is the drawer scenario,
 *   `Left`/`Right` the sheet one.
 * @param draggable Whether the panel can be dragged; only meaningful for `Bottom`
 *   (`Left`/`Right` panels never drag, matching the non-draggable Sheet).
 * @param showDragHandle Whether the drag-handle pill is rendered (`Bottom` only,
 *   default follows [draggable]).
 * @param showCloseButton Whether the close (X) button is rendered in the panel
 *   corner (the Sheet customization).
 * @param panelWidth Width of `Left`/`Right` panels (`max-w-sm` of the reference).
 * @param maxHeightFraction Fraction of the window height available to the `Bottom`
 *   panel (`max-h-[80vh]` of the reference); the content scrolls beyond it.
 * @param content Panel pieces: [DrawerHeader] with [DrawerTitle]/[DrawerDescription],
 *   arbitrary content and [DrawerFooter].
 */
@Composable
fun Drawer(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: DrawerSide = DrawerSide.Bottom,
    draggable: Boolean = true,
    showDragHandle: Boolean = draggable,
    showCloseButton: Boolean = false,
    panelWidth: Dp = BaseTokens.token384, // max-w-sm
    maxHeightFraction: Float = 0.8f, // max-h-[80vh]
    content: @Composable ColumnScope.() -> Unit,
) {
    val currentOnOpenChange by rememberUpdatedState(onOpenChange)
    val scope = remember {
        DrawerScope(onClose = { currentOnOpenChange(false) })
    }

    CompositionLocalProvider(
        LocalDrawerSide provides side,
        LocalDrawerScope provides scope,
    ) {
        when (side) {
            DrawerSide.Bottom -> BottomDrawer(
                open = open,
                onOpenChange = currentOnOpenChange,
                modifier = modifier,
                draggable = draggable,
                showDragHandle = showDragHandle,
                maxHeightFraction = maxHeightFraction,
                content = content,
            )

            DrawerSide.Left, DrawerSide.Right -> SideDrawer(
                open = open,
                onOpenChange = currentOnOpenChange,
                modifier = modifier,
                side = side,
                showCloseButton = showCloseButton,
                panelWidth = panelWidth,
                content = content,
            )
        }
    }
}

@Composable
private fun BottomDrawer(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier,
    draggable: Boolean,
    showDragHandle: Boolean,
    maxHeightFraction: Float,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialDetent = SheetDetent.Hidden,
        detents = listOf(SheetDetent.Hidden, SheetDetent.FullyExpanded),
    )

    LaunchedEffect(open) {
        sheetState.targetDetent = if (open) SheetDetent.FullyExpanded else SheetDetent.Hidden
    }

    UnstyledModalBottomSheet(
        state = sheetState,
        enabled = draggable,
        properties = ModalBottomSheetProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            offsetForIme = false,
        ),
        onDismiss = { onOpenChange(false) },
        overlay = {
            Scrim(
                scrimColor = DrawerScrimColor, // bg-black/50
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200)),
            )
        },
    ) {
        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(Theme[ColorProps][ColorTokens.background]),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val maxPanelHeight = maxHeight * maxHeightFraction
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxPanelHeight)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (showDragHandle) {
                        DragHandle()
                    }
                    content()
                }
            }
        }
    }
}

@Composable
private fun DragHandle() {
    val colors = resolveDrawerPanelColors()
    Box(
        modifier = Modifier
            .padding(top = BaseTokens.token16) // mt-4
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .heightIn(min = BaseTokens.token8), // h-2
    ) {
        Box(
            modifier = Modifier
                .width(BaseTokens.token100) // w-[100px]
                .height(BaseTokens.token8) // h-2
                .clip(CircleShape)
                .background(colors.dragHandle), // bg-muted
        )
    }
}

@Composable
private fun SideDrawer(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier,
    side: DrawerSide,
    showCloseButton: Boolean,
    panelWidth: Dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val alignToEnd = side == DrawerSide.Right
    val enter = if (alignToEnd) {
        slideInHorizontally(tween(300)) { it } + fadeIn(tween(200))
    } else {
        slideInHorizontally(tween(300)) { -it } + fadeIn(tween(200))
    }
    val exit = if (alignToEnd) {
        slideOutHorizontally(tween(300)) { it } + fadeOut(tween(200))
    } else {
        slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(200))
    }

    UnstyledDialog(
        visible = open,
        onDismissRequest = { onOpenChange(false) },
        properties = DialogProperties(),
        overlay = {
            Scrim(
                scrimColor = DrawerScrimColor, // bg-black/50
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200)),
            )
        },
    ) {
        AnimatedVisibility(
            visible = open,
            enter = enter,
            exit = exit,
        ) {
            val colors = resolveDrawerPanelColors()
            val panelShape = RectangleShape
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = if (alignToEnd) Alignment.CenterEnd else Alignment.CenterStart,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxHeight()
                        .width(panelWidth) // max-w-sm
                        .shadow(
                            elevation = Effects.boxShadowShadowLgToken0.radius,
                            shape = RectangleShape,
                            clip = false,
                            ambientColor = Effects.boxShadowShadowLgToken0.color,
                            spotColor = Effects.boxShadowShadowLgToken1.color,
                        )
                        .background(colors.background),
                ) {
                    content()
                }
                // border-l / border-r of the reference
                EdgeLine(
                    panelAtEnd = alignToEnd,
                    modifier = Modifier.align(
                        if (alignToEnd) Alignment.CenterStart else Alignment.CenterEnd,
                    ),
                )
                if (showCloseButton) {
                    CloseButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(BaseTokens.token16), // top-4 right-4
                    )
                }
            }
        }
    }
}

/**
 * One-pixel rule on the panel edge facing the content: `border-l` for a right panel,
 * `border-r` for a left one.
 */
@Composable
private fun EdgeLine(
    panelAtEnd: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = resolveDrawerPanelColors()
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(Theme[DimProps][DimTokens.borderWidth])
            .background(colors.border),
    )
}

/**
 * The close (X) button of the Sheet customization: `top-4 right-4`, `size-4` icon,
 * `opacity-70` dimmed and full opacity on hover.
 */
@Composable
private fun CloseButton(
    modifier: Modifier = Modifier,
) {
    val scope = LocalDrawerScope.current ?: return
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val colors = resolveDrawerPanelColors()
    UnstyledButton(
        onClick = { scope.close() },
        interactionSource = interactionSource,
        indication = null,
        modifier = modifier,
    ) {
        UnstyledIcon(
            imageVector = Lucide.X,
            contentDescription = stringResource(Res.string.drawer_close),
            modifier = Modifier
                .size(TwDimensions.heightHToken4) // size-4
                .alpha(if (hovered) 1f else 0.7f),
            tint = if (hovered) colors.content else colors.muted,
        )
    }
}

/**
 * Header block of a [Drawer] panel, matching the shadcn/ui `DrawerHeader`
 * (`p-4 flex flex-col gap-1.5`); the text is centered for the bottom side.
 */
@Composable
fun DrawerHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val centered = LocalDrawerSide.current == DrawerSide.Bottom
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(TwDimensions.paddingPxToken4), // p-4
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapN1_5), // md:gap-1.5
        content = content,
    )
}

/**
 * Title of a [Drawer] panel, matching the shadcn/ui `DrawerTitle`
 * (`font-semibold text-foreground`).
 */
@Composable
fun DrawerTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveDrawerPanelColors()
    val centered = LocalDrawerSide.current == DrawerSide.Bottom
    androidx.compose.foundation.text.BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textLgSemiBold.copy(
            color = colors.content,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        ),
    )
}

/**
 * Description of a [Drawer] panel, matching the shadcn/ui `DrawerDescription`
 * (`text-sm text-muted-foreground`).
 */
@Composable
fun DrawerDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveDrawerPanelColors()
    val centered = LocalDrawerSide.current == DrawerSide.Bottom
    androidx.compose.foundation.text.BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(
            color = colors.muted,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start,
        ),
    )
}

/**
 * Footer block of a [Drawer] panel, matching the shadcn/ui `DrawerFooter`
 * (`mt-auto flex flex-col gap-2 p-4`). Place the primary action last.
 *
 * Note: the `mt-auto` pinning is not applied — the footer follows the content, like
 * in dialogs. Pin it manually with a `Spacer(Modifier.weight(1f))` before it when
 * needed.
 */
@Composable
fun DrawerFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(TwDimensions.paddingPxToken4), // p-4
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            TwDimensions.gapGapToken2,
            alignment = Alignment.End,
        ), // gap-2, buttons pinned to the end
        content = content,
    )
}
