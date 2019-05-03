package fractal;
/**
 * Complex library.
 * @version 1.0
 * @author TGH
 *
 */
public class Complex implements Cloneable{

	public Complex(double real, double imag){
		this.real = real;
		this.imag = imag;
	}
	
	public Complex(String src){
		if (src.endsWith("i")){
			real = 0;
			imag = Double.parseDouble(src.substring(0, src.length()-1));
		}else{
			real = Double.parseDouble(src);
			imag = 0;
		}
	}
	
	public String toString(){
		String re = new String();
		String im = new String();
		if (real != 0){
			re = removeZero(real);
			im = (imag == 0)?(""):((imag > 0)?((imag == 1)?("+i"):('+' + removeZero(imag) + 'i')):((imag == -1)?("-i"):(removeZero(imag) + 'i')));
		}
		else {
			re = (imag == 0)?("0"):("");
			im = (imag == 0)?(""):((imag > 0)?((imag == 1)?("i"):(removeZero(imag) + 'i')):((imag == -1)?("-i"):(removeZero(imag) + 'i')));
		}
		return re + im;
	}
	
	public Complex add(Complex c){
		return new Complex(real + c.real, imag + c.imag);
	}
	
	public Complex sub(Complex c){
		return new Complex(real - c.real, imag - c.imag);
	}
	
	public Complex mul(Complex c){
		return new Complex(real * c.real - imag * c.imag,
				imag * c.real + real * c.imag);
	}
	
	public Complex div(Complex c) throws Exception{
		Complex res = new Complex(0, 0);
		if (c.real == 0 && c.imag == 0)
			throw new Exception("Divided by zero");
		res.real = (real * c.real + imag * c.imag) / (c.real * c.real + c.imag * c.imag);
		res.imag = (imag * c.real - real * c.imag) / (c.real * c.real + c.imag * c.imag);
		return res;
	}
	
	public Complex pow(Complex c) throws Exception{
		if (c.imag != 0 || ((int)c.real+0.0 != c.real))
			throw new Exception("Powered by a non-int number.");
		Complex res = new Complex(1, 0);
		for (int i = 0; i < ((int) c.real); i++){
			res = res.mul(this);
		}
		return res;
	}
	
	private static String removeZero(double num){
		String str = Double.toString(num);
		if (str.substring(str.length() - 2, str.length()).equals(".0"))
			str = str.substring(0, str.length() - 2);
		return str;
	}
	
	public Complex clone() throws CloneNotSupportedException
	{
		Complex cloned = (Complex) super.clone();
		cloned.real = this.real;
		cloned.imag = this.imag;
		return cloned;
	}
	private double real;
	private double imag;

	public void setReal(double d) {
		this.real = d;
	}

	public void setImag(double d) {
		this.imag = d;
	}

	public double getMod() {
		return Math.sqrt(real*real+imag*imag);
	}

	public double getReal() {
		return real;
	}

	public double getImag() {
		return imag;
	}
	
	public static void main(String[] args) {
//		System.out.println(new Complex(0, -1));
//		System.out.println(new Complex(0, 0));
//		System.out.println(new Complex(0, 1));
//		System.out.println(new Complex(-1, -1));
//		System.out.println(new Complex(-1, 0));
//		System.out.println(new Complex(-1, 1));
//		System.out.println(new Complex(0, 2));
//		System.out.println(new Complex(2, 0));
//		System.out.println(new Complex(2, 2));
		try{
			System.out.println(new Complex(2, 2).pow(new Complex(2, 1)));
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		
	}
}
