package org.lseixas.mineguerra_client_audit.audit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Valida os 3 cenarios E2E de allowlist usando o mesmo codec do plugin Paper.
 * Logica espelhada de {@code ClientAllowlist.rejectReason} no repo mineguerra_plugin_2026.
 */
class ClientAuditAllowlistIntegrationTest {

    private static final Set<String> ALLOWED_MODS = Set.of(
            "mineguerra-client-audit",
            "cloth-config",
            "debugify",
            "dynamic_fps",
            "entityculling",
            "ferritecore",
            "immediatelyfast",
            "iris",
            "krypton",
            "lithium",
            "modmenu",
            "moreculling",
            "reeses-sodium-options",
            "placeholder-api",
            "yet_another_config_lib_v3",
            "sodium"
    );

    private static final Set<String> REQUIRED_MODS = Set.of("mineguerra-client-audit");

    private static final Set<String> IGNORED_MOD_IDS = Set.of(
            "minecraft",
            "java",
            "fabricloader",
            "mixinextras",
            "cloth-basic-math",
            "conditional-mixin",
            "mixinsquared",
            "transition",
            "trender"
    );

    private static final List<String> IGNORED_PREFIXES = List.of("fabric-", "com_", "org_", "io_", "net_");

    @Test
    void exactAllowlistAcceptsAfterCodecRoundTrip() throws IOException {
        ClientAuditPayload payload = payload(List.of(
                mod("minecraft", "1.21.8"),
                mod("fabric-api", "0.134.0"),
                mod("fabric-networking-api-v1", "5.0.3"),
                mod("mineguerra-client-audit", "0.1.1"),
                mod("cloth-config", "17.0.0"),
                mod("debugify", "1.21.8+1.0"),
                mod("dynamic_fps", "3.9.3"),
                mod("entityculling", "1.8.2"),
                mod("ferritecore", "7.0.2"),
                mod("immediatelyfast", "1.8.1"),
                mod("iris", "1.8.8"),
                mod("krypton", "0.2.8"),
                mod("lithium", "0.14.8"),
                mod("modmenu", "12.0.0"),
                mod("moreculling", "1.2.1"),
                mod("reeses-sodium-options", "1.8.3"),
                mod("placeholder-api", "2.6.0"),
                mod("yet_another_config_lib_v3", "3.6.2"),
                mod("sodium", "0.6.13")
        ), List.of(new ClientAuditPayload.PackEntry("vanilla", new byte[20])), "");

        ClientAuditPayload decoded = ClientAuditCodec.decode(ClientAuditCodec.encode(payload));
        assertTrue(rejectReason(decoded).isEmpty(), rejectReason(decoded).orElse(""));
    }

    @Test
    void missingAuditModIsRejected() throws IOException {
        ClientAuditPayload payload = payload(
                List.of(mod("sodium", "0.6.13"), mod("iris", "1.8.8")),
                List.of(),
                ""
        );
        ClientAuditPayload decoded = ClientAuditCodec.decode(ClientAuditCodec.encode(payload));
        Optional<String> reason = rejectReason(decoded);
        assertTrue(reason.isPresent());
        assertTrue(reason.get().contains("mineguerra-client-audit"));
    }

    @Test
    void extraModJadeIsRejected() throws IOException {
        ClientAuditPayload payload = payload(
                List.of(
                        mod("mineguerra-client-audit", "0.1.0"),
                        mod("sodium", "0.6.13"),
                        mod("iris", "1.8.8"),
                        mod("jade", "1.0.0")
                ),
                List.of(),
                ""
        );
        ClientAuditPayload decoded = ClientAuditCodec.decode(ClientAuditCodec.encode(payload));
        Optional<String> reason = rejectReason(decoded);
        assertTrue(reason.isPresent());
        assertTrue(reason.get().toLowerCase(Locale.ROOT).contains("jade"));
    }

    @Test
    void pluginCodecCanDecodeClientEncodedBytes() throws IOException {
        byte[] encoded = ClientAuditCodec.encode(payload(
                List.of(mod("mineguerra-client-audit", "0.1.0"), mod("sodium", "0.6.0")),
                List.of(new ClientAuditPayload.PackEntry("vanilla", new byte[20])),
                "Complementary"
        ));
        org.lseixas.mineguerra_plugins.clientaudit.ClientAuditPayload decoded =
                org.lseixas.mineguerra_plugins.clientaudit.ClientAuditCodec.decode(encoded);
        assertEquals("1.21.8", decoded.mcVersion());
        assertEquals("Complementary", decoded.shader());
        assertEquals(2, decoded.mods().size());
    }

    private static ClientAuditPayload payload(
            List<ClientAuditPayload.ModEntry> mods,
            List<ClientAuditPayload.PackEntry> packs,
            String shader
    ) {
        return new ClientAuditPayload(1, "1.21.8", "0.16.14", new ArrayList<>(mods), new ArrayList<>(packs), shader);
    }

    private static ClientAuditPayload.ModEntry mod(String id, String version) {
        return new ClientAuditPayload.ModEntry(id, version);
    }

    private static Optional<String> rejectReason(ClientAuditPayload payload) {
        if (payload.protocol() != ClientAuditPayload.PROTOCOL) {
            return Optional.of("Protocolo de auditoria invalido.");
        }
        if (!"1.21.8".equals(payload.mcVersion())) {
            return Optional.of("Versao de Minecraft nao permitida.");
        }

        Set<String> reported = new TreeSet<>();
        for (ClientAuditPayload.ModEntry mod : payload.mods()) {
            if (!isIgnored(mod.id())) {
                reported.add(mod.id());
            }
        }

        for (String required : REQUIRED_MODS) {
            if (!reported.contains(required)) {
                return Optional.of("Mod obrigatorio ausente: " + required);
            }
        }

        Set<String> extra = new TreeSet<>(reported);
        extra.removeAll(ALLOWED_MODS);
        if (!extra.isEmpty()) {
            return Optional.of("Mods nao permitidos: " + String.join(", ", extra));
        }

        Set<String> missing = new TreeSet<>(ALLOWED_MODS);
        missing.removeAll(reported);
        if (!missing.isEmpty()) {
            return Optional.of("Faltam mods da allowlist: " + String.join(", ", missing));
        }

        return Optional.empty();
    }

    private static boolean isIgnored(String modId) {
        if (IGNORED_MOD_IDS.contains(modId)) {
            return true;
        }
        for (String prefix : IGNORED_PREFIXES) {
            if (modId.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
