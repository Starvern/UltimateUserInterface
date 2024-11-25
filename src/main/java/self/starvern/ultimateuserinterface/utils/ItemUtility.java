package self.starvern.ultimateuserinterface.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.item.ItemConfig;
import self.starvern.ultimateuserinterface.item.ItemContainer;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.utils.PlayerUtility;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * As of 0.4.0, this is now a static utility class.
 */
public class ItemUtility
{
    public static ItemStack build(UUI api, ItemConfig config)
    {
        if (config.getSection() == null)
            return new ItemStack(Material.AIR);

        Material material = config.getMaterial();
        String hdbId = config.getHdbId();

        ItemStack item = new ItemStack(material, config.getAmount());

        if (material.equals(Material.PLAYER_HEAD) && !hdbId.isEmpty())
            item = parseHDB(hdbId);

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        if (material.equals(Material.PLAYER_HEAD))
        {
            String playerName = config.getPlayerName();
            String texture = config.getTexture();

            if (!playerName.isEmpty())
                parsePlayerHead((SkullMeta) itemMeta, playerName);
            if (!texture.isEmpty())
                parseTexture(api, itemMeta, texture);
        }

        Map<Enchantment, Integer> enchantments = config.getEnchantments();
        for (Enchantment enchantment : enchantments.keySet())
            itemMeta.addEnchant(enchantment, enchantments.get(enchantment), true);

        itemMeta.addItemFlags(config.getItemFlags().toArray(new ItemFlag[0]));

        itemMeta.setDisplayName(ChatManager.colorize(config.getName()));
        itemMeta.setLore(ChatManager.colorize(config.getLore()));

        itemMeta.getPersistentDataContainer().set(api.getItemKey(), new ItemContainer(), config);
        item.setItemMeta(itemMeta);

        return item;
    }

    /**
     * @param section The section representation of an item.
     * @return The built item.
     */
    public static ItemStack build(UUI api, File file, ConfigurationSection section)
    {
        return build(api, new ItemConfig(file, section));
    }

    private static ItemMeta parseTexture(UUI api, ItemMeta itemMeta, String texture)
    {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try
        {
            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalAccessException exception)
        {
            api.getPlugin().getLogger().warning(exception.toString());
        }

        return itemMeta;
    }

    private static ItemStack parseHDB(String id)
    {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if (HeadDatabaseHook.getApi().isPresent())
        {
            ItemStack headItem = HeadDatabaseHook.getApi().get().getItemHead(id);
            if (headItem != null)
                item = headItem;
        }
        return item;
    }

    private static ItemMeta parsePlayerHead(SkullMeta itemMeta, String playerName)
    {
        itemMeta.setOwningPlayer(PlayerUtility.getPlayer(playerName).orElse(null));
        return itemMeta;
    }

    public static ItemStack addUUID(UUI api, ItemStack item, String value)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        itemMeta.getPersistentDataContainer().set(api.getKey(), PersistentDataType.STRING, value);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Nullable
    public static String getUUID(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;

        return itemMeta.getPersistentDataContainer()
                .get(api.getKey(), PersistentDataType.STRING);
    }

    public static void removeUUID(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().remove(api.getKey());
        item.setItemMeta(itemMeta);
    }

    public static ItemStack parsePlaceholders(UUI api, Player player, ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;

        ItemConfig config = itemMeta.getPersistentDataContainer().get(api.getItemKey(), new ItemContainer());
        if (config == null)
            return itemStack;

        String displayName = PlaceholderAPIHook.parse(player, config.getName());
        List<String> lore = PlaceholderAPIHook.parse(player, config.getLore());

        itemMeta.setDisplayName(ChatManager.colorize(displayName));
        itemMeta.setLore(ChatManager.colorize(lore));

        if (itemStack.getType().equals(Material.PLAYER_HEAD))
        {
            String playerName = config.getPlayerName();
            String texture = config.getTexture();

            if (!playerName.isEmpty())
                parsePlayerHead((SkullMeta) itemMeta, PlaceholderAPIHook.parse(player, playerName));
            if (!texture.isEmpty())
                parseTexture(api, itemMeta, PlaceholderAPIHook.parse(player, texture));
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}