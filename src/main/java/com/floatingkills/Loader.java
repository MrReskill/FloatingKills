package com.floatingkills;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.floatingkills.listeners.PluginListeners;
import com.floatingkills.scheduler.ParticleScheduler;

public class Loader extends PluginBase {

    public static Loader getLoader;
    public static Config pluginConfig;
    public static Config killsConfig;

    @Override
    public void onEnable() {
        getLoader = this;
        pluginConfig = new Config(getDataFolder() + "/config.yml", Config.YAML);
        killsConfig = new Config(getDataFolder() + "/kills.yml", Config.YAML);
        initConfig();
        registerListeners();
        initScheduler();
    }

    private void initScheduler() {
        getServer().getScheduler().scheduleRepeatingTask(new ParticleScheduler(this), 60);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PluginListeners(), this);
    }

    private void initConfig()
    {
        if(!pluginConfig.exists("version"))
        {
            pluginConfig.set("version", 1.0);
            pluginConfig.set("MaxPlayerInScoreboard", 10);
            pluginConfig.set("floatingLocation.x", 0);
            pluginConfig.set("floatingLocation.y", 0);
            pluginConfig.set("floatingLocation.z", 0);
            pluginConfig.set("floatingLocation.world", Server.getInstance().getDefaultLevel().getName());
            pluginConfig.save();
        }
    }

    public static Loader getLoader()
    {
        return getLoader;
    }
}
