package com.github.jershell.shadcn.components.field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.label.Label
import com.github.jershell.shadcn.components.typography.UnorderedList
import com.github.jershell.shadcn.theme.BaseTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

/**
 * Field pieces read these locals provided by [Field]: [FieldLabel] and [FieldTitle]
 * color their text `destructive` when the field is invalid and dim when disabled.
 */
// Declared in FieldColors.kt together with the resolve function.

/**
 * Orientation of a [Field].
 */
enum class FieldOrientation {

    /** Content stacked vertically, `flex-col gap-3`; children stretch full width. */
    Vertical,

    /** Label and content side by side, `flex-row items-center gap-3`. */
    Horizontal,
}

/**
 * Variant of a [FieldLegend] heading.
 */
enum class FieldLegendVariant {

    /** `text-base font-medium` heading of a field group. */
    Legend,

    /** `text-sm font-medium`, matches the size of [FieldLabel]. */
    Label,
}

/**
 * A field group styled after the shadcn/ui `FieldGroup`: a vertical `gap-7` stack.
 *
 * @param content Fields, field sets, legends and separators.
 */
@Composable
fun FieldGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken7), // gap-7
        content = content,
    )
}

/**
 * A field set styled after the shadcn/ui `FieldSet`: a vertical `gap-6` stack.
 *
 * @param content Groups, fields or legends.
 */
@Composable
fun FieldSet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken6), // gap-6
        content = content,
    )
}

/**
 * A heading of a [FieldSet] or [FieldGroup], matching the shadcn/ui `FieldLegend`:
 * `mb-3 font-medium`, `text-base` in the [FieldLegendVariant.Legend] variant and
 * `text-sm` in the [FieldLegendVariant.Label] one.
 *
 * @param variant [FieldLegendVariant.Legend] for group headings, [FieldLegendVariant.Label]
 *   to visually match a [FieldLabel].
 */
@Composable
fun FieldLegend(
    text: String,
    modifier: Modifier = Modifier,
    variant: FieldLegendVariant = FieldLegendVariant.Legend,
) {
    val colors = resolveFieldTextColors(isInvalid = false)
    Box(modifier = modifier.padding(bottom = BaseTokens.token12)) { // mb-3
        BasicText(
            text = text,
            style = when (variant) {
                FieldLegendVariant.Legend -> TypographyStyles.textBaseMedium
                FieldLegendVariant.Label -> TypographyStyles.textSmMedium
            }.copy(color = colors.content),
        )
    }
}

/**
 * A single form field styled after the shadcn/ui `Field`: a `gap-3` row or column
 * that propagates [isInvalid] and [enabled] to the field pieces and renders the
 * standard slots in order: [label], [input], [description], [error].
 *
 * String slots render the standard pieces ([FieldLabel], [FieldDescription],
 * [FieldError]); composable slots accept anything, e.g. [FieldContent] with
 * [FieldTitle] + [FieldDescription] for the horizontal switch pattern.
 *
 * ```
 * Field(
 *     label = "Email",
 *     description = "We will send confirmations to this address.",
 *     error = if (invalid) "Already taken." else null,
 *     isInvalid = invalid,
 * ) {
 *     Input(state = state, placeholder = "you@example.com")
 * }
 * ```
 *
 * @param input The control slot (an [com.github.jershell.shadcn.components.input.Input],
 *   [com.github.jershell.shadcn.components.switch.Switch], etc.).
 * @param label Rendered before the input; pass a string via the text overload or use
 *   [FieldLabel]/[FieldContent] here.
 * @param description Rendered after the input.
 * @param error Rendered after the description.
 * @param orientation Vertical stacks the slots with full width; horizontal puts the
 *   label next to the input, centered, and moves description/error below the row.
 * @param isInvalid Colors labels/titles destructive and hints the state to inputs.
 * @param enabled Dims the field pieces when `false`.
 */
@Composable
fun Field(
    modifier: Modifier = Modifier,
    orientation: FieldOrientation = FieldOrientation.Vertical,
    isInvalid: Boolean = false,
    enabled: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    error: (@Composable () -> Unit)? = null,
    input: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalFieldIsInvalid provides isInvalid,
        LocalFieldEnabled provides enabled,
    ) {
        when (orientation) {
            FieldOrientation.Vertical -> Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3), // gap-3
            ) {
                label?.invoke()
                input()
                description?.invoke()
                error?.invoke()
            }

            FieldOrientation.Horizontal -> Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3), // gap-3
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken3), // gap-3
                ) {
                    label?.invoke()
                    input()
                }
                description?.invoke()
                error?.invoke()
            }
        }
    }
}

/**
 * Convenience overload of [Field] with plain-text slots: renders [FieldLabel],
 * [FieldDescription] and [FieldError] around the [input] control.
 */
@Composable
fun Field(
    label: String,
    modifier: Modifier = Modifier,
    orientation: FieldOrientation = FieldOrientation.Vertical,
    isInvalid: Boolean = false,
    enabled: Boolean = true,
    description: String? = null,
    error: String? = null,
    input: @Composable () -> Unit,
) {
    Field(
        modifier = modifier,
        orientation = orientation,
        isInvalid = isInvalid,
        enabled = enabled,
        label = { FieldLabel(label) },
        description = {
            if (description != null) {
                FieldDescription(description)
            }
        },
        error = { FieldError(error) },
        input = input,
    )
}

/**
 * The label of a field, matching the shadcn/ui `FieldLabel`: the [Label] text-sm
 * font-medium, colored `destructive` when the field is invalid and dimmed when the
 * field is disabled.
 */
@Composable
fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveFieldTextColors(LocalFieldIsInvalid.current)
    Label(
        text = text,
        modifier = modifier,
        enabled = LocalFieldEnabled.current,
        color = colors.content,
    )
}

/**
 * A title row of a field with an optional leading icon, matching the shadcn/ui
 * `FieldTitle`: `text-sm font-medium` with `gap-2`. Unlike [FieldLabel] it is a plain
 * row (for patterns like icon + text + switch), not a form label.
 */
@Composable
fun FieldTitle(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val colors = resolveFieldTextColors(LocalFieldIsInvalid.current)
    Row(
        modifier = modifier.alpha(if (LocalFieldEnabled.current) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2), // gap-2
    ) {
        if (icon != null) {
            UnstyledIcon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(TwDimensions.heightHToken4),
                tint = colors.content,
            )
        }
        BasicText(
            text = text,
            style = TypographyStyles.textSmMedium.copy(color = colors.content),
        )
    }
}

/**
 * Helper text under a field, matching the shadcn/ui `FieldDescription`:
 * `text-sm text-muted-foreground`. Stays muted even when the field is invalid
 * (the error itself is reported by [FieldError]).
 */
@Composable
fun FieldDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = resolveFieldTextColors(isInvalid = false)
    BasicText(
        text = text,
        modifier = modifier
            .alpha(if (LocalFieldEnabled.current) 1f else 0.5f)
            .fillMaxWidth(),
        style = TypographyStyles.textSmRegular.copy(color = colors.muted),
    )
}

/**
 * The error of a field, matching the shadcn/ui `FieldError`: `text-sm
 * text-destructive` with an `alert` live region. Nothing is rendered when
 * [message] is `null`.
 */
@Composable
fun FieldError(
    message: String?,
    modifier: Modifier = Modifier,
) {
    if (message == null) {
        return
    }
    val colors = resolveFieldTextColors(isInvalid = false)
    BasicText(
        text = message,
        modifier = modifier
            .fillMaxWidth()
            .semanticsError(),
        style = TypographyStyles.textSmRegular.copy(color = colors.destructive),
    )
}

/**
 * A validation error with several messages, rendering them as a destructive
 * unordered list when more than one unique message is present (like the `errors`
 * array of the reference).
 */
@Composable
fun FieldError(
    messages: List<String?>,
    modifier: Modifier = Modifier,
) {
    val unique = messages.filterNotNull().filter { it.isNotBlank() }.distinct()
    if (unique.isEmpty()) {
        return
    }
    if (unique.size == 1) {
        FieldError(message = unique.first(), modifier = modifier)
        return
    }
    val colors = resolveFieldTextColors(isInvalid = false)
    UnorderedList(
        items = unique,
        modifier = modifier,
        color = colors.destructive,
    )
}

/**
 * Content block of a field, matching the shadcn/ui `FieldContent`: a vertical
 * `gap-1.5` stack, typically [FieldTitle] + [FieldDescription].
 */
@Composable
fun FieldContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapN1_5), // gap-1.5
        content = content,
    )
}

/**
 * A separator with an optional centered label, matching the shadcn/ui
 * `FieldSeparator`: a 20dp-high line with the text on `bg-background px-2` in the
 * middle. The negative vertical margins of the reference are replicated by shrinking
 * the flow height, so neighbors sit 8dp closer on both sides.
 */
@Composable
fun FieldSeparator(
    modifier: Modifier = Modifier,
    text: String? = null,
) {
    val colors = resolveFieldTextColors(isInvalid = false)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .negativeVerticalMargins(BaseTokens.token8) // -my-2
            .height(TwDimensions.heightHToken5), // h-5
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.border),
        )
        if (text != null) {
            BasicText(
                text = text,
                modifier = Modifier
                    .background(colors.background)
                    .padding(horizontal = TwDimensions.paddingPxToken2), // px-2
                style = TypographyStyles.textSmRegular.copy(color = colors.muted),
            )
        }
    }
}

private fun Modifier.semanticsError(): Modifier = semantics {
    liveRegion = LiveRegionMode.Polite
}

/**
 * Replicates negative vertical margins: the node keeps its visual size but the
 * reported flow height shrinks so neighbors move closer by [margins] on both sides.
 */
private fun Modifier.negativeVerticalMargins(margins: Dp): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val shrink = margins.roundToPx()
    layout(placeable.width, placeable.height - shrink * 2) {
        placeable.placeRelative(0, shrink)
    }
}