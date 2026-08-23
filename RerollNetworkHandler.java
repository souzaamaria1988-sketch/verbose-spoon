package com.example.villagerroll.network;

import com.example.villagerroll.handler.RerollButtonHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class RerollNetworkHandler {

    private final int villagerId;

    public RerollNetworkHandler(int villagerId) {
        this.villagerId = villagerId;
    }

    public RerollNetworkHandler(FriendlyByteBuf buf) {
        this.villagerId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(villagerId);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            var entity = player.level().getEntity(villagerId);
            if (entity instanceof Villager villager) {
                double distance = player.distanceTo(villager);
                if (distance <= 8.0) {
                    RerollButtonHandler.performReroll(villager, player);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
