package org.lseixas.mineguerra_client_audit.audit;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import org.lseixas.mineguerra_client_audit.MineguerraClientAuditMod;

import java.io.IOException;

/**
 * Registra o canal e envia o handshake uma vez após entrar no mundo (PLAY).
 */
public final class ClientAuditSender {

    private ClientAuditSender() {
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ClientAuditPacket.ID, ClientAuditPacket.CODEC);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                client.execute(() -> send(client)));
    }

    private static void send(MinecraftClient client) {
        if (!ClientPlayNetworking.canSend(ClientAuditPacket.ID)) {
            MineguerraClientAuditMod.LOGGER.debug(
                    "Servidor nao declarou canal {}; handshake nao enviado",
                    ClientAuditCodec.CHANNEL
            );
            return;
        }

        try {
            ClientAuditPayload payload = ClientAuditCollector.collect(client);
            byte[] encoded = ClientAuditCodec.encode(payload);
            ClientPlayNetworking.send(new ClientAuditPacket(encoded));
            MineguerraClientAuditMod.LOGGER.debug(
                    "Handshake enviado: {} mods, {} packs, shader='{}'",
                    payload.mods().size(),
                    payload.packs().size(),
                    payload.shader()
            );
        } catch (IOException ex) {
            MineguerraClientAuditMod.LOGGER.error("Falha ao codificar handshake de auditoria", ex);
        }
    }
}
