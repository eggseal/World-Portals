package co.wholesome_seal.worldportals.structure;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public String name;
    public String description;
    public List<List<String>> args;

    public SubCommand(String name, String description, List<List<String>> args) {
        this.name = name;
        this.description = description;
        this.args = args;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public abstract void updateArgs(CommandSender sender);
}