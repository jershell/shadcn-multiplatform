package com.github.jershell.shadcn.components.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.composeunstyled.RadioButton
import com.composeunstyled.SelectedIndicator
import com.composeunstyled.UnstyledRadioGroup
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A radio group styled after shadcn/ui.
 *
 * Options are provided as [DataItem] values. Selection is tracked by the item's [DataItem.key].
 *
 * @param items The radio options.
 * @param selectedKey The key of the currently selected option, or `null`.
 * @param onSelectedChange Called when an option is selected. Receives the selected item key.
 * @param enabled Whether the whole group is interactive.
 * @param accessibilityLabel Accessibility label for the group.
 */
@Composable
fun <T> RadioGroup(
    items: List<DataItem<T>>,
    selectedKey: Int?,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accessibilityLabel: String? = null,
) {
    val colors = resolveRadioColors()
    val radius = Theme[DimProps][DimTokens.radiusSm]
    val itemShape = RoundedCornerShape(radius)
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    UnstyledRadioGroup(
        value = selectedKey,
        onValueChange = { onSelectedChange(it) },
        modifier = modifier,
        accessibilityLabel = accessibilityLabel,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken1),
        ) {
            items.forEach { item ->
                val selected = item.key == selectedKey

                RadioButton(
                    value = item.key,
                    enabled = enabled && item.enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(itemShape),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = TwDimensions.paddingPxToken2,
                                vertical = TwDimensions.paddingPxToken1,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(TwDimensions.heightHToken4)
                                .alpha(if (enabled && item.enabled) 1f else 0.5f)
                                .clip(CircleShape)
                                .border(
                                    width = borderWidth,
                                    color = if (selected) Color.Transparent else colors.border,
                                    shape = CircleShape,
                                )
                                .background(
                                    if (selected) colors.selectedBackground else colors.background,
                                    CircleShape,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            SelectedIndicator {
                                Box(
                                    modifier = Modifier
                                        .size(TwDimensions.heightHToken2)
                                        .clip(CircleShape)
                                        .background(colors.selectedContent),
                                )
                            }
                        }

                        BasicText(
                            text = item.title,
                            style = TypographyStyles.textSmRegular.copy(
                                color = if (enabled && item.enabled) colors.content else colors.content.copy(alpha = 0.5f),
                            ),
                        )
                    }
                }
            }
        }
    }
}
