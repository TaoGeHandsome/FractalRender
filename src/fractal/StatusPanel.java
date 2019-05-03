package fractal;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
	public static final int STATUS_WIDTH = 250;
	public static final int STATUS_HEIGHT = ShowPanel.PIC_WIDTH - ControlPanel.CTRL_HEIGHT;
	public static final int INFO_SUCCESS = 1;
	public static final int INFO_WARNING = 2;
	public static final int INFO_ERROR = 3;
	private static final Color DARK_GREEN = new Color(0, 127, 0);
	private static JLabel lbl_time = new JLabel("");
	private static JLabel lbl_scale = new JLabel("");
	private static JLabel lbl_detail_sx = new JLabel("");
	private static JLabel lbl_detail_sy = new JLabel("");
	private static JLabel lbl_detail_rx = new JLabel("");
	private static JLabel lbl_detail_ry = new JLabel("");
	
	public StatusPanel() {
		setLayout(null);
		setBounds(ShowPanel.PIC_WIDTH + 5, ControlPanel.CTRL_HEIGHT - 10, STATUS_WIDTH - 15, STATUS_HEIGHT);
		setBorder(BorderFactory.createTitledBorder(" Status "));
		lbl_time.setBounds(20, 7, 200, 30);
		lbl_scale.setBounds(20, 21, 200, 30);
		lbl_detail_sx.setBounds(20, 35, 200, 30);
		lbl_detail_sy.setBounds(20, 49, 200, 30);
		lbl_detail_rx.setBounds(20, 63, 200, 30);
		lbl_detail_ry.setBounds(20, 77, 200, 30);
		
		add(lbl_time);
		add(lbl_scale);
		add(lbl_detail_sx);
		add(lbl_detail_sy);
		add(lbl_detail_rx);
		add(lbl_detail_ry);
		
	}

	public static void setDetails(double shiftx, double shifty, double rx, double ry) {
		lbl_detail_sx.setText("sx:       " + shiftx);
		lbl_detail_sy.setText("sy:       " + shifty);
		lbl_detail_rx.setText("rx:        " + rx);
		lbl_detail_ry.setText("ry:        " + ry);
		
	}
	
	public static void setScale(double value){
		DecimalFormat df = null;
		if (value < 1){
			df = new DecimalFormat("###.#############");
		}else{
			df = new DecimalFormat("###");
		}
		
		lbl_scale.setText("Scale: x " + df.format(value));
	}
	
	public static void setStatus(String info, int info_type) {
		if (info_type == INFO_SUCCESS) lbl_time.setText("Time:  " + info + " secs");
		else if (info_type == INFO_WARNING) lbl_time.setText("Warning: " + info);
		else if (info_type == INFO_ERROR) lbl_time.setText("Error:  "+ info);
		else lbl_time.setText("info");
		
		if (info_type == INFO_SUCCESS) lbl_time.setForeground(DARK_GREEN);
		else if(info_type == INFO_WARNING) lbl_time.setForeground(Color.YELLOW);
		else if(info_type == INFO_ERROR) lbl_time.setForeground(Color.RED);
		else lbl_time.setForeground(Color.BLACK);
	}
}
