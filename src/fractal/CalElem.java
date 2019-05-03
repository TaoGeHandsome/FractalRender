package fractal;

public class CalElem {
	public static final int TK_NULL = 0;
	public static final int TK_COM = 1;
	public static final int TK_VAR = 2;
	public static final int TK_TREE = 3;
	
	public static final int OP_ADD = 4;
	public static final int OP_SUB = 5;
	public static final int OP_MUL = 6;
	public static final int OP_DIV = 7;
	public static final int OP_POW = 8;

	
	public static final String[] TK_DESCRIBE = new String[]{
			"", "NUM", "z", "TREE", "+", "-", "*", "/", "^", "ERR"
	};
	
	public CalElem(){
		ltag = CalElem.TK_NULL;
		rtag = CalElem.TK_NULL;
	}
	
	public CalElem addLChild(){
		CalElem node = new CalElem();
		this.ltree = node;
		this.ltag = CalElem.TK_TREE;
		return node;
	}
	
	public CalElem addRChild(){
		CalElem node = new CalElem();
		this.rtree = node;
		this.rtag = CalElem.TK_TREE;
		return node;
	}

	public CalElem addLParent(){
		CalElem node = new CalElem();
		node.ltree = this;
		node.ltag = CalElem.TK_TREE;
		return node;
	}

	public void setLValue(Complex val){
		ltag = CalElem.TK_COM;
		lvalue = val;
	}
	
	public void setLValue(String src){
		if (src.equals("z")){
			ltag = CalElem.TK_VAR;
		}else{
			ltag = CalElem.TK_COM;
			lvalue = new Complex(src);
		}
	}

	public void setRValue(Complex val){
		rtag = CalElem.TK_COM;
		rvalue = val;
	}
	
	public void setRValue(String src){
		if (src.equals("z")){
			rtag = CalElem.TK_VAR;
		}else{
			rtag = CalElem.TK_COM;
			rvalue = new Complex(src);
		}
	}
	
	public void setLChild(CalElem e){
		ltag = CalElem.TK_TREE;
		ltree = e;
	}
	
	public void setRChild(CalElem e){
		rtag = CalElem.TK_TREE;
		rtree = e;
	}
	
	public void setOP(char src){
		if (src == '+'){
			op = CalElem.OP_ADD;
		}else if (src == '-'){
			op = CalElem.OP_SUB;
		}else if (src == '*'){
			op = CalElem.OP_MUL;
		}else if (src == '/'){
			op = CalElem.OP_DIV;
		}else if (src == '^'){
			op = CalElem.OP_POW;
		}
	}
	
	
	public char getOP(){
		if (op == CalElem.OP_ADD){
			return '+';
		} else if (op == CalElem.OP_SUB){
			return '-';
		} else if (op == CalElem.OP_MUL){
			return '*';
		} else if (op == CalElem.OP_DIV){
			return '/';
		} else if (op == CalElem.OP_POW){
			return '^';
		}
		return ' ';
	}
	
	public Complex getLValue(){
		return lvalue;
	}
	
	public Complex getRValue(){
		return rvalue;
	}
	
	public CalElem getLTree(){
		return ltree;
	}
	
	public CalElem getRTree(){
		return rtree;
	} 
	
	public int getLTag(){
		return ltag;
	}
	
	public int getRTag(){
		return rtag;
	}
	
	private static double parseDouble(String src) throws NumberFormatException{
		try{
			return Double.parseDouble(src);
		}catch (NumberFormatException e){
			return Double.NaN;
//			throw e;
		}
	}
	
	/**
	 * Methods for calculating value of this node.
	 * @param z
	 * @return valueof this tree.
	 */
	public Complex valueOf(Complex z) throws Exception{
		Complex lval = ltag == TK_COM ? lvalue : ltag == TK_VAR ? z : ltree.valueOf(z);
		Complex rval = rtag == TK_COM ? rvalue : rtag == TK_VAR ? z : 
				rtag == TK_NULL ? null : rtree.valueOf(z);
//		System.out.print("Left: " + ltag + "," + lval);
//		System.out.print("      Right: " + rtag + "," + rval + "\n");
		
		if (rval == null) return lval;
		if (op == OP_ADD)
			return lval.add(rval);
		else if (op == OP_SUB)
			return lval.sub(rval);
		else if (op == OP_MUL)
			return lval.mul(rval);
		else if (op == OP_DIV)
			return lval.div(rval);
		else if (op == OP_POW)
			return lval.pow(rval);
		else 
			return null;
	}
	
	/**
	 * For displaying string of this node.
	 * @return string of this node.
	 */
	public String toString(){
		String ls = ltag == TK_COM ? lvalue.toString() : 
			ltag == TK_TREE ? ltree.toString() : TK_DESCRIBE[ltag]; 
		String rs = rtag == TK_COM ? rvalue.toString() : 
			rtag == TK_TREE ? rtree.toString() : TK_DESCRIBE[rtag]; 
		return "(" + ls + TK_DESCRIBE[op] + rs + ")";
	}
	
	/* Private Variables */
	
	/*  [Calculate Structure]
	 * +------+--------+----+--------+------+
	 * |      | lvalue |    | rvalue |      |
	 * | ltag |        | op |        | rtag |
	 * |      |  lcal  |    |  rcal  |      |
	 * +------+--------+----+--------+------+
	 *
	 *  [Tag]
	 *  0 for NULL(only rtag), 1 for NUM, 2 for VAR and 3 for CalTree
	 */
	
	private int ltag;
	private Complex lvalue;
	private CalElem ltree;
	private int op;
	private int rtag;
	private CalElem rtree;
	private Complex rvalue;
	
	public static void main(String[] args){
		CalElem root = new CalElem();
		CalElem b = root.addLChild();
		CalElem a = b.addRChild();
		root.setRValue("3");
		root.setOP('+');
		b.setLValue("z");
		b.setOP('*');
		a.setLValue("5");
		a.setOP('+');
		a.setRValue("z");
		System.out.println(root);
		try{
			System.out.println(root.valueOf(new Complex("1")));
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
//		CalElem b = new Number(3);
//		CalStruct s = new CalStruct(b, CalOP.ADD, a);
//		System.out.println(s);
//		System.out.println(s.value());
	}
}
