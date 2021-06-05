package com.cxz.wanandroid.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.cxz.wanandroid.utils.AnimatorUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by chenxz on 2018/5/13.
 *
 * FAB 上滑隐藏 下滑显示
 */
class ScrollAwareFABBehavior : FloatingActionButton.Behavior {

    /**
     * 是否正在动画
     */
    private var isAnimateIng = false

    /**
     * 是否已经显示
     */
    private var isShow = true

    constructor(context: Context, attrs: AttributeSet) : super()

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        // 手指上滑，隐藏FAB
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimateIng && isShow) {
            AnimatorUtil.translateHide(child, object : StateListener() {
                override fun onAnimationStart(view: View) {
                    super.onAnimationStart(view)
                    isShow = false
                }
            })
        } else if (dyConsumed < 0 || dyUnconsumed < 0 && !isAnimateIng && !isShow) {
            // 手指下滑，显示FAB
            AnimatorUtil.translateShow(child, object : StateListener() {
                override fun onAnimationStart(view: View) {
                    super.onAnimationStart(view)
                    isShow = true
                }
            })
        }
    }


    internal open inner class StateListener : ViewPropertyAnimatorListener {
        override fun onAnimationStart(view: View) {
            isAnimateIng = true
        }

        override fun onAnimationEnd(view: View) {
            isAnimateIng = false
        }

        override fun onAnimationCancel(view: View) {
            isAnimateIng = false
        }
    }

}