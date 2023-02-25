package co.wholesome_seal.worldportals.command;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.wholesome_seal.worldportals.Plugin;
import co.wholesome_seal.worldportals.structure.SubCommand;
import co.wholesome_seal.worldportals.util.PrivateMessage;

public class CommandManager implements CommandExecutor {
    public final HashMap<String, SubCommand> subCommands = new HashMap<>();
    
    private Plugin plugin;
    public String name;

    public CommandManager(Plugin plugin, String name, ArrayList<SubCommand> subCommands) {
        this.name = name.toLowerCase();
        this.plugin = plugin;
        this.plugin.getCommand(name).setExecutor(this);

        new CommandCompleter(this, plugin);

        subCommands.forEach((SubCommand subCommand) -> {
            this.subCommands.put(subCommand.name, subCommand); 
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            PrivateMessage.sendError(sender, "A subcommand must be provided");
            return false;
        }

        String inputSubCommand = args[0];
        SubCommand subCommand = subCommands.get(inputSubCommand);
        if (subCommand == null) {
            PrivateMessage.sendError(sender, "This subcommand does not exist");
            return false;
        }

        if (!(sender.hasPermission(name + "." + subCommand.name) || sender.hasPermission(name + ".*"))) {
            PrivateMessage.sendError(sender, "Missing Permissions: <" + name + "." + subCommand.name + ">");
            return false;
        }

        return subCommand.execute(sender, args);
    }
}
