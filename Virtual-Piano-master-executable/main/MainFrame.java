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
 * ��װ�����Ŀ�����Ի��򣬰������е������
 * ֻ������һ��
 * @author group
 *
 */
public class MainFrame extends JFrame {
	
	private Piano piano;
	
	/**
	 * ����������塣���������������͸��ټ���
	 * @return
	 */
	private JPanel createContentPanel() {
		piano = new Piano();

		//canvas = new Canvas(piano.getPianoWidth());
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());  //���񲼾�
		//contentPanel.add(canvas, new CustomGridBagConstraints(0, 0));
		//��piano��������ڵ�0�У���1��
		contentPanel.add(piano, new CustomGridBagConstraints(0, 1)); 
		
		return contentPanel;
	}
	
	/**
	 * ����һ��Ĭ�ϱ༭��ܡ�
	 */
	public MainFrame() {
		super("Piano Master Version 1.0.0");
		
		add(createContentPanel());
		
		// ��������
		setLocationByPlatform(true); //�������´δ��ڿɼ�ʱ��������Ӧ��ʾλ��
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // ��������
		setResizable(false); //���ɸı��С
		pack();//�������ڴ�С����Ӧ�����������ѡ��С�Ͳ���
		setVisible(false); // ���Ƚ��ô�������

		addWindowListener(new MainFrameWindowListener());
	}
	
	/*########################################################################
	 *   WindowListener 
	 *########################################################################*/

	private class MainFrameWindowListener extends WindowAdapter {
		/**
		 * ���ùرղ������ڹرմ���ǰ����һ���Ի���
		 */
		@Override		
		public void windowClosing(WindowEvent e) {
			
			// ע�⣬����Ի�������ˣ���ʱȥ���ˡ� 
			/*JOptionPane.showOptionDialog(null, LayoutConstant.byeTitle,
					LayoutConstant.byeMessage, JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, LayoutConstant.byeOptions, 
					LayoutConstant.byeOptions[0]);*/
			
			System.exit(0);
		}
	}
	
	
}
