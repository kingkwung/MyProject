package ChatWithGame;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySound implements Runnable{
	private String audioFile;
	private int flag;

	/* "실행방법"
		new PlaySound("test.wav");	
	 */
	public PlaySound(String file){
		audioFile = file;
		flag = 0;
	}
	public void startMusic(){
		(new Thread(this)).start();
		
	}
	public void run(){ 		//Play Audio File.
		int ThreadFlag = ++flag;
		Clip clip=null;
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(audioFile)));

			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();			
			while(clip.getMicrosecondLength() != (clip.getMicrosecondPosition()-49000)){
				if(ThreadFlag!=flag) return;
			}

			clip.stop();
			return;
		}catch(Exception e){e.printStackTrace();
		}finally{
			if(clip != null)
				try{clip.close();}catch(Exception e){}
		}
	}
}