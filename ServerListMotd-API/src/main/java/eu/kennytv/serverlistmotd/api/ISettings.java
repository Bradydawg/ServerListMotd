package eu.kennytv.serverlistmotd.api;

/**
 * @author KennyTV
 * @since 1.0
 * @author Bradydawg
 * @since 1.2.5
 */
public interface ISettings {

    String getMotd();

    void reloadConfig();

    String getPlayerCountMessage();

    String getPlayerCountHoverMessage();

    boolean reloadServerIcon();

    boolean hasCustomPlayerCount();

    boolean hasCustomPlayerCountHoverMessage();

    boolean showPlayerCount();

    String getServerIconPath();

    boolean updateChecksEnabled();
}
