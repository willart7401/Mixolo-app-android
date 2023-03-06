package fr.willban.mixolo.util.wave

import android.content.Context
import android.content.res.Resources
import android.graphics.Path
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import kotlin.math.max
import kotlin.math.sin

enum class ShapeType {
    Rect, RoundRect, Oval
}

class Wave(offsetX: Int, offsetY: Int, velocity: Int, private val scaleX: Float, private val scaleY: Float, private var wave: Int) {
    var path: Path
    var width = 0
    var offsetX: Float
    var offsetY: Float
    var velocity: Float
    private var curWave = 0

    init {
        this.offsetX = offsetX.toFloat()
        this.offsetY = offsetY.toFloat()
        this.velocity = velocity.toFloat()
        path = Path()
    }

    fun updateWavePath(w: Int, h: Int, waveHeight: Int, fullScreen: Boolean, progress: Float) {
        wave = waveHeight
        width = (2 * scaleX * w).toInt()
        path = buildWavePath(width, h, fullScreen, progress)
    }

    fun updateWavePath(w: Int, h: Int, progress: Float) {
        var wave = (scaleY * wave).toInt()
        val maxWave = h * max(0f, 1 - progress)
        if (wave > maxWave) {
            wave = maxWave.toInt()
        }
        if (curWave != wave) {
            width = (2 * scaleX * w).toInt()
            path = buildWavePath(width, h, true, progress)
        }
    }

    private fun buildWavePath(width: Int, height: Int, fullScreen: Boolean, progress: Float): Path {
        var DP = WaveUtil.dp2px(1f)
        if (DP < 1) {
            DP = 1
        }
        var wave = (scaleY * wave).toInt()
        if (fullScreen) {
            val maxWave = height * max(0f, 1 - progress)
            if (wave > maxWave) {
                wave = maxWave.toInt()
            }
        }
        curWave = wave
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, (height - wave).toFloat())
        if (wave > 0) {
            var x = DP
            while (x < width) {
                path.lineTo(x.toFloat(), height - wave - wave * sin(4.0 * Math.PI * x / width).toFloat())
                x += DP
            }
        }
        path.lineTo(width.toFloat(), (height - wave).toFloat())
        path.lineTo(width.toFloat(), 0f)
        path.close()
        return path
    }
}

object WaveUtil {
    @ColorInt
    fun getColor(context: Context, @ColorRes colorId: Int): Int {
        return context.getColor(colorId)
    }

    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().displayMetrics).toInt()
    }
}