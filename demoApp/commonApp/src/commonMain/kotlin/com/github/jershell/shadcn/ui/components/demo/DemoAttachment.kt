package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.FileText
import com.composables.icons.lucide.Lucide
import com.github.jershell.shadcn.components.attachment.Attachment
import com.github.jershell.shadcn.components.attachment.AttachmentSpec
import com.github.jershell.shadcn.components.attachment.AttachmentStatus
import com.github.jershell.shadcn.components.icon.toShadcnIcon
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.InlineCode
import com.github.jershell.shadcn.components.typography.P
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_removed_backup_tar_gz
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_removed_cyclone_png
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_removed_report_q3_pdf
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_size_mb_gzip_archive
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_size_mb_of_mb
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_size_mb_pdf_document
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_size_mb_png_image
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_upload_cancelled
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_a_failed_upload_with_a_retry_action
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_a_file_attachment_with_an_icon_preview_and_metad
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_an_image_attachment_renders_the_image_itself_as
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_backup_tar_gz
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_basic
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_cyclone_png
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_dataset_zip
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_error
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_image
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_opening_cyclone_png
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_opening_report_q3_pdf
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_report_q3_pdf
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_upload_failed_network_error
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_upload_progress_with_the_progress_component_and
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_uploading
import com.github.jershell.shadcn.demoapp.generated.resources.attachment_usage
import com.github.jershell.shadcn.demoapp.generated.resources.ic_cyclone
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun DemoAttachment() {
    val s_attachment_removed_backup_tar_gz = stringResource(Res.string.attachment_removed_backup_tar_gz)
    val s_attachment_upload_cancelled = stringResource(Res.string.attachment_upload_cancelled)
    val s_attachment_removed_cyclone_png = stringResource(Res.string.attachment_removed_cyclone_png)
    val s_attachment_opening_cyclone_png = stringResource(Res.string.attachment_opening_cyclone_png)
    val s_attachment_removed_report_q3_pdf = stringResource(Res.string.attachment_removed_report_q3_pdf)
    val s_attachment_opening_report_q3_pdf = stringResource(Res.string.attachment_opening_report_q3_pdf)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        DemoSection(
            title = stringResource(Res.string.attachment_basic),
            description = stringResource(Res.string.attachment_a_file_attachment_with_an_icon_preview_and_metad),
        ) {
            Attachment(
                spec = AttachmentSpec(
                    name = stringResource(Res.string.attachment_report_q3_pdf),
                    size = stringResource(Res.string.attachment_size_mb_pdf_document),
                    icon = Lucide.FileText.toShadcnIcon(),
                    status = AttachmentStatus.Idle,
                ),
                onOpen = { Toast(s_attachment_opening_report_q3_pdf) },
                onRemove = { Toast.destructive(s_attachment_removed_report_q3_pdf) },
            )
        }
        DemoSection(
            title = stringResource(Res.string.attachment_image),
            description = stringResource(Res.string.attachment_an_image_attachment_renders_the_image_itself_as),
        ) {
            Attachment(
                spec = AttachmentSpec(
                    name = stringResource(Res.string.attachment_cyclone_png),
                    size = stringResource(Res.string.attachment_size_mb_png_image),
                    preview = painterResource(Res.drawable.ic_cyclone),
                    status = AttachmentStatus.Idle,
                ),
                onOpen = { Toast(s_attachment_opening_cyclone_png) },
                onRemove = { Toast.destructive(s_attachment_removed_cyclone_png) },
            )
        }
        DemoSection(
            title = stringResource(Res.string.attachment_uploading),
            description = stringResource(Res.string.attachment_upload_progress_with_the_progress_component_and),
        ) {
            Attachment(
                spec = AttachmentSpec(
                    name = stringResource(Res.string.attachment_dataset_zip),
                    size = stringResource(Res.string.attachment_size_mb_of_mb),
                    icon = Lucide.FileText.toShadcnIcon(),
                    status = AttachmentStatus.Uploading(progress = 0.6f),
                ),
                onRemove = { Toast.destructive(s_attachment_upload_cancelled) },
            )
        }
        DemoSection(
            title = stringResource(Res.string.attachment_error),
            description = stringResource(Res.string.attachment_a_failed_upload_with_a_retry_action),
        ) {
            Attachment(
                spec = AttachmentSpec(
                    name = stringResource(Res.string.attachment_backup_tar_gz),
                    size = stringResource(Res.string.attachment_size_mb_gzip_archive),
                    icon = Lucide.FileText.toShadcnIcon(),
                    status = AttachmentStatus.Error(message = stringResource(Res.string.attachment_upload_failed_network_error)),
                ),
                onRemove = { Toast.destructive(s_attachment_removed_backup_tar_gz) },
            )
        }
        P(stringResource(Res.string.attachment_usage))
        InlineCode(
            text = """
                Attachment(
                    spec = AttachmentSpec(
                        name = "report-q3.pdf",
                        size = stringResource(Res.string.attachment_size_mb_pdf_document),
                        icon = Lucide.FileText.toShadcnIcon(),
                        status = AttachmentStatus.Uploading(progress = 0.6f),
                    ),
                    onRemove = { ... },
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
