package fractal;
import java.awt.image.BufferedImage;

public class Fractal {
	private int MAX_ITER = 500;
	private CalTree caltree;
	private String exp;
	private static String last_exp = "";
	private boolean boost = false;
	
	public Fractal(String exp) throws Exception{
		caltree = null;
		try {
			caltree = new CalTree(exp);
		} catch (Exception e) {
			throw e;
		}
		boost = false;
	}
	
	public Fractal(String exp, boolean boost_graph) throws Exception{
		caltree = null;
		try {
			caltree = new CalTree(exp);
		} catch (Exception e) {
			throw e;
		}
		boost = boost_graph;
	}
	
	public BufferedImage createJuliaImage(String exp, int width, int height, double x0, double y0, double rx, double ry) throws Exception
	{
		long time = System.currentTimeMillis();
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Complex z = new Complex(0, 0);
		for (int j = 0; j < height; j++){
			for (int i = 0; i < width; i++){
				double x=(2*rx)*i/width+x0-rx;
	            double y=(2*ry)*j/height+y0-ry;
				z.setReal(x);
				z.setImag(y);
				int re_cnt = iter_times(z);
				
				temp.setRGB(i, j, boost ? get_color2(re_cnt): get_color(re_cnt));
			}
//			StatusPanel.setProgress((j+1)/(double)width * 100);
//			System.out.println((j+1)/(double)width * 100);
		}
		time = System.currentTimeMillis() - time;
		StatusPanel.setStatus(""+time/1000.0, StatusPanel.INFO_SUCCESS);
		return temp;
	}
	
	private int iter_times(Complex c) throws Exception{
		int i = 0;
		
		for (;i<MAX_ITER;i++){
			if (c.getMod()>=4)  
	            break; 
			c = caltree.valueOf(c);
//			c = cc.evaluate(cc.preProcess(exp, c));
		}
		last_exp = exp;
		return i;
	}
	
	public static String get_last_exp(){
		return last_exp;
	}
	
	private int get_color(int iter){
		if (iter==MAX_ITER)
	        return format_color(0, 0, 0);  
	    else
	    	return format_color(((iter*20)%256), ((iter*15+85)%256), (iter*30+171)%256);
	}
	
	private int sin_color(int iter){
		return (int) ((Math.sin(iter*2*3.1415926/510-3.1415926*0.5)+1)*0.5*255 );
	}
	
	private int get_color2(int iter){
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


