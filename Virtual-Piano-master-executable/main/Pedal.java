package main;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import music.MusicManager;
import resource.ImageResource;

/**
 * ��װ̤��ģ�� ,������״̬��Ҫ����ͼ
 * @author group
 *
 */
public class Pedal {

	private boolean isDown; //����Ƿ񱻰���

	// �������б�
	private List<PedalListener> listeners;

	/**
	 * ����δ���µ�̤�壨Ĭ��ֵ��.  
	 */
	public Pedal() {
		isDown = false;

		this.listeners = new ArrayList<PedalListener>();
	}
	
	public boolean isDown() {
		return isDown;
	}
	
	/**
	 * ���ݰ��µ�̤�巵��ͼƬ
	 * @return
	 */
	public Image getImage() {
		return ImageResource.getImage( (isDown) ?
				ImageResource.PEDAL_DOWN : ImageResource.PEDAL_UP); 
	}
	
	/*########################################################################
	 *  S E T T E R S
	 *########################################################################*/
	
	/**
	 * ����̤���Ƿ񱻲���. ������õķ���˳��Ҳ����̤�塣
	 * @param isDown��true��Ϊ������, false��Ϊû������
	 */
	public void setDown(boolean isDown) {			
		if (!this.isDown && isDown) {
			MusicManager.getInstance().pedalDown(); //֮ǰû�����£�����ǰ�����£���̤�尴��
		} else if (this.isDown && !isDown) {
			MusicManager.getInstance().pedalUp();  //֮ǰ�����£��ٶȰ��£���̤�嵯��
		}

		this.isDown = isDown;
		fireNeedsRedraw(); // ��ʹisDown�����ı䡣 
	}
	
	/*########################################################################
	 *  L I S T E N E R S
	 *########################################################################*/

	/**
	 * ע���ض��ļ�����.
	 * @param listener
	 */
	public void addListener(PedalListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * ��ȥ�ض��ļ�����.
	 * @param listener
	 */
	public void removeListener(PedalListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fires pedalNeedsRedraw method in the registered listeners.
	 */
	private void fireNeedsRedraw() {
		for (PedalListener listener: listeners)
			listener.pedalNeedsRedraw(this);
	}
}
