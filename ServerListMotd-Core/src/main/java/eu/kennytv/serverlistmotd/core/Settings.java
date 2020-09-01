package eu.kennytv.serverlistmotd.core;

import eu.kennytv.serverlistmotd.api.ISettings;

import java.util.List;
import java.util.Random;

public abstract class Settings implements ISettings {
    private static final Random RANDOM = new Random();
    private boolean changePlayerCount;
    private boolean changePlayerCountHoverMessage;
    private boolean showPlayerCount;
    private boolean updateChecks;
    private List<String> motds;
    private String playerCountMessage;
    private String playerCountHoverMessage;
    private String noPermMessage;
    private String serverIconPath;

    protected void loadSettings() {
        updateConfig();
        changePlayerCount = getConfigBoolean("custom-playercountmessage");
        changePlayerCountHoverMessage = getConfigBoolean("custom-playercounthovermessage", true);
        showPlayerCount = getConfigBoolean("show-playercount");
        motds = getConfigList("motds");
        noPermMessage = getColoredConfigString("no-permission-message");
        playerCountMessage = getColoredConfigString("playercountmessage");
        playerCountHoverMessage = getColoredConfigString("playercounthovermessage");
        serverIconPath = getConfigString("server-icon-path", "server-icon.png");
        updateChecks = getConfigBoolean("update-checks", true);
    }

    public abstract void updateConfig();

    public List<String> getMotds() {
        return motds;
    }

    @Override
    public String getMotd() {
        if (motds.isEmpty()) return "";
        final String s = motds.size() > 1 ? motds.get(RANDOM.nextInt(motds.size())) : motds.get(0);
        return getColoredString(s).replace("%NEWLINE%", "\n");
    }

    @Override
    public String getPlayerCountMessage() {
        return playerCountMessage;
    }

    @Override
    public String getPlayerCountHoverMessage() {
        return playerCountHoverMessage;
    }

    @Override
    public boolean hasCustomPlayerCount() {
        return changePlayerCount;
    }

    @Override
    public boolean hasCustomPlayerCountHoverMessage() {
        return changePlayerCountHoverMessage;
    }

    @Override
    public boolean showPlayerCount() {
        return showPlayerCount;
    }

    @Override
    public String getServerIconPath() {
        return serverIconPath;
    }

    @Override
    public boolean updateChecksEnabled() {
        return updateChecks;
    }

    public abstract String getColoredString(String message);

    public abstract void saveConfig();

    public abstract void setToConfig(String path, Object var);

    public abstract String getColoredConfigString(String path);

    public abstract String getConfigString(String path, String def);

    public abstract boolean getConfigBoolean(String path, boolean def);

    public boolean getConfigBoolean(final String path) {
        return getConfigBoolean(path, false);
    }

    public abstract List<String> getConfigList(String path);

    public String getNoPermMessage() {
        return noPermMessage;
    }
}
