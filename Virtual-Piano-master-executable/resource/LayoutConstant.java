package resource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 * �����ڲ������õ������ó����ȡ�
 * ���г���������ͨ��������á�������Ĭ��ʵ��ֻ�Ƿ���˽�о�̬������ֵ����δ��������ܻ�ı䡣
 * @author Group
 *
 */
public class LayoutConstant {
	// mainframe��س���
	
	public static final int heightbeforepiano=100;
	public static final int pianotoleft=100;
	public static final int pianotoright=100;
	
	// piano
	public static final Color pianoBackgroundColor = new Color(0.4f, 0.1f, 0.1f);
	
	// spectrum
	public static final int spectrumLeft = 100;
	public static final int spectrumTop = 140;
	public static final int spectrumWidth = 600;
	public static final int spectrumHeight = 500;
	
	public static final int pianoKeyBottom=100;  //�����·��߶ȣ�ԭ100��
	public static final int pianoKeyLeft = 100;  //������߿��
	public static final int pianoKeyTop = 170;  //�����Ϸ��߶ȣ�ԭ170��
	public static final int pianoKeyRight=100;  //�����ұ߿��
	public static final int keyFrameLeft = pianoKeyLeft - 1;
	public static final int keyFrameTop = pianoKeyTop - 1;
	
	public static final int plainKeyWidth = 36;  //��ͨ�ټ�
	public static final int plainKeyHeight = 180;
	public static final int chromaticKeyWidth = 20;  //�����ټ�
	public static final int chromaticKeyHeight = 100;
	public static final int keyLeftOffset = plainKeyWidth + 1;
	public static final int keyFrameWidth = plainKeyWidth + 2;
	public static final int keyFrameHeight =  plainKeyHeight + 2;
	
	public static final int pedalWidth = 48; //̤�����εĳ���
	public static final int pedalHeight = 36;
	public static final int pedalPadding = 4;
	public static final int pedalAreaHeight = pedalPadding + pedalHeight;
	
	// instrument number���й��������͵���ʾ��
	public static final int instrumentNumberLeft = 10;
	public static final int instrumentNumberPadding = 25;
	public static final Color instrumentNumberColor = new Color(1.0f, 1.0f, 1.0f);
	public static final Font instrumentNumberFont = new Font("Axure Handwriting", Font.ITALIC, 25);
	
	//pedal̤������
	public static final Font pedalNumberFont = new Font("Axure Handwriting", Font.ITALIC , 25);
	
	//explain area
	public static final int explaintoleft=150;
	public static final Color eplainbgc=new Color(1.0f,1.0f,1.0f);
	public static final int explainlength=400;
	
	//background
	public static final Color bgc=new Color(0.0f,0.0f,0.0f);
	
	// octave number
	public static final int octaveNumberLeft = 560;
	
	// canvas
	public static final int canvasMinHeight = 350;
	public static final int buttonRightBottomPadding = 10;
	
	// bye dialog ��رպ���ֵĶԻ���
	public static final String byeTitle = "Bye";
	public static final String byeMessage = "Bye?";
	public static final String[] byeOptions = new String[]{"Bye!"};
	
	// keys ̤�崥��
	public static final int pedalKey = KeyEvent.VK_SPACE;	
	
	// help
	public static final int helptoleft = 800;  //���� �������
	public static final int helptotop = 30;   //���� ���붥����ԭ����500��
	public static final Font helpFont = new Font("Axure Handwriting", Font.ITALIC , 20);
}
