package com.crmbl.flying_mod;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class FlyingModItem extends ElytraItem {
    public FlyingModItem(Properties builder) {
        super(builder);
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.CHEST;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return false; }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return super.getDisplayName(stack).applyTextStyles(TextFormatting.YELLOW);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "flying_mod:textures/entity/flying_mod_texture.png";
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }
}
