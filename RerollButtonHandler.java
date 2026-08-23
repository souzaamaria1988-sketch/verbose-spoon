package com.example.villagerroll.handler;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "villagerroll")
public class RerollButtonHandler {

    @SubscribeEvent
    public static void onVillagerTradeScreenOpen(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Villager villager)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Este método é chamado quando a interação ocorre. 
        // A lógica de injeção do botão é feita via Mixin na classe MerchantMenu/AbstractContainerScreen.
        // Aqui garantimos apenas que o servidor reconhece a entidade como válida para o mod.
    }

    /**
     * Executa o reroll mantendo profissão e nível.
     * Chamado pelo pacote de rede personalizado quando o botão é clicado no cliente.
     */
    public static void performReroll(Villager villager, ServerPlayer player) {
        VillagerData data = villager.getVillagerData();
        
        // Preserva profissão e nível explicitamente
        var profession = data.getProfession();
        int level = data.getLevel();

        // Limpa ofertas atuais e regenera sem alterar metadados de carreira
        villager.resetOffers();
        villager.restock();

        // Garante que os dados não foram corrompidos durante o reset
        villager.setVillagerData(new VillagerData(
            data.getType(),
            profession,
            level
        ));

        villager.setLastRestockGameTime(villager.level().getGameTime());
        player.sendSystemMessage(Component.literal("§aOfertas atualizadas (Profissão e Nível mantidos)."));
    }
}
