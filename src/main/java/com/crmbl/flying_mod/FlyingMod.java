package com.crmbl.flying_mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("flying_mod")
public class FlyingMod
{
    public static final String MOD_ID = "flying_mod";

    public FlyingMod() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new FlyingModEventHandler());
        FlyingModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        new FlyingModPacketHandler().register();
    }
}