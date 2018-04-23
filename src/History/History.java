/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author CMQ
 */
public class History {

    public static final int maximumAction = 20;

    private final ArrayDeque<AbstractImageAction> undoDeque = new ArrayDeque<>();

    public ArrayDeque<AbstractImageAction> getUndoDeque() {
        return undoDeque;
    }

    private final ArrayDeque<AbstractImageAction> redoDeque = new ArrayDeque<>();

    public ArrayDeque<AbstractImageAction> getRedoDeque() {
        return redoDeque;
    }

    public BufferedImage getCurrentImage() {
        if (undoDeque.size() > 0) {
            return undoDeque.getFirst().getModifiedImage();
        } else if (redoDeque.size() > 0) {
            return redoDeque.getFirst().getOriginalImage();
        }

        return null;
    }

    public int getActionCount() {
        return undoDeque.size() + redoDeque.size();
    }

    private ChangeListener<Boolean> isModifiedChangeListener;

    public ChangeListener<Boolean> getIsModifiedChangeListener() {
        return isModifiedChangeListener;
    }

    public void setIsModifiedChangeListener(ChangeListener<Boolean> listener) {
        isModifiedChangeListener = listener;
    }

    public History() {
    }

    public void add(AbstractImageAction action) {
        if (isModifiedChangeListener != null) {
            isModifiedChangeListener.changed(null, isModified(), true);
        }

        if (redoDeque.size() > 0) {
            redoDeque.clear();
        }

        undoDeque.push(action);

        if (getActionCount() > maximumAction) {
            undoDeque.removeLast();
        }
    }

    public boolean isRedoable() {
        return redoDeque.size() > 0;
    }

    public boolean isUndoable() {
        return undoDeque.size() > 0;
    }

    public void redo() {
        if (isRedoable()) {
            undoDeque.push(redoDeque.pop());
        }
    }

    public void undo() {
        if (isUndoable()) {
            redoDeque.push(undoDeque.pop());

            if (isModifiedChangeListener != null) {
                isModifiedChangeListener.changed(null, true, isModified());
            }
        }
    }

    public void clear() {
        undoDeque.clear();
        redoDeque.clear();
    }

    public boolean isModified() {
        return undoDeque.size() > 0;
    }
}
