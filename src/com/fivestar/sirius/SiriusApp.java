package com.fivestar.sirius;

import android.app.Application;
import android.widget.TextView;

public class SiriusApp extends Application {
	public Board board;
	public BoardLogic boardLogic;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public Board initNewGame() {
		boardLogic = new BoardLogic();
		board = boardLogic.initBoard();
		return board;
	}
}		