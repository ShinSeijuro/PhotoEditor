/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import Action.AbstractImageAction;
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import java.util.ArrayDeque;
import javafx.event.Event;
import javafx.event.EventType;

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

    public Image getCurrentImage() {
        if (undoDeque.size() > 0) {
            return undoDeque.getFirst().getModifiedImage();
        } else if (redoDeque.size() > 0) {
            return redoDeque.getFirst().getOriginalImage();
        }

        return null;
    }

    public AbstractImageAction getCurrentAction() {
        return undoDeque.peekFirst();
    }

    public int getActionCount() {
        return undoDeque.size() + redoDeque.size();
    }

    public static final EventType<Event> ON_UNDONE_EVENT
            = new EventType<Event>(Event.ANY, "ON_UNDONE_EVENT");

    private EventHandler<Event> onUndone;

    public EventHandler<Event> getOnUndone() {
        return onUndone;
    }

    public void setOnUndone(EventHandler<Event> onUndone) {
        this.onUndone = onUndone;
    }

    public static final EventType<Event> ON_REDONE_EVENT
            = new EventType<Event>(Event.ANY, "ON_REDONE_EVENT");

    private EventHandler<Event> onRedone;

    public EventHandler<Event> getOnRedone() {
        return onRedone;
    }

    public void setOnRedone(EventHandler<Event> onRedone) {
        this.onRedone = onRedone;
    }

    public History() {
    }

    public void add(AbstractImageAction action) {
        if (redoDeque.size() > 0) {
            redoDeque.clear();
        }

        undoDeque.push(action);

        if (onRedone != null) {
            onRedone.handle(new Event(ON_REDONE_EVENT));
        }

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

            if (onRedone != null) {
                onRedone.handle(new Event(ON_REDONE_EVENT));
            }
        }
    }

    public void undo() {
        if (isUndoable()) {
            redoDeque.push(undoDeque.pop());

            if (onUndone != null) {
                onUndone.handle(new Event(ON_UNDONE_EVENT));
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
