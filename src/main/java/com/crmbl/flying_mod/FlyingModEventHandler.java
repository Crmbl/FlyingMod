package com.crmbl.flying_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;

public class FlyingModEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            if (player != null && !player.abilities.isCreativeMode) {
                boolean isInWater = player.isInWater();
                boolean isInLava = player.isInLava();
                ItemStack stack = player.inventory.armorInventory.get(2);
                boolean isFlyingModItem = stack.getItem() instanceof FlyingModItem;
                if (event.side == LogicalSide.SERVER) {
                    if ((player.abilities.allowFlying && !isFlyingModItem) || isInWater || isInLava) {
                        player.abilities.allowFlying = false;
                        player.abilities.isFlying = false;
                        player.sendPlayerAbilities();
                        FlyingModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new FlyingModPacket(player.getEntityId(), player.abilities.isFlying));
                    }
                    if (!player.abilities.allowFlying && isFlyingModItem && !isInWater && !isInLava) {
                        player.abilities.allowFlying = true;
                        player.sendPlayerAbilities();
                        FlyingModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new FlyingModPacket(player.getEntityId(), player.abilities.isFlying));
                    }

                    if (isFlyingModItem) {
                        CompoundNBT tag = stack.getOrCreateTag();
                        boolean isFlying = tag.getBoolean("flying_mod_flying_item");
                        if (isFlying != player.abilities.isFlying) {
                            tag.putBoolean("flying_mod_flying_item", player.abilities.isFlying);
                            stack.setTag(tag);
                            FlyingModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new FlyingModPacket(event.player.getEntityId(), player.abilities.isFlying));
                        }
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
    }

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        ItemStack stack = event.getFrom();
        if (event.getSlot() == EquipmentSlotType.CHEST && stack.getItem() instanceof FlyingModItem) {
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.getBoolean("flying_mod_flying_item")) {
                tag.putBoolean("flying_mod_flying_item", false);
                stack.setTag(tag);
            }
        }
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
    public void onLivingDead(LivingDeathEvent event) {
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
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerRenderer playerRenderer = Minecraft.getInstance().getRenderManager().getSkinMap().get("default");
            playerRenderer.addLayer(new FlyingModItemLayer<>(playerRenderer));
            PlayerRenderer playerRendererSlim = Minecraft.getInstance().getRenderManager().getSkinMap().get("slim");
            playerRendererSlim.addLayer(new FlyingModItemLayer<>(playerRendererSlim));
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayerEntity) {
            ServerPlayerEntity target = ((ServerPlayerEntity) event.getTarget());
            ServerPlayerEntity player = ((ServerPlayerEntity) event.getPlayer());
            FlyingModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new FlyingModPacket(target.getEntityId(), target.abilities.isFlying));
        }
    }
}
