package com.rolay.testapplication;

public interface MainViewContract {

	void markBold(boolean bold);

	void markItalic(boolean italic);

	void setCustomText(String text);

	void setCustomTextBold(boolean bold);

	void setCustomTextItalic(boolean italic);

	void setCustomTextSize(int mTextSize);

	void scrollTo(int i);
}
