package self.starvern.ultimateuserinterface;

import org.bukkit.plugin.java.JavaPlugin;
import self.starvern.ultimateuserinterface.commands.InterfaceCommand;
import self.starvern.ultimateuserinterface.events.GuiListener;

import java.io.File;

public class UUIPlugin extends JavaPlugin
{
    private static UUIPlugin singleton;
    private static UUI api;

    @Override
    public void onEnable()
    {
        singleton = this;
        api = new UUI(this);

        load();

        new InterfaceCommand(this);
        new GuiListener(this);
    }

    @Override
    public void onDisable()
    {
        singleton = null;
        api = null;
    }

    public static UUIPlugin getSingleton()
    {
        return singleton;
    }

    public UUI getApi()
    {
        return api;
    }

    public void load()
    {
        File folder = new File(getDataFolder(), "gui");
        boolean created = folder.mkdirs();
        if (created)
            getLogger().info("Created gui folder");

        saveResource("gui/example_menu.yml", false);

        api.getGuiManager().loadGuis();
    }
}
