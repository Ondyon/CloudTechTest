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
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;


public class CustomTextView extends View {

	private static final float INTERLINE_HEIGHT_RATIO = 0.9f;
	private static final int TEXT_COLOR = 0xFF0066CC;
	private static final int TEXT_SIZE = 70;

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
	private float mScrollShift = 0;
	private int mScrollShiftLine;
	private int mScrollShiftPitch;

	private OnTouchListener onTouchListener = new OnTouchListener() {
		private int moves;
		private float currentYPos;
		private boolean tracking;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				tracking = true;
				currentYPos = event.getY();
				moves = 0;
			}
			if(event.getAction() == MotionEvent.ACTION_UP) {
				tracking = false;
				if(moves == 0) {
					CustomTextView.this.performClick();
				}
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				if(!tracking) return true;
				moves++;
				float delta = currentYPos - event.getY();
				currentYPos = event.getY();
				doScrollBy(delta);
			}


			return true;
		}

	};

	/**
	 * scroll text vertically by delat pixels
	 * and validate if it is in borders
	 * @param delta should be negative!
	 */
	public void doScrollBy(float delta) {
		if(mScrollShift - delta > 0) {
			mScrollShift = 0;
			return;
		}
		if(mScrollShift - delta < - (lines.size()+1) * mTextSize * INTERLINE_HEIGHT_RATIO + mMeasuredHeight) {
			mScrollShift = - (lines.size()+1) * mTextSize * INTERLINE_HEIGHT_RATIO + mMeasuredHeight;
			return;
		}
		mScrollShift -= delta;

		// smart scrollshift
		float lineHeight = mTextSize * INTERLINE_HEIGHT_RATIO;
 		mScrollShiftLine = -(int)(mScrollShift / lineHeight);
		mScrollShiftPitch = (int) (mScrollShift % lineHeight);

		invalidate();
	}

	/**
	 * scroll text to position in pixels
	 * @param i should be negative (above the screen)
	 */
	public void scrollTo(int i) {
		mScrollShift = i;
		doScrollBy(0);	//to adjust if out of borders and invalidate
	}

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

		setOnTouchListener(onTouchListener);
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

			//find '\n' on current line
			int newLinePos = internalValue.indexOf('\n', start);
			int newLineCount = newLinePos - start;

			// if found and it is not beyond border
			if(newLinePos != -1 && newLineCount < count) {
				if(newLineCount == 0) {
					// special case if '\n' just equals the border ( newLineCount + 1 = count)
					start += 1;
					continue;
				}
				//recalculate 'calculatedWidth' with new length from start to '\n'
				textPaint.breakText(internalValue.toCharArray(), start, newLineCount, mMeasuredWidth, calculatedWidth);
				lines.add(new TextLine(start, newLineCount, calculatedWidth[0]));
				start += newLineCount + 1;
			} else {
				lines.add(new TextLine(start, count, calculatedWidth[0]));
				start += count;
			}

		} while (start < internalValue.length());

		doScrollBy(0);	//also recalculate scroll if it is outside
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

		// simplest version:
		//int vOffset = mScrollShift + mTextSize;

		int vOffset = mScrollShiftPitch + mTextSize;

		for(int i = mScrollShiftLine; i < lines.size(); i++) {
			TextLine oneLine = lines.get(i);
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
