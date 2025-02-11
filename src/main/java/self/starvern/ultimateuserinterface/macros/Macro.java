package self.starvern.ultimateuserinterface.macros;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;

public abstract class Macro
{
    protected final UUI api;
    protected final Plugin plugin;
    protected final String id;

    public Macro(UUI api, Plugin plugin, String id)
    {
        this.api = api;
        this.plugin = plugin;
        this.id = id;
    }

    public String getIdentification()
    {
        return this.id;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    /**
     * @param event The event to run.
     * @param action The action to run the event for.
     * @since 0.4.0
     */
    public abstract void run(GuiEvent event, GuiAction<? extends GuiBased> action);

    @Override
    public String toString()
    {
        if (this.plugin instanceof UUIPlugin)
            return "[" + this.id + "]";
        return "[" + this.plugin.getName() + "::" + this.id + "]";
    }

    public void register()
    {
        this.api.getMacroManager().register(this);
    }
}
