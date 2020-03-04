package com.rolay.testapplication;

public class MainPresenter implements MainPresenterContract {
	private static final int INITIAL_TEXT_SIZE = 70;
	private static final int MAX_TEXT_SIZE = 2000;
	private static final int MIN_TEXT_SIZE = 13;

	private MainViewContract mView;
	private boolean bold = false;
	private boolean italic = false;
	private int mTextSize = INITIAL_TEXT_SIZE;


	MainPresenter() {
	}

	@Override
	public void takeView(MainViewContract panelView) {
		this.mView = panelView;
	}


	@Override
	public void customTextClicked() {
		bold = false;
		italic = false;
		mTextSize = INITIAL_TEXT_SIZE;

		mView.markBold(bold);
		mView.setCustomTextBold(bold);
		mView.markItalic(italic);
		mView.setCustomTextItalic(italic);
		mView.setCustomTextSize(mTextSize);
	}

	@Override
	public void setBoldPressed() {
		bold = !bold;
		mView.markBold(bold);
		mView.setCustomTextBold(bold);
	}

	@Override
	public void setItalicPressed() {
		italic = !italic;
		mView.markItalic(italic);
		mView.setCustomTextItalic(italic);
	}

	@Override
	public void increasePressed() {
	    if(mTextSize > MAX_TEXT_SIZE)
	        return;
		mTextSize += 10;
		mView.setCustomTextSize(mTextSize);
	}

	@Override
	public void decreasePressed() {
	    if(mTextSize < MIN_TEXT_SIZE)
	    	return;
		mTextSize -= 10;
		mView.setCustomTextSize(mTextSize);
	}

}
