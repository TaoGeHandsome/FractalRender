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
	
	private static JLabel lbl_progress = new JLabel("");
	private static JLabel lbl_status = new JLabel("");
	private static JLabel lbl_scale = new JLabel("");
	
	public StatusPanel() {
		setLayout(null);
		setBounds(ShowPanel.PIC_WIDTH + 5, ControlPanel.CTRL_HEIGHT - 10, STATUS_WIDTH - 15, STATUS_HEIGHT);
		setBorder(BorderFactory.createTitledBorder(" Status Panel "));
		
		lbl_progress.setBounds(10, 7, 200, 30);
		lbl_status.setBounds(10, 21, 200, 30);
		lbl_scale.setBounds(10, 35, 200, 30);
		
		add(lbl_progress);
		add(lbl_status);
		add(lbl_scale);
	}

	public static void setProgress(double value) {
		lbl_progress.setText("Progress: " + value + " %");
	}
	
	public static void setScale(double value){
		DecimalFormat df = new DecimalFormat("###");
		lbl_scale.setText("Scale: x " + df.format(value));
	}
	
	public static void setStatus(String status, int info_type) {
		lbl_status.setText("     Status: " + status);
		if (info_type == INFO_SUCCESS) lbl_status.setForeground(Color.GREEN);
		else if(info_type == INFO_WARNING) lbl_status.setForeground(Color.YELLOW);
		else if(info_type == INFO_ERROR) lbl_status.setForeground(Color.RED);
		else lbl_status.setForeground(Color.BLACK);
	}
}
