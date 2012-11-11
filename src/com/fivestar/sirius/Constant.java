package com.fivestar.sirius;

public class Constant {
	public static final int BLACK = 1;
	public static final int WHITE = 0;
	public static final int EMPTY = 2;
	
    /* NORTH */
    public static final long N1 = 0xFFFFFFFFFFFF0000L;
    public static final long N2 = 0xFFFFFFFFFF000000L;
    public static final long N3 = 0xFFFFFFFF00000000L;
    public static final long N4 = 0xFFFFFF0000000000L;
    public static final long N5 = 0xFFFF000000000000L;
    public static final long N6 = 0xFF00000000000000L;

    /* EAST */       
    public static final long E1 = 0x3F3F3F3F3F3F3F3FL;
    public static final long E2 = 0x1F1F1F1F1F1F1F1FL;
    public static final long E3 = 0x0F0F0F0F0F0F0F0FL;
    public static final long E4 = 0x0707070707070707L;
    public static final long E5 = 0x0303030303030303L;
    public static final long E6 = 0x0101010101010101L;

    /* WEST */
    public static final long W1 = 0xFCFCFCFCFCFCFCFCL;
    public static final long W2 = 0xF8F8F8F8F8F8F8F8L;
    public static final long W3 = 0xF0F0F0F0F0F0F0F0L;
    public static final long W4 = 0xE0E0E0E0E0E0E0E0L;
    public static final long W5 = 0xC0C0C0C0C0C0C0C0L;
    public static final long W6 = 0x8080808080808080L;

    /* SOUTH */
    public static final long S1 = 0x0000FFFFFFFFFFFFL;
    public static final long S2 = 0x000000FFFFFFFFFFL;
    public static final long S3 = 0x00000000FFFFFFFFL;
    public static final long S4 = 0x0000000000FFFFFFL;
    public static final long S5 = 0x000000000000FFFFL;
    public static final long S6 = 0x00000000000000FFL;

    /* NORTH EAST */
    public static final long NE1 = 0x3F3F3F3F3F3F0000L;
    public static final long NE2 = 0x1F1F1F1F1F000000L;
    public static final long NE3 = 0x0F0F0F0F00000000L;
    public static final long NE4 = 0x0707070000000000L;
    public static final long NE5 = 0x0303000000000000L;
    public static final long NE6 = 0x0100000000000000L;

    /*SOUTH EAST */
    public static final long SE1 = 0x00003F3F3F3F3F3FL;
    public static final long SE2 = 0x0000001F1F1F1F1FL;
    public static final long SE3 = 0x000000000F0F0F0FL;
    public static final long SE4 = 0x0000000000070707L;
    public static final long SE5 = 0x0000000000000303L;
    public static final long SE6 = 0x0000000000000001L;

    /* SOUTH WEST */
    public static final long SW1 = 0x0000FCFCFCFCFCFCL;
    public static final long SW2 = 0x000000F8F8F8F8F8L;
    public static final long SW3 = 0x00000000F0F0F0F0L;
    public static final long SW4 = 0x0000000000E0E0E0L;
    public static final long SW5 = 0x000000000000C0C0L;
    public static final long SW6 = 0x0000000000000080L;
	
    /* NORTH WEST */
    public static final long NW1 = 0xFCFCFCFCFCFC0000L;
    public static final long NW2 = 0xF8F8F8F8F8000000L;
    public static final long NW3 = 0xF0F0F0F000000000L;
    public static final long NW4 = 0xE0E0E00000000000L;
    public static final long NW5 = 0xC0C0000000000000L;
    public static final long NW6 = 0x8000000000000000L;

}