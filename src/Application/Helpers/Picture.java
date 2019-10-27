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

    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty on = new SimpleBooleanProperty();
    
    private String fileName;
    private Boolean selected = false;
    private ImageView imageView;

    public Picture(String url, String name, boolean on, String fileName) {
        super(url);
        this.fileName = fileName;
        setName(name);
        setOn(on);
        imageView = new ImageView(this);
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
    }

    private StringProperty nameProperty() {
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
