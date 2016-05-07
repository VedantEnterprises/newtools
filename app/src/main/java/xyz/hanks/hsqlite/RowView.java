package xyz.hanks.hsqlite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class RowView extends View {
    private int mExampleColor = Color.BLUE; // TODO: use a default from R.color...
    private float mExampleDimension = 14; // TODO: use a default from R.dimen...

    private TextPaint mTextPaint;
    private float mTextHeight;
    private String[] mTextAttay;
    private float[] mTextLengthArray;

    public RowView(Context context) {
        super(context);
        init(null, 0);
    }

    public RowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension( 1080 ,40);
    }

    public void setTextArray(String[] textArray) {
        mTextAttay = textArray;
        invalidate();
    }

    public void setTextLengthArray(float[] textLengthArray) {
        mTextLengthArray = textLengthArray;

    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        if (mTextAttay == null || mTextAttay.length <= 0 || mTextLengthArray == null || mTextLengthArray.length <= 0 ) {
            return;
        }
        // Draw the text.
        float startX = paddingLeft;
        float startY = getHeight() - mTextHeight;
        for (int i = 0; i < mTextAttay.length && i < mTextLengthArray.length; i++) {
            String s = mTextAttay[i];
            if(s ==null) continue;
            int count = (int) (mTextLengthArray[i] / dp2px(10));
            s = s.substring(0, count<=s.length()? count:s.length());
            canvas.drawText(s,
                    startX,
                    startY,
                    mTextPaint);
            startX += mTextLengthArray[i];
        }
    }

    public int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
