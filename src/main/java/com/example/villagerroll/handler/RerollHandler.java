package com.example.villagerroll.handler;

import com.example.villagerroll.network.RerollPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.network.chat.Component;

public class RerollHandler {
    public static void handleReroll(RerollPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        var entity = player.level().getEntity(payload.villagerId());
        
        if (entity instanceof Villager villager && player.distanceTo(villager) <= 8.0) {
            VillagerData data = villager.getVillagerData();
            var profession = data.getProfession();
            int level = data.getLevel();

            villager.resetOffers();
            villager.restock();
            villager.setVillagerData(new VillagerData(data.getType(), profession, level));
            villager.setLastRestockGameTime(villager.level().getGameTime());
            
            player.sendSystemMessage(Component.literal("§aOfertas atualizadas (Profissão e Nível mantidos)."));
        }
    }
}
