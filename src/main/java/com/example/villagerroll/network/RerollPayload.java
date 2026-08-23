package com.example.villagerroll.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import com.example.villagerroll.VillagerRollMod;

public record RerollPayload(int villagerId) implements CustomPacketPayload {
    public static final Type<RerollPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(VillagerRollMod.MOD_ID, "reroll"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, RerollPayload> CODEC = 
        StreamCodec.ofMember(
            (payload, buf) -> buf.writeInt(payload.villagerId()),
            buf -> new RerollPayload(buf.readInt())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
