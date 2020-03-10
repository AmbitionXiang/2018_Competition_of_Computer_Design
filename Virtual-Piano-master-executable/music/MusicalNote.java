package music;

/**
 * ��װһ�������ĳ���������ֻ��ģ�ͣ�
 * ��ǰֻ������һ��������̬���������ֿռ䡣
 * 
 * @author group
 */
public class MusicalNote {
	
	//һ��8�Ȱ��� C��C#��D��D#��E��F��F#��G��G#��A��Bb,B��12������
	public static final int OCTAVE_PITCH_DELTA = 12;
	
	/**
	 * ����ָ���������Ƿ�Ϊ����������������
	 * C#, D#, F#, Ab, Bb.
	 * @param ����
	 * @return ����true��ʾ�ǰ���������������
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
