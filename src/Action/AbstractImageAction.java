/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import History.ITransformable;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public abstract class AbstractImageAction implements INameable, ITransformable<Image> {

    private String name = "";
    private Task<Image> applyTransformTask;

    public AbstractImageAction() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public final void setName(String newName) {
        this.name = newName;
    }

    public Task<Image> getApplyTransformTask(Image image) {
        if (applyTransformTask == null) {
            applyTransformTask = new Task<Image>() {
                @Override
                protected Image call() throws Exception {
                    updateMessage("Applying " + getName() + "...");
                    return applyTransform(image);
                }
            };
        }

        return applyTransformTask;
    }
}
