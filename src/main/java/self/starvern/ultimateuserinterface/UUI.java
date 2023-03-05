package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import self.starvern.ultimateuserinterface.managers.GuiManager;

public class UUI
{
    private final UUIPlugin plugin;
    private final GuiManager guiManager;

    protected UUI(UUIPlugin plugin)
    {
        this.plugin = plugin;
        this.guiManager = new GuiManager(this);

        Bukkit.getServicesManager().register(UUI.class, this, this.plugin, ServicePriority.Normal);
    }

    public GuiManager getGuiManager()
    {
        return guiManager;
    }

    public UUIPlugin getPlugin()
    {
        return plugin;
    }
}
