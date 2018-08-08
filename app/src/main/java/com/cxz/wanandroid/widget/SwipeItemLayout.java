package com.cxz.wanandroid.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by chenxz on 2018/8/8.
 */
public class SwipeItemLayout extends ViewGroup {
    enum Mode {
        RESET, DRAG, FLING, TAP
    }

    private Mode mTouchMode;

    private ViewGroup mMainView;
    private ViewGroup mSideView;

    private ScrollRunnable mScrollRunnable;
    private int mScrollOffset;
    private int mMaxScrollOffset;

    private boolean mInLayout;
    private boolean mIsLaidOut;

    public SwipeItemLayout(Context context) {
        this(context, null);
    }

    public SwipeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchMode = Mode.RESET;
        mScrollOffset = 0;
        mIsLaidOut = false;

        mScrollRunnable = new ScrollRunnable(context);
    }

    public boolean isOpen() {
        return mScrollOffset != 0;
    }

    Mode getTouchMode() {
        return mTouchMode;
    }

    void setTouchMode(Mode mode) {
        switch (mTouchMode) {
            case FLING:
                mScrollRunnable.abort();
                break;
            case RESET:
                break;
        }

        mTouchMode = mode;
    }

    public void open() {
        if (mScrollOffset != -mMaxScrollOffset) {
            //正在open，不需要处理
            if (mTouchMode == Mode.FLING && mScrollRunnable.isScrollToLeft())
                return;

            //当前正在向右滑，abort
            if (mTouchMode == Mode.FLING /*&& !mScrollRunnable.mScrollToLeft*/)
                mScrollRunnable.abort();

            mScrollRunnable.startScroll(mScrollOffset, -mMaxScrollOffset);
        }
    }

    public void close() {
        if (mScrollOffset != 0) {
            //正在close，不需要处理
            if (mTouchMode == Mode.FLING && !mScrollRunnable.isScrollToLeft())
                return;

            //当前正向左滑，abort
            if (mTouchMode == Mode.FLING /*&& mScrollRunnable.mScrollToLeft*/)
                mScrollRunnable.abort();

            mScrollRunnable.startScroll(mScrollOffset, 0);
        }
    }

    void fling(int xVel) {
        mScrollRunnable.startFling(mScrollOffset, xVel);
    }

    void revise() {
        if (mScrollOffset < -mMaxScrollOffset / 2)
            open();
        else
            close();
    }

    boolean trackMotionScroll(int deltaX) {
        if (deltaX == 0)
            return false;

        boolean over = false;
        int newLeft = mScrollOffset + deltaX;
        if ((deltaX > 0 && newLeft > 0) || (deltaX < 0 && newLeft < -mMaxScrollOffset)) {
            over = true;
            newLeft = Math.min(newLeft, 0);
            newLeft = Math.max(newLeft, -mMaxScrollOffset);
        }

        offsetChildrenLeftAndRight(newLeft - mScrollOffset);
        mScrollOffset = newLeft;
        return over;
    }

    private boolean ensureChildren() {
        int childCount = getChildCount();

        if (childCount != 2)
            return false;

        View childView = getChildAt(0);
        if (!(childView instanceof ViewGroup))
            return false;
        mMainView = (ViewGroup) childView;

        childView = getChildAt(1);
        if (!(childView instanceof ViewGroup))
            return false;
        mSideView = (ViewGroup) childView;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!ensureChildren())
            throw new RuntimeException("SwipeItemLayout的子视图不符合规定");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        MarginLayoutParams lp = null;
        int horizontalMargin, verticalMargin;
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        int verticalPadding = getPaddingTop() + getPaddingBottom();

        lp = (MarginLayoutParams) mMainView.getLayoutParams();
        horizontalMargin = lp.leftMargin + lp.rightMargin;
        verticalMargin = lp.topMargin + lp.bottomMargin;
        measureChildWithMargins(mMainView,
                widthMeasureSpec, horizontalMargin + horizontalPadding,
                heightMeasureSpec, verticalMargin + verticalPadding);

        if (widthMode == MeasureSpec.AT_MOST)
            widthSize = Math.min(widthSize, mMainView.getMeasuredWidth() + horizontalMargin + horizontalPadding);
        else if (widthMode == MeasureSpec.UNSPECIFIED)
            widthSize = mMainView.getMeasuredWidth() + horizontalMargin + horizontalPadding;

        if (heightMode == MeasureSpec.AT_MOST)
            heightSize = Math.min(heightSize, mMainView.getMeasuredHeight() + verticalMargin + verticalPadding);
        else if (heightMode == MeasureSpec.UNSPECIFIED)
            heightSize = mMainView.getMeasuredHeight() + verticalMargin + verticalPadding;

        setMeasuredDimension(widthSize, heightSize);

        //side layout大小为自身实际大小
        lp = (MarginLayoutParams) mSideView.getLayoutParams();
        verticalMargin = lp.topMargin + lp.bottomMargin;
        mSideView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - verticalMargin - verticalPadding, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!ensureChildren())
            throw new RuntimeException("SwipeItemLayout的子视图不符合规定");

        mInLayout = true;

        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();

        MarginLayoutParams mainLp = (MarginLayoutParams) mMainView.getLayoutParams();
        MarginLayoutParams sideParams = (MarginLayoutParams) mSideView.getLayoutParams();

        int childLeft = pl + mainLp.leftMargin;
        int childTop = pt + mainLp.topMargin;
        int childRight = getWidth() - (pr + mainLp.rightMargin);
        int childBottom = getHeight() - (mainLp.bottomMargin + pb);
        mMainView.layout(childLeft, childTop, childRight, childBottom);

        childLeft = childRight + sideParams.leftMargin;
        childTop = pt + sideParams.topMargin;
        childRight = childLeft + sideParams.leftMargin + sideParams.rightMargin + mSideView.getMeasuredWidth();
        childBottom = getHeight() - (sideParams.bottomMargin + pb);
        mSideView.layout(childLeft, childTop, childRight, childBottom);

        mMaxScrollOffset = mSideView.getWidth() + sideParams.leftMargin + sideParams.rightMargin;
        mScrollOffset = mScrollOffset < -mMaxScrollOffset / 2 ? -mMaxScrollOffset : 0;

        offsetChildrenLeftAndRight(mScrollOffset);
        mInLayout = false;
        mIsLaidOut = true;
    }

    void offsetChildrenLeftAndRight(int delta) {
        ViewCompat.offsetLeftAndRight(mMainView, delta);
        ViewCompat.offsetLeftAndRight(mSideView, delta);
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams ? p : new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mScrollOffset != 0 && mIsLaidOut) {
            offsetChildrenLeftAndRight(-mScrollOffset);
            mScrollOffset = 0;
        } else
            mScrollOffset = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mScrollOffset != 0 && mIsLaidOut) {
            offsetChildrenLeftAndRight(-mScrollOffset);
            mScrollOffset = 0;
        } else
            mScrollOffset = 0;
        removeCallbacks(mScrollRunnable);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        //click main view，但是它处于open状态，所以，不需要点击效果，直接拦截不调用click listener
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                View pointView = findTopChildUnder(this, x, y);
                if (pointView != null && pointView == mMainView && mScrollOffset != 0)
                    return true;
                break;
            }

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                View pointView = findTopChildUnder(this, x, y);
                if (pointView != null && pointView == mMainView && mTouchMode == Mode.TAP && mScrollOffset != 0)
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        //click main view，但是它处于open状态，所以，不需要点击效果，直接拦截不调用click listener
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                View pointView = findTopChildUnder(this, x, y);
                if (pointView != null && pointView == mMainView && mScrollOffset != 0)
                    return true;
                break;
            }

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                View pointView = findTopChildUnder(this, x, y);
                if (pointView != null && pointView == mMainView && mTouchMode == Mode.TAP && mScrollOffset != 0) {
                    close();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (getVisibility() != View.VISIBLE) {
            mScrollOffset = 0;
            invalidate();
        }
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    class ScrollRunnable implements Runnable {
        private static final int FLING_DURATION = 200;
        private Scroller mScroller;
        private boolean mAbort;
        private int mMinVelocity;
        private boolean mScrollToLeft;

        ScrollRunnable(Context context) {
            mScroller = new Scroller(context, sInterpolator);
            mAbort = false;
            mScrollToLeft = false;

            ViewConfiguration configuration = ViewConfiguration.get(context);
            mMinVelocity = configuration.getScaledMinimumFlingVelocity();
        }

        void startScroll(int startX, int endX) {
            if (startX != endX) {
                //Log.e("scroll - startX - endX", "" + startX + " " + endX);
                setTouchMode(Mode.FLING);
                mAbort = false;
                mScrollToLeft = endX < startX;
                mScroller.startScroll(startX, 0, endX - startX, 0, 400);
                ViewCompat.postOnAnimation(SwipeItemLayout.this, this);
            }
        }

        void startFling(int startX, int xVel) {
            //Log.e("fling - startX", "" + startX);

            if (xVel > mMinVelocity && startX != 0) {
                startScroll(startX, 0);
                return;
            }

            if (xVel < -mMinVelocity && startX != -mMaxScrollOffset) {
                startScroll(startX, -mMaxScrollOffset);
                return;
            }

            startScroll(startX, startX > -mMaxScrollOffset / 2 ? 0 : -mMaxScrollOffset);
        }

        void abort() {
            if (!mAbort) {
                mAbort = true;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    removeCallbacks(this);
                }
            }
        }

        //是否正在滑动需要另外判断
        boolean isScrollToLeft() {
            return mScrollToLeft;
        }

        @Override
        public void run() {
            //Log.e("abort", Boolean.toString(mAbort));
            if (!mAbort) {
                boolean more = mScroller.computeScrollOffset();
                int curX = mScroller.getCurrX();
                //Log.e("curX", "" + curX);

                boolean atEdge = trackMotionScroll(curX - mScrollOffset);
                if (more && !atEdge) {
                    ViewCompat.postOnAnimation(SwipeItemLayout.this, this);
                    return;
                }

                if (atEdge) {
                    removeCallbacks(this);
                    if (!mScroller.isFinished())
                        mScroller.abortAnimation();
                    setTouchMode(Mode.RESET);
                }

                if (!more) {
                    setTouchMode(Mode.RESET);
                    //绝对不会出现这种意外的！！！可以注释掉
                    if (mScrollOffset != 0) {
                        if (Math.abs(mScrollOffset) > mMaxScrollOffset / 2)
                            mScrollOffset = -mMaxScrollOffset;
                        else
                            mScrollOffset = 0;
                        ViewCompat.postOnAnimation(SwipeItemLayout.this, this);
                    }
                }
            }
        }
    }

    public static class OnSwipeItemTouchListener implements RecyclerView.OnItemTouchListener {
        private SwipeItemLayout mCaptureItem;
        private float mLastMotionX;
        private float mLastMotionY;
        private VelocityTracker mVelocityTracker;

        private int mActivePointerId;

        private int mTouchSlop;
        private int mMaximumVelocity;

        private boolean mDealByParent;
        private boolean mIsProbeParent;

        public OnSwipeItemTouchListener(Context context) {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            mTouchSlop = configuration.getScaledTouchSlop();
            mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
            mActivePointerId = -1;
            mDealByParent = false;
            mIsProbeParent = false;
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent ev) {
            if (mIsProbeParent)
                return false;

            boolean intercept = false;
            final int action = ev.getActionMasked();

            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mActivePointerId = ev.getPointerId(0);
                    final float x = ev.getX();
                    final float y = ev.getY();
                    mLastMotionX = x;
                    mLastMotionY = y;

                    boolean pointOther = false;
                    SwipeItemLayout pointItem = null;
                    //首先知道ev针对的是哪个item
                    View pointView = findTopChildUnder(rv, (int) x, (int) y);
                    if (pointView == null || !(pointView instanceof SwipeItemLayout)) {
                        //可能是head view或bottom view
                        pointOther = true;
                    } else
                        pointItem = (SwipeItemLayout) pointView;

                    //此时的pointOther=true，意味着点击的view为空或者点击的不是item
                    //还没有把点击的是item但是不是capture item给过滤出来
                    if (!pointOther && (mCaptureItem == null || mCaptureItem != pointItem))
                        pointOther = true;

                    //点击的是capture item
                    if (!pointOther) {
                        Mode touchMode = mCaptureItem.getTouchMode();

                        //如果它在fling，就转为drag
                        //需要拦截，并且requestDisallowInterceptTouchEvent
                        boolean disallowIntercept = false;
                        if (touchMode == Mode.FLING) {
                            mCaptureItem.setTouchMode(Mode.DRAG);
                            disallowIntercept = true;
                            intercept = true;
                        } else {//如果是expand的，就不允许parent拦截
                            mCaptureItem.setTouchMode(Mode.TAP);
                            if (mCaptureItem.isOpen())
                                disallowIntercept = true;
                        }

                        if (disallowIntercept) {
                            final ViewParent parent = rv.getParent();
                            if (parent != null)
                                parent.requestDisallowInterceptTouchEvent(true);
                        }
                    } else {//capture item为null或者与point item不一样
                        //直接将其close掉
                        if (mCaptureItem != null && mCaptureItem.isOpen()) {
                            mCaptureItem.close();
                            mCaptureItem = null;
                            intercept = true;
                        }

                        if (pointItem != null) {
                            mCaptureItem = pointItem;
                            mCaptureItem.setTouchMode(Mode.TAP);
                        } else
                            mCaptureItem = null;
                    }

                    //如果parent处于fling状态，此时，parent就会转为drag。此时，应该将后续move都交给parent处理
                    mIsProbeParent = true;
                    mDealByParent = rv.onInterceptTouchEvent(ev);
                    mIsProbeParent = false;
                    if (mDealByParent)
                        intercept = false;
                    break;
                }

                case MotionEvent.ACTION_POINTER_DOWN: {
                    final int actionIndex = ev.getActionIndex();
                    mActivePointerId = ev.getPointerId(actionIndex);

                    mLastMotionX = ev.getX(actionIndex);
                    mLastMotionY = ev.getY(actionIndex);
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    final int actionIndex = ev.getActionIndex();
                    final int pointerId = ev.getPointerId(actionIndex);
                    if (pointerId == mActivePointerId) {
                        final int newIndex = actionIndex == 0 ? 1 : 0;
                        mActivePointerId = ev.getPointerId(newIndex);

                        mLastMotionX = ev.getX(newIndex);
                        mLastMotionY = ev.getY(newIndex);
                    }
                    break;
                }

                //down时，已经将capture item定下来了。所以，后面可以安心考虑event处理
                case MotionEvent.ACTION_MOVE: {
                    final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1)
                        break;

                    //在down时，就被认定为parent的drag，所以，直接交给parent处理即可
                    if (mDealByParent) {
                        if (mCaptureItem != null && mCaptureItem.isOpen())
                            mCaptureItem.close();
                        return false;
                    }

                    final int x = (int) (ev.getX(activePointerIndex) + .5f);
                    final int y = (int) ((int) ev.getY(activePointerIndex) + .5f);

                    int deltaX = (int) (x - mLastMotionX);
                    int deltaY = (int) (y - mLastMotionY);
                    final int xDiff = Math.abs(deltaX);
                    final int yDiff = Math.abs(deltaY);

                    if (mCaptureItem != null && !mDealByParent) {
                        Mode touchMode = mCaptureItem.getTouchMode();

                        if (touchMode == Mode.TAP) {
                            //如果capture item是open的，下拉有两种处理方式：
                            //  1、下拉后，直接close item
                            //  2、只要是open的，就拦截所有它的消息，这样如果点击open的，就只能滑动该capture item
                            //网易邮箱，在open的情况下，下拉直接close
                            //QQ，在open的情况下，下拉也是close。但是，做的不够好，没有达到该效果。
                            if (xDiff > mTouchSlop && xDiff > yDiff) {
                                mCaptureItem.setTouchMode(Mode.DRAG);
                                final ViewParent parent = rv.getParent();
                                parent.requestDisallowInterceptTouchEvent(true);

                                deltaX = deltaX > 0 ? deltaX - mTouchSlop : deltaX + mTouchSlop;
                            } else {// if(yDiff>mTouchSlop){
                                mIsProbeParent = true;
                                boolean isParentConsume = rv.onInterceptTouchEvent(ev);
                                mIsProbeParent = false;
                                if (isParentConsume) {
                                    //表明不是水平滑动，即不判定为SwipeItemLayout的滑动
                                    //但是，可能是下拉刷新SwipeRefreshLayout或者RecyclerView的滑动
                                    //一般的下拉判定，都是yDiff>mTouchSlop，所以，此处这么写不会出问题
                                    //这里这么做以后，如果判定为下拉，就直接close
                                    mDealByParent = true;
                                    mCaptureItem.close();
                                }
                            }
                        }

                        touchMode = mCaptureItem.getTouchMode();
                        if (touchMode == Mode.DRAG) {
                            intercept = true;
                            mLastMotionX = x;
                            mLastMotionY = y;

                            //对capture item进行拖拽
                            mCaptureItem.trackMotionScroll(deltaX);
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_UP:
                    if (mCaptureItem != null) {
                        Mode touchMode = mCaptureItem.getTouchMode();
                        if (touchMode == Mode.DRAG) {
                            final VelocityTracker velocityTracker = mVelocityTracker;
                            velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                            int xVel = (int) velocityTracker.getXVelocity(mActivePointerId);
                            mCaptureItem.fling(xVel);

                            intercept = true;
                        }
                    }
                    cancel();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    if (mCaptureItem != null)
                        mCaptureItem.revise();
                    cancel();
                    break;
            }

            return intercept;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent ev) {
            final int action = ev.getActionMasked();
            final int actionIndex = ev.getActionIndex();

            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);

            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    mActivePointerId = ev.getPointerId(actionIndex);

                    mLastMotionX = ev.getX(actionIndex);
                    mLastMotionY = ev.getY(actionIndex);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    final int pointerId = ev.getPointerId(actionIndex);
                    if (pointerId == mActivePointerId) {
                        final int newIndex = actionIndex == 0 ? 1 : 0;
                        mActivePointerId = ev.getPointerId(newIndex);

                        mLastMotionX = ev.getX(newIndex);
                        mLastMotionY = ev.getY(newIndex);
                    }
                    break;

                //down时，已经将capture item定下来了。所以，后面可以安心考虑event处理
                case MotionEvent.ACTION_MOVE: {
                    final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1)
                        break;

                    final float x = ev.getX(activePointerIndex);
                    final float y = (int) ev.getY(activePointerIndex);

                    int deltaX = (int) (x - mLastMotionX);

                    if (mCaptureItem != null && mCaptureItem.getTouchMode() == Mode.DRAG) {
                        mLastMotionX = x;
                        mLastMotionY = y;

                        //对capture item进行拖拽
                        mCaptureItem.trackMotionScroll(deltaX);
                    }
                    break;
                }

                case MotionEvent.ACTION_UP:
                    if (mCaptureItem != null) {
                        Mode touchMode = mCaptureItem.getTouchMode();
                        if (touchMode == Mode.DRAG) {
                            final VelocityTracker velocityTracker = mVelocityTracker;
                            velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                            int xVel = (int) velocityTracker.getXVelocity(mActivePointerId);
                            mCaptureItem.fling(xVel);
                        }
                    }
                    cancel();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    if (mCaptureItem != null)
                        mCaptureItem.revise();

                    cancel();
                    break;

            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        void cancel() {
            mDealByParent = false;
            mActivePointerId = -1;
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

    }

    static View findTopChildUnder(ViewGroup parent, int x, int y) {
        final int childCount = parent.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight()
                    && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    public static void closeAllItems(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            if (child instanceof SwipeItemLayout) {
                SwipeItemLayout swipeItemLayout = (SwipeItemLayout) child;
                if (swipeItemLayout.isOpen())
                    swipeItemLayout.close();
            }
        }
    }

}

