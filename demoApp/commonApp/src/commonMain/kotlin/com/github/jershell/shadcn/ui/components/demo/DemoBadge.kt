package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.badge.Badge
import com.github.jershell.shadcn.components.badge.BadgeVariant
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.badge_a_set_of_status_indicators_for_labels_counts_and
import com.github.jershell.shadcn.demoapp.generated.resources.badge_badges_can_include_a_leading_or_trailing_icon
import com.github.jershell.shadcn.demoapp.generated.resources.badge_default
import com.github.jershell.shadcn.demoapp.generated.resources.badge_destructive
import com.github.jershell.shadcn.demoapp.generated.resources.badge_new
import com.github.jershell.shadcn.demoapp.generated.resources.badge_outline
import com.github.jershell.shadcn.demoapp.generated.resources.badge_secondary
import com.github.jershell.shadcn.demoapp.generated.resources.badge_variants
import com.github.jershell.shadcn.demoapp.generated.resources.badge_verified
import com.github.jershell.shadcn.demoapp.generated.resources.badge_with_icon
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoBadge() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.badge_variants))
            P(stringResource(Res.string.badge_a_set_of_status_indicators_for_labels_counts_and))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Badge(stringResource(Res.string.badge_default))
                Badge(
                    text = stringResource(Res.string.badge_secondary),
                    variant = BadgeVariant.Secondary,
                )
                Badge(
                    text = stringResource(Res.string.badge_destructive),
                    variant = BadgeVariant.Destructive,
                )
                Badge(
                    text = stringResource(Res.string.badge_outline),
                    variant = BadgeVariant.Outline,
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            H4(stringResource(Res.string.badge_with_icon))
            P(stringResource(Res.string.badge_badges_can_include_a_leading_or_trailing_icon))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Badge(
                    text = stringResource(Res.string.badge_verified),
                    leadingIcon = Lucide.Check.toShadcnIcon(),
                )
                Badge(
                    text = stringResource(Res.string.badge_new),
                    variant = BadgeVariant.Secondary,
                    trailingIcon = Lucide.ChevronDown.toShadcnIcon(),
                )
            }
        }
    }
}
