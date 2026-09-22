package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Copy
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.input.InputGroupButtonSize
import com.github.jershell.shadcn.components.label.Label
import com.github.jershell.shadcn.components.textarea.Textarea
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_0_280
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_194_34_83_74
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_a_vertical_separator_divides_addon_items_it_stre
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_addon_buttons_have_their_own_sizes_xs_default_sm
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_block_start_addon_with_a_label_and_a_small_icon
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_button_sizes
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_character_counter_and_a_post_button_below_the_te
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_com
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_copy
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_email
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_enter_a_secret
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_help
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_https
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_icon_at_the_start_keyboard_hints_at_the_end
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_k
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_loading_indicator_as_an_inline_addon
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_muted_text_prefix_and_suffix_around_the_value
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_post
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_reveal
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_search
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_search_2
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_search_in
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_secondary_button_addon_pulls_to_the_edge_of_the
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_textarea_with_footer
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_type_to_search
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_usage
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_user_example_com
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_button
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_icon_and_kbd
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_label
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_separator
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_spinner
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_with_text
import com.github.jershell.shadcn.demoapp.generated.resources.input_group_write_a_comment
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoInputGroup() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.input_group_with_label),
            description = stringResource(Res.string.input_group_block_start_addon_with_a_label_and_a_small_icon),
        ) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_group_user_example_com),
                topAddon = {
                    Label(text = stringResource(Res.string.input_group_email))
                    Button(
                        onClick = {},
                        size = InputGroupButtonSize.IconXs,
                        shape = CircleShape,
                    ) {
                        ButtonIcon(
                            icon = Lucide.Info.toShadcnIcon(),
                            contentDescription = stringResource(Res.string.input_group_help),
                        )
                    }
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_with_button),
            description = stringResource(Res.string.input_group_secondary_button_addon_pulls_to_the_edge_of_the),
        ) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_group_type_to_search),
                endAddon = {
                    Button(
                        onClick = {},
                        variant = ButtonVariant.Secondary,
                    ) {
                        ButtonText(stringResource(Res.string.input_group_search))
                    }
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_button_sizes),
            description = stringResource(Res.string.input_group_addon_buttons_have_their_own_sizes_xs_default_sm),
        ) {
            val state = rememberTextFieldState("shadcn-multiplatform")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Input(
                    state = state,
                    placeholder = stringResource(Res.string.input_group_type_to_search),
                    endAddon = {
                        Button(
                            onClick = {},
                            size = InputGroupButtonSize.IconXs,
                            variant = ButtonVariant.Ghost,
                        ) {
                            ButtonIcon(
                                icon = Lucide.Copy.toShadcnIcon(),
                                contentDescription = stringResource(Res.string.input_group_copy),
                            )
                        }
                    },
                )
                Input(
                    state = state,
                    placeholder = stringResource(Res.string.input_group_type_to_search),
                    endAddon = {
                        Button(
                            onClick = {},
                            size = InputGroupButtonSize.IconSm,
                            variant = ButtonVariant.Ghost,
                        ) {
                            ButtonIcon(
                                icon = Lucide.Copy.toShadcnIcon(),
                                contentDescription = stringResource(Res.string.input_group_copy),
                            )
                        }
                    },
                )
            }
        }

        DemoSection(
            title = stringResource(Res.string.input_group_with_separator),
            description = stringResource(Res.string.input_group_a_vertical_separator_divides_addon_items_it_stre),
        ) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_group_enter_a_secret),
                endAddon = {
                    Button(
                        onClick = {},
                        size = InputGroupButtonSize.IconSm,
                        variant = ButtonVariant.Ghost,
                    ) {
                        ButtonIcon(
                            icon = Lucide.Eye.toShadcnIcon(),
                            contentDescription = stringResource(Res.string.input_group_reveal),
                        )
                    }
                    Separator()
                    Button(
                        onClick = {},
                        size = InputGroupButtonSize.IconSm,
                        variant = ButtonVariant.Ghost,
                    ) {
                        ButtonIcon(
                            icon = Lucide.Copy.toShadcnIcon(),
                            contentDescription = stringResource(Res.string.input_group_copy),
                        )
                    }
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_with_text),
            description = stringResource(Res.string.input_group_muted_text_prefix_and_suffix_around_the_value),
        ) {
            val state = rememberTextFieldState(stringResource(Res.string.input_group_194_34_83_74))
            Input(
                state = state,
                startAddon = {
                    Text(stringResource(Res.string.input_group_https))
                },
                endAddon = {
                    Text(stringResource(Res.string.input_group_com))
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_with_icon_and_kbd),
            description = stringResource(Res.string.input_group_icon_at_the_start_keyboard_hints_at_the_end),
        ) {
            val state = rememberTextFieldState()
            Input(
                state = state,
                placeholder = stringResource(Res.string.input_group_search_2),
                startAddon = {
                    Icon(
                        imageVector = Lucide.Search,
                        contentDescription = null,
                    )
                },
                endAddon = {
                    Kbd(text = stringResource(Res.string.input_group_k))
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_with_spinner),
            description = stringResource(Res.string.input_group_loading_indicator_as_an_inline_addon),
        ) {
            val state = rememberTextFieldState(stringResource(Res.string.input_group_search_in))
            Input(
                state = state,
                endAddon = {
                    CircularProgress()
                },
            )
        }

        DemoSection(
            title = stringResource(Res.string.input_group_textarea_with_footer),
            description = stringResource(Res.string.input_group_character_counter_and_a_post_button_below_the_te),
        ) {
            val state = rememberTextFieldState()
            Textarea(
                state = state,
                placeholder = stringResource(Res.string.input_group_write_a_comment),
                bottomAddon = {
                    Text(stringResource(Res.string.input_group_0_280))
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {},
                        variant = ButtonVariant.Outline,
                        size = InputGroupButtonSize.Sm,
                    ) {
                        ButtonText(stringResource(Res.string.input_group_post))
                    }
                },
            )
        }

        Muted(stringResource(Res.string.input_group_usage))
        InlineCode(
            text = """
                Input(
                    state = state,
                    placeholder = "Enter a secret",
                    endAddon = {
                        Button(onClick = {}, size = InputGroupButtonSize.IconSm) {
                            ButtonIcon(icon = Lucide.Eye.toShadcnIcon(), contentDescription = "Reveal")
                        }
                        Separator()
                        Button(onClick = {}, size = InputGroupButtonSize.IconSm) {
                            ButtonIcon(icon = Lucide.Copy.toShadcnIcon(), contentDescription = "Copy")
                        }
                    },
                )
                Textarea(
                    state = state,
                    bottomAddon = {
                        Text("0/280"); Spacer(Modifier.weight(1f)); Button(...) { ButtonText("Post") }
                    },
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
