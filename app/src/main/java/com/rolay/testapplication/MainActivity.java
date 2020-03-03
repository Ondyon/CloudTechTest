package com.rolay.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainPresenterContract presenter = new MainPresenter();
		MainViewContract mainView = new MainView(this, presenter);
		presenter.takeView(mainView);

		mainView.setCustomText("Some long text");
	}

}
