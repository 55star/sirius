package com.fivestar.sirius;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;


public class Sirius extends Activity {
	final static String TAG = "Sirius.java";
	SiriusApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Some kind of startpage
		setContentView(R.layout.sirius);

		app = ((SiriusApp)getApplicationContext());
		app.initNewGame();

	}	

	public void mainMenuClickHandler(View view) {
		if(view.getId() == R.id.start_button) {
			Intent boardIntent = new Intent(app, BoardView.class);
			startActivity(boardIntent);
		}

	}
}