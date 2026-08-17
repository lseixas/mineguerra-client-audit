package org.lseixas.mineguerra_client_audit.audit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientAuditCodecTest {

    @Test
    void codecRoundTrip() throws IOException {
        ClientAuditPayload original = new ClientAuditPayload(
                1,
                "1.21.8",
                "0.16.0",
                List.of(new ClientAuditPayload.ModEntry("sodium", "0.6.0")),
                List.of(new ClientAuditPayload.PackEntry("vanilla", new byte[20])),
                "Complementary"
        );
        byte[] encoded = ClientAuditCodec.encode(original);
        ClientAuditPayload decoded = ClientAuditCodec.decode(encoded);
        assertEquals(original.protocol(), decoded.protocol());
        assertEquals(original.mcVersion(), decoded.mcVersion());
        assertEquals(original.loaderVersion(), decoded.loaderVersion());
        assertEquals(original.mods(), decoded.mods());
        assertEquals(original.shader(), decoded.shader());
        assertEquals("vanilla", decoded.packs().getFirst().id());
        assertEquals(20, decoded.packs().getFirst().sha1().length);
    }

    @Test
    void codecRoundTripMultipleModsAndPacks() throws IOException {
        byte[] sha1 = {
                0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A,
                0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14
        };
        ClientAuditPayload original = new ClientAuditPayload(
                ClientAuditPayload.PROTOCOL,
                "1.21.8",
                "0.16.14",
                List.of(
                        new ClientAuditPayload.ModEntry("mineguerra-client-audit", "0.1.0"),
                        new ClientAuditPayload.ModEntry("sodium", "0.6.13"),
                        new ClientAuditPayload.ModEntry("iris", "1.8.8")
                ),
                List.of(
                        new ClientAuditPayload.PackEntry("vanilla", new byte[20]),
                        new ClientAuditPayload.PackEntry("file/custom", sha1)
                ),
                ""
        );

        ClientAuditPayload decoded = ClientAuditCodec.decode(ClientAuditCodec.encode(original));

        assertEquals(original.protocol(), decoded.protocol());
        assertEquals(original.mcVersion(), decoded.mcVersion());
        assertEquals(original.loaderVersion(), decoded.loaderVersion());
        assertEquals(original.mods(), decoded.mods());
        assertEquals(original.shader(), decoded.shader());
        assertEquals(original.packs().size(), decoded.packs().size());
        assertEquals("vanilla", decoded.packs().get(0).id());
        assertEquals("file/custom", decoded.packs().get(1).id());
        assertArrayEquals(new byte[20], decoded.packs().get(0).sha1());
        assertArrayEquals(sha1, decoded.packs().get(1).sha1());
    }
}
