package de.lcpcraft.lucas.smartblackboard.listeners;

import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;
import de.lcpcraft.lucas.smartblackboard.blackboard.BoardManager;
import de.lcpcraft.lucas.smartblackboard.utils.Message;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;

public class BookListener implements Listener {
    @EventHandler
    public void onBookEdit(PlayerEditBookEvent e) {
        if (e.getPreviousBookMeta().getPersistentDataContainer().has(new NamespacedKey(SmartBlackboard.plugin, "blackboard"))) {
            if (e.isSigning()) {
                String title = e.getNewBookMeta().getTitle();
                String description = e.getNewBookMeta().getPages().get(0);
                BoardManager.addPost(e.getPlayer(), title, description);
                for (ItemStack content : e.getPlayer().getInventory().getContents()) {
                    if (content != null && content.getItemMeta() != null
                            && content.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(SmartBlackboard.plugin, "blackboard")))
                        content.setAmount(0);
                }
            } else
                e.getPlayer().sendMessage(Message.prefix + "§cYou have to sign the book with your title to create a new post.");
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getItemMeta() != null
                && e.getEntity().getItemStack().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(SmartBlackboard.plugin, "blackboard")))
            e.setCancelled(true);
    }
}
