package com.github.jershell.shadcn.components.textarea

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import com.composeunstyled.TextInput
import com.composeunstyled.UnstyledTextField
import com.composeunstyled.focusRing
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.input.InputAddonAlign
import com.github.jershell.shadcn.components.input.InputAddonBlock
import com.github.jershell.shadcn.components.input.InputAddonInline
import com.github.jershell.shadcn.components.input.InputAddonScope
import com.github.jershell.shadcn.components.input.resolveTextFieldColors
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A multi-line text field styled after shadcn/ui [Textarea](https://ui.shadcn.com/docs/components/textarea).
 *
 * The text content is managed by a [TextFieldState] from Compose Foundation. Obtain a state with
 * [androidx.compose.foundation.text.input.rememberTextFieldState].
 *
 * @param state Text field state.
 * @param placeholder Text shown when the field is empty.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the text can be edited.
 * @param isInvalid Shows the shadcn `aria-invalid` state: destructive border and focus ring.
 * @param minLines Minimum number of visible lines.
 * @param maxLines Maximum number of visible lines.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardAction Handler for keyboard actions such as IME done/go.
 * @param topAddon Block addon displayed above the textarea (align block-start).
 * @param startAddon Inline addon displayed before the textarea (align inline-start).
 * @param endAddon Inline addon displayed after the textarea (align inline-end).
 * @param bottomAddon Block addon displayed below the textarea (align block-end).
 */
@Composable
fun Textarea(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isInvalid: Boolean = false,
    minLines: Int = 3,
    maxLines: Int = 6,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardAction: KeyboardActionHandler? = null,
    topAddon: (@Composable InputAddonScope.() -> Unit)? = null,
    startAddon: (@Composable InputAddonScope.() -> Unit)? = null,
    endAddon: (@Composable InputAddonScope.() -> Unit)? = null,
    bottomAddon: (@Composable InputAddonScope.() -> Unit)? = null,
) {
    val colors = resolveTextFieldColors()
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val controlShape = RoundedCornerShape(radius)
    val interactionSource = remember { MutableInteractionSource() }
    val borderColor = if (isInvalid) colors.borderInvalid else colors.border
    val ringColor = if (isInvalid) colors.focusRingInvalid else colors.focusRing
    val focusRingWidth = Effects.boxShadowFocusRing.spread
    val focusRequester = remember { FocusRequester() }
    val focusOnPress: () -> Unit = { focusRequester.requestFocus() }

    val selectionColors = LocalTextSelectionColors.current
    UnstyledTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = TypographyStyles.textSmRegular,
        textColor = colors.content,
        selectionColors = selectionColors,
        cursorBrush = SolidColor(colors.content),
        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = minLines, maxHeightInLines = maxLines),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardAction,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier
                .alpha(if (enabled) 1f else 0.5f)
                .fillMaxWidth()
                // shadow-xs from the reference is not reproducible via Compose
                // elevation (the gray halo looks like a second outline) — skip it
                .clip(controlShape)
                .border(borderWidth, borderColor, controlShape)
                .background(colors.background)
                .focusRing(
                    interactionSource = interactionSource,
                    width = focusRingWidth,
                    color = ringColor,
                    shape = controlShape,
                ),
        ) {
            topAddon?.let { addon ->
                InputAddonBlock(InputAddonAlign.BlockStart, focusOnPress) { addon() }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = TwDimensions.paddingPxToken2),
                verticalAlignment = Alignment.Top,
            ) {
                startAddon?.let { addon ->
                    InputAddonInline(InputAddonAlign.InlineStart, focusOnPress) { addon() }
                }
                TextInput(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .padding(
                            start = if (startAddon != null) TwDimensions.paddingPxToken2 else TwDimensions.paddingPxToken3,
                            end = if (endAddon != null) TwDimensions.paddingPxToken2 else TwDimensions.paddingPxToken3,
                        ),
                    placeholder = {
                        BasicText(
                            text = placeholder,
                            style = TypographyStyles.textSmRegular.copy(color = colors.placeholder),
                        )
                    },
                )
                endAddon?.let { addon ->
                    InputAddonInline(InputAddonAlign.InlineEnd, focusOnPress) { addon() }
                }
            }

            bottomAddon?.let { addon ->
                InputAddonBlock(InputAddonAlign.BlockEnd, focusOnPress) { addon() }
            }
        }
    }
}
