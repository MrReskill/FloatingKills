package com.floatingkills.listeners;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import com.floatingkills.Loader;

public class PluginListeners implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        if(e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
            Entity damager = ev.getDamager();
            if(damager instanceof Player)
            {
                if(Loader.killsConfig.exists(damager.getName()))
                {
                    int kill = Loader.killsConfig.getInt(damager.getName()) + 1;
                    Loader.killsConfig.set(damager.getName(), kill);
                } else {
                    Loader.killsConfig.set(damager.getName(), 1);
                }
                Loader.killsConfig.save();
            }
        }
    }

}
