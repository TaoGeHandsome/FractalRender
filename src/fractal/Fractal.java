package fractal;
import java.awt.image.BufferedImage;

public class Fractal {
	private static int MAX_ITER = 1000;
	
	public static Complex func(Complex z, Complex c){
		z = z.mul(z).add(c);
		return z;
	}
	
	public static BufferedImage createJuliaImage(String exp, int width, int height, double x0, double y0, double rx, double ry) throws Exception
	{
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Complex z = new Complex(0, 0);
		for (int j = 0; j < height; j++){
			for (int i = 0; i < width; i++){
				double x=(2*rx)*i/width+x0-rx;
	            double y=(2*ry)*j/height+y0-ry;
				z.setReal(x);
				z.setImag(y);
				int re_cnt = iter_times(exp, z);
				temp.setRGB(i, j, get_color2(re_cnt));
			}
//			StatusPanel.setProgress((j+1)/(double)width * 100);
			System.out.println((j+1)/(double)width * 100);
		}
		StatusPanel.setStatus("Done!", StatusPanel.INFO_SUCCESS);
		return temp;
	}
	
	private static int iter_times(String exp, Complex c){
		int i = 0;
		CalCore cc = new CalCore();
		for (;i<MAX_ITER;i++){
			
			if (c.getMod()>=4)  
	            break; 
			c = cc.evaluate(cc.preProcess(exp, c));
//	        double tmp=x*x-y*y+c.getReal();
//	        y=x*y*2+c.getImag();
//	        x=tmp;  
		}
		return i;
	}
	
	private static int get_color(int iter){
		if (iter==MAX_ITER)
	        return format_color(0, 0, 0);  
	    else
	    	return format_color(((iter*20)%256), ((iter*15+85)%256), (iter*30+171)%256);
	}
	
	private static int sin_color(int iter){
		return (int) ((Math.sin(iter*2*3.1415926/510-3.1415926*0.5)+1)*0.5*255 );
	}
	
	private static int get_color2(int iter){
		if (iter == MAX_ITER){
	        return format_color(0,0,0);
	    }else{ 
	        return format_color(sin_color(iter*20),sin_color(iter*15+85),sin_color(iter*30+171));
	    }
	}
	
	private static int format_color(int r, int g, int b){
		return (255<<24) + (r<<16) + (g<<8) + (b);
	}
}


