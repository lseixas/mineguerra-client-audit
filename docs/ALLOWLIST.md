# Allowlist estrita (perfil Guerra)

Política: **assinatura exact dos ids** (nem extra, nem faltando). Versões **não** são pinadas.  
Referência no plugin: `mineguerra_plugin_2026/main/resources/client-allowlist.yml`.

## Obrigatório

| Mod | mod id |
|-----|--------|
| **MineGuerra Client Audit** | `mineguerra-client-audit` |

Fabric API (`fabric-api`) é ignorado no check (prefixo `fabric-`).

## Mods da assinatura (`allowedMods`)

| Nome | mod id |
|------|--------|
| MineGuerra Client Audit | `mineguerra-client-audit` |
| Cloth Config API | `cloth-config` |
| Debugify | `debugify` |
| Dynamic FPS | `dynamic_fps` |
| Entity Culling | `entityculling` |
| FerriteCore | `ferritecore` |
| ImmediatelyFast | `immediatelyfast` |
| Iris Shaders | `iris` |
| Krypton | `krypton` |
| Lithium | `lithium` |
| Mod Menu | `modmenu` |
| MoreCulling | `moreculling` |
| Reese's Sodium Options | `reeses-sodium-options` |
| Text Placeholder API | `placeholder-api` |
| YetAnotherConfigLib | `yet_another_config_lib_v3` |
| Sodium | `sodium` |

`sodium` não veio na lista informal, mas **entra na allowlist**: Iris e Reese's Sodium Options dependem dele. Sem o id, quem instalar o pack levaria kick de mod extra.

## Shaders permitidos (Iris)

Shader desligado (`""`) é ok. Ligado: só estes (match ignora espaços/`_`/`-`/`.zip` e sufixo de versão):

- Miniature Shader by ukrech
- Complementary Unbound
- Complementary Reimagined

Banidos por substring (camada extra): `xray`, `cave finder`, `fullbright`, `wallhack`, `esp`.

## Resource packs

Só **vanilla/server** + zip **MineGuerra Weapons** (`resourcepack/MineGuerra_Weapons/`).

SHA-1 do zip (recalcular se as texturas mudarem):

```
b5d400b5b99d32a7b55ef22beeac475dbd70293b
```

Qualquer outro pack de arquivo → kick.

## Ignorados no check

`minecraft`, `java`, `fabricloader`, `mixinextras`, prefixo `fabric-`.

## O que este mod reporta

Todos os mods carregados (ids + versões). O servidor filtra internals e aplica `exact` na lista acima.
