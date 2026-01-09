package self.starvern.ultimateuserinterface.item;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.lib.*;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.properties.GuiProperties;
import self.starvern.ultimateuserinterface.properties.GuiProperty;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *     Responsible for building {@link ItemStack}(s) from a config.
 * </p>
 * @since 0.7.0
 */
public class ItemTemplate implements GuiBased
{
    private final UUI api;
    private final GuiItem item;
    private final ConfigurationSection section;

    public ItemTemplate(GuiItem item)
    {
        this.api = item.getApi();
        this.item = item;
        this.section = this.item.getSection();
    }

    /**
     * @param player The {@link OfflinePlayer} to build for.
     * @return A built {@link ItemStack}.
     * @since 0.7.0
     */
    public ItemStack build(OfflinePlayer player)
    {
        ItemStack itemStack = ItemStack.of(Material.STONE);

        Set<String> keys = this.section.getKeys(false);

        GuiContext context = new GuiContext(
                this.item,
                player
        );

        for (ItemFieldType<?, ?> fieldType : this.api.getFieldManager().getFields())
        {
            if (!keys.contains(fieldType.getKey()))
                continue;

            ItemField<?, ?> field = fieldType.fillField(this, this.section);
            itemStack = field.apply(itemStack, context);
        }

        for (String key : this.section.getKeys(false))
        {
            @Nullable ItemFieldType<?, ?> fieldType = this.api.getFieldManager().getFieldByKey(key);
            if (fieldType == null)
                continue;

            ItemField<?, ?> field = fieldType.fillField(this, this.section);
            itemStack = field.apply(itemStack, context);
        }

        return itemStack;
    }

    /**
     * @param input The input to parse. (Property & Papi)
     * @return The parsed input.
     * @since 0.7.0
     */
    public String parseAllPlaceholders(@NotNull String input, OfflinePlayer player)
    {
        GuiProperties<GuiItem> itemProperties = this.item.getProperties();
        GuiProperties<GuiPage> pageProperties = this.item.getPage().getProperties();
        GuiProperties<GuiBased> newProps = new GuiProperties<>(this);

        for (GuiProperty<?> prop : pageProperties.getProperties())
            newProps.setProperty(prop, true);

        for (GuiProperty<?> prop : itemProperties.getProperties())
            newProps.setProperty(prop, true);

        return ChatManager.decolorize(newProps.parsePropertyPlaceholders(input, player));
    }

    /**
     * @param input The inputs to parse.
     * @return The parsed input.
     * @since 0.7.0
     */
    public List<String> parseAllPlaceholders(List<String> input, OfflinePlayer player)
    {
        return input.stream().map(i -> this.parseAllPlaceholders(i, player)).toList();
    }

    @Override
    public Gui getGui()
    {
        return this.item.getGui();
    }

    @Override
    public ConfigurationSection getSection()
    {
        return this.section;
    }
}
