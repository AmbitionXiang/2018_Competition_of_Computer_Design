package main;

import java.awt.Canvas;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.util.CustomGridBagConstraints;
import resource.LayoutConstant;


/**
 * 封装这个项目的主对话框，包括所有的组件。
 * 只被创建一次
 * @author group
 *
 */
public class MainFrame extends JFrame {
	
	private Piano piano;
	
	/**
	 * 返回内容面板。内容面板包含画布和钢琴键。
	 * @return
	 */
	private JPanel createContentPanel() {
		piano = new Piano();

		//canvas = new Canvas(piano.getPianoWidth());
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());  //网格布局
		//contentPanel.add(canvas, new CustomGridBagConstraints(0, 0));
		//将piano对象放置在第0行，第1列
		contentPanel.add(piano, new CustomGridBagConstraints(0, 1)); 
		
		return contentPanel;
	}
	
	/**
	 * 创建一个默认编辑框架。
	 */
	public MainFrame() {
		super("Piano Master Version 1.0.0");
		
		add(createContentPanel());
		
		// 总体设置
		setLocationByPlatform(true); //设置在下次窗口可见时，窗口是应显示位置
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 将被覆盖
		setResizable(false); //不可改变大小
		pack();//调整窗口大小以适应其子组件的首选大小和布局
		setVisible(false); // 首先将该窗口隐藏

		addWindowListener(new MainFrameWindowListener());
	}
	
	/*########################################################################
	 *   WindowListener 
	 *########################################################################*/

	private class MainFrameWindowListener extends WindowAdapter {
		/**
		 * 调用关闭操作。在关闭窗口前开启一个对话框
		 */
		@Override		
		public void windowClosing(WindowEvent e) {
			
			// 注意，这个对话框很恼人，暂时去掉了。 
			/*JOptionPane.showOptionDialog(null, LayoutConstant.byeTitle,
					LayoutConstant.byeMessage, JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, LayoutConstant.byeOptions, 
					LayoutConstant.byeOptions[0]);*/
			
			System.exit(0);
		}
	}
	
	
}
