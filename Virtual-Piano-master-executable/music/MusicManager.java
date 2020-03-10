package music;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.swing.JOptionPane;

import util.ErrorHandler;
import util.Utilities;

/**
 * A class that handles MIDI music playing.  This class takes care of the
 * Sequencer, provides functions to play and stop music, and to play a single
 * note on a special channel.
 * 
 * 这个类将控制default MIDI Sequencer and Synthesizer当程序运行的时候
 * 
 * 任何播放的 MIDI sequence 禁止占用最后的channel （最高标号）
 * 因为那个channel被用来 synthesizing.（合成）
 * 
 * Implements singleton pattern.
 * 
 * @author group
 *
 */
public class MusicManager {

	private static final String instrumentFileName = "resources/data/instruments.txt";
	
	// single instance
	private static MusicManager musicManager;

	public static final int NOTE_ON=144;
	public static final int NOTE_OFF=128;
	public static final int NUM_INSTRUMENT = 128;

	public static  int isRuning=0;
	private static int TICK=0;
	// default values for synthesizer
	public static final int SYNTH_CHANNEL_NO = 15;
	public static final int SYNTH_NOTE_VELOCITY = 120;
	public static final int SYNTH_INSTRUMENT = 0; // 声学钢琴 

	// pedal
	public static final int PEDAL_ID = 64;
	public static final int PEDAL_ON = 127;
	public static final int PEDAL_OFF = 0;
	
	private Sequencer sequencer;
	private Synthesizer synth;
	private MidiChannel synthChannel;
	private int synthInstrument;
	private Sequence sequence;

	private static List<String> instrumentNames;
	
	/**
	 * 返回默认实例
	 * @return the default instance
	 */
	public static MusicManager getInstance() {
		if (musicManager == null)
			musicManager = new MusicManager();
		
		return musicManager;
	}

	/**
	 * 初始化默认实例.  必须在程序open the sequencer之前调用。
	 */


    static class myThread extends Thread
	{
    	boolean r=true;
    	
		public void run()
		{
		    while(true)
            {
	    		try {
                sleep(135);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
		    	if(r)
		    	{	
	                TICK++;
		    	}
            }
		}
	}
    
    static myThread thread=new myThread();

	public static void init() {
		musicManager = new MusicManager();
		initInstrumentNames();
		thread.start();
	}

	/**
	 * 向私有静态列表填充乐器名称
	 */
	private static void initInstrumentNames() {
		instrumentNames = new ArrayList<String>();
		try {
			URL url = Utilities.getResourceURL(MusicManager.instrumentFileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			// 逐行读。空行结束循环。
			while (true) {
				String line = in.readLine();
				if (line == null || line.trim().isEmpty()) {
					break;
				} else {					
					instrumentNames.add(line);
				}
			}

			in.close();
			
		} catch (IOException e) {
			ErrorHandler.display("Cannot read MIDI instrument names");
		}

		if (instrumentNames.size() != MusicManager.NUM_INSTRUMENT) {
			ErrorHandler.display("Wrong MIDI instrument names data");
		}
		
		// 容错：追加空字符串直到大小至少为128（NUM_INSTRUMENT)
		while (instrumentNames.size() < MusicManager.NUM_INSTRUMENT)
			instrumentNames.add(""); // 默认以空字符串填充

	}

	/**
	 * 返回指定ID的乐器名称
	 * @param id ：假定在正确的范围内(0..127)
	 * @return
	 */
	public static String getInstrumentName(int id) {
		return MusicManager.instrumentNames.get(id);
	}
	
	/**
	 *默认构造函数。初始化 sequencer.
	 */
	private MusicManager() {
		try {
			// init sequencer
			sequencer = MidiSystem.getSequencer();
			sequencer.open();

			// init synthesizer
			synth = MidiSystem.getSynthesizer();
			synth.open();
			
			// DEBUG
			//System.out.print("latency = " + synth.getLatency());
			//System.out.print("max polyphony = " + synth.getMaxPolyphony());

			// get channel for synthesizing: the highest numbered channel.  sets it up
			MidiChannel[] channels = synth.getChannels();
			synthChannel = channels[channels.length - 1];
			setSynthInstrument(MusicManager.SYNTH_INSTRUMENT);

		} catch (MidiUnavailableException e){
			ErrorHandler.display("Cannot play MIDI music");
			sequencer = null; // remember this!  sequencer can be null.
			return;
		}
		
	}
	
	/**
	 * Releases the sequencer.
	 */
	@Override
	protected void finalize() throws Throwable {
		if (synth != null)
			synth.close();
		if (sequencer != null)
			sequencer.close();
		super.finalize();
	}

	/**
	 * Plays the supplied Sequence.  Only one MIDI music can play at the same
	 * time.  If a music is currently playing, the previous music will be
	 * stopped first.
	 * @param sequence
	 */
	public void startRecording()
    {
        try {
            isRuning=1;
            thread.r=true;
            TICK=0;
            sequence = new Sequence(Sequence.PPQ,4);
        }catch (InvalidMidiDataException e){
            e.printStackTrace();
        }
        sequence.createTrack();


    }

	public void play() {
		if (sequencer == null) return;
		isRuning=0;
		thread.r=false;
		sequencer.stop();
		sequencer.close();
		try {
			sequencer.open();
		} catch (MidiUnavailableException e) {
			ErrorHandler.display("Cannot play MIDI music");
			return;
		}
		
		try {

			sequencer.setSequence(sequence);
			sequencer.setLoopCount(0);
			sequencer.start();
		} catch (Exception e) {
			ErrorHandler.display("You havn't started recording!");
			// no error recovery
		}
	}
	
	/**
	 * Stops whatever is currently playing.
	 */
	
	public void stop() {

		    if(sequence==null) 
			{
				ErrorHandler.display("You havn't started recording!");
				return;
			}
		    
		    sequence.getTracks()[0].add(createEvent(NOTE_OFF,SYNTH_CHANNEL_NO,0,MusicManager.SYNTH_NOTE_VELOCITY,TICK));
			
		    isRuning=0;
		    
		    thread.r=false;
		    
		    TICK=0;
		    
		    String path="resources/music/";
			
			File f=new File(path);
			
			if(!f.exists())
			{
				f.mkdirs();
			}
		
			String filename=System.currentTimeMillis()+".mid";
			
			
			
			File file=new File(f,filename);
			
			if(!file.exists())
			{
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.out.println("Me!");
				}
			}
		try {
			System.out.println(file.getPath());
			
		   	 MidiSystem.write(sequence,0,file);
		   	 JOptionPane.showMessageDialog(null,"已保存到文件: "+file.getAbsolutePath() , "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e) {
        	ErrorHandler.display("You havn't started recording!");
            //e.printStackTrace();
        }
	}
	
	/**
	 * Plays a single note with a default instrument in the synth channel.
	 * @param pitch an int, 0 = C0, 60 = middle C
	 */
	public MidiEvent createEvent(int comd, int channel, int pitch, int velocity, int tick){
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd,channel,pitch,velocity);
			//琛ㄧず鍦╰ick鎷嶅惎鍔╝杩欎釜Message
			event = new MidiEvent(a, tick);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return event;
	}

	public void playNote(int pitch) {

		synthChannel.noteOn(pitch, MusicManager.SYNTH_NOTE_VELOCITY);
        if(isRuning>0){
            sequence.getTracks()[0].add(createEvent(NOTE_ON,SYNTH_CHANNEL_NO,pitch,MusicManager.SYNTH_NOTE_VELOCITY,TICK));
            sequence.getTracks()[0].add(createEvent(NOTE_OFF,SYNTH_CHANNEL_NO,pitch,MusicManager.SYNTH_NOTE_VELOCITY,TICK+1));
        }

	}




	/**
	 * Stop a single note with a default instrument in the synth channel.
	 * @param pitch an int, 0 = C0, 60 = middle C
	 */
	public void stopNote(int pitch) {
		synthChannel.noteOff(pitch, 127);
	}

	/**
	 * Pedal on in synthesizer.
	 */
	public void pedalDown() {
		synthChannel.controlChange(MusicManager.PEDAL_ID, MusicManager.PEDAL_ON);
	}

	/**
	 * Pedal off in synthesizer.
	 */
	public void pedalUp() {
		synthChannel.controlChange(MusicManager.PEDAL_ID, MusicManager.PEDAL_OFF);
	}
	
	/**
	 * Sets a new instrument for the synthesizer and change the instrument for real.
	 * @param synthInstrument
	 */
	public void setSynthInstrument(int synthInstrument) {
		// no error checking
		this.synthInstrument = synthInstrument;
		synthChannel.programChange(synthInstrument);
	}

	public void decSynthInstrument() {
		if (synthInstrument > 0) {
			setSynthInstrument(synthInstrument - 1);
		} else {
			setSynthInstrument(MusicManager.NUM_INSTRUMENT - 1);
		}
	}
	
	public void incSynthInstrument() {
		if (synthInstrument < MusicManager.NUM_INSTRUMENT - 1) {
			setSynthInstrument(synthInstrument + 1);
		} else {
			setSynthInstrument(0);
		}
	}
	
	public int getSynthInstrument() {
		return synthInstrument;
	}
	
	/**
	 * Returns the name of the current instrument in synthesizer channel.
	 * @return the name of the instrument
	 */
	public String getInstrumentName() {
		return MusicManager.getInstrumentName(synthInstrument);
	}
}
