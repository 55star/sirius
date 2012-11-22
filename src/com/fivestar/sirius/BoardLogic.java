package com.fivestar.sirius;


public class BoardLogic {
	final static String TAG = "BoardLogic.java";

	public BoardLogic() {}

	public static native long calculateLegal(long me, long you);
	public static native long calculateFlips(long me, long you, long mask);


	final static int[] movePriority = {
		55,50,15,10,
		16,56,63,58,49,9,7,2,
		51,54,47,42,23,18,14,11,
		39,31,53,52,34,26,13,12,
		38,30,45,44,35,27,21,20,
		33,25,61,60,40,32,5,4,
		46,43,22,19,
		62,59,48,41,24,17,6,3,
		64,57,8,1
	};

	public Board initBoard() {
		long mask = 1;
		Board mBoard = new Board();

		mBoard.black = 0;
		mBoard.black |= (mask << 28);
		mBoard.black |= (mask << 35);

		mBoard.white = 0;
		mBoard.white |= (mask << 27);
		mBoard.white |= (mask << 36);

		mBoard.halfMove = 0;
		mBoard.gameOver = 0;
		mBoard.pass = 0;

		return mBoard;
	}


	public void dumpToConsole(Board b) {
		int svart = 0;
		int vit = 0;
		int column = 0;
		Utils.log(TAG,"\n A  B  C  D  E  F  G  H  ");
		StringBuffer sb = new StringBuffer();
		for(int i=1; i<65; i++) {
			switch(pos(b,i)) {
			case Constant.BLACK :
				sb.append(" X ");
				svart++;
				break;
			case Constant.WHITE :
				sb.append(" 0 ");
				vit++;
				break;
			case Constant.EMPTY :
				sb.append(" + ");
				break;
			}
			if(i%8 == 0) {
				Utils.log(TAG,sb.toString() + " " + ++column);
				sb = new StringBuffer();
			}
		}
		Utils.log(TAG,"         " + svart + " - " + vit);
	}

	public int pos(Board b, int move) {
		long mask = 1;

		if((b.black & (mask << (move-1))) != 0) {
			return (Constant.BLACK);
		} else {
			if((b.white & (mask << (move-1))) != 0) {
				return (Constant.WHITE);
			} else {
				return(Constant.EMPTY);
			}
		}
	}


	/*
	 * Convert from long to boolean
	 */
	public boolean l2B(long value) {
		return (value != 0);
	}

	public int numbits(long orig) {
		orig = (orig & 0x5555555555555555L) + ((orig & 0xaaaaaaaaaaaaaaaaL) >> 1);
		orig = (orig & 0x3333333333333333L) + ((orig & 0xccccccccccccccccL) >> 2);
		orig = (orig & 0x0f0f0f0f0f0f0f0fL) + ((orig & 0xf0f0f0f0f0f0f0f0L) >> 4);
		orig = (orig & 0x00ff00ff00ff00ffL) + ((orig & 0xff00ff00ff00ff00L) >> 8);
		orig = (orig & 0x0000ffff0000ffffL) + ((orig & 0xffff0000ffff0000L) >> 16);
		orig = (orig & 0x00000000ffffffffL) + ((orig & 0xffffffff00000000L) >> 32);

		return ((int)(orig));
	}

	public void legalMoves(Board b, int bestmove) {
		int i = 1;
		int sortnum;
		int j;		
		long mask = 1;
		long legal;

		if(b.colorToMove == Constant.BLACK) {
			legal = calculateLegal(b.black, b.white);
		} else {
			legal = calculateLegal(b.white, b.black);
		}
		b.legalMove.clear();
		while(l2B(legal)) {
			if(l2B(legal & mask)) {
				b.legalMove.add(i);
				//				Utils.log(TAG,"Add "+i+" to legal. Now we have "+b.legalMove.size());
				legal &= ~mask;
			}
			i++;
			mask = mask << 1;
		}

		/* sort the movelist */
		/* put the best move first */
		if(b.legalMove.size() > 1) {
			if(bestmove != 0) {
				sortnum = 0;
				for(i = b.legalMove.size(); i>=0; i-- ) {
					if(b.legalMove.get(i) == bestmove) {
						if(i > 0) {
							int tmp = b.legalMove.get(0);
							b.legalMove.set(0, b.legalMove.get(i));
							b.legalMove.set(i,tmp);
							sortnum = 1;
						}
						break;
					}
				}
				if(b.halfMove <= 30) {
					for(i=59; i>=0; i-- ) {
						for(j=b.legalMove.size(); j>=0; j-- ) {
							if(b.legalMove.get(j) == movePriority[i]) {
								int tmp = b.legalMove.get(sortnum);
								b.legalMove.set(sortnum, b.legalMove.get(j));
								b.legalMove.set(j,tmp);
								sortnum++;
								break;
							}
						}
						if((sortnum == 3) || (b.legalMove.size() == sortnum))
							break;
					}
				}
			}

			if(b.legalMove.size() == 0) {
				if(b.pass == 1 || b.halfMove == 60 || b.black == 0 || b.white == 0) {
					b.gameOver = 1;
				} else {
					b.pass = 1;
				}
			} else {
				b.pass = 0;
			}
		}
	}

	/**
	 * do_move:
	 * Make a move.
	 */
	public Board doMove(Board b, int move) {
		long mask = 1;
		long flips;

		mask = mask << (move-1);
		if(b.colorToMove == Constant.BLACK) {
			flips = calculateFlips(b.black, b.white, mask);
			b.black |= flips;
			b.white &= ~flips;
			b.colorToMove = Constant.WHITE;
		} else {
			flips = calculateFlips(b.white, b.black, mask);
			b.white |= flips;
			b.black &= ~flips;
			b.colorToMove = Constant.BLACK;
		}
		Utils.log(TAG,"Flips: " + flips);

		b.halfMove++;
		b.pass = 0;

		return (b);
	}

	public Board do_pass(Board b) {

		if(b.colorToMove == Constant.BLACK) {
			b.colorToMove = Constant.WHITE;
		} else {
			b.colorToMove = Constant.BLACK;
		}
		b.pass = 1;

		return (b);
	}

	public boolean legal(Board b, int move) {
		int i;
		Utils.log(TAG, "legal: "+ move);
		for(i=0; i<b.legalMove.size(); i++) {
			if(b.legalMove.get(i) == move) {
				return true;
			}
		}
		return false;
	}
}	