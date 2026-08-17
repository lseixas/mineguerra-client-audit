# Integração com o servidor Paper

Plugin: **[mineguerra_plugin_2026](https://github.com/lseixas/mineguerra_plugin_2026)**

| Item | Caminho |
|------|---------|
| Listener + timeout | `main/java/.../clientaudit/ClientAuditListener.java` |
| Codec (referência) | `ClientAuditCodec.java` |
| Allowlist | `main/resources/client-allowlist.yml` → `plugins/mineguerra_plugins/client-allowlist.yml` |
| Docs plugin | `docs/CLIENT_AUDIT.md` |

## Fluxo

```mermaid
sequenceDiagram
  participant Client
  participant AuditMod
  participant Paper
  Client->>Paper: login vanilla
  Paper->>Paper: agenda kick 100 ticks
  AuditMod->>Paper: mineguerra:client_audit
  Paper->>Paper: ClientAllowlist.rejectReason
  alt ok
    Paper->>Client: permanece
  else fail
    Paper->>Client: kick
  end
```

## Config servidor (`enabled: false` hoje)

```yaml
enabled: false          # ligar só quando este mod existir
timeoutTicks: 100
bypassPermission: mineguerra.admin
mode: exact
expectedMcVersion: "1.21.8"
```

## Limitações

- O cliente **declara** mods/packs; cheat pode mentir. Objetivo: barra casual (mod extra, sem audit, pack xray).
- Não substitui anti-cheat de combate (Grim, etc.).

## Ativar no evento

1. Publicar JAR deste mod + lista de mods no Discord/wiki
2. Jogadores instalam o pack Fabric estrito
3. Servidor: `enabled: true` + `allowedPackSha1` quando o resource pack MineGuerra for obrigatório
4. Testar join staff (bypass) e jogador com allowlist exata

## Servidor de teste

- Docker: `mine-guerra-bukkit-2026`, porta **25567**
- Deploy plugin: `./gradlew jar` no repo do plugin (copia JAR via `gradle.local.properties`)
