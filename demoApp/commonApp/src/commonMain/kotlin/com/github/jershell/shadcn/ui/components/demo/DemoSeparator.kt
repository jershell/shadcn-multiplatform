package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jershell.shadcn.components.separator.Separator
import com.github.jershell.shadcn.components.separator.SeparatorOrientation
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.separator_billing
import com.github.jershell.shadcn.demoapp.generated.resources.separator_blog
import com.github.jershell.shadcn.demoapp.generated.resources.separator_docs
import com.github.jershell.shadcn.demoapp.generated.resources.separator_horizontal
import com.github.jershell.shadcn.demoapp.generated.resources.separator_profile
import com.github.jershell.shadcn.demoapp.generated.resources.separator_separates_content_in_horizontal_layouts
import com.github.jershell.shadcn.demoapp.generated.resources.separator_separates_content_in_vertical_layouts
import com.github.jershell.shadcn.demoapp.generated.resources.separator_source
import com.github.jershell.shadcn.demoapp.generated.resources.separator_usage
import com.github.jershell.shadcn.demoapp.generated.resources.separator_vertical
import org.jetbrains.compose.resources.stringResource

@Composable
fun DemoSeparator() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.separator_horizontal),
            description = stringResource(Res.string.separator_separates_content_in_vertical_layouts),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                P(stringResource(Res.string.separator_profile))
                Separator()
                P(stringResource(Res.string.separator_billing))
            }
        }

        DemoSection(
            title = stringResource(Res.string.separator_vertical),
            description = stringResource(Res.string.separator_separates_content_in_horizontal_layouts),
        ) {
            Row(
                modifier = Modifier.height(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                P(stringResource(Res.string.separator_blog))
                Separator(orientation = SeparatorOrientation.Vertical)
                P(stringResource(Res.string.separator_docs))
                Separator(orientation = SeparatorOrientation.Vertical)
                P(stringResource(Res.string.separator_source))
            }
        }

        Muted(stringResource(Res.string.separator_usage))
        InlineCode(
            text = """
                Separator()
                
                Separator(
                    orientation = SeparatorOrientation.Vertical
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
