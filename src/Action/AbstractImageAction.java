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
public abstract class AbstractImageAction extends NamedAction implements IUndoable {

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
        originalImage = getModifiedImage();
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        History.add(this);
        modifiedImage = applyTransform(originalImage);
    }

    public abstract BufferedImage applyTransform(BufferedImage image);
}
