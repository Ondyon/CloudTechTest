package com.rolay.testapplication;

public interface MainPresenterContract {

	void setBoldPressed();

	void setItalicPressed();

	void increasePressed();

	void decreasePressed();

	void takeView(MainViewContract panelView);

	void customTextClicked();
}
