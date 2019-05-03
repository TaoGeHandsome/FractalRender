package fractal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	public static final int CTRL_WIDTH = 250;
	public static final int CTRL_HEIGHT = 295;
	public JTextField txt_exp;
	public JButton btn_render;
	public JButton btn_reset;
	public MultilineLabel lbl_help;
	
	public ControlPanel() {
		setLayout(null);
		setBounds(ShowPanel.PIC_WIDTH + 5, 0, CTRL_WIDTH - 15, CTRL_HEIGHT - 10);
		setBorder(BorderFactory.createTitledBorder(" Control Panel "));
		
		JLabel lbl_exp = new JLabel("Expression:");
		lbl_exp.setHorizontalAlignment(JLabel.LEFT);
		lbl_exp.setBounds(15, 22, 80, 10);
		
		JLabel lbl_fz = new JLabel("f(z) = ");
		lbl_fz.setHorizontalAlignment(JLabel.LEFT);
		lbl_fz.setBounds(22, 44, 40, 10);
		
		txt_exp = new JTextField("z^2+0.3");
		txt_exp.setHorizontalAlignment(JLabel.LEFT);
		txt_exp.setBounds(56, 40, CTRL_WIDTH-80, 20);
		
		btn_render = new JButton("Render");
		btn_render.setBounds(25, 70, 180, 30);
		btn_render.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Window.setExp(txt_exp.getText());
            }           
        });
		
		btn_reset = new JButton("Reset View");
		btn_reset.setBounds(25, 110, 180, 30);
		btn_reset.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Window.resetView();
            }           
        });
		
		String help_content = 	"Help :\n" + 
								"    Left mouse drag => Move\n" + 
								"    Right mouse drag => Zoom\n" + 
								"    Ctrl + Click => Zoom in (x2)\n" + 
								"    Alt + Click => Zoom out (x0.5)\n" + 
								"    Ctrl for operation from center\n" + 
								"    Shift for fixed scaling\n";
		lbl_help = new MultilineLabel(help_content);
		lbl_help.setForeground(new Color(125, 125, 125));
		lbl_help.setBounds(15, 145, 200, 130);
		
		add(lbl_exp);
		add(lbl_fz);
		add(txt_exp);
		add(btn_render);
		add(btn_reset);
		add(lbl_help);
	}

}

@SuppressWarnings("serial")
class MultilineLabel extends JTextArea{

	   public MultilineLabel(String s){  
	    
	     super(s);
	   }

	   public void updateUI(){
	      super.updateUI();
	      setLineWrap(true);
	      setWrapStyleWord(true);
	      setHighlighter(null);
	      setEditable(false);
	      LookAndFeel.installBorder(this,"Label.border");
	      LookAndFeel.installColorsAndFont(this,"Label.background", "Label.foreground", "Label.font");
	      }
	   }
