package co.wholesome_seal.worldportals.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import co.wholesome_seal.worldportals.Plugin;
import co.wholesome_seal.worldportals.structure.SubCommand;

public class CommandCompleter implements TabCompleter {
    CommandManager manager;
    Plugin plugin;

    public CommandCompleter(CommandManager manager, Plugin plugin) {
        this.manager = manager;
        this.plugin = plugin;

        plugin.getCommand(manager.name).setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int writingIndex = args.length - 2;

        switch (args.length) {
            case 0:
                break;
            case 1:
                return manager.subCommands.keySet().parallelStream().toList();
            default:
                SubCommand subCommand = manager.subCommands.get(args[0]);
                if (subCommand == null) break;

                subCommand.updateArgs(sender);

                if (writingIndex >= subCommand.args.size()) break;
                return subCommand.args.get(writingIndex);
        }
        return new ArrayList<String>();
    }
    
}
