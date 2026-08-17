package org.lseixas.mineguerra_client_audit.audit;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import org.lseixas.mineguerra_client_audit.MineguerraClientAuditMod;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * Calcula SHA-1 (20 bytes) de resource packs habilitados.
 */
final class ResourcePackHasher {

    private static final int SHA1_LEN = 20;

    private ResourcePackHasher() {
    }

    static byte[] sha1(ResourcePackProfile profile) {
        ResourcePackSource source = profile.getSource();
        if (source == ResourcePackSource.BUILTIN || source == ResourcePackSource.SERVER) {
            return new byte[SHA1_LEN];
        }
        try (ResourcePack pack = profile.createResourcePack()) {
            return sha1Content(pack);
        } catch (IOException ex) {
            MineguerraClientAuditMod.LOGGER.warn("Falha ao calcular SHA-1 do pack {}", profile.getId(), ex);
            return new byte[SHA1_LEN];
        }
    }

    private static byte[] sha1Content(ResourcePack pack) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-1 indisponivel", ex);
        }

        TreeMap<String, byte[]> entries = new TreeMap<>();
        pack.findResources(ResourceType.CLIENT_RESOURCES, "", "", (id, supplier) -> {
            try (InputStream in = supplier.get()) {
                entries.put(id.toString(), in.readAllBytes());
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });

        for (var entry : entries.entrySet()) {
            digest.update(entry.getKey().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            digest.update(entry.getValue());
        }
        return digest.digest();
    }
}
