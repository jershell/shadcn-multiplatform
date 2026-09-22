package com.github.jershell.shadcn.components.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.github.jershell.shadcn.theme.TwDimensions

/**
 * Visual sizes of the [Avatar] component.
 */
enum class AvatarSize {
    Xs,
    Sm,
    Default,
    Lg,
    Xl,
}

@Composable
internal fun AvatarSize.toDp(): Dp = when (this) {
    AvatarSize.Xs -> TwDimensions.heightHToken6
    AvatarSize.Sm -> TwDimensions.heightHToken8
    AvatarSize.Default -> TwDimensions.heightHToken10
    AvatarSize.Lg -> TwDimensions.heightHToken12
    AvatarSize.Xl -> TwDimensions.heightHToken20
}
