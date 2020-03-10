package main;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import music.MusicManager;
import resource.ImageResource;

/**
 * 封装踏板模型 ,包含其状态和要画的图
 * @author group
 *
 */
public class Pedal {

	private boolean isDown; //标记是否被按下

	// 监视器列表
	private List<PedalListener> listeners;

	/**
	 * 创建未按下的踏板（默认值）.  
	 */
	public Pedal() {
		isDown = false;

		this.listeners = new ArrayList<PedalListener>();
	}
	
	public boolean isDown() {
		return isDown;
	}
	
	/**
	 * 根据按下的踏板返回图片
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
	 * 设置踏板是否被踩下. 这个设置的方法顺便也调整踏板。
	 * @param isDown：true认为被按下, false认为没被按下
	 */
	public void setDown(boolean isDown) {			
		if (!this.isDown && isDown) {
			MusicManager.getInstance().pedalDown(); //之前没被按下，但当前被按下，则踏板按下
		} else if (this.isDown && !isDown) {
			MusicManager.getInstance().pedalUp();  //之前被按下，再度按下，则踏板弹起
		}

		this.isDown = isDown;
		fireNeedsRedraw(); // 即使isDown并不改变。 
	}
	
	/*########################################################################
	 *  L I S T E N E R S
	 *########################################################################*/

	/**
	 * 注册特定的监视器.
	 * @param listener
	 */
	public void addListener(PedalListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 除去特定的监视器.
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
