package de.lcpcraft.lucas.smartblackboard.utils;

import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;

import java.io.IOException;

public class Message {

    public static String prefix;
    public static String postCreated;
    public static String postEdited;
    public static String clickToOpen;
    public static String clickToCreate;
    public static String noFreeHand;
    public static String error;
    public static String postDeleted;
    public static String signToCreate;
    public static String signToEdit;
    public static String noEdit;


    public static void load() {
        if (!SmartBlackboard.config.isSet("prefix"))
            SmartBlackboard.config.set("prefix", prefix);
        if (!SmartBlackboard.config.isSet("postCreated"))
            SmartBlackboard.config.set("postCreated", "§a%player% created a new post on the blackboard");
        if (!SmartBlackboard.config.isSet("postEdited"))
            SmartBlackboard.config.set("postEdited", "§a%player% edited a post on the blackboard");
        if (!SmartBlackboard.config.isSet("clickToOpen"))
            SmartBlackboard.config.set("clickToOpen", "§7[§eOpen blackboard§7]");
        if (!SmartBlackboard.config.isSet("clickToCreate"))
            SmartBlackboard.config.set("clickToCreate", "§8[Click here to create a new post]");
        if (!SmartBlackboard.config.isSet("noFreeHand"))
            SmartBlackboard.config.set("noFreeHand", "§cYou have no free hand to hold the book. It has been added to your inventory.");
        if (!SmartBlackboard.config.isSet("error"))
            SmartBlackboard.config.set("error", "§cAn error occurred");
        if (!SmartBlackboard.config.isSet("postDeleted"))
            SmartBlackboard.config.set("postDeleted", "§aSuccessfully deleted post");
        if (!SmartBlackboard.config.isSet("signToCreate"))
            SmartBlackboard.config.set("signToCreate", "§cYou have to sign the book with your title to create a new post.");
        if (!SmartBlackboard.config.isSet("signToEdit"))
            SmartBlackboard.config.set("signToEdit", "§cYou have to sign the book to edit the post.");
        if (!SmartBlackboard.config.isSet("noEdit"))
            SmartBlackboard.config.set("noEdit", "§cYou can't edit this post.");
        try {
            SmartBlackboard.config.save(SmartBlackboard.configFile);
        } catch (IOException ignored) {
        }

        prefix = SmartBlackboard.config.getString("prefix", "§1[§9SmartBlackboard§1] §r");
        postCreated = SmartBlackboard.config.getString("postCreated", "§a%player% created a new post on the blackboard");
        postEdited = SmartBlackboard.config.getString("postEdited", "§a%player% edited a post on the blackboard");
        clickToOpen = SmartBlackboard.config.getString("clickToOpen", "§7[§eOpen blackboard§7]");
        clickToCreate = SmartBlackboard.config.getString("clickToCreate", "§8[Click here to create a new post]");
        noFreeHand = SmartBlackboard.config.getString("noFreeHand", "§cYou have no free hand to hold the book. It has been added to your inventory.");
        error = SmartBlackboard.config.getString("error", "§cAn error occurred");
        postDeleted = SmartBlackboard.config.getString("postDeleted", "§aSuccessfully deleted post");
        signToCreate = SmartBlackboard.config.getString("signToCreate", "§cYou have to sign the book with your title to create a new post.");
        signToEdit = SmartBlackboard.config.getString("signToEdit", "§cYou have to sign the book to edit the post.");
        noEdit = SmartBlackboard.config.getString("noEdit", "§cYou can't edit this post.");
    }
}
