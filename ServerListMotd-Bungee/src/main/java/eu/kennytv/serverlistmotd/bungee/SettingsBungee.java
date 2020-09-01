package eu.kennytv.serverlistmotd.bungee;

import com.google.common.collect.Lists;
import eu.kennytv.serverlistmotd.bungee.listener.ProxyPingListener;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public final class SettingsBungee extends Settings {
    private final ServerListMotdBungeeBase plugin;
    private final IPingListener pingListener;
    private Configuration config;

    SettingsBungee(final ServerListMotdBungeeBase plugin) {
        this.plugin = plugin;

        final PluginManager pm = plugin.getProxy().getPluginManager();
        final ProxyPingListener listener = new ProxyPingListener(this);
        pm.registerListener(plugin, listener);
        pingListener = listener;

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        final File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (final InputStream in = plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (final IOException e) {
                throw new RuntimeException("Unable to create config.yml!", e);
            }
        }

        reloadConfig();
        reloadServerIcon();
    }

    @Override
    public void reloadConfig() {
        final File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to load config.yml!", e);
        }

        loadSettings();
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
        final File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config, new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to save config.yml!", e);
        }
    }

    @Override
    public void setToConfig(final String path, final Object var) {
        config.set(path, var);
    }

    @Override
    public String getColoredConfigString(final String path) {
        final String s = config.getString(path);
        if (s == null) {
            plugin.getLogger().warning("The config is missing the following string: " + path);
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
    public boolean reloadServerIcon() {
        return pingListener.loadIcon();
    }
}
