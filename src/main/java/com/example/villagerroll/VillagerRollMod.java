package com.example.villagerroll;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.example.villagerroll.network.RerollPayload;
import com.example.villagerroll.handler.RerollHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerRollMod implements ModInitializer {
    public static final String MOD_ID = "villagerroll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(RerollPayload.ID, RerollPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RerollPayload.ID, RerollHandler::handleReroll);
        LOGGER.info("Villager Reroll Mod v1.1.0 inicializado (Fabric 1.21.1)");
    }
}
