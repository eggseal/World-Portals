package co.wholesome_seal.worldportals;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import co.wholesome_seal.worldportals.command.CommandManager;
import co.wholesome_seal.worldportals.command.worldportals.*;
import co.wholesome_seal.worldportals.structure.DataStorage;
import co.wholesome_seal.worldportals.structure.types.TeleportArea;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        ConfigurationSerialization.registerClass(TeleportArea.class, "TeleportArea");
        new DataStorage(this, "portals");

        //  SUBCOMMANDS
        new CommandManager(this, "worldportals", new ArrayList<>(Arrays.asList(
            new Send(this),
            new TPArea(this),
            new Token(this)
        )));
    }
}
