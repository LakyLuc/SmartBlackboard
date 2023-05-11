package de.lcpcraft.lucas.smartblackboard.listeners;

import de.lcpcraft.lucas.smartblackboard.utils.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("smartblackboard.update"))
            Updater.sendUpdateMessage(e.getPlayer());
    }
}
