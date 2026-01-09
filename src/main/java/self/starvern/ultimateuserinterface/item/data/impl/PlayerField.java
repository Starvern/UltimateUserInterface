package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.lib.GuiContext;

public class PlayerField extends ItemField<OfflinePlayer, String>
{
    public PlayerField(ItemTemplate template, ItemFieldType<OfflinePlayer, String> fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable OfflinePlayer player = this.fieldType.getComplex(
                this.primitive,
                s -> context.parseAllPlaceholders(s, context.getPlayer())
        );

        if (player == null)
            return itemStack;

        context.setPlayer(player);

        return itemStack;
    }
}
