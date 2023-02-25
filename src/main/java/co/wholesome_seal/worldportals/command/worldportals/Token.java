package co.wholesome_seal.worldportals.command.worldportals;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import co.wholesome_seal.worldportals.Plugin;
import co.wholesome_seal.worldportals.structure.DataStorage;
import co.wholesome_seal.worldportals.structure.SubCommand;
import co.wholesome_seal.worldportals.util.PrivateMessage;

public class Token extends SubCommand {
    @SuppressWarnings("unused")
    private Plugin plugin;

    public Token(Plugin plugin) {
        super("token", "Manage the tokens", new ArrayList<>());
        this.plugin = plugin;
        args.add(Arrays.asList("set", "remove"));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            PrivateMessage.sendError(sender, "Missing Arguments: /worldportals token <set|remove>");
            return false;
        }

        DataStorage storages = DataStorage.storages.get("portals");
        String action = args[1];

        switch (action) {
            case "set":
                if (!(sender instanceof Player)) {
                    PrivateMessage.sendError(sender, "Missing access: Player only command");
                    return false;
                }
                ItemStack heldItem = ((Player) sender).getInventory().getItemInMainHand();
                if (heldItem == null || heldItem.getType().isAir()) {
                    PrivateMessage.sendError(sender, "An item must be held to set it as a token");
                    return false;
                }

                ItemStack heldToken = heldItem.clone();
                heldToken.setAmount(1);
                PrivateMessage.sendSuccess(
                    sender, 
                    "Set the token: " + heldToken.getType().getKey().getKey() + heldToken.getItemMeta().getAsString()
                );
                storages.config.set("token-item", heldToken);
                storages.save();
                break;
            case "remove":
                storages.config.set("token-item", null);
                storages.save();
                PrivateMessage.sendPrivate(sender, "Removed the token item");
                break;
            default:
                PrivateMessage.sendError(sender, "Invalid argument: <set|remove>");
                return false;
        }
        return true;
    }

    @Override
    public void updateArgs(CommandSender sender) {
        args.clear();
        args.add(Arrays.asList("set", "remove"));
    }
}
