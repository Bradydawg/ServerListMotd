package eu.kennytv.serverlistmotd.bungee;

import eu.kennytv.serverlistmotd.api.IServerListMotd;
import eu.kennytv.serverlistmotd.api.ISettings;
import eu.kennytv.serverlistmotd.api.ServerListMotdBungeeAPI;
import eu.kennytv.serverlistmotd.bungee.command.ServerListMotdBungeeCommand;
import eu.kennytv.serverlistmotd.bungee.command.ServerListMotdBungeeCommandBase;
import eu.kennytv.serverlistmotd.bungee.listener.PostLoginListener;
import eu.kennytv.serverlistmotd.bungee.metrics.MetricsLite;
import eu.kennytv.serverlistmotd.core.ServerListMotdPlugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;

public final class ServerListMotdBungeePlugin extends ServerListMotdPlugin {
    private final ServerListMotdBungeeBase plugin;
    private final SettingsBungee settings;

    ServerListMotdBungeePlugin(final ServerListMotdBungeeBase plugin) {
        super("§8[§bServerListMotdBungee§8] ", plugin.getDescription().getVersion());
        this.plugin = plugin;
        plugin.getLogger().info("Plugin by KennyTV");

        settings = new SettingsBungee(plugin);

        final PluginManager pm = plugin.getProxy().getPluginManager();
        pm.registerListener(plugin, new PostLoginListener(this));
        final ServerListMotdBungeeCommand command = new ServerListMotdBungeeCommand(this, settings);
        pm.registerCommand(plugin, new ServerListMotdBungeeCommandBase(command));

        new MetricsLite(plugin);
    }

    @Deprecated
    public static IServerListMotd getAPI() {
        return ServerListMotdBungeeAPI.getAPI();
    }

    @Override
    public ISettings getSettings() {
        return settings;
    }

    @Override
    public File getPluginFile() {
        return plugin.getPluginFile();
    }

    @Override
    public void async(final Runnable runnable) {
        plugin.getProxy().getScheduler().runAsync(plugin, runnable);
    }
}
