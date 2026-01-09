package self.starvern.ultimateuserinterface.item.data.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

import java.util.List;

public class LoreField extends ItemField<List<Component>, List<String>>
{
    public LoreField(ItemTemplate template, LoreFieldType fieldType, List<String> primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable List<Component> lore = this.fieldType.getComplex(
                primitive,
                s -> this.template.parseAllPlaceholders(s, context.getPlayer())
        );

        if (lore == null || itemStack.isEmpty())
            return itemStack;

        itemStack.editMeta(meta -> meta.lore(lore));

        return itemStack;
    }
}
