package Application.Helpers;

import java.io.File;

/**
 * Class to clean up and remove temp files in directories
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Cleaner {

	// the directories
    private final File WIKIT_DIR = new File(".Wikit_Directory" + System.getProperty("file.separator"));
    private final File AUDIO_DIR = new File(".Audio_Directory" + System.getProperty("file.separator"));
    private final File OUTPUT_DIR = new File(".Output_Directory" + System.getProperty("file.separator"));
    private final File CREATION_DIR = new File("Creation_Directory" + System.getProperty("file.separator"));
    private final File IMAGE_DIR = new File(".Image_Directory" + System.getProperty("file.separator"));

    public final void cleanAll() {
        cleanWikit();
        cleanAudio();
        cleanImage();
        cleanOutput();
    }
    
    public final void cleanCreation(String folderName) {
    	File folder = new File(CREATION_DIR + System.getProperty("file.separator") + folderName);
    	System.out.println(folder.toString());
        File[] files = folder.listFiles();
        if(files != null) {
            for (File f: files) {
                f.delete();
            }
        }
    }

    public final void cleanWikit() {
        File[] files = WIKIT_DIR.listFiles();
        if(files != null) {
            for (File f: files) {
                    f.delete();
            }
        }
    }

    public final void cleanAudio() {
        File[] files = AUDIO_DIR.listFiles();
        if(files != null) {
            for (File f: files) {
                f.delete();
            }
        }
    }

    public final void cleanOutput() {
        File[] files = OUTPUT_DIR.listFiles();
        if(files != null) {
            for (File f: files) {
                f.delete();
            }
        }
    }

    public final void cleanImage() {
        File[] files = IMAGE_DIR.listFiles();
        if(files != null) {
            for (File f: files) {
                f.delete();
            }
        }
    }
}
