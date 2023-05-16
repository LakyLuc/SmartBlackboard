package de.lcpcraft.lucas.smartblackboard.blackboard;

import com.google.gson.Gson;
import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;
import de.lcpcraft.lucas.smartblackboard.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BoardManager {
    private static final List<Post> posts = new ArrayList<>();

    public static void openBoard(Player player) {
        player.openBook(BookManager.getBook(player, posts));
    }

    public static void addPost(Player player, String title, String description) {
        Post post = new Post(title, player.getUniqueId(), description, System.currentTimeMillis());
        posts.add(post);
        posts.sort(Comparator.comparingLong(Post::getTimestamp));
        Collections.reverse(posts);
        String json = new Gson().toJson(posts);
        Bukkit.getScheduler().runTaskAsynchronously(SmartBlackboard.plugin, () -> {
            try {
                Files.writeString(SmartBlackboard.postsFile.toPath(), json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Bukkit.getOnlinePlayers().forEach(p ->
                p.sendMessage(Component.text(Message.prefix + Message.postCreated.replace("%player%", player.getName() + " "))
                        .append(Component.text(Message.clickToOpen)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard")))));
    }

    public static boolean deletePost(Player p, long index) {
        if (posts.removeIf(post -> post.getTimestamp() == index
                && (post.getAuthor().toString().equals(p.getUniqueId().toString()) || p.hasPermission("blackboard.admin")))) {
            String json = new Gson().toJson(posts);
            Bukkit.getScheduler().runTaskAsynchronously(SmartBlackboard.plugin, () -> {
                try {
                    Files.writeString(SmartBlackboard.postsFile.toPath(), json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        return false;
    }

    public static boolean editPost(Player p, long index) {
        for (Post post : posts) {
            if (post.getTimestamp() == index
                    && (post.getAuthor().toString().equals(p.getUniqueId().toString()) || p.hasPermission("blackboard.admin"))) {
                BookManager.openWritableBook(p, post);
                return true;
            }
        }
        return false;
    }

    public static boolean editPost(Player player, long index, String description) {
        for (Post post : posts) {
            if (post.getTimestamp() == index
                    && (post.getAuthor().toString().equals(player.getUniqueId().toString()) || player.hasPermission("blackboard.admin"))) {
                post.setDescription(description);
                String json = new Gson().toJson(posts);
                Bukkit.getScheduler().runTaskAsynchronously(SmartBlackboard.plugin, () -> {
                    try {
                        Files.writeString(SmartBlackboard.postsFile.toPath(), json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                Bukkit.getOnlinePlayers().forEach(p ->
                        p.sendMessage(Component.text(Message.prefix + Message.postEdited.replace("%player%", player.getName() + " "))
                                .append(Component.text(Message.clickToOpen)
                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard")))));
                return true;
            }
        }
        return false;
    }

    public static void loadPosts(List<Post> posts) {
        BoardManager.posts.clear();
        BoardManager.posts.addAll(posts);
        BoardManager.posts.sort(Comparator.comparingLong(Post::getTimestamp));
        Collections.reverse(BoardManager.posts);
        Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aLoaded " + posts.size() + " post(s)");
    }
}
