package com.fivestar.sirius;

import java.util.ArrayList;


public class Board {
	public long black;
	public long white;
	public int color_to_move;
	public ArrayList<Integer> legal_move;

	public int halfMove;
	public int pass;
	public int gameOver;

	public Board() {
		// Black always start 
		color_to_move = Constant.BLACK;
		legal_move = new ArrayList<Integer>(10);
	}

	public long rotate_bit_pattern(long orig) {
		int[] delta = {7,15,23,31,39,47,55,63};
		int i,j,k;
		long rotated =  0;
		long mask1 = 1;
		long mask2 = 1;

		j=0;
		for(i=0; i<64; i++) {
			if(((mask1 << i) & orig) > 0) {
				rotated |= (mask2 << delta[j]);
			}
			if(j == 7) {
				j = 0;
				for(k=0; k<8; k++) {
					delta[k] = delta[k] - 1;
				}
			} else {
				j++;
			}
		}
		return rotated;
	}

//	public void mobility(Board b) {
//		long legal;
//
//		if(b.color_to_move) {
//			legal = calculate_legal(b.black,b.white);
//		} else {
//			legal = calculate_legal(b.white,b.black);
//		}	
//		b->num_legal_moves = numbits(legal);
//	}
//
//
//	public int get_mobility_from_move(Board b, int move) {
//		long mask = 1;
//		long flips;
//		long bl,wh;
//
//		bl = b.black;
//		wh = b.white;
//
//		mask = mask << (move-1);
//		if(b.color_to_move > 0) {
//			flips = b.calculate_flips(bl, wh, mask);
//			bl |= flips;
//			wh &= ~flips;
//			return (numbits(calculate_legal(bl, wh)));
//		} else {
//			flips = calculate_flips(wh, bl, mask);
//			wh |= flips;
//			bl &= ~flips;
//			return (numbits(calculate_legal(wh,bl)));
//		}
//
//		return (0);
//	}

}	