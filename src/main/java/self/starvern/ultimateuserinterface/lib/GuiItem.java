package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.managers.ItemManager;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;

public class GuiItem
{
    private final GuiPage page;
    private final String id;
    private final ItemUtility item;
    private final NamespacedKey key = new NamespacedKey(UUI.getSingleton(), "uui-item-id");
    private final int slot;
    private final UUID uuid = UUID.randomUUID();

    private Consumer<GuiItemClickEvent> event;

    public GuiItem(GuiPage page, int slot)
    {
        this.page = page;
        this.id = "";
        this.item = new ItemUtility(Material.AIR);
        this.slot = slot;
    }

    public GuiItem(GuiPage page, String id, int slot)
    {
        this.page = page;
        this.id = id;
        this.item = ItemManager.buildItem(page.getGui().getConfig(), this.id);
        this.slot = slot;
    }

    public int getSlot()
    {
        return this.slot;
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
     * @return The GUI page this item appears in.
     * @since 0.1.0
     */
    public GuiPage getPage()
    {
        return this.page;
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
    public GuiItem executes(Consumer<GuiItemClickEvent> event)
    {
        this.event = event;
        return this;
    }

    /**
     * Executes the event
     * @param event The event to run
     * @since 0.1.0
     */
    public void runEvent(@NotNull GuiItemClickEvent event)
    {
        if (this.event == null) return;

        this.event.accept(event);
    }
}

