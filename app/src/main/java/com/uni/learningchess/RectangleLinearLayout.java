package com.uni.learningchess;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RectangleLinearLayout extends LinearLayout {

    public RectangleLinearLayout(Context context) {
        super(context);
    }

    public RectangleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}

