package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.questionnaire.FreeformQuestion
import com.github.jershell.shadcn.components.questionnaire.MultipleChoiceQuestion
import com.github.jershell.shadcn.components.questionnaire.QuestionAnswer
import com.github.jershell.shadcn.components.questionnaire.Questionnaire
import com.github.jershell.shadcn.components.questionnaire.QuestionnaireLabels
import com.github.jershell.shadcn.components.questionnaire.SingleChoiceQuestion
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.util.Log
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_a_colleague
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_a_one_step_questionnaire_no_back_the_only_button
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_all_built_in_navigation_texts_are_overridable_vi
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_anything_else_you_want_to_share
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_average
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_component_coverage
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_documentation
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_done
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_e_g_command
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_excellent
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_finish_without_answering_required_false
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_finished_the_flow_restart_it_to_try_back_skip_ag
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_flow_finished
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_four_short_questions_about_your_experience_with
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_full_flow
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_github
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_good
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_how_did_you_hear_about_this_library
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_how_would_you_rate_the_component_library
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_kotlin
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_localized_labels
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_one_question_per_step_next_is_blocked_until_a_re
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_performance
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_poll_completed
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_poor
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_product_feedback
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_quick_poll
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_restart
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_search
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_single_question
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_10
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_11
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_12
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_13
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_2
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_3
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_4
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_5
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_6
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_7
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_8
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_text_9
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_theming_tokens
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_usage
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_what_is_your_favorite_component
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_which_parts_should_we_improve_any_number_of_opti
import com.github.jershell.shadcn.demoapp.generated.resources.questionnaire_write_your_feedback
import org.jetbrains.compose.resources.stringResource

/** Formats answers into a short readable summary for a toast. */
private fun answersSummary(answers: Map<String, QuestionAnswer>): String =
    answers.entries.joinToString(" | ") { (id, answer) ->
        when (answer) {
            is QuestionAnswer.Choice -> "$id=${answer.option}"
            is QuestionAnswer.Choices ->
                if (answer.options.isEmpty()) "$id=none" else "$id=${answer.options.joinToString("/")}"
            is QuestionAnswer.Text -> "$id=${answer.text.take(24)}"
        }
    }

@Composable
fun DemoQuestionnaire() {
    val s_questionnaire_done = stringResource(Res.string.questionnaire_done)
    val s_questionnaire_poll_completed = stringResource(Res.string.questionnaire_poll_completed)
    val s_questionnaire_flow_finished = stringResource(Res.string.questionnaire_flow_finished)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.questionnaire_full_flow),
            description = stringResource(Res.string.questionnaire_one_question_per_step_next_is_blocked_until_a_re),
        ) {
            var restartKey by remember { mutableIntStateOf(0) }
            key(restartKey) {
                Questionnaire(
                    title = stringResource(Res.string.questionnaire_product_feedback),
                    description = stringResource(Res.string.questionnaire_four_short_questions_about_your_experience_with),
                    questions = listOf(
                        SingleChoiceQuestion(
                            id = "rating",
                            question = stringResource(Res.string.questionnaire_how_would_you_rate_the_component_library),
                            options = listOf(stringResource(Res.string.questionnaire_excellent), stringResource(Res.string.questionnaire_good), stringResource(Res.string.questionnaire_average), stringResource(Res.string.questionnaire_poor)),
                            skippable = true,
                        ),
                        MultipleChoiceQuestion(
                            id = "improvements",
                            question = stringResource(Res.string.questionnaire_which_parts_should_we_improve_any_number_of_opti),
                            options = listOf(
                                stringResource(Res.string.questionnaire_documentation),
                                stringResource(Res.string.questionnaire_theming_tokens),
                                stringResource(Res.string.questionnaire_component_coverage),
                                stringResource(Res.string.questionnaire_performance),
                            ),
                        ),
                        FreeformQuestion(
                            id = "favorite",
                            question = stringResource(Res.string.questionnaire_what_is_your_favorite_component),
                            placeholder = stringResource(Res.string.questionnaire_e_g_command),
                        ),
                        FreeformQuestion(
                            id = "feedback",
                            question = stringResource(Res.string.questionnaire_anything_else_you_want_to_share),
                            multiline = true,
                            placeholder = stringResource(Res.string.questionnaire_write_your_feedback),
                            skippable = true,
                        ),
                    ),
                    onFinish = { answers ->
                        Log.debug("finished: " + answersSummary(answers))
                        Toast(s_questionnaire_flow_finished)
                    },
                )
            }
            P(stringResource(Res.string.questionnaire_finished_the_flow_restart_it_to_try_back_skip_ag))
            Button(
                onClick = { restartKey += 1 },
                variant = ButtonVariant.Outline,
            ) {
                ButtonText(stringResource(Res.string.questionnaire_restart))
            }
        }

        DemoSection(
            title = stringResource(Res.string.questionnaire_single_question),
            description = stringResource(Res.string.questionnaire_a_one_step_questionnaire_no_back_the_only_button),
        ) {
            var restartKey by remember { mutableIntStateOf(0) }
            key(restartKey) {
                Questionnaire(
                    title = stringResource(Res.string.questionnaire_quick_poll),
                    questions = listOf(
                        SingleChoiceQuestion(
                            id = "source",
                            question = stringResource(Res.string.questionnaire_how_did_you_hear_about_this_library),
                            options = listOf(
                                stringResource(Res.string.questionnaire_github),
stringResource(Res.string.questionnaire_a_colleague),
stringResource(Res.string.questionnaire_search),
                            ),
                            required = false,
                        ),
                    ),
                    onFinish = { answers ->
                        Log.debug("poll: " + answersSummary(answers))
                        Toast(s_questionnaire_poll_completed)
                    },
                )
            }
            P(stringResource(Res.string.questionnaire_finish_without_answering_required_false))
            Button(
                onClick = { restartKey += 1 },
                variant = ButtonVariant.Outline,
            ) {
                ButtonText(stringResource(Res.string.questionnaire_restart))
            }
        }

        DemoSection(
            title = stringResource(Res.string.questionnaire_localized_labels),
            description = stringResource(Res.string.questionnaire_all_built_in_navigation_texts_are_overridable_vi),
        ) {
            var restartKey by remember { mutableIntStateOf(0) }
            key(restartKey) {
                Questionnaire(
                    title = stringResource(Res.string.questionnaire_text),
                    description = stringResource(Res.string.questionnaire_text_2),
                    labels = QuestionnaireLabels(
                        step = stringResource(Res.string.questionnaire_text_3),
                        of = stringResource(Res.string.questionnaire_text_4),
                        back = stringResource(Res.string.questionnaire_text_5),
                        skip = stringResource(Res.string.questionnaire_text_6),
                        next = stringResource(Res.string.questionnaire_text_7),
                        finish = stringResource(Res.string.questionnaire_text_8),
                        completed = stringResource(Res.string.questionnaire_text_9),
                        completedDescription = stringResource(Res.string.questionnaire_text_10),
                    ),
                    questions = listOf(
                        SingleChoiceQuestion(
                            id = "experience",
                            question = stringResource(Res.string.questionnaire_kotlin),
                            options = listOf(stringResource(Res.string.questionnaire_text_11), "1–3 года", "Более 3 лет"),
                        ),
                        FreeformQuestion(
                            id = "comment",
                            question = stringResource(Res.string.questionnaire_text_12),
                            multiline = true,
                            placeholder = stringResource(Res.string.questionnaire_text_13),
                            skippable = true,
                        ),
                    ),
                    onFinish = { answers ->
                        Log.debug("done: " + answersSummary(answers))
                        Toast(s_questionnaire_done)
                    },
                )
            }
            Button(
                onClick = { restartKey += 1 },
                variant = ButtonVariant.Outline,
            ) {
                ButtonText(stringResource(Res.string.questionnaire_restart))
            }
        }

        P(stringResource(Res.string.questionnaire_usage))
        InlineCode(
            text = """
                Questionnaire(
                    title = "Product feedback",
                    description = "Four short questions.",
                    questions = listOf(
                        SingleChoiceQuestion(id = "rating", question = "...", options = listOf("A", "B"), skippable = true),
                        MultipleChoiceQuestion(id = "improvements", question = "...", options = listOf("X", "Y")),
                        FreeformQuestion(id = "comment", question = "...", multiline = true),
                    ),
                    labels = QuestionnaireLabels(next = "Далее", finish = "Завершить"),
                    onFinish = { answers -> /* Map<String, QuestionAnswer> */ },
                )
            """.trimIndent(),
            selected = false,
            selectEnabled = true,
            copyEnabled = true,
        )
    }
}

@Composable
private fun DemoSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        H4(title)
        P(description)
        content()
    }
}
