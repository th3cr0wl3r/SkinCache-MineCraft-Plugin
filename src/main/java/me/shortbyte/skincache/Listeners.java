package me.shortbyte.skincache;

import me.shortbyte.skincache.misc.ItemBuilder;
import me.shortbyte.skincache.misc.Skin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class Listeners implements Listener {

    private final SkinCache plugin;

    public Listeners(SkinCache plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        Skin skin = plugin.getSkin(player.getUniqueId().toString());
        player.sendMessage("Name: " + skin.getName());
        player.sendMessage("Value: " + skin.getValue());
        player.sendMessage("Signature: " + skin.getSignature());
        
        player.getInventory().addItem(ItemBuilder.getItem(Material.SKULL_ITEM, 1, (short) 3)
                .setDisplayName("&a&l" + player.getName()).setSkullOwner(skin).build());
    }
}
