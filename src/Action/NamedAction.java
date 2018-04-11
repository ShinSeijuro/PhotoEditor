package Action;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * An action that can be simply renamed
 */
public abstract class NamedAction extends AbstractAction implements INameable {

    protected NamedAction() {
    }

    protected NamedAction(String name) {
        super(name);
    }

    protected NamedAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setName(String newName) {
        this.putValue(AbstractAction.NAME, newName);
    }

    public String getName() {
        return (String) getValue(AbstractAction.NAME);
    }
}
