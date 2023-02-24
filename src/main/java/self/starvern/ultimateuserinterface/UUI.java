package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import self.starvern.ultimateuserinterface.commands.InterfaceCommand;
import self.starvern.ultimateuserinterface.events.GuiListener;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.io.File;

public final class UUI extends JavaPlugin
{
    private static UUI singleton;

    @Override
    public void onEnable()
    {
        singleton = this;

        load();

        new InterfaceCommand();
        new GuiListener();
    }

    @Override
    public void onDisable()
    {
        singleton = null;
    }

    public static UUI getSingleton()
    {
        return singleton;
    }

    public void load()
    {
        File folder = new File(UUI.getSingleton().getDataFolder(), "gui");
        boolean created = folder.mkdirs();
        if (created) Bukkit.getLogger().info("Created gui folder");

        saveResource("gui/example_menu.yml", false);

        GuiManager.loadGuis();
    }
}
