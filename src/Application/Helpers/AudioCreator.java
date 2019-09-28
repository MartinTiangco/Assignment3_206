package Application.Helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Application.Controllers.Add_Audio_ScreenController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class AudioCreator extends Task<Long> {
	private final String DIR = ".Audio_Directory" + System.getProperty("file.separator");
	private List<String> _content;
	private Add_Audio_ScreenController _controller;
	private Audio _audio;
	
	public AudioCreator(int audioIndex, Audio audio, Add_Audio_ScreenController controller) {
		audio.setFilename("temp" + String.valueOf(audioIndex) + ".wav");
		_audio = audio;
		_content = audio.getContent();
		_controller = controller;
	}
	
	@Override
	protected Long call() throws Exception {

		try {
			Path file = Paths.get(DIR + "temp.txt");
			Files.write(file, _content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String cmd = "";
//		if (_audio.getVoice() == "kal_diphone" || _audio.getVoice() == "akl_nz_jdt_diphone" ) {
//            cmd = "festival -b .Audio_Directory/speech.scm";
//        } else {
//        	cmd = "text2wave -o " + DIR + _audio.getFilename() + " " + DIR + "temp.txt";
//        }
		String texts = "";
        for (int i = 0; i < _audio.getContent().size(); i++) {
            texts = texts + _audio.getContent().get(i);
        }
		
		String cmd = "espeak -w " + DIR + _audio.getFilename() + " \"" + texts + "\"";
		System.out.println(cmd);
		// ProcessBuilder to generate audio 
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
			} else {
				AlertMessage alert = new AlertMessage("create_audio_failed");
				Platform.runLater(alert);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//Append onto ListView
				_controller.getAudioList().getItems().add(_audio);
				_controller.enableBottomHalf();
			}
		});
		
		return null;
	}
	
//	public void CreateScmFile(){
//        List<String> instruction = new ArrayList<>();
//        instruction.add("(voice_" +_audio.getVoice() + ")");
//        System.out.println(_audio.getVoice());
//        instruction.add("(Parameter.set 'Duration_Stretch " + String.format("%1f", _audio.getSpeed()) + ")");
//        instruction.add("(set! duffint_params '((start " + String.valueOf(_audio.getPitch()) + ") (end " + String.valueOf(_audio.getPitch()) + ")))");
//        instruction.add("(Parameter.set 'Int_Method 'DuffInt)");
//        instruction.add("(Parameter.set 'Int_Target_Method Int_Targets_Default)");
//        instruction.add("(utt.save.wave (SayText " + _audio.getContent().toString() + "\") " + _audio.getFilename() + " 'riff");
//        try {
//            Path file = Paths.get(".Audio_Directory/speech.scm");
//            Files.write(file, instruction);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
