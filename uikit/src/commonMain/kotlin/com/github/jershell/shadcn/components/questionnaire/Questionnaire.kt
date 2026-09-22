package com.github.jershell.shadcn.components.questionnaire

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.BasicText
import org.jetbrains.compose.resources.stringResource
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.checkbox.Checkbox
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.label.Label
import com.github.jershell.shadcn.components.progress.Progress
import com.github.jershell.shadcn.components.radio.RadioGroup
import com.github.jershell.shadcn.components.textarea.Textarea
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.questionnaire_back
import com.github.jershell.shadcn.generated.resources.questionnaire_completed
import com.github.jershell.shadcn.generated.resources.questionnaire_completed_description
import com.github.jershell.shadcn.generated.resources.questionnaire_finish
import com.github.jershell.shadcn.generated.resources.questionnaire_next
import com.github.jershell.shadcn.generated.resources.questionnaire_of
import com.github.jershell.shadcn.generated.resources.questionnaire_skip
import com.github.jershell.shadcn.generated.resources.questionnaire_step
import kotlin.collections.mapIndexed

/**
 * Answer of a single [QuestionSpec], delivered to [Questionnaire]'s `onFinish`.
 */
sealed interface QuestionAnswer {

    /** One selected option (single-choice question). */
    data class Choice(val option: String) : QuestionAnswer

    /** A set of selected options (multiple-choice question). */
    data class Choices(val options: Set<String>) : QuestionAnswer

    /** Free-form text answer. */
    data class Text(val text: String) : QuestionAnswer
}

/**
 * Base of every questionnaire question.
 *
 * @param id Stable identifier used as the key of the answers map.
 * @param question Question text.
 * @param required When `true`, [Questionnaire] blocks *Next* until the question is answered.
 * @param skippable When `true`, a *Skip* action is offered for this question.
 */
sealed interface QuestionSpec {
    val id: String
    val question: String
    val required: Boolean
    val skippable: Boolean
}

/**
 * Single-choice question rendered as a [RadioGroup].
 *
 * @param options Options; the value stored in answers is `options[selectedIndex]`.
 */
data class SingleChoiceQuestion(
    override val id: String,
    override val question: String,
    val options: List<String>,
    override val required: Boolean = true,
    override val skippable: Boolean = false,
) : QuestionSpec

/**
 * Multiple-choice question rendered as a list of [Checkbox]es.
 *
 * @param options Options; the value stored in answers is the set of selected options.
 */
data class MultipleChoiceQuestion(
    override val id: String,
    override val question: String,
    val options: List<String>,
    override val required: Boolean = true,
    override val skippable: Boolean = false,
) : QuestionSpec

/**
 * Free-form question rendered as an [Input] or [Textarea].
 *
 * @param multiline `true` renders a [Textarea], `false` a single-line [Input].
 * @param placeholder Input placeholder.
 */
data class FreeformQuestion(
    override val id: String,
    override val question: String,
    val multiline: Boolean = false,
    val placeholder: String? = null,
    override val required: Boolean = true,
    override val skippable: Boolean = false,
) : QuestionSpec

/**
 * A multi-step questionnaire styled after shadcn/ui: one question per step, a
 * header with title/description, a [Progress] line with a "Step N of M" counter,
 * navigation buttons and a completion state.
 *
 * Validation is expressed through a disabled *Next* button: the step cannot be
 * advanced while a required question is unanswered. *Back* returns to the
 * previous step keeping all answers; *Skip* (shown for `skippable` questions)
 * moves forward without an answer. After the last step [onFinish] receives the
 * collected answers keyed by [QuestionSpec.id] (skipped questions are absent).
 *
 * @param title Header title.
 * @param description Optional header description.
 * @param questions Questions; one step per question.
 * @param onFinish Called with the answers map after the final step.
 * @param labels Texts of the built-in navigation; override for localization.
 * @param modifier Applied to the root column.
 */
@Composable
fun Questionnaire(
    title: String,
    questions: List<QuestionSpec>,
    onFinish: (Map<String, QuestionAnswer>) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    labels: QuestionnaireLabels = QuestionnaireLabels(),
) {
    require(questions.isNotEmpty()) { "Questionnaire requires at least one question." }

    val labelStep = labels.step ?: stringResource(Res.string.questionnaire_step)
    val labelOf = labels.of ?: stringResource(Res.string.questionnaire_of)
    val labelBack = labels.back ?: stringResource(Res.string.questionnaire_back)
    val labelSkip = labels.skip ?: stringResource(Res.string.questionnaire_skip)
    val labelNext = labels.next ?: stringResource(Res.string.questionnaire_next)
    val labelFinish = labels.finish ?: stringResource(Res.string.questionnaire_finish)
    val labelCompleted = labels.completed ?: stringResource(Res.string.questionnaire_completed)
    val labelCompletedDescription = labels.completedDescription
        ?: stringResource(Res.string.questionnaire_completed_description)

    val total = questions.size
    var step by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    // Choices live in answers; freeform answers live in TextFieldState, which is
    // also the source of truth.
    val answers = remember { mutableStateMapOf<String, QuestionAnswer>() }
    val textStates = remember(questions) {
        questions.filterIsInstance<FreeformQuestion>().associate { it.id to TextFieldState() }
    }

    fun advance() {
        if (step < total - 1) {
            step += 1
        } else {
            finished = true
            onFinish(answers.toMap())
        }
    }

    fun answerOf(question: QuestionSpec): QuestionAnswer? = when (question) {
        is SingleChoiceQuestion -> answers[question.id]
        is MultipleChoiceQuestion -> answers[question.id]
        is FreeformQuestion -> textStates[question.id]?.text?.toString()
            ?.takeIf { it.isNotBlank() }
            ?.let { QuestionAnswer.Text(it) }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(com.github.jershell.shadcn.theme.TwDimensions.gapGapToken4),
    ) {
        if (!finished) {
            // Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(
                    com.github.jershell.shadcn.theme.TwDimensions.gapGapToken1,
                ),
            ) {
                H4(
                    text = title,
                )
                if (description != null) {
                    BasicText(
                        text = description,
                        style = TypographyStyles.textSmRegular.copy(
                            color = Theme[com.github.jershell.shadcn.theme.ColorProps][
                                com.github.jershell.shadcn.theme.ColorTokens.mutedForeground],
                        ),
                    )
                }
            }

            // Progress + step counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    com.github.jershell.shadcn.theme.TwDimensions.gapGapToken3,
                ),
            ) {
                Progress(
                    value = (step + 1f) / total,
                    modifier = Modifier.weight(1f),
                )
                BasicText(
                    text = "$labelStep ${step + 1} $labelOf $total",
                    style = TypographyStyles.textXsRegular.copy(
                        color = Theme[com.github.jershell.shadcn.theme.ColorProps][
                            com.github.jershell.shadcn.theme.ColorTokens.mutedForeground],
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            val question = questions[step]
            QuestionStep(
                question = question,
                answer = answerOf(question),
                freeformState = (question as? FreeformQuestion)?.let { textStates[it.id] },
                onSingleChoice = { option -> answers[question.id] = QuestionAnswer.Choice(option) },
                onMultipleChoice = { selected ->
                    answers[question.id] = QuestionAnswer.Choices(selected)
                },
            )

            // Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    com.github.jershell.shadcn.theme.TwDimensions.gapGapToken2,
                    Alignment.End,
                ),
            ) {
                if (step > 0) {
                    Button(
                        onClick = { step -= 1 },
                        variant = ButtonVariant.Outline,
                    ) {
                        ButtonText(labelBack)
                    }
                }
                if (question.skippable) {
                    Button(
                        onClick = {
                            answers.remove(question.id)
                            (question as? FreeformQuestion)?.let { spec ->
                                textStates[spec.id]?.edit { replace(0, length, "") }
                            }
                            advance()
                        },
                        variant = ButtonVariant.Ghost,
                    ) {
                        ButtonText(labelSkip)
                    }
                }
                val canContinue = question.required.not() || answerOf(question) != null
                Button(
                    onClick = ::advance,
                    enabled = canContinue,
                ) {
                    ButtonText(if (step == total - 1) labelFinish else labelNext)
                }
            }
        } else {
            // Completed state
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(
                    com.github.jershell.shadcn.theme.TwDimensions.gapGapToken2,
                ),
            ) {
                BasicText(
                    text = labelCompleted,
                    style = TypographyStyles.textLgSemiBold,
                )
                BasicText(
                    text = labelCompletedDescription,
                    style = TypographyStyles.textSmRegular.copy(
                        color = Theme[com.github.jershell.shadcn.theme.ColorProps][
                            com.github.jershell.shadcn.theme.ColorTokens.mutedForeground],
                    ),
                )
            }
        }
    }
}

/**
 * Texts of the built-in [Questionnaire] navigation.
 *
 * @param step Counter prefix ("Step"), e.g. "Step".
 * @param of Counter middle ("of"), e.g. "of".
 * @param back Back button label.
 * @param skip Skip button label.
 * @param next Next button label (non-final steps).
 * @param finish Finish button label (the final step).
 * @param completed Completed-state title.
 * @param completedDescription Completed-state description.
 */
data class QuestionnaireLabels(
    val step: String? = null,
    val of: String? = null,
    val back: String? = null,
    val skip: String? = null,
    val next: String? = null,
    val finish: String? = null,
    val completed: String? = null,
    val completedDescription: String? = null,
)

@Composable
private fun QuestionStep(
    question: QuestionSpec,
    answer: QuestionAnswer?,
    freeformState: TextFieldState?,
    onSingleChoice: (String) -> Unit,
    onMultipleChoice: (Set<String>) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            com.github.jershell.shadcn.theme.TwDimensions.gapGapToken3,
        ),
    ) {
        when (question) {
            is SingleChoiceQuestion -> {
                Label(text = question.question)
                val selectedKey = (answer as? QuestionAnswer.Choice)?.option
                    ?.let { question.options.indexOf(it) }
                    ?.takeIf { it >= 0 }
                RadioGroup(
                    items = question.options.mapIndexed { index, option ->
                        DataItem(key = index, title = option, data = option)
                    },
                    selectedKey = selectedKey,
                    onSelectedChange = { key -> onSingleChoice(question.options[key]) },
                )
            }

            is MultipleChoiceQuestion -> {
                Label(text = question.question)
                val selected = (answer as? QuestionAnswer.Choices)?.options ?: emptySet()
                question.options.forEach { option ->
                    Checkbox(
                        checked = option in selected,
                        onCheckedChange = { checked ->
                            val next = selected.toMutableSet()
                            if (checked) next.add(option) else next.remove(option)
                            onMultipleChoice(next)
                        },
                        label = option,
                    )
                }
            }

            is FreeformQuestion -> {
                val state = freeformState ?: remember(question.id) { TextFieldState() }
                Label(text = question.question)
                if (question.multiline) {
                    Textarea(state = state, placeholder = question.placeholder ?: "")
                } else {
                    Input(state = state, placeholder = question.placeholder ?: "")
                }
            }
        }
    }
}
