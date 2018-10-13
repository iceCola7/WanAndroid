package com.cxz.wanandroid.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by chenxz on 2018/5/9.
 *
 * RecyclerView 分割线
 */
class RecyclerViewItemDecoration : RecyclerView.ItemDecoration {

    private var context: Context
    private var orientation: Int = LinearLayoutManager.VERTICAL // 列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private var dividerHeight: Int = 2//分割线高度
    private var dividerColor: Int = 0
    private var mDivider: Drawable? = null

    private var mPaint: Paint? = null
    private var attrs: IntArray = intArrayOf(android.R.attr.listDivider)

    constructor(context: Context) : this(context, LinearLayoutManager.VERTICAL)

    constructor(context: Context, orientation: Int) {
        this.context = context
        this.orientation = orientation

        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw IllegalArgumentException("请输入正确的参数！")
        }
        var a = context.obtainStyledAttributes(attrs)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(context, orientation) {
        this.dividerHeight = dividerHeight
        this.dividerColor = dividerColor

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.let {
            it.color = dividerColor
            it.style = Paint.Style.FILL
        }
    }

    constructor(context: Context, orientation: Int, drawableId: Int) : this(context, orientation) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        dividerHeight = mDivider!!.intrinsicHeight
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, dividerHeight)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    /**
     * 绘制纵向 item 分割线
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + dividerHeight
            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }
            mPaint?.let {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

    /**
     * 绘制横向 item 分割线
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + dividerHeight
            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }
            mPaint?.let {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }
}