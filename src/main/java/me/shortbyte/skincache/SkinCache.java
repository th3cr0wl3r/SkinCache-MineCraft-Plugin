package me.shortbyte.skincache;

import me.shortbyte.skincache.managers.RedisManager;
import me.shortbyte.skincache.misc.Skin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class SkinCache extends JavaPlugin {

    private RedisManager redisManager;

    private Listeners listeners;
    
    @Override
    public void onEnable() {
        init();
    }

    private void init() {
        this.redisManager = new RedisManager(this);
        this.listeners = new Listeners(this);
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public Skin getSkin(String uuid) {
        return new Skin(this, uuid);
    }
}
