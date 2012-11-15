package com.fivestar.sirius;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BoardView extends Activity {

	final static String TAG = "BoardView.java";
	GridView mGridView;
	BoardAdapter mBoardAdapter;
	SiriusApp app;
		
	public static native void legalMoves(Board b, int bestmove);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		app = ((SiriusApp)getApplicationContext());

		final TextView mStatusText = (TextView) findViewById(R.id.statusText);
		final TextView mStatText = (TextView) findViewById(R.id.statText);

		//		app.boardLogic.dumpToConsole(app.board);

		mBoardAdapter = new BoardAdapter(this, app.board, app);
		mGridView = (GridView) findViewById(R.id.boardGrid);
		mGridView.setAdapter(mBoardAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				int position = pos;
				
				Utils.log(TAG, "Board click: " + pos);

//				if(position != 63) {
					position += 1;
//				}
				/*
				1 -> 1  
				9 -> 2   -7
				17 -> 3  -14
				3 -> 9   + 6 32A
				
				*/
				Utils.log(TAG, "Check legal/do move position: " + position);

				
//				app.boardLogic.legal_moves(app.board, 0);
				BoardView.legalMoves(app.board,0);
				if(app.boardLogic.legal(app.board, position)) {
					app.boardLogic.do_move(app.board, position);
					mBoardAdapter.notifyDataSetChanged();
				} else {
					Utils.log(TAG, "not a legal move!");
				}
				if (app.board.color_to_move == Constant.BLACK) {
					mStatusText.setText("Black's turn to play");
				} else {
					mStatusText.setText("White's turn to play");
				}
				mStatText.setText("Black "+app.boardLogic.numbits(app.board.black)
									+" - "+ app.boardLogic.numbits(app.board.white) + " White");
//				app.boardLogic.dumpToConsole(app.board);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}