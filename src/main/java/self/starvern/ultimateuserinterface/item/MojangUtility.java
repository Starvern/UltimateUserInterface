package self.starvern.ultimateuserinterface.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.UUID;

public class MojangUtility
{
    /**
     * Sets a base64 texture onto an item.
     * @param itemMeta The meta to set onto.
     * @param playerName The player's name to parse.
     * @since 0.7.0
     */
    public static void parsePlayerHead(ItemMeta itemMeta, String playerName)
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
            Bukkit.getLogger().warning("Failed to parse player texture: " + playerName);
        }
    }
}
