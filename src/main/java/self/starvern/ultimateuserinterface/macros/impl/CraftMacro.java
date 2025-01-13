package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.*;

public class CraftMacro extends Macro {
    private final UUI api;

    public CraftMacro(UUI api, Plugin plugin) {
        super(api, plugin, "craft");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action) {
        Player player = (Player) event.getHuman();

        if (action.getArguments().isEmpty())
            return;

        String character = action.getArguments().get(0);

        Set<NamespacedKey> discoveredRecipes = player.getDiscoveredRecipes();
        List<ItemStack> craftItems = new ArrayList<>();

        for (NamespacedKey itemKey : discoveredRecipes)
        {
            if (!canCraft(player, itemKey))
                continue;

            Material material = Registry.MATERIAL.get(itemKey);
            if (material == null)
                continue;
            craftItems.add(new ItemStack(material));
        }

        if (craftItems.isEmpty())
        {
            if (action.getArguments().size() == 1)
                return;

            String replacement = action.getArguments().get(1);
            Optional<GuiItem> replacementItem = event.getPage().getItem(replacement);
            if (replacementItem.isEmpty())
                return;

            List<SlottedGuiItem> items = event.getPage().getSlottedItems(character);
            items.get(0).setItem(replacementItem.get().getItem());
        }

        event.getGui().ensureSize(event.getPage(), character, craftItems.size());

        int index = 0;

        for (SlottedGuiItem item : event.getGui().getAllSlottedItems(character))
        {
            if (index < craftItems.size())
                item.setItem(craftItems.get(index++));
        }

        event.getGui().getPages().forEach(GuiPage::update);
    }

    private boolean canCraft(Player player, NamespacedKey itemKey) {
        Recipe recipe = api.getPlugin().getServer().getRecipe(itemKey);
        boolean canCraft = false;

        if (recipe instanceof ShapedRecipe shapedRecipe)
            canCraft = checkShaped(player, shapedRecipe);

        return canCraft;
    }

    private boolean checkShaped(Player player, ShapedRecipe shapedRecipe)
    {
        int progress = 0;
        boolean canCraft = false;
        Collection<ItemStack> requiredItems = shapedRecipe.getIngredientMap().values();
        Map<ItemStack, Integer> requiredAmounts = new HashMap<>();

        for (ItemStack requiredItem : requiredItems) {
            if (requiredItem == null) continue;
            requiredAmounts.computeIfPresent(requiredItem, (item, amount) -> amount + item.getAmount());
            requiredAmounts.computeIfAbsent(requiredItem, ItemStack::getAmount);
        }

        for (ItemStack item : requiredAmounts.keySet()) {
            int requiredAmount = requiredAmounts.get(item);
            if (player.getInventory().containsAtLeast(item, requiredAmount))
                progress++;
        }

        if (progress == requiredAmounts.size())
            canCraft = true;

        return canCraft;
    }
}
