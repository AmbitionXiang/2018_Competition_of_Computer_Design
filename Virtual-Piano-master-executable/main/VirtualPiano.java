package main;

import javax.swing.UIManager;

import music.MusicManager;

import resource.ImageResource;
/**
 * 包含程序的总入口
 * @author group
 */
public class VirtualPiano {

	/**
	 * 程序的开始。无参数。
	 * Sets up things and display the main dialog.
	 * @param args
	 */


	public static void main(String[] args) {
		// use current platform's look and feel 
		try { //设定对应的外观:当前系统的风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		// init resources and systems（加载除了谱子外的图片）
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
