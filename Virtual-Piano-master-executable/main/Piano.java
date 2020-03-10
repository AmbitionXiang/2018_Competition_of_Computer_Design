package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import music.MusicManager;
import music.MusicalNote;
import resource.ImageResource;
import resource.LayoutConstant;

/**
 * 钢琴面板包含点击琴键和踏板。
 * @author group
 */
public class Piano extends JPanel {

	private static final int NUM_OCTAVES = 3;//更改此处，可改变显示的琴键数，不过需要增多keymap
	//琴键数量选取了3个八度
	private static final int NUM_KEYS = MusicalNote.OCTAVE_PITCH_DELTA * Piano.NUM_OCTAVES + 1;
	//每八度有12个琴键
	private static final int NUM_KEYS_PER_OCTAVE = 12;
	private static final int KEY_NOT_FOUND = -1;

	// 默认的基础音调(即键盘最低处的琴键对应的音调序号，例如序号48对应C4-根据音阶频率表)
	private static final int DEFAULT_BASE_PITCH = 48;
	private static final int MIN_BASE_PITCH = 0;
	private static final int MAX_BASE_PITCH = 216;

	private List<PianoKey> pianoKeys;  //琴键
	private Pedal pedal;  //踏板

	// Piano JPanel的描述
	private int width;
	private int height;
	private Point pedalPos; //踏板位置
	//谱子面板
	private Song song;
	//谱子搜索文本框
	private JTextField wanted_song = new JTextField(20);
	//谱子图片格式
	private JTextField type =new JTextField(6);
	//谱子搜索按钮
	private JButton search = new JButton("Search and show");
	
	private int currentHovered = KEY_NOT_FOUND;
	
	private int basePitch = DEFAULT_BASE_PITCH; //基础音调
	
	private Map<Integer, Integer> keyMap;//琴键的映射
	
	/**
	 * 默认构造函数
	 */
	public Piano() {
		createKeys();		
		createPedal();
		initKeyMap();
		this.setLayout(null);
		wanted_song.setBounds(new Rectangle(240,460,200,40)); //300
		type.setBounds(450,460,50,40);
		search.setBounds(510, 460, 170, 40);

		add(wanted_song);//有待美观
		add(type);
		add(search);//有待美观
		
		search.setBackground(Color.black);	////
		int selectedstyle=Font.BOLD+Font.ITALIC;	////
		search.setFont(new Font("Serif",selectedstyle,17));		////
		type.setFont(new Font("Serif",selectedstyle,17));
		wanted_song.setFont(new Font("Serif",selectedstyle,20));////
		
		
		
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//谱子框架
				JFrame songFrame = new JFrame("Spectrum of Song");
				//输入为空异常处理
				if(wanted_song.getText().length() == 0)
				{
					Object[] options = { "OK", "CANCEL" }; 
					JOptionPane.showOptionDialog(null, "Input is not allowed to be empty！", "Warning", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
					null, options, options[0]); 
					return;
				}
				//设置弹出的frame
				songFrame.setLocationByPlatform(true); //设置在下次窗口可见时，窗口是应显示位置
				songFrame.setLayout(new GridLayout());
				songFrame.add(new Song(wanted_song.getText(),type.getText())); //将之放入搜索引擎。
				songFrame.pack();
				songFrame.setVisible(true);
				requestFocus(true);

			}
		});
		


		// 在createKeys()中琴键宽度width已经被指定
		height = LayoutConstant.keyFrameTop + LayoutConstant.keyFrameHeight + LayoutConstant.pedalPadding +
				 LayoutConstant.pedalHeight + LayoutConstant.pianoKeyBottom + 20;
		setPreferredSize(new Dimension(width, height));//为了适应pack(),否则pack()将失效
		setFocusable(true); // 聚焦界面，使之可以读取按键值
		
		// 监听器接口
		addMouseMotionListener(new PianoMouseMotionListener());
		addMouseListener(new PianoMouseListener());
		addKeyListener(new PianoViewKeyListener());
	}

	/**
	 * 初始化琴键映射的引导程序。键映射是一个从虚拟键代码到键号的映射。 
	 */
	private void initKeyMap() {
		keyMap = new HashMap<Integer, Integer>();
		keyMap.put(KeyEvent.VK_Z, 12 * 0 + 0); // C4
		keyMap.put(KeyEvent.VK_S, 12 * 0 + 1);
		keyMap.put(KeyEvent.VK_X, 12 * 0 + 2);
		keyMap.put(KeyEvent.VK_D, 12 * 0 + 3);
		keyMap.put(KeyEvent.VK_C, 12 * 0 + 4);
		keyMap.put(KeyEvent.VK_V, 12 * 0 + 5);
		keyMap.put(KeyEvent.VK_G, 12 * 0 + 6);
		keyMap.put(KeyEvent.VK_B, 12 * 0 + 7);    
		keyMap.put(KeyEvent.VK_H, 12 * 0 + 8);
		keyMap.put(KeyEvent.VK_N, 12 * 0 + 9);
		keyMap.put(KeyEvent.VK_J, 12 * 0 + 10);
		keyMap.put(KeyEvent.VK_M, 12 * 0 + 11);
		keyMap.put(KeyEvent.VK_Q, 12 * 1 + 0); // C5
		keyMap.put(KeyEvent.VK_2, 12 * 1 + 1);
		keyMap.put(KeyEvent.VK_W, 12 * 1 + 2);
		keyMap.put(KeyEvent.VK_3, 12 * 1 + 3);
		keyMap.put(KeyEvent.VK_E, 12 * 1 + 4);
		keyMap.put(KeyEvent.VK_R, 12 * 1 + 5);
		keyMap.put(KeyEvent.VK_5, 12 * 1 + 6);
		keyMap.put(KeyEvent.VK_T, 12 * 1 + 7);
		keyMap.put(KeyEvent.VK_6, 12 * 1 + 8);
		keyMap.put(KeyEvent.VK_Y, 12 * 1 + 9);
		keyMap.put(KeyEvent.VK_7, 12 * 1 + 10);
		keyMap.put(KeyEvent.VK_U, 12 * 1 + 11);
		keyMap.put(KeyEvent.VK_I, 12 * 2 + 0); // C6
		keyMap.put(KeyEvent.VK_9, 12 * 2 + 1);
		keyMap.put(KeyEvent.VK_O, 12 * 2 + 2);
		keyMap.put(KeyEvent.VK_0, 12 * 2 + 3);
		keyMap.put(KeyEvent.VK_P, 12 * 2 + 4);
		keyMap.put(KeyEvent.VK_OPEN_BRACKET, 12 * 2 + 5);
		keyMap.put(KeyEvent.VK_EQUALS, 12 * 2 + 6);
		keyMap.put(KeyEvent.VK_CLOSE_BRACKET, 12 * 2 + 7);
		keyMap.put(KeyEvent.VK_BACK_SPACE, 12 * 2 + 8);
		keyMap.put(KeyEvent.VK_BACK_SLASH, 12 * 2 + 9);

	}

	/**
	 * 创建钢琴键，初始化，注册监听器，并填充宽度。
	 */
	private void createKeys() {
		pianoKeys = new ArrayList<PianoKey>();
		
		// the left coordinate of the next plain key
		int pianoKeyCurLeft = LayoutConstant.pianoKeyLeft;
		
		// creates the PianoKey's and position them correctly
		for (int i = 0; i < Piano.NUM_KEYS-1; i++) {
			PianoKey pianoKey = new PianoKey(this, i);
			
			// 放置琴键
			if (pianoKey.isChromatic()) {
				pianoKey.setBounds(new Rectangle(
						pianoKeyCurLeft - LayoutConstant.chromaticKeyWidth / 2,
						LayoutConstant.pianoKeyTop,
						LayoutConstant.chromaticKeyWidth,
						LayoutConstant.chromaticKeyHeight));
			} else {
				pianoKey.setBounds(new Rectangle(
						pianoKeyCurLeft,
						LayoutConstant.pianoKeyTop,
						LayoutConstant.plainKeyWidth,
						LayoutConstant.plainKeyHeight));
				pianoKeyCurLeft += LayoutConstant.keyLeftOffset; // 位置移动到下一个白键的左边
			}
			
			// 注册监听器
			pianoKey.addListener(new PianoPianoKeyListener());
			
			pianoKeys.add(pianoKey);
		}
		
		width = pianoKeyCurLeft + LayoutConstant.pianoKeyRight; // JPanel Piano的精确宽度
	}

	/**
	 * 创建踏板，并初始化，注册监听器。 填充pedalPos.
	 */
	private void createPedal() {
		pedal = new Pedal();

		// 注册监听器
		pedal.addListener(new PianoPedalListener());
		
		// 我们能够假定已经确定Piano的宽度
		pedalPos = new Point((width - LayoutConstant.pedalWidth) / 2 +60 ,
				 LayoutConstant.pianoKeyTop + LayoutConstant.keyFrameHeight + 70+ + LayoutConstant.pedalPadding);
	}
	
	/**
	 * 停止当前所有发音的音符。
	 */
	public void reset() {
		pedal.setDown(false);
		for (PianoKey pianoKey: pianoKeys) {
			pianoKey.setDown(false);
		}
	}
	
	/**
	 * 画整个 Piano JPanel.
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBackGround(g);
		paintKeys(g);
		paintPedalArea(g);
		paintName(g);
	}

	@Override
	public void repaint() {
		//Utilities.trace("Piano repaint()");
		super.repaint();
	}
	
	/**
	 * Paints all the piano keys with its frames.
	 * @param g
	 */
	private void paintKeys(Graphics g) {		
		// paint key frames
		for (int i = 0; i < 7*Piano.NUM_OCTAVES-1; i++) {
			g.setColor(PianoKey.COLOR_KEY_FRAME);
			g.drawRect(LayoutConstant.keyFrameLeft + i * LayoutConstant.keyLeftOffset,
					   LayoutConstant.keyFrameTop, LayoutConstant.keyFrameWidth - 1,
					   LayoutConstant.keyFrameHeight - 1);
		}
		
		// plain first
		for (PianoKey key: pianoKeys) {
			if (!key.isChromatic()) {
				g.setColor(key.getFillColor());
				Rectangle bounds = key.getBounds();
				g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
		
		// chromatic second
		for (PianoKey key: pianoKeys) {
			if (key.isChromatic()) {
				g.setColor(key.getFillColor());
				Rectangle bounds = key.getBounds();
				g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
		
	}
	
	/**
	 * Paints the pedal area (the area below piano keys).
	 * @param g
	 */

	private void paintBackGround(Graphics g) {
		//g.setColor(LayoutConstant.bgc);
		//g.fillRect(0, 0, width, height);
		g.drawImage(ImageResource.getImage(ImageResource.BACKGROUND), 0, 0,width,height, null);
	}
	private void paintPedalArea(Graphics g) {
		// background
		//g.setColor(LayoutConstant.pianoBackgroundColor);
		//g.fillRect(LayoutConstant.explaintoleft, LayoutConstant.keyFrameTop - LayoutConstant.pedalAreaHeight, (width/2-LayoutConstant.explaintoleft)*2, LayoutConstant.pedalAreaHeight);
		
		// pedal
		g.drawImage(pedal.getImage(), pedalPos.x, pedalPos.y, null);
		
		// instrument number and octave number
		g.setFont(LayoutConstant.instrumentNumberFont);
		g.setColor(LayoutConstant.instrumentNumberColor);
		g.drawString("instrument: "+MusicManager.getInstance().getSynthInstrument() + ". " +
				MusicManager.getInstance().getInstrumentName(),
				LayoutConstant.explaintoleft,
				LayoutConstant.keyFrameTop + LayoutConstant.keyFrameHeight + 2*LayoutConstant.instrumentNumberPadding);
		
		g.drawString("major key:C" + getBasePitch() / 12 ,
				LayoutConstant.explaintoleft + 500,
				LayoutConstant.keyFrameTop + LayoutConstant.keyFrameHeight + 2*LayoutConstant.instrumentNumberPadding );
		
		g.setFont(LayoutConstant.pedalNumberFont);
		g.drawString("Pedal: " ,
				LayoutConstant.explaintoleft + 290,
				LayoutConstant.keyFrameTop + LayoutConstant.keyFrameHeight + 3*LayoutConstant.instrumentNumberPadding + 15 );
			
		g.setFont(LayoutConstant.helpFont);
		g.drawString("press F12 for help", LayoutConstant.helptoleft, LayoutConstant.helptotop);
	}
	
	private void paintName(Graphics g) {
		g.setFont(new Font("Edwardian Script ITC",Font.BOLD,90));
		g.setColor(new Color(0.75f,1.0f,1.0f));
		g.drawString("Piano Master", width/2-200,120);
	}
	
	/**
	 * Paints only the given PianoKey and its neighboring keys.
	 * 该方法目前没被用到
	 * @param g
	 * @param pianoKey
	 */
	private void paintKey(Graphics g, PianoKey pianoKey) {
		// if the specified key is chromatic, draw neighbors first
		// otherwise, draw the specified key first
		
		// nothing
	}

	/**
	 * Paints only the pedal area.
	 * 该方法目前没被用到
	 * @param g
	 */
	private void paintPedal(Graphics g) {
		// nothing
	}
	
	/**
	 * 返回鼠标悬停点的键号
	 * Returns KEY_NOT_FOUND if the point is not inside any key.
	 * @param point
	 * @return the key number or KEY_NOT_FOUND
	 */
	public int getKeyNoHovered(Point point) {
		// chromatic first
		for (PianoKey key: pianoKeys) {
			if (key.isChromatic())
				if (key.containPoint(point))
					return key.getNo();
		}

		// plain second
		for (PianoKey key: pianoKeys) {
			if (!key.isChromatic())
				if (key.containPoint(point))
					return key.getNo();
		}
		
		return Piano.KEY_NOT_FOUND;
		
	}
	
	/**
	 * 返回Pinao实际的width,我们没用JComponent中默认的getWidth方法
	 * 因为component被画之前，getWidth会返回0.
	 * @return the width 
	 */
	public int getPianoWidth() {
		return width;
	}
	
	/**
	 * 返回Piano的base pitch。该方法被PianoKeys使用
	 * @return the base pitch 
	 */
	public int getBasePitch() {
		return basePitch;
	}
	
	/*########################################################################
	 *  S E T T E R S
	 *########################################################################*/

	/**
	 * 将base pitch提高一个八度
	 */
	public void incOctave() {
		if (basePitch < MAX_BASE_PITCH) {
			basePitch += NUM_KEYS_PER_OCTAVE;
		}
	}
	
	/**
	 * 将base pitch降低一个八度
	 */
	public void decOctave() {
		if (basePitch > MIN_BASE_PITCH) {
			basePitch -= NUM_KEYS_PER_OCTAVE;
		}
	}
	
	/**
	 * 将当前的键号设置为一个新的数字，即切换琴键（只是切换鼠标的悬停位置）
	 * @param no ：新的键号
	 */
	private void setCurrentHovered(int no) {
		if (currentHovered != no && currentHovered != Piano.KEY_NOT_FOUND) {
			// force release the previous hovered key
			pianoKeys.get(currentHovered).setDown(false);
		}
		
		currentHovered = no;
	}
	
	/*########################################################################
	 *  L I S T E N E R   I N T E R F A C E S
	 *########################################################################*/
	
	/**
	 * 处理鼠标移动。每当鼠标指针退出钢琴键时进行检测。
	 */
	private class PianoMouseMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			setCurrentHovered(getKeyNoHovered(e.getPoint()));
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}
	}
	
	/**
	 * 处理钢琴按键上的鼠标按下和释放。
	 */
	private class PianoMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int hovered = getKeyNoHovered(e.getPoint());
			if (hovered != Piano.KEY_NOT_FOUND)
				pianoKeys.get(hovered).setDown(true);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			int hovered = getKeyNoHovered(e.getPoint());
			if (hovered != Piano.KEY_NOT_FOUND)
				pianoKeys.get(hovered).setDown(false);
		}
	}
	
	/**
	 *Handles pedal up / down with keyboard. 
	 */
	private class PianoViewKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			if (keyCode == LayoutConstant.pedalKey) {
				pedal.setDown(true);
			} else if (keyCode == KeyEvent.VK_LEFT) { // instrument --
				MusicManager.getInstance().decSynthInstrument();
				repaint();
			} else if (keyCode == KeyEvent.VK_RIGHT) { // instrument ++
				MusicManager.getInstance().incSynthInstrument();
				repaint();
			} else if (keyCode == KeyEvent.VK_PAGE_UP) { // octave ++
				//MusicManager.getInstance().play();
				reset();
				incOctave();
				repaint();
			} else if (keyCode == KeyEvent.VK_PAGE_DOWN) { // octave --

				reset();
				decOctave();
				repaint();
			} else if (keyCode == KeyEvent.VK_ENTER) { // reset
				reset();
				repaint();
			} else if(keyCode == KeyEvent.VK_HOME){
				MusicManager.getInstance().play();
			}else if(keyCode == KeyEvent.VK_END){
				MusicManager.getInstance().stop();
			} else if(keyCode == KeyEvent.VK_INSERT) {
				MusicManager.getInstance().startRecording();
			} else if(keyCode == KeyEvent.VK_F12)
			{
				String help="Page Up & Page Down to change scale\n";
				help+="Left & Right to change instruments\n";
				help+="Space For Pedal\n";
				JOptionPane.showMessageDialog(null,help , "Help", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				if (keyMap.containsKey(keyCode))
					pianoKeys.get(keyMap.get(keyCode)).setDown(true);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == LayoutConstant.pedalKey) {
				pedal.setDown(false);
			} else {
				if (keyMap.containsKey(keyCode))
					pianoKeys.get(keyMap.get(keyCode)).setDown(false);
			}
		}
	}
	
	/**
	 * Responds to piano key's notification to redraw. 
	 */
	private class PianoPianoKeyListener implements PianoKeyListener {
		public void pianoKeyNeedsRedraw(PianoKey pianoKey) {
			repaint();
		}
	}
	
	/**
	 * Responds to pedal's notification to redraw. 
	 */
	private class PianoPedalListener implements PedalListener {
		public void pedalNeedsRedraw(Pedal pedal) {
			repaint();
		}
	}
	
}
