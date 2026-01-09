package self.starvern.ultimateuserinterface.lib;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.properties.GuiProperties;
import self.starvern.ultimateuserinterface.properties.GuiProperty;

import java.util.List;

public class GuiContext implements GuiBased
{
    private final GuiItem guiItem;
    private OfflinePlayer player;

    public GuiContext(GuiItem guiItem, OfflinePlayer player)
    {
        this.guiItem = guiItem;
        this.player = player;
    }

    /**
     * @return The {@link OfflinePlayer} this context is for.
     * @since 0.7.0
     */
    public OfflinePlayer getPlayer()
    {
        return this.player;
    }

    /**
     * @param player The {@link OfflinePlayer} to use this context for.
     * @since 0.7.0
     */
    public void setPlayer(OfflinePlayer player)
    {
        this.player = player;
    }

    /**
     * @param input The input to parse. (Property & Papi)
     * @return The parsed input.
     * @since 0.7.0
     */
    public String parseAllPlaceholders(@NotNull String input, OfflinePlayer player)
    {
        GuiProperties<GuiItem> itemProperties = this.guiItem.getProperties();
        GuiProperties<GuiPage> pageProperties = this.guiItem.getPage().getProperties();
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
        return this.guiItem.getGui();
    }

    @Override
    public ConfigurationSection getSection()
    {
        return this.guiItem.getSection();
    }
}
