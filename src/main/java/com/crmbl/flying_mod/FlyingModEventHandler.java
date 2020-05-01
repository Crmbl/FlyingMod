package com.crmbl.flying_mod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class FlyingModEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START /*&& event.side == LogicalSide.SERVER*/) {
            event.player.abilities.allowFlying = event.player.inventory.armorInventory.get(2).getItem() instanceof FlyingModItem;
            if (!event.player.abilities.allowFlying)
                event.player.abilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) event.getEntity());
            if (player != null) {
                ItemStack itemStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                if (itemStack.getItem() instanceof FlyingModItem)
                    itemStack.damageItem(1, player, playerEntity -> {});
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeadEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) event.getEntity());
            if (player != null) {
                ItemStack itemStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                if (itemStack.getItem() instanceof FlyingModItem)
                    itemStack.damageItem(itemStack.getMaxDamage(), player, playerEntity -> {});
            }
        }
    }
}
