package fractal;

import java.util.Arrays;
import java.util.Stack;

public class CalTree {
	/* Constructors */
	public CalTree(String str) throws Exception{
		checkExp(str);
	
//		System.out.println("Preprocessed:\t\t"+preProcess(str));
		parseCalTree(preProcess(str));
	}
	
	
	
	private void parseCalTree(String src){
		char[] s = src.toLowerCase().toCharArray();
		CalElem cur = new CalElem();
		Stack<CalElem> s_cal = new Stack<CalElem>();
		s_cal.push(cur);
		for (int i = 0; i < s.length; i++){
			if (s[i] == '('){
				cur = cur.getLTag()==CalElem.TK_NULL ? cur.addLChild(): cur.addRChild();
				s_cal.push(cur);
			}else if (s[i] == ')'){
				s_cal.pop();
				cur = s_cal.peek();
			}else if (s[i] == '+' || s[i] == '-' || s[i] == '*' || s[i] == '/' || s[i] == '^'){
				if (cur.getRTag()!=CalElem.TK_NULL){
					if (getOrder(cur.getOP(), s[i])<0){
						cur = s_cal.pop();
						cur = cur.addLParent();
						if (!s_cal.isEmpty()){
							CalElem temp = s_cal.peek();
							if (temp.getLTag() == CalElem.TK_TREE && temp.getLTree() == cur.getLTree()){
								temp.setLChild(cur);
							}else{
								temp.setRChild(cur);
							}
						}
					}else{
						if (cur.getRTag() == CalElem.TK_COM){
							Complex val = cur.getRValue();
							cur = cur.addRChild();
							cur.setLValue(val);
						}else{
							cur = cur.addRChild();
							cur.setLValue("z");
						}
					}
					s_cal.push(cur);
				}
				cur.setOP(s[i]);
			}else if (s[i] == 'z'){
				if (cur.getLTag()==CalElem.TK_NULL)
					cur.setLValue("z");
				else
					cur.setRValue("z");
			}else{	// Numbers
				String s_num = "";
				s_num += s[i++];
				while(true){
					if (s[i-1] == 'e' && s[i]=='-' && s[i+1]>='0' && s[i+1]<='9'){
						s_num += s[i++];
						continue;
					}
					if (i < s.length && ((s[i]>='0' && s[i]<='9') || s[i] == 'e' || s[i] == '.' || s[i] == 'i'))
						s_num += s[i++];
					else break;
				}
//				System.out.println(s_num);
				i--;
				if (cur.getLTag()==CalElem.TK_NULL)
					cur.setLValue(s_num);
				else
					cur.setRValue(s_num);
			}
//			printStack(s_cal);
		}
		while(!s_cal.isEmpty())
			root = s_cal.pop();
	}

	private static int getOrder(char op1, char op2){
		if (op1 == op2) return op1 == '^' ? 1: -1;
		int order1 = 0, order2 = 0;
		
		if (op1 == '+'||op1 == '-') order1 = 3;
		else if (op1 == '*'||op1 == '/') order1 = 2;
		else order1 = 1;
		
		if (op2 == '+'||op2 == '-') order2 = 3;
		else if (op2 == '*'||op2 == '/') order2 = 2;
		else order2 = 1;
		
		return (order1 == order2) ? -1 : order1 - order2;
	}
	
	private String preProcess(String str){
		char[] src = str.toCharArray();
		
		String tp_str = new String();
		int i = 0;
		if(src[i] == '-')
			tp_str += '0';
		
		while (i < str.length()-1){
			tp_str += src[i];
			if (InOP(src[i])){
				/*      ")(" -> ")*("  */
				/*  ")<num>" -> ")*<num>"  */
				if(src[i] == ')' && ((src[i+1] >= '0' && src[i+1] <= '9') 
						|| src[i+1]=='(' || src[i+1]=='i' || src[i+1]=='z')){
					tp_str += '*';
					if (src[i+1]=='i') tp_str += '1';
				}
				
				/* "(-" -> "(0-)" */
				if(src[i] == '(' && (src[i+1] == '-' || src[i+1] == ')'))
					tp_str += '0';
				
				/* "[+,-,*,/]i" */
				if ((src[i]=='+' || src[i]=='-' || src[i]=='*' || src[i]=='/' || src[i]=='^') && 
						src[i+1] == 'i')
					tp_str += '1';
			}else
				/* "<num>(" -> "<num>*(" */
				if (src[i+1] == '(' || src[i+1] == 'i' || src[i+1] == 'z'){
					tp_str += '*';
					if (src[i+1]=='i') tp_str += '1';
				}
			i++;
		}
		tp_str += src[i];
		return tp_str;
	}
	
	/* Check expression */
	private static final int NUM_MAX = 10;		/* Max digits */
	private static final int EXP_MAX = 200;				/* Max exp digits */
	private static final char[] OP_SET = {'+','-','*','/','(',')','^'};		/* Operators */
	private static final char[] VALID_SET = {'0','1','2','3','4','5','6','7','8','9','i','z','^', 'e',
									 '.','+','-','*','/','(',')'};	/* Valid chars */
	private boolean InOP(char x){
		int pos = 0, i;
		for (i = 0; i < OP_SET.length; i++)
			if (OP_SET[pos++] == x) return true;
		return false;
	}
	
	private static enum CHK_MODE {NUM_MODE, OP_MODE};
	private static enum ERR_MSG  {	/* Error Type */
		RIGHT,				/* No error */
		INVALID_CHAR,		/* Invalid characters */
		INVALID_DOT,		/* Invalid dot like  ..	.<num>.	*/
		LOST_CLOSE_BRACKET,	/* No open bracket */
		LOST_OPEN_BRACKET,	/* No close bracket */
		LOST_OPND,			/* No operand  [+,-,*,/][),+,*,/] (+   (*   (/ */
		LOST_OPND_MINUS,	/* No operator [+,-,*,/]- */
		NUM_OVERFLOW,		/* Number is too long */
		UNEXPECTED_DOT,		/* Unexpected dot  .<op> */
		DIV_ZERO			/* Divided by zero  .<op> */
	};
	
	private void checkExp(String str) throws Exception{
		if (str.trim().length()==0) throw new Exception("Requires expression.");
		int spos = 0;

		CHK_MODE chk_mode = CHK_MODE.OP_MODE;
		int chk_bracket = 0; /* bracket fitting */
		int first_num_pos = 0; /* Number overflow */
		int chk_dot = 0;	/* numbers of dot */
		int chk_i = 0;
		int first_i_pos = 0;
		int chk_e = 0;
		int first_e_pos = 0;
		
		/* Lost operand at the end of string */
		if (str.charAt(str.length()-1)=='+' || str.charAt(str.length()-1)=='-' || 
				str.charAt(str.length()-1)=='*' || str.charAt(str.length()-1)=='/' || str.charAt(str.length()-1)=='^'){
			throw new Exception(errorProcess(ERR_MSG.LOST_OPND, str.length()));
		}
		
		/* Lost operand at the start of string */
		if (str.charAt(0)=='+' || str.charAt(0)=='*' || str.charAt(0)=='/' || str.charAt(0)=='^'){
			throw new Exception(errorProcess(ERR_MSG.LOST_OPND, -1));
		}
		if (str.startsWith("e"))
			throw new Exception(errorProcess("Cannot start with e.", -1));
		
		char[] src = Arrays.copyOf(str.toCharArray(), str.length()+1);
		
		while (spos < str.length()){
			/* Check invalid char */
			int valid_char_found = 0;
			for(int i = 0; i < VALID_SET.length; i++)
				if(VALID_SET[i] == src[spos])
					valid_char_found = 1;
			if(valid_char_found == 0)
				throw new Exception(errorProcess(ERR_MSG.INVALID_CHAR, spos));
			
			if(!InOP(src[spos])){	// Operands
				/* Records first num's pos */
				if(chk_mode == CHK_MODE.OP_MODE)
					first_num_pos = spos;
				chk_mode = CHK_MODE.NUM_MODE;
				if (src[spos] == '.'){
					if (src[spos+1]<'0' || src[spos+1]>'9')
						/* .[<op>,i,e,z] */
						throw new Exception(errorProcess(ERR_MSG.UNEXPECTED_DOT, spos));
					chk_dot++;
				}else if(src[spos]=='i'){
					chk_i++;
					if (chk_i == 1)
						first_i_pos = spos;
				}else if(src[spos]=='z'){
					if((src[spos+1]>='0'&&src[spos+1]<='9')||src[spos+1]=='z'||src[spos+1]=='i')
						throw new Exception(errorProcess(ERR_MSG.LOST_OPND, ++spos));
				}else if(src[spos]=='e'){
					chk_e++;
					if (chk_e == 1)
						first_e_pos = spos;
					/* <num>e[<op>,i,e,z,.] */
					if((src[spos+1]<'0' || src[spos+1]>'9') && src[spos+1]!='-')
						throw new Exception(errorProcess("", spos));
					/* <num>e-[<op>,i,e,z,.] */
					if(src[spos+1]=='-' && (src[spos+2]<'0'&&src[spos+2]>'9'))
						throw new Exception(errorProcess("", spos));
				}else{		// Numeric
					// pass
				}
			}
			else{
				chk_mode = CHK_MODE.OP_MODE;
				first_num_pos = spos;
				chk_dot = 0;
				chk_i = 0;
				chk_e = 0;
				if (src[spos] == ')'){
					chk_bracket--;
				} else if (src[spos] == '('){
						/* (+   (*   (/ */
						if (spos < str.length()-1)
							if (InOP(src[spos+1]) && (src[spos+1]=='+'||src[spos+1]=='*'||src[spos+1]=='/'))
								throw new Exception(errorProcess(ERR_MSG.LOST_OPND, ++spos));
						chk_bracket++;
				} else {
					if (spos < str.length()-1)
						if ((InOP(src[spos+1])||src[spos+1]==0)&&(src[spos+1]!='('))
							/* [+,-,*,/][),+,-,*,/] */
							if (src[spos+1] == '-')
								throw new Exception(errorProcess(ERR_MSG.LOST_OPND_MINUS, ++spos));
							else throw new Exception(errorProcess(ERR_MSG.LOST_OPND, ++spos));
				}
				if (src[spos+1] == 'e')
					throw new Exception(errorProcess("Unexpected e.", ++spos));
			}
			/* ()) */
			if (chk_bracket < 0)
				throw new Exception(errorProcess(ERR_MSG.LOST_OPEN_BRACKET, spos));
			
			/* ..	.<num>. */
			if (chk_dot > 1)
				throw new Exception(errorProcess(ERR_MSG.INVALID_DOT, spos));
			
			/*  <num>i<num>i*/
			if (chk_i > 1)
				throw new Exception(errorProcess("Error use for i.", first_i_pos+1));
			
			/* <num>e<num>e<num> */
			if (chk_e > 1)
				throw new Exception(errorProcess("Error use for e.", first_e_pos+1));
			
			/* Number overflow */
			if (spos - first_num_pos >= NUM_MAX)
				throw new Exception(errorProcess(ERR_MSG.NUM_OVERFLOW, first_num_pos));
			if (spos > str.length()) break;
				
		spos++;	
		}
		if (chk_bracket > 0 && chk_dot <= 1)
			/* (() */
			throw new Exception(errorProcess(ERR_MSG.LOST_CLOSE_BRACKET, spos));
	}
	
	/* Public Methods */
	public String toString(){
		return root.toString();
	}
	
	public Complex valueOf(Complex z) throws Exception{
		return root.valueOf(z);
	}
	
	private String errorProcess(String str, int pos){
		return (pos >= 0 ? "At char[" + pos + "]: " : "Before char[0]: ") + str;
	}
	
	private String errorProcess(ERR_MSG err, int pos){
		if (err == ERR_MSG.RIGHT)
			return "";
		else{
			String err_str = new String("");
			if (err != ERR_MSG.DIV_ZERO){
				if (pos >= 0)
					err_str += "At char[" + pos + "]: ";
				else
					err_str += "Before char[0]: ";
			}
			switch (err){
				case INVALID_CHAR:
					err_str += "Invalid char.";
					break;
				case INVALID_DOT:
					err_str += "Invalid not.";
					break;
				case LOST_CLOSE_BRACKET:
					err_str += "Lost close bracket.";
					break;
				case LOST_OPEN_BRACKET:
					err_str += "Right bracket not fitted.";
					break;
				case LOST_OPND:
					err_str += "Lost operand.";
					break;
				case LOST_OPND_MINUS:
					err_str += "Lost operand. (You may need a pair of brackets)";
					break;
				case NUM_OVERFLOW:
					err_str += "Number overflow.";
					break;
				case UNEXPECTED_DOT:
					err_str += "Invalid dot. (You may need add a zero)";
					break;
				case DIV_ZERO:
					err_str += "Divided by zero.";
					break;
				default:
					break;
			}// switch
			return err_str;
		}// if
	}// errorProcess
	
	private CalElem root;
	
	
	// Debug
	private void printStack(Stack<CalElem> s){
		for (int i =0; i< s.size(); i++)
			System.out.println(i+"\t"+s.elementAt(i).hashCode());
		System.out.println("");
	}
	
	/* Test Code */
	public static void main(String[] args){
//		String exp = "-z(-1+i)z";
		String exp = "2.73^iz";
		CalTree t = null;
		try {
			t = new CalTree(exp);
		} catch (Exception e) {
			String error_content = "Your expression:\n"+
					exp + "\nhas errors:\n" + e.getMessage() + "\nplease check it!";
			e.printStackTrace();
			System.out.println(error_content);
			System.exit(0);
		}
		
		try{
			System.out.println("Assigned with 2i:\t" + t.valueOf(new Complex("2i")));
		}catch (Exception e){
			System.out.println("Error catched: " + e.getMessage());
			
		}
		System.out.println("CalTree:\t\t" + t);
	}
}
