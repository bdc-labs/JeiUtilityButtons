package io.bdc_labs.mc.jeiutilitybuttons.core.handlers;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MagnetModeHandler {

    public static boolean state = false;
    private List<PlayerEntity> players = new ArrayList<PlayerEntity>();
    private int r;

    public MagnetModeHandler() {
        r = ConfigHandler.COMMON.magnetRadius.get();
    }

    public void addPlayer(PlayerEntity p) {
        players.add(p);
    }

    public void removePlayer(PlayerEntity p) {
        players.remove(p);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (System.currentTimeMillis() % 5 == 0 && players.size() > 0) {
            for (PlayerEntity p : players) {
                double x = p.getX();
                double y = p.getY() + 1.5;
                double z = p.getZ();

                List<ItemEntity> items = p.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r));

                int pulled = 0;

                for (ItemEntity i : items) {
                    i.setPos(x, y, z);

                    if(pulled > 200)
                        break;
                    pulled++;
                }
            }
        }
    }
}
