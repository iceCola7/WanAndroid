package com.cxz.wanandroid.widget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.cxz.wanandroid.R

/**
 * @author chenxz
 * @date 2018/9/19
 * @desc 倾斜的 TextView
 */
class TiltTextView : View {

    private val MODE_LEFT_TOP = 0
    private val MODE_LEFT_TOP_TRIANGLE = 1
    private val MODE_LEFT_BOTTOM = 2
    private val MODE_LEFT_BOTTOM_TRIANGLE = 3
    private val MODE_RIGHT_TOP = 4
    private val MODE_RIGHT_TOP_TRIANGLE = 5
    private val MODE_RIGHT_BOTTOM = 6
    private val MODE_RIGHT_BOTTOM_TRIANGLE = 7

    val ROTATE_ANGLE = 45

    private var mPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mTiltBgColor = Color.TRANSPARENT
    private var mTextColor = Color.WHITE
    private var mTextSize = 16f
    private var mTiltLength = 40f
    private var mTiltText: String? = ""
    private var mMode = MODE_LEFT_TOP

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TiltTextView)
        mTiltBgColor = array.getColor(R.styleable.TiltTextView_tiltBgColor, mTiltBgColor)
        mTextSize = array.getDimension(R.styleable.TiltTextView_tiltTextSize, mTextSize)
        mTextColor = array.getColor(R.styleable.TiltTextView_tiltTextColor, mTextColor)
        mTiltLength = array.getDimension(R.styleable.TiltTextView_tiltLength, mTiltLength)
        if (array.hasValue(R.styleable.TiltTextView_tiltText)) {
            mTiltText = array.getString(R.styleable.TiltTextView_tiltText)
        }
        if (array.hasValue(R.styleable.TiltTextView_tiltMode)) {
            mMode = array.getInt(R.styleable.TiltTextView_tiltMode, MODE_LEFT_TOP)
        }
        array.recycle()

        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint?.style = Paint.Style.FILL
        mPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        mPaint?.isAntiAlias = true
        mPaint?.color = mTiltBgColor

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint?.isAntiAlias = true
        mTextPaint?.textSize = mTextSize
        mTextPaint?.color = mTextColor

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawText(canvas)
    }

    /**
     * 绘制控件背景
     */
    private fun drawBg(canvas: Canvas) {
        var path = Path()
        val w = width
        val h = height

        if (w != h) throw IllegalStateException("TiltTextView's width must equal to height")

        when (mMode) {
            MODE_LEFT_TOP -> path = getModeLeftTopPath(path, w, h)
            MODE_LEFT_TOP_TRIANGLE -> path = getModeLeftTopTrianglePath(path, w, h)
            MODE_LEFT_BOTTOM -> path = getModeLeftBottomPath(path, w, h)
            MODE_LEFT_BOTTOM_TRIANGLE -> path = getModeLeftBottomTrianglePath(path, w, h)
            MODE_RIGHT_TOP -> path = getModeRightTopPath(path, w, h)
            MODE_RIGHT_TOP_TRIANGLE -> path = getModeRightTopTrianglePath(path, w, h)
            MODE_RIGHT_BOTTOM -> path = getModeRightBottomPath(path, w, h)
            MODE_RIGHT_BOTTOM_TRIANGLE -> path = getModeRightBottomTrianglePath(path, w, h)
        }

        path.close()
        canvas.drawPath(path, mPaint!!)
        canvas.save()
    }

    private fun getModeRightBottomTrianglePath(path: Path, w: Int, h: Int): Path {
        path.moveTo(0f, h.toFloat())
        path.lineTo(w.toFloat(), h.toFloat())
        path.lineTo(w.toFloat(), 0f)
        return path
    }

    private fun getModeRightBottomPath(path: Path, w: Int, h: Int): Path {
        path.moveTo(0f, h.toFloat())
        path.lineTo(mTiltLength, h.toFloat())
        path.lineTo(w.toFloat(), mTiltLength)
        path.lineTo(w.toFloat(), 0f)
        return path
    }

    private fun getModeRightTopTrianglePath(path: Path, w: Int, h: Int): Path {
        path.lineTo(w.toFloat(), 0f)
        path.lineTo(w.toFloat(), h.toFloat())
        return path
    }

    private fun getModeRightTopPath(path: Path, w: Int, h: Int): Path {
        path.lineTo(w.toFloat(), h.toFloat())
        path.lineTo(w.toFloat(), h - mTiltLength)
        path.lineTo(mTiltLength, 0f)
        return path
    }

    private fun getModeLeftBottomTrianglePath(path: Path, w: Int, h: Int): Path {
        path.lineTo(w.toFloat(), h.toFloat())
        path.lineTo(0f, h.toFloat())
        return path
    }

    private fun getModeLeftBottomPath(path: Path, w: Int, h: Int): Path {
        path.lineTo(w.toFloat(), h.toFloat())
        path.lineTo(w - mTiltLength, h.toFloat())
        path.lineTo(0f, mTiltLength)
        return path
    }

    private fun getModeLeftTopPath(path: Path, w: Int, h: Int): Path {
        path.moveTo(w.toFloat(), 0f)
        path.lineTo(0f, h.toFloat())
        path.lineTo(0f, h - mTiltLength)
        path.lineTo(w - mTiltLength, 0f)
        return path
    }

    private fun getModeLeftTopTrianglePath(path: Path, w: Int, h: Int): Path {
        path.lineTo(0f, h.toFloat())
        path.lineTo(w.toFloat(), 0f)
        return path
    }

    /**
     * 绘制文字
     */
    private fun drawText(canvas: Canvas) {
        val w = (canvas.width - mTiltLength / 2).toInt()
        val h = (canvas.height - mTiltLength / 2).toInt()
        val xy = calculateXY(canvas, w, h)

        val toX = xy[0]
        val toY = xy[1]
        val centerX = xy[2]
        val centerY = xy[3]
        val angle = xy[4]

        canvas.rotate(angle, centerX, centerY)

        canvas.drawText(mTiltText!!, toX, toY, mTextPaint!!)
    }

    private fun calculateXY(canvas: Canvas, w: Int, h: Int): FloatArray {
        val xy = FloatArray(5)
        var rect: Rect? = null
        var rectF: RectF? = null
        val offset = (mTiltLength / 2).toInt()

        when (mMode) {
            MODE_LEFT_TOP, MODE_LEFT_TOP_TRIANGLE -> {
                rect = Rect(0, 0, w, h)
                rectF = RectF(rect)
                rectF.right = mTextPaint!!.measureText(mTiltText, 0, mTiltText!!.length)
                rectF.bottom = mTextPaint!!.descent() - mTextPaint!!.ascent()
                rectF.left += (rect.width() - rectF.right) / 2.0f
                rectF.top += (rect.height() - rectF.bottom) / 2.0f
                xy[0] = rectF.left
                xy[1] = rectF.top - mTextPaint!!.ascent()
                xy[2] = (w / 2).toFloat()
                xy[3] = (h / 2).toFloat()
                xy[4] = (-ROTATE_ANGLE).toFloat()
            }
        }
        return xy
    }

    //================= API =====================

    fun setText(text: String): TiltTextView {
        mTiltText = text
        postInvalidate()
        return this
    }

    fun setText(resId: Int): TiltTextView {
        val text = resources.getString(resId)
        if (!TextUtils.isEmpty(text)) {
            setText(text)
        }
        return this
    }

    fun getText(): String? {
        return mTiltText
    }

    fun setTiltBgColor(color: Int): TiltTextView {
        mTiltBgColor = color
        mPaint!!.color = mTiltBgColor
        postInvalidate()
        return this
    }

    fun setTextColor(color: Int): TiltTextView {
        mTextColor = color
        mTextPaint!!.color = mTextColor
        postInvalidate()
        return this
    }

    fun setTextSize(size: Int): TiltTextView {
        mTextSize = size.toFloat()
        mTextPaint!!.textSize = mTextSize
        postInvalidate()
        return this
    }

    fun setTiltLength(length: Int): TiltTextView {
        mTiltLength = length.toFloat()
        postInvalidate()
        return this
    }
}
