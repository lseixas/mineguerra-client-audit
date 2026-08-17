# Prompt para agente — implementar MineGuerra Client Audit

Copie o bloco abaixo para outro agente (Cursor, etc.).

---

## Contexto

Repositório: **mineguerra-client-audit** (Fabric 1.21.8, client-only).  
Servidor Paper: **mineguerra_plugin_2026** — já implementa handshake em `clientaudit/` com canal `mineguerra:client_audit`, allowlist `exact`, timeout 100 ticks. Config ainda com `enabled: false`.

Este mod deve, **no join ao servidor**, enviar um plugin message com: versão MC, versão Fabric Loader, lista completa de mods (id + version), resource packs habilitados (id + SHA-1 20 bytes), nome do shader Iris (ou vazio).

## Tarefa

1. Ler `docs/PROTOCOL.md` e espelhar `ClientAuditCodec` do plugin (`mineguerra_plugin_2026/.../clientaudit/ClientAuditCodec.java`).
2. Implementar `ClientAuditCollector` + `ClientAuditSender`.
3. Registrar em `ClientPlayConnectionEvents.JOIN` (Fabric API).
4. Garantir mod id **`mineguerra-client-audit`** em `fabric.mod.json`.
5. Testes unitários de encode/decode (round-trip).
6. `./gradlew build` deve passar.

## Regras

- **Client-only** — nada no servidor Fabric.
- Enviar **todos** os mods carregados; não filtrar no cliente.
- MC target: **1.21.8**, Java 21.
- Não adicionar dependência obrigatória em Sodium/Iris.
- Seguir allowlist em `docs/ALLOWLIST.md` só como documentação; validação é no Paper.

## Referências no plugin (repo irmão)

```
mineguerra_plugin_2026/
  main/java/.../clientaudit/ClientAuditCodec.java
  main/java/.../clientaudit/ClientAuditPayload.java
  main/java/.../clientaudit/ClientAllowlist.java
  main/resources/client-allowlist.yml
  src/test/.../clientaudit/ClientAuditAllowlistTest.java
```

## Critérios de pronto

- [x] Join em servidor com `enabled: true` + allowlist exata → permanece conectado (validado via `ClientAuditAllowlistIntegrationTest` + codec cruzado com plugin)
- [x] Join sem o mod → kick timeout (esperado; servidor teste com audit ATIVO)
- [x] Join com mod extra (ex. Jade) → kick do servidor (validado via teste de integração)
- [x] `./gradlew test` + `./gradlew build` ok
- [x] Compatibilidade wire format: bytes do cliente decodificados por `ClientAuditCodec` do plugin Paper

## Não implementar agora

- Mod menu de config
- Download automático de mods
- Anti-tamper / obfuscação

---

## Comando sugerido para o agente

> Implemente o handshake descrito em `docs/PROTOCOL.md` e `docs/IMPLEMENTATION.md`. Copie a lógica de codec do plugin Paper em `../mineguerra_plugin_2026`. Mantenha o mod minimalista e client-only.
