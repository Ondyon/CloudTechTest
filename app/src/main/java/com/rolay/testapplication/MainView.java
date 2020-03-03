package com.rolay.testapplication;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageButton;

import com.rolay.views.CustomTextView;

class MainView implements MainViewContract {

	private static final int ACTIVE_BUTTON_TINT = 0x99FFDD00;
	private final ImageButton buttonBold;
	private final ImageButton buttonItalic;
	private MainPresenterContract mPresenter;

	private CustomTextView customTextView;

	MainView(Activity activity, MainPresenterContract presenter) {
		this.mPresenter = presenter;
		buttonBold = activity.findViewById(R.id.button_bold);
		buttonItalic = activity.findViewById(R.id.button_italic);
		View buttonIncrease = activity.findViewById(R.id.button_inc);
		View buttonDecrease = activity.findViewById(R.id.button_dec);

		buttonBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.setBoldPressed();
			}
		});
		buttonItalic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.setItalicPressed();
			}
		});
		buttonIncrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.increasePressed();
			}
		});
		buttonDecrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.decreasePressed();
			}
		});


		customTextView = activity.findViewById(R.id.custom_text);
		customTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.customTextClicked();
			}
		});
	}


	@Override
	public void markBold(boolean bold) {
		if(bold) {
			buttonBold.setColorFilter(ACTIVE_BUTTON_TINT, PorterDuff.Mode.DARKEN);
		} else {
			buttonBold.clearColorFilter();
		}
	}

	@Override
	public void markItalic(boolean italic) {
		if(italic) {
			buttonItalic.setColorFilter(ACTIVE_BUTTON_TINT, PorterDuff.Mode.DARKEN);
		} else {
			buttonItalic.clearColorFilter();
		}
	}

	@Override
	public void setCustomText(String text) {
		customTextView.setText(text);
	}

	@Override
	public void setCustomTextBold(boolean bold) {
		customTextView.setBold(bold);
	}

	@Override
	public void setCustomTextItalic(boolean italic) {
		customTextView.setItalic(italic);
	}

	@Override
	public void setCustomTextSize(int textSize) {
		customTextView.setTextSize(textSize);
	}


}
