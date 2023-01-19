package self.starvern.ultimateuserinterface.lib;

import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.managers.ItemManager;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.UUID;
import java.util.function.Consumer;

public class GuiItem
{
    private final Gui gui;
    private final String id;
    private final ItemUtility item;
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
     * @return The UUID of the item.
     * @since 0.1.0
     */
    public UUID getUniqueId()
    {
        return this.uuid;
    }

    /**
     * @return The GUI this item appears in.
     * @since 0.1.0
     */
    public Gui getGui()
    {
        return this.gui;
    }

    /**
     * Creates a duplicate from the GUI file
     * @return An un-altered instance of the item
     * @since 0.1.0
     */
    public GuiItem duplicate()
    {
        return new GuiItem(this.gui, this.id);
    }

    /**
     * @return The character associated with the item.
     * @since 0.1.0
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * <p>
     *     Returns an instance of ItemUtility, which manages how the item
     *     appears. Use this method as an entry-point for item customization.
     * </p>
     * @return The item's ItemUtility
     * @since 0.1.0
     */
    public ItemUtility getItem()
    {
        return this.item.addKey(this.key, this.uuid.toString());
    }

    /**
     * Attaches an event to the item
     * @param event The event to run
     * @return The instance of the item
     * @since 0.1.0
     */
    public GuiItem executes(Consumer<InventoryClickEvent> event)
    {
        this.event = event;
        return this;
    }

    /**
     * Executes the event
     * @param event The event to run
     * @since 0.1.0
     */
    public void runEvent(@NotNull InventoryClickEvent event)
    {
        if (this.event == null) return;

        this.event.accept(event);
    }
}

