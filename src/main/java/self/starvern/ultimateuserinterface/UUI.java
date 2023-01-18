package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import self.starvern.ultimateuserinterface.events.GuiListener;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.io.File;
import java.util.List;

public final class UUI extends JavaPlugin
{
    private static UUI singleton;

    @Override
    public void onEnable()
    {
        singleton = this;

        File folder = new File(UUI.getSingleton().getDataFolder(), "gui");
        boolean created = folder.mkdirs();
        if (created) Bukkit.getLogger().info("Created gui folder");

        saveResource("gui/example_menu.yml", false);

        GuiManager.loadGuis();

        new InterfaceCommand();
        new GuiListener();

        GuiPage page = GuiManager.getGui("example_menu").getPage(0);
        page.splitInstances("#");

        List<GuiItem> instances = page.getAllInstances("#");

        int i = 0;
        for (GuiItem item : instances)
        {
            i++;
            item.getItem().addDisplayName("&aExample, " + i);
            item.executes(event -> {
                event.getWhoClicked().sendMessage("&7Clicked on item ");
            });
        }

        instances.get(2).executes(event -> {
            event.getWhoClicked().openInventory(page.getGui().getPage(1).getInventory());
        }).getItem().setMaterial(Material.ARROW).addDisplayName("&aNext Page");
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
}
