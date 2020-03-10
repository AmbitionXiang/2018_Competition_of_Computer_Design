package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import music.MusicManager;
import music.MusicalNote;

/**
 * ��װһ����һ�ĸ��ټ���֪���������ߣ���ɫ���ƣ�λ�õȡ�
 * @author Group
 *
 */
public class PianoKey {

	public static final Color COLOR_KEY_FRAME = new Color(0.0f, 0.0f, 0.0f); // frame�ټ��ı�
	
	public static final Color COLOR_NORMAL_PLAIN = new Color(1.0f, 1.0f, 1.0f);
	public static final Color COLOR_DOWN_PLAIN = new Color(1.0f, 1.0f, 0.5f); // ����ɫ
	public static final Color COLOR_NORMAL_CHROMATIC = new Color(0.0f, 0.0f, 0.0f);
	public static final Color COLOR_DOWN_CHROMATIC = new Color(0.5f, 0.5f, 0.2f); // ����ɫ
	
	// I decide to do this (even though a little dirty) because the Piano is the
	// context of a PianoKey.  A PianoKey cannot live without the Piano it
	// belongs to.
	private Piano piano;
	
	private int no;
	private int pitchOffset;
	private boolean isChromatic;
	private Rectangle bounds;
	private boolean isDown;

	// �������б�
	private List<PianoKeyListener> listeners;

	/**
	 * ����PianoKey������֮��������setBounds��
	 * @param no,��������.  0 ��ʾ����������ߵļ���no �������ߣ�����no ��������һ�µĲ��졣.
	 */
	public PianoKey(Piano piano, int no) {
		this.piano = piano;
		this.no = no;
		this.pitchOffset = no;
		this.isChromatic = MusicalNote.isChromatic(pitchOffset);
		this.bounds = null;
		this.isDown = false; // һ��ʼû���ټ������¡�
		
		this.listeners = new ArrayList<PianoKeyListener>();
	}
	
	/**
	 * ���ش˸��ټ��Ļ�ͼ��Χ�Ƿ�����õ㡣
	 * @param point
	 * @return true if yes, false otherwise
	 */
	public boolean containPoint(Point point) {
		return bounds.contains(point);
	}

	/**
	 * �����ټ��������ɫ (for drawing purpose).
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
	 * �����ټ��Ƿ񱻰��¡����ң������������������
	 * @param isDown true means down, false means up
	 */
	public void setDown(boolean isDown) {
		if (!this.isDown && isDown) {
			MusicManager.getInstance().playNote(getPitch());
		} else if (this.isDown && !isDown) {
			// TODO: ������ֹͣǰ���˶ȱ仯�˽�����Σ�
			MusicManager.getInstance().stopNote(getPitch());
		}
			
		this.isDown = isDown;
		fireNeedsRedraw(); // even though isDown doesn't change 
	}
	
	/*########################################################################
	 *  L I S T E N E R S
	 *########################################################################*/

	/**
	 * ע���ض��ļ�����
	 * @param listener
	 */
	public void addListener(PianoKeyListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * ��ȥ�ض��ļ�����
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
