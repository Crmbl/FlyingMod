package com.crmbl.flying_mod;

import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FlyingModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, FlyingMod.MOD_ID);
    public static final RegistryObject<Item> FLYING_ITEM = ITEMS.register("flying_item", () ->
            new FlyingModItem(new ElytraItem.Properties()
                    .maxStackSize(1)
                    .maxDamage(30)
                    .group(ItemGroup.TRANSPORTATION)
            )
    );
}
