package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.skeleton.Skeleton
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.skeleton_a_typical_loading_card_avatar_text_lines_and_an
import com.github.jershell.shadcn.demoapp.generated.resources.skeleton_basic
import com.github.jershell.shadcn.demoapp.generated.resources.skeleton_card
import com.github.jershell.shadcn.demoapp.generated.resources.skeleton_loading_lines_the_size_is_set_by_the_caller_s_mo
import com.github.jershell.shadcn.demoapp.generated.resources.skeleton_usage
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.skeleton_basic),
            description = stringResource(Res.string.skeleton_loading_lines_the_size_is_set_by_the_caller_s_mo),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Skeleton(modifier = Modifier.fillMaxWidth().height(16.dp))
                Skeleton(modifier = Modifier.fillMaxWidth(0.9f).height(16.dp))
                Skeleton(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
            }
        }

        DemoSection(
            title = stringResource(Res.string.skeleton_card),
            description = stringResource(Res.string.skeleton_a_typical_loading_card_avatar_text_lines_and_an),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Skeleton(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Skeleton(modifier = Modifier.width(160.dp).height(12.dp))
                        Skeleton(modifier = Modifier.width(100.dp).height(12.dp))
                    }
                }
                Skeleton(modifier = Modifier.fillMaxWidth().height(120.dp))
            }
        }

        Muted(stringResource(Res.string.skeleton_usage))
        InlineCode(
            text = """
                Skeleton(modifier = Modifier.fillMaxWidth().height(16.dp))
                Skeleton(modifier = Modifier.size(40.dp), shape = CircleShape)
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
