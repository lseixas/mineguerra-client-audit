package org.lseixas.mineguerra_client_audit.audit;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Custom payload Fabric compatível com plugin messages Bukkit no canal {@link ClientAuditCodec#CHANNEL}.
 */
public record ClientAuditPacket(byte[] data) implements CustomPayload {

    public static final CustomPayload.Id<ClientAuditPacket> ID =
            new CustomPayload.Id<>(Identifier.of("mineguerra", "client_audit"));

    public static final PacketCodec<PacketByteBuf, ClientAuditPacket> CODEC = PacketCodec.of(
            (packet, buf) -> buf.writeBytes(packet.data()),
            buf -> {
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                return new ClientAuditPacket(bytes);
            }
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
