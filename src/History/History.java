/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

/**
 *
 * @author CMQ
 */
public class History {

    public static final int maximumAction = 20;

    private static final ArrayDeque<AbstractImageAction> undoDeque = new ArrayDeque<>();

    public static ArrayDeque<AbstractImageAction> getUndoDeque() {
        return undoDeque;
    }

    private static final ArrayDeque<AbstractImageAction> redoDeque = new ArrayDeque<>();

    public static ArrayDeque<AbstractImageAction> getRedoDeque() {
        return redoDeque;
    }

    public static BufferedImage getCurrentImage() {
        if (undoDeque.size() > 0) {
            return undoDeque.getFirst().getModifiedImage();
        } else if (redoDeque.size() > 0) {
            return redoDeque.getFirst().getOriginalImage();
        }

        return null;
    }

    public static int getActionCount() {
        return undoDeque.size() + redoDeque.size();
    }

    public History() {
    }

    public static void add(AbstractImageAction action) {
        if (redoDeque.size() > 0) {
            redoDeque.clear();
        }

        undoDeque.push(action);

        if (getActionCount() > maximumAction) {
            undoDeque.removeLast();
        }
    }

    public static boolean isRedoable() {
        return redoDeque.size() > 0;
    }

    public static boolean isUndoable() {
        return undoDeque.size() > 0;
    }

    public static void redo() {
        if (isRedoable()) {
            undoDeque.push(redoDeque.pop());
        }
    }

    public static void undo() {
        if (isUndoable()) {
            redoDeque.push(undoDeque.pop());
        }
    }

    public static void clear() {
        undoDeque.clear();
        redoDeque.clear();
    }
}
