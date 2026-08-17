# MineGuerra Client Audit

Mod **Fabric client-side** (1.21.8) que envia ao servidor Paper a lista de mods, resource packs ativos e shader Iris — para validação contra a allowlist estrita do evento **MineGuerra**.

Servidor (plugin Paper): [`lseixas/mineguerra_plugin_2026`](https://github.com/lseixas/mineguerra_plugin_2026) — pacote `clientaudit/`, config `client-allowlist.yml`.

## Status

- [x] Scaffold Fabric + docs + contrato wire v1
- [x] Coletar mods (`FabricLoader.getInstance().getAllMods()`)
- [x] Coletar resource packs habilitados + SHA-1
- [x] Coletar shader Iris ativo (nome ou vazio)
- [x] Enviar plugin message `mineguerra:client_audit` no join
- [x] Testes unitários + integração allowlist (codec compatível com plugin Paper)
- [x] Servidor teste com `enabled: true` (Docker `mine-guerra-bukkit-2026:25567`)

Validação manual de join in-game recomendada após instalar o JAR da [release](https://github.com/lseixas/mineguerra-client-audit/releases).

## Build

```bash
./gradlew build
```

JAR em `build/libs/mineguerra-client-audit-0.1.1.jar` (ou baixar da [release GitHub](https://github.com/lseixas/mineguerra-client-audit/releases)).

## Instalação (jogadores)

1. Baixar `mineguerra-client-audit-0.1.1.jar` da release
2. Colocar em `.minecraft/mods/` (Fabric Loader 0.16.14+, MC 1.21.8)
3. Instalar o restante do pack conforme [docs/ALLOWLIST.md](docs/ALLOWLIST.md)
4. Conectar ao servidor MineGuerra — o handshake é enviado automaticamente no join

## Documentação

| Doc | Conteúdo |
|-----|----------|
| [docs/README.md](docs/README.md) | Índice |
| [docs/PROTOCOL.md](docs/PROTOCOL.md) | Formato binário v1 (espelha o plugin) |
| [docs/ALLOWLIST.md](docs/ALLOWLIST.md) | Mods permitidos (perfil estrito) |
| [docs/SERVER_INTEGRATION.md](docs/SERVER_INTEGRATION.md) | Lado Paper, kicks, config |
| [docs/IMPLEMENTATION.md](docs/IMPLEMENTATION.md) | Checklist técnico Fabric |
| [docs/AGENT_PROMPT.md](docs/AGENT_PROMPT.md) | Prompt para outro agente implementar |

## Licença

MIT — ver [LICENSE](LICENSE).
