package com.github.jershell.shadcn.ui.navigation

import androidx.compose.runtime.Composable
import com.github.jershell.shadcn.ui.components.demo.DemoAccordion
import com.github.jershell.shadcn.ui.components.demo.DemoAlert
import com.github.jershell.shadcn.ui.components.demo.DemoAttachment
import com.github.jershell.shadcn.ui.components.demo.DemoAvatar
import com.github.jershell.shadcn.ui.components.demo.DemoBadge
import com.github.jershell.shadcn.ui.components.demo.DemoBreadcrumb
import com.github.jershell.shadcn.ui.components.demo.DemoButton
import com.github.jershell.shadcn.ui.components.demo.DemoButtonGroup
import com.github.jershell.shadcn.ui.components.demo.DemoCalendar
import com.github.jershell.shadcn.ui.components.demo.DemoCard
import com.github.jershell.shadcn.ui.components.demo.DemoCarousel
import com.github.jershell.shadcn.ui.components.demo.DemoCharts
import com.github.jershell.shadcn.ui.components.demo.DemoCheckbox
import com.github.jershell.shadcn.ui.components.demo.DemoCollapsible
import com.github.jershell.shadcn.ui.components.demo.DemoCombobox
import com.github.jershell.shadcn.ui.components.demo.DemoCommand
import com.github.jershell.shadcn.ui.components.demo.DemoContextMenu
import com.github.jershell.shadcn.ui.components.demo.DemoDatePicker
import com.github.jershell.shadcn.ui.components.demo.DemoDialog
import com.github.jershell.shadcn.ui.components.demo.DemoDrawer
import com.github.jershell.shadcn.ui.components.demo.DemoDropdownMenu
import com.github.jershell.shadcn.ui.components.demo.DemoEmpty
import com.github.jershell.shadcn.ui.components.demo.DemoField
import com.github.jershell.shadcn.ui.components.demo.DemoInput
import com.github.jershell.shadcn.ui.components.demo.DemoInputGroup
import com.github.jershell.shadcn.ui.components.demo.DemoItem
import com.github.jershell.shadcn.ui.components.demo.DemoKbd
import com.github.jershell.shadcn.ui.components.demo.DemoLabel
import com.github.jershell.shadcn.ui.components.demo.DemoMenubar
import com.github.jershell.shadcn.ui.components.demo.DemoNavigationMenu
import com.github.jershell.shadcn.ui.components.demo.DemoPagination
import com.github.jershell.shadcn.ui.components.demo.DemoPopover
import com.github.jershell.shadcn.ui.components.demo.DemoProgress
import com.github.jershell.shadcn.ui.components.demo.DemoQuestionnaire
import com.github.jershell.shadcn.ui.components.demo.DemoRadio
import com.github.jershell.shadcn.ui.components.demo.DemoResizable
import com.github.jershell.shadcn.ui.components.demo.DemoSelect
import com.github.jershell.shadcn.ui.components.demo.DemoSeparator
import com.github.jershell.shadcn.ui.components.demo.DemoSheet
import com.github.jershell.shadcn.ui.components.demo.DemoSidebar
import com.github.jershell.shadcn.ui.components.demo.DemoSkeleton
import com.github.jershell.shadcn.ui.components.demo.DemoSlider
import com.github.jershell.shadcn.ui.components.demo.DemoSpinner
import com.github.jershell.shadcn.ui.components.demo.DemoSwitch
import com.github.jershell.shadcn.ui.components.demo.DemoTable
import com.github.jershell.shadcn.ui.components.demo.DemoTabs
import com.github.jershell.shadcn.ui.components.demo.DemoTextarea
import com.github.jershell.shadcn.ui.components.demo.DemoToast
import com.github.jershell.shadcn.ui.components.demo.DemoToggle
import com.github.jershell.shadcn.ui.components.demo.DemoToggleGroup
import com.github.jershell.shadcn.ui.components.demo.DemoTooltip
import com.github.jershell.shadcn.ui.components.demo.DemoTypography
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_callout_for_displaying_important_messages
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_carousel_with_snapping_and_previous_next_contr
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_circular_image_representation_with_a_fallback
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_command_palette_with_filtering_and_keyboard_na
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_container_for_grouping_related_content_and_act
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_control_that_allows_the_user_to_toggle_between
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_control_that_allows_the_user_to_toggle_between_2
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_data_table_with_pinned_header_horizontal_and_v
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_date_picker_with_month_navigation_selection_an
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_drawer_configured_to_slide_from_the_left_right
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_horizontal_bar_of_menus_file_edit_with_hover_s
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_horizontal_navigation_with_card_panels_below_t
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_joined_group_of_buttons_and_label_blocks
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_loading_placeholder_with_a_pulsing_animation
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_menu_that_opens_on_right_click_at_the_pointer
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_modal_window_that_overlays_the_content_local_o
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_multi_line_text_input_for_longer_messages
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_multi_step_questionnaire_with_choice_and_freef
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_navigation_helper_that_shows_the_current_locat
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_panel_that_slides_in_from_the_bottom_edge_with
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_placeholder_for_missing_content_with_media_tex
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_popup_that_displays_information_on_hover_or_ke
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_searchable_select_that_lets_the_user_pick_from
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_set_of_checkable_buttons_where_only_one_can_be
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_set_of_layered_sections_of_content_known_as_ta
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_set_of_two_state_buttons_with_single_or_multip
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_small_status_indicator_for_labels_counts_and_t
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_text_label_for_form_controls
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_trigger_button_with_a_date_opening_a_calendar
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_two_state_button_that_can_be_either_on_or_off
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_versatile_row_component_with_media_content_and
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_vertically_stacked_set_of_collapsible_sections
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_a_visual_divider_between_content_sections
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_accessible_resizable_panel_groups_and_layouts
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_accordion
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_add_addons_buttons_and_helper_content_to_inputs
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_alert
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_an_indicator_that_can_be_used_to_show_a_loading
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_an_input_for_selecting_a_value_from_a_range_via
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_an_interactive_container_that_shows_or_hides_its
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_attachment
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_avatar
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_badge
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_breadcrumb
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_button
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_button_group
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_calendar
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_card
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_carousel
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_charts
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_checkbox
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_collapsible
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_combobox
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_command
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_context_menu
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_date_picker
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_dialog
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_a_form_input_field_or_a_component_that
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_a_keyboard_shortcut
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_a_list_of_options_for_the_user_to_pick
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_a_menu_anchored_to_a_trigger_items_chec
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_an_indicator_showing_the_completion_pro
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_displays_rich_content_in_a_portal_triggered_by_a
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_drawer
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_dropdown_menu
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_empty
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_field
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_form_field_composition_label_title_description_e
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_input
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_input_group
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_item
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_kbd
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_label
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_menubar
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_navigation_between_pages_with_previous_next_and
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_navigation_menu
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_pagination
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_popover
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_progress
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_questionnaire
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_radio_group
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_resizable
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_select
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_separator
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_shadcn_styles_for_hdcharts
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_sheet
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_sidebar
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_skeleton
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_slider
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_spinner
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_styles_for_headings_paragraphs_lists_etc
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_switch
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_table
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_tabs
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_textarea
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_toast
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_toast_notifications_with_stacking_positions_and
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_toggle
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_toggle_group
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_tooltip
import com.github.jershell.shadcn.demoapp.generated.resources.components_registry_typography
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object ComponentsRegistry {
    @Composable
    fun get(id: ComponentId): ComponentEntry? = all()[id]

    @Composable
    fun all(): Map<ComponentId, ComponentEntry> = mapOf(
        ComponentId("Typography") to ComponentEntry(
            name = stringResource(Res.string.components_registry_typography),
            description = stringResource(Res.string.components_registry_styles_for_headings_paragraphs_lists_etc),
            demo = {
                DemoTypography()
            }
        ),
        ComponentId("Sidebar") to ComponentEntry(
            name = stringResource(Res.string.components_registry_sidebar),
            description = "",
            demo = {
                DemoSidebar()
            }
        ),
        ComponentId("Attachment") to ComponentEntry(
            name = stringResource(Res.string.components_registry_attachment),
            description = "",
            demo = {
                DemoAttachment()
            }
        ),
        ComponentId("Questionnaire") to ComponentEntry(
            name = stringResource(Res.string.components_registry_questionnaire),
            description = stringResource(Res.string.components_registry_a_multi_step_questionnaire_with_choice_and_freef),
            demo = {
                DemoQuestionnaire()
            }
        ),
        ComponentId("DemoCharts") to ComponentEntry(
            name = stringResource(Res.string.components_registry_charts),
            description = stringResource(Res.string.components_registry_shadcn_styles_for_hdcharts),
            demo = {
                DemoCharts()
            }
        ),
        ComponentId("Skeleton") to ComponentEntry(
            name = stringResource(Res.string.components_registry_skeleton),
            description = stringResource(Res.string.components_registry_a_loading_placeholder_with_a_pulsing_animation),
            demo = {
                DemoSkeleton()
            }
        ),
        ComponentId("Slider") to ComponentEntry(
            name = stringResource(Res.string.components_registry_slider),
            description = stringResource(Res.string.components_registry_an_input_for_selecting_a_value_from_a_range_via),
            demo = {
                DemoSlider()
            }
        ),
        ComponentId("Accordion") to ComponentEntry(
            name = stringResource(Res.string.components_registry_accordion),
            description = stringResource(Res.string.components_registry_a_vertically_stacked_set_of_collapsible_sections),
            demo = {
                DemoAccordion()
            }
        ),
        ComponentId("Alert") to ComponentEntry(
            name = stringResource(Res.string.components_registry_alert),
            description = stringResource(Res.string.components_registry_a_callout_for_displaying_important_messages),
            demo = {
                DemoAlert()
            }
        ),
        ComponentId("Breadcrumb") to ComponentEntry(
            name = stringResource(Res.string.components_registry_breadcrumb),
            description = stringResource(Res.string.components_registry_a_navigation_helper_that_shows_the_current_locat),
            demo = {
                DemoBreadcrumb()
            }
        ),
        ComponentId("Calendar") to ComponentEntry(
            name = stringResource(Res.string.components_registry_calendar),
            description = stringResource(Res.string.components_registry_a_date_picker_with_month_navigation_selection_an),
            demo = {
                DemoCalendar()
            }
        ),
        ComponentId("Avatar") to ComponentEntry(
            name = stringResource(Res.string.components_registry_avatar),
            description = stringResource(Res.string.components_registry_a_circular_image_representation_with_a_fallback),
            demo = {
                DemoAvatar()
            }
        ),
        ComponentId("Badge") to ComponentEntry(
            name = stringResource(Res.string.components_registry_badge),
            description = stringResource(Res.string.components_registry_a_small_status_indicator_for_labels_counts_and_t),
            demo = {
                DemoBadge()
            }
        ),
        ComponentId("Button") to ComponentEntry(
            name = stringResource(Res.string.components_registry_button),
            description = "",
            demo = {
                DemoButton()
            }
        ),
        ComponentId("Button Group") to ComponentEntry(
            name = stringResource(Res.string.components_registry_button_group),
            description = stringResource(Res.string.components_registry_a_joined_group_of_buttons_and_label_blocks),
            demo = {
                DemoButtonGroup()
            }
        ),
        ComponentId("Card") to ComponentEntry(
            name = stringResource(Res.string.components_registry_card),
            description = stringResource(Res.string.components_registry_a_container_for_grouping_related_content_and_act),
            demo = {
                DemoCard()
            }
        ),
        ComponentId("Carousel") to ComponentEntry(
            name = stringResource(Res.string.components_registry_carousel),
            description = stringResource(Res.string.components_registry_a_carousel_with_snapping_and_previous_next_contr),
            demo = {
                DemoCarousel()
            }
        ),
        ComponentId("Select") to ComponentEntry(
            name = stringResource(Res.string.components_registry_select),
            description = stringResource(Res.string.components_registry_displays_a_list_of_options_for_the_user_to_pick),
            demo = {
                DemoSelect()
            }
        ),
        ComponentId("Combobox") to ComponentEntry(
            name = stringResource(Res.string.components_registry_combobox),
            description = stringResource(Res.string.components_registry_a_searchable_select_that_lets_the_user_pick_from),
            demo = {
                DemoCombobox()
            }
        ),
        ComponentId("Dialog") to ComponentEntry(
            name = stringResource(Res.string.components_registry_dialog),
            description = stringResource(Res.string.components_registry_a_modal_window_that_overlays_the_content_local_o),
            demo = {
                DemoDialog()
            }
        ),
        ComponentId("Dropdown Menu") to ComponentEntry(
            name = stringResource(Res.string.components_registry_dropdown_menu),
            description = stringResource(Res.string.components_registry_displays_a_menu_anchored_to_a_trigger_items_chec),
            demo = {
                DemoDropdownMenu()
            }
        ),
        ComponentId("Context Menu") to ComponentEntry(
            name = stringResource(Res.string.components_registry_context_menu),
            description = stringResource(Res.string.components_registry_a_menu_that_opens_on_right_click_at_the_pointer),
            demo = {
                DemoContextMenu()
            }
        ),
        ComponentId("Menubar") to ComponentEntry(
            name = stringResource(Res.string.components_registry_menubar),
            description = stringResource(Res.string.components_registry_a_horizontal_bar_of_menus_file_edit_with_hover_s),
            demo = {
                DemoMenubar()
            }
        ),
        ComponentId("Navigation Menu") to ComponentEntry(
            name = stringResource(Res.string.components_registry_navigation_menu),
            description = stringResource(Res.string.components_registry_a_horizontal_navigation_with_card_panels_below_t),
            demo = {
                DemoNavigationMenu()
            }
        ),
        ComponentId("Date Picker") to ComponentEntry(
            name = stringResource(Res.string.components_registry_date_picker),
            description = stringResource(Res.string.components_registry_a_trigger_button_with_a_date_opening_a_calendar),
            demo = {
                DemoDatePicker()
            }
        ),
        ComponentId("Field") to ComponentEntry(
            name = stringResource(Res.string.components_registry_field),
            description = stringResource(Res.string.components_registry_form_field_composition_label_title_description_e),
            demo = {
                DemoField()
            }
        ),
        ComponentId("Drawer") to ComponentEntry(
            name = stringResource(Res.string.components_registry_drawer),
            description = stringResource(Res.string.components_registry_a_panel_that_slides_in_from_the_bottom_edge_with),
            demo = {
                DemoDrawer()
            }
        ),
        ComponentId("Sheet") to ComponentEntry(
            name = stringResource(Res.string.components_registry_sheet),
            description = stringResource(Res.string.components_registry_a_drawer_configured_to_slide_from_the_left_right),
            demo = {
                DemoSheet()
            }
        ),
        ComponentId("Empty") to ComponentEntry(
            name = stringResource(Res.string.components_registry_empty),
            description = stringResource(Res.string.components_registry_a_placeholder_for_missing_content_with_media_tex),
            demo = {
                DemoEmpty()
            }
        ),
        ComponentId("Input") to ComponentEntry(
            name = stringResource(Res.string.components_registry_input),
            description = stringResource(Res.string.components_registry_displays_a_form_input_field_or_a_component_that),
            demo = {
                DemoInput()
            }
        ),
        ComponentId("Input Group") to ComponentEntry(
            name = stringResource(Res.string.components_registry_input_group),
            description = stringResource(Res.string.components_registry_add_addons_buttons_and_helper_content_to_inputs),
            demo = {
                DemoInputGroup()
            }
        ),
        ComponentId("Label") to ComponentEntry(
            name = stringResource(Res.string.components_registry_label),
            description = stringResource(Res.string.components_registry_a_text_label_for_form_controls),
            demo = {
                DemoLabel()
            }
        ),
        ComponentId("Checkbox") to ComponentEntry(
            name = stringResource(Res.string.components_registry_checkbox),
            description = stringResource(Res.string.components_registry_a_control_that_allows_the_user_to_toggle_between),
            demo = {
                DemoCheckbox()
            }
        ),
        ComponentId("Collapsible") to ComponentEntry(
            name = stringResource(Res.string.components_registry_collapsible),
            description = stringResource(Res.string.components_registry_an_interactive_container_that_shows_or_hides_its),
            demo = {
                DemoCollapsible()
            }
        ),
        ComponentId("Command") to ComponentEntry(
            name = stringResource(Res.string.components_registry_command),
            description = stringResource(Res.string.components_registry_a_command_palette_with_filtering_and_keyboard_na),
            demo = {
                DemoCommand()
            }
        ),
        ComponentId("Radio") to ComponentEntry(
            name = stringResource(Res.string.components_registry_radio_group),
            description = stringResource(Res.string.components_registry_a_set_of_checkable_buttons_where_only_one_can_be),
            demo = {
                DemoRadio()
            }
        ),
        ComponentId("Separator") to ComponentEntry(
            name = stringResource(Res.string.components_registry_separator),
            description = stringResource(Res.string.components_registry_a_visual_divider_between_content_sections),
            demo = {
                DemoSeparator()
            }
        ),
        ComponentId("Switch") to ComponentEntry(
            name = stringResource(Res.string.components_registry_switch),
            description = stringResource(Res.string.components_registry_a_control_that_allows_the_user_to_toggle_between_2),
            demo = {
                DemoSwitch()
            }
        ),
        ComponentId("Toggle") to ComponentEntry(
            name = stringResource(Res.string.components_registry_toggle),
            description = stringResource(Res.string.components_registry_a_two_state_button_that_can_be_either_on_or_off),
            demo = {
                DemoToggle()
            }
        ),
        ComponentId("Toggle Group") to ComponentEntry(
            name = stringResource(Res.string.components_registry_toggle_group),
            description = stringResource(Res.string.components_registry_a_set_of_two_state_buttons_with_single_or_multip),
            demo = {
                DemoToggleGroup()
            }
        ),
        ComponentId("Spinner") to ComponentEntry(
            name = stringResource(Res.string.components_registry_spinner),
            description = stringResource(Res.string.components_registry_an_indicator_that_can_be_used_to_show_a_loading),
            demo = {
                DemoSpinner()
            }
        ),
        ComponentId("Tabs") to ComponentEntry(
            name = stringResource(Res.string.components_registry_tabs),
            description = stringResource(Res.string.components_registry_a_set_of_layered_sections_of_content_known_as_ta),
            demo = {
                DemoTabs()
            }
        ),
        ComponentId("Table") to ComponentEntry(
            name = stringResource(Res.string.components_registry_table),
            description = stringResource(Res.string.components_registry_a_data_table_with_pinned_header_horizontal_and_v),
            demo = {
                DemoTable()
            }
        ),
        ComponentId("Toast") to ComponentEntry(
            name = stringResource(Res.string.components_registry_toast),
            description = stringResource(Res.string.components_registry_toast_notifications_with_stacking_positions_and),
            demo = {
                DemoToast()
            }
        ),
        ComponentId("Pagination") to ComponentEntry(
            name = stringResource(Res.string.components_registry_pagination),
            description = stringResource(Res.string.components_registry_navigation_between_pages_with_previous_next_and),
            demo = {
                DemoPagination()
            }
        ),
        ComponentId("Popover") to ComponentEntry(
            name = stringResource(Res.string.components_registry_popover),
            description = stringResource(Res.string.components_registry_displays_rich_content_in_a_portal_triggered_by_a),
            demo = {
                DemoPopover()
            }
        ),
        ComponentId("Progress") to ComponentEntry(
            name = stringResource(Res.string.components_registry_progress),
            description = stringResource(Res.string.components_registry_displays_an_indicator_showing_the_completion_pro),
            demo = {
                DemoProgress()
            }
        ),
        ComponentId("Resizable") to ComponentEntry(
            name = stringResource(Res.string.components_registry_resizable),
            description = stringResource(Res.string.components_registry_accessible_resizable_panel_groups_and_layouts),
            demo = {
                DemoResizable()
            }
        ),
        ComponentId("Item") to ComponentEntry(
            name = stringResource(Res.string.components_registry_item),
            description = stringResource(Res.string.components_registry_a_versatile_row_component_with_media_content_and),
            demo = {
                DemoItem()
            }
        ),
        ComponentId("Textarea") to ComponentEntry(
            name = stringResource(Res.string.components_registry_textarea),
            description = stringResource(Res.string.components_registry_a_multi_line_text_input_for_longer_messages),
            demo = {
                DemoTextarea()
            }
        ),
        ComponentId("Kbd") to ComponentEntry(
            name = stringResource(Res.string.components_registry_kbd),
            description = stringResource(Res.string.components_registry_displays_a_keyboard_shortcut),
            demo = {
                DemoKbd()
            }
        ),
        ComponentId("Tooltip") to ComponentEntry(
            name = stringResource(Res.string.components_registry_tooltip),
            description = stringResource(Res.string.components_registry_a_popup_that_displays_information_on_hover_or_ke),
            demo = {
                DemoTooltip()
            }
        )
    ).toList().sortedBy { it.second.name }.toMap()
}

class ComponentEntry(
    val name: String,
    val description: String,
    val demo: @Composable () -> Unit = {}
)

@JvmInline
@Serializable
value class ComponentId(val id: String)
