package de.lcpcraft.lucas.smartblackboard.blackboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Post {
    private final String title;
    private final UUID author;
    private final String description;
    private final long timestamp;

    public Post(String title, UUID author, String description, long timestamp) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.timestamp = timestamp;
    }

    public Component construct(boolean owner) {
        String authorName = Bukkit.getOfflinePlayer(author).getName();
        Component delete = Component.text("§7[§cDelete§7]")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard delete " + timestamp));
        return Component.text(title + " ", NamedTextColor.GOLD)
                .append(Component.text("(" + authorName + " at " + formatTime() + ") ", NamedTextColor.GRAY))
                .append(owner ? delete : Component.empty())
                .append(Component.text("\n" + description, NamedTextColor.BLACK));
    }

    private String formatTime() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(timestamp));
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getAuthor() {
        return author;
    }
}
