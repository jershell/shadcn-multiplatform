package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.avatar.Avatar
import com.github.jershell.shadcn.components.avatar.AvatarFallback
import com.github.jershell.shadcn.components.avatar.AvatarImage
import com.github.jershell.shadcn.components.avatar.AvatarSize
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_ab
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_avatar_image
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_cn
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_compose_avatarimage_and_avatarfallback_manually
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_fallback_text_is_shown_while_the_image_loads_or
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_jd
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_loads_an_image_with_coil_falls_back_to_initials
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_shadcn_style_api
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_sizes
import com.github.jershell.shadcn.demoapp.generated.resources.avatar_with_image
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoAvatar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.avatar_sizes))
            P(stringResource(Res.string.avatar_fallback_text_is_shown_while_the_image_loads_or))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(fallbackText = stringResource(Res.string.avatar_jd), size = AvatarSize.Xs)
                Avatar(fallbackText = stringResource(Res.string.avatar_jd), size = AvatarSize.Sm)
                Avatar(fallbackText = stringResource(Res.string.avatar_jd), size = AvatarSize.Default)
                Avatar(fallbackText = stringResource(Res.string.avatar_jd), size = AvatarSize.Lg)
                Avatar(fallbackText = stringResource(Res.string.avatar_jd), size = AvatarSize.Xl)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.avatar_with_image))
            P(stringResource(Res.string.avatar_loads_an_image_with_coil_falls_back_to_initials))
            Avatar(
                model = "https://github.com/shadcn.png",
                fallbackText = stringResource(Res.string.avatar_cn),
                size = AvatarSize.Lg,
                contentDescription = stringResource(Res.string.avatar_avatar_image),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.avatar_shadcn_style_api))
            P(stringResource(Res.string.avatar_compose_avatarimage_and_avatarfallback_manually))
            Avatar(size = AvatarSize.Lg) {
                AvatarImage(
                    model = "invalid-url",
                    contentDescription = null,
                    fallback = { AvatarFallback(stringResource(Res.string.avatar_ab)) },
                )
            }
        }
    }
}
