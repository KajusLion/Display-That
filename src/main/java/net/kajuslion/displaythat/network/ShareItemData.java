package net.kajuslion.displaythat.network;

import net.kajuslion.displaythat.DisplayThat;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ShareItemData(int slot) implements CustomPacketPayload {
    public static final Type<ShareItemData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DisplayThat.MODID, "share_item_data"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
