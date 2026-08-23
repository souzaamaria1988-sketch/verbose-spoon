package com.example.villagerroll.mixin;

import com.example.villagerroll.network.ModNetworkInit;
import com.example.villagerroll.network.RerollNetworkHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends AbstractContainerScreen<?> {

    private Button rerollButton;

    protected MerchantScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addRerollButton(CallbackInfo ci) {
        MerchantScreen screen = (MerchantScreen) (Object) this;
        
        this.rerollButton = Button.builder(
            Component.literal("↻ Reroll"),
            button -> {
                if (screen.getMenu().getMerchant() instanceof Villager villager) {
                    ModNetworkInit.CHANNEL.sendToServer(
                        new RerollNetworkHandler(villager.getId())
                    );
                }
            }
        )
        .bounds(this.leftPos + this.imageWidth - 25, this.topPos + 5, 20, 20)
        .build();

        this.addRenderableWidget(this.rerollButton);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.rerollButton != null && this.rerollButton.isHoveredOrFocused()) {
            graphics.renderTooltip(
                this.font,
                Component.literal("Atualiza ofertas sem resetar nível/profissão"),
                mouseX,
                mouseY
            );
        }
    }
}
