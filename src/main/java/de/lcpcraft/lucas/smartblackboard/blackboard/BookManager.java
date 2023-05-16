package de.lcpcraft.lucas.smartblackboard.blackboard;

import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;
import de.lcpcraft.lucas.smartblackboard.utils.Message;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BookManager {
    public static Book getBook(Player player, List<Post> posts) {
        Book.Builder builder = Book.builder();
        builder.title(Component.text("Blackboard"));
        builder.author(Component.text("SmartBlackboard"));
        Component header = Component.text("§l§nBlackboard\n§7" + posts.size() + " posts\n\n");
        header = header.append(Component.text(Message.clickToCreate)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard create")));
        builder.addPage(header);
        for (Post post : posts)
            builder.addPage(post.construct(post.getAuthor().equals(player.getUniqueId()) || player.hasPermission("blackboard.admin")));
        return builder.build();
    }

    public static void openWritableBook(Player player) {
        openWritableBook(player, null);
    }

    public static void openWritableBook(Player player, Post post) {
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (post != null) {
            meta.addPages(Component.text(post.getDescription()));
            meta.getPersistentDataContainer().set(new NamespacedKey(SmartBlackboard.plugin, "blackboard"),
                    PersistentDataType.STRING, String.valueOf(post.getTimestamp()));
            meta.displayName(Component.text("Blackboard - Edit: " + post.getTitle()));
        } else {
            meta.addPages(Component.text("Replace this text with your post description and sign the book with your title to create a new post."));
            meta.getPersistentDataContainer().set(new NamespacedKey(SmartBlackboard.plugin, "blackboard"), PersistentDataType.STRING, "create");
            meta.displayName(Component.text("Blackboard - Create"));
        }
        book.setItemMeta(meta);
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
            player.getInventory().setItemInMainHand(book);
        else if (player.getInventory().getItemInOffHand().getType() == Material.AIR)
            player.getInventory().setItemInOffHand(book);
        else {
            player.getInventory().addItem(book);
            player.sendMessage(Message.prefix + Message.noFreeHand);
        }
    }
}
