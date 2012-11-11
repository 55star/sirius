package com.fivestar.sirius;


public class BoardLogic {
	final static String TAG = "BoardLogic.java";

	public BoardLogic() {}


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

		if((b.black & (mask << (move-1))) != 0) { // move-1
			return (Constant.BLACK);
		} else {
			if((b.white & (mask << (move-1))) != 0) {  // move-1
				return (Constant.WHITE);
			} else {
				return(Constant.EMPTY);
			}
		}
	}


	public long calculate_legal(long me, long you) {
		long free  = ~(me | you);
		long value;

		value = (free &
				((Constant.N1  & (you << 8) & (me << 16))  |
						(Constant.NW1 & (you << 9) & (me << 18))  |
						(Constant.W1  & (you << 1) & (me << 2))   |
						(Constant.SW1 & (you >> 7) & (me >> 14))  |
						(Constant.S1  & (you >> 8) & (me >> 16))  |
						(Constant.SE1 & (you >> 9) & (me >> 18))  |
						(Constant.E1  & (you >> 1) & (me >> 2))   |
						(Constant.NE1 & (you << 7) & (me << 14))));

		value |= (free &
				((Constant.N2   & (you << 8) & (you << 16) & (me << 24))  |
						(Constant.NW2  & (you << 9) & (you << 18) & (me << 27))  |
						(Constant.W2   & (you << 1) & (you << 2)  & (me << 3))   |
						(Constant.SW2  & (you >> 7) & (you >> 14) & (me >> 21))  |
						(Constant.S2   & (you >> 8) & (you >> 16) & (me >> 24))  |
						(Constant.SE2  & (you >> 9) & (you >> 18) & (me >> 27))  |
						(Constant.E2   & (you >> 1) & (you >> 2)  & (me >> 3))   |
						(Constant.NE2  & (you << 7) & (you << 14) & (me << 21))));

		value |= (free &
				((Constant.N3   & (you << 8) & (you << 16) & (you << 24) & (me << 32))  |
						(Constant.NW3  & (you << 9) & (you << 18) & (you << 27) & (me << 36))  |
						(Constant.W3   & (you << 1) & (you << 2)  & (you << 3)  & (me << 4))   |
						(Constant.SW3  & (you >> 7) & (you >> 14) & (you >> 21) & (me >> 28))  |
						(Constant.S3   & (you >> 8) & (you >> 16) & (you >> 24) & (me >> 32))  |
						(Constant.SE3  & (you >> 9) & (you >> 18) & (you >> 27) & (me >> 36))  |
						(Constant.E3   & (you >> 1) & (you >> 2)  & (you >> 3)  & (me >> 4))   |
						(Constant.NE3  & (you << 7) & (you << 14) & (you << 21) & (me << 28))));

		value |= (free &
				((Constant.N4   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (me << 40))  |
						(Constant.NW4  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (me << 45))  |
						(Constant.W4   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (me << 5))   |
						(Constant.SW4  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (me >> 35))  |
						(Constant.S4   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (me >> 40))  |
						(Constant.SE4  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (me >> 45))  |
						(Constant.E4   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (me >> 5))   |
						(Constant.NE4  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (me << 35))));

		value |= (free &
				((Constant.N5   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (you << 40) & (me << 48))  |
						(Constant.NW5  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (you << 45) & (me << 54))  |
						(Constant.W5   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (you << 5)  & (me << 6))   |
						(Constant.SW5  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (you >> 35) & (me >> 42))  |
						(Constant.S5   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (you >> 40) & (me >> 48))  |
						(Constant.SE5  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (you >> 45) & (me >> 54))  |
						(Constant.E5   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (you >> 5)  & (me >> 6))   |
						(Constant.NE5  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (you << 35) & (me << 42))));

		value |= (free &
				((Constant.N6   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (you << 40) & (you << 48) & (me << 56))  |
						(Constant.NW6  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (you << 45) & (you << 54) & (me << 63))  |
						(Constant.W6   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (you << 5)  & (you << 6)  & (me << 7))   |
						(Constant.SW6  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (you >> 35) & (you >> 42) & (me >> 49))  |
						(Constant.S6   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (you >> 40) & (you >> 48) & (me >> 56))  |
						(Constant.SE6  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (you >> 45) & (you >> 54) & (me >> 63))  |
						(Constant.E6   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (you >> 5)  & (you >> 6)  & (me >> 7))   |
						(Constant.NE6  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (you << 35) & (you << 42) & (me << 49))));

		return (value);
	}

	/*
	 * Convert from long to boolean
	 */
	public boolean l2B(long value) {
		return (value != 0);
	}


	public long calculate_flips(long me, long you, long mask) {
		long tmp;
		long value = mask;

		/* SOUTH */
		tmp = ((mask << 8) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.S1) && l2B((mask << 16) & me)) {
				value |= (mask << 8);
			} else if(l2B(mask & Constant.S2) &&
					l2B((mask << 16) & you) && l2B((mask << 24) & me)) {
				value |= ((mask << 8) | (mask << 16));
			} else if(l2B(mask & Constant.S3) &&
					l2B((mask << 16) & you) && l2B((mask << 24) & you) &&
					l2B((mask << 32) & me)) {
				value |= ((mask << 8) | (mask << 16) | (mask << 24));
			} else if(l2B(mask & Constant.S4) &&
					l2B((mask << 16) & you) && l2B((mask << 24) & you) &&
					l2B((mask << 32) & you) && l2B((mask << 40) & me)) {
				value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32));
			} else if(l2B(mask & Constant.S5) &&
					l2B((mask << 16) & you) && l2B((mask << 24) & you) &&
					l2B((mask << 32) & you) && l2B((mask << 40) & you) &&
					l2B((mask << 48) & me)) {
				value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32) | (mask << 40));
			} else if(l2B(mask & Constant.S6) &&
					l2B((mask << 16) & you) && l2B((mask << 24) & you) &&
					l2B((mask << 32) & you) && l2B((mask << 40) & you) &&
					l2B((mask << 48) & you) && l2B((mask << 56) & me)) {
				value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32) | (mask << 40) | (mask << 48));
			}
		}
		/* SOUTHEAST */
		tmp = ((mask << 9) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.SE1) && l2B((mask << 18) & me)) {
				value |= (mask << 9);
			} else if(l2B(mask & Constant.SE2) &&
					l2B((mask << 18) & you) && l2B((mask << 27) & me)) {
				value |= ((mask << 9) | (mask << 18));
			} else if(l2B(mask & Constant.SE3) &&
					l2B((mask << 18) & you) && l2B((mask << 27) & you) &&
					l2B((mask << 36) & me)) {
				value |= ((mask << 9) | (mask << 18) | (mask << 27));
			} else if(l2B(mask & Constant.SE4) &&
					l2B((mask << 18) & you) && l2B((mask << 27) & you) &&
					l2B((mask << 36) & you) && l2B((mask << 45) & me)) {
				value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36));
			} else if(l2B(mask & Constant.SE5) &&
					l2B((mask << 18) & you) && l2B((mask << 27) & you) &&
					l2B((mask << 36) & you) && l2B((mask << 45) & you) &&
					l2B((mask << 54) & me)) {
				value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36) | (mask << 45));
			} else if(l2B(mask & Constant.SE6) &&
					l2B((mask << 18) & you) && l2B((mask << 27) & you) &&
					l2B((mask << 36) & you) && l2B((mask << 45) & you) &&
					l2B((mask << 54) & you) && l2B((mask << 63) & me)) {
				value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36) | (mask << 45) | (mask << 54));
			}
		}
		/* EAST */
		tmp = ((mask << 1) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.E1) && l2B((mask << 2) & me)) {
				value |= (mask << 1);
			} else if(l2B(mask & Constant.E2) &&
					l2B((mask << 2) & you) && l2B((mask << 3) & me)) {
				value |= ((mask << 1) | (mask << 2));
			} else if(l2B(mask & Constant.E3) &&
					l2B((mask << 2) & you) && l2B((mask << 3) & you) &&
					l2B((mask << 4) & me)) {
				value |= ((mask << 1) | (mask << 2) | (mask << 3));
			} else if(l2B(mask & Constant.E4) &&
					l2B((mask << 2) & you) && l2B((mask << 3) & you) &&
					l2B((mask << 4) & you) && l2B((mask << 5) & me)) {
				value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4));
			} else if(l2B(mask & Constant.E5) &&
					l2B((mask << 2) & you) && l2B((mask << 3) & you) &&
					l2B((mask << 4) & you) && l2B((mask << 5) & you) &&
					l2B((mask << 6) & me)) {
				value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4) | (mask << 5));
			} else if(l2B(mask & Constant.E6) &&
					l2B((mask << 2) & you) && l2B((mask << 3) & you) &&
					l2B((mask << 4) & you) && l2B((mask << 5) & you) &&
					l2B((mask << 6) & you) && l2B((mask << 7) & me)) {
				value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4) | (mask << 5) | (mask << 6));
			}
		}
		/* SOUTHWEST */
		tmp = ((mask << 7) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.SW1) && l2B((mask << 14) & me)) {
				value |= (mask << 7);
			} else if(l2B(mask & Constant.SW2) &&
					l2B((mask << 14) & you) && l2B((mask << 21) & me)) {
				value |= ((mask << 7) | (mask << 14));
			} else if(l2B(mask & Constant.SW3) &&
					l2B((mask << 14) & you) && l2B((mask << 21) & you) &&
					l2B((mask << 28) & me)) {
				value |= ((mask << 7) | (mask << 14) | (mask << 21));
			} else if(l2B(mask & Constant.SW4) &&
					l2B((mask << 14) & you) && l2B((mask << 21) & you) &&
					l2B((mask << 28) & you) && l2B((mask << 35) & me)) {
				value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28));
			} else if(l2B(mask & Constant.SW5) &&
					l2B((mask << 14) & you) && l2B((mask << 21) & you) &&
					l2B((mask << 28) & you) && l2B((mask << 35) & you) &&
					l2B((mask << 42) & me)) {
				value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28) | (mask << 35));
			} else if(l2B(mask & Constant.SW6) &&
					l2B((mask << 14) & you) && l2B((mask << 21) & you) &&
					l2B((mask << 28) & you) && l2B((mask << 35) & you) &&
					l2B((mask << 42) & you) && l2B((mask << 49) & me)) {
				value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28) | (mask << 35) | (mask << 42));
			}
		}
		/* NORTH */
		tmp = ((mask >> 8) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.N1) && l2B((mask >> 16) & me)) {
				value |= (mask >> 8);
			} else if(l2B(mask & Constant.N2) &&
					l2B((mask >> 16) & you) && l2B((mask >> 24) & me)) {
				value |= ((mask >> 8) | (mask >> 16));
			} else if(l2B(mask & Constant.N3) &&
					l2B((mask >> 16) & you) && l2B((mask >> 24) & you) &&
					l2B((mask >> 32) & me)) {
				value |= ((mask >> 8) | (mask >> 16) | (mask >> 24));
			} else if(l2B(mask & Constant.N4) &&
					l2B((mask >> 16) & you) && l2B((mask >> 24) & you) &&
					l2B((mask >> 32) & you) && l2B((mask >> 40) & me)) {
				value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32));
			} else if(l2B(mask & Constant.N5) &&
					l2B((mask >> 16) & you) && l2B((mask >> 24) & you) &&
					l2B((mask >> 32) & you) && l2B((mask >> 40) & you) &&
					l2B((mask >> 48) & me)) {
				value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32) | (mask >> 40));
			} else if(l2B(mask & Constant.N6) &&
					l2B((mask >> 16) & you) && l2B((mask >> 24) & you) &&
					l2B((mask >> 32) & you) && l2B((mask >> 40) & you) &&
					l2B((mask >> 48) & you) && l2B((mask >> 56) & me)) {
				value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32) | (mask >> 40) | (mask >> 48));
			}
		}
		/* NORTHWEST */
		tmp = ((mask >> 9) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.NW1) && l2B((mask >> 18) & me)) {
				value |= (mask >> 9);
			} else if(l2B(mask & Constant.NW2) &&
					l2B((mask >> 18) & you) && l2B((mask >> 27) & me)) {
				value |= ((mask >> 9) | (mask >> 18));
			} else if(l2B(mask & Constant.NW3) &&
					l2B((mask >> 18) & you) && l2B((mask >> 27) & you) &&
					l2B((mask >> 36) & me)) {
				value |= ((mask >> 9) | (mask >> 18) | (mask >> 27));
			} else if(l2B(mask & Constant.NW4) &&
					l2B((mask >> 18) & you) && l2B((mask >> 27) & you) &&
					l2B((mask >> 36) & you) && l2B((mask >> 45) & me)) {
				value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36));
			} else if(l2B(mask & Constant.NW5) &&
					l2B((mask >> 18) & you) && l2B((mask >> 27) & you) &&
					l2B((mask >> 36) & you) && l2B((mask >> 45) & you) &&
					l2B((mask >> 54) & me)) {
				value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36) | (mask >> 45));
			} else if(l2B(mask & Constant.NW6) &&
					l2B((mask >> 18) & you) && l2B((mask >> 27) & you) &&
					l2B((mask >> 36) & you) && l2B((mask >> 45) & you) &&
					l2B((mask >> 54) & you) && l2B((mask >> 63) & me)) {
				value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36) | (mask >> 45) | (mask >> 54));
			}
		}
		/* WEST */
		tmp = ((mask >> 1) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.W1) && l2B((mask >> 2) & me)) {
				value |= (mask >> 1);
			} else if(l2B(mask & Constant.W2) &&
					l2B((mask >> 2) & you) && l2B((mask >> 3) & me)) {
				value |= ((mask >> 1) | (mask >> 2));
			} else if(l2B(mask & Constant.W3) &&
					l2B((mask >> 2) & you) && l2B((mask >> 3) & you) &&
					l2B((mask >> 4) & me)) {
				value |= ((mask >> 1) | (mask >> 2) | (mask >> 3));
			} else if(l2B(mask & Constant.W4) &&
					l2B((mask >> 2) & you) && l2B((mask >> 3) & you) &&
					l2B((mask >> 4) & you) && l2B((mask >> 5) & me)) {
				value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4));
			} else if(l2B(mask & Constant.W5) &&
					l2B((mask >> 2) & you) && l2B((mask >> 3) & you) &&
					l2B((mask >> 4) & you) && l2B((mask >> 5) & you) &&
					l2B((mask >> 6) & me)) {
				value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4) | (mask >> 5));
			} else if(l2B(mask & Constant.W6) &&
					l2B((mask >> 2) & you) && l2B((mask >> 3) & you) &&
					l2B((mask >> 4) & you) && l2B((mask >> 5) & you) &&
					l2B((mask >> 6) & you) && l2B((mask >> 7) & me)) {
				value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4) | (mask >> 5) | (mask >> 6));
			}
		}
		/* NORTHEAST */
		tmp = ((mask >> 7) & you);
		if(l2B(tmp)) {
			if(l2B(mask & Constant.NE1) && l2B((mask >> 14) & me)) {
				value |= (mask >> 7);
			} else if(l2B(mask & Constant.NE2) &&
					l2B((mask >> 14) & you) && l2B((mask >> 21) & me)) {
				value |= ((mask >> 7) | (mask >> 14));
			} else if(l2B(mask & Constant.NE3) &&
					l2B((mask >> 14) & you) && l2B((mask >> 21) & you) &&
					l2B((mask >> 28) & me)) {
				value |= ((mask >> 7) | (mask >> 14) | (mask >> 21));
			} else if(l2B(mask & Constant.NE4) &&
					l2B((mask >> 14) & you) && l2B((mask >> 21) & you) &&
					l2B((mask >> 28) & you) && l2B((mask >> 35) & me)) {
				value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28));
			} else if(l2B(mask & Constant.NE5) &&
					l2B((mask >> 14) & you) && l2B((mask >> 21) & you) &&
					l2B((mask >> 28) & you) && l2B((mask >> 35) & you) &&
					l2B((mask >> 42) & me)) {
				value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28) | (mask >> 35));
			} else if(l2B(mask & Constant.NE6) &&
					l2B((mask >> 14) & you) && l2B((mask >> 21) & you) &&
					l2B((mask >> 28) & you) && l2B((mask >> 35) & you) &&
					l2B((mask >> 42) & you) && l2B((mask >> 49) & me)) {
				value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28) | (mask >> 35) | (mask >> 42));
			}
		}
		return (value);
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

	public void legal_moves(Board b, int bestmove) {
		int i = 1;
		long mask = 1;
		long legal;

		if(b.color_to_move == Constant.BLACK) {
			legal = calculate_legal(b.black, b.white);
		} else {
			legal = calculate_legal(b.white, b.black);
		}
		b.legal_move.clear();
		while(l2B(legal)) {
			if(l2B(legal & mask)) {
				b.legal_move.add(i);
				Utils.log(TAG,"Add "+i+" to legal. Now we have "+b.legal_move.size());
				legal &= ~mask;
			}
			i++;
			mask = mask << 1;
		}
//		b.num_legal_moves = pnt - b->legal_move; 


		/* sort the movelist */
		/* put the best move first */
		/*
        if(b->num_legal_moves > 1) {
            if(bestmove != 0) {
                sortnum = 0;
                for(i = b->num_legal_moves; i--; ) {
                    if(b->legal_move[i] == bestmove) {
                        if(i > 0) {
                            int tmp = b->legal_move[0];
                            b->legal_move[0] = b->legal_move[i];
                            b->legal_move[i] = tmp;
                            sortnum = 1;
                        }
                        break;
                    }
                }
                if(b->half_move <= 30) {
                    for(i=59; i--; ) {
                        for(j=b->num_legal_moves; j--; ) {
                            if(b->legal_move[j] == move_priority[i]) {
                                int tmp = b->legal_move[sortnum];
                                b->legal_move[sortnum] = b->legal_move[j];
                                b->legal_move[j] = tmp;
                                sortnum++;
                                break;
                            }
                        }
                        if((sortnum == 3) || (b->num_legal_moves == sortnum)) break;
                    }
                }
            }
        }
		 */
		if(b.legal_move.size() == 0) {
			if(b.pass == 1 || b.halfMove == 60 || b.black == 0 || b.white == 0) {
				b.gameOver = 1;
			} else {
				b.pass = 1;
			}
		} else {
			b.pass = 0;
		}
	}

	/**
	 * do_move:
	 * Make a move.
	 */
	public Board do_move(Board b, int move) {
		long mask = 1;
		long flips;

		mask = mask << (move-1);
		if(b.color_to_move == Constant.BLACK) {
			flips = calculate_flips(b.black, b.white, mask);
			b.black |= flips;
			b.white &= ~flips;
			b.color_to_move = Constant.WHITE;
		} else {
			flips = calculate_flips(b.white, b.black, mask);
			b.white |= flips;
			b.black &= ~flips;
			b.color_to_move = Constant.BLACK;
		}

		b.halfMove++;
		b.pass = 0;
		return (b);
	}

	public Board do_pass(Board b) {

		if(b.color_to_move == Constant.BLACK) {
			b.color_to_move = Constant.WHITE;
		} else {
			b.color_to_move = Constant.BLACK;
		}
		b.pass = 1;

		return (b);
	}

	public boolean legal(Board b, int move) {
		int i;
		Utils.log(TAG, "legal: "+ move);
		for(i=0; i<b.legal_move.size(); i++) {
			if(b.legal_move.get(i) == move) {
				return true;
			}
		}
		return false;
	}
}	