package com.github.jershell.shadcn.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

private val LocalItemIconTint = staticCompositionLocalOf { Color.Unspecified }

/**
 * Visual variant of an [Item], matching the reference `itemVariants`:
 * `default` — transparent, `outline` — bordered surface, `muted` — `bg-muted/50`.
 */
enum class ItemVariant {
    Default,
    Outline,
    Muted,
}

enum class ItemSize {
    Default,
    Sm,

    /** Compact extension beyond the reference (`px-3 py-2 gap-2`, media `size-7`). */
    Xs,
}

enum class ItemMediaVariant {
    Default,
    Icon,
    Image,
    Avatar,
}

private data class ItemSizeSpec(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val gap: Dp,
    val mediaSize: Dp,
)

@Composable
private fun ItemSize.spec(): ItemSizeSpec = when (this) {
    ItemSize.Default -> ItemSizeSpec(
        horizontalPadding = TwDimensions.paddingPxToken4,
        verticalPadding = TwDimensions.paddingPxToken4,
        gap = TwDimensions.gapGapToken4,
        mediaSize = TwDimensions.heightHToken10,
    )

    ItemSize.Sm -> ItemSizeSpec(
        horizontalPadding = TwDimensions.paddingPxToken4,
        verticalPadding = TwDimensions.paddingPxToken3,
        gap = TwDimensions.gapGapN2_5,
        mediaSize = TwDimensions.heightHToken8,
    )

    ItemSize.Xs -> ItemSizeSpec(
        horizontalPadding = TwDimensions.paddingPxToken3,
        verticalPadding = TwDimensions.paddingPxToken2,
        gap = TwDimensions.gapGapToken2,
        mediaSize = TwDimensions.heightHToken7,
    )
}

/**
 * Group of items rendered as a vertical list, matching the shadcn/ui `ItemGroup`
 * (`flex flex-col`); separate rows with [ItemSeparator].
 */
@Composable
fun ItemGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        content = content,
    )
}

/** Full-width hairline between items, matching `ItemSeparator` (a Separator). */
@Composable
fun ItemSeparator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Theme[DimProps][DimTokens.borderWidth])
            .background(Theme[ColorProps][ColorTokens.border]),
    )
}

/** Shared padding slot for the header, main row, and footer. */
@Composable
private fun ItemSlot(
    horizontalPadding: Dp,
    verticalPadding: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            ),
    ) {
        content()
    }
}

/**
 * shadcn/ui `Item`: a rounded card row with optional `header`/`footer` slots and
 * a main content row ([ItemMedia] + [ItemContent] + [ItemActions]).
 *
 * Container styling follows the reference `itemVariants`: transparent by default,
 * bordered for [ItemVariant.Outline], `bg-muted/50` for [ItemVariant.Muted];
 * a clickable item shows `bg-accent/50` on hover/press and the standard
 * `ring-ring/50` focus ring.
 *
 * @param variant Visual variant.
 * @param size Spacing scale; also defaults [ItemMedia] sizing.
 * @param enabled Disables the click handler when [onClick] is set.
 * @param onClick Makes the item clickable (hover/press/focus states apply).
 * @param header Optional slot rendered above the content row.
 * @param footer Optional slot rendered below the content row.
 * @param content Main row: typically [ItemMedia], [ItemContent], [ItemActions].
 */
@Composable
fun Item(
    modifier: Modifier = Modifier,
    variant: ItemVariant = ItemVariant.Default,
    size: ItemSize = ItemSize.Default,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveItemContainerColors(variant)
    val spec = size.spec()
    val shape = RoundedCornerShape(Theme[DimProps][DimTokens.radiusMd])
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val interactive = onClick != null
    val showHoverBackground = interactive && enabled && (isHovered || isPressed)
    val background = if (showHoverBackground) colors.hoverBackground else colors.background

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(background, shape)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = colors.focusRing,
                shape = shape,
            )
            .then(
                if (interactive) {
                    Modifier.clickable(
                        enabled = enabled,
                        onClick = onClick!!,
                        interactionSource = interactionSource,
                        indication = null,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        header?.let {
            ItemSlot(
                horizontalPadding = spec.horizontalPadding,
                verticalPadding = spec.verticalPadding,
            ) {
                it()
            }
        }

        ItemSlot(
            horizontalPadding = spec.horizontalPadding,
            verticalPadding = spec.verticalPadding,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spec.gap),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }

        footer?.let {
            ItemSlot(
                horizontalPadding = spec.horizontalPadding,
                verticalPadding = spec.verticalPadding,
            ) {
                it()
            }
        }
    }
}

/**
 * Media slot of an [Item]: icon box, image, or avatar, matching the reference
 * `itemMediaVariants` (`icon`: bordered `bg-muted` box with a small icon,
 * `image`: rounded picture frame, `avatar`: circular).
 *
 * @param variant Media kind; controls shape, size, and decoration.
 * @param size Spacing scale; defaults to the size passed to [Item].
 */
@Composable
fun ItemMedia(
    modifier: Modifier = Modifier,
    variant: ItemMediaVariant = ItemMediaVariant.Default,
    size: ItemSize = ItemSize.Default,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = resolveItemMediaColors(variant)
    val spec = size.spec()
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape: Shape = when (variant) {
        ItemMediaVariant.Avatar -> CircleShape
        else -> RoundedCornerShape(Theme[DimProps][DimTokens.radiusSm])
    }

    val decoratedModifier = when (variant) {
        ItemMediaVariant.Default -> modifier
        ItemMediaVariant.Icon -> modifier
            .size(spec.mediaSize)
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(colors.background, shape)
        ItemMediaVariant.Image -> modifier
            .size(spec.mediaSize)
            .clip(shape)
        ItemMediaVariant.Avatar -> modifier
            .size(spec.mediaSize)
            .clip(shape)
            .background(colors.background, shape)
    }

    val iconTint = when (variant) {
        ItemMediaVariant.Icon -> resolveItemTextColors().description
        ItemMediaVariant.Avatar -> resolveItemTextColors().title
        else -> Color.Unspecified
    }

    Box(
        modifier = decoratedModifier,
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalItemIconTint provides iconTint) {
            content()
        }
    }
}

/**
 * Text column of an [Item]: expands to push [ItemActions] toward the end edge,
 * matching the reference `flex-1`.
 */
@Composable
fun RowScope.ItemContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        content = content,
    )
}

/** Item heading text (`text-sm font-medium`), single line with an ellipsis. */
@Composable
fun ItemTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmMedium.copy(color = resolveItemTextColors().title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/** Item supporting text (`text-muted-foreground text-sm`), clamped to two lines. */
@Composable
fun ItemDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textSmRegular.copy(color = resolveItemTextColors().description),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Trailing action slot of an [Item] (`gap-2`); contained icons are tinted with
 * the muted description color by default.
 */
@Composable
fun ItemActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalItemIconTint provides resolveItemTextColors().description) {
            content()
        }
    }
}

/**
 * Header row of an [Item] (`basis-full justify-between`); use inside the
 * `header` slot of [Item].
 */
@Composable
fun ItemHeader(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * Footer row of an [Item] (`basis-full justify-between`); use inside the
 * `footer` slot of [Item].
 */
@Composable
fun ItemFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Icon inside [ItemMedia]/[ItemActions], tinted by the enclosing slot. */
@Composable
fun ItemIcon(
    icon: ShadcnIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    ShadcnIconContent(
        icon = icon,
        contentDescription = contentDescription,
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = LocalItemIconTint.current,
    )
}

@Composable
fun ItemIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ItemIcon(
    icon = imageVector.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)

@Composable
fun ItemIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
): Unit = ItemIcon(
    icon = painter.toShadcnIcon(),
    contentDescription = contentDescription,
    modifier = modifier,
)
