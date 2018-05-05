/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import java.awt.image.BufferedImage;

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

    private BufferedImage originalImage;

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    private BufferedImage modifiedImage;

    public BufferedImage getModifiedImage() {
        return modifiedImage;
    }

    public AbstractImageAction(BufferedImage originalImage) {
        this.originalImage = originalImage;
        this.name = "";
    }

    public final BufferedImage applyTransform() {
        if (modifiedImage == null) {
            modifiedImage = applyTransform(originalImage);
        }

        return modifiedImage;
    }

    protected abstract BufferedImage applyTransform(BufferedImage image);
}
