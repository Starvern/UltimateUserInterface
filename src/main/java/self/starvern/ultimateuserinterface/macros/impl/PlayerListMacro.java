package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *      This macro is exclusive to GuiPage's actions. It is recommended to use with
 *      either tick, or the less intensive open action type. It will automatically populate
 *      items and parse placeholders as each respective player.
 *      Usage:
 *          '[playerlist] <gui item id> <gui item id if no more players>'
 * </p>
 * @since 0.4.2
 */
public class PlayerListMacro extends Macro
{
    private final UUI api;

    public PlayerListMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "playerlist");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(action.getHolder() instanceof GuiPage))
            return;

        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .map(OfflinePlayer::getPlayer)
                .toList();

        if (action.getArguments().isEmpty())
            return;

        String character = action.getArguments().get(0);
        String replaceCharacter = "";

        if (action.getArguments().size() > 1)
            replaceCharacter = action.getArguments().get(1);

        event.getGui().ensureSize(event.getPage(), character, onlinePlayers.size());
        int index = 0;

        for (SlottedGuiItem item : event.getGui().getAllSlottedItems(character))
        {
            if (index < onlinePlayers.size())
            {
                parseItem(item, onlinePlayers.get(index++));
                continue;
            }

            if (action.getArguments().size() == 1)
                continue;

            parseReplacement(item, replaceCharacter);
        }
    }

    private void parseItem(SlottedGuiItem item, Player player)
    {
        ItemStack itemStack = item.getPage().getInventory().getItem(item.getSlot());

        if (itemStack == null || itemStack.getType().isAir())
            return;

        for (GuiAction<GuiItem> itemAction : item.getActions())
        {
            List<String> newArgs = itemAction.getArguments().stream()
                    .map(argument -> PlaceholderAPIHook.parse(player, argument))
                    .collect(Collectors.toList());

            itemAction.setArguments(newArgs);
        }

        item.setItem(ItemUtility.parsePlaceholders(this.api, player, itemStack));
    }

    private void parseReplacement(SlottedGuiItem item, String replaceCharacter)
    {
        Optional<GuiItem> optionalReplacement = item.getPage().getItem(replaceCharacter);
        if (optionalReplacement.isEmpty()) return;

        GuiItem replacementItem = optionalReplacement.get();

        item.setItem(replacementItem.getItem());
        item.setActions(replacementItem.getActions());
        item.getPage().update();
    }
}
