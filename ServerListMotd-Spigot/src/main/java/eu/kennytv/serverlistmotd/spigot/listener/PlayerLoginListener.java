package eu.kennytv.serverlistmotd.spigot.listener;

import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public final class PlayerLoginListener implements Listener {
    private final ServerListMotdSpigotPlugin plugin;
    private final UUID notifyUuid = new UUID(-6334418481592579467L, -4779835342378829761L);

    public PlayerLoginListener(final ServerListMotdSpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLogin(final PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;

        final Player p = event.getPlayer();
        // Just a harmless message to maybe give me a little smile :)
        if (p.getUniqueId().equals(notifyUuid)) {
            p.sendMessage("§6ServerListMotdSpigot §aVersion " + plugin.getVersion());
        }

        if (!plugin.getSettings().updateChecksEnabled() || !p.hasPermission("serverlistmotd.admin")) return;

        plugin.async(() -> {
            try {
                if (plugin.updateAvailable()) {
                    p.sendMessage(plugin.getPrefix() + "§cThere is a newer version available: §aVersion " + plugin.getNewestVersion() + "§c, you're still on §a" + plugin.getVersion());

                    final TextComponent tc1 = new TextComponent(TextComponent.fromLegacyText(plugin.getPrefix()));
                    final TextComponent tc2 = new TextComponent(TextComponent.fromLegacyText("§cDownload it at: §6https://www.spigotmc.org/resources/57851"));
                    final TextComponent click = new TextComponent(TextComponent.fromLegacyText(" §7§l§o(CLICK ME)"));
                    click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/57851"));
                    click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aDownload the latest version").create()));
                    tc1.addExtra(tc2);
                    tc1.addExtra(click);

                    p.spigot().sendMessage(tc1);
                }
            } catch (final Exception ignored) {
            }
        });
    }
}
