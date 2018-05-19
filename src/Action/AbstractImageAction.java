/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public abstract class AbstractImageAction implements INameable {

    private String name;

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final void setName(String newName) {
        this.name = newName;
    }

    private Image originalImage;

    public Image getOriginalImage() {
        return originalImage;
    }

    private Image modifiedImage;

    public Image getModifiedImage() {
        return modifiedImage;
    }

    private Task<Image> applyTransformTask;

    public Task<Image> getApplyTransformTask() {
        if (applyTransformTask == null) {
            applyTransformTask = new Task<Image>() {
                @Override
                protected Image call() throws Exception {
                    updateMessage("Applying " + getName() + "...");
                    return applyTransform();
                }
            };
        }

        return applyTransformTask;
    }

    public AbstractImageAction(Image originalImage) {
        this.originalImage = originalImage;
        this.name = "";
    }

    public final Image applyTransform() {
        if (modifiedImage == null) {
            modifiedImage = applyTransform(originalImage);
        }

        return modifiedImage;
    }

    protected abstract Image applyTransform(Image image);

}
