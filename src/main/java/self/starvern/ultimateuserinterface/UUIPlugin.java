package self.starvern.ultimateuserinterface;

import org.bukkit.plugin.java.JavaPlugin;
import self.starvern.ultimateuserinterface.commands.InterfaceCommand;
import self.starvern.ultimateuserinterface.events.GuiListener;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.commands.GuiCommandExecutor;
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

        if (HeadDatabaseHook.isInstalled())
            HeadDatabaseHook.registerListener(this.api);

        if (this.api.getCommandManager().getCommandMap() != null)
            this.api.getCommandManager().getCommandMap()
                    .register("uui", new GuiCommandExecutor(this.api, "uui"));
    }

    @Override
    public void onDisable()
    {
        api = null;
    }

    /**
     * @return The instance of UUI API.
     * @since 0.5.0
     */
    public UUI getApi()
    {
        return api;
    }

    public void loadMacros()
    {
        // Interactions
        new CloseMacro(this.api, this).register();
        new CommandMacro(this.api, this).register();
        new MessageMacro(this.api, this).register();
        new PlayerCommandMacro(this.api, this).register();
        new StaticMacro(this.api, this).register();
        new CustomEventMacro(this.api, this).register();
        new UpdateMacro(this.api, this).register();

        // Page Navigation
        new SetPageMacro(this.api, this).register();
        new NextPageMacro(this.api, this).register();
        new LastPageMacro(this.api, this).register();

        // Item Management
        new SetItemMacro(this.api, this).register();
        new SetPropertyMacro(this.api, this).register();
        new SetTitleMacro(this.api, this).register();

        // Conditions
        new PermissionCheckMacro(this.api, this).register();
        new PlaceholderCompareNumberMacro(this.api, this).register();
        new PlaceholderCompareStringMacro(this.api, this).register();

        // Special
        new PlayerListMacro(this.api, this).register();
    }

    /**
     * Creates any required files and loads all Guis
     * @since 0.5.0
     */
    public void load()
    {
        File folder = new File(getDataFolder(), "gui");
        boolean created = folder.mkdirs();
        if (created)
        {
            this.getLogger().info("Created gui folder. [UltimateUserInterface/gui]");
            this.saveResource("gui/example_menu.yml", false);
        }

        this.api.getGuiManager().loadGuis();
    }
}
