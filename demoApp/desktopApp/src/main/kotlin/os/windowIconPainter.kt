package os

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt
import org.jetbrains.skia.Image

/**
 * Window title-bar/taskbar icon: dev-run equivalent of the packaged `iconFile`
 * (hicolor/.desktop handles the rest on installed Linux distributions).
 */
internal fun windowIconPainter(): Painter? {
    val bytes = resourceBytes("/icons/icon-256.png") ?: return null
    val bitmap = Image.makeFromEncoded(bytes).toComposeImageBitmap()
    return object : Painter() {
        override val intrinsicSize: Size = Size(256f, 256f)

        override fun DrawScope.onDraw() {
            drawImage(
                image = bitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(bitmap.width, bitmap.height),
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()),
            )
        }
    }
}
