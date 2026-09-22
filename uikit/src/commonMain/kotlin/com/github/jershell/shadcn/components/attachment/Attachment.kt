package com.github.jershell.shadcn.components.attachment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Paperclip
import com.composables.icons.lucide.X
import com.github.jershell.shadcn.components.icon.ShadcnIcon
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.attachment_open
import com.github.jershell.shadcn.generated.resources.attachment_remove
import com.github.jershell.shadcn.generated.resources.attachment_status_upload_failed
import com.github.jershell.shadcn.generated.resources.attachment_status_uploaded
import com.github.jershell.shadcn.generated.resources.attachment_status_uploading
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Upload state of an attachment.
 */
sealed interface AttachmentStatus {

    /** Ready, nothing in flight. */
    data object Idle : AttachmentStatus

    /** Upload in progress; [progress] in `0f..1f`. */
    data class Uploading(val progress: Float) : AttachmentStatus

    /** Fully uploaded. */
    data object Uploaded : AttachmentStatus

    /** Upload failed; [message] describes the reason. */
    data class Error(val message: String? = null) : AttachmentStatus
}

/**
 * A single attachment card: media preview, file metadata, upload state and actions.
 *
 * @param name File name shown as the title.
 * @param size Optional size text (e.g. "2.4 MB") shown under the title.
 * @param preview Optional image preview; when `null` the [icon] glyph is shown instead.
 * @param icon Fallback file icon used when [preview] is absent.
 * @param status Current upload state.
 */
data class AttachmentSpec(
    val name: String,
    val size: String? = null,
    val preview: Painter? = null,
    val icon: ShadcnIcon? = null,
    val status: AttachmentStatus = AttachmentStatus.Idle,
)

@Composable
internal fun resolveAttachmentColors(): AttachmentColors = AttachmentColors(
    container = Theme[ColorProps][ColorTokens.background],
    border = Theme[ColorProps][ColorTokens.border],
    foreground = Theme[ColorProps][ColorTokens.foreground],
    muted = Theme[ColorProps][ColorTokens.muted],
    mutedContent = Theme[ColorProps][ColorTokens.mutedForeground],
    destructive = Theme[ColorProps][ColorTokens.destructive],
)

internal class AttachmentColors(
    val container: Color,
    val border: Color,
    val foreground: Color,
    val muted: Color,
    val mutedContent: Color,
    val destructive: Color,
)

/**
 * A single attachment card styled after the shadcn/ui Attachment (item-based):
 * `rounded-lg border bg-background p-2` with a size-10 preview, name and
 * metadata, optional progress line and trailing actions.
 *
 * @param spec Attachment data.
 * @param onRemove Called when the remove action is tapped; `null` hides the button.
 * @param onOpen Called when the open action is tapped; `null` hides the button.
 * @param enabled Whether the actions are interactive (loading state dims them).
 */
@Composable
fun Attachment(
    spec: AttachmentSpec,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val colors = resolveAttachmentColors()
    val radius = Theme[DimProps][DimTokens.radiusLg]
    val previewRadius = Theme[DimProps][DimTokens.radiusMd]
    val containerShape = RoundedCornerShape(radius)
    val previewShape = RoundedCornerShape(previewRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(containerShape)
            .background(colors.container)
            .border(BaseTokens.token1, colors.border, containerShape)
            .padding(BaseTokens.token8), // p-2
        verticalArrangement = Arrangement.spacedBy(BaseTokens.token8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BaseTokens.token8),
        ) {
            // Preview: size-10 rounded-md bg-muted; image or fallback icon
            Box(
                modifier = Modifier
                    .size(BaseTokens.token40)
                    .clip(previewShape)
                    .background(colors.muted),
                contentAlignment = Alignment.Center,
            ) {
                val preview = spec.preview
                if (preview != null) {
                    Image(
                        painter = preview,
                        contentDescription = spec.name,
                        modifier = Modifier.size(BaseTokens.token40),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    val vector = (spec.icon as? ShadcnIcon.Vector)?.imageVector
                    if (vector != null) {
                        UnstyledIcon(
                            imageVector = vector,
                            contentDescription = null,
                            modifier = Modifier.size(BaseTokens.token20),
                            tint = colors.mutedContent,
                        )
                    }
                }
            }

            // Metadata: title + size, min-w-0 for ellipsis
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp), // gap-0.5
            ) {
                BasicText(
                    text = spec.name,
                    style = TypographyStyles.textSmMedium.copy(color = colors.foreground),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val statusText = when (val status = spec.status) {
                    is AttachmentStatus.Error -> status.message
                        ?: stringResource(Res.string.attachment_status_upload_failed)
                    AttachmentStatus.Idle -> null
                    AttachmentStatus.Uploaded -> stringResource(Res.string.attachment_status_uploaded)
                    is AttachmentStatus.Uploading -> stringResource(Res.string.attachment_status_uploading)
                }
                if (spec.size != null || statusText != null) {
                    BasicText(
                        text = listOfNotNull(spec.size, statusText).joinToString(" · "),
                        style = TypographyStyles.textXsRegular.copy(color = colors.mutedContent),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BaseTokens.token2),
            ) {
                if (onOpen != null) {
                    AttachmentAction(
                        icon = Lucide.Paperclip,
                        contentDescription = stringResource(Res.string.attachment_open, spec.name),
                        colors = colors,
                        enabled = enabled,
                        onClick = onOpen,
                    )
                }
                if (onRemove != null) {
                    AttachmentAction(
                        icon = Lucide.X,
                        contentDescription = stringResource(Res.string.attachment_remove, spec.name),
                        colors = colors,
                        enabled = enabled,
                        onClick = onRemove,
                    )
                }
            }
        }

        // Progress line while uploading
        when (val status = spec.status) {
            is AttachmentStatus.Uploading -> {
                com.github.jershell.shadcn.components.progress.Progress(
                    value = status.progress,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            else -> Unit
        }
    }
}

/**
 * Vertical list of [Attachment] cards.
 *
 * @param specs Attachments to render.
 * @param onRemove Called with the tapped attachment; `null` hides all remove buttons.
 * @param onOpen Called with the tapped attachment; `null` hides all open buttons.
 * @param enabled Whether the actions are interactive.
 * @param spacing Vertical gap between cards.
 */
@Composable
fun AttachmentList(
    specs: List<AttachmentSpec>,
    modifier: Modifier = Modifier,
    onRemove: ((AttachmentSpec) -> Unit)? = null,
    onOpen: ((AttachmentSpec) -> Unit)? = null,
    enabled: Boolean = true,
    spacing: androidx.compose.ui.unit.Dp = BaseTokens.token8,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        specs.forEach { spec ->
            Attachment(
                spec = spec,
                onRemove = onRemove?.let { callback -> { callback(spec) } },
                onOpen = onOpen?.let { callback -> { callback(spec) } },
                enabled = enabled,
            )
        }
    }
}

@Composable
private fun AttachmentAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    colors: AttachmentColors,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(BaseTokens.token32) // size-8
            .clip(RoundedCornerShape(Theme[DimProps][DimTokens.radiusSm]))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        UnstyledIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(BaseTokens.token16), // size-4
            tint = colors.mutedContent,
        )
    }
}
