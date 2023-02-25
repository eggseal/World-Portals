package co.wholesome_seal.worldportals.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PrivateMessage {
    private static final String name = "[WorldPortals] ";

    private static boolean argsNull(CommandSender sender, String message) {
        return sender == null || message == null;
    }

    public static void sendError(CommandSender sender, String message) {
        if (argsNull(sender, message)) return;
        
        String errorMessage = ChatColor.GREEN + name + ChatColor.RED + message;
        sender.sendMessage(errorMessage);
    }

    public static void sendPrivate(CommandSender sender, String message) {
        if (argsNull(sender, message)) return;

        String privateMessage = ChatColor.GREEN + name + ChatColor.GRAY + message;
        sender.sendMessage(privateMessage);
    }

    public static void sendSuccess(CommandSender sender, String message) {
        if (argsNull(sender, message)) return;

        String succMessage = ChatColor.GREEN + name + ChatColor.AQUA + message;
        sender.sendMessage(succMessage);
    }
}
