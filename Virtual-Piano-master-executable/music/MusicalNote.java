package music;

/**
 * 封装一个单独的抽象音符（只有模型）
 * 当前只被用作一个包含静态方法的名字空间。
 * 
 * @author group
 */
public class MusicalNote {
	
	//一个8度包含 C，C#，D，D#，E，F，F#，G，G#，A，Bb,B共12个音符
	public static final int OCTAVE_PITCH_DELTA = 12;
	
	/**
	 * 返回指定的音调是否为半音。半音音符：
	 * C#, D#, F#, Ab, Bb.
	 * @param 音调
	 * @return 返回true表示是半音，否则是其他
	 */
	public static boolean isChromatic(int pitch) {
		int basePitch = pitch % MusicalNote.OCTAVE_PITCH_DELTA;
		return (
				basePitch == 1 ||
				basePitch == 3 ||
				basePitch == 6 ||
				basePitch == 8 ||
				basePitch == 10
		);
	}
	
}
