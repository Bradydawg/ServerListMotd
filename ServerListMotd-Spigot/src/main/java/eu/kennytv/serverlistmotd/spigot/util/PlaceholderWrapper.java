package eu.kennytv.serverlistmotd.spigot.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public final class PlaceholderWrapper {

    public String replace(final String s) {
        return PlaceholderAPI.setPlaceholders((OfflinePlayer) null, s);
    }
}
