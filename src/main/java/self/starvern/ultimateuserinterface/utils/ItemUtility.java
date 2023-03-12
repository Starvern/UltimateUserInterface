package self.starvern.ultimateuserinterface.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Unlike previous versions, this is now a static utility class.
 */
public class ItemUtility
{
    /**
     * @param displayName The name of the enchantment.
     * @return The enchantment based on the name provided.
     * @since 0.3.7
     */
    private static Optional<Enchantment> getEnchant(String displayName)
    {
        return Arrays.stream(Enchantment.values())
                .filter(enchantment -> enchantment.getKey().getKey().equalsIgnoreCase(displayName))
                .findFirst();
    }

    /**
     * @param section The section representation of an item.
     * @return The built item.
     */
    public static ItemStack build(ConfigurationSection section)
    {
        if (section == null)
            return new ItemStack(Material.AIR);

        String name = section.getString("name", "UNKNOWN");
        String materialName = section.getString("material", "AIR");
        List<String> lore = section.getStringList("lore");
        int amount = section.getInt("amount", 1);

        Material material = Material.matchMaterial(materialName);
        if (material == null) material = Material.AIR;

        ItemStack item = new ItemStack(material, amount);

        String texture = section.getString("texture", null);
        String hdbId = section.getString("hdb", null);
        String playerName = section.getString("player", null);

        if (material.equals(Material.PLAYER_HEAD) && hdbId != null)
            item = parseHDB(hdbId);

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        if (material.equals(Material.PLAYER_HEAD))
        {
            if (playerName != null)
                parsePlayerHead((SkullMeta) itemMeta, playerName);
            if (texture != null)
                parseTexture(itemMeta, texture);
        }

        itemMeta.setDisplayName(ChatManager.colorize(name));
        itemMeta.setLore(ChatManager.colorize(lore));

        ConfigurationSection enchantSection  = section.getConfigurationSection("enchantments");
        if (enchantSection != null)
        {
            for (String enchantName : enchantSection.getKeys(false))
            {
                Optional<Enchantment> enchantmentOptional = getEnchant(enchantName);
                if (enchantmentOptional.isEmpty()) continue;

                itemMeta.addEnchant(enchantmentOptional.get(), enchantSection.getInt(enchantName), true);
            }
        }

        for (String flagName : section.getStringList("flags"))
        {
            ItemFlag flag;
            try
            {
                flag = ItemFlag.valueOf(flagName.toUpperCase(Locale.ROOT));
            }
            catch (IllegalArgumentException exception)
            {
                continue;
            }
            itemMeta.addItemFlags(flag);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    private static ItemMeta parseTexture(ItemMeta itemMeta, String texture)
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
            exception.printStackTrace();
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

    public static ItemStack addLocalizedName(UUI api, ItemStack item, String value)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        itemMeta.getPersistentDataContainer().set(api.getKey(), PersistentDataType.STRING, value);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Nullable
    public static String getLocalizedName(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;

        return itemMeta.getPersistentDataContainer()
                .get(api.getKey(), PersistentDataType.STRING);
    }

    public static void removedLocalizedName(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().remove(api.getKey());
        item.setItemMeta(itemMeta);
    }

    public static ItemStack parsePlaceholders(Player player, ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;

        String displayName = PlaceholderAPIHook.parse(player, itemMeta.getDisplayName());
        List<String> lore = PlaceholderAPIHook.parse(player, (itemMeta.getLore() != null) ?
                itemMeta.getLore() : new ArrayList<>());

        itemMeta.setDisplayName(ChatManager.colorize(displayName));
        itemMeta.setLore(ChatManager.colorize(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}