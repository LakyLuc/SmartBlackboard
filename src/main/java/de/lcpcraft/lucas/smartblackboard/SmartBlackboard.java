package de.lcpcraft.lucas.smartblackboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.lcpcraft.lucas.smartblackboard.blackboard.BoardManager;
import de.lcpcraft.lucas.smartblackboard.blackboard.Post;
import de.lcpcraft.lucas.smartblackboard.commands.BlackboardCommand;
import de.lcpcraft.lucas.smartblackboard.listeners.BookListener;
import de.lcpcraft.lucas.smartblackboard.listeners.JoinListener;
import de.lcpcraft.lucas.smartblackboard.utils.Message;
import de.lcpcraft.lucas.smartblackboard.utils.Metrics;
import de.lcpcraft.lucas.smartblackboard.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SmartBlackboard extends JavaPlugin {

    public static final String MODRINTH_ID = "hNkktef2";
    public static SmartBlackboard plugin;
    public static File configFile;
    public static File postsFile;
    public static YamlConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this, 18443);

        File pluginFolder = new File(plugin.getDataFolder().getParentFile(), "SmartBlackboard");
        if (!pluginFolder.exists())
            pluginFolder.mkdir();
        configFile = new File(pluginFolder, "config.yml");
        postsFile = new File(pluginFolder, "posts.yml");
        if (!postsFile.exists()) {
            try {
                postsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("update_channel")) {
            config.addDefault("update_channel", "release");
            config.options().header("Configuration file of SmartBlackboard by LakyLuc").copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        }
        Message.load();
        Updater.checkForUpdates();

        try {
            String json = Files.readString(postsFile.toPath());
            if (json.length() > 0) {
                List<Post> posts = new Gson().fromJson(json, new TypeToken<ArrayList<Post>>() {
                }.getType());
                if (posts != null && posts.size() > 0)
                    BoardManager.loadPosts(posts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new BookListener(), this);
        Objects.requireNonNull(getCommand("blackboard")).setExecutor(new BlackboardCommand());
    }

    @Override
    public void onDisable() {
    }

    public static String updateChannel() {
        String channel = config.getString("update_channel", "release");
        if (channel.equals("release") || channel.equals("beta") || channel.equals("alpha"))
            return channel;
        return "release";
    }
}
