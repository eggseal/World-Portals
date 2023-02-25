package co.wholesome_seal.worldportals.command.worldportals;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.wholesome_seal.worldportals.Plugin;
import co.wholesome_seal.worldportals.structure.SubCommand;
import co.wholesome_seal.worldportals.structure.types.TeleportArea;
import co.wholesome_seal.worldportals.util.PrivateMessage;

public class TPArea extends SubCommand {
    @SuppressWarnings("unused")
    private Plugin plugin;

    public TPArea(Plugin plugin) {
        super("tparea", "Manage the teleport areas for portals", new ArrayList<>());
        this.plugin = plugin;
        args.add(Arrays.asList("add", "remove"));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            PrivateMessage.sendError(sender, "Missing arguments: /worldportals tparea <add|remove> <area_name>");
            return false;
        }
        String action = args[1];
        switch (action) {
            case "add": 
                return addTPArea(sender, args);
            case "remove": 
                return removeTPArea(sender, args);
            default: {
                PrivateMessage.sendError(sender, "Invalid action");
                return false;
            }
        }
    }

    @Override
    public void updateArgs(CommandSender sender) {
        args.clear();
        args.add(Arrays.asList("add", "remove"));
        
        ArrayList<TeleportArea> tpAreas = TeleportArea.getTPAreas();
        ArrayList<String> areaNames = new ArrayList<>(tpAreas.stream().map((TeleportArea area) -> area.name).toList());
        args.add(areaNames);
    }

    private boolean addTPArea(CommandSender sender, String[] args) {
        String x;
        String z;
        if (args.length < 5) {
            if (!(sender instanceof Player)) {
                PrivateMessage.sendError(sender, "Only players can use the command without a coordinate input");
                return false;
            }
            Player player = (Player) sender;
            Location playerPos = player.getLocation();
            x = String.valueOf(playerPos.getBlockX());
            z = String.valueOf(playerPos.getBlockZ());
        } else {
            x = args[3];
            z = args[4];
        }

        boolean validX = x.matches("-?\\d+");
        boolean validZ = z.matches("-?\\d+");
        if (!(validX && validZ)) {
            PrivateMessage.sendError(sender, "Invalid coordinates were provided: Accepted values are Integers.");
            return false;
        }

        TeleportArea tpArea = new TeleportArea(args[2], Integer.parseInt(x), Integer.parseInt(z));
        boolean success = tpArea.addToConfig();
        if (success) {
            PrivateMessage.sendSuccess(
                sender, 
                "The teleport area [" + tpArea.name.toUpperCase() + "] has been added and set to (" + tpArea.x + " ~ " + tpArea.z + ")"
            );
            return true;
        } else {
            PrivateMessage.sendError(
                sender, 
                "A teleport area with this name already exists, if you wish to change it, delete it first and then create a new one"
            );
            return false;
        }
    }

    private boolean removeTPArea(CommandSender sender, String[] args) {
        String name = args[2];
        TeleportArea tpArea = TeleportArea.get((TeleportArea area) -> area.name.equalsIgnoreCase(name));
        if (tpArea == null) {
            PrivateMessage.sendError(sender, "There is no teleport area with this name.");
            return false;
        } 

        tpArea.removeFromConfig();
        PrivateMessage.sendSuccess(sender, "The teleport area [" + tpArea.name.toUpperCase() + "] has been removed");
        return true;
    }
}
