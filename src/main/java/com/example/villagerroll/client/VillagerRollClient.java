package com.example.villagerroll.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.example.villagerroll.network.RerollPayload;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;

public class VillagerRollClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof MerchantScreen merchantScreen) {
                Button rerollBtn = Button.builder(
                    Component.literal("↻ Reroll"),
                    button -> {
                        if (merchantScreen.getMenu().getMerchant() instanceof Villager villager) {
                            ClientPlayNetworking.send(new RerollPayload(villager.getId()));
                        }
                    }
                ).bounds(merchantScreen.width / 2 + 80, merchantScreen.height / 2 - 90, 20, 20).build();
                
                ScreenEvents.afterRender(screen).register((s, graphics, mouseX, mouseY, tickDelta) -> {
                    if (rerollBtn.isHoveredOrFocused()) {
                        graphics.renderTooltip(client.font, Component.literal("Atualiza ofertas sem resetar nível/profissão"), mouseX, mouseY);
                    }
                });
                
                screen.addRenderableWidget(rerollBtn);
            }
        });
    }
}
