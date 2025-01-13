package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import self.starvern.ultimateuserinterface.commands.InterfaceCommand;
import self.starvern.ultimateuserinterface.events.GuiListener;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.macros.impl.*;

import java.io.File;

public class UUIPlugin extends JavaPlugin
{
    private UUI api;

    @Override
    public void onEnable()
    {
        this.api = new UUI(this);

        loadMacros();
        load();

        new InterfaceCommand(this);
        new GuiListener(this.api);
        if (Bukkit.getPluginManager().getPlugin("HeadDatabase") != null)
            new HeadDatabaseHook(this.api);
    }

    @Override
    public void onDisable()
    {
        api = null;
    }

    public UUI getApi()
    {
        return api;
    }

    public void loadMacros()
    {
        new CloseMacro(this.api, this).register();
        new CommandMacro(this.api, this).register();
        new MessageMacro(this.api, this).register();
        new PlayerCommandMacro(this.api, this).register();
        new PapiMacro(this.api, this).register();
        new NextPageMacro(this.api, this).register();
        new LastPageMacro(this.api, this).register();
        new SetItemMacro(this.api, this).register();
        new StaticMacro(this.api, this).register();
        new ViewPermissionMacro(this.api, this).register();

        // special macros
        new PlayerListMacro(this.api, this).register();
        new CraftMacro(this.api, this).register();
    }

    public void load()
    {
        File folder = new File(getDataFolder(), "gui");
        boolean created = folder.mkdirs();
        if (created)
            getLogger().info("Created gui folder");

        File file = new File(getDataFolder(), "gui/example_menu.yml");

        if (!file.exists())
            saveResource("gui/example_menu.yml", false);

        api.getGuiManager().loadGuis();
    }
}
