package com.fivestar.sirius;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Sirius extends Activity {
	final static String TAG = "Sirius.java";
	SiriusApp app;

	static {
		System.loadLibrary("board");
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Some kind of startpage
		//setContentView(R.layout.board);
		
		app = ((SiriusApp)getApplicationContext());
		app.initNewGame();
		
		Intent boardIntent = new Intent(app, BoardView.class);
		startActivity(boardIntent);
	}	
}
