package com.github.jershell.shadcn.components.breadcrumb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Ellipsis
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.components.icon.ShadcnIconContent
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.breadcrumb_list
import com.github.jershell.shadcn.generated.resources.breadcrumb_more
import com.github.jershell.shadcn.generated.resources.breadcrumb_root

/**
 * Top-level navigation container for breadcrumbs.
 *
 * @param modifier Modifier applied to the container.
 * @param content Usually a [BreadcrumbList].
 */
@Composable
fun Breadcrumb(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val breadcrumbDescription = stringResource(Res.string.breadcrumb_root)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = breadcrumbDescription },
    ) {
        content()
    }
}

/**
 * A horizontally scrollable list of breadcrumb items.
 *
 * @param modifier Modifier applied to the list.
 * @param content Items, separators, and ellipsis content.
 */
@Composable
fun BreadcrumbList(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val scrollState = rememberScrollState()
    val breadcrumbListDescription = stringResource(Res.string.breadcrumb_list)
    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .fillMaxWidth()
            .semantics { contentDescription = breadcrumbListDescription },
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * A single item in a breadcrumb, usually wrapping a [BreadcrumbLink] or [BreadcrumbPage].
 *
 * @param modifier Modifier applied to the item.
 * @param content Item content.
 */
@Composable
fun BreadcrumbItem(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * A clickable link inside a breadcrumb.
 *
 * Uses the muted theme color and switches to the foreground color on hover.
 *
 * @param text Link text.
 * @param onClick Action invoked when the link is clicked.
 * @param modifier Modifier applied to the link.
 * @param leadingIcon Optional icon displayed before the text.
 * @param trailingIcon Optional icon displayed after the text.
 */
@Composable
fun BreadcrumbLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ShadcnIcon? = null,
    trailingIcon: ShadcnIcon? = null,
) {
    val colors = resolveBreadcrumbColors()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val color = if (isHovered) colors.foreground else colors.muted

    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = interactionSource,
            ),
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.let { icon ->
            ShadcnIconContent(
                icon = icon,
                contentDescription = null,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                tint = color,
            )
        }
        BasicText(
            text = text,
            style = TypographyStyles.textSmRegular.copy(color = color),
        )
        trailingIcon?.let { icon ->
            ShadcnIconContent(
                icon = icon,
                contentDescription = null,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                tint = color,
            )
        }
    }
}

/**
 * The current page element in a breadcrumb.
 *
 * Marked with [selected] semantics for accessibility.
 *
 * @param text Current page text.
 * @param modifier Modifier applied to the page element.
 */
@Composable
fun BreadcrumbPage(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveBreadcrumbColors()
    BasicText(
        text = text,
        modifier = modifier.semantics { selected = true },
        style = TypographyStyles.textSmRegular.copy(color = colors.foreground),
    )
}

/**
 * Visual separator between breadcrumb items. Hidden from accessibility by default.
 *
 * @param modifier Modifier applied to the separator.
 * @param icon Icon used as the separator. Defaults to a chevron pointing right.
 */
@Composable
fun BreadcrumbSeparator(
    modifier: Modifier = Modifier,
    icon: ShadcnIcon = Lucide.ChevronRight.toShadcnIcon(),
) {
    val colors = resolveBreadcrumbColors()
    ShadcnIconContent(
        icon = icon,
        contentDescription = null,
            modifier = modifier
                .size(TwDimensions.heightHToken4)
                .semantics { hideFromAccessibility() },
        tint = colors.separator,
    )
}

/**
 * An ellipsis indicator for collapsed breadcrumb items.
 *
 * @param modifier Modifier applied to the ellipsis.
 * @param icon Icon used as the ellipsis. Defaults to three horizontal dots.
 */
@Composable
fun BreadcrumbEllipsis(
    modifier: Modifier = Modifier,
    icon: ShadcnIcon = Lucide.Ellipsis.toShadcnIcon(),
) {
    val colors = resolveBreadcrumbColors()
    ShadcnIconContent(
        icon = icon,
        contentDescription = stringResource(Res.string.breadcrumb_more),
        modifier = modifier.size(TwDimensions.heightHToken4),
        tint = colors.separator,
    )
}
