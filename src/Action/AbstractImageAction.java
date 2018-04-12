/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import History.IUndoable;
import History.History;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author CMQ
 */
public abstract class AbstractImageAction implements IUndoable {

    public BufferedImage originalImage;

    private BufferedImage modifiedImage;

    public BufferedImage getModifiedImage() {
        return modifiedImage;
    }

    public AbstractImageAction(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    @Override
    public final void Undo() {
        originalImage = modifiedImage;
    }

    public final BufferedImage applyTransform() {
        History.add(this);
        if (modifiedImage == null) {
            modifiedImage = applyTransform(originalImage);
        }

        return modifiedImage;
    }

    protected abstract BufferedImage applyTransform(BufferedImage image);
}
