package com.crmbl.flying_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class FlyingModEventHandler {

    //TODO render flying wings to others

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            if (player == null)
                return;

            boolean isInWater = player.isInWater();
            boolean isFlyingModItem = player.inventory.armorInventory.get(2).getItem() instanceof FlyingModItem;
            if (event.side == LogicalSide.SERVER) {
                if (player.abilities.allowFlying && !isFlyingModItem || isInWater) {
                    player.abilities.allowFlying = false;
                    player.abilities.isFlying = false;
                    player.sendPlayerAbilities();
                }
                if (!player.abilities.allowFlying && isFlyingModItem && !isInWater) {
                    player.abilities.allowFlying = true;
                    player.sendPlayerAbilities();
                }
            }

            if (event.side == LogicalSide.CLIENT) {
                if (player.abilities.allowFlying && player.abilities.getFlySpeed() != 0.02f) {
                    player.abilities.setFlySpeed(0.04f);
                    player.sendPlayerAbilities();
                }
                if (!player.abilities.allowFlying && player.abilities.getFlySpeed() != 0.05f) {
                    player.abilities.setFlySpeed(0.05f);
                    player.sendPlayerAbilities();
                }
            }
        }
    }

    @SubscribeEvent
    public void livingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity))
            return;

        PlayerEntity eventPlayer = (PlayerEntity)event.getEntity();
        if (eventPlayer.inventory.armorInventory.get(2).getItem() instanceof FlyingModItem)
            event.setDistance(0F);
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) event.getEntity());

            if (player != null) {
                ItemStack itemStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                if (itemStack.getItem() instanceof FlyingModItem)
                    itemStack.damageItem(10, player, playerEntity -> { /*TODO play sound here*/ });
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void entityConstructingEvent(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerRenderer playerRenderer = Minecraft.getInstance().getRenderManager().getSkinMap().get("default");
            playerRenderer.addLayer(new FlyingModItemLayer<>(playerRenderer));
            PlayerRenderer playerRendererSlim = Minecraft.getInstance().getRenderManager().getSkinMap().get("slim");
            playerRendererSlim.addLayer(new FlyingModItemLayer<>(playerRendererSlim));
        }
    }
}
