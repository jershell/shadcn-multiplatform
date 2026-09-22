package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Inbox
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.ShieldAlert
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.item.Item
import com.github.jershell.shadcn.components.item.ItemActions
import com.github.jershell.shadcn.components.item.ItemContent
import com.github.jershell.shadcn.components.item.ItemDescription
import com.github.jershell.shadcn.components.item.ItemFooter
import com.github.jershell.shadcn.components.item.ItemGroup
import com.github.jershell.shadcn.components.item.ItemHeader
import com.github.jershell.shadcn.components.item.ItemIcon
import com.github.jershell.shadcn.components.item.ItemMedia
import com.github.jershell.shadcn.components.item.ItemMediaVariant
import com.github.jershell.shadcn.components.item.ItemSeparator
import com.github.jershell.shadcn.components.item.ItemSize
import com.github.jershell.shadcn.components.item.ItemTitle
import com.github.jershell.shadcn.components.item.ItemVariant
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.item_a_simple_item_with_title_description_and_actions
import com.github.jershell.shadcn.demoapp.generated.resources.item_action
import com.github.jershell.shadcn.demoapp.generated.resources.item_advanced_thinking_or_reasoning
import com.github.jershell.shadcn.demoapp.generated.resources.item_basic_item
import com.github.jershell.shadcn.demoapp.generated.resources.item_bordered_item_for_denser_layouts
import com.github.jershell.shadcn.demoapp.generated.resources.item_compact_style_for_secondary_information
import com.github.jershell.shadcn.demoapp.generated.resources.item_default_variant
import com.github.jershell.shadcn.demoapp.generated.resources.item_er
import com.github.jershell.shadcn.demoapp.generated.resources.item_evil_rabbit
import com.github.jershell.shadcn.demoapp.generated.resources.item_group
import com.github.jershell.shadcn.demoapp.generated.resources.item_header_and_footer
import com.github.jershell.shadcn.demoapp.generated.resources.item_icon_avatar_and_image_media_are_all_supported
import com.github.jershell.shadcn.demoapp.generated.resources.item_img
import com.github.jershell.shadcn.demoapp.generated.resources.item_invite
import com.github.jershell.shadcn.demoapp.generated.resources.item_last_seen_5_months_ago
import com.github.jershell.shadcn.demoapp.generated.resources.item_maxleiter
import com.github.jershell.shadcn.demoapp.generated.resources.item_maxleiter_vercel_com
import com.github.jershell.shadcn.demoapp.generated.resources.item_media_variants
import com.github.jershell.shadcn.demoapp.generated.resources.item_midnight_city_lights_electric_nights
import com.github.jershell.shadcn.demoapp.generated.resources.item_model_optimized_for_deeper_tasks
import com.github.jershell.shadcn.demoapp.generated.resources.item_muted_extra_small
import com.github.jershell.shadcn.demoapp.generated.resources.item_neon_dreams_3_45
import com.github.jershell.shadcn.demoapp.generated.resources.item_new_login_detected_from_unknown_device
import com.github.jershell.shadcn.demoapp.generated.resources.item_open_item
import com.github.jershell.shadcn.demoapp.generated.resources.item_optional_header_and_footer_can_be_used_for_conte
import com.github.jershell.shadcn.demoapp.generated.resources.item_outline_small
import com.github.jershell.shadcn.demoapp.generated.resources.item_review
import com.github.jershell.shadcn.demoapp.generated.resources.item_security_alert
import com.github.jershell.shadcn.demoapp.generated.resources.item_shadcn
import com.github.jershell.shadcn.demoapp.generated.resources.item_shadcn_vercel_com
import com.github.jershell.shadcn.demoapp.generated.resources.item_transparent_background_regular_spacing
import com.github.jershell.shadcn.demoapp.generated.resources.item_updated_2_hours_ago
import com.github.jershell.shadcn.demoapp.generated.resources.item_usage
import com.github.jershell.shadcn.demoapp.generated.resources.item_use_itemgroup_with_separators_to_build_structure
import com.github.jershell.shadcn.demoapp.generated.resources.item_use_variant_and_size_to_adapt_item_appearance
import com.github.jershell.shadcn.demoapp.generated.resources.item_v0_1_5_lg
import com.github.jershell.shadcn.demoapp.generated.resources.item_variants_and_sizes
import com.github.jershell.shadcn.demoapp.generated.resources.item_your_profile_has_been_verified
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoItem() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.item_basic_item),
            description = stringResource(Res.string.item_a_simple_item_with_title_description_and_actions),
        ) {
            Item(variant = ItemVariant.Outline) {
                ItemMedia(variant = ItemMediaVariant.Icon) {
                    ItemIcon(
                        imageVector = Lucide.BadgeCheck,
                        contentDescription = null,
                    )
                }
                ItemContent {
                    ItemTitle(stringResource(Res.string.item_action))
                    ItemDescription(stringResource(Res.string.item_your_profile_has_been_verified))
                }
                ItemActions {
                    Button(
                        onClick = {},
                        size = ButtonSize.Sm,
                        variant = ButtonVariant.Outline,
                    ) {
                        ButtonText(stringResource(Res.string.item_review))
                    }
                    ItemIcon(imageVector = Lucide.ChevronRight, contentDescription = stringResource(Res.string.item_open_item))
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.item_variants_and_sizes),
            description = stringResource(Res.string.item_use_variant_and_size_to_adapt_item_appearance),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Item(variant = ItemVariant.Default, size = ItemSize.Default) {
                    ItemMedia(variant = ItemMediaVariant.Icon, size = ItemSize.Default) {
                        ItemIcon(imageVector = Lucide.Inbox, contentDescription = null)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_default_variant))
                        ItemDescription(stringResource(Res.string.item_transparent_background_regular_spacing))
                    }
                }
                Item(variant = ItemVariant.Outline, size = ItemSize.Sm) {
                    ItemMedia(variant = ItemMediaVariant.Icon, size = ItemSize.Sm) {
                        ItemIcon(imageVector = Lucide.Inbox, contentDescription = null)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_outline_small))
                        ItemDescription(stringResource(Res.string.item_bordered_item_for_denser_layouts))
                    }
                }
                Item(variant = ItemVariant.Muted, size = ItemSize.Xs) {
                    ItemMedia(variant = ItemMediaVariant.Icon, size = ItemSize.Xs) {
                        ItemIcon(imageVector = Lucide.Inbox, contentDescription = null)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_muted_extra_small))
                        ItemDescription(stringResource(Res.string.item_compact_style_for_secondary_information))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.item_media_variants),
            description = stringResource(Res.string.item_icon_avatar_and_image_media_are_all_supported),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Item(variant = ItemVariant.Outline) {
                    ItemMedia(variant = ItemMediaVariant.Icon) {
                        ItemIcon(imageVector = Lucide.ShieldAlert, contentDescription = null)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_security_alert))
                        ItemDescription(stringResource(Res.string.item_new_login_detected_from_unknown_device))
                    }
                }
                Item(variant = ItemVariant.Outline) {
                    ItemMedia(variant = ItemMediaVariant.Avatar) {
                        BasicText(
                            text = stringResource(Res.string.item_er),
                            style = TypographyStyles.textXsSemiBold,
                        )
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_evil_rabbit))
                        ItemDescription(stringResource(Res.string.item_last_seen_5_months_ago))
                    }
                    ItemActions {
                        Button(onClick = {}, size = ButtonSize.Sm) {
                            ButtonText(stringResource(Res.string.item_invite))
                        }
                    }
                }
                Item(variant = ItemVariant.Outline) {
                    ItemMedia(variant = ItemMediaVariant.Image) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Theme[ColorProps][ColorTokens.muted]),
                            contentAlignment = Alignment.Center,
                        ) {
                            BasicText(
                                text = stringResource(Res.string.item_img),
                                style = TypographyStyles.textXsMedium,
                            )
                        }
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_midnight_city_lights_electric_nights))
                        ItemDescription(stringResource(Res.string.item_neon_dreams_3_45))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.item_group),
            description = stringResource(Res.string.item_use_itemgroup_with_separators_to_build_structure),
        ) {
            ItemGroup {
                Item(variant = ItemVariant.Outline) {
                    ItemMedia(variant = ItemMediaVariant.Avatar) {
                        BasicText("s", style = TypographyStyles.textSmMedium)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_shadcn))
                        ItemDescription(stringResource(Res.string.item_shadcn_vercel_com))
                    }
                }
                ItemSeparator()
                Item(variant = ItemVariant.Outline) {
                    ItemMedia(variant = ItemMediaVariant.Avatar) {
                        BasicText("m", style = TypographyStyles.textSmMedium)
                    }
                    ItemContent {
                        ItemTitle(stringResource(Res.string.item_maxleiter))
                        ItemDescription(stringResource(Res.string.item_maxleiter_vercel_com))
                    }
                }
            }
        }

        DemoSection(
            title = stringResource(Res.string.item_header_and_footer),
            description = stringResource(Res.string.item_optional_header_and_footer_can_be_used_for_conte),
        ) {
            Item(
                variant = ItemVariant.Outline,
                header = {
                    ItemHeader {
                        P(stringResource(Res.string.item_v0_1_5_lg))
                    }
                },
                footer = {
                    ItemFooter {
                        Muted(stringResource(Res.string.item_updated_2_hours_ago))
                    }
                },
            ) {
                ItemMedia(variant = ItemMediaVariant.Icon) {
                    ItemIcon(imageVector = Lucide.Plus, contentDescription = null)
                }
                ItemContent {
                    ItemTitle(stringResource(Res.string.item_advanced_thinking_or_reasoning))
                    ItemDescription(stringResource(Res.string.item_model_optimized_for_deeper_tasks))
                }
            }
        }

        Muted(stringResource(Res.string.item_usage))
        InlineCode(
            text = """
                Item(variant = ItemVariant.Outline, size = ItemSize.Default) {
                    ItemMedia(variant = ItemMediaVariant.Icon) { Icon() }
                    ItemContent {
                        ItemTitle("Title")
                        ItemDescription("Description")
                    }
                    ItemActions { Button(onClick = {}) { ButtonText("Action") } }
                }
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
