package eu.kennytv.serverlistmotd.spigot;

import com.google.common.collect.Lists;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.listener.PacketListener;
import eu.kennytv.serverlistmotd.spigot.listener.PaperServerListPingListener;
import eu.kennytv.serverlistmotd.spigot.listener.ServerListPingListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public final class SettingsSpigot extends Settings {
    private final ServerListMotdSpigotBase base;
    private final IPingListener pingListener;
    private FileConfiguration config;

    SettingsSpigot(final ServerListMotdSpigotPlugin plugin, final ServerListMotdSpigotBase base) {
        this.base = base;

        final PluginManager pm = base.getServer().getPluginManager();
        if (isPaper()) {
            final PaperServerListPingListener listener = new PaperServerListPingListener(plugin, this);
            pm.registerEvents(listener, base);
            pingListener = listener;
        } else if (pm.getPlugin("ProtocolLib") != null) {
            pingListener = new PacketListener(plugin, this);
        } else {
            final ServerListPingListener listener = new ServerListPingListener(plugin, this);
            pm.registerEvents(listener, base);
            pingListener = listener;
        }

        if (!base.getDataFolder().exists())
            base.getDataFolder().mkdirs();

        final File file = new File(base.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (final InputStream in = base.getResource("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create config.yml!", e);
            }
        }

        reloadConfig();
        reloadServerIcon();
    }

    private boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.event.server.PaperServerListPingEvent");
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void updateConfig() {
        if (!config.contains("motd")) return;
        config.set("motds", Lists.newArrayList(config.getString("motd")));
        config.set("motd", null);
        saveConfig();
    }

    @Override
    public String getColoredString(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void saveConfig() {
        try {
            config.save(new File(base.getDataFolder(), "config.yml"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadConfig() {
        final File file = new File(base.getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load config.yml!", e);
        }

        loadSettings();
    }

    @Override
    public String getColoredConfigString(final String path) {
        final String s = config.getString(path);
        if (s == null) {
            base.getLogger().warning("The config is missing the following string: " + path);
            return "null";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public String getConfigString(final String path, final String def) {
        final String s = config.getString(path);
        return s != null ? s : def;
    }

    @Override
    public boolean getConfigBoolean(final String path, final boolean def) {
        return config.getBoolean(path, def);
    }

    @Override
    public List<String> getConfigList(final String path) {
        return config.getStringList(path);
    }

    @Override
    public void setToConfig(final String path, final Object var) {
        config.set(path, var);
    }

    @Override
    public boolean reloadServerIcon() {
        return pingListener.loadIcon();
    }
}
