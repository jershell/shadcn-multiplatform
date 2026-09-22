package com.github.jershell.shadcn.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import com.composeunstyled.DialogProperties
import com.composeunstyled.Scrim
import com.composeunstyled.UnstyledDialog
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.generated.resources.Res
import com.github.jershell.shadcn.generated.resources.dialog_cancel
import com.github.jershell.shadcn.generated.resources.dialog_confirm
import kotlinx.coroutines.delay

/**
 * Window-level host rendering the [DialogManager] queue, mirroring the Base UI
 * global manager pattern. Compose it once in the application root
 * ([com.github.jershell.shadcn.containers.ShadcnUI] already does this inside the
 * [com.composeunstyled.ModalHost] layer).
 *
 * The queue is FIFO: only the front dialog is on screen; the next one opens after
 * the current dialog finishes its exit animation.
 *
 * @param manager Queue to render and mutate.
 */
@Composable
fun DialogHost(manager: DialogManager = Dialogs.manager) {
    val current = manager.entries.firstOrNull() ?: return

    key(current.id) {
        UnstyledDialog(
            visible = !current.exiting,
            onDismissRequest = {
                current.spec.onCancel?.invoke()
                current.exiting = true
            },
            properties = DialogProperties(
                dismissOnBackPress = current.spec.dismissOnBackPress,
                dismissOnClickOutside = current.spec.dismissOnClickOutside,
            ),
            overlay = {
                Scrim(
                    scrimColor = DialogScrimColor, // bg-black/50
                    enter = fadeIn(tween(DialogAnimMillis)),
                    exit = fadeOut(tween(DialogAnimMillis)),
                )
            },
        ) {
            AnimatedVisibility(
                visible = !current.exiting,
                enter = DialogEnter,
                exit = DialogExit,
            ) {
                DialogPanelBox(
                    showCloseButton = current.spec.showCloseButton,
                    contentPadding = TwDimensions.paddingPxToken6,
                    onClose = {
                        current.spec.onCancel?.invoke()
                        current.exiting = true
                    },
                ) {
                    DialogHeader {
                        DialogTitle(current.spec.title)
                        current.spec.description?.let { description ->
                            DialogDescription(description)
                        }
                    }
                    current.spec.content?.invoke(this)
                    val defaultConfirmLabel = stringResource(Res.string.dialog_confirm)
                    val defaultCancelLabel = stringResource(Res.string.dialog_cancel)
                    val confirmLabel = current.spec.confirmLabel
                        ?: defaultConfirmLabel.takeIf { current.spec.useDefaultLabels }
                    val cancelLabel = current.spec.cancelLabel
                        ?: defaultCancelLabel.takeIf { current.spec.useDefaultLabels }
                    if (confirmLabel != null || cancelLabel != null) {
                        DialogFooter {
                            cancelLabel?.let {
                                Button(
                                    onClick = {
                                        current.spec.onCancel?.invoke()
                                        current.exiting = true
                                    },
                                    variant = ButtonVariant.Outline,
                                ) {
                                    ButtonText(it)
                                }
                            }
                            confirmLabel?.let {
                                Button(
                                    onClick = {
                                        current.spec.onConfirm?.invoke()
                                        current.exiting = true
                                    },
                                    variant = when {
                                        current.spec.isDestructive -> ButtonVariant.Destructive
                                        else -> ButtonVariant.Default
                                    },
                                ) {
                                    ButtonText(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(current.id, current.exiting) {
        if (current.exiting) {
            delay(DialogAnimMillis + 50L)
            manager.finalizeRemove(current.id)
        }
    }
}
