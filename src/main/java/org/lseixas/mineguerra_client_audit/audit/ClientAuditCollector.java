package org.lseixas.mineguerra_client_audit.audit;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.SharedConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Coleta mods, resource packs habilitados e shader Iris para o handshake v1.
 */
public final class ClientAuditCollector {

    private ClientAuditCollector() {
    }

    public static ClientAuditPayload collect(MinecraftClient client) {
        String mcVersion = SharedConstants.getGameVersion().name();
        String loaderVersion = FabricLoader.getInstance()
                .getModContainer("fabricloader")
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");

        List<ClientAuditPayload.ModEntry> mods = FabricLoader.getInstance().getAllMods().stream()
                .map(mod -> new ClientAuditPayload.ModEntry(
                        mod.getMetadata().getId(),
                        mod.getMetadata().getVersion().getFriendlyString()))
                .toList();

        List<ClientAuditPayload.PackEntry> packs = collectPacks(client);
        String shader = IrisShaderProbe.currentShaderName();

        return new ClientAuditPayload(
                ClientAuditPayload.PROTOCOL,
                mcVersion,
                loaderVersion,
                mods,
                packs,
                shader
        );
    }

    private static List<ClientAuditPayload.PackEntry> collectPacks(MinecraftClient client) {
        ResourcePackManager manager = client.getResourcePackManager();
        List<ClientAuditPayload.PackEntry> packs = new ArrayList<>();
        for (ResourcePackProfile profile : manager.getEnabledProfiles()) {
            packs.add(new ClientAuditPayload.PackEntry(profile.getId(), ResourcePackHasher.sha1(profile)));
        }
        return packs;
    }
}
