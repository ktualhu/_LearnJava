import javax.swing.*;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
package myPack.pack

public class BeatBox01 {
	JPanel mainPanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame theFrame;
	JLabel tempoLabel;
	boolean isBeatStarted = false;
	
	String[] instrumentName = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap", 
	"High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", 
	"Open Hi Conga"}; 
	
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63}; 
	double tempo = 120;
	
	public static void main(String[] args) {
		BeatBox01 beatBox = new BeatBox01();
		beatBox.buildGui();
	}
	
	public void buildGui() {
		theFrame = new JFrame("BeatBox.01");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton btStart = new JButton("Start");
		btStart.addActionListener(new StartButton());
		buttonBox.add(btStart);
		
		JButton btStop = new JButton("Stop");
		btStop.addActionListener(new StopButton());
		buttonBox.add(btStop);
		
		JButton btTempoUp = new JButton("Tempo Up");
		btTempoUp.addActionListener(new TempoUpButton());
		buttonBox.add(btTempoUp);
		
		tempoLabel = new JLabel("Tempo: " + tempo + " BPM");
		buttonBox.add(tempoLabel);
		
		JButton btTempoDown = new JButton("Tempo Down");
		btTempoDown.addActionListener(new TempoDownButton());
		buttonBox.add(btTempoDown);
		
		JButton btSaveBeat = new JButton("Save Beat");
		btSaveBeat.addActionListener(new MySendListener());
		buttonBox.add(btSaveBeat);
		
		JButton btLoadBeat = new JButton("Load Beat");
		btLoadBeat.addActionListener(new MyReadListener());
		buttonBox.add(btLoadBeat);
		
		JButton btRemoveBeat = new JButton("Remove");
		btRemoveBeat.addActionListener(new MyRemoveListener());
		buttonBox.add(btRemoveBeat);
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i = 0; i < 16; i++) {
			nameBox.add(new Label(instrumentName[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		theFrame.setBounds(250,250,300,300);
		theFrame.setSize(600,600);
		theFrame.pack();
		theFrame.setVisible(true);
		
		for(int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
			c.addActionListener(new MyCheckboxListener());
		}
		
		setUpMidi();
	}
	
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM((float) (tempo));
		} catch(Exception ex) {ex.printStackTrace();}
	}
	
	public void buildTrackAndStart() {
		int[] trackList = null;
		
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		for(int i = 0; i < 16; i++) {
			trackList = new int[16];
			int key = instruments[i];
			for(int j = 0; j < 16; j++) {
				JCheckBox jc = (JCheckBox) checkboxList.get(j + (16 * i));
				if(jc.isSelected()) {
					trackList[j] = key;
				}
				else {
					trackList[j] = 0;
				}
			}
			makeTracks(trackList);
			track.add(makeEvent(176,1,127,0,16));
		}
		
		track.add(makeEvent(192,9,1,0,15));
		try {
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
			sequencer.setTempoInBPM((float)(tempo));
		} catch(Exception ex) {ex.printStackTrace();}
	}
	
	public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd,chan,one,two);
			event = new MidiEvent(a,tick);
		} catch(Exception e) {}
		return event;
	}
	
	public class StartButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			isBeatStarted = true;
			buildTrackAndStart();
		}
	}
	
	public class StopButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			isBeatStarted = false;
			sequencer.stop();
		}
	}
	
	public class MyCheckboxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(isBeatStarted) {
				buildTrackAndStart();
			}
		}
	}
	
	public class TempoUpButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
			tempo = tempo * 1.03;
			tempoLabel.setText("Tempo: " + (int) (tempo) + " BPM");
		}
	}
	
	public class TempoDownButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * .97));
			tempo = tempo * .97;
			tempoLabel.setText("Tempo: " + (int) (tempo) + " BPM");
		}
	}
	
	public class MySendListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			try {
				JFileChooser fileSave = new JFileChooser(System.getProperty("user.dir"));
				fileSave.showSaveDialog(theFrame);
				if(JFileChooser.APPROVE_OPTION == 0) {
					saveFile(fileSave.getSelectedFile());	
				}
				else if(JFileChooser.CANCEL_OPTION == 1) {}
			} catch(Exception ex) {
				System.out.println("Cancel option selected. File not saved!");
				ex.printStackTrace();
			}
		}
	}
	
	public void saveFile(File file) {
		boolean[] checkboxState = new boolean[256];
		double temp = tempo;
		
		for(int i = 0; i < 256; i++) {
			JCheckBox check = (JCheckBox) checkboxList.get(i);
			if(check.isSelected()) {
				checkboxState[i] = true;
			}
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fileOut);
			os.writeObject(checkboxState);
			os.writeObject(temp);
			os.close();
		} catch(Exception ex) { ex.printStackTrace(); }
	}
	
	public class MyReadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			try {
				JFileChooser fileOpen = new JFileChooser(System.getProperty("user.dir"));
				fileOpen.showOpenDialog(theFrame);
				loadFile(fileOpen.getSelectedFile());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			sequencer.stop();
			//buildTrackAndStart();
		}
	}
	
	public void loadFile(File file) {
			boolean[] checkboxState = null;
			double temp = 0;
			try {
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream is = new ObjectInputStream(fileIn);
				checkboxState = (boolean[]) is.readObject(); 
				temp = (double) is.readObject();
			} catch(Exception ex) {ex.printStackTrace();}
			
			for(int i = 0; i < 256; i++) {
				JCheckBox check = (JCheckBox) checkboxList.get(i);
				if(checkboxState[i]) {
					check.setSelected(true);
				}
				else {
					check.setSelected(false);
				}
			
			}
			tempo = temp;
			tempoLabel.setText("Tempo: " + (int) (tempo) + " BPM");
	}
	
	public class MyRemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boolean[] checkBoxState = new boolean[256];
			
			for(int i = 0; i < 256; i++) {
				JCheckBox check = (JCheckBox) checkboxList.get(i);
				if(check.isSelected()) {
					check.setSelected(false);
				}
			}
			tempo = 120;
			tempoLabel.setText("Tempo: " + (int) (tempo) + " BPM");
			sequencer.stop();
		}
	}
	
	public void makeTracks(int[] list) {
		for(int i = 0; i < 16; i++) {
			int key = list[i];
			if(key != 0) {
				track.add(makeEvent(144,9,key,100,i));
				track.add(makeEvent(128,9,key,100,i + 1));
			}
		}
	}
}
