package main;

/**
 * 琴键上监听事件的接口
 * @author group
 */
public interface PianoKeyListener {
	/**
	 * Called when the specified piano key needs redraw.
	 * @param pianoKey
	 */
	public void pianoKeyNeedsRedraw(PianoKey pianoKey);
}
