package com.github.jershell.shadcn.components.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.composeunstyled.CheckedIndicator
import com.composeunstyled.UnstyledCheckbox
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A checkbox styled after shadcn/ui.
 *
 * The whole row is clickable when a [label] is provided. The checked indicator is animated.
 *
 * @param checked Whether the checkbox is checked.
 * @param onCheckedChange Called when the checked state changes.
 * @param enabled Whether the checkbox is interactive.
 * @param label Optional text label rendered next to the checkbox.
 */
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    val colors = resolveCheckboxColors()
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val shape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    UnstyledCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier,
        accessibilityLabel = label,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(TwDimensions.heightHToken4)
                    .alpha(if (enabled) 1f else 0.5f)
                    .clip(shape)
                    .border(
                        width = borderWidth,
                        color = if (checked) Color.Transparent else colors.border,
                        shape = shape,
                    )
                    .background(
                        if (checked) colors.checkedBackground else colors.background,
                        shape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CheckedIndicator {
                    UnstyledIcon(
                        imageVector = Lucide.Check,
                        contentDescription = null,
                        modifier = Modifier.size(TwDimensions.heightHToken3),
                        tint = colors.checkedContent,
                    )
                }
            }

            if (label != null) {
                BasicText(
                    text = label,
                    style = TypographyStyles.textSmRegular.copy(
                        color = if (enabled) colors.content else colors.content.copy(alpha = 0.5f),
                    ),
                )
            }
        }
    }
}
