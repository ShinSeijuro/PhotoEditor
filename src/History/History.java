/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import javafx.event.EventHandler;
import java.util.ArrayDeque;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author CMQ
 */
public class History<T> {

    public static final int MAXIMUM_ACTION_COUNT = 20;

    private T original;

    public T getOriginal() {
        return original;
    }

    private final ArrayDeque<Item<ITransformable<T>>> undoDeque = new ArrayDeque<>();

    public ArrayDeque<Item<ITransformable<T>>> getUndoDeque() {
        return undoDeque;
    }

    private final ArrayDeque<Item<ITransformable<T>>> redoDeque = new ArrayDeque<>();

    public ArrayDeque<Item<ITransformable<T>>> getRedoDeque() {
        return redoDeque;
    }

    public T getCurrentResult() {
        if (undoDeque.size() > 0) {
            return undoDeque.getFirst().result;
        } else if (redoDeque.size() > 0) {
            return original;
        }

        return null;
    }

    public ITransformable<T> getCurrentAction() {
        return undoDeque.peekFirst().object;
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

    public History(T original) {
        this.original = original;
    }

    public void add(ITransformable<T> action, T result) {
        if (redoDeque.size() > 0) {
            redoDeque.clear();
        }

        Item<ITransformable<T>> item = new Item<>(action, result);
        undoDeque.push(item);

        if (onRedone != null) {
            onRedone.handle(new Event(ON_REDONE_EVENT));
        }

        if (getActionCount() > MAXIMUM_ACTION_COUNT) {
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

    public class Item<F extends ITransformable<T>> {

        private F object;
        private T result;

        public F getObject() {
            return object;
        }

        public T getResult() {
            return result;
        }

        public Item(F object, T result) {
            this.object = object;
            this.result = result;
        }
    }
}
