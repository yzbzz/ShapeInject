package com.shape;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class ShapeButton extends AppCompatButton {

    private ShapeInject mShapeInject;

    public ShapeButton(Context context) {
        this(context, null);
    }

    public ShapeButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public ShapeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        mShapeInject = ShapeInject.inject(this);
        mShapeInject.parseAttributeSet(attrs);
        mShapeInject.background();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
