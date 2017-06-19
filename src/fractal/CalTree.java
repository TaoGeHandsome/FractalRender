package fractal;

public class CalTree {
	/* Constructors */
	public CalTree(){
		
	}

	
	/* Public Methods */
	public String toString(){
		return root.toString();
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
	 *  0 for VAR, 1 for NUM and 2 for CalTree
	 */
	
	private CalElem root;
	
	/* Test Code */
	public static void main(String[] args){
//		CalElem a = new Number(2);
//		CalElem b = new Number(3);
//		CalStruct s = new CalStruct(b, CalOP.ADD, a);
//		System.out.println(s);
//		System.out.println(s.value());
	}
}
