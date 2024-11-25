package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.managers.ChatManager;

/**
 * Sends the player a message.
 * @since 0.4.2
 */
public class MessageMacro extends Macro
{
    public MessageMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "message");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        this.sendMessage((Player) event.getHuman(), String.join(" ", action.getArguments()));
    }

    private void sendMessage(Player player, String text)
    {
        player.sendMessage(ChatManager.colorize(text));
    }


}
