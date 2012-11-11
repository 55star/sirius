package com.fivestar.sirius;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BoardAdapter extends BaseAdapter {
	final static String TAG = "BoardAdapter.java";
    private Context mContext;
    private Board mBoard;
    private SiriusApp app;

    public BoardAdapter(Context c, Board b, SiriusApp a) {
        mContext = c;
        mBoard = b;
        app = a;
    }

    public int getCount() {
        return 64;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 1;
    }

    // create a new ImageView for each item referenced by the Adapter                                                                                                                                                          
    public View getView(int pos, View convertView, ViewGroup parent) {
        ImageView imageView;
        int position = pos + 1;
//        Utils.log(TAG, "Onclick: "+position);
        if (convertView == null) {  // if it's not recycled, initialize some attributes                                                                                                                                        
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(2,2,2,2);
        } else {
            imageView = (ImageView) convertView;
        }

//        BoardLogic mBoardLogic = new BoardLogic();
        switch(app.boardLogic.pos(app.board, position)) {
        case Constant.BLACK :
            imageView.setImageResource(R.drawable.brick_black);
            break;
        case Constant.WHITE :
            imageView.setImageResource(R.drawable.brick_white);
            break;
        case Constant.EMPTY :
            imageView.setImageResource(R.drawable.brick_empty);
            break;
        }
        return imageView;
    }
}

