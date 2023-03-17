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
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());

        if (action.getArguments().size() == 0)
            return;

        String character = action.getArguments().get(0);
        event.getGui().ensureSize(event.getPage(), character, onlinePlayers.size());
        int index = 0;

        for (GuiItem item : event.getGui().getAllItems(character))
        {
            if (index < onlinePlayers.size())
            {
                parseItem(item, onlinePlayers.get(index++));
                continue;
            }

            if (action.getArguments().size() == 1)
                return;

            String replaceCharacter = action.getArguments().get(1);
            parseReplacement(item, replaceCharacter);
        }

    }

    private void parseItem(GuiItem item, Player player)
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

    private void parseReplacement(GuiItem item, String replaceCharacter)
    {
        Optional<GuiItem> optionalItem = item.getPage().getItems().stream()
                .filter(guiItem -> guiItem.getId().equalsIgnoreCase(replaceCharacter))
                .findFirst();

        if (optionalItem.isEmpty()) return;
        item.setItem(optionalItem.get().getItem());
        item.setActions(optionalItem.get().getActions());
    }
}
