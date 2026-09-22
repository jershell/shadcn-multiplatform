package com.github.jershell.shadcn.components.switch

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.composeunstyled.SwitchThumb
import com.composeunstyled.UnstyledSwitch
import com.composeunstyled.focusRing
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.Effects

private val SwitchWidth = 32.dp
private val SwitchHeight = 18.4.dp
private val ThumbSize = 16.dp
private val ThumbPaddingHorizontal = 2.dp
private val ThumbPaddingVertical = 1.2.dp

/**
 * A switch styled after shadcn/ui.
 *
 * @param checked Whether the switch is on.
 * @param onCheckedChange Called when the checked state changes.
 * @param enabled Whether the switch is interactive.
 */
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = resolveSwitchColors()
    val interactionSource = remember { MutableInteractionSource() }
    val trackShape = RoundedCornerShape(50)
    val focusRingColor = Theme[ColorProps][ColorTokens.ring]
    val focusRingWidth = Effects.boxShadowFocusRing.spread

    UnstyledSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier
            .size(SwitchWidth, SwitchHeight)
            .focusRing(
                interactionSource = interactionSource,
                width = focusRingWidth,
                color = focusRingColor,
                shape = trackShape,
            ),
        interactionSource = interactionSource,
        indication = null,
    ) {
        val trackColor = if (checked) colors.trackChecked else colors.trackUnchecked
        val thumbColor = if (checked) colors.thumbChecked else colors.thumbUnchecked

        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (enabled) 1f else colors.disabledAlpha)
                .shadow(
                    elevation = Effects.boxShadowShadowXs.radius,
                    shape = trackShape,
                    clip = false,
                )
                .clip(trackShape)
                .background(trackColor),
        )

        SwitchThumb(
            modifier = Modifier
                .height(SwitchHeight)
                .width(ThumbSize + ThumbPaddingHorizontal * 2),
            animationSpec = spring(stiffness = 500f, dampingRatio = 0.75f),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = ThumbPaddingHorizontal, vertical = ThumbPaddingVertical)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .alpha(if (enabled) 1f else colors.disabledAlpha)
                    .background(thumbColor),
            )
        }
    }
}
