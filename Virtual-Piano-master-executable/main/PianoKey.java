package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import music.MusicManager;
import music.MusicalNote;

/**
 * 封装一个单一的钢琴键，知道它的音高，颜色绘制，位置等。
 * @author Group
 *
 */
public class PianoKey {

	public static final Color COLOR_KEY_FRAME = new Color(0.0f, 0.0f, 0.0f); // frame琴键的边
	
	public static final Color COLOR_NORMAL_PLAIN = new Color(1.0f, 1.0f, 1.0f);
	public static final Color COLOR_DOWN_PLAIN = new Color(1.0f, 1.0f, 0.5f); // 淡黄色
	public static final Color COLOR_NORMAL_CHROMATIC = new Color(0.0f, 0.0f, 0.0f);
	public static final Color COLOR_DOWN_CHROMATIC = new Color(0.5f, 0.5f, 0.2f); // 暗黄色
	
	// I decide to do this (even though a little dirty) because the Piano is the
	// context of a PianoKey.  A PianoKey cannot live without the Piano it
	// belongs to.
	private Piano piano;
	
	private int no;
	private int pitchOffset;
	private boolean isChromatic;
	private Rectangle bounds;
	private boolean isDown;

	// 监听器列表
	private List<PianoKeyListener> listeners;

	/**
	 * 创建PianoKey。在这之后必须调用setBounds。
	 * @param no,键的索引.  0 表示钢琴上最左边的键。no 不是音高，但是no 和音高有一致的差异。.
	 */
	public PianoKey(Piano piano, int no) {
		this.piano = piano;
		this.no = no;
		this.pitchOffset = no;
		this.isChromatic = MusicalNote.isChromatic(pitchOffset);
		this.bounds = null;
		this.isDown = false; // 一开始没有琴键被按下。
		
		this.listeners = new ArrayList<PianoKeyListener>();
	}
	
	/**
	 * 返回此钢琴键的绘图范围是否包含该点。
	 * @param point
	 * @return true if yes, false otherwise
	 */
	public boolean containPoint(Point point) {
		return bounds.contains(point);
	}

	/**
	 * 返回琴键的填充颜色 (for drawing purpose).
	 * @return
	 */
	public Color getFillColor() {
		return (isChromatic) ?
				((isDown) ? PianoKey.COLOR_DOWN_CHROMATIC : PianoKey.COLOR_NORMAL_CHROMATIC)
				:
				((isDown) ? PianoKey.COLOR_DOWN_PLAIN : PianoKey.COLOR_NORMAL_PLAIN);
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public boolean isChromatic() {
		return isChromatic;
	}
	
	public boolean isDown() {
		return isDown; 
	}
	
	public int getPitch() {
		return pitchOffset + piano.getBasePitch(); //no+48
	}
	
	public int getNo() {
		return no;
	}
	
	/*########################################################################
	 *  S E T T E R S
	 *########################################################################*/

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	/**
	 * 设置琴键是否被按下。并且，这个方法播放音符。
	 * @param isDown true means down, false means up
	 */
	public void setDown(boolean isDown) {
		if (!this.isDown && isDown) {
			MusicManager.getInstance().playNote(getPitch());
		} else if (this.isDown && !isDown) {
			// TODO: 在音符停止前，八度变化了将会如何？
			MusicManager.getInstance().stopNote(getPitch());
		}
			
		this.isDown = isDown;
		fireNeedsRedraw(); // even though isDown doesn't change 
	}
	
	/*########################################################################
	 *  L I S T E N E R S
	 *########################################################################*/

	/**
	 * 注册特定的监视器
	 * @param listener
	 */
	public void addListener(PianoKeyListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 除去特定的监视器
	 * @param listener
	 */
	public void removeListener(PianoKeyListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fires pianoKeyNeedsRedraw method in the registered listeners.
	 */
	private void fireNeedsRedraw() {
		for (PianoKeyListener listener: listeners)
			listener.pianoKeyNeedsRedraw(this);
	}
}
