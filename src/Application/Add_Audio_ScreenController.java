package Application;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Add_Audio_ScreenController extends Controller {
	@FXML private Button _mainMenuButton;
	@FXML private Button _searchButton;
	@FXML private TextField _searchTextField;
	@FXML private TextArea _contentTextArea;
	//@FXML private ListView _content;
	
	//directory for wiki text files
	private File wikitDir = new File(".Wikit_Directory");
	private File wikitRaw = new File(wikitDir + System.getProperty("file.separator") + "raw.txt"); //raw content - where content is not separated to lines
	private File wikitTemp = new File(wikitDir + System.getProperty("file.separator") + "temp.txt"); //temp content - where content is separated
	
	public void handleBackToMainMenu() {
		Stage stage = (Stage) _mainMenuButton.getScene().getWindow();
        stage.close();
	}
	
	public void handleSearch() {
		System.out.println("Searching");
		String searchInput = _searchTextField.getText();
		
		if (!validateSearch(searchInput)) {
			System.out.println("Search term is invalid");
			return;
		}

		try {
			//First clears the TextArea and hides the line count
			//lineCount.setVisible(false);
			_contentTextArea.clear();
			
			wikitSearch(searchInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void wikitSearch(String searchInput) {
		try {
			//create raw.txt for raw wikit content (has not been separated)
			Writer rawFileWriter = new FileWriter(wikitRaw, false);
			
			String cmd = "wikit " + searchInput;
			
			// Runs the wikit command on a worker thread
			WikitWorker wikitWorker = new WikitWorker(cmd, searchInput, rawFileWriter, wikitRaw, wikitTemp, this);
			wikitWorker.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean validateSearch(String searchInput) {
		// checks for textfield being an empty string or only spaces
		if (searchInput.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public TextArea getContentTextArea() {
		return _contentTextArea;
	}
}
