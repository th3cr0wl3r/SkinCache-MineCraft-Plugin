package me.shortbyte.skincache.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class ItemBuilder {

    private Class<?> skullMetaClass;

    private ItemStack itemStack;

    public static ItemBuilder getItem(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder getItem(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder getItem(Material material, int amount, short damage) {
        return new ItemBuilder(material, amount, damage);
    }
    
    public ItemBuilder(Material material) {
        setClasses();
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        setClasses();
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, short damage) {
        setClasses();
        this.itemStack = new ItemStack(material, amount, damage);
    }
    
    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullOwner(Skin skin) {
        if (skin.getName() != null && skin.getValue() != null)
            if (itemStack.getType() == Material.SKULL_ITEM) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                GameProfile gameProfile = new GameProfile(UUID.fromString(skin.getUuid()), "skin");
                gameProfile.getProperties().clear();
                gameProfile.getProperties().put(skin.getName(), new Property(skin.getName(), skin.getValue(), skin.getSignature()));

                try {
                    Field field = skullMetaClass.getDeclaredField("profile");
                    field.setAccessible(true);
                    field.set(skullMeta, gameProfile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(ItemBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                itemStack.setItemMeta(skullMeta);
            }
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

    private void setClasses() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");
        } catch (ClassNotFoundException e) {
        }
    }
}
