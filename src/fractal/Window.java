package fractal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener{
	private static final int JMENU_HEIGHT = 10;
	private static final int DEFAULT_WIDTH = ShowPanel.PIC_WIDTH + ControlPanel.CTRL_WIDTH;
	private static final int DEFAULT_HEIGHT = ShowPanel.PIC_HEIGHT + 35 + JMENU_HEIGHT;
	
	private static final String MN_SAVE = "Save";
	private static final String MN_OPTION = "Option";
	private static final String MN_ABOUT = "About";
	private static final String MNI_COPY = "Copy to clipboard      Ctrl+C";
	private static final String MNI_SAVE = "Save as file ...             Ctrl+S";
	private static final String MNI_EXIT = "Exit                                Alt+F4";
	private static final String MNI_UNDO = "Undo View       Ctrl+Z";
	private static final String MNI_SET_VIEW1 = "Default";
	private static final String MNI_SET_VIEW2 = "Classic";
	private static final String MNI_SET_VIEW3 = "Rabbits";
	private static final String MNI_SET_VIEW4 = "Dragon";
	private static final String MNI_SET_VIEW5 = "Plane";
	private static final String MNI_SET_VIEW6 = "Tri-Julia";
	private static final String MNI_FIX_SCALING = "Fixed scaling";
	private static final String MNI_OPT_GRAPH = "Optimize Graph (slow)";
	private static final String MNI_OPT_SPEED = "Optimize Speed";
	private static final String MNI_HELP = "Help...";
	private static final String MNI_INFO = "Info...";
	
	private static ControlPanel ctrl_panel = new ControlPanel();
	private static ShowPanel show_panel = new ShowPanel();
	private static StatusPanel status_panel = new StatusPanel();
	private static JMenuBar menuBar;
	private static JFileChooser fc = new JFileChooser();
	
	public static int max_times;
	public Window() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screensize.width - DEFAULT_WIDTH) / 2;
		int y = (screensize.height - DEFAULT_HEIGHT) / 2;
		setLocation(x, y);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Fractal Generator v1.3           By TGH");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);

		// Add Key Listener
		addKeyListener(keyHandler);
		ctrl_panel.addKeyListener(keyHandler);
		ctrl_panel.txt_exp.addKeyListener(keyHandler);
		ctrl_panel.btn_render.addKeyListener(keyHandler);
		ctrl_panel.btn_reset.addKeyListener(keyHandler);
		ctrl_panel.lbl_help.addKeyListener(keyHandler);
		
		// Add menu
		menuBar = new JMenuBar();
		
		JMenu mnSave = new JMenu(MN_SAVE);
		JMenuItem miCopy = new JMenuItem(MNI_COPY);
		JMenuItem miSave = new JMenuItem(MNI_SAVE);
		JMenuItem miExit = new JMenuItem(MNI_EXIT);
		mnSave.add(miCopy);
		mnSave.add(miSave);
		mnSave.addSeparator();
		mnSave.add(miExit);
		
		JMenu mnOption = new JMenu(MN_OPTION);
		JMenuItem miUndo = new JMenuItem(MNI_UNDO);
		JMenuItem miView1 = new JMenuItem(MNI_SET_VIEW1);
		JMenuItem miView2 = new JMenuItem(MNI_SET_VIEW2);
		JMenuItem miView3 = new JMenuItem(MNI_SET_VIEW3);
		JMenuItem miView4 = new JMenuItem(MNI_SET_VIEW4);
		JMenuItem miView5 = new JMenuItem(MNI_SET_VIEW5);
		JMenuItem miView6 = new JMenuItem(MNI_SET_VIEW6);
		JCheckBoxMenuItem miFixScaling = new JCheckBoxMenuItem(MNI_FIX_SCALING);
		JCheckBoxMenuItem miOptimizeGraph = new JCheckBoxMenuItem(MNI_OPT_GRAPH);
		JCheckBoxMenuItem miOptimizeSpeed = new JCheckBoxMenuItem(MNI_OPT_SPEED);
		mnOption.add(miUndo);
		mnOption.addSeparator();
		//Initial pic formula
		mnOption.add(miView1);
		mnOption.add(miView2);
		mnOption.add(miView3);
		mnOption.add(miView4);
		mnOption.add(miView5);
		mnOption.add(miView6);
		mnOption.addSeparator();
		mnOption.add(miFixScaling);
		mnOption.add(miOptimizeGraph);
//		mnOption.add(miOptimizeSpeed);
		
		JMenu mnHelp = new JMenu(MN_ABOUT);
		JMenuItem miHelp = new JMenuItem(MNI_HELP);
		JMenuItem miInfo = new JMenuItem(MNI_INFO);
		mnHelp.add(miHelp);
		mnHelp.addSeparator();
		mnHelp.add(miInfo);
		
		menuBar.setBounds(0, 0, DEFAULT_WIDTH, JMENU_HEIGHT);
		
		mnSave.setFont(new Font("Arial", Font.PLAIN, 12));
		mnOption.setFont(new Font("Arial", Font.PLAIN, 12));
		mnHelp.setFont(new Font("Arial", Font.PLAIN, 12));
		miCopy.setFont(new Font("Arial", Font.PLAIN, 12));
		miSave.setFont(new Font("Arial", Font.PLAIN, 12));
		miExit.setFont(new Font("Arial", Font.PLAIN, 12));
		miUndo.setFont(new Font("Arial", Font.PLAIN, 12));
		miView1.setFont(new Font("Arial", Font.PLAIN, 12));
		miView2.setFont(new Font("Arial", Font.PLAIN, 12));
		miView3.setFont(new Font("Arial", Font.PLAIN, 12));
		miView4.setFont(new Font("Arial", Font.PLAIN, 12));
		miView5.setFont(new Font("Arial", Font.PLAIN, 12));
		miView6.setFont(new Font("Arial", Font.PLAIN, 12));
		miFixScaling.setFont(new Font("Arial", Font.PLAIN, 12));
		miOptimizeGraph.setFont(new Font("Arial", Font.PLAIN, 12));
		miOptimizeSpeed.setFont(new Font("Arial", Font.PLAIN, 12));
		miHelp.setFont(new Font("Arial", Font.PLAIN, 12));
		miInfo.setFont(new Font("Arial", Font.PLAIN, 12));
		Window.setUndoEnabled(true);
		
		menuBar.add(mnSave);
		menuBar.add(mnOption);
		menuBar.add(mnHelp);
		
		// Event driven
		for (int i = 0; i < menuBar.getMenuCount(); i++){
			JMenu menu = menuBar.getMenu(i);
			for(int j = 0; j < menu.getItemCount(); j++){
				JMenuItem mi = menu.getItem(j);
				if (mi != null) mi.addActionListener(this);
			}
		}
		
		// Add to Frame
		setJMenuBar(menuBar);
		add(show_panel);
		add(ctrl_panel);
		add(status_panel);
		
		// Set UI Font
		UIManager.put("Label.font", new Font("Courier New", Font.PLAIN, 12));
		
		// Refresh View
		show_panel.refreshView(false);
	}
	
	public static void setUndoEnabled(boolean enabled){
		if (menuBar.getMenu(1) != null) 
			menuBar.getMenu(1).getItem(0).setEnabled(enabled);
	}

	public static void resetView(){
		show_panel.resetView();
	}
	
	public static void setExp(String exp){
		show_panel.setNewView(exp, 300);
	}
	
	public static void saveImage(){
		int result;
		fc.setDialogTitle("Save Julia Picture ...");
		fc.setFileFilter(new FileNameExtensionFilter("*.jpg", "JPEG image"));
		result = fc.showSaveDialog(null);
		if (result != JFileChooser.APPROVE_OPTION) 
			return;
		
		File file = fc.getSelectedFile();
		if (!file.getName().endsWith(".jpg"))
			file = new File(file.getPath() + ".jpg");
		BufferedImage buffer = show_panel.getBufferedImage();
		try {
			ImageIO.write(buffer, "jpg", new File(file.getPath()));
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	public static void copyImage(){
		Images image = new Images(show_panel.getBufferedImage());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(image, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// http://blog.csdn.net/tsyj810883979/article/details/6494441
		switch (e.getActionCommand()){
			case MNI_COPY:
				Window.copyImage(); break;
			case MNI_SAVE:
				Window.saveImage(); break;
			case MNI_EXIT:
				System.exit(0); break;
			case MNI_UNDO:
				show_panel.undoView(); 
				break;
			case MNI_SET_VIEW1:		// Default
				setExp("z^2+0.3");
				ctrl_panel.txt_exp.setText("z^2+0.3");
				break;
			case MNI_SET_VIEW2:		// Classic
				setExp("z^2-0.75");
				ctrl_panel.txt_exp.setText("z^2-0.75");
				break;
			case MNI_SET_VIEW3:		// Rabbit
				setExp("z^2-0.123+0.745i");
				ctrl_panel.txt_exp.setText("z^2-0.123+0.745i");
				break;
			case MNI_SET_VIEW4:		// Dragon
				setExp("z^2-0.8+0.15i");
				ctrl_panel.txt_exp.setText("z^2-0.8+0.15i");
				break;
			case MNI_SET_VIEW5:		// Plane
				setExp("z^2-1.755");
				ctrl_panel.txt_exp.setText("z^2-1.755");
				break;
			case MNI_SET_VIEW6:		// Tri-Julia
				setExp("z^3-0.6+0.3i");
				ctrl_panel.txt_exp.setText("z^3-0.6+0.3i");
				break;
			case MNI_FIX_SCALING:
				ShowPanel.alwaysFixScale = !ShowPanel.alwaysFixScale;
				break;
			case MNI_OPT_GRAPH:
				ShowPanel.boost = !ShowPanel.boost;
				break;
			case MNI_OPT_SPEED:
				// TODO Optimize graph.
				break;
			case MNI_HELP:
				String[] help_options = new String[]{"    I know.    "};
				String help_content = "Steps: \n"
									+ "  1. Input expressions in plane text box;\n"
									+ "  2. Click 'Render' button;\n"
									+ "  3. Use your mouse to move in view box;\n"
									+ "  4. Click 'Reset View' button to reset;\n"
									+ "  5. Try some built-in exps in Option menu.\n"
									+ "  6. Ctrl + C can copy this pic to clipboard;\n"
									+ "  7. Share pics to your friend. You can save\n"
									+ "     it in File menu.\n\n\n"
									+ "     Made by TaoGeHandsome.\n\n"
									+ "                                2017.06\n";
				JOptionPane.showOptionDialog(null, help_content, "Help - Julia Render",
						JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, help_options, help_options[0]);
				break;
				
			case MNI_INFO:
				String[] info_options = new String[]{"    I know.    "};
				String info_content = "  This beautiful julia render is for Java\n"
									+ "GUI study and final assignment.\n"
									+ "  Made by TaoGeHandsome.\n\n"
									+ "                                2017.06\n";
				JOptionPane.showOptionDialog(null, info_content, "Information - Julia Render",
						JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, info_options, info_options[0]);
				break;
				
			default: break;
		}
	}
	
	public static void errorDialog(String str){
		String[] err_title = new String[]{"    Let me check it.    "};
		JOptionPane.showOptionDialog(null, str, "Error occurs",
				JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, err_title, err_title[0]);
	}
	
	KeyListener keyHandler = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 16)		// Shift
				show_panel.setPressShift(true);
			else if (e.getKeyCode() == 17)	// Ctrl
				show_panel.setPressCtrl(true);
			else if (e.getKeyCode() == 18)	// Alt
				show_panel.setPressAlt(true);
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z)
				show_panel.undoView();
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S)
				Window.saveImage();
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C)
				Window.copyImage();
			if (e.getKeyCode() == 18) e.consume();
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == 16)		// Shift
				show_panel.setPressShift(false);
			else if (e.getKeyCode() == 17)	// Ctrl
				show_panel.setPressCtrl(false);
			else if (e.getKeyCode() == 18)	// Alt
				show_panel.setPressAlt(false);
			if (e.getKeyCode() == 18) e.consume();
		}
		
		public void keyTyped(KeyEvent e){
			if (e.getKeyCode() == 16)		// Shift
				show_panel.setPressShift(false);
			else if (e.getKeyCode() == 17)	// Ctrl
				show_panel.setPressCtrl(false);
			else if (e.getKeyCode() == 18)	// Alt
				show_panel.setPressAlt(false);
			if (e.getKeyCode() == 18) e.consume();
		}
	};
}

class Images implements Transferable {
    private Image image;
    public Images(Image image){
    	this.image = image;
    }

    public DataFlavor[] getTransferDataFlavors(){
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor){
        return DataFlavor.imageFlavor.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException{
        if (!DataFlavor.imageFlavor.equals(flavor))
        	throw new UnsupportedFlavorException(flavor);
        return image;
    }
}
