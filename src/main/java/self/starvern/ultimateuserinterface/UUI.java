package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import self.starvern.ultimateuserinterface.managers.FieldManager;
import self.starvern.ultimateuserinterface.managers.GuiManager;
import self.starvern.ultimateuserinterface.managers.ItemInputManager;
import self.starvern.ultimateuserinterface.managers.MacroManager;

import java.util.logging.Logger;

public class UUI
{
    private final UUIPlugin plugin;

    private final GuiManager guiManager;
    private final ItemInputManager itemInputManager;
    private final MacroManager macroManager;
    private final Logger logger;
    private final FieldManager fieldManager;

    protected UUI(UUIPlugin plugin)
    {
        this.plugin = plugin;
        this.guiManager = new GuiManager(this);
        this.itemInputManager = new ItemInputManager();
        this.macroManager = new MacroManager();
        this.logger = Logger.getLogger("UUI");
        this.fieldManager = new FieldManager();

        Bukkit.getServicesManager().register(UUI.class, this, this.plugin, ServicePriority.Normal);
    }

    public FieldManager getFieldManager()
    {
        return this.fieldManager;
    }

    public GuiManager getGuiManager()
    {
        return guiManager;
    }

    public UUIPlugin getPlugin()
    {
        return plugin;
    }

    public ItemInputManager getItemInputManager()
    {
        return itemInputManager;
    }

    public MacroManager getMacroManager()
    {
        return macroManager;
    }

    public NamespacedKey getKey()
    {
        return new NamespacedKey(this.plugin, "uui-uuid");
    }

    public Logger getLogger()
    {
        return this.logger;
    }
}
