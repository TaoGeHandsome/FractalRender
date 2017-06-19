package fractal;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	public static final int CTRL_WIDTH = 250;
	public static final int CTRL_HEIGHT = 340;
	
	public ControlPanel() {
		setBounds(ShowPanel.PIC_WIDTH + 5, 0, CTRL_WIDTH - 15, CTRL_HEIGHT - 10);
		setBorder(BorderFactory.createTitledBorder(" Control Panel "));
//		JLabel lbl_
	}

}
