package com.github.jershell.shadcn.components.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Resolved chrome of [Table], after the shadcn/ui table:
 * - header: `bg-muted text-muted-foreground font-medium`;
 * - body: `bg-background text-foreground`, `border-b` between rows;
 * - footer: `bg-muted/50 font-medium border-t`;
 * - caption: `text-muted-foreground text-sm`.
 */
internal data class DataTableColors(
    val background: Color,
    val headerBackground: Color,
    val footerBackground: Color,
    val border: Color,
    val borderWidthPx: Float,
    val gripForeground: Color,
    val headerTextStyle: TextStyle,
    val cellTextStyle: TextStyle,
    val footerTextStyle: TextStyle,
    val captionTextStyle: TextStyle,
)

/** shadcn `bg-muted/50` of the footer. */
private const val FooterMutedAlpha = 0.5f

@Composable
internal fun resolveDataTableColors(): DataTableColors {
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val muted = Theme[ColorProps][ColorTokens.muted]
    return DataTableColors(
        background = Theme[ColorProps][ColorTokens.background],
        headerBackground = muted,
        footerBackground = muted.copy(alpha = muted.alpha * FooterMutedAlpha),
        border = Theme[ColorProps][ColorTokens.border],
        borderWidthPx = with(LocalDensity.current) { Theme[DimProps][DimTokens.borderWidth].toPx() },
        gripForeground = mutedForeground,
        headerTextStyle = TypographyStyles.textSmMedium.copy(color = mutedForeground),
        cellTextStyle = TypographyStyles.textSmRegular.copy(color = foreground),
        footerTextStyle = TypographyStyles.textSmMedium.copy(color = foreground),
        captionTextStyle = TypographyStyles.textSmRegular.copy(color = mutedForeground),
    )
}
