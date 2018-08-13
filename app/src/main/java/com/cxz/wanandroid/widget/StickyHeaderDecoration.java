package com.cxz.wanandroid.widget;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxz on 2018/8/8.
 */
public abstract class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    protected String TAG = "CXZ";
    private Paint mHeaderTxtPaint;
    private Paint mHeaderContentPaint;

    protected int headerHeight = 136;//头部高度
    private int textPaddingLeft = 50;//头部文字左边距
    private int textSize = 50;
    private int textColor = Color.BLACK;
    private int headerContentColor = 0xffeeeeee;
    private final float txtYAxis;
    private RecyclerView mRecyclerView;


    public StickyHeaderDecoration() {
        mHeaderTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTxtPaint.setColor(textColor);
        mHeaderTxtPaint.setTextSize(textSize);
        mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);

        mHeaderContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderContentPaint.setColor(headerContentColor);
        Paint.FontMetrics fontMetrics = mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        txtYAxis = total / 2 - fontMetrics.descent;
    }

    private boolean isInitHeight = false;

    /**
     * 先调用getItemOffsets再调用onDraw
     */
    @Override
    public void getItemOffsets(Rect outRect, View itemView, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, itemView, parent, state);
        if (mRecyclerView == null) {
            mRecyclerView = parent;
        }

        if (headerDrawEvent != null && !isInitHeight) {
            View headerView = headerDrawEvent.getHeaderView(0);
            headerView
                    .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            headerHeight = headerView.getMeasuredHeight();
            isInitHeight = true;
        }

        /*我们为每个不同头部名称的第一个item设置头部高度*/
        int pos = parent.getChildAdapterPosition(itemView); //获取当前itemView的位置
        String curHeaderName = getHeaderName(pos);         //根据pos获取要悬浮的头部名

        if (TextUtils.isEmpty(curHeaderName)) {
            return;
        }
        if (pos == 0 || !curHeaderName.equals(getHeaderName(pos - 1))) {//如果当前位置为0，或者与上一个item头部名不同的，都腾出头部空间
            outRect.top = headerHeight;                                 //设置itemView PaddingTop的距离
        }
    }

    public abstract String getHeaderName(int pos);

    private SparseArray<Integer> stickyHeaderPosArray = new SparseArray<>();//记录每个头部和悬浮头部的坐标信息【用于点击事件】
    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView;
        }
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(recyclerView.getContext(), gestureListener);
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        stickyHeaderPosArray.clear();

        int childCount = recyclerView.getChildCount();//获取屏幕上可见的item数量
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() - recyclerView.getPaddingRight();

        String firstHeaderName = null;
        int firstPos = 0;
        int translateTop = 0;//绘制悬浮头部的偏移量
        /*for循环里面绘制每个分组的头部*/
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            int pos = recyclerView.getChildAdapterPosition(childView); //获取当前view在Adapter里的pos
            String curHeaderName = getHeaderName(pos);                 //根据pos获取要悬浮的头部名
            if (i == 0) {
                firstHeaderName = curHeaderName;
                firstPos = pos;
            }
            if (TextUtils.isEmpty(curHeaderName))
                continue;//如果headerName为空，跳过此次循环

            int viewTop = childView.getTop() + recyclerView.getPaddingTop();
            if (pos == 0 || !curHeaderName.equals(getHeaderName(pos - 1))) {//如果当前位置为0，或者与上一个item头部名不同的，都腾出头部空间
                if (headerDrawEvent != null) {
                    View headerView;
                    if (headViewMap.get(pos) == null) {
                        headerView = headerDrawEvent.getHeaderView(pos);
                        headerView
                                .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        headerView.setDrawingCacheEnabled(true);
                        headerView.layout(0, 0, right, headerHeight);//布局layout
                        headViewMap.put(pos, headerView);
                        canvas.drawBitmap(headerView.getDrawingCache(), left, viewTop - headerHeight, null);

                    } else {
                        headerView = headViewMap.get(pos);
                        canvas.drawBitmap(headerView.getDrawingCache(), left, viewTop - headerHeight, null);
                    }
                } else {
                    canvas.drawRect(left, viewTop - headerHeight, right, viewTop, mHeaderContentPaint);
                    canvas.drawText(curHeaderName, left + textPaddingLeft, viewTop - headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
                }
                if (headerHeight < viewTop && viewTop <= 2 * headerHeight) { //此判断是刚好2个头部碰撞，悬浮头部就要偏移
                    translateTop = viewTop - 2 * headerHeight;
                }
                stickyHeaderPosArray.put(pos, viewTop);//将头部信息放进array
                // Log.i(TAG, "绘制各个头部" + pos);
            }
        }
        if (firstHeaderName == null)
            return;

        canvas.save();
        canvas.translate(0, translateTop);
        if (headerDrawEvent != null) {//inflater
            View headerView;
            if (headViewMap.get(firstPos) == null) {
                headerView = headerDrawEvent.getHeaderView(firstPos);
                headerView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                headerView.setDrawingCacheEnabled(true);
                headerView.layout(0, 0, right, headerHeight);//布局layout
                headViewMap.put(firstPos, headerView);
                canvas.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            } else {
                headerView = headViewMap.get(firstPos);
                canvas.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            }
        } else {
            /*绘制悬浮的头部*/
            canvas.drawRect(left, 0, right, headerHeight, mHeaderContentPaint);
            canvas.drawText(firstHeaderName, left + textPaddingLeft, headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
//           canvas.drawLine(0, headerHeight / 2, right, headerHeight / 2, mHeaderTxtPaint);//画条线看看文字居中不
        }
        canvas.restore();
        // Log.i(TAG, "绘制悬浮头部");
    }

    private Map<Integer, View> headViewMap = new HashMap<>();

    public interface OnHeaderClickListener {
        void headerClick(int pos);
    }

    private OnHeaderClickListener headerClickEvent;

    public void setOnHeaderClickListener(OnHeaderClickListener headerClickListener) {
        this.headerClickEvent = headerClickListener;
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < stickyHeaderPosArray.size(); i++) {
                int value = stickyHeaderPosArray.valueAt(i);
                float y = e.getY();
                if (value - headerHeight <= y && y <= value) {//如果点击到分组头
                    if (headerClickEvent != null) {
                        headerClickEvent.headerClick(stickyHeaderPosArray.keyAt(i));
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    private OnDecorationHeadDraw headerDrawEvent;

    public interface OnDecorationHeadDraw {
        View getHeaderView(int pos);
    }

    /**
     * 只是用来绘制，不能做其他处理/点击事件等
     */
    public void setOnDecorationHeadDraw(OnDecorationHeadDraw decorationHeadDraw) {
        this.headerDrawEvent = decorationHeadDraw;
    }

    public void loadImage(final String url, final int pos, ImageView imageView) {

        if (getImg(url) != null) {
            // Log.i(TAG, "Glide 加载完图片" + pos);

            imageView.setImageDrawable(getImg(url));

        } else {
            Glide.with(mRecyclerView.getContext()).load(url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    // Log.i(TAG, "Glide回调" + pos);
                    headViewMap.remove(pos);//删除，重新更新
                    imgDrawableMap.put(url, resource);
                    mRecyclerView.postInvalidate();
                }
            });
        }
    }

    private Map<String, Drawable> imgDrawableMap = new HashMap<>();

    private Drawable getImg(String url) {
        return imgDrawableMap.get(url);
    }

    public void onDestroy() {
        headViewMap.clear();
        imgDrawableMap.clear();
        stickyHeaderPosArray.clear();
        mRecyclerView = null;
        setOnHeaderClickListener(null);
        setOnDecorationHeadDraw(null);
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public void setTextPaddingLeft(int textPaddingLeft) {
        this.textPaddingLeft = textPaddingLeft;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.mHeaderTxtPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.mHeaderTxtPaint.setColor(textColor);
    }

    public void setHeaderContentColor(int headerContentColor) {
        this.headerContentColor = headerContentColor;
        this.mHeaderContentPaint.setColor(headerContentColor);
    }
}
