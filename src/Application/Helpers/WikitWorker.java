package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Worker thread calling the wikit command in the background.
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class WikitWorker extends Task<Long> {

	// wikit directory and the wikit txt files
	private File _wikitDir = new File(".Wikit_Directory");
	//raw content - where content is not separated to lines
	private File _wikitRaw = new File(_wikitDir + System.getProperty("file.separator") + "raw.txt");
	//temp content - where content is separated
	private File _wikitTemp = new File(_wikitDir + System.getProperty("file.separator") + "temp.txt");

	private Add_Audio_ScreenController _controller;
	private List<String> _sepLines; // each entry in the List is a sentence
	private String _cmd;
	private Writer _rawFileWriter;
	
	
	public WikitWorker(String cmd, Add_Audio_ScreenController controller) {
		_cmd = cmd;
		_controller = controller;
		
		//create raw.txt for raw wikit content (content has not been separated)
		try {
			_rawFileWriter = new FileWriter(_wikitRaw, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calls the wikit command, writes into a txt file and separates into sentences.
	 */ 
	@Override
	public Long call() {
		
		try {
			// ProcessBuilder to execute wikit 
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", _cmd);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line = stdoutBuffered.readLine();
				
				if (line.contains("not found :^(")) {
					// term is not found on wikit search
					AlertMessage alert = new AlertMessage("not_found");
					Platform.runLater(alert);
				} else {
					// Writes line into raw.txt
					writeIntoFile(line, _rawFileWriter);
					_rawFileWriter.close();
					
					// Command to separate into sentences
					_sepLines = new ArrayList<>();
					sepIntoSentences(_sepLines);		
				}
			}
			
			Append append = new Append();
			Platform.runLater(append);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Sets the progress indicator to be invisible
		ProgressRunnable progress = new ProgressRunnable(_controller);
		Platform.runLater(progress);
		return null;	
	}

	/**
	 * Inner class that appends the ListView of the content from using wikit
	 */
	class Append implements Runnable {
		@Override
		public void run() {
			// clears ListView and appends all lines of content
			if (!(_sepLines == null)) {
				
				//append lines onto text field
				StringBuilder content = new StringBuilder(""); 
				for (String line : _sepLines) {
				   content.append(line);
				}
				_controller.getContent().getItems().addAll(_sepLines);
				
				// enables the customization panel (i.e. the voice settings)
				_controller.enableCustomization();
			}		
		}		
	}
	
	/**
	 * Writes a line into a file
	 */
	private void writeIntoFile(String line, Writer fileWriter) {
		try {
			fileWriter.write(line + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Separates wikit content into sentences.
	 */
	private void sepIntoSentences(List<String> sepLines) {
		try {
			// command for separating into sentences
			String sepCmd = "cat " + _wikitRaw + " | sed 's/\\([.!?]\\) \\([[:upper:]]\\)/\\1\\\n\\2/g' | sed \"s/^[  ]*/ /\"";
			
			// start the process
			ProcessBuilder sepBuilder = new ProcessBuilder("/bin/bash", "-c", sepCmd);
			Process sepProcess = sepBuilder.start();
			InputStream sepStdout = sepProcess.getInputStream();
			BufferedReader sepStdoutBuffered = new BufferedReader(new InputStreamReader(sepStdout));
			int sepExitStatus = sepProcess.waitFor();
			
			if (sepExitStatus == 0) {
				String sepLine = sepStdoutBuffered.readLine();
				
				//create new temp.txt
				Writer tempFileWriter = new FileWriter(_wikitTemp, false);
				
				// Appends text onto temp.txt
				while (sepLine != null) {
					writeIntoFile(sepLine, tempFileWriter);
					sepLines.add(sepLine);
					
					//read next line
					sepLine = sepStdoutBuffered.readLine();
				}
				tempFileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

