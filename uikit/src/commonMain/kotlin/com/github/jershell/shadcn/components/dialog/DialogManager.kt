package com.github.jershell.shadcn.components.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Declarative description of a dialog shown through [Dialogs] / [DialogHost].
 *
 * The host builds the standard panel layout: [DialogHeader] with [title] and
 * [description], optional [content], and a [DialogFooter] with the cancel and
 * confirm buttons.
 *
 * @param title Dialog title.
 * @param description Optional secondary message below the title.
 * @param confirmLabel Label of the primary action button; `null` hides the button.
 * @param cancelLabel Label of the secondary action button; `null` hides the button.
 * @param isDestructive Renders the confirm button in the destructive variant.
 * @param dismissOnBackPress Whether Escape/back press dismisses the dialog.
 * @param dismissOnClickOutside Whether clicks on the scrim dismiss the dialog.
 * @param showCloseButton Whether the close (X) button is rendered.
 * @param content Optional custom body rendered between the header and the footer.
 *   The receiver [DialogScope] provides [DialogScope.close] and column layout modifiers.
 * @param onConfirm Called when the confirm button is pressed, before the dialog closes.
 * @param onCancel Called when the dialog is dismissed without confirm (cancel button,
 *   scrim click, Escape/back press, close button).
 */
data class DialogSpec(
    val title: String,
    val description: String? = null,
    val confirmLabel: String? = null,
    val cancelLabel: String? = null,
    val isDestructive: Boolean = false,
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val showCloseButton: Boolean = true,
    val content: (@Composable DialogScope.() -> Unit)? = null,
    val onConfirm: (() -> Unit)? = null,
    val onCancel: (() -> Unit)? = null,
    /** When true, unspecified labels resolve to library defaults (see [DialogHost]). */
    val useDefaultLabels: Boolean = false,
)

internal class DialogEntry(
    val id: Long,
    val spec: DialogSpec,
) {
    var exiting by mutableStateOf(false)
}

/**
 * FIFO queue of dialogs rendered by [DialogHost], mirroring the Base UI global
 * toast manager pattern: dialogs can be queued from anywhere in the app
 * (including non-composable code) while a single host renders them.
 *
 * Only the front dialog is visible at a time; the next one opens after the
 * current dialog finishes its exit animation.
 *
 * Must be used from the UI thread (state-backed queue).
 */
class DialogManager {

    internal val entries = mutableStateListOf<DialogEntry>()
    private var idCounter = 0L

    /**
     * Queues [spec] and returns the dialog id.
     */
    fun show(spec: DialogSpec): Long {
        val id = ++idCounter
        entries += DialogEntry(id, spec)
        return id
    }

    /**
     * Starts the dismiss animation of the dialog with [id]. No-op for unknown,
     * already exiting or queued (not yet shown) dialogs.
     */
    fun close(id: Long) {
        val entry = entries.firstOrNull { it.id == id && !it.exiting } ?: return
        // Only the front dialog is on screen; queued ones are removed instantly.
        if (entries.first() === entry) {
            entry.spec.onCancel?.invoke()
            entry.exiting = true
        } else {
            entries.removeAll { it.id == id }
        }
    }

    /**
     * Removes every dialog immediately, invoking [DialogSpec.onCancel] for the
     * dialog currently on screen.
     */
    fun clear() {
        entries.firstOrNull()?.spec?.onCancel?.invoke()
        entries.clear()
    }

    internal fun finalizeRemove(id: Long) {
        entries.removeAll { it.id == id }
    }
}

/**
 * Global dialog facade with a [Toast]-style ergonomics, backed by [Dialogs.manager].
 *
 * ```
 * Dialogs.alert("Saved", "Your changes are stored.")
 * Dialogs.confirm(
 *     title = "Delete account",
 *     description = "This action cannot be undone.",
 *     confirmLabel = "Delete",
 *     isDestructive = true,
 *     onConfirm = { deleteAccount() },
 * )
 * ```
 *
 * The default [DialogHost] inside
 * [com.github.jershell.shadcn.containers.ShadcnUI] renders [Dialogs.manager];
 * pass a custom manager to both to keep separate queues.
 */
object Dialogs {

    /**
     * The manager used by the [Dialogs] helpers and the default [DialogHost].
     */
    val manager = DialogManager()

    /**
     * Queues [spec]. See [DialogManager.show] for details.
     */
    fun show(spec: DialogSpec): Long = manager.show(spec)

    /**
     * Queues an informational dialog with a single confirm button.
     */
    fun alert(
        title: String,
        description: String? = null,
        confirmLabel: String? = null,
        onConfirm: (() -> Unit)? = null,
    ): Long = manager.show(
        DialogSpec(
            title = title,
            description = description,
            confirmLabel = confirmLabel,
            onConfirm = onConfirm,
            useDefaultLabels = true,
        ),
    )

    /**
     * Queues a confirmation dialog with confirm and cancel buttons.
     * Set [isDestructive] for destructive confirmations (red confirm button).
     */
    fun confirm(
        title: String,
        description: String? = null,
        confirmLabel: String? = null,
        cancelLabel: String? = null,
        isDestructive: Boolean = false,
        onConfirm: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null,
    ): Long = manager.show(
        DialogSpec(
            title = title,
            description = description,
            confirmLabel = confirmLabel,
            cancelLabel = cancelLabel,
            isDestructive = isDestructive,
            onConfirm = onConfirm,
            onCancel = onCancel,
            useDefaultLabels = true,
        ),
    )

    /**
     * Starts the dismiss animation for the dialog with [id].
     */
    fun close(id: Long) = manager.close(id)

    /**
     * Removes every dialog immediately.
     */
    fun clear() = manager.clear()
}
