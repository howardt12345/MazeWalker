package MazeWalker;
import java.io.*;
import sun.audio.*;

public class SoundManager {
	
	public static void Play (String file){ //Play
		try {
			InputStream in = new FileInputStream(file);
			AudioStream stream = new AudioStream(in);
			AudioPlayer.player.start(stream); //Plays file
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
