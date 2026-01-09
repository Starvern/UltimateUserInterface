package self.starvern.ultimateuserinterface.item.data.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.hooks.HeadDatabaseHook;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.lib.GuiContext;

import java.util.UUID;

public class TextureField extends ItemField<String, String>
{
    public TextureField(ItemTemplate template, ItemFieldType<String, String> fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable String texture = this.fieldType.getComplex(
                this.primitive,
                s -> this.template.parseAllPlaceholders(s, context.getPlayer())
        );

        if (texture == null)
            return itemStack;

        if (texture.startsWith("player:"))
            return parsePlayer(itemStack, texture.replace("player:", ""));

        if (texture.startsWith("hdb:"))
            return parseHdb(itemStack, texture.replace("hdb:", ""));

        return parseTexture(itemStack, texture);
    }

    private ItemStack parsePlayer(ItemStack itemStack, String playerName)
    {
        itemStack.editMeta(SkullMeta.class, meta -> {
            OfflinePlayer player = Bukkit.getServer().getOfflinePlayerIfCached(playerName);
            if (player == null)
                return;
            meta.setOwningPlayer(player);
        });
        return itemStack;
    }

    private ItemStack parseHdb(ItemStack itemStack, String hdbId)
    {
        if (!HeadDatabaseHook.isInstalled() || HeadDatabaseHook.getApi() == null)
            return itemStack;

        String texture = HeadDatabaseHook.getApi().getBase64(hdbId);

        return parseTexture(itemStack, texture);
    }

    private ItemStack parseTexture(ItemStack itemStack, String texture)
    {
        if (texture == null)
            return itemStack;

        itemStack.editMeta(SkullMeta.class, meta -> {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", texture));
            meta.setPlayerProfile(profile);
        });

        return itemStack;
    }
}
