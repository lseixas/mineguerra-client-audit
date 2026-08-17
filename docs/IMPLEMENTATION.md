# Implementação Fabric — checklist

## Pacotes sugeridos

```
org.lseixas.mineguerra_client_audit
  MineguerraClientAuditMod          # ClientModInitializer
  audit/
    ClientAuditPayload.java         # record (espelhar plugin)
    ClientAuditCodec.java           # encode/decode v1 — copiar lógica do plugin
    ClientAuditCollector.java       # mods + packs + shader
    ClientAuditSender.java          # JOIN -> send channel
```

## 1. Coletar mods

```java
FabricLoader.getInstance().getAllMods().stream()
    .map(mod -> new ModEntry(mod.getMetadata().getId(), mod.getMetadata().getVersion().getFriendlyString()))
```

Incluir **todos** os mods (incluindo `fabric-api`, `sodium`, etc.).

## 2. Coletar resource packs

- Iterar resource packs **habilitados** no cliente.
- Para cada um: `packId` (nome/id estável) + SHA-1 do conteúdo (20 bytes).
- Vanilla: id `vanilla` ou equivalente; SHA-1 pode ser zeros — servidor trata como ignorado.

Referências: `MinecraftClient.getResourcePackManager()`, `ResourcePackProfile`.

## 3. Shader Iris

- Se Iris presente: ler nome do shader pack selecionado (`""` se desligado).
- Opcional: reflection ou Iris API se estável em 1.21.8.

## 4. Enviar no join

- `ClientPlayConnectionEvents.JOIN.register(...)`
- Canal: `Identifier.of("mineguerra", "client_audit")`
- Payload: `ClientAuditCodec.encode(...)`
- Log debug em dev; não spammar.

## 5. Testes

- Unit: round-trip codec (copiar testes de `ClientAuditAllowlistTest` do plugin como referência de bytes).
- Manual: join servidor local com allowlist `enabled: true`.

## 6. Versões

- MC **1.21.8**, Java **21**, Fabric Loader **0.16.14+**
- `fabric.mod.json` id: **`mineguerra-client-audit`** (deve bater com `requiredMods` do servidor)

## Não fazer neste mod

- UI de config complexa (opcional depois)
- Bloquear mods localmente (servidor decide)
- Dependência hard em Sodium/Iris (só `suggests`)
