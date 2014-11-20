package com.merge.hifi.merge;


import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class RecommendedUserView extends View {
    private static final int INVALID_POINTER_ID = -1;

    private String mNameString; // TODO: use a default from R.string...
    private int mNameColor = Color.RED; // TODO: use a default from R.color...
    private float mTextDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mProfilePicture;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public RecommendedUserView(Context context) {
        super(context);
        init(null, 0);
    }

    public RecommendedUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RecommendedUserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RecommendedUserView, defStyle, 0);

        mNameString = a.getString(
                R.styleable.RecommendedUserView_nameString);
        mNameColor = a.getColor(
                R.styleable.RecommendedUserView_nameColor,
                mNameColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mTextDimension = a.getDimension(
                R.styleable.RecommendedUserView_textDimension,
                mTextDimension);

        if (a.hasValue(R.styleable.RecommendedUserView_profilePicture)) {
            mProfilePicture = a.getDrawable(
                    R.styleable.RecommendedUserView_profilePicture);

            //Bitmap b =  ((BitmapDrawable)mProfilePicture).getBitmap();
            //Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            //int w = mProfilePicture.getIntrinsicWidth();
            //Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
            //mProfilePicture = new BitmapDrawable(getResources(), roundBitmap);
            mProfilePicture.setCallback(this);

        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        OnDragListener onDragListener = new recommendedUserDragEventListener();
        this.setOnDragListener(onDragListener);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mTextDimension);
        mTextPaint.setColor(mNameColor);
        mTextWidth = mTextPaint.measureText(mNameString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
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

        // Draw the text.
        canvas.drawText(mNameString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight-3),
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mProfilePicture != null) {
            mProfilePicture.setBounds(paddingLeft + 50, paddingTop+50,
                    paddingLeft + contentWidth - 50, paddingTop + contentHeight - 50);
            mProfilePicture.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getNameString() {
        return mNameString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param nameString The example string attribute value to use.
     */
    public void setNameString(String nameString) {
        mNameString = nameString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getNameColor() {
        return mNameColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param nameColor The example color attribute value to use.
     */
    public void setNameColor(int nameColor) {
        mNameColor = nameColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getTextDimension() {
        return mTextDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param textDimension The example dimension attribute value to use.
     */
    public void setTextDimension(float textDimension) {
        mTextDimension = textDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getProfilePicture() {
        return mProfilePicture;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param profilePicture The example drawable attribute value to use.
     */
    public void setProfilePicture(Drawable profilePicture) {
        mProfilePicture = profilePicture;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ClipData clipData = ClipData.newPlainText("nameToRecommend", this.getNameString());
                View.DragShadowBuilder dsb = new View.DragShadowBuilder(this);
                this.startDrag(clipData, dsb, this, 0);
                this.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }

    protected class recommendedUserDragEventListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return true;
        }
    }

    // http://stackoverflow.com/questions/16208365/create-a-circular-image-view-in-android
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth() / factor), (int)(bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}
