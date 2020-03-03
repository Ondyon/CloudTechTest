/**
 * custom text control extends android.view.View
 * uses outText on canvas and font size calculations
 */
package com.rolay.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;


public class CustomTextView extends View {

	private static final float INTERLINE_HEIGHT_RATIO = 0.9f;
	private static final int TEXT_COLOR = 0xFF0066CC;
	private static final int TEXT_SIZE = 210;

	private Paint textPaint;
	private String internalValue;
	private boolean mBold;
	private boolean mItalic;
	private int mTextSize = TEXT_SIZE;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private List<TextLine> lines;
	private int mTextColor = TEXT_COLOR;
	private boolean mHorAlighCenter = true;

	class TextLine {
		int start;
		int count;
		int width;

		TextLine(int start, int count, float w) {
			this.start = start;
			this.count = count;
			this.width = (int) w;
		}
	}

	public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CustomTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		textPaint = new Paint();
		textPaint.setColor(mTextColor);
	}


	private void recalculateText() {
		textPaint.setTextSize(mTextSize);
		int style = mItalic && mBold ? Typeface.BOLD_ITALIC : (mItalic ? Typeface.ITALIC : (mBold ? Typeface.BOLD : Typeface.NORMAL));
		textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, style));

		if(mMeasuredWidth == 0 || internalValue == null || internalValue.length() == 0)
			return;

		int start = 0;
		lines = new ArrayList<>();
		do {
			float[] calculatedWidth = new float[1];
			int count = textPaint.breakText(internalValue.toCharArray(), start, internalValue.length() - start, mMeasuredWidth, calculatedWidth);
			if (count == 0) {
				return;	// safety nut. It may happen if something is not measured or set yet
			}
			lines.add(new TextLine(start, count, calculatedWidth[0]));
			start += count;

		} while (start < internalValue.length());

		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mMeasuredWidth = getMeasuredWidth();
		mMeasuredHeight = getMeasuredHeight();
		recalculateText();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int vOffset = mTextSize;
		for(TextLine oneLine : lines) {
			int x = mHorAlighCenter ? (mMeasuredWidth/2 - oneLine.width/2) : 0;
			canvas.drawText(internalValue.toCharArray(), oneLine.start, oneLine.count, x, vOffset, textPaint);
			if(vOffset >= mMeasuredHeight)
				break;
			vOffset += mTextSize * INTERLINE_HEIGHT_RATIO;
		}
	}

	/**
	 * set text string value to be displayed
	 * @param text string value
	 */
	public void setText(String text) {
		internalValue = text;
		recalculateText();
		invalidate();
	}

	/**
	 * set bold text property
	 * @param bold true for bold, false for normal
	 */
	public void setBold(boolean bold) {
		mBold = bold;
		recalculateText();
	}

	/**
	 * set italic text property
	 * @param italic true for italic, false for normal
	 */
	public void setItalic(boolean italic) {
		mItalic = italic;
		recalculateText();
	}

	/**
	 * set text size in pix. Use dp2pix conversion if needed
	 * @param textSize size in pix
	 */
	public void setTextSize(int textSize) {
		mTextSize = textSize;
		recalculateText();
	}

	/**
	 * set horizontal text align
	 * @param align center if true, left if false
	 */
	public void setAlignCenter(boolean align) {
		mHorAlighCenter = align;
		recalculateText();
	}

    /**
     * set text color
     * @param color integer color value
     */
	public void setTextColor(@ColorInt int color) {
		mTextColor = color;
		textPaint = new Paint();
		textPaint.setColor(mTextColor);
		invalidate();
	}

}
