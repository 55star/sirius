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

#include "com_fivestar_sirius_Board.h"

#define BLACK 1
#define WHITE 0
#define EMPTY 2
#define u64 unsigned long long

#define N1 0xFFFFFFFFFFFF0000
#define N2 0xFFFFFFFFFF000000
#define N3 0xFFFFFFFF00000000
#define N4 0xFFFFFF0000000000
#define N5 0xFFFF000000000000
#define N6 0xFF00000000000000

#define E1 0x3F3F3F3F3F3F3F3F
#define E2 0x1F1F1F1F1F1F1F1F
#define E3 0x0F0F0F0F0F0F0F0F
#define E4 0x0707070707070707
#define E5 0x0303030303030303
#define E6 0x0101010101010101

#define W1 0xFCFCFCFCFCFCFCFC
#define W2 0xF8F8F8F8F8F8F8F8
#define W3 0xF0F0F0F0F0F0F0F0
#define W4 0xE0E0E0E0E0E0E0E0
#define W5 0xC0C0C0C0C0C0C0C0
#define W6 0x8080808080808080

#define S1 0x0000FFFFFFFFFFFF
#define S2 0x000000FFFFFFFFFF
#define S3 0x00000000FFFFFFFF
#define S4 0x0000000000FFFFFF
#define S5 0x000000000000FFFF
#define S6 0x00000000000000FF

#define NE1 0x3F3F3F3F3F3F0000
#define NE2 0x1F1F1F1F1F000000
#define NE3 0x0F0F0F0F00000000
#define NE4 0x0707070000000000
#define NE5 0x0303000000000000
#define NE6 0x0100000000000000

#define SE1 0x00003F3F3F3F3F3F
#define SE2 0x0000001F1F1F1F1F
#define SE3 0x000000000F0F0F0F
#define SE4 0x0000000000070707
#define SE5 0x0000000000000303
#define SE6 0x0000000000000001

#define SW1 0x0000FCFCFCFCFCFC
#define SW2 0x000000F8F8F8F8F8
#define SW3 0x00000000F0F0F0F0
#define SW4 0x0000000000E0E0E0
#define SW5 0x000000000000C0C0
#define SW6 0x0000000000000080

#define NW1 0xFCFCFCFCFCFC0000
#define NW2 0xF8F8F8F8F8000000
#define NW3 0xF0F0F0F000000000
#define NW4 0xE0E0E00000000000
#define NW5 0xC0C0000000000000
#define NW6 0x8000000000000000


static u64 calculate_flips(u64 m, u64 y, u64 mask);



JNIEXPORT void JNICALL Java_com_fivestar_sirius_Board_doMove(JNIEnv *env, jobject obj, jint move) {
	u64 black;
	u64 white;

	long mask;
	long flips;

	int colorToMove;
	int halfMove;
	int pass;

	// Get a reference to this object's class
	jclass board = (*env)->FindClass(env, "com.fivestar.sirius.Board");

	// ColorToMove
	jfieldID fidColorToMove = (*env)->GetFieldID(env, board, "colorToMove", "I");
	jint ColorToMove = (*env)->GetIntField(env, board, fidColorToMove);
	colorToMove = ColorToMove;

	// black
	jfieldID fidblack = (*env)->GetFieldID(env, board, "black", "L");
	jlong Black = (*env)->GetLongField(env, board, fidblack);
	black = Black;

	// white
	jfieldID fidwhite = (*env)->GetFieldID(env, board, "white", "L");
	jlong White = (*env)->GetLongField(env, board, fidwhite);
	white = White;

	// halfMove
	jfieldID fidhalfMove = (*env)->GetFieldID(env, board, "halfMove", "I");
	jint HalfMove = (*env)->GetIntField(env, board, fidhalfMove);
	halfMove = HalfMove;

	// pass
	jfieldID fidpass = (*env)->GetFieldID(env, board, "pass", "I");
	jint Pass = (*env)->GetIntField(env, board, fidpass);
	pass = Pass;

	mask = 1;
	mask = mask << (move-1);
	if(colorToMove == BLACK) {
		flips = calculate_flips(black, white, mask);
		black |= flips;
		white &= ~flips;
		colorToMove = WHITE;
	} else {
		flips = calculate_flips(white, black, mask);
		white |= flips;
		black &= ~flips;
		colorToMove = BLACK;
	}
	halfMove++;
	pass = 0;

	(*env)->SetLongField(env, board, fidwhite, white);
	(*env)->SetLongField(env, board, fidblack, black);
	(*env)->SetIntField(env, board, fidColorToMove, colorToMove);
	(*env)->SetIntField(env, board, fidhalfMove, halfMove);
	(*env)->SetIntField(env, board, fidpass, pass);
}


/*
static u64 calculate_legal(u64 m, u64 y) {

	u64 me = (u64) m;
	u64 you = (u64) y;

	u64 free;
	u64 value;

	free = ~(me | you);

	value =  (free &
			((N1  & (you << 8) & (me << 16))  |
					(NW1 & (you << 9) & (me << 18))  |
					(W1  & (you << 1) & (me << 2))   |
					(SW1 & (you >> 7) & (me >> 14))  |
					(S1  & (you >> 8) & (me >> 16))  |
					(SE1 & (you >> 9) & (me >> 18))  |
					(E1  & (you >> 1) & (me >> 2))   |
					(NE1 & (you << 7) & (me << 14))));

	value |= (free &
			((N2   & (you << 8) & (you << 16) & (me << 24))  |
					(NW2  & (you << 9) & (you << 18) & (me << 27))  |
					(W2   & (you << 1) & (you << 2)  & (me << 3))   |
					(SW2  & (you >> 7) & (you >> 14) & (me >> 21))  |
					(S2   & (you >> 8) & (you >> 16) & (me >> 24))  |
					(SE2  & (you >> 9) & (you >> 18) & (me >> 27))  |
					(E2   & (you >> 1) & (you >> 2)  & (me >> 3))   |
					(NE2  & (you << 7) & (you << 14) & (me << 21))));

	value |= (free &
			((N3   & (you << 8) & (you << 16) & (you << 24) & (me << 32))  |
					(NW3  & (you << 9) & (you << 18) & (you << 27) & (me << 36))  |
					(W3   & (you << 1) & (you << 2)  & (you << 3)  & (me << 4))   |
					(SW3  & (you >> 7) & (you >> 14) & (you >> 21) & (me >> 28))  |
					(S3   & (you >> 8) & (you >> 16) & (you >> 24) & (me >> 32))  |
					(SE3  & (you >> 9) & (you >> 18) & (you >> 27) & (me >> 36))  |
					(E3   & (you >> 1) & (you >> 2)  & (you >> 3)  & (me >> 4))   |
					(NE3  & (you << 7) & (you << 14) & (you << 21) & (me << 28))));

	value |= (free &
			((N4   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (me << 40))  |
					(NW4  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (me << 45))  |
					(W4   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (me << 5))   |
					(SW4  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (me >> 35))  |
					(S4   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (me >> 40))  |
					(SE4  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (me >> 45))  |
					(E4   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (me >> 5))   |
					(NE4  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (me << 35))));

	value |= (free &
			((N5   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (you << 40) & (me << 48))  |
					(NW5  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (you << 45) & (me << 54))  |
					(W5   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (you << 5)  & (me << 6))   |
					(SW5  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (you >> 35) & (me >> 42))  |
					(S5   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (you >> 40) & (me >> 48))  |
					(SE5  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (you >> 45) & (me >> 54))  |
					(E5   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (you >> 5)  & (me >> 6))   |
					(NE5  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (you << 35) & (me << 42))));

	value |= (free &
			((N6   & (you << 8) & (you << 16) & (you << 24) & (you << 32) & (you << 40) & (you << 48) & (me << 56))  |
					(NW6  & (you << 9) & (you << 18) & (you << 27) & (you << 36) & (you << 45) & (you << 54) & (me << 63))  |
					(W6   & (you << 1) & (you << 2)  & (you << 3)  & (you << 4)  & (you << 5)  & (you << 6)  & (me << 7))   |
					(SW6  & (you >> 7) & (you >> 14) & (you >> 21) & (you >> 28) & (you >> 35) & (you >> 42) & (me >> 49))  |
					(S6   & (you >> 8) & (you >> 16) & (you >> 24) & (you >> 32) & (you >> 40) & (you >> 48) & (me >> 56))  |
					(SE6  & (you >> 9) & (you >> 18) & (you >> 27) & (you >> 36) & (you >> 45) & (you >> 54) & (me >> 63))  |
					(E6   & (you >> 1) & (you >> 2)  & (you >> 3)  & (you >> 4)  & (you >> 5)  & (you >> 6)  & (me >> 7))   |
					(NE6  & (you << 7) & (you << 14) & (you << 21) & (you << 28) & (you << 35) & (you << 42) & (me << 49))));

	return (value);
}
 */

static u64 calculate_flips(u64 m, u64 y, u64 mask) {

	u64 me = (u64) m;
	u64 you = (u64) y;
	u64 tmp;
	register u64 value = mask;

	/* SOUTH */
	tmp = ((mask << 8) & you);
	if(tmp) {
		if((mask & S1) && ((mask << 16) & me)) {
			value |= (mask << 8);
		} else if((mask & S2) &&
				((mask << 16) & you) && ((mask << 24) & me)) {
			value |= ((mask << 8) | (mask << 16));
		} else if((mask & S3) &&
				((mask << 16) & you) && ((mask << 24) & you) &&
				((mask << 32) & me)) {
			value |= ((mask << 8) | (mask << 16) | (mask << 24));
		} else if((mask & S4) &&
				((mask << 16) & you) && ((mask << 24) & you) &&
				((mask << 32) & you) && ((mask << 40) & me)) {
			value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32));
		} else if((mask & S5) &&
				((mask << 16) & you) && ((mask << 24) & you) &&
				((mask << 32) & you) && ((mask << 40) & you) &&
				((mask << 48) & me)) {
			value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32) | (mask << 40));
		} else if((mask & S6) &&
				((mask << 16) & you) && ((mask << 24) & you) &&
				((mask << 32) & you) && ((mask << 40) & you) &&
				((mask << 48) & you) && ((mask << 56) & me)) {
			value |= ((mask << 8) | (mask << 16) | (mask << 24) | (mask << 32) | (mask << 40) | (mask << 48));
		}
	}
	/* SOUTHEAST */
	tmp = ((mask << 9) & you);
	if(tmp) {
		if((mask & SE1) && ((mask << 18) & me)) {
			value |= (mask << 9);
		} else if((mask & SE2) &&
				((mask << 18) & you) && ((mask << 27) & me)) {
			value |= ((mask << 9) | (mask << 18));
		} else if((mask & SE3) &&
				((mask << 18) & you) && ((mask << 27) & you) &&
				((mask << 36) & me)) {
			value |= ((mask << 9) | (mask << 18) | (mask << 27));
		} else if((mask & SE4) &&
				((mask << 18) & you) && ((mask << 27) & you) &&
				((mask << 36) & you) && ((mask << 45) & me)) {
			value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36));
		} else if((mask & SE5) &&
				((mask << 18) & you) && ((mask << 27) & you) &&
				((mask << 36) & you) && ((mask << 45) & you) &&
				((mask << 54) & me)) {
			value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36) | (mask << 45));
		} else if((mask & SE6) &&
				((mask << 18) & you) && ((mask << 27) & you) &&
				((mask << 36) & you) && ((mask << 45) & you) &&
				((mask << 54) & you) && ((mask << 63) & me)) {
			value |= ((mask << 9) | (mask << 18) | (mask << 27) | (mask << 36) | (mask << 45) | (mask << 54));
		}
	}
	/* EAST */
	tmp = ((mask << 1) & you);
	if(tmp) {
		if((mask & E1) && ((mask << 2) & me)) {
			value |= (mask << 1);
		} else if((mask & E2) &&
				((mask << 2) & you) && ((mask << 3) & me)) {
			value |= ((mask << 1) | (mask << 2));
		} else if((mask & E3) &&
				((mask << 2) & you) && ((mask << 3) & you) &&
				((mask << 4) & me)) {
			value |= ((mask << 1) | (mask << 2) | (mask << 3));
		} else if((mask & E4) &&
				((mask << 2) & you) && ((mask << 3) & you) &&
				((mask << 4) & you) && ((mask << 5) & me)) {
			value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4));
		} else if((mask & E5) &&
				((mask << 2) & you) && ((mask << 3) & you) &&
				((mask << 4) & you) && ((mask << 5) & you) &&
				((mask << 6) & me)) {
			value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4) | (mask << 5));
		} else if((mask & E6) &&
				((mask << 2) & you) && ((mask << 3) & you) &&
				((mask << 4) & you) && ((mask << 5) & you) &&
				((mask << 6) & you) && ((mask << 7) & me)) {
			value |= ((mask << 1) | (mask << 2) | (mask << 3) | (mask << 4) | (mask << 5) | (mask << 6));
		}
	}
	/* SOUTHWEST */
	tmp = ((mask << 7) & you);
	if(tmp) {
		if((mask & SW1) && ((mask << 14) & me)) {
			value |= (mask << 7);
		} else if((mask & SW2) &&
				((mask << 14) & you) && ((mask << 21) & me)) {
			value |= ((mask << 7) | (mask << 14));
		} else if((mask & SW3) &&
				((mask << 14) & you) && ((mask << 21) & you) &&
				((mask << 28) & me)) {
			value |= ((mask << 7) | (mask << 14) | (mask << 21));
		} else if((mask & SW4) &&
				((mask << 14) & you) && ((mask << 21) & you) &&
				((mask << 28) & you) && ((mask << 35) & me)) {
			value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28));
		} else if((mask & SW5) &&
				((mask << 14) & you) && ((mask << 21) & you) &&
				((mask << 28) & you) && ((mask << 35) & you) &&
				((mask << 42) & me)) {
			value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28) | (mask << 35));
		} else if((mask & SW6) &&
				((mask << 14) & you) && ((mask << 21) & you) &&
				((mask << 28) & you) && ((mask << 35) & you) &&
				((mask << 42) & you) && ((mask << 49) & me)) {
			value |= ((mask << 7) | (mask << 14) | (mask << 21) | (mask << 28) | (mask << 35) | (mask << 42));
		}
	}
	/* NORTH */
	tmp = ((mask >> 8) & you);
	if(tmp) {
		if((mask & N1) && ((mask >> 16) & me)) {
			value |= (mask >> 8);
		} else if((mask & N2) &&
				((mask >> 16) & you) && ((mask >> 24) & me)) {
			value |= ((mask >> 8) | (mask >> 16));
		} else if((mask & N3) &&
				((mask >> 16) & you) && ((mask >> 24) & you) &&
				((mask >> 32) & me)) {
			value |= ((mask >> 8) | (mask >> 16) | (mask >> 24));
		} else if((mask & N4) &&
				((mask >> 16) & you) && ((mask >> 24) & you) &&
				((mask >> 32) & you) && ((mask >> 40) & me)) {
			value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32));
		} else if((mask & N5) &&
				((mask >> 16) & you) && ((mask >> 24) & you) &&
				((mask >> 32) & you) && ((mask >> 40) & you) &&
				((mask >> 48) & me)) {
			value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32) | (mask >> 40));
		} else if((mask & N6) &&
				((mask >> 16) & you) && ((mask >> 24) & you) &&
				((mask >> 32) & you) && ((mask >> 40) & you) &&
				((mask >> 48) & you) && ((mask >> 56) & me)) {
			value |= ((mask >> 8) | (mask >> 16) | (mask >> 24) | (mask >> 32) | (mask >> 40) | (mask >> 48));
		}
	}
	/* NORTHWEST */
	tmp = ((mask >> 9) & you);
	if(tmp) {
		if((mask & NW1) && ((mask >> 18) & me)) {
			value |= (mask >> 9);
		} else if((mask & NW2) &&
				((mask >> 18) & you) && ((mask >> 27) & me)) {
			value |= ((mask >> 9) | (mask >> 18));
		} else if((mask & NW3) &&
				((mask >> 18) & you) && ((mask >> 27) & you) &&
				((mask >> 36) & me)) {
			value |= ((mask >> 9) | (mask >> 18) | (mask >> 27));
		} else if((mask & NW4) &&
				((mask >> 18) & you) && ((mask >> 27) & you) &&
				((mask >> 36) & you) && ((mask >> 45) & me)) {
			value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36));
		} else if((mask & NW5) &&
				((mask >> 18) & you) && ((mask >> 27) & you) &&
				((mask >> 36) & you) && ((mask >> 45) & you) &&
				((mask >> 54) & me)) {
			value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36) | (mask >> 45));
		} else if((mask & NW6) &&
				((mask >> 18) & you) && ((mask >> 27) & you) &&
				((mask >> 36) & you) && ((mask >> 45) & you) &&
				((mask >> 54) & you) && ((mask >> 63) & me)) {
			value |= ((mask >> 9) | (mask >> 18) | (mask >> 27) | (mask >> 36) | (mask >> 45) | (mask >> 54));
		}
	}
	/* WEST */
	tmp = ((mask >> 1) & you);
	if(tmp) {
		if((mask & W1) && ((mask >> 2) & me)) {
			value |= (mask >> 1);
		} else if((mask & W2) &&
				((mask >> 2) & you) && ((mask >> 3) & me)) {
			value |= ((mask >> 1) | (mask >> 2));
		} else if((mask & W3) &&
				((mask >> 2) & you) && ((mask >> 3) & you) &&
				((mask >> 4) & me)) {
			value |= ((mask >> 1) | (mask >> 2) | (mask >> 3));
		} else if((mask & W4) &&
				((mask >> 2) & you) && ((mask >> 3) & you) &&
				((mask >> 4) & you) && ((mask >> 5) & me)) {
			value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4));
		} else if((mask & W5) &&
				((mask >> 2) & you) && ((mask >> 3) & you) &&
				((mask >> 4) & you) && ((mask >> 5) & you) &&
				((mask >> 6) & me)) {
			value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4) | (mask >> 5));
		} else if((mask & W6) &&
				((mask >> 2) & you) && ((mask >> 3) & you) &&
				((mask >> 4) & you) && ((mask >> 5) & you) &&
				((mask >> 6) & you) && ((mask >> 7) & me)) {
			value |= ((mask >> 1) | (mask >> 2) | (mask >> 3) | (mask >> 4) | (mask >> 5) | (mask >> 6));
		}
	}
	/* NORTHEAST */
	tmp = ((mask >> 7) & you);
	if(tmp) {
		if((mask & NE1) && ((mask >> 14) & me)) {
			value |= (mask >> 7);
		} else if((mask & NE2) &&
				((mask >> 14) & you) && ((mask >> 21) & me)) {
			value |= ((mask >> 7) | (mask >> 14));
		} else if((mask & NE3) &&
				((mask >> 14) & you) && ((mask >> 21) & you) &&
				((mask >> 28) & me)) {
			value |= ((mask >> 7) | (mask >> 14) | (mask >> 21));
		} else if((mask & NE4) &&
				((mask >> 14) & you) && ((mask >> 21) & you) &&
				((mask >> 28) & you) && ((mask >> 35) & me)) {
			value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28));
		} else if((mask & NE5) &&
				((mask >> 14) & you) && ((mask >> 21) & you) &&
				((mask >> 28) & you) && ((mask >> 35) & you) &&
				((mask >> 42) & me)) {
			value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28) | (mask >> 35));
		} else if((mask & NE6) &&
				((mask >> 14) & you) && ((mask >> 21) & you) &&
				((mask >> 28) & you) && ((mask >> 35) & you) &&
				((mask >> 42) & you) && ((mask >> 49) & me)) {
			value |= ((mask >> 7) | (mask >> 14) | (mask >> 21) | (mask >> 28) | (mask >> 35) | (mask >> 42));
		}
	}
	return (value);
}
