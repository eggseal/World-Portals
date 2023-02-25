# World-Portals

A spigot plugin that allows you to teleport to customizable areas of your world

## Dependencies

While this plugins does not directly depend on another plugin, it was intended to be used by custom portal plugins such as [AdvancedPortals](https://www.spigotmc.org/resources/advanced-portals.14356/)

## Commands

- `/worldportals` All the needed functionalities are grouped as subcommands of this command
  - `send <player> <tp_point>` Teleports the player to the specified teleport point if the player has at least as many tokens as the cost, the exact position of the teleport depends on the rtp-radius
  - `token <set|remove>`
    - On `set` sets the currently held item with all its NBT data as the token item. Note that any change on a token's NBT will make it invalid.
    - On `remove` removes the item set as a token
  - `tparea <add|remove> <name>`
    - On `add` creates a tp_point at the player's current location with the given name. A teleport point's name must be unique.
    - On `remove` removes the tp_point assigned with the given name

## Config

| Key          | Type       | Default | Description                                                           |
| ------------ | ---------- | ------- | --------------------------------------------------------------------- |
| `rtp-radius` | number/int | `0`     | The radius around the teleport point in which a player can be sent to |
| `cost`       | number/int | `1`     | The amount of tokens required to activate the teleport                |
