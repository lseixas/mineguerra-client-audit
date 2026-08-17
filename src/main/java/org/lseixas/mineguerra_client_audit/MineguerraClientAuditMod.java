package org.lseixas.mineguerra_client_audit;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mod client-side que reporta mods/packs/shader ao servidor Paper.
 * Implementação do handshake: ver docs/PROTOCOL.md e docs/IMPLEMENTATION.md.
 */
public final class MineguerraClientAuditMod implements ClientModInitializer {

    public static final String MOD_ID = "mineguerra-client-audit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("MineGuerra Client Audit carregado (stub — handshake ainda nao implementado).");
        // TODO: registrar ClientPlayConnectionEvents.JOIN -> ClientAuditSender.send()
    }
}
