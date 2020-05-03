package com.crmbl.flying_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FlyingModPacket {
    private int id;
    private boolean flag;

    public FlyingModPacket(int id, boolean flag) {
        this.id = id;
        this.flag = flag;
    }

    public FlyingModPacket(PacketBuffer packetBuffer) {
        id = packetBuffer.readInt();
        flag = packetBuffer.readBoolean();
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(id);
        packetBuffer.writeBoolean(flag);
    }

    public static FlyingModPacket decode(PacketBuffer buf) {
        return new FlyingModPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().world != null) {
                PlayerEntity entity = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(id);
                if (entity != null) {
                    entity.abilities.isFlying = flag;
                    entity.abilities.allowFlying = flag;
                    entity.sendPlayerAbilities();
                }
            }
        });

        context.setPacketHandled(true);
    }
}