package Application.Helpers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Picture extends Image {
    private static final String IMAGE_DIR = ".Image_Directory" + System.getProperty("file.separator");
    
    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty on = new SimpleBooleanProperty();
    
    private String fileName = "";
    private Boolean selected = false;
    private ImageView imageView;
    private String url;

    public Picture(String url) {
        super(url);
    }
    public Picture(String url, String name, boolean on, String fileName) {
        super(url);
        this.url = url;
        this.fileName = fileName;
        setName(name);
        setOn(on);
        imageView = new ImageView(this);
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
    }

    public final StringProperty nameProperty() {
        return this.name;
    }

    public final String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final String name) {
        this.nameProperty().set(name);
    }

    public final BooleanProperty onProperty() {
        return this.on;
    }

    public final void setOn(final boolean on) {
        this.onProperty().set(on);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return getName();
    }

}
