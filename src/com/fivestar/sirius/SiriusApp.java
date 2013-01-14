package com.fivestar.sirius;

import android.app.Application;

public class SiriusApp extends Application {
	public Board board;
	final static String TAG = "SiriusApp.java";
	
//	static {
//		System.loadLibrary("board");
//	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void initNewGame() {
		board = new Board();
	}
}		