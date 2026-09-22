package com.github.jershell.shadcn.components.dialog

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.DialogProperties
import com.composeunstyled.Scrim
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledDialog
import com.composeunstyled.UnstyledIcon
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
import com.github.jershell.shadcn.generated.resources.dialog_close

/**
 * Receiver of the [Dialog] content slot. Provides [close] to dismiss the dialog
 * from inside the content.
 */
interface DialogScope {

    /**
     * Dismisses the dialog.
     */
    fun close()
}

/**
 * A modal dialog styled after the shadcn/ui Dialog: a centered `max-w-lg` panel with
 * `bg-background p-6 gap-4 rounded-lg border shadow-lg` over a black/50 scrim.
 *
 * Rendered through [com.composeunstyled.UnstyledDialog], so it sits in the modal layer
 * (the application root must be wrapped in [com.composeunstyled.ModalHost]), traps focus,
 * and dismisses on Escape/back press and outside clicks per [properties].
 *
 * Build the panel with [DialogHeader] / [DialogTitle] / [DialogDescription] /
 * [DialogFooter]; use [DialogScope.close] to dismiss from inside the content.
 *
 * For dialogs shown from anywhere in the app (alerts, confirms) see [Dialogs].
 *
 * @param open Whether the dialog is visible.
 * @param onOpenChange Called when the user asks to show or hide the dialog.
 * @param modifier Modifier applied to the panel container.
 * @param properties Dismiss behavior (Escape/back press, outside clicks).
 * @param showCloseButton Whether the close (X) button is rendered in the top end corner.
 * @param contentPadding Inner padding of the panel; defaults to the shadcn `p-6`,
 *   pass `0.dp` for edge-to-edge panels (e.g. command palettes).
 * @param content Panel content, laid out vertically.
 */
@Composable
fun Dialog(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    showCloseButton: Boolean = true,
    contentPadding: Dp = TwDimensions.paddingPxToken6, // p-6
    content: @Composable DialogScope.() -> Unit,
) {
    val currentOnOpenChange by rememberUpdatedState(onOpenChange)
    val scope = remember {
        object : DialogScope {
            override fun close() {
                currentOnOpenChange(false)
            }
        }
    }

    UnstyledDialog(
        visible = open,
        onDismissRequest = { currentOnOpenChange(false) },
        properties = properties,
        overlay = {
            Scrim(
                scrimColor = DialogScrimColor, // bg-black/50
                enter = fadeIn(tween(DialogAnimMillis)),
                exit = fadeOut(tween(DialogAnimMillis)),
            )
        },
    ) {
        AnimatedVisibility(
            visible = open,
            enter = DialogEnter,
            exit = DialogExit,
        ) {
            DialogPanelBox(
                modifier = modifier,
                showCloseButton = showCloseButton,
                contentPadding = contentPadding,
                onClose = { currentOnOpenChange(false) },
                content = { scope.content() },
            )
        }
    }
}

internal const val DialogAnimMillis = 200

internal val DialogEnter: EnterTransition =
    fadeIn(tween(DialogAnimMillis)) +
        scaleIn(initialScale = 0.95f, animationSpec = tween(DialogAnimMillis))

internal val DialogExit: ExitTransition =
    fadeOut(tween(DialogAnimMillis)) +
        scaleOut(targetScale = 0.95f, animationSpec = tween(DialogAnimMillis))

/**
 * The styled dialog panel: centered on the screen, `max-w-lg`, `p-6 gap-4 rounded-lg`
 * with `bg-background`, border and `shadow-lg`, plus the close (X) button.
 */
@Composable
internal fun DialogPanelBox(
    modifier: Modifier = Modifier,
    showCloseButton: Boolean,
    contentPadding: Dp,
    onClose: () -> Unit,
    content: @Composable DialogScope.() -> Unit,
) {
    val colors = resolveDialogColors()
    val radius = Theme[DimProps][DimTokens.radiusLg]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)
    val closeInteractionSource = remember { MutableInteractionSource() }
    val closeHovered by closeInteractionSource.collectIsHoveredAsState()
    val closeTint = if (closeHovered) colors.content else colors.close
    val scope = remember {
        object : DialogScope {
            override fun close() {
                onClose()
            }
        }
    }

    // max-w-[calc(100%-2rem)] on small screens, sm:max-w-lg on large ones
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(BaseTokens.token32) // max-w-[calc(100%-2rem)] margin
            .wrapContentSize(Alignment.Center),
    ) {
        Box(
            modifier = modifier
                .widthIn(max = TwDimensions.maxWidthMaxWLg) // sm:max-w-lg
                .fillMaxWidth()
                .clip(shape)
                .shadow(
                    elevation = Effects.boxShadowShadowLgToken0.radius,
                    shape = shape,
                    clip = false,
                    ambientColor = Effects.boxShadowShadowLgToken0.color,
                    spotColor = Effects.boxShadowShadowLgToken1.color,
                )
                .background(colors.background)
                .border(borderWidth, colors.border, shape),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken4), // gap-4
            ) {
                content(scope)
            }
            if (showCloseButton) {
                UnstyledButton(
                    onClick = onClose,
                    interactionSource = closeInteractionSource,
                    indication = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(TwDimensions.paddingPxToken4), // top-4 right-4
                ) {
                    UnstyledIcon(
                        imageVector = Lucide.X,
                        contentDescription = stringResource(Res.string.dialog_close),
                        modifier = Modifier.size(TwDimensions.heightHToken4), // size-4
                        tint = closeTint,
                    )
                }
            }
        }
    }
}

/**
 * Header block of a [Dialog] panel, matching the shadcn/ui `DialogHeader`
 * (`flex flex-col gap-2`).
 */
@Composable
fun DialogHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2), // gap-2
        content = content,
    )
}

/**
 * Title of a [Dialog] panel, matching the shadcn/ui `DialogTitle`
 * (`text-lg font-semibold`).
 */
@Composable
fun DialogTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textLgSemiBold.copy(
            color = Theme[ColorProps][ColorTokens.foreground],
        ),
    )
}

/**
 * Description of a [Dialog] panel, matching the shadcn/ui `DialogDescription`
 * (`text-sm text-muted-foreground`).
 */
@Composable
fun DialogDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(
            color = Theme[ColorProps][ColorTokens.mutedForeground],
        ),
    )
}

/**
 * Footer of a [Dialog] panel, matching the shadcn/ui `DialogFooter`
 * (`gap-2 justify-end` on wide screens). Place the primary action last.
 */
@Composable
fun DialogFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = TwDimensions.gapGapToken2, // gap-2
            alignment = Alignment.End,
        ),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
