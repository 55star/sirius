//
//package com.fivestar.sirius;
//
//import java.io.BufferedInputStream;
//import java.io.DataInputStream;
//import java.io.EOFException;
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.Hashtable;
//
//
///*
//#define E(x)   ((x >> 1) & E1)
//#define W(x)   ((x << 1) & W1)
//#define N(x)   ((x << 8) & N1)
//#define S(x)   ((x >> 8) & S1) 
//#define SE(x)  ((x >> 9) & SE1)
//#define SW(x)  ((x >> 7) & SW1)
//#define NE(x)  ((x << 7) & NE1)
//#define NW(x)  ((x << 9) & NW1)
// */
//
//
//
//class Evaluation {
//	final static long CORNER1 = 0x0000000000070707L;
//	final static long CORNER2 = 0x0707070000000000L;
//	final static long CORNER3 = 0xE0E0E00000000000L;
//	final static long CORNER4 = 0x0000000000E0E0E0L;
//
//	final static long NEDGE1 = 0x00000000000000FFL;
//	final static long SEDGE1 = 0xFF00000000000000L;
//	final static long EEDGE1 = 0x8080808080808080L;
//	final static long WEDGE1 = 0x0101010101010101L;
//
//	final static long POTMOB = 0x003c7e7e7e7e3c00L;
//
//	final static long N1 = 0xFFFFFFFFFFFF0000L;
//	final static long E1 = 0x3F3F3F3F3F3F3F3FL;
//	final static long W1 = 0xFCFCFCFCFCFCFCFCL;
//	final static long S1 = 0x0000FFFFFFFFFFFFL;
//	final static long NE1 = 0x3F3F3F3F3F3F0000L;
//	final static long SE1 = 0x00003F3F3F3F3F3FL;
//	final static long SW1 = 0x0000FCFCFCFCFCFCL;
//	final static long NW1 = 0xFCFCFCFCFCFC0000L;
//
//	private float[] mobility_coef = {0.088947f, 0.20782f, 0.40319f, 0.433412f, 1.0798f, 2.3535f, 3.0989f, 4.3f};
//	private float[] edge_coef     = {1.1315185f, 0.807301f, 0.406408f, 0.5252555f, 0.87827f, 1.18325f, 0.92189f, 0.9f};
//	private float[] corner_coef   = {1.1643080f, 0.935728f, 0.948664f, 1.0113549f, 1.05460f, 1.11807f, 0.97812f, 0.9f};
//
//	final static String TAG = "Evaluation.java";
//
//	Hashtable edge1x = new Hashtable();
//	Hashtable corner3x3 = new Hashtable();
//
//
//	public int evaluate(Board b) {
//		float value;
//		int stage = (int)((b.halfMove - 10) / 6);
//
//		value  = get_corner(b, stage) * corner_coef[stage];
//		value += get_edge1x(b, stage) * edge_coef[stage];
//		value += get_mobility(b) * mobility_coef[stage];
//		value += get_potential_mobility(b) * 10;
//		if(stage == 8) {
//			value += get_parity(b) * 600;
//		}
//
//		return (int)(Math.round(value));
//	}
//
//	private float get_corner(Board b, int stage) {
//		float value;
//
//		value  = hash_table_find(corner3x3, (b.black & CORNER1), (b.white & CORNER1), stage);
//		value += hash_table_find(corner3x3, (b.black & CORNER2), (b.white & CORNER2), stage);
//		value += hash_table_find(corner3x3, (b.black & CORNER3), (b.white & CORNER3), stage);
//		value += hash_table_find(corner3x3, (b.black & CORNER4), (b.white & CORNER4), stage);
//
//		return (value);
//	}
//
//	private float get_edge1x(Board b, int stage) {
//		float value;
//
//		value  = hash_table_find(edge1x, (b.black & NEDGE1), (b.white & NEDGE1), stage);
//		value += hash_table_find(edge1x, (b.black & SEDGE1), (b.white & SEDGE1), stage);
//		value += hash_table_find(edge1x, (b.black & EEDGE1), (b.white & EEDGE1), stage);
//		value += hash_table_find(edge1x, (b.black & WEDGE1), (b.white & WEDGE1), stage);
//
//		return (value);
//	}
//
//	public int get_mobility(Board b) {
//		int value;
//
//		if(b.color_to_move == Constant.BLACK) {
//			b.color_to_move = Constant.WHITE;
//			mobility(b);
//			value = -(b.legal_move.size());
//
//			b.color_to_move = Constant.BLACK;
//			mobility(b);
//			value += b.legal_move.size();
//		} else {
//			b.color_to_move = Constant.BLACK;
//			mobility(b);
//			value = b.legal_move.size();
//
//			b.color_to_move = Constant.WHITE;
//			mobility(b);
//			value -= b.legal_move.size();
//		}
//
//		return (-value);
//	}
//
//	public int get_potential_mobility(Board b) {
//		long black;
//		long white;
//		long empty;
//
//		empty = ((~(b.black | b.white)) & POTMOB);
//
//		if(empty == 0) {
//			return (0);
//		}
//
//		black = (empty & (N(b.black)  | E(b.black)  | S(b.black)  | W(b.black)  |
//				NE(b.black) | SE(b.black) | SW(b.black) | NW(b.black)));
//
//		white = (empty & (N(b.white)  | E(b.white)  | S(b.white)  | W(b.white)  |
//				NE(b.white) | SE(b.white) | SW(b.white) | NW(b.white)));
//
//		return (numbits(white) - numbits(black));
//	}
//
//	private int get_parity(Board b) {
//		if(((b.halfMove % 2 == 0) && (b.color_to_move == Constants.BLACK)) || 
//				((b.halfMove % 2 != 0) && (b.color_to_move == Constants.WHITE))) {
//			return (1);
//		} else {
//			return (-1);
//		}
//	}
//
//
//
//	public void init_evaluation(char corner, char edge) {
//		Utils.log(TAG,"Loading eval tables...");
//
//		corner3x3 = load(corner, 18, 4);  /* 26444 - 19614 18<->265000 */
//		edge1x = load(edge, 17, 4);      /* 19382 - 13930 */
//
//		Utils.log(TAG,"done!");
//	}
//
//
//	private Hashtable load(String filename, int size, int n) {
//		Hashtable table;
//		int stage;
//		int num;
//		long black;
//		long white;
//		float eval;
//
//		File inputFile = new File(filename);
//		FileInputStream fis = new FileInputStream(inputFile);
//		BufferedInputStream bis =  new BufferedInputStream(fis);
//		DataInputStream dis = new DataInputStream(bis);
//
//		num = dis.readInt();
//
//		for(int i; i<num; i++) {
//			black = 0;
//			white = 0;
//			stage = 0;
//			eval = 0;
//			try {
//				black = dis.readLong();
//				white = dis.readLong();
//				stage = dis.readInt();
//				eval = dis.readFloat();
//			}
//			catch(EOFException eof) {
//				Utils.log(TAG,"End of File");
//				break;
//			}
//
//			for(int j=0; j<n; j++) {
//				hash_table_add(table, black, white, stage, eval);
//
//				black = rotate_bit_pattern(black);
//				white = rotate_bit_pattern(white);
//			}
//
//		}
//
//		for(j=0; j<n; j++) {
//			hash_table_add(table, black, white, stage, eval);
//
//			black = rotate_bit_pattern(black);
//			white = rotate_bit_pattern(white);
//		}
//	}
//	fclose(inputfile);
//} else {
//	printf("File not found: %s\n", filename);
//	exit(1);
//}
//
//return (table);
//
//
//
//if((inputfile = fopen(filename, "rb")) != NULL) {
//	int num;
//
//	table = hash_table_create(size);
//
//	fread(&num, 1, sizeof(int), inputfile);
//
//	#ifdef DEBUG
//	printf("  %s including %d patterns\n", filename, num);
//	#endif
//
//	for(i=0; i<num; i++) {
//		u64 black,white;
//		int stage,j;
//		float eval;
//
//		fread(&black, 1, sizeof(u64), inputfile);
//		fread(&white, 1, sizeof(u64), inputfile);
//		fread(&stage, 1, sizeof(int), inputfile);
//		fread(&eval,  1, sizeof(float), inputfile);
//
//		for(j=0; j<n; j++) {
//			hash_table_add(table, black, white, stage, eval);
//
//			black = rotate_bit_pattern(black);
//			white = rotate_bit_pattern(white);
//		}
//	}
//	fclose(inputfile);
//} else {
//	printf("File not found: %s\n", filename);
//	exit(1);
//}
//
//return (table);
//}
//}
