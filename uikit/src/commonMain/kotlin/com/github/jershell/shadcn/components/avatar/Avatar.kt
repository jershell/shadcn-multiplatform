package com.github.jershell.shadcn.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A circular image container styled after shadcn/ui.
 *
 * This is the low-level composable that accepts arbitrary content. For a simple image + fallback
 * text use the convenience overload below.
 *
 * @param modifier Modifier applied to the avatar container.
 * @param size Visual size of the avatar.
 * @param content Content of the avatar, usually an [AvatarImage] and an [AvatarFallback].
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Default,
    content: @Composable () -> Unit,
) {
    val colors = resolveAvatarColors()
    val sizeDp = size.toDp()

    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(colors.fallbackBackground, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * Convenience overload for the common image + fallback text case.
 *
 * @param model Image model passed to Coil (URL, resource, file, etc.).
 * @param modifier Modifier applied to the avatar container.
 * @param size Visual size of the avatar.
 * @param contentDescription Accessibility description of the image.
 * @param fallbackText Text shown while the image is loading or if it fails to load.
 */
@Composable
fun Avatar(
    model: Any? = null,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Default,
    contentDescription: String? = null,
    fallbackText: String? = null,
) {
    Avatar(modifier = modifier, size = size) {
        AvatarImage(
            model = model,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            fallback = {
                if (fallbackText != null) {
                    AvatarFallback(text = fallbackText)
                }
            },
        )
    }
}

/**
 * Image part of the avatar. Uses Coil to load the image asynchronously.
 *
 * @param model Image model passed to Coil.
 * @param contentDescription Accessibility description of the image.
 * @param modifier Modifier applied to the image.
 * @param fallback Composable shown while loading or on error.
 */
@Composable
fun AvatarImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallback: @Composable () -> Unit = {},
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    ) {
        val scope = this
        val state by painter.state.collectAsState()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (state is AsyncImagePainter.State.Success) {
                scope.SubcomposeAsyncImageContent()
            } else {
                fallback()
            }
        }
    }
}

/**
 * Fallback text shown when the avatar image is not available.
 *
 * @param text The fallback text, typically initials.
 * @param modifier Modifier applied to the text.
 */
@Composable
fun AvatarFallback(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveAvatarColors()
    BasicText(
        text = text,
        modifier = modifier,
        style = TypographyStyles.textXsMedium.copy(
            color = colors.fallbackContent,
            textAlign = TextAlign.Center,
        ),
    )
}
