package main;

/**
 * 一个监视器接口：监视踏板的事件.
 * @author group
 */
public interface PedalListener {
	/**
	 * 当踏板需要重画时调用
	 * @param pedal
	 */
	public void pedalNeedsRedraw(Pedal pedal);
}
