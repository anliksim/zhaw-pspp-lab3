package ch.zhaw.anliksim;

import java.util.Hashtable;


public class Scanner {
	public static int pos;
	static String input;
	static char ch;
	static final char EOF = '\u0080';
	static Hashtable<String, Integer> keywords;

	private static void readName(Token t) {
		int state = 0;

		t.kind = Token.IDENT;
		t.str = "";
		for (;;) {
			switch (state) {
			case 0:
				if (ch == '$'|| ch >= 'A' && ch <= 'z' || ch >= '0' && ch <= '9') {
					t.str += ch;
					nextCh();
				} else {
					state = 1;
				}
				break;

			case 1:
				Integer k = (Integer) keywords.get(t.str);

				if (k != null) {
					t.kind = k.intValue();
				}
				return;
			}
		}
	}

	private static void readNumber(Token t) {
		t.kind = Token.NUMBER;
		t.str = "";
		int state = 0;

		for (;;) {
			switch (state) {
			case 0:
				if (ch >= '0' && ch <= '9') {
					t.str += ch;
					nextCh();
				} else {
					state = 1;
				}
				break;

			case 1:
				if (ch == '.') {
					t.str += ch;
					nextCh();
					state = 2;
				} else {
					state = 3;
				}
				break;

			case 2:
				if (ch >= '0' && ch <= '9') {
					t.str += ch;
					nextCh();
				} else {
					state = 3;
				}
				break;

			case 3:
				if (ch == 'E') {
					t.str += ch;
					nextCh();
					state = 4;
				} else {
					state = 5;
				}
				break;

			case 4:
				if (ch >= '0' && ch <= '9') {
					t.str += ch;
					nextCh();
				} else {
					state = 5;
				}
				break;

			case 5:
				t.val = Double.parseDouble(t.str);
				return;
			}
		}
	}

	private static void nextCh() {
		if (pos < input.length()) {
			ch = input.charAt(pos++);
		} else {
			ch = EOF;
		}
	}

	public static void init(String s) {
		input = s;
		pos = 0;
		nextCh();
	}

	public static Token next() {
		while (ch <= ' ') {
			nextCh();
		} // skip blanks, tabs, eols
		Token t = new Token();

		t.pos = pos;
		switch (ch) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			readNumber(t);
			break;
		case '$':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
			readName(t);
			break;

		case '+':
			t.kind = Token.PLUS;
			nextCh();
			break;

		case '-':
			t.kind = Token.MINUS;
			nextCh();
			break;

		case '*':
			t.kind = Token.TIMES;
			nextCh();
			break;

		case '/':
			t.kind = Token.SLASH;
			nextCh();
			break;

		case '(':
			t.kind = Token.LBRACK;
			nextCh();
			break;

		case ')':
			t.kind = Token.RBRACK;
			nextCh();
			break;

		case '{':
			t.kind = Token.LCBRACK;
			nextCh();
			break;

		case '}':
			t.kind = Token.RCBRACK;
			nextCh();
			break;

		case '!':
			t.kind = Token.NOT;
			nextCh();
			break;

		case '=':
			t.kind = Token.EQUAL;
			nextCh();
			break;

		case ';':
			t.kind = Token.SCOLON;
			nextCh();
			break;

		case EOF:
			t.kind = Token.EOF;
			break;

		default:
			t.kind = Token.NONE;
			nextCh();
			break;
		}
		return t;
	}

	// lookahead Methoden
	public static int la;
	public static Token token; // zuletzt erkanntes Token
	public static Token laToken; // lookahead token (noch nicht erkannt

	public static void scan() {
		token = laToken;
		laToken = Scanner.next();
		la = laToken.kind;
	}

	public static void check(int expected) throws Exception {
		if (la == expected) {
			scan();
		} // erkannt, daher weiterlesen
		else {
			error(Token.names[expected] + " expected");
		}
	}

	public static void error(String msg) throws Exception {
		throw new Exception(msg + " at " + Scanner.laToken.pos);
	}

	/* Test */
	public static void main(String[] args) {
		init("if (a) {z = u}");
		Token t = next();

		while (t.kind != Token.EOF) {
			System.out.print(
					"<" + Token.names[t.kind] + ":" + t.val + ":" + t.str + ">");
			t = next();
		}
		System.out.println();
	}

	static {
		keywords = new Hashtable<String, Integer>();
		keywords.put("while", new Integer(Token.WHILE));
		keywords.put("if", new Integer(Token.IF));
		keywords.put("else", new Integer(Token.ELSE));
		keywords.put("return", new Integer(Token.RETURN));
	}
}
