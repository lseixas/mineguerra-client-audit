# MineGuerra Client Audit

Mod **Fabric client-side** (1.21.8) que envia ao servidor Paper a lista de mods, resource packs ativos e shader Iris — para validação contra a allowlist estrita do evento **MineGuerra**.

Servidor (plugin Paper): [`lseixas/mineguerra_plugin_2026`](https://github.com/lseixas/mineguerra_plugin_2026) — pacote `clientaudit/`, config `client-allowlist.yml`.

## Status

- [x] Scaffold Fabric + docs + contrato wire v1
- [ ] Coletar mods (`FabricLoader.getInstance().getAllMods()`)
- [ ] Coletar resource packs habilitados + SHA-1
- [ ] Coletar shader Iris ativo (nome ou vazio)
- [ ] Enviar plugin message `mineguerra:client_audit` no join
- [ ] Testar contra Paper com `enabled: true`

## Build

```bash
./gradlew build
```

JAR em `build/libs/mineguerra-client-audit-0.1.0.jar`.

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
