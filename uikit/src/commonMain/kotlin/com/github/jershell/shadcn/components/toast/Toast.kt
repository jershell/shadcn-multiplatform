package com.github.jershell.shadcn.components.toast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Visual variants of the toast, matching the shadcn/ui Sonner variants.
 *
 * Only variants backed by design tokens are exposed. Success, warning and info
 * variants require dedicated tokens in the Figma export and will be added later.
 */
enum class ToastVariant {
    Default,
    Destructive,
}

/**
 * Screen corner/edge the toast viewport is anchored to.
 */
enum class ToastPosition {
    TopStart,
    TopCenter,
    TopEnd,
    BottomStart,
    BottomCenter,
    BottomEnd,
}

/**
 * An interactive button rendered inside a toast (the "Undo" pattern from Sonner
 * and Base UI). Clicking it runs [onClick] and dismisses the toast.
 */
data class ToastAction(
    val label: String,
    val onClick: () -> Unit,
)

internal class ToastEntry(
    val id: Long,
    val title: String,
    val description: String?,
    val variant: ToastVariant,
    val durationMillis: Long,
    val action: ToastAction?,
    val onDismiss: (() -> Unit)?,
) {
    var exiting by mutableStateOf(false)
}

/**
 * Toast queue and lifecycle manager, mirroring the Base UI toast manager
 * (`add`, `close`) and the `<Toast.Provider>` options (`limit`, `timeout`).
 *
 * When [limit] is exceeded, the oldest toasts start animating out. A
 * [ToastEntry.durationMillis] of `0` disables auto-dismiss for that toast.
 *
 * Must be used from the UI thread (state-backed queue).
 */
class ToastManager {

    internal val entries = mutableStateListOf<ToastEntry>()
    private var idCounter = 0L

    /**
     * Maximum number of simultaneously visible toasts. Oldest toasts beyond the
     * limit animate out. Default matches the Base UI provider `limit`.
     */
    var limit: Int = 3

    /**
     * Default auto-dismiss delay in milliseconds, used when a toast does not
     * specify its own duration. `0` disables auto-dismiss. Default matches the
     * Base UI provider `timeout`.
     */
    var defaultDurationMillis: Long = 5000L

    /**
     * Adds a toast and returns its id.
     *
     * @param title Primary toast message.
     * @param description Optional secondary message rendered below the title.
     * @param variant Visual variant of the toast.
     * @param durationMillis Auto-dismiss delay; `null` uses [defaultDurationMillis], `0` disables.
     * @param action Optional action button rendered inside the toast.
     * @param onDismiss Called when the toast is fully removed (timeout, close, action, clear).
     */
    fun add(
        title: String,
        description: String? = null,
        variant: ToastVariant = ToastVariant.Default,
        durationMillis: Long? = null,
        action: ToastAction? = null,
        onDismiss: (() -> Unit)? = null,
    ): Long {
        val id = ++idCounter
        entries += ToastEntry(
            id = id,
            title = title,
            description = description,
            variant = variant,
            durationMillis = durationMillis ?: defaultDurationMillis,
            action = action,
            onDismiss = onDismiss,
        )
        enforceLimit()
        return id
    }

    /**
     * Starts the dismiss animation for the toast with [id]. No-op for unknown
     * or already exiting toasts.
     */
    fun close(id: Long) {
        entries.firstOrNull { it.id == id && !it.exiting }?.exiting = true
    }

    /**
     * Removes every toast immediately, invoking their [ToastEntry.onDismiss] callbacks.
     */
    fun clear() {
        val removed = entries.toList()
        entries.clear()
        removed.forEach { it.onDismiss?.invoke() }
    }

    internal fun finalizeRemove(id: Long) {
        val removed = entries.firstOrNull { it.id == id } ?: return
        entries.removeAll { it.id == id }
        removed.onDismiss?.invoke()
    }

    private fun enforceLimit() {
        var overflow = entries.count { !it.exiting } - limit
        if (overflow <= 0) return
        for (entry in entries) {
            if (overflow == 0) break
            if (!entry.exiting) {
                entry.exiting = true
                overflow--
            }
        }
    }
}

/**
 * Global toast facade with Sonner ergonomics, backed by [Toast.manager].
 *
 * ```
 * Toast("Event created", description = "Monday, 10:00")
 * Toast.destructive("Something went wrong")
 * Toast.close(id)
 * Toast.clear()
 * ```
 *
 * The default [ToastHost] inside [com.github.jershell.shadcn.containers.ShadcnUI] renders
 * [Toast.manager]; pass a custom manager to both to keep separate queues.
 */
object Toast {

    /**
     * The manager used by the [Toast] helpers and the default [ToastHost].
     */
    val manager = ToastManager()

    /**
     * Adds a default-variant toast. See [ToastManager.add] for details.
     */
    operator fun invoke(
        title: String,
        description: String? = null,
        durationMillis: Long? = null,
        action: ToastAction? = null,
        onDismiss: (() -> Unit)? = null,
    ): Long = manager.add(
        title = title,
        description = description,
        variant = ToastVariant.Default,
        durationMillis = durationMillis,
        action = action,
        onDismiss = onDismiss,
    )

    /**
     * Adds a destructive-variant toast. See [ToastManager.add] for details.
     */
    fun destructive(
        title: String,
        description: String? = null,
        durationMillis: Long? = null,
        action: ToastAction? = null,
        onDismiss: (() -> Unit)? = null,
    ): Long = manager.add(
        title = title,
        description = description,
        variant = ToastVariant.Destructive,
        durationMillis = durationMillis,
        action = action,
        onDismiss = onDismiss,
    )

    /**
     * Starts the dismiss animation for the toast with [id].
     */
    fun close(id: Long) = manager.close(id)

    /**
     * Removes every toast immediately.
     */
    fun clear() = manager.clear()
}

/**
 * Viewport settings of the [ToastHost], matching the Base UI viewport placement.
 *
 * @param position Screen corner/edge the viewport is anchored to.
 * @param stacked When `true`, toasts render as a Sonner-style stacked deck that expands
 *   on hover; when `false`, they render as a plain column.
 */
data class ToasterConfig(
    val position: ToastPosition = ToastPosition.BottomEnd,
    val stacked: Boolean = true,
)
