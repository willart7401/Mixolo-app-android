package fr.willban.mixolo.util.wave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorRes
import androidx.core.graphics.ColorUtils
import fr.willban.mixolo.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MultiWaveHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    var mPath: Path? = null
    var mShape: ShapeType? = ShapeType.Rect
    var mPaint = Paint()
    var mMatrix = Matrix()
    var mltWave: MutableList<Wave> = ArrayList()
    var mCornerRadius: Float
    var mWaveHeight: Int
    var mStartColor: Int
    var mCloseColor: Int
    var mGradientAngle: Int
    var isRunning: Boolean
    var isEnableFullScreen: Boolean
    var velocity: Float
    var mColorAlpha: Float
    var mProgress: Float
    var mCurProgress: Float
    var mLastTime: Long = 0
    var reboundAnimator: ValueAnimator? = null

    init {
        mPaint.isAntiAlias = true
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MultiWaveHeader)
        mWaveHeight = ta.getDimensionPixelOffset(R.styleable.MultiWaveHeader_mwhWaveHeight, WaveUtil.dp2px(6f))
        mStartColor = ta.getColor(R.styleable.MultiWaveHeader_mwhStartColor, -0xfa9330)
        mCloseColor = ta.getColor(R.styleable.MultiWaveHeader_mwhCloseColor, -0xce5002)
        mColorAlpha = ta.getFloat(R.styleable.MultiWaveHeader_mwhColorAlpha, 1f)
        velocity = ta.getFloat(R.styleable.MultiWaveHeader_mwhVelocity, 8f)
        mGradientAngle = ta.getInt(R.styleable.MultiWaveHeader_mwhGradientAngle, 119)
        isRunning = ta.getBoolean(R.styleable.MultiWaveHeader_mwhIsRunning, true)
        isEnableFullScreen = ta.getBoolean(R.styleable.MultiWaveHeader_mwhEnableFullScreen, false)
        mCornerRadius = ta.getDimensionPixelOffset(R.styleable.MultiWaveHeader_mwhCornerRadius, WaveUtil.dp2px(25f)).toFloat()
        mShape = ShapeType.values()[ta.getInt(R.styleable.MultiWaveHeader_mwhShape, mShape!!.ordinal)]
        mCurProgress = ta.getFloat(R.styleable.MultiWaveHeader_mwhProgress, 1f)
        mProgress = mCurProgress
        if (ta.hasValue(R.styleable.MultiWaveHeader_mwhWaves)) {
            tag = ta.getString(R.styleable.MultiWaveHeader_mwhWaves)
        } else if (tag == null) {
            tag = """
                70,25,1.4,1.4,-26
                100,5,1.4,1.2,15
                420,0,1.15,1,-10
                520,10,1.7,1.5,20
                220,0,1,1,-15
                """.trimIndent()
        }
        ta.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (mltWave.isEmpty()) {
            updateWavePath()
            updateWavePath(r - l, b - t)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateShapePath()
        updateWavePath(w, h)
        updateLinearGradient(w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mltWave.size > 0) {
            if (mPath != null) {
                canvas.save()
                canvas.clipPath(mPath!!)
            }
            val thisView: View = this
            val height = thisView.height
            val thisTime = System.currentTimeMillis()
            for (wave in mltWave) {
                mMatrix.reset()
                canvas.save()
                if (isRunning && mLastTime > 0 && wave.velocity != 0f) {
                    var offsetX = wave.offsetX - wave.velocity * velocity * (thisTime - mLastTime) / 1000f
                    if (-wave.velocity > 0) {
                        offsetX %= wave.width.toFloat() / 2
                    } else {
                        while (offsetX < 0) {
                            offsetX += wave.width.toFloat() / 2
                        }
                    }
                    wave.offsetX = offsetX
                    mMatrix.setTranslate(offsetX, (1 - mCurProgress) * height) //wave.offsetX =
                    canvas.translate(-offsetX, -wave.offsetY - (1 - mCurProgress) * height)
                } else {
                    mMatrix.setTranslate(wave.offsetX, (1 - mCurProgress) * height)
                    canvas.translate(-wave.offsetX, -wave.offsetY - (1 - mCurProgress) * height)
                }
                mPaint.shader.setLocalMatrix(mMatrix)
                canvas.drawPath(wave.path, mPaint)
                canvas.restore()
            }
            mLastTime = thisTime
            if (mPath != null) {
                canvas.restore()
            }
            if (isRunning) {
                invalidate()
            }
        }
    }

    private fun updateLinearGradient(width: Int, height: Int) {
        val startColor = ColorUtils.setAlphaComponent(mStartColor, (mColorAlpha * 255).toInt())
        val closeColor = ColorUtils.setAlphaComponent(mCloseColor, (mColorAlpha * 255).toInt())
        val w = width.toDouble()
        val h = (height * mCurProgress).toDouble()
        val r = sqrt(w * w + h * h) / 2
        val y = r * sin(2 * Math.PI * mGradientAngle / 360)
        val x = r * cos(2 * Math.PI * mGradientAngle / 360)
        mPaint.shader = LinearGradient(
            (w / 2 - x).toInt().toFloat(),
            (h / 2 - y).toInt().toFloat(),
            (w / 2 + x).toInt().toFloat(),
            (h / 2 + y).toInt().toFloat(),
            startColor,
            closeColor,
            Shader.TileMode.CLAMP
        )
    }

    private fun updateShapePath() {
        val thisView: View = this
        val w = thisView.width
        val h = thisView.height
        if (w > 0 && h > 0 && mShape != null && mShape != ShapeType.Rect) {
            mPath = Path()
            when (mShape) {
                ShapeType.RoundRect -> mPath!!.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), mCornerRadius, mCornerRadius, Path.Direction.CW)
                ShapeType.Oval -> mPath!!.addOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), Path.Direction.CW)
                else -> {}
            }
        } else {
            mPath = null
        }
    }

    private fun updateWavePath() {
        mltWave.clear()
        if (tag is String) {
            var waves = tag.toString().split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if ("-1" == tag) {
                waves = "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split("\\s+".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            } else if ("-2" == tag) {
                waves = "0,0,1,0.5,90\n90,0,1,0.5,90".split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            for (wave in waves) {
                val args = wave.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (args.size == 5) {
                    mltWave.add(
                        Wave(
                            WaveUtil.dp2px(args[0].toFloat()),
                            WaveUtil.dp2px(args[1].toFloat()),
                            WaveUtil.dp2px(args[4].toFloat()),
                            args[2].toFloat(),
                            args[3].toFloat(),
                            mWaveHeight / 2
                        )
                    )
                }
            }
        } else {
            mltWave.add(Wave(WaveUtil.dp2px(50f), WaveUtil.dp2px(0f), WaveUtil.dp2px(5f), 1.7f, 2f, mWaveHeight / 2))
        }
    }

    private fun updateWavePath(w: Int, h: Int) {
        for (wave in mltWave) {
            wave.updateWavePath(w, h, mWaveHeight / 2, isEnableFullScreen, mCurProgress)
        }
    }

    private fun animProgress(progress: Float, interpolator: Interpolator?, duration: Int) {
        if (mCurProgress != progress) {
            if (reboundAnimator != null) {
                reboundAnimator!!.cancel()
            }
            reboundAnimator = ValueAnimator.ofFloat(mCurProgress, progress)
            reboundAnimator?.duration = duration.toLong()
            reboundAnimator?.interpolator = interpolator
            reboundAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    reboundAnimator = null
                }
            })
            reboundAnimator?.addUpdateListener { animation: ValueAnimator -> updateProgress(animation.animatedValue as Float) }
            reboundAnimator?.start()
        }
    }

    private fun updateProgress(progress: Float) {
        val thisView: View = this
        mCurProgress = progress
        updateLinearGradient(thisView.width, thisView.height)
        if (isEnableFullScreen) {
            for (wave in mltWave) {
                wave.updateWavePath(thisView.width, thisView.height, mCurProgress)
            }
        }
        if (!isRunning) {
            invalidate()
        }
    }

    fun setWaves(waves: String?) {
        tag = waves
        if (mLastTime > 0) {
            val thisView: View = this
            updateWavePath()
            updateWavePath(thisView.width, thisView.height)
        }
    }

    var waveHeight: Int
        get() = mWaveHeight
        set(waveHeight) {
            mWaveHeight = WaveUtil.dp2px(waveHeight.toFloat())
            if (mltWave.isNotEmpty()) {
                val thisView: View = this
                updateWavePath(thisView.width, thisView.height)
            }
        }
    var progress: Float
        get() = mProgress
        set(progress) {
            mProgress = progress
            if (!isRunning) {
                updateProgress(progress)
            } else {
                animProgress(progress, DecelerateInterpolator(), 300)
            }
        }

    fun setProgress(progress: Float, interpolator: Interpolator?, duration: Int) {
        mProgress = progress
        animProgress(progress, DecelerateInterpolator(), duration)
    }

    var gradientAngle: Int
        get() = mGradientAngle
        set(angle) {
            mGradientAngle = angle
            if (mltWave.isNotEmpty()) {
                val thisView: View = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }
    var startColor: Int
        get() = mStartColor
        set(color) {
            mStartColor = color
            if (mltWave.isNotEmpty()) {
                val thisView: View = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    fun setStartColorId(@ColorRes colorId: Int) {
        val thisView: View = this
        startColor = WaveUtil.getColor(thisView.context, colorId)
    }

    var closeColor: Int
        get() = mCloseColor
        set(color) {
            mCloseColor = color
            if (mltWave.isNotEmpty()) {
                val thisView: View = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    fun setCloseColorId(@ColorRes colorId: Int) {
        val thisView: View = this
        closeColor = WaveUtil.getColor(thisView.context, colorId)
    }

    var colorAlpha: Float
        get() = mColorAlpha
        set(alpha) {
            mColorAlpha = alpha
            if (mltWave.isNotEmpty()) {
                val thisView: View = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    fun start() {
        if (!isRunning) {
            isRunning = true
            mLastTime = System.currentTimeMillis()
            invalidate()
        }
    }

    fun stop() {
        isRunning = false
    }

    var shape: ShapeType?
        get() = mShape
        set(shape) {
            mShape = shape
            updateShapePath()
        }
}