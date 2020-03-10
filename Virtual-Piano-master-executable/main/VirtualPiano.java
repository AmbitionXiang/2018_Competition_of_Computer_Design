package main;

import javax.swing.UIManager;

import music.MusicManager;

import resource.ImageResource;
/**
 * ��������������
 * @author group
 */
public class VirtualPiano {

	/**
	 * ����Ŀ�ʼ���޲�����
	 * Sets up things and display the main dialog.
	 * @param args
	 */


	public static void main(String[] args) {
		// use current platform's look and feel 
		try { //�趨��Ӧ�����:��ǰϵͳ�ķ��
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		// init resources and systems�����س����������ͼƬ��
		ImageResource.initFactory(); 
		//MusicResource.initFactory();
		MusicManager.init();

		// intro message box
		/*JOptionPane.showMessageDialog(null, "Tips:\nSpacebar = pedal",
				"Ready?", JOptionPane.INFORMATION_MESSAGE);*/
		
		// create and show the main dialog
		MainFrame mainFrame = new MainFrame();
		
		mainFrame.setVisible(true);
		
		// TODO
	}

}
