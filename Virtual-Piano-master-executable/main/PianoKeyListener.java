package main;

/**
 * �ټ��ϼ����¼��Ľӿ�
 * @author group
 */
public interface PianoKeyListener {
	/**
	 * Called when the specified piano key needs redraw.
	 * @param pianoKey
	 */
	public void pianoKeyNeedsRedraw(PianoKey pianoKey);
}
