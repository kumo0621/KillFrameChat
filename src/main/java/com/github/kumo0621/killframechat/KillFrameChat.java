package com.github.kumo0621.killframechat;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class KillFrameChat extends JavaPlugin implements org.bukkit.event.Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    Map<UUID, Location> map = new HashMap<>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Location location = player.getLocation();
        map.put(player.getUniqueId(), location);
    }
    //mapに入れてplayerの値を取ってリスト化して
    //その人とアーマースタンドを連携する。その後コマンドを打つとあとから名前が記入される。
    //問題はコマンドの値をどうやって変数に入れるか…

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("KillFrameChat")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    String chat = args[0];
                    UUID id = ((Player) sender).getUniqueId();
                    Location location = map.get(id);
                    if (location != null) {
                        @NotNull ArmorStand entity = location.getWorld().spawn(location, ArmorStand.class);
                        entity.setGravity(false);
                        entity.setInvisible(true);
                        entity.setInvulnerable(true);
                        entity.setCustomNameVisible(true);
                        entity.setCustomName(chat);
                        map.remove(id);
                        sender.sendMessage("死亡した地点にメッセージを残しました。");
                    } else {
                        sender.sendMessage("死んでません。");
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}