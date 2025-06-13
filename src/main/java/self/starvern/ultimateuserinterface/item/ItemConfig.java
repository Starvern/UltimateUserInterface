package self.starvern.ultimateuserinterface.item;

import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.properties.GuiProperties;
import self.starvern.ultimateuserinterface.utils.PlayerUtility;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * <p>
 *     Reads all values from the config and converts them into usable types.
 *     If any values are unspecified or null, default types are provided
 * </p>
 * @since 0.5.0
 */
public class ItemConfig implements Serializable
{
    private final UUI api;
    private final GuiItem item;
    private final ConfigurationSection section;

    private String name;
    private String rawMaterial;
    private List<String> lore;
    private String rawAmount;

    private PersistentDataContainer container;

    private final @Nullable String texture;
    private final @Nullable String playerName;
    private final @Nullable String hdbId;
    private final @Nullable String customModelData;

    private final ConfigurationSection enchantmentSection;
    private final List<String> itemFlags;

    public ItemConfig(GuiItem item)
    {
        this.item = item;
        this.api = item.getApi();
        this.section = item.getSection();
        this.name = section.getString("name", "");
        this.rawMaterial = section.getString("material", "AIR");
        this.lore = section.getStringList("lore");
        this.rawAmount = section.getString("amount", "1");

        this.texture = section.getString("texture");
        this.hdbId = section.getString("hdb");
        this.playerName = section.getString("player");
        this.customModelData = section.getString("custom_model_data");

        this.enchantmentSection = section.getConfigurationSection("enchantments");
        this.itemFlags = section.getStringList("flags");
    }

    /**
     * @param rawAmount The amount to set to.
     * @since 0.6.0
     */
    public void setRawAmount(String rawAmount)
    {
        this.rawAmount = rawAmount;
    }

    /**
     * Copies the {@link ItemMeta} of the given item.
     * @param itemStack The {@link ItemStack} to copy.
     * @since 0.6.2
     */
    public void copyOf(ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        this.name = itemMeta.getDisplayName();
        this.lore = itemMeta.getLore();
        this.rawMaterial = itemStack.getType().toString();
        this.rawAmount = String.valueOf(itemStack.getAmount());
        this.container = itemMeta.getPersistentDataContainer();
    }

    /**
     * @return A new ItemConfig with all the same values.
     * @since 0.5.0
     */
    public ItemConfig copy()
    {
        return new ItemConfig(this.item);
    }

    /**
     * @return The display name of the item. [section.name]
     * @since 0.5.0
     */
    @NotNull
    public String getName()
    {
        return this.name;
    }

    /**
     * @return The material of the item (without matching to Material). [section.material]
     * @since 0.5.0
     */
    @NotNull
    public String getRawMaterial()
    {
        return this.rawMaterial;
    }

    public void setRawMaterial(String rawMaterial)
    {
        this.rawMaterial = rawMaterial;
    }

    /**
     * @return The lore of the item. [section.lore]
     * @since 0.5.0
     */
    @NotNull
    public List<String> getLore()
    {
        return this.lore;
    }

    /**
     * @return The amount of the item (default: 1). [section.amount]
     * @since 0.5.0
     */
    public String getRawAmount()
    {
        return this.rawAmount;
    }

    /**
     * @return The section of the item.
     * @since 0.5.0
     */
    @Nullable
    public ConfigurationSection getSection()
    {
        return this.section;
    }

    /**
     * @return The hdb id of the item. [section.hdb]
     * @since 0.5.0
     */
    public @Nullable String getHdbId()
    {
        return this.hdbId;
    }

    /**
     * @return The player to parse heads and placeholders for. [section.player]
     * @since 0.5.0
     */
    public @Nullable String getPlayerName()
    {
        return this.playerName;
    }

    /**
     * @return The texture of the item (if #getMaterial is PLAYER_HEAD). [section.texture]
     * @since 0.5.0
     */
    public @Nullable String getTexture()
    {
        return this.texture;
    }

    /**
     * @param displayName The name of the enchantment.
     * @return The enchantment based on the name provided.
     * @since 0.3.7
     */
    private static @Nullable Enchantment getEnchant(String displayName)
    {
        for (Enchantment enchantment : Registry.ENCHANTMENT)
        {
            if (enchantment.getKey().getKey().equalsIgnoreCase(displayName))
                return enchantment;
        }

        return null;
    }

    /**
     * @return The enchantments on the item. [section.enchantments]
     * @since 0.5.0
     */
    public Map<Enchantment, Integer> getEnchantments()
    {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (this.enchantmentSection == null)
            return enchantments;

        for (String enchantName : enchantmentSection.getKeys(false))
        {
            @Nullable Enchantment enchantment = getEnchant(enchantName);
            if (enchantment == null) continue;

            enchantments.put(enchantment, enchantmentSection.getInt(enchantName));
        }

        return enchantments;
    }

    /**
     * @return The item flags on the item. [section.flags]
     * @since 0.5.0
     */
    public List<ItemFlag> getItemFlags()
    {
        List<ItemFlag> flags = new ArrayList<>();

        for (String flagName : this.itemFlags)
        {
            try
            {
                flags.add(ItemFlag.valueOf(flagName.toUpperCase(Locale.ROOT)));
            }
            catch (IllegalArgumentException ignored)
            {
                this.item.getGui().getLogger().warning("Invalid item flag: " + flagName);
            }
        }

        return flags;
    }

    /**
     * Parses all properties placeholders up to 5 times (if nested).
     * @param input The input to parse.
     * @return The parsed input.
     * @since 0.5.0
     */
    public String parseAllPlaceholders(String input)
    {
        GuiProperties<GuiItem> itemProperties = this.item.getProperties();
        GuiProperties<GuiPage> pageProperties = this.item.getPage().getProperties();

        return pageProperties.parsePropertyPlaceholders(
                itemProperties.parsePropertyPlaceholders(input)
        );
    }

    /**
     * Parses all properties placeholders up to 5 times (if nested).
     * @param input The inputs to parse.
     * @return The parsed input.
     * @since 0.5.0
     */
    public List<String> parseAllPlaceholders(List<String> input)
    {
        GuiProperties<GuiItem> itemProperties = this.item.getProperties();
        GuiProperties<GuiPage> pageProperties = this.item.getPage().getProperties();

        return pageProperties.parsePropertyPlaceholders(
                itemProperties.parsePropertyPlaceholders(input)
        );
    }

    /**
     * Sets a base64 texture onto an item.
     * @param itemMeta The meta to set onto.
     * @param texture The texture (base64) to parse.
     * @since 0.5.0
     */
    public void parseTexture(ItemMeta itemMeta, String texture)
    {
        if (texture == null) return;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "textures");
        profile.getProperties().put("textures", new Property("textures", texture));

        try
        {
            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalAccessException exception)
        {
            this.api.getPlugin().getLogger().warning("Failed to parse texture: " + texture);
        }
    }

    /**
     * Sets a base64 texture onto an item.
     * @param itemMeta The meta to set onto.
     * @param playerName The player's name to parse.
     * @since 0.5.0
     */
    public void parsePlayerHead(ItemMeta itemMeta, String playerName)
    {
        if (playerName == null) return;

        GameProfile profile = new GameProfile(UUID.randomUUID(), playerName);

        String idUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        try {
            URL url = new URL(idUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + data.get("id").getAsString() + "?unsigned=false");

            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = JsonParser.parseReader(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            Property prop = new Property("textures", texture, signature);
            profile.getProperties().put("textures", prop);

            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalAccessException | IOException exception)
        {
            this.api.getPlugin().getLogger().warning("Failed to parse player texture: " + playerName);
        }
    }

    /**
     * Compares two lists.
     * @param first The first list.
     * @param second The second list to compare against.
     * @return True if the lists are different.
     * @since 0.5.0
     */
    public boolean compareList(List<String> first, List<String> second)
    {
        if (first.size() != second.size())
            return true;

        for (int i = 0; i < first.size(); i++)
            if (!first.get(i).equals(second.get(i))) return true;

        return false;
    }

    /**
     * @param player The player to parse placeholders for.
     * @param itemStack The item to add meta to.
     * @since 0.5.0
     */
    public void applyMeta(OfflinePlayer player, ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        String playerName = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.playerName));
        boolean playerUnknown = false;

        if (playerName != null)
        {
            OfflinePlayer targetPlayer = PlayerUtility.getPlayer(playerName);
            if (targetPlayer != null)
            {
                player = targetPlayer;
                if (!player.isOnline())
                    playerUnknown = true;
            }
            if (targetPlayer == null && !this.item.getProperties().containsPlaceholders(playerName)
                    && !PlaceholderAPIHook.containsPlaceholders(playerName))
                playerUnknown = true;
        }

        String name = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.name));
        List<String> lore = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.lore));
        String texture = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.texture));
        String hdbId = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.hdbId));
        String rawCustomModelData = PlaceholderAPIHook.parse(player, parseAllPlaceholders(this.customModelData));

        try
        {
            if (rawCustomModelData != null)
            {
                int data = Integer.parseInt(rawCustomModelData);
                itemMeta.setCustomModelData(data);
            }
        }
        catch (NumberFormatException exception)
        {
            item.getGui().getLogger().warning("custom_model_data doesn't parse to a number.");
        }

        if (itemStack.getType().equals(Material.PLAYER_HEAD))
        {
            ((SkullMeta) itemMeta).setOwningPlayer(player);

            if (playerUnknown)
                Bukkit.getScheduler().runTaskAsynchronously(this.api.getPlugin(), () -> {
                    this.parsePlayerHead(itemMeta, playerName);
                    itemStack.setItemMeta(itemMeta);
                    this.item.getPage().update();
                });

            if (hdbId != null && HeadDatabaseHook.getApi() != null)
                texture = HeadDatabaseHook.getApi().getBase64(hdbId);
            if (texture != null)
                this.parseTexture(itemMeta, texture);
        }

        Map<Enchantment, Integer> enchantments = this.getEnchantments();
        for (Enchantment enchantment : enchantments.keySet())
            itemMeta.addEnchant(enchantment, enchantments.get(enchantment), true);

        itemMeta.addItemFlags(this.getItemFlags().toArray(new ItemFlag[0]));
        itemMeta.setAttributeModifiers(MultimapBuilder.hashKeys().hashSetValues().build());

        if (this.container != null)
            this.container.copyTo(itemMeta.getPersistentDataContainer(), true);

        itemMeta.setDisplayName(ChatManager.colorize(name));
        itemMeta.setLore(ChatManager.colorize(lore));

        itemStack.setItemMeta(itemMeta);
    }

    /**
     * <p>
     *     Builds out an ItemStack based on this config's variables.
     *     Additionally, parses properties placeholders & PlaceholderAPI (if installed).
     * </p>
     * @return The built ItemStack.
     * @param player The player to parse placeholders for.
     * @since 0.5.0
     */
    public ItemStack buildItem(OfflinePlayer player)
    {
        String rawMaterial = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.rawMaterial));
        String rawAmount = PlaceholderAPIHook.parse(player, this.parseAllPlaceholders(this.rawAmount));

        int amount = 1;

        try
        {
            amount = Integer.parseInt(rawAmount);
        }
        catch (NumberFormatException e)
        {
            this.api.getLogger().warning("<" + this.item.getGui().getId() + ".yml> Amount does not parse to int.");
        }

        Material material = Material.matchMaterial(rawMaterial);
        if (material == null || !material.isItem()) return new ItemStack(Material.AIR);

        ItemStack item = new ItemStack(material, amount);
        this.applyMeta(player, item);

        return item;
    }

    /**
     * <p>
     *     Builds out an ItemStack based on this config's variables.
     *     Additionally, parses properties placeholders.
     * </p>
     * @return The built ItemStack.
     * @since 0.5.0
     */
    public ItemStack buildItem()
    {
        return this.buildItem(null);
    }
}
