package de.lcpcraft.lucas.smartblackboard.utils;

import de.lcpcraft.lucas.smartblackboard.SmartBlackboard;

import java.io.IOException;

public class Message {

    public static String prefix;


    public static void load() {
        prefix = SmartBlackboard.config.getString("prefix", "§1[§9SmartBlackboard§1] §r");

        if (!SmartBlackboard.config.isSet("prefix"))
            SmartBlackboard.config.set("prefix", prefix);
        try {
            SmartBlackboard.config.save(SmartBlackboard.configFile);
        } catch (IOException ignored) {
        }
    }
}
