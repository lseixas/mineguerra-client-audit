package org.lseixas.mineguerra_client_audit.audit;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import org.lseixas.mineguerra_client_audit.MineguerraClientAuditMod;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * SHA-1 (20 bytes) de resource packs habilitados.
 * Packs {@code file/*.zip} usam o SHA-1 do zip (igual a {@code resource-pack-sha1} / allowlist).
 */
final class ResourcePackHasher {

    private static final int SHA1_LEN = 20;
    private static final String FILE_PREFIX = "file/";

    private ResourcePackHasher() {
    }

    static byte[] sha1(ResourcePackProfile profile, Path resourcePackDir) {
        ResourcePackSource source = profile.getSource();
        if (source == ResourcePackSource.BUILTIN || source == ResourcePackSource.SERVER) {
            return new byte[SHA1_LEN];
        }
        String packId = profile.getId();
        if (packId == null || !packId.startsWith(FILE_PREFIX)) {
            // Fabric Mods (id fabric), packs escondidos dos mods, vanilla.
            return new byte[SHA1_LEN];
        }
        byte[] fromZip = sha1ZipFile(profile.getId(), resourcePackDir);
        if (fromZip != null) {
            return fromZip;
        }
        try (ResourcePack pack = profile.createResourcePack()) {
            return sha1Content(pack);
        } catch (IOException ex) {
            MineguerraClientAuditMod.LOGGER.warn("Falha ao calcular SHA-1 do pack {}", profile.getId(), ex);
            return new byte[SHA1_LEN];
        }
    }

    private static byte[] sha1ZipFile(String packId, Path resourcePackDir) {
        if (resourcePackDir == null || packId == null || !packId.startsWith(FILE_PREFIX)) {
            return null;
        }
        Path path = resourcePackDir.resolve(packId.substring(FILE_PREFIX.length()));
        if (!Files.isRegularFile(path)) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(Files.readAllBytes(path));
            return digest.digest();
        } catch (IOException | NoSuchAlgorithmException ex) {
            MineguerraClientAuditMod.LOGGER.warn("Falha ao hashear zip do pack {}", packId, ex);
            return null;
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
