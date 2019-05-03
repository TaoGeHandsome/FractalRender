package fractal;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public class ShowPanel extends JPanel {
	public static final int PIC_WIDTH = 400;
	public static final int PIC_HEIGHT = 400;
	private double init_shiftx = 0;
	private double init_shifty = 0;
	private double init_rx = 2;
	private double init_ry = 2;
	private static String exp = "z^2+0.3";
	public static boolean boost = false;
	public static boolean alwaysFixScale = false;
	
	LinkedList<ViewInfo> rect_history = new LinkedList<ViewInfo>();

	private static JLabel picbox = new JLabel("");

	public ShowPanel() {
		setBounds(0, -5, PIC_WIDTH, PIC_HEIGHT);
		picbox.setBounds(0, 0, PIC_WIDTH, PIC_HEIGHT);
		picbox.addMouseListener(mouseHandler);
		picbox.addMouseMotionListener(mouseHandler);
		picbox.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		add(picbox);
		rect_history.addLast(new ViewInfo(init_shiftx, init_shifty, init_rx, init_ry));
	}
	
	// View Controller
	public void refreshView(boolean isRecord) {
		if (rect == null)
			rect = new Rectangle(0, 0, PIC_WIDTH, PIC_HEIGHT);
		ViewInfo info = rect_history.peekLast();
		double shiftx = info.getShiftx() - info.getRx() / PIC_WIDTH * (PIC_WIDTH / 2 - rect.x - rect.width / 2) * 2 * (isMoving ? -1 : 1);
		double shifty = info.getShifty() - info.getRy() / PIC_HEIGHT * (PIC_HEIGHT / 2 - rect.y - rect.height / 2) * 2 * (isMoving ? -1 : 1);
		double rx = info.getRx() / ((double) PIC_WIDTH / rect.width);
		double ry = info.getRy() / ((double) PIC_HEIGHT / rect.height);
		if (rx < 1E-10 || ry < 1E-10) {
			StatusPanel.setStatus("Too large scaling!", StatusPanel.INFO_ERROR);
			return;
		}
		refreshView(shiftx, shifty, rx, ry, isRecord);
		
	}
	
	public void refreshView(double shiftx, double shifty, double rx, double ry, boolean isRecord){
		StatusPanel.setScale(init_rx * init_ry / (rx * ry));
		StatusPanel.setDetails(shiftx, shifty, rx, ry);
		if(isRecord) rect_history.addLast(new ViewInfo(shiftx, shifty, rx, ry));
		Fractal f = null;
		try {
			f = new Fractal(exp, boost);
		} catch (Exception e) {
			Window.errorDialog(e.getMessage());
			exp = Fractal.get_last_exp();
			return ;
		}
		BufferedImage bi = null;
		try {
			bi = f.createJuliaImage(exp, 400, 400, shiftx, shifty, rx, ry);
		} catch (Exception e) {
			Window.errorDialog(e.getMessage());
			exp = Fractal.get_last_exp();
			return ;
		}
		picbox.setIcon(new ImageIcon(bi));
		Window.setUndoEnabled(rect_history.size() > 1);
	}
	
	public void undoView(){
		if (rect_history.size() > 1){
			rect_history.removeLast();
			ViewInfo info = rect_history.peekLast();
			refreshView(info.getShiftx(), info.getShifty(), info.getRx(), info.getRy(), false);
		}else{
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	public void resetView(){
		ViewInfo info = rect_history.peekFirst();
		rect_history.clear();
		refreshView(info.getShiftx(), info.getShifty(), info.getRx(), info.getRy(), true);
	}
	
	public void setNewView(String str, int max_times){
		@SuppressWarnings("unused")
		Fractal temp = null;
		try{
			temp = new Fractal(str);
		}catch (Exception e){
			// Warning!
//			Toolkit.getDefaultToolkit().beep();
			String error_content = "Your expression:\n"+
					str + "\nhas errors:\n" + e.getMessage() + "\nPlease check it!\n\n";
			Window.errorDialog(error_content);
			StatusPanel.setStatus("Expression not valid!", StatusPanel.INFO_ERROR);
			return ;
		}
		temp = null;
		exp = str;
		ViewInfo info = rect_history.peekFirst();
		rect_history.clear();
		refreshView(info.getShiftx(), info.getShifty(), info.getRx(), info.getRy(), true);
	}
	
	// Mouse and Keyboard Event of View
	private static Rectangle rect = null;
	private boolean isPressLeftMouse = false;
	private boolean isPressRightMouse = false;
	private boolean isPressShift = false;
	private boolean isPressCtrl = false;
	private boolean isPressAlt = false;
	private boolean isMoving = false;

	MouseInputListener mouseHandler = new MouseInputAdapter() {
		Point start_point;

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() != 1) return ;
			if (isPressCtrl){
				refreshView(true);
				rect = null;
				repaint();
			}else if (isPressAlt){
				if (rect != null)
					rect = new Rectangle(rect.x - PIC_HEIGHT * 3 / 4, rect.y - PIC_HEIGHT * 3 / 4, PIC_WIDTH * 2,
							PIC_HEIGHT * 2);
				refreshView(true);
				rect = null;
				repaint();
			}
			
			if (e.getButton() == 1 && (isPressCtrl || isPressAlt)) {
				if (isPressAlt && rect != null)
					repaint();
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			if (!isPressLeftMouse && (isPressCtrl || isPressAlt)) {
				isMoving = false;
				Point p = e.getPoint();
				p.x = p.x < PIC_WIDTH / 4 ? PIC_WIDTH / 4 : (p.x > PIC_WIDTH * 3 / 4 ? PIC_WIDTH * 3 / 4 : p.x);
				p.y = p.y < PIC_HEIGHT / 4 ? PIC_HEIGHT / 4 : (p.y > PIC_HEIGHT * 3 / 4 ? PIC_HEIGHT * 3 / 4 : p.y);
				rect = new Rectangle();
				if (isPressCtrl) {
					makeRectangle(p, new Point(p.x + PIC_WIDTH / 4, p.y + PIC_HEIGHT / 4));
				} else if (isPressAlt) {
					makeRectangle(new Point(p.x - PIC_WIDTH / 4, p.y - PIC_HEIGHT / 4),
							new Point(p.x + PIC_WIDTH / 4, p.y + PIC_HEIGHT / 4));
				}
				repaint();
			}

		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == 1)
				isPressLeftMouse = true;
			if (e.getButton() == 3)
				isPressRightMouse = true;
			
			if (isPressLeftMouse && (isPressCtrl || isPressAlt))
				return;
			
			if (e.getButton() == 3) {
				picbox.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				start_point = e.getPoint();
				rect = new Rectangle();
			} else if (e.getButton() == 1 && !isPressCtrl) {
				isMoving = true;
				start_point = e.getPoint();
				rect = new Rectangle();
			} else if (e.getButton() == 1 && isPressCtrl) {
				isMoving = false;
				rect = new Rectangle();
			} else
				rect = null;

		}

		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == 1)
				isPressLeftMouse = false;
			if (e.getButton() == 3)
				isPressRightMouse = false;
			
			if (e.getButton() == 1 && (isPressCtrl || isPressAlt || isPressShift) && 
					e.getPoint()!= start_point) 
				return;
			makeRectangle(start_point, e.getPoint());
			if (rect.width > 0 && rect.height > 0) {
				refreshView(true);
				rect = null;
				repaint();
			}
			if (e.getButton() == 1) 
				isMoving = false;
			picbox.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		}

		public void mouseDragged(MouseEvent e) {
			if (isPressLeftMouse && (isPressCtrl || isPressShift || isPressAlt))
				return ;
			if (e.getX() < 0 || e.getY() < 0)
				return;
			if (isPressLeftMouse)
				picbox.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			else if (isPressRightMouse)
				picbox.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			if (rect != null) {// && !isPressCtrl
				makeRectangle(start_point, e.getPoint());
				repaint();
			}
		}

		private void makeRectangle(Point p1, Point p2) {
			if (rect == null)
				return;
			int x, y, w, h;
			
			if (isPressLeftMouse && (isPressCtrl || isPressShift))
				return ;
			if (!isMoving) {
				
				int p2x = p2.x < 0 ? 0 : (p2.x > PIC_WIDTH ? PIC_WIDTH - 1 : p2.x);
				int p2y = p2.y < 0 ? 0 : (p2.y > PIC_HEIGHT ? PIC_HEIGHT - 1 : p2.y);
				x = Math.min(p1.x, p2x);
				y = Math.min(p1.y, p2y) + 5;
				w = Math.abs(p1.x - p2x);
				h = Math.abs(p1.y - p2y);
				
				if (isPressShift || alwaysFixScale) {	// Fixed Scaling
					int min = Math.min(w, h);
					w = min;
					h = min;
					if (p2.x < p1.x)
						x = x + p1.x - p2.x - w;
					if (p2.y < p1.y)
						y = y + p1.y - p2.y - h;
				}
				if (isPressCtrl) {	// 1:1 Scaling
					x -= p2.x < p1.x ? 0 : w;
					y -= p2.y < p1.y ? 0 : h;
					w *= 2;
					h *= 2;
				}
			} else {
				x = p2.x - p1.x;
				y = p2.y - p1.y;
				w = PIC_WIDTH;
				h = PIC_HEIGHT;
			}
			rect.setBounds(x, y, w, h);
			
		}
	};
	
	public void setPressShift(boolean isPressShift) {
		this.isPressShift = isPressShift;
	}

	public void setPressCtrl(boolean isPressCtrl) {
		this.isPressCtrl = isPressCtrl;
		if (isPressCtrl && !isPressLeftMouse && !isPressRightMouse)
			picbox.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit()
					.getImage("icon/zoom_in.png").getScaledInstance(64, 64, Image.SCALE_SMOOTH), new Point(0, 0), ""));
		else if (!isPressLeftMouse)
			picbox.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		if (isPressRightMouse)
			picbox.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		if (!isPressLeftMouse && !isPressCtrl) {
			rect = null;
			repaint();
			rect = new Rectangle();
		}
	}

	public void setPressAlt(boolean isPressAlt) {
		this.isPressAlt = isPressAlt;
		if (isPressAlt && !isPressLeftMouse && !isPressRightMouse)
			picbox.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit()
					.getImage("icon/zoom_out.png").getScaledInstance(64, 64, Image.SCALE_SMOOTH), new Point(0, 0), ""));
		else if (!isPressLeftMouse)
			picbox.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		if (isPressRightMouse)
			picbox.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		if (!isPressLeftMouse && !isPressAlt) {
			rect = null;
			repaint();
			rect = new Rectangle();
		}
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public BufferedImage getBufferedImage(){
		Image image = ((ImageIcon)picbox.getIcon()).getImage();
		int w = image.getWidth(this); 
        int h = image.getHeight(this);
        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = buffer.createGraphics();  
        g.drawImage(image, 0, 0, null);  
        g.dispose();
		return buffer;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.yellow);
		if (rect != null) {
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
		}
	}
}

class ViewInfo{
	private double shiftx;
	private double shifty;
	private double rx;
	private double ry;
	
	public ViewInfo(double shiftx, double shifty, double rx, double ry){
		this.shiftx = shiftx;
		this.shifty = shifty;
		this.rx = rx;
		this.ry = ry;
	}
	
	public double getShiftx(){
		return shiftx;
	}
	
	public double getShifty(){
		return shifty;
	}
	
	public double getRx(){
		return rx;
	}
	
	public double getRy(){
		return ry;
	}
	
	
}