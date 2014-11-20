package com.merge.phillipjones.mergehifi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class DragToShareView extends View {
    final public static int defaultAlpha = 80;
    final public static int hoverAlpha = 150;

    private String mStatusString = "Drag up to Share!";
    private int mViewColor = Color.RED; // TODO: use a default from R.color...
    private int mStatusStringColor = Color.WHITE;
    private float mViewDimension = 0; // TODO: use a default from R.dimen...
    private int mAlpha = defaultAlpha;

    private TextPaint mTextPaint;
    private Paint mHoverRegionPaint;

    private float mTextWidth;
    private float mTextHeight;

    public DragToShareView(Context context) {
        super(context);
        init(null, 0);
    }

    public DragToShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DragToShareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DragToShareView, defStyle, 0);

        mStatusString = a.getString(
                R.styleable.DragToShareView_statusString);

        mStatusStringColor = a.getColor(
                R.styleable.DragToShareView_statusStringColor,
                mStatusStringColor);

        mViewColor = a.getColor(
                R.styleable.DragToShareView_viewColor,
                mViewColor);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mViewDimension = a.getDimension(
                R.styleable.DragToShareView_viewDimension,
                mViewDimension);

        a.recycle();

        mHoverRegionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHoverRegionPaint.setStyle(Paint.Style.FILL);
        mHoverRegionPaint.setAlpha(mAlpha);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setShadowLayer(5.0f, 0.0f, 0.0f, Color.BLACK);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mViewDimension);
        mTextPaint.setColor(mStatusStringColor);
        mTextWidth = mTextPaint.measureText(mStatusString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;

        mHoverRegionPaint.setColor(mViewColor);
        mHoverRegionPaint.setAlpha(mAlpha);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        int cx = getWidth()/2;
        int cy = getHeight()/2;

        canvas.drawCircle(cx, cy, (float)contentHeight/2, mHoverRegionPaint);
        // Draw the text.
        canvas.drawText(mStatusString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getStatusString() {
        return mStatusString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setStatusString(String exampleString) {
        mStatusString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getViewColor() {
        return mViewColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setViewcolor(int exampleColor) {
        mViewColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getStatusStringColor() {
        return mStatusStringColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setStatusStringColor(int exampleColor) {
        mStatusStringColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getViewDimension() {
        return mViewDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setViewDimension(float exampleDimension) {
        mViewDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    public void setDisplayAlpha(int alpha) {
        mAlpha = alpha;
        invalidateTextPaintAndMeasurements();
    }
}
