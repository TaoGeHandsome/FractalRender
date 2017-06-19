package fractal;

public class CalElem {
	public static final int TK_VAR = 0;
	public static final int TK_NUM = 1;
	public static final int TK_TREE = 2;
	public static final int TK_LP = 3;
	public static final int TK_RP = 4;
	public static final int TK_PLUS = 5;
	public static final int TK_MINUS = 6;
	public static final int TK_MUL = 7;
	public static final int TK_DIV = 8;
	public static final int TK_POW = 9;
	public static final int TK_ERR = 10;
	
	public static final String[] TK_DESCRIBE = new String[]{
			"X", "NUM", "TREE", "(", ")", "+", "-", "*", "/", "^", "ERR"
	};
	
	public int getOP(){
		return op;
	}
	
	public String toString(){
		if (op == CalElem.TK_ERR) return "Error!";
		String ls = ltag == TK_NUM ? Double.toString(lvalue) : 
			ltag == TK_TREE ? ltree.toString() : TK_DESCRIBE[ltag]; 
		String rs = rtag == TK_NUM ? Double.toString(rvalue) : 
			rtag == TK_TREE ? rtree.toString() : TK_DESCRIBE[rtag]; 
		return ls + op + rs;
	}
	
	private int ltag;
	private double lvalue;
	private CalTree ltree;
	private int op;
	private int rtag;
	private CalTree rtree;
	private double rvalue;
}
