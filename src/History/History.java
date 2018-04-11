/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import java.util.ArrayDeque;

/**
 *
 * @author CMQ
 */
public class History {

    public static final int maximumAction = 20;

    private static final ArrayDeque<IUndoable> undoDeque = new ArrayDeque<>();

    public static ArrayDeque<IUndoable> getUndoDeque() {
        return undoDeque;
    }

    private static final ArrayDeque<IUndoable> redoDeque = new ArrayDeque<>();

    public static ArrayDeque<IUndoable> getRedoDeque() {
        return redoDeque;
    }

    public static int getActionCount() {
        return undoDeque.size() + redoDeque.size();
    }

    public History() {
    }

    public static void add(IUndoable action) {
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
            redoDeque.getLast().Undo();
            undoDeque.addLast(redoDeque.getLast());
        }
    }

    public static void undo() {
        if (isUndoable()) {
            undoDeque.getFirst().Undo();
            redoDeque.push(undoDeque.pop());
        }
    }

    public static void clear() {
        undoDeque.clear();
        redoDeque.clear();
    }
}
