package self.starvern.ultimateuserinterface.lib;

import self.starvern.ultimateuserinterface.macros.GuiAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Implemented by classes that want to have actions (list of macros with action type)
 * @since 0.4.2
 */
public abstract class Actionable<T extends GuiBased>
{
    protected final List<GuiAction<T>> actions;

    protected Actionable()
    {
        this.actions = new ArrayList<>();
    }

    /**
     * Optional method to preload actions.
     * @since 0.4.2
     */
    public abstract void loadActions();

    /**
     * Removes all actions.
     * @since 0.4.2
     */
    public void clearActions()
    {
        this.actions.clear();
    }

    /**
     * @return All the actions on this item.
     * @since 0.4.2
     */
    public List<GuiAction<T>> getActions()
    {
        return this.actions;
    }

    /**
     * @param actions The new actions of the GuiItem.
     * @since 0.4.2
     */
    public void setActions(List<GuiAction<T>> actions)
    {
        this.clearActions();

        this.actions.addAll(actions);
    }

    /**
     * @param action The action to remove from the GuiItem.
     * @since 0.4.2
     */
    public void removeAction(GuiAction<T> action)
    {
        this.actions.remove(action);
    }

    /**
     * @param action The action to add to the GuiItem.
     * @since 0.4.2
     */
    public void addAction(GuiAction<T> action)
    {
        this.actions.add(action);
    }
}
