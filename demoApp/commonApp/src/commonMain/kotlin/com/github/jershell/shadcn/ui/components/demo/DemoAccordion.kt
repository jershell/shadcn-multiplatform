package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.accordion.Accordion
import com.github.jershell.shadcn.components.accordion.AccordionContent
import com.github.jershell.shadcn.components.accordion.AccordionItem
import com.github.jershell.shadcn.components.accordion.AccordionTrigger
import com.github.jershell.shadcn.components.accordion.AccordionType
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_content_with_a_leading_icon_on_the_trigger
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_default_single
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_first_item
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_first_panel_content
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_is_it_accessible
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_is_it_animated
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_is_it_styled
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_multiple
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_second_item
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_second_panel_content
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_verified_section
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_with_icon
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_yes_it_adheres_to_the_wai_aria_design_pattern
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_yes_it_comes_with_default_styles_that_match_the
import com.github.jershell.shadcn.demoapp.generated.resources.accordion_yes_it_s_animated_by_default_but_you_can_disable
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoAccordion() {
    var singleExpanded by remember { mutableStateOf(setOf(1)) }
    var multipleExpanded by remember { mutableStateOf(setOf(1)) }
    var iconExpanded by remember { mutableStateOf(setOf(1)) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.accordion_default_single))
            Accordion(
                expandedKeys = singleExpanded,
                onExpandedKeysChange = { singleExpanded = it },
            ) {
                AccordionItem(key = 1) {
                    AccordionTrigger { P(stringResource(Res.string.accordion_is_it_accessible)) }
                    AccordionContent { P(stringResource(Res.string.accordion_yes_it_adheres_to_the_wai_aria_design_pattern)) }
                }
                AccordionItem(key = 2) {
                    AccordionTrigger { P(stringResource(Res.string.accordion_is_it_styled)) }
                    AccordionContent { P(stringResource(Res.string.accordion_yes_it_comes_with_default_styles_that_match_the)) }
                }
                AccordionItem(key = 3) {
                    AccordionTrigger { P(stringResource(Res.string.accordion_is_it_animated)) }
                    AccordionContent { P(stringResource(Res.string.accordion_yes_it_s_animated_by_default_but_you_can_disable)) }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.accordion_multiple))
            Accordion(
                expandedKeys = multipleExpanded,
                onExpandedKeysChange = { multipleExpanded = it },
                type = AccordionType.Multiple,
            ) {
                AccordionItem(key = 1) {
                    AccordionTrigger { P(stringResource(Res.string.accordion_first_item)) }
                    AccordionContent { P(stringResource(Res.string.accordion_first_panel_content)) }
                }
                AccordionItem(key = 2) {
                    AccordionTrigger { P(stringResource(Res.string.accordion_second_item)) }
                    AccordionContent { P(stringResource(Res.string.accordion_second_panel_content)) }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.accordion_with_icon))
            Accordion(
                expandedKeys = iconExpanded,
                onExpandedKeysChange = { iconExpanded = it },
            ) {
                AccordionItem(key = 1) {
                    AccordionTrigger(
                        leadingIcon = Lucide.Check.toShadcnIcon(),
                    ) { P(stringResource(Res.string.accordion_verified_section)) }
                    AccordionContent { P(stringResource(Res.string.accordion_content_with_a_leading_icon_on_the_trigger)) }
                }
            }
        }
    }
}
