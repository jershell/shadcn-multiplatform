package com.github.jershell.shadcn.components.collapsible

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import com.composeunstyled.UnstyledDisclosureButton
import com.composeunstyled.UnstyledDisclosedContent
import com.composeunstyled.UnstyledDisclosure
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Scope of the [Collapsible] content.
 */
interface CollapsibleScope {

    /**
     * The styled trigger row: bordered, rounded, with a trailing chevron that rotates
     * 180 degrees while the collapsible is expanded. Clicking it toggles the state.
     *
     * @param text Trigger label.
     * @param modifier Modifier applied to the trigger row.
     * @param enabled Whether the trigger is interactive.
     */
    @Composable
    fun Trigger(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    )

    /**
     * A trigger row with custom content. The collapsible state and the toggle behavior
     * are handled by the group; render a [chevron][CollapsibleDefaults.Chevron] manually
     * if needed.
     *
     * @param modifier Modifier applied to the trigger row.
     * @param enabled Whether the trigger is interactive.
     * @param content Row content.
     */
    @Composable
    fun Trigger(
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit,
    )

    /**
     * The collapsible body, shown and hidden with a vertical expand/shrink animation.
     *
     * @param modifier Modifier applied to the body container.
     * @param content Body content, laid out vertically.
     */
    @Composable
    fun Content(
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit,
    )
}

internal class CollapsibleScopeImpl(
    private val expanded: Boolean,
) : CollapsibleScope {

    @Composable
    override fun Trigger(
        text: String,
        modifier: Modifier,
        enabled: Boolean,
    ) {
        TriggerRow(
            modifier = modifier,
            enabled = enabled,
            expanded = expanded,
            text = text,
            content = null,
        )
    }

    @Composable
    override fun Trigger(
        modifier: Modifier,
        enabled: Boolean,
        content: @Composable RowScope.() -> Unit,
    ) {
        TriggerRow(
            modifier = modifier,
            enabled = enabled,
            expanded = expanded,
            text = null,
            content = content,
        )
    }

    @Composable
    override fun Content(
        modifier: Modifier,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        UnstyledDisclosedContent(
            modifier = modifier,
            enter = fadeIn(tween(200)) + expandVertically(expandFrom = Alignment.Top, animationSpec = tween(200)),
            exit = fadeOut(tween(200)) + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(200)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BaseTokens.token8), // space-y-2 gap from the trigger
                verticalArrangement = Arrangement.spacedBy(BaseTokens.token8), // space-y-2
                content = content,
            )
        }
    }
}

@Composable
private fun TriggerRow(
    modifier: Modifier,
    enabled: Boolean,
    expanded: Boolean,
    text: String?,
    content: (@Composable RowScope.() -> Unit)?,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val colors = resolveCollapsibleColors(isHovered)
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val shape = RoundedCornerShape(radius)

    UnstyledDisclosureButton(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        modifier = modifier
            .fillMaxWidth()
            .focusRing(
                interactionSource = interactionSource,
                width = Effects.boxShadowFocusRing.spread,
                color = colors.focusRing,
                shape = shape,
            )
            .clip(shape)
            .border(borderWidth, colors.border, shape)
            .background(colors.container, shape),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = TwDimensions.paddingPxToken4, // px-4
                vertical = BaseTokens.token14, // py-3.5
            ),
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (text != null) {
                BasicText(
                    text = text,
                    style = TypographyStyles.textSmMedium.copy(color = colors.content),
                )
                // justify-between: text on the left, chevron at the right edge
                Spacer(Modifier.weight(1f))
                CollapsibleDefaults.Chevron(expanded = expanded)
            }
            content?.invoke(this)
        }
    }
}

/**
 * Reusable pieces of the collapsible trigger.
 */
object CollapsibleDefaults {

    /**
     * The trailing chevron of the default trigger; rotates 180 degrees while expanded.
     */
    @Composable
    fun Chevron(
        expanded: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val rotation by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            animationSpec = tween(200),
            label = "collapsibleChevronRotation",
        )
        val colors = resolveCollapsibleColors(isHovered = false)

        ShadcnIconContent(
            icon = Lucide.ChevronDown.toShadcnIcon(),
            contentDescription = null,
            modifier = modifier
                .size(TwDimensions.heightHToken4) // size-4
                .graphicsLayer {
                    rotationZ = rotation
                },
            tint = colors.icon,
        )
    }
}

/**
 * A container that shows or hides its content on demand, styled after the shadcn/ui
 * Collapsible docs demo: a bordered rounded trigger row and an animated body.
 *
 * Built on [com.composeunstyled.UnstyledDisclosure], which provides the expand/collapse
 * accessibility actions on the trigger.
 *
 * Build the content with [CollapsibleScope.Trigger] and [CollapsibleScope.Content].
 *
 * @param expanded Whether the body is visible.
 * @param onExpandedChange Called when the user asks to expand or collapse.
 * @param modifier Modifier applied to the container.
 * @param content Content with the trigger and the collapsible body.
 */
@Composable
fun Collapsible(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable CollapsibleScope.() -> Unit,
) {
    UnstyledDisclosure(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        // The primitive wraps the content in a Box (a stack): lay the trigger and
        // body out in a column so expanding grows the container downward instead
        // of overlapping the trigger.
        Column {
            val scope = remember(expanded) { CollapsibleScopeImpl(expanded) }
            scope.content()
        }
    }
}
