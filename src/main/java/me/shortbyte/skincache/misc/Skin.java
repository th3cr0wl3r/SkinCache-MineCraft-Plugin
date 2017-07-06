package me.shortbyte.skincache.misc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.shortbyte.skincache.SkinCache;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LeonEnkn
 *
 * Copyright (c) 2015 - 2017 by ShortByte.de to present. All rights reserved.
 */
public class Skin {

    private final SkinCache plugin;

    private final String uuid;

    private String name;
    private String value;
    private String signature;

    public Skin(SkinCache plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        load();
    }

    private void load() {
        String skin = plugin.getRedisManager().getSkin(this.uuid);

        if (skin != null && !skin.isEmpty()) {
            try {
                JSONObject json = (JSONObject) new JSONParser().parse(skin);
                this.name = (String) json.get("name");
                this.value = (String) json.get("value");
                this.signature = (String) json.get("signature");
            } catch (ParseException ex) {
                Logger.getLogger(Skin.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            URL url = new URL(MessageFormat.format("https://sessionserver.mojang.com/session/minecraft/profile/{0}?unsigned=false", this.uuid.replaceAll("-", "")));

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("User-Agent", "curl/7.26.0");
            connection.setRequestProperty("Host", "sessionserver.mojang.com");
            connection.setRequestProperty("Accept", "*/*");

            String json = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
            JSONArray array = (JSONArray) ((JSONObject) new JSONParser().parse(json)).get("properties");

            JSONObject properties = (JSONObject) array.get(0);
            this.name = (String) properties.get("name");
            this.value = (String) properties.get("value");
            this.signature = properties.containsKey("signature") ? (String) properties.get("signature") : null;

            plugin.getRedisManager().setSkin(this.uuid, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Skin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Skin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

}
