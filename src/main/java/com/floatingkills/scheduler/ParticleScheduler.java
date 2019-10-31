package com.floatingkills.scheduler;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import com.floatingkills.Loader;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ParticleScheduler extends PluginTask {

    public ParticleScheduler(Plugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        loadScoreBoard();
    }

    private void loadScoreBoard()
    {
        float x = Loader.pluginConfig.getInt("floatingLocation.x");
        float y = Loader.pluginConfig.getInt("floatingLocation.y");
        float z = Loader.pluginConfig.getInt("floatingLocation.z");
        Map<String, Integer> counter = calculateScore();
        int maxScore = Loader.pluginConfig.getInt("MaxPlayerInScoreboard");
        String title = TextFormat.GOLD + "" + TextFormat.BOLD + "TOP "+maxScore+" KILLS" + TextFormat.RESET;
        this.createAndSendPacket(title, 1000, x, y, z);
        int place = 1;
        for(Map.Entry<String, Integer> entry : counter.entrySet()) {
            if(place <= maxScore)
            {
                title = "§6(§f#" + place + "§6) §e" + entry.getKey() + "§6 with §e"+entry.getValue()+" kills";
                this.createAndSendPacket(title, 1000 + place, x, y - place *0.4f, z);
                place++;
            }
        }
    }

    private void createAndSendPacket(String name, long id, float x, float y, float z)
    {
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityRuntimeId = id;
        pk.entityUniqueId = id;
        pk.uuid = UUID.randomUUID();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        long flags = (
                (1L << Entity.DATA_FLAG_IMMOBILE)
        );
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putLong(Entity.DATA_LEAD_HOLDER_EID,-1)
                .putFloat(Entity.DATA_SCALE, 0.0001f);
        pk.item = Item.get(Item.AIR);
        pk.username = name;
        Server.broadcastPacket(Loader.getLoader().getServer().getLevelByName(Loader.pluginConfig.getString("floatingLocation.world")).getPlayers().values(), pk);
    }


    private Map<String, Integer> calculateScore() {
        HashMap<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : Loader.killsConfig.getAll().entrySet()) {
            map.put(entry.getKey(), (Integer) entry.getValue());
        }
        Map<String, Integer> sorted = map;
        sorted = map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        return sorted;
    }

}
