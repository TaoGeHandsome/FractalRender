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
	
	public Complex div(Complex c){
		Complex res = new Complex(0, 0);
		if (c.real == 0 && c.imag == 0)
			return null;
		res.real = (real * c.real + imag * c.imag) / (c.real * c.real + c.imag * c.imag);
		res.imag = (imag * c.real - real * c.imag) / (c.real * c.real + c.imag * c.imag);
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

	public static void main(String[] args) {
		System.out.println(new Complex(0, -1));
		System.out.println(new Complex(0, 0));
		System.out.println(new Complex(0, 1));
		System.out.println(new Complex(-1, -1));
		System.out.println(new Complex(-1, 0));
		System.out.println(new Complex(-1, 1));
		System.out.println(new Complex(0, 2));
		System.out.println(new Complex(2, 0));
		System.out.println(new Complex(2, 2));
	}

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

}
