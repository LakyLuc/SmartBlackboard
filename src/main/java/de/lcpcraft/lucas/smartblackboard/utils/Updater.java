package de.lcpcraft.lucas.smartblackboard.utils;

import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;
import de.lcpcraft.lucas.smartblackboard.utils.modrinth.ProjectVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

public class Updater {
    private static final String URL = "https://api.modrinth.com/v2/";
    public static ProjectVersion latestVersion;

    public static void checkForUpdates() {
        String gameVersion = Bukkit.getMinecraftVersion();
        ProjectVersion latestVersion = getLatestVersion(gameVersion, SmartBlackboard.updateChannel());
        if (latestVersion == null) {
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§cFailed to check for updates!");
            return;
        }
        String pluginVersion = Bukkit.getPluginManager().getPlugin("SmartBlackboard").getPluginMeta().getVersion();
        if (!latestVersion.version_number.equals(pluginVersion)) {
            Updater.latestVersion = latestVersion;
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aA new version of SmartBlackboard is available: §e" + latestVersion.version_number);
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aDownload it at §e"
                    + SmartBlackboard.MODRINTH_LINK.replace("%version%", latestVersion.version_number));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("smartblackboard.update"))
                    sendUpdateMessage(onlinePlayer);
            }
        }
    }

    public static void sendUpdateMessage(Player player) {
        if (latestVersion != null) {
            Component link = Component.text("Modrinth").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,
                            SmartBlackboard.MODRINTH_LINK.replace("%version%", latestVersion.version_number)))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§7Änderungsliste:\n" + latestVersion.changelog)));
            Component message = Component.text(Message.prefix + "§eEine neue Version von SmartBlackboard ist verfügbar ("
                    + latestVersion.version_number + "). Download auf ").append(link).append(Component.text("§e verfügbar."));
            player.sendMessage(message);
        }
    }

    private static ProjectVersion getLatestVersion(String gameVersion, String versionType) {
        RequestBuilder request = new RequestBuilder(URL + "project/" + SmartBlackboard.MODRINTH_ID
                + "/version?loaders=[%22paper%22]&game_versions=[%22" + gameVersion + "%22]");
        HttpsURLConnection connection = request.execute();
        try {
            if (connection.getResponseCode() == 200) {
                ProjectVersion[] versions = request.projectVersions();
                if (versions != null) {
                    for (ProjectVersion version : versions) {
                        if (versionType.equals("alpha"))
                            return version;
                        else if (version.version_type.equals(versionType))
                            return version;
                        else if (versionType.equals("beta") && version.version_type.equals("release"))
                            return version;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
