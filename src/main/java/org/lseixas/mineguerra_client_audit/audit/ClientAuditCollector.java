package org.lseixas.mineguerra_client_audit.audit;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
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
                .filter(ClientAuditCollector::isTopLevelMod)
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
            packs.add(new ClientAuditPayload.PackEntry(profile.getId(), ResourcePackHasher.sha1(profile, client.getResourcePackDir())));
        }
        return packs;
    }

    /**
     * Jar-in-jar / nested libs (Cloth math, TwelveMonkeys, ANTLR, etc.) nao entram na assinatura.
     */
    static boolean isTopLevelMod(ModContainer mod) {
        if (mod.getContainingMod().isPresent()) {
            return false;
        }
        return mod.getOrigin().getKind() != ModOrigin.Kind.NESTED;
    }
}
