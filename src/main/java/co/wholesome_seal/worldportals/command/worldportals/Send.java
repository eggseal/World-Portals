package co.wholesome_seal.worldportals.command.worldportals;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import co.wholesome_seal.worldportals.Plugin;
import co.wholesome_seal.worldportals.structure.DataStorage;
import co.wholesome_seal.worldportals.structure.SubCommand;
import co.wholesome_seal.worldportals.structure.types.TeleportArea;
import co.wholesome_seal.worldportals.util.PrivateMessage;

public class Send extends SubCommand {
    private Plugin plugin;

    public Send(Plugin plugin) {
        super("send", "Send a player to a designated area", new ArrayList<>());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            PrivateMessage.sendError(sender, "Missing arguments: /worldportals send <player> <tp_area>");
            return false;
        }

        String playerName = args[1];
        String area = args[2];
        Player sendingPlayer = plugin.getServer().getPlayer(playerName);
        if (sendingPlayer == null) {
            PrivateMessage.sendError(sender, "Failed to find player: " + playerName);
            return false;
        }

        TeleportArea targetArea = TeleportArea.get((TeleportArea tpArea) -> tpArea.name.equals(area));
        if (targetArea == null) {
            PrivateMessage.sendError(sender, "There is not an area with that name");
            return false;
        }

        Inventory inventory = sendingPlayer.getInventory();
        DataStorage storage = DataStorage.storages.get("portals");

        int tokenPrice = plugin.getConfig().getInt("cost");
        if (tokenPrice < 0) tokenPrice = 0;

        ItemStack storedToken = storage.config.getItemStack("token-item");

        //  CHECK IF ENOUGH TOKENS
        boolean hasEnough = inventory.containsAtLeast(storedToken, tokenPrice);
        if (!hasEnough) {
            PrivateMessage.sendPrivate(sendingPlayer, "You don't have enough tokens with you to activate this portal");
            return false;
        }

        //  TAKE THE NECESSARY PRICE
        int reducedAmount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (reducedAmount >= tokenPrice) break;
            if (!(item != null && item.getItemMeta().equals(storedToken.getItemMeta()))) continue;

            int amountToReduce = Math.min(item.getAmount(), tokenPrice - reducedAmount);
            item.setAmount(item.getAmount() - amountToReduce);
            reducedAmount += item.getAmount();
        }

        int spreadRadius = plugin.getConfig().getInt("rtp-radius");

        int targetX;
        int targetY;
        int targetZ;
        if (spreadRadius > 0) {
            Random random = new Random();
            targetX = random.nextInt(targetArea.x - spreadRadius, targetArea.x + spreadRadius);
            targetZ = random.nextInt(targetArea.z - spreadRadius, targetArea.z + spreadRadius);
        } else {
            targetX = targetArea.x;
            targetZ = targetArea.z;
        }
        targetY = sendingPlayer.getWorld().getHighestBlockYAt(targetX, targetZ) + 1;

        Location destination = new Location(sendingPlayer.getWorld(), targetX, targetY, targetZ);
        sendingPlayer.teleport(destination);

        return true;
    }

    @Override
    public void updateArgs(CommandSender sender) {
        args.clear();

        ArrayList<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        args.add(new ArrayList<>(players.stream().map((Player player) -> player.getName()).toList()));

        ArrayList<TeleportArea> tpAreas = TeleportArea.getTPAreas();
        args.add(new ArrayList<>(tpAreas.stream().map((TeleportArea area) -> area.name).toList()));
    }
}
