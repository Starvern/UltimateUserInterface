package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

import java.util.List;
import java.util.Optional;

public class PlayerListMacro extends Macro
{

    public PlayerListMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "playerList");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(action.getHolder() instanceof GuiPage page)) return;

        if (action.getArguments().size() < 2) return;

        String targetItemId = action.getArguments().get(0);
        String replacementItemId = action.getArguments().get(1);

        Optional<GuiItem> replacementItemOptional = page.getItem(replacementItemId);

        if (replacementItemOptional.isEmpty()) return;

        GuiItem replacementItem = replacementItemOptional.get();

        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .map(OfflinePlayer::getPlayer)
                .toList();
        event.getGui().ensureSize(event.getPage(), targetItemId, onlinePlayers.size());
        List<SlottedGuiItem> targetItems = event.getGui().getAllSlottedItems(targetItemId);

        for (int index = 0; index < targetItems.size(); index ++)
        {
            SlottedGuiItem item = targetItems.get(index);
            if (index >= onlinePlayers.size())
            {
                replacementItem.slot(item.getSlot());
                continue;
            }
            Player player = onlinePlayers.get(index);

            StringProperty property = new StringProperty("player", player.getName());

            item.getProperties().setProperty(property, true);
        }
    }
}
