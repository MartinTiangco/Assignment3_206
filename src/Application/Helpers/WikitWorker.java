package Application.Helpers;

import Application.Controllers.Add_Audio_ScreenController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Worker thread calling the wikit command in the background.
 */
public class WikitWorker extends Task<Long> {

	private File wikitRaw;
	private File wikitTemp;
	private List<String> sepLines;
	private String cmd;
	private String searchTerm;
	private Writer rawFileWriter;
	
	private Add_Audio_ScreenController controller;
	
	public WikitWorker(String cmd, String searchTerm, Writer rawFileWriter, File wikitRaw, File wikitTemp, Add_Audio_ScreenController controller) {
		this.cmd = cmd;
		this.searchTerm = searchTerm;
		this.rawFileWriter = rawFileWriter;
		this.wikitRaw = wikitRaw;
		this.wikitTemp = wikitTemp;
		this.controller = controller;
	}
	
	/**
	 * Calls the wikit command, writes into a txt file and separates into sentences.
	 */ 
	@Override
	public Long call() {
		
		try {
			// ProcessBuilder to execute wikit 
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line = stdoutBuffered.readLine();
				
				// Writes line into raw.txt
				writeIntoFile(line, rawFileWriter);
				rawFileWriter.close();
				
				// Command to separate into sentences
				sepLines = new ArrayList<>();
				sepIntoSentences(sepLines);	
			}
			
			Append append = new Append();
			Platform.runLater(append);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Sets the progress indicator to be invisible
		ProgressRunnable progress = new ProgressRunnable(controller);
		Platform.runLater(progress);
		return null;	
	}
	
//	class ProgressRunnable implements Runnable {
//		@Override
//		public void run() {
//			controller.getEntireScreenPane().setDisable(false);
//			controller.getProgressIndicator().setVisible(false);
//		}
//		
//	}
	
	class Append implements Runnable {
		@Override
		public void run() {
			// clears ListView and appends all lines of content
			if (!(sepLines == null)) {
				
				//append lines onto text field
				StringBuilder content = new StringBuilder(""); 
				for (String line : sepLines) {
				   content.append(line);
				}
				controller.getContent().getItems().addAll(sepLines);
				controller.enableCustomization();
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
			String sepCmd = "cat " + wikitRaw + " | sed 's/\\([.!?]\\) \\([[:upper:]]\\)/\\1\\\n\\2/g' | sed \"s/^[  ]*/ /\"";
			
			ProcessBuilder sepBuilder = new ProcessBuilder("/bin/bash", "-c", sepCmd);
			Process sepProcess = sepBuilder.start();
			InputStream sepStdout = sepProcess.getInputStream();
			BufferedReader sepStdoutBuffered = new BufferedReader(new InputStreamReader(sepStdout));
			int sepExitStatus = sepProcess.waitFor();
			
			if (sepExitStatus == 0) {
				String sepLine = sepStdoutBuffered.readLine();
				
				//create new temp.txt
				Writer tempFileWriter = new FileWriter(wikitTemp, false);
				
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

