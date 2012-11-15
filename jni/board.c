/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include "com_fivestar_sirius_BoardView.h"

#define BLACK 1
#define WHITE 0
#define EMPTY 2
#define u64 unsigned long long

JNIEXPORT void JNICALL Java_com_fivestar_sirius_BoardView_legalMoves
  (JNIEnv *, jclass, jobject, jint) {
	int sortnum;
	register unsigned int j;
	register unsigned int i = 1;
	short int *pnt;
	u64 mask = 1;
	u64 legal;

	if(b->color_to_move) {
		legal = calculate_legal(b->black,b->white);
	} else {
		legal = calculate_legal(b->white,b->black);
	}

	pnt = (short int *) &(b->legal_move);
	while(legal) {
		if(legal & mask) {
			*pnt++ = i;
			legal &= ~mask;
		}
		i++;
		mask = mask << 1;
	}
	*pnt = -1;
	b->num_legal_moves = pnt - b->legal_move;


	/* sort the movelist */
	/* put the best move first */
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
}
