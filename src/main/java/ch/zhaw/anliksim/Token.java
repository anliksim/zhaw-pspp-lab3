package ch.zhaw.anliksim;

public class Token {
    public static final int NONE = 0;
    public static final int NUMBER = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int TIMES = 4;
    public static final int SLASH = 5;
    public static final int LBRACK = 6;
    public static final int RBRACK = 7;
    public static final int EQUAL = 8;
    public static final int IDENT = 9;
    public static final int NOT = 10;
    public static final int WHILE = 11;
    public static final int IF = 12;
    public static final int LCBRACK = 13;
    public static final int RCBRACK = 14;
    public static final int SCOLON = 15;
    public static final int ELSE = 16;
    public static final int RETURN = 17;
    public static final int EOF = 18;
    public static String[] names = {
            "none", "number", "+", "-", "*", "/", "(", ")", "=", "ident", "!",
            "while", "if", "{", "}", ";", "else", "return", "eof"};

    public int kind; // token code
    public int pos; // position
    public double val; // for numbers
    public String str; // for numbers and identifiers
}
