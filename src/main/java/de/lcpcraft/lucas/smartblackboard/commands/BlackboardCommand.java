package de.lcpcraft.lucas.smartblackboard.commands;

import de.lcpcraft.lucas.smartblackboard.blackboard.BoardManager;
import de.lcpcraft.lucas.smartblackboard.blackboard.BookManager;
import de.lcpcraft.lucas.smartblackboard.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BlackboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            if (strings.length == 1 && strings[0].equalsIgnoreCase("create")) {
                BookManager.openWritableBook(p);
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("delete")) {
                try {
                    long index = Long.parseLong(strings[1]);
                    if (BoardManager.deletePost(p, index))
                        p.sendMessage(Component.text(Message.prefix + "§aSuccessfully deleted post ")
                                .append(Component.text("§7[§eOpen blackboard§7]")
                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard"))));
                    else p.sendMessage(Component.text(Message.prefix + "§cError while deleting post ")
                            .append(Component.text("§7[§eOpen blackboard§7]")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard"))));
                } catch (NumberFormatException e) {
                    p.sendMessage(Component.text(Message.prefix + "§cError while deleting post ")
                            .append(Component.text("§7[§eOpen blackboard§7]")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/blackboard"))));
                }
            } else BoardManager.openBoard(p);
            return true;
        }
        return false;
    }
}
