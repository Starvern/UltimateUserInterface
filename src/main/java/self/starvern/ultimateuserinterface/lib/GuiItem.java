package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.managers.ItemManager;

import java.util.UUID;
import java.util.function.Consumer;

public class GuiItem
{
    private final Gui gui;
    private final String id;
    private final ItemStack item;
    private final NamespacedKey key;
    private final UUID uuid = UUID.randomUUID();

    private Consumer<InventoryClickEvent> event;

    public GuiItem(Gui gui, String id)
    {
        this.gui = gui;
        this.id = id;
        this.item = ItemManager.buildItem(gui.getConfig(), this.id);
        this.key = new NamespacedKey(UUI.getSingleton(), "uui-item-id");
    }

    /**
     * @return The GUI this item appears in.
     */
    public Gui getGui()
    {
        return this.gui;
    }

    /**
     * @return The character associated with the item.
     */
    public String getId()
    {
        return this.id;
    }

    public ItemStack getItem()
    {
        this.addKey();
        return this.item;
    }

    private void addKey()
    {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, this.uuid.toString());
        this.item.setItemMeta(itemMeta);
    }

    /**
     * Attaches an event to the item
     * @param event The event to run
     * @return The instance of the item
     */
    public GuiItem executes(Consumer<InventoryClickEvent> event)
    {
        Bukkit.getLogger().info("Attached Event");
        this.event = event;
        return this;
    }

    /**
     * Executes the event
     * @param event The event to run
     */
    public void runEvent(@NotNull InventoryClickEvent event)
    {
        if (this.event == null) return;

        this.event.accept(event);
    }
}

