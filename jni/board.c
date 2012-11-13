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
#include <jni.h>

#define BLACK 1
#define WHITE 0
#define EMPTY 2

#define u64 unsigned long long


jint
Java_com_example_twolibs_TwoLibs_add( JNIEnv*  env,
		jobject  this,
		jint     x,
		jint     y )

{
	return first(x, y);
}

void
Java_com.fivestar_sirius_Sirius_do_move( JNIEnv* env, jobject this, jint black, jint white, jint move, jint color_to_move) {
	u64 mask = 1;
	u64 flips;
	u64 board[2];
	short int *to;
	short int *from;

	board[WHITE]=white;
	board[BLACK]=black;

	mask = mask << (move-1);
	if(b->color_to_move) {
		flips = calculate_flips(board[BLACK], board[WHITE, mask);
		board[BLACK] |= flips;
		board[WHITE] &= ~flips;
		/*		b->color_to_move = WHITE; */

		/* save undo info */
		ui->undo_pattern = flips;
		ui->undo_color = BLACK;
		*/
	} else {
		flips = calculate_flips(board[WHITE], board[BLACK], mask);
		board[WHITE] |= flips;
		board[BLACK] &= ~flips;
/*		b->color_to_move = BLACK; */

		/* save undo info
		ui->undo_pattern = flips;
		ui->undo_color = WHITE;
		*/
	}

	/* save more undo info
	ui->undo_x               = b->x;
	ui->undo_y               = b->y;
	ui->undo_mask            = mask;
	ui->undo_num_legal_moves = b->num_legal_moves;
	ui->pass                 = b->pass;
	ui->game_over            = b->game_over;
	*/

	to   = (short int *) &(ui->undo_legal_move);
	from = (short int *) &(b->legal_move);
	while((*from) != -1) {
		*to++ = *from++;
	}
	*to = -1;

	/*
	for(i=0; i<ui->undo_num_legal_moves+1; i++) {
		printf("%d\n", ui->undo_legal_move[i]);
	}
	exit(1);
	 */


	/*
	for(i=b->num_legal_moves; i--; ) {
		ui->undo_legal_move[i] = b->legal_move[i];
	}
	 */

	/* update the transposition table indexes */
	/*
	b = transposition_hash(b);

	b->half_move++;
	b->pass = 0;
*/
	return (board);
}
