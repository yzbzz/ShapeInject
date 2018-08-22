package com.shape;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yzbzz on 2017/5/2.
 */

public class ShapeInject {

    public static final int TYPE_NULL = 0x00;
    public static final int TYPE_OVAL = 0x01;
    public static final int TYPE_ROUND = 0x02;
    public static final int TYPE_ROUND_RECT = 0x04;
    public static final int TYPE_SEGMENT = 0x08;

    public static final int DIRECTION_LEFT = 0x0010;
    public static final int DIRECTION_RIGHT = 0x0020;
    public static final int DIRECTION_TOP = 0x0040;
    public static final int DIRECTION_BOTTOM = 0x0080;

    private View mView;
    private TextView mTextView;

    private Drawable mBackground;
    private ColorDrawable mColorDrawable;

    private ColorStateList mColorStateList;
    private GradientDrawable mNormalBackground;
    private GradientDrawable mPressedBackground;
    private GradientDrawable mDisableBackground;

    //animation duration
    private int mDuration = 0;

    //radius
    private float mRadius = 0;
    private float[] mRadii = new float[8];

    private int mWidth = -1;
    private int mHeight = -1;

    @IShapeType
    private int mShapeType = TYPE_NULL;

    private int mGradientShapeType = GradientDrawable.RECTANGLE;

    @IShapeDirection
    private int mDirection = DIRECTION_LEFT;

    //stroke
    private float mStrokeDashWidth = 0;
    private float mStrokeDashGap = 0;

    private int mNormalStrokeWidth = 0;
    private int mPressedStrokeWidth = 0;
    private int mDisableStrokeWidth = 0;

    private int mNormalStrokeColor = 0;
    private int mPressedStrokeColor = 0;
    private int mDisableStrokeColor = 0;

    // text color
    private int mNormalTextColor = Color.BLACK;
    private int mPressedTextColor = Color.BLACK;
    private int mDisableTextColor = Color.BLACK;

    //background color
    @ColorInt
    private int mNormalBackgroundColor = Color.TRANSPARENT;
    private int mPressedBackgroundColor = Color.TRANSPARENT;
    private int mDisableBackgroundColor = Color.TRANSPARENT;

    private int[][] mStates;
    private StateListDrawable mStateBackground;

    private boolean mIsUserSystemBackground;

    private ShapeInject(View view, boolean isUserSystemBackground) {
        mView = view;
        mBackground = mView.getBackground();
        mIsUserSystemBackground = isUserSystemBackground;
        initDrawable();
    }

    public static ShapeInject inject(View view) {
        return inject(view, false);
    }

    public static ShapeInject inject(View view, boolean isUserSystemBackground) {
        return new ShapeInject(view, isUserSystemBackground);
    }

    private void initDrawable() {
        boolean isInitStateListDrawable = ((mBackground != null) && mIsUserSystemBackground);
        if (!isInitStateListDrawable) {
            mNormalBackground = new GradientDrawable();
            mPressedBackground = new GradientDrawable();
            mDisableBackground = new GradientDrawable();

            //pressed, focused, normal, Disable
            mStates = new int[4][];
            mStates[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
            mStates[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
            mStates[3] = new int[]{-android.R.attr.state_enabled};
            mStates[2] = new int[]{android.R.attr.state_enabled};

            mStateBackground = new StateListDrawable();
            mStateBackground.addState(mStates[0], mPressedBackground);
            mStateBackground.addState(mStates[1], mPressedBackground);
            mStateBackground.addState(mStates[3], mDisableBackground);
            mStateBackground.addState(mStates[2], mNormalBackground);
        }
    }

    public void parseAttributeSet(AttributeSet attrs) {
        initDrawable();
        if (null != mStateBackground) {
            TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.ShapeInject);

            //set animation duration
            mDuration = a.getInteger(R.styleable.ShapeInject_animationDuration, mDuration);
            mStateBackground.setEnterFadeDuration(mDuration);
            mStateBackground.setExitFadeDuration(mDuration);

            //set background color
            if (mBackground instanceof ColorDrawable) {
                mColorDrawable = (ColorDrawable) mBackground;
            }

            int defValue = Color.TRANSPARENT;
            if (null != mColorDrawable) {
                defValue = mColorDrawable.getColor();
            }

            // backgroundColor
            mNormalBackgroundColor = a.getColor(R.styleable.ShapeInject_normalBackgroundColor, defValue);
            mPressedBackgroundColor = a.getColor(R.styleable.ShapeInject_pressedBackgroundColor, mNormalBackgroundColor);
            mDisableBackgroundColor = a.getColor(R.styleable.ShapeInject_disableBackgroundColor, mNormalBackgroundColor);

            // stroke
            mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.ShapeInject_strokeDashWidth, 0);
            mStrokeDashGap = a.getDimensionPixelSize(R.styleable.ShapeInject_strokeDashGap, 0);
            mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeInject_normalStrokeWidth, 0);
            mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeInject_pressedStrokeWidth, mNormalStrokeWidth);
            mDisableStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeInject_disableStrokeWidth, mNormalStrokeWidth);
            mNormalStrokeColor = a.getColor(R.styleable.ShapeInject_normalStrokeColor, 0);
            mPressedStrokeColor = a.getColor(R.styleable.ShapeInject_pressedStrokeColor, mNormalStrokeColor);
            mDisableStrokeColor = a.getColor(R.styleable.ShapeInject_disableStrokeColor, mNormalStrokeColor);

            // radius
            mRadius = a.getDimensionPixelSize(R.styleable.ShapeInject_shapeRadius, 0);

            mShapeType = a.getInt(R.styleable.ShapeInject_shapeType, TYPE_NULL);
            mDirection = a.getInt(R.styleable.ShapeInject_direction, DIRECTION_LEFT);

            if (mView instanceof TextView) {
                mTextView = (TextView) mView;
                mColorStateList = mTextView.getTextColors();

                int currentTextColor = mTextView.getCurrentTextColor();
                int mDefaultNormalTextColor = mColorStateList.getColorForState(mStates[2], currentTextColor);
                int mDefaultPressedTextColor = mColorStateList.getColorForState(mStates[0], currentTextColor);
                int mDefaultDisableTextColor = mColorStateList.getColorForState(mStates[3], currentTextColor);

                mNormalTextColor = a.getColor(R.styleable.ShapeInject_normalTextColor, mDefaultNormalTextColor);
                mPressedTextColor = a.getColor(R.styleable.ShapeInject_pressedTextColor, mDefaultPressedTextColor);
                mDisableTextColor = a.getColor(R.styleable.ShapeInject_disableTextColor, mDefaultDisableTextColor);
            }
            a.recycle();
        }
    }

    public ShapeInject setTextColor(@ColorInt int textColor) {
        setTextColor(textColor, textColor, textColor);
        return this;
    }

    public ShapeInject setTextColor(@ColorInt int pressedTextColor, @ColorInt int disableTextColor, @ColorInt int normalTextColor) {
        mPressedTextColor = pressedTextColor;
        mDisableTextColor = disableTextColor;
        mNormalTextColor = normalTextColor;
        return this;
    }

    public ShapeInject setTextColor(@ColorInt int pressedTextColor, @ColorInt int disableTextColor, @ColorInt int normalTextColor, TextView textView) {
        int[] colors = new int[]{pressedTextColor, pressedTextColor, normalTextColor, disableTextColor};
        mColorStateList = new ColorStateList(mStates, colors);
        textView.setTextColor(mColorStateList);
        return this;
    }

    public ShapeInject setBackgroundColor(@ColorInt int color) {
        setBackgroundColor(color, color, color);
        return this;
    }

    public ShapeInject setBackgroundColor(@ColorInt int pressed, @ColorInt int disable, @ColorInt int normal) {
        mPressedBackgroundColor = pressed;
        mDisableBackgroundColor = disable;
        mNormalBackgroundColor = normal;
        return this;
    }

    public ShapeInject setStroke(@IntRange(from = 0) int width, @ColorInt int color) {
        mPressedStrokeWidth = mDisableStrokeWidth = mNormalStrokeWidth = width;
        mPressedStrokeColor = mDisableStrokeColor = mNormalStrokeColor = color;
        return this;
    }

    public ShapeInject setStroke(@IntRange(from = 0) int width, @ColorInt int pressedColor, int normalColor) {
        mPressedStrokeWidth = mDisableStrokeWidth = mNormalStrokeWidth = width;
        mPressedStrokeColor = mDisableStrokeColor = pressedColor;
        mNormalStrokeColor = normalColor;
        return this;
    }

    public ShapeInject setStroke(@IntRange(from = 0) int width, @ColorInt int color, float dashWidth, float dashGap) {
        setStroke(width, color);
        mStrokeDashWidth = dashWidth;
        mStrokeDashGap = dashGap;
        return this;
    }

    public ShapeInject setStroke(@IntRange(from = 0) int width, @ColorInt int pressedColor, int normalColor, float dashWidth, float dashGap) {
        setStroke(width, pressedColor, normalColor);
        mStrokeDashWidth = dashWidth;
        mStrokeDashGap = dashGap;
        return this;
    }

    public ShapeInject setRadius(@FloatRange(from = 0) float radius) {
        mRadius = radius;
        return this;
    }

    public ShapeInject setRadii(float[] radii) {
        mRadii = radii;
        return this;
    }

    public ShapeInject setShapeType(@IShapeType int shapeType) {
        mShapeType = shapeType;
        return this;
    }

    public ShapeInject setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    private void setShape() {
        if (mShapeType == TYPE_OVAL) {
            mGradientShapeType = GradientDrawable.OVAL;
        } else if (mShapeType == TYPE_ROUND) {
            mGradientShapeType = GradientDrawable.OVAL;
            int height = mView.getMeasuredHeight();
            int width = mView.getMeasuredWidth();
            int size = Math.max(height, width);
            setSize(size, size);
        } else if (mShapeType == TYPE_ROUND_RECT) {
            setRadius(mView.getMeasuredHeight() / 2);
        } else if (mShapeType == TYPE_SEGMENT) {
            setSegmented();
        }
    }

    private void setSegmented() {
        float radius = mView.getMeasuredHeight();
        float[] radii;
        if (mDirection == DIRECTION_TOP) {
            radii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        } else if (mDirection == DIRECTION_BOTTOM) {
            radii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        } else if (mDirection == DIRECTION_RIGHT) {
            radii = new float[]{0, 0, radius, radius, radius, radius, 0, 0};
        } else {
            radii = new float[]{radius, radius, 0, 0, 0, 0, radius, radius};
        }
        setRadii(radii);
    }

    public void background() {
        if (null != mTextView) {
            int[] colors = new int[]{mPressedTextColor, mPressedTextColor, mNormalTextColor, mDisableTextColor};
            mColorStateList = new ColorStateList(mStates, colors);
            mTextView.setTextColor(mColorStateList);
        }

        if (null != mView) {
            mView.post(new Runnable() {
                @Override
                public void run() {
                    setShape();
                    background(mPressedBackground, mPressedBackgroundColor, mPressedStrokeWidth, mPressedStrokeColor);
                    background(mDisableBackground, mDisableBackgroundColor, mDisableStrokeWidth, mDisableStrokeColor);
                    background(mNormalBackground, mNormalBackgroundColor, mNormalStrokeWidth, mNormalStrokeColor);
                    mView.setBackground(mStateBackground);
                }
            });
        }
    }

    public void background1() {
        if (null != mTextView) {
            int[] colors = new int[]{mPressedTextColor, mPressedTextColor, mNormalTextColor, mDisableTextColor};
            mColorStateList = new ColorStateList(mStates, colors);
            mTextView.setTextColor(mColorStateList);
        }

        if (null != mView) {
            mView.post(new Runnable() {
                @Override
                public void run() {
                    setShape();
                    background(mPressedBackground, mPressedBackgroundColor, mPressedStrokeWidth, mPressedStrokeColor);
                    background(mDisableBackground, mDisableBackgroundColor, mDisableStrokeWidth, mDisableStrokeColor);
                    background(mNormalBackground, mNormalBackgroundColor, mNormalStrokeWidth, mNormalStrokeColor);
                    mView.setBackground(mStateBackground);
                }
            });
        }
    }

    private void background(GradientDrawable gradientDrawable, int color, int strokeWidth, int strokeColor) {
        if (null != gradientDrawable) {
            gradientDrawable.setShape(mGradientShapeType);
            gradientDrawable.setColor(color);
            if (mRadius != 0) {
                gradientDrawable.setCornerRadius(mRadius);
            } else {
                gradientDrawable.setCornerRadii(mRadii);
            }
            gradientDrawable.setStroke(strokeWidth, strokeColor, mStrokeDashWidth, mStrokeDashGap);
            gradientDrawable.setSize(mWidth, mHeight);
        }
    }


    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        this.mDuration = duration;
        mStateBackground.setEnterFadeDuration(mDuration);
    }

    @IntDef({TYPE_OVAL, TYPE_ROUND, TYPE_ROUND_RECT, TYPE_SEGMENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IShapeType {
    }

    @IntDef({DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_TOP, DIRECTION_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IShapeDirection {
    }
}
