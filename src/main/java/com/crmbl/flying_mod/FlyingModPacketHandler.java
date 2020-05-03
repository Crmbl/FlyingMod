package com.crmbl.flying_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class FlyingModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("flying_mod","main"),() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
        INSTANCE.registerMessage(0, FlyingModPacket.class, FlyingModPacket::encode, FlyingModPacket::decode, FlyingModPacket::handle);
    }
}