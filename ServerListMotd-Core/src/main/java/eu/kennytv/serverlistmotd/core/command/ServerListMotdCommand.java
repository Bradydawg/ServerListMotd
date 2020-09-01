package eu.kennytv.serverlistmotd.core.command;

import eu.kennytv.serverlistmotd.core.ServerListMotdPlugin;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.util.SenderInfo;

import java.util.Arrays;

public abstract class ServerListMotdCommand {
    protected final ServerListMotdPlugin plugin;
    protected final Settings settings;
    private final String name;

    protected ServerListMotdCommand(final ServerListMotdPlugin plugin, final Settings settings, final String name) {
        this.plugin = plugin;
        this.settings = settings;
        this.name = name;
    }

    public void execute(final SenderInfo sender, final String[] args) {
        if (checkPermission(sender, "command")) return;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (checkPermission(sender, "reload")) return;
                settings.reloadConfig();
                settings.reloadServerIcon();
                sender.sendMessage(plugin.getPrefix() + "§aReloaded config and the motd icon");
            } else if (args[0].equalsIgnoreCase("update")) {
                if (checkPermission(sender, "update")) return;
                checkForUpdate(sender);
            } else if (args[0].equals("forceupdate")) {
                if (checkPermission(sender, "update")) return;
                sender.sendMessage(plugin.getPrefix() + "§c§lDownloading update...");

                if (plugin.installUpdate())
                    sender.sendMessage(plugin.getPrefix() + "§a§lThe update was successful! To prevent issues with tasks and to complete the update, you have to restart the server!");
                else
                    sender.sendMessage(plugin.getPrefix() + "§4Failed!");
            } else if (args[0].equalsIgnoreCase("list")) {
                if (checkPermission(sender, "list")) return;
                sender.sendMessage(plugin.getPrefix() + "§7List of your motds:");
                for (int i = 0; i < settings.getMotds().size(); i++) {
                    sender.sendMessage("§b" + (i + 1) + "§8§m---------");
                    for (final String motd : settings.getColoredString(settings.getMotds().get(i)).split("%NEWLINE%")) {
                        sender.sendMessage(motd);
                    }
                }
                sender.sendMessage("§8§m----------");
            } else
                sendUsage(sender);
        } else if (args.length > 3 && args[0].equalsIgnoreCase("set")) {
            if (checkPermission(sender, "setmotd")) return;
            if (!isNumeric(args[1])) {
                sender.sendMessage(plugin.getPrefix() + "§cThe first argument has to be the motd index!");
                return;
            }

            final int index = Integer.parseInt(args[1]);
            if (index < 1 || index > settings.getMotds().size() + 1) {
                sender.sendMessage(plugin.getPrefix() + "§cYou currently have " + settings.getMotds().size()
                        + " motds, so you have to pick a number between 1 and " + (settings.getMotds().size() + 1));
                return;
            }

            if (!isNumeric(args[2])) {
                sender.sendMessage(plugin.getPrefix() + "§cThe second argument has to be the line number (1 or 2)!");
                return;
            }

            final int line = Integer.parseInt(args[2]);
            if (line != 1 && line != 2) {
                sender.sendMessage(plugin.getPrefix() + "§cThe second argument has to be the line number (1 or 2)!");
                return;
            }

            final String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
            final String oldMessage = index > settings.getMotds().size() ? "" : settings.getMotds().get(index - 1);
            final String newMessage;
            if (line == 1)
                newMessage = oldMessage.contains("%NEWLINE%") ?
                        message + "%NEWLINE%" + oldMessage.split("%NEWLINE%", 2)[1] : message;
            else
                newMessage = oldMessage.contains("%NEWLINE%") ?
                        oldMessage.split("%NEWLINE%", 2)[0] + "%NEWLINE%" + message : oldMessage + "%NEWLINE%" + message;

            if (index > settings.getMotds().size())
                settings.getMotds().add(newMessage);
            else
                settings.getMotds().set(index - 1, newMessage);
            settings.setToConfig("motds", settings.getMotds());
            settings.saveConfig();
            sender.sendMessage(plugin.getPrefix() + "§aSet line " + line + " of the " + index + ". motd to §f" + settings.getColoredString(message));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            if (checkPermission(sender, "setmotd")) return;
            if (!isNumeric(args[1])) {
                sender.sendMessage(plugin.getPrefix() + "§cThe argument has to be a number!");
                return;
            }
            if (settings.getMotds().size() <= 1) {
                sender.sendMessage(plugin.getPrefix() + "§cYou can only remove a motd if there are more than 1!");
                return;
            }

            final int index = Integer.parseInt(args[1]);
            if (index < 1 || index > settings.getMotds().size()) {
                sender.sendMessage(plugin.getPrefix() + "§cYou currently have " + settings.getMotds().size()
                        + " motds, so you have to pick a number between 1 and " + settings.getMotds().size());
                return;
            }

            settings.getMotds().remove(index - 1);

            settings.setToConfig("motds", settings.getMotds());
            settings.saveConfig();
            sender.sendMessage(plugin.getPrefix() + "§7Removed §emotd " + index);
        } else
            sendUsage(sender);
    }

    private void sendUsage(final SenderInfo sender) {
        sender.sendMessage("");
        sender.sendMessage("§8===========[ §e" + name + " §8| §eVersion: §e" + plugin.getVersion() + " §8]===========");
        if (sender.hasPermission("serverlistmotd.reload"))
            sender.sendMessage("§6/motd reload §7(Reloads the config file and the server-icon)");
        if (sender.hasPermission("serverlistmotd.setmotd")) {
            sender.sendMessage("§6/motd set <index> <1/2> <motd> §7(Sets a motd)");
            sender.sendMessage("§6/motd remove <index> §7(Removes a motd)");
        }
        if (sender.hasPermission("serverlistmotd.list"))
            sender.sendMessage("§6/motd list §7(Lists the currently set motds)");
        if (sender.hasPermission("serverlistmotd.update"))
            sender.sendMessage("§6/motd update §7(Remotely downloads the newest version of the plugin onto your server)");
        sender.sendMessage("§7Created by §bKennyTV");
        sender.sendMessage("§8===========[ §e" + name + " §8| §eVersion: §e" + plugin.getVersion() + " §8]===========");
        sender.sendMessage("");
    }

    private boolean checkPermission(final SenderInfo sender, final String permission) {
        if (!sender.hasPermission("serverlistmotd." + permission)) {
            sender.sendMessage(settings.getNoPermMessage());
            return true;
        }
        return false;
    }

    private boolean isNumeric(final String string) {
        try {
            Integer.parseInt(string);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected abstract void checkForUpdate(SenderInfo sender);
}
