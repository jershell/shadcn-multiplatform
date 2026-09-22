package com.github.jershell.shadcn.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.TextInput
import com.composeunstyled.UnstyledTextField
import com.composeunstyled.focusRing
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.Effects
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * A single-line text field styled after shadcn/ui [Input](https://ui.shadcn.com/docs/components/input).
 *
 * The text content is managed by a [TextFieldState] from Compose Foundation. Obtain a state with
 * [androidx.compose.foundation.text.input.rememberTextFieldState].
 *
 * @param state Text field state.
 * @param placeholder Text shown when the field is empty.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the text can be edited.
 * @param isInvalid Shows the shadcn `aria-invalid` state: destructive border and focus ring.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardAction Handler for keyboard actions such as IME done/go.
 * @param inputTransformation Transformation applied while editing.
 * @param outputTransformation Transformation applied to the rendered text.
 * @param topAddon Block addon displayed above the input (align block-start).
 * @param startAddon Inline addon displayed before the input (align inline-start).
 * @param endAddon Inline addon displayed after the input (align inline-end).
 * @param bottomAddon Block addon displayed below the input (align block-end).
 */
@Composable
fun Input(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isInvalid: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
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
    // In Compose the extra lineHeight is added above the line (in CSS it is
    // split evenly), so with text-sm (lineHeight 20sp) glyphs sat below the row
    // center. For a single-line field, align lineHeight with the font size.
    val fieldTextStyle = TypographyStyles.textSmRegular.copy(lineHeight = 14.sp)

    val selectionColors = LocalTextSelectionColors.current
    UnstyledTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = fieldTextStyle,
        textColor = colors.content,
        selectionColors = selectionColors,
        cursorBrush = SolidColor(colors.content),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardAction,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
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
                    .height(TwDimensions.heightHToken9),
                verticalAlignment = Alignment.CenterVertically,
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
                            style = fieldTextStyle.copy(color = colors.placeholder),
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
