package com.github.jershell.shadcn.components.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composeunstyled.DisclosureButton
import com.composeunstyled.DisclosureScope
import com.composeunstyled.UnstyledDisclosure
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions

internal class AccordionState(
    val expandedKeys: Set<Int>,
    val type: AccordionType,
    val collapsible: Boolean,
    private val onChange: (Set<Int>) -> Unit,
) {
    fun isExpanded(key: Int): Boolean = key in expandedKeys

    fun toggle(key: Int, expanded: Boolean) {
        if (!expanded && !collapsible) return
        val newKeys = when (type) {
            AccordionType.Single -> if (expanded) setOf(key) else emptySet()
            AccordionType.Multiple -> if (expanded) expandedKeys + key else expandedKeys - key
        }
        onChange(newKeys)
    }
}

internal data class AccordionItemInfo(
    val key: Int,
    val expanded: Boolean,
    val enabled: Boolean,
)

internal val LocalAccordionState = staticCompositionLocalOf<AccordionState?> { null }
internal val LocalAccordionItemInfo = staticCompositionLocalOf<AccordionItemInfo?> { null }
internal val LocalAccordionItemIndex = staticCompositionLocalOf<Int> { 0 }

/**
 * A vertically stacked set of collapsible sections, styled after shadcn/ui.
 *
 * Built on top of [compose-unstyled] [UnstyledDisclosure]. Supports single or
 * multiple expanded items, and fully collapsible or always-open-one behavior.
 *
 * @param expandedKeys Set of keys of the currently expanded items.
 * @param onExpandedKeysChange Called when the expanded set changes.
 * @param modifier Modifier applied to the accordion container.
 * @param type Whether only one or multiple items can be expanded at once.
 * @param collapsible Whether the last expanded item can be collapsed.
 * @param content Accordion items defined with [AccordionItem], [AccordionTrigger], [AccordionContent].
 */
@Composable
fun Accordion(
    expandedKeys: Set<Int>,
    onExpandedKeysChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    type: AccordionType = AccordionType.Single,
    collapsible: Boolean = true,
    content: @Composable () -> Unit,
) {
    val state = remember(expandedKeys, type, collapsible, onExpandedKeysChange) {
        AccordionState(expandedKeys, type, collapsible, onExpandedKeysChange)
    }
    val colors = resolveAccordionColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    CompositionLocalProvider(LocalAccordionState provides state) {
        Column(
            modifier = modifier
                .clip(shape)
                .border(borderWidth, colors.border, shape)
                .background(colors.background, shape),
        ) {
            CompositionLocalProvider(LocalAccordionItemIndex provides 0) {
                content()
            }
        }
    }
}

/**
 * A single collapsible section inside an [Accordion].
 *
 * @param key Unique identifier of this item. Used for selection tracking.
 * @param modifier Modifier applied to the item wrapper.
 * @param enabled Whether the item trigger is interactive.
 * @param content Define the trigger with [AccordionTrigger] and the panel with [AccordionContent].
 */
@Composable
fun AccordionItem(
    key: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable DisclosureScope.() -> Unit,
) {
    val state = LocalAccordionState.current
        ?: error("AccordionItem must be used inside an Accordion")
    val index = LocalAccordionItemIndex.current
    val colors = resolveAccordionColors()
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val expanded = state.isExpanded(key)
    val itemModifier = if (index > 0) {
        modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = borderWidth.toPx()
                drawLine(
                    color = colors.border,
                    start = Offset(0f, strokeWidth / 2),
                    end = Offset(size.width, strokeWidth / 2),
                    strokeWidth = strokeWidth,
                )
            }
    } else {
        modifier.fillMaxWidth()
    }

    CompositionLocalProvider(
        LocalAccordionItemInfo provides AccordionItemInfo(key, expanded, enabled),
        LocalAccordionItemIndex provides index + 1,
    ) {
        UnstyledDisclosure(
            expanded = expanded,
            onExpandedChange = { if (enabled) state.toggle(key, it) },
            modifier = itemModifier,
        ) {
            val disclosureScope = this
            Column(modifier = Modifier.fillMaxWidth()) {
                with(disclosureScope) {
                    content()
                }
            }
        }
    }
}

/**
 * The clickable header of an [AccordionItem].
 *
 * Renders a full-width row with optional leading icon, custom content, and a rotating
 * chevron that points down when collapsed and up when expanded.
 *
 * @param modifier Modifier applied to the trigger button.
 * @param leadingIcon Optional icon displayed before the custom content.
 * @param content The trigger content, usually a title.
 */
@Composable
fun DisclosureScope.AccordionTrigger(
    modifier: Modifier = Modifier,
    leadingIcon: ShadcnIcon? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = resolveAccordionColors()
    val itemInfo = LocalAccordionItemInfo.current
        ?: error("AccordionTrigger must be used inside an AccordionItem")
    val expanded = itemInfo.expanded
    val enabled = itemInfo.enabled
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "accordionChevronRotation",
    )

    DisclosureButton(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = TwDimensions.paddingPxToken4,
                    vertical = TwDimensions.paddingPxToken3,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.let { icon ->
                    ShadcnIconContent(
                        icon = icon,
                        contentDescription = null,
                        modifier = Modifier.size(TwDimensions.heightHToken4),
                        tint = colors.triggerContent,
                    )
                }
                content()
            }
            Box(modifier = Modifier.rotate(rotation)) {
                ShadcnIconContent(
                    icon = Lucide.ChevronDown.toShadcnIcon(),
                    contentDescription = null,
                    modifier = Modifier.size(TwDimensions.heightHToken4),
                    tint = colors.chevron,
                )
            }
        }
    }
}

/**
 * The expandable panel of an [AccordionItem].
 *
 * @param modifier Modifier applied to the panel container.
 * @param content The panel content.
 */
@Composable
fun DisclosureScope.AccordionContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val itemInfo = LocalAccordionItemInfo.current
        ?: error("AccordionContent must be used inside an AccordionItem")

    AnimatedVisibility(
        visible = itemInfo.expanded,
        modifier = modifier.fillMaxWidth(),
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
    ) {
        Box(
            modifier = Modifier.padding(
                start = TwDimensions.paddingPxToken4,
                end = TwDimensions.paddingPxToken4,
                bottom = TwDimensions.paddingPxToken4,
            ),
        ) {
            content()
        }
    }
}

