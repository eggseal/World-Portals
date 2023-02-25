package co.wholesome_seal.worldportals.structure.types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import co.wholesome_seal.worldportals.structure.DataStorage;

@SerializableAs("TeleportArea")
public class TeleportArea implements ConfigurationSerializable {
    public String name;
    public int x;
    public int z;

    public TeleportArea(String name, Location location) {
        this.name = name;
        this.x = (int) location.getX();
        this.z = (int) location.getZ();
    }

    public TeleportArea(String name, int x, int z) {
        this.name = name;
        this.x = x;
        this.z = z;
    }

    public boolean addToConfig() {
        ArrayList<TeleportArea> tpAreas = getTPAreas();

        boolean exists = tpAreas.stream().anyMatch((TeleportArea tpArea) -> tpArea.name.equalsIgnoreCase(this.name));
        if (exists) return false;

        tpAreas.add(this);
        setTPAreas(tpAreas);
        return true;
    }

    public boolean removeFromConfig() {
        ArrayList<TeleportArea> tpAreas = getTPAreas();

        tpAreas.removeIf((TeleportArea area) -> area.name.equalsIgnoreCase(name));
        setTPAreas(tpAreas);
        return true;
    }

    public static TeleportArea get(Predicate<TeleportArea> predicate) {
        ArrayList<TeleportArea> tpAreas = getTPAreas();

        return tpAreas.stream()
        .filter(predicate)
        .findFirst()
        .orElse(null);
    }

    public static ArrayList<TeleportArea> getTPAreas() {
        DataStorage portalStorage = DataStorage.storages.get("portals");
        @SuppressWarnings("unchecked")
        ArrayList<TeleportArea> tpAreas = (ArrayList<TeleportArea>) portalStorage.config.get("tp-areas");

        return tpAreas == null ? new ArrayList<TeleportArea>() : tpAreas;
    }

    private static void setTPAreas(ArrayList<TeleportArea> tpAreas) {
        DataStorage portalStorage = DataStorage.storages.get("portals");
        portalStorage.config.set("tp-areas", tpAreas);
        portalStorage.save();
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put("name", name);
        result.put("x", x);
        result.put("z", z);

        return result;
    }

    public static TeleportArea deserialize(Map<String, Object> args) {
        if (!(args.containsKey("name") && args.containsKey("x") && args.containsKey("z"))) {
            return null;
        }
        
        String name = (String) args.get("name");
        int x = (int) args.get("x");
        int z = (int) args.get("z");

        return new TeleportArea(name, x, z);
    }
    
}
