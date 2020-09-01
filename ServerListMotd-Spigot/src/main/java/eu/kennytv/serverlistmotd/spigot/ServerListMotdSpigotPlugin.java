package eu.kennytv.serverlistmotd.spigot;

import eu.kennytv.serverlistmotd.api.IServerListMotd;
import eu.kennytv.serverlistmotd.api.ISettings;
import eu.kennytv.serverlistmotd.api.ServerListMotdSpigotAPI;
import eu.kennytv.serverlistmotd.core.ServerListMotdPlugin;
import eu.kennytv.serverlistmotd.spigot.command.ServerListMotdSpigotCommand;
import eu.kennytv.serverlistmotd.spigot.listener.PlayerLoginListener;
import eu.kennytv.serverlistmotd.spigot.metrics.MetricsLite;
import eu.kennytv.serverlistmotd.spigot.util.PlaceholderWrapper;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

import java.io.File;

public final class ServerListMotdSpigotPlugin extends ServerListMotdPlugin {
    private final ServerListMotdSpigotBase plugin;
    private final SettingsSpigot settings;
    private final PlaceholderWrapper placeholderWrapper;

    ServerListMotdSpigotPlugin(final ServerListMotdSpigotBase plugin) {
        super("§8[§bServerListMotdSpigot§8] ", plugin.getDescription().getVersion());

        this.plugin = plugin;
        settings = new SettingsSpigot(this, plugin);

        plugin.getLogger().info("Plugin by KennyTV");
        plugin.getCommand("serverlistmotdspigot").setExecutor(new ServerListMotdSpigotCommand(this, settings));

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getServer().getLogger().info("Found PlaceholderAPI, now using its placeholders!");
            placeholderWrapper = new PlaceholderWrapper();
        } else {
            placeholderWrapper = null;
        }

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerLoginListener(this), plugin);

        new MetricsLite(plugin);
    }

    @Override
    public void async(final Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public ISettings getSettings() {
        return settings;
    }

    @Override
    public File getPluginFile() {
        return plugin.getPluginFile();
    }

    @Deprecated
    public static IServerListMotd getAPI() {
        return ServerListMotdSpigotAPI.getAPI();
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public ServerListMotdSpigotBase getPlugin() {
        return plugin;
    }

    public String replacePlaceholders(final String s) {
        return placeholderWrapper != null ? placeholderWrapper.replace(s) : s;
    }
}
