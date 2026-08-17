# Protocolo v1 — `mineguerra:client_audit`

Canal Bukkit/Fabric (plugin message): **`mineguerra:client_audit`**

Espelha [`ClientAuditCodec.java`](https://github.com/lseixas/mineguerra_plugin_2026/blob/main/main/java/org/lseixas/mineguerra_plugins/clientaudit/ClientAuditCodec.java) no plugin Paper.

## Quando enviar

- **Uma vez** após entrar no mundo (estado PLAY), logo após `ClientPlayConnectionEvents.JOIN` (ou equivalente).
- Timeout servidor: **100 ticks (~5s)**. Sem pacote → kick `Instale o mod MineGuerra Audit`.
- Staff com `mineguerra.admin` ignora o handshake.

## Ordem dos campos (big-endian)

| # | Tipo | Campo |
|---|------|--------|
| 1 | `unsigned short` | `protocol` = **1** |
| 2 | UTF-8 | `mcVersion` — ex. `"1.21.8"` |
| 3 | UTF-8 | `loaderVersion` — ex. `"0.16.14"` |
| 4 | varint + lista | `mods` |
| 5 | varint + lista | `packs` |
| 6 | UTF-8 | `shader` — nome do pack Iris ou `""` |

### UTF-8 string

- varint `length` (bytes UTF-8)
- `length` bytes

### Mods

- varint `n`
- `n` × (`modId` UTF-8, `version` UTF-8)
- **Não** é obrigatório ordenar no cliente; o servidor usa conjuntos.

### Packs

- varint `n`
- `n` × (`packId` UTF-8, **20 bytes** SHA-1 raw)
- Só packs **habilitados** pelo jogador.
- Vanilla/server: enviar com id reconhecível (`vanilla`, `server`, etc.) — o servidor ignora na checagem de SHA-1.

### Shader

- Nome do shader pack Iris selecionado, ou string vazia.

## Varint (Java unsigned LEB128)

Mesmo algoritmo do protocolo Minecraft: 7 bits por byte, MSB = continuação.

## Fabric: envio

Usar **Fabric Networking API** (`ClientPlayNetworking`) ou registrar o canal custom payload compatível com plugin messages Bukkit.

Exemplo de registro (pseudocódigo):

```java
Identifier channel = Identifier.of("mineguerra", "client_audit");
ClientPlayNetworking.registerGlobalReceiver(...); // servidor — não é este mod

// Cliente:
ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
    byte[] payload = ClientAuditCodec.encode(collectPayload());
    ClientPlayNetworking.send(channel, payload);
});
```

Verificar na versão 1.21.8 se o canal deve ser `CustomPayload` tipado; o plugin Paper usa `PluginMessageListener` clássico — testar compatibilidade com `ClientPlayNetworking.send(Identifier, PacketByteBuf)`.

## Erros de kick (servidor)

| Motivo | Mensagem (exemplo) |
|--------|-------------------|
| Timeout | Instale o mod MineGuerra Audit. |
| Protocol ≠ 1 | Protocolo de auditoria invalido. |
| MC ≠ 1.21.8 | Versao de Minecraft nao permitida. |
| Mod extra | Mods nao permitidos: jade, … |
| Mod faltando | Faltam mods da allowlist: sodium, … |
| Pack SHA-1 | Resource pack nao permitido: … |
| Shader banido | Shader nao permitido. |

## Teste local

1. Plugin: `client-allowlist.yml` → `enabled: true`
2. Entrar com perfil exato da [ALLOWLIST.md](ALLOWLIST.md)
3. Entrar com Jade instalado → kick
4. Entrar sem este mod → timeout kick
