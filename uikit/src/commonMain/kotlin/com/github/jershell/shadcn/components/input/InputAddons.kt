package com.github.jershell.shadcn.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.button.LocalButtonContentColor
import com.github.jershell.shadcn.components.button.LocalButtonIconSize
import com.github.jershell.shadcn.components.button.LocalButtonTextStyle
import com.github.jershell.shadcn.components.button.LocalButtonLinkUnderlined
import com.github.jershell.shadcn.components.button.resolveButtonColors
import com.github.jershell.shadcn.components.button.resolveButtonFocusRingColor
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

internal enum class InputAddonAlign {
    InlineStart,
    InlineEnd,
    BlockStart,
    BlockEnd,
}

/**
 * Sizes of buttons inside input addons, matching the shadcn/ui `InputGroupButton`
 * sizes. They differ from the standalone [com.github.jershell.shadcn.components.button.ButtonSize]:
 * xs is `h-6 px-2 gap-1` with a `size-3.5` icon, sm is `h-8 px-2.5`, and the icon
 * variants are square.
 */
enum class InputGroupButtonSize {
    Xs,
    Sm,
    IconXs,
    IconSm,
}

internal data class InputGroupButtonSizeSpec(
    val height: Dp,
    val horizontalPadding: Dp,
    val gap: Dp,
    val iconSize: Dp,
    val shape: Shape,
    val isIconOnly: Boolean,
)

@Composable
internal fun InputGroupButtonSize.spec(): InputGroupButtonSizeSpec = when (this) {
    InputGroupButtonSize.Xs -> InputGroupButtonSizeSpec(
        height = TwDimensions.heightHToken6,
        horizontalPadding = TwDimensions.paddingPxToken2, // px-2
        gap = BaseTokens.token4, // gap-1
        iconSize = BaseTokens.token14, // size-3.5
        shape = RoundedCornerShape(InputGroupButtonRadius),
        isIconOnly = false,
    )

    InputGroupButtonSize.Sm -> InputGroupButtonSizeSpec(
        height = TwDimensions.heightHToken8,
        horizontalPadding = BaseTokens.token10, // px-2.5
        gap = TwDimensions.gapGapN1_5, // gap-1.5
        iconSize = TwDimensions.heightHToken4,
        shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]), // rounded-md
        isIconOnly = false,
    )

    InputGroupButtonSize.IconXs -> InputGroupButtonSizeSpec(
        height = TwDimensions.heightHToken6, // size-6
        horizontalPadding = TwDimensions.paddingPxToken0,
        gap = BaseTokens.token4,
        iconSize = TwDimensions.heightHToken4,
        shape = RoundedCornerShape(InputGroupButtonRadius),
        isIconOnly = true,
    )

    InputGroupButtonSize.IconSm -> InputGroupButtonSizeSpec(
        height = TwDimensions.heightHToken8, // size-8
        horizontalPadding = TwDimensions.paddingPxToken0,
        gap = TwDimensions.gapGapN1_5,
        iconSize = TwDimensions.heightHToken4,
        shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd]),
        isIconOnly = true,
    )
}

/**
 * Scope of the Input/Textarea addons. Extends [RowScope], so the slots also
 * provide `Modifier.weight(1f)` (the Tailwind `ml-auto` equivalent) and the
 * other row modifiers.
 */
interface InputAddonScope : RowScope {
    @Composable
    fun Icon(
        icon: ShadcnIcon,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    )

    @Composable
    fun Icon(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    )

    @Composable
    fun Icon(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    )

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
    )

    @Composable
    fun Spinner(
        modifier: Modifier = Modifier,
        size: Dp = TwDimensions.heightHToken4,
    )

    @Composable
    fun CircularProgress(
        modifier: Modifier = Modifier,
        size: Dp = TwDimensions.heightHToken4,
    )

    @Composable
    fun Button(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        size: InputGroupButtonSize = InputGroupButtonSize.Xs,
        variant: ButtonVariant = ButtonVariant.Ghost,
        shape: Shape? = null,
        content: @Composable RowScope.() -> Unit,
    )

    @Composable
    fun Kbd(
        text: String,
        modifier: Modifier = Modifier,
    )

    /** Horizontal divider spanning the full slot width (for block-addons). */
    @Composable
    fun Divider(modifier: Modifier = Modifier)

    /** Vertical separator between inline-addon items (bg-input, w-px). */
    @Composable
    fun Separator(modifier: Modifier = Modifier)
}

internal class InputAddonScopeImpl(
    rowScope: RowScope,
    private val contentColor: Color,
    private val align: InputAddonAlign,
    private val focusOnPress: (() -> Unit)?,
) : InputAddonScope, RowScope by rowScope {

    @Composable
    override fun Icon(
        icon: ShadcnIcon,
        contentDescription: String?,
        modifier: Modifier,
    ) {
        ShadcnIconContent(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier
                .focusOnClick()
                .size(TwDimensions.heightHToken4),
            tint = contentColor,
        )
    }

    @Composable
    override fun Icon(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
    ) = Icon(
        icon = imageVector.toShadcnIcon(),
        contentDescription = contentDescription,
        modifier = modifier,
    )

    @Composable
    override fun Icon(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
    ) = Icon(
        icon = painter.toShadcnIcon(),
        contentDescription = contentDescription,
        modifier = modifier,
    )

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
    ) {
        BasicText(
            text = text,
            modifier = modifier.focusOnClick(),
            style = TypographyStyles.textSmMedium.copy(color = contentColor),
        )
    }

    @Composable
    override fun Spinner(
        modifier: Modifier,
        size: Dp,
    ) {
        com.github.jershell.shadcn.components.spinner.Spinner(
            modifier = modifier.focusOnClick(),
            size = size,
            tint = contentColor,
        )
    }

    @Composable
    override fun CircularProgress(
        modifier: Modifier,
        size: Dp,
    ) {
        com.github.jershell.shadcn.components.spinner.Spinner(
            modifier = modifier.focusOnClick(),
            size = size,
            tint = contentColor,
        )
    }

    @Composable
    override fun Button(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        size: InputGroupButtonSize,
        variant: ButtonVariant,
        shape: Shape?,
        content: @Composable RowScope.() -> Unit,
    ) {
        val sizeSpec = size.spec()
        val buttonShape = shape ?: sizeSpec.shape
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        val isPressed by interactionSource.collectIsPressedAsState()
        val colors = resolveButtonColors(
            variant = variant,
            isHovered = isHovered,
            isPressed = isPressed,
        )
        val focusRingColor = resolveButtonFocusRingColor(variant)
        val focusRingWidth = Effects.boxShadowFocusRing.spread
        val borderWidth = Theme[DimProps][DimTokens.borderWidth]

        val containerModifier = modifier
            // has-[>button]:m-[-0.45rem] — pull the button toward the field edge
            .then(AddonNegativeMargins.button(align))
            .then(
                if (sizeSpec.isIconOnly) {
                    Modifier.size(sizeSpec.height)
                } else {
                    Modifier.height(sizeSpec.height)
                },
            )
            .clip(buttonShape)
            .then(
                if (colors.border != null) {
                    Modifier.border(borderWidth, colors.border, buttonShape)
                } else {
                    Modifier
                },
            )
            .background(colors.container, buttonShape)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = buttonShape,
            )

        UnstyledButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            role = Role.Button,
            modifier = containerModifier,
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalButtonContentColor provides colors.content,
                LocalButtonIconSize provides sizeSpec.iconSize,
                LocalButtonTextStyle provides TypographyStyles.textSmRegular,
                LocalButtonLinkUnderlined provides false,
            ) {
                if (sizeSpec.isIconOnly) {
                    content()
                } else {
                    Row(
                        modifier = Modifier.padding(horizontal = sizeSpec.horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = sizeSpec.gap,
                            alignment = Alignment.CenterHorizontally,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        content = content,
                    )
                }
            }
        }
    }

    @Composable
    override fun Kbd(
        text: String,
        modifier: Modifier,
    ) {
        // pointer-events-none in the reference: clicking kbd does not focus the field
        com.github.jershell.shadcn.components.kbd.Kbd(
            text = text,
            // has-[>kbd]:m-[-0.35rem]
            modifier = modifier.then(AddonNegativeMargins.kbd(align)),
        )
    }

    @Composable
    override fun Divider(modifier: Modifier) {
        Box(
            modifier = modifier
                .fullBleedHorizontal(TwDimensions.paddingPxToken3)
                .fillMaxWidth()
                .height(Theme[DimProps][DimTokens.borderWidth])
                .background(Theme[ColorProps][ColorTokens.input]),
        )
    }

    @Composable
    override fun Separator(modifier: Modifier) {
        // w-px bg-input: stretches to the height of the tallest row item
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(Theme[DimProps][DimTokens.borderWidth])
                .background(Theme[ColorProps][ColorTokens.input]),
        )
    }

    private fun Modifier.focusOnClick(): Modifier =
        if (focusOnPress != null) {
            clickable { focusOnPress.invoke() }
        } else {
            this
        }
}
private object AddonNegativeMargins {
    // -0.45rem for Button, -0.35rem for Kbd (no tokens in the Figma export)
    private val buttonPull = 7.2.dp
    private val kbdPull = 5.6.dp

    fun button(align: InputAddonAlign): Modifier = when (align) {
        InputAddonAlign.InlineStart -> Modifier.pullStart(buttonPull)
        InputAddonAlign.InlineEnd -> Modifier.pullEnd(buttonPull)
        else -> Modifier
    }

    fun kbd(align: InputAddonAlign): Modifier = when (align) {
        InputAddonAlign.InlineStart -> Modifier.pullStart(kbdPull)
        InputAddonAlign.InlineEnd -> Modifier.pullEnd(kbdPull)
        else -> Modifier
    }
}

/** rounded-[calc(var(--radius)-5px)] for buttons inside input-group (token missing from the export). */
private val InputGroupButtonRadius = 5.dp

/**
 * CSS `margin-inline-start: -amount`: the content extends past the slot toward
 * the start and the freed space is taken by the neighbors.
 */
private fun Modifier.pullStart(amount: Dp): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        if (!constraints.hasBoundedWidth) {
            // intrinsic measurement (IntrinsicSize.Min): do not grow infinite constraints
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        } else {
            val px = amount.roundToPx()
            val placeable = measurable.measure(
                constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth + px),
            )
            layout((placeable.width - px).coerceAtLeast(0), placeable.height) {
                placeable.placeRelative(-px, 0)
            }
        }
    },
)

/** CSS `margin-inline-end: -amount`: the content extends past the slot toward the end. */
private fun Modifier.pullEnd(amount: Dp): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        if (!constraints.hasBoundedWidth) {
            // intrinsic measurement (IntrinsicSize.Min): do not grow infinite constraints
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        } else {
            val px = amount.roundToPx()
            val placeable = measurable.measure(
                constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth + px),
            )
            layout((placeable.width - px).coerceAtLeast(0), placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
    },
)

/** Stretches the content past the parent's horizontal bounds by [amount] on each side. */
private fun Modifier.fullBleedHorizontal(amount: Dp): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        if (!constraints.hasBoundedWidth) {
            // intrinsic measurement (IntrinsicSize.Min): do not grow infinite constraints
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        } else {
            val extendPx = amount.roundToPx()
            val targetWidth = constraints.maxWidth + extendPx * 2
            val placeable = measurable.measure(
                constraints.copy(minWidth = targetWidth, maxWidth = targetWidth),
            )
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(-extendPx, 0)
            }
        }
    },
)

/**
 * Inline-addon container: pl-3/pr-3 by align, gap-2 between items, py-1.5.
 * The addon↔field distance is set by the field's own padding (pl-2/pr-2), so
 * there is no outer gap.
 */
@Composable
internal fun InputAddonInline(
    align: InputAddonAlign,
    focusOnPress: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable InputAddonScope.() -> Unit,
) {
    val horizontalPadding = when (align) {
        InputAddonAlign.InlineStart -> PaddingValues(start = TwDimensions.paddingPxToken3)
        InputAddonAlign.InlineEnd -> PaddingValues(end = TwDimensions.paddingPxToken3)
        else -> PaddingValues()
    }
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = BaseTokens.token6) // py-1.5
            .padding(horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val scope = InputAddonScopeImpl(
            rowScope = this,
            contentColor = resolveTextFieldColors().placeholder,
            align = align,
            focusOnPress = focusOnPress,
        )
        scope.content()
    }
}

/** Block-addon container: px-3, pt-2.5 (block-start) / pb-2.5 (block-end) — above/below the input. */
@Composable
internal fun InputAddonBlock(
    align: InputAddonAlign,
    focusOnPress: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable InputAddonScope.() -> Unit,
) {
    val verticalPadding = when (align) {
        InputAddonAlign.BlockStart -> PaddingValues(top = BaseTokens.token10) // pt-2.5
        InputAddonAlign.BlockEnd -> PaddingValues(bottom = BaseTokens.token10) // pb-2.5
        else -> PaddingValues()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = TwDimensions.paddingPxToken3)
            .then(Modifier.padding(verticalPadding)),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val scope = InputAddonScopeImpl(
                rowScope = this,
                contentColor = resolveTextFieldColors().placeholder,
                align = align,
                focusOnPress = focusOnPress,
            )
            scope.content()
        }
    }
}
