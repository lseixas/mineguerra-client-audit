# Allowlist estrita (perfil Guerra)

Política acordada: **só performance + Iris + QoL sem HUD extra**. Modo servidor: `exact` (depois de ignorar internals do Fabric).

Referência no plugin: `mineguerra_plugin_2026/main/resources/client-allowlist.yml`.

## Obrigatório neste mod pack

| Mod | mod id (`fabric.mod.json`) |
|-----|---------------------------|
| **MineGuerra Client Audit** (este repo) | `mineguerra-client-audit` |
| Fabric API | `fabric-api` (ignorado no check: prefixo `fabric-`) |

## Performance (instalar no perfil do jogador)

| Modrinth | mod id |
|----------|--------|
| Sodium | `sodium` |
| Lithium | `lithium` |
| FerriteCore | `ferritecore` |
| Entity Culling | `entityculling` |
| ImmediatelyFast | `immediatelyfast` |
| MoreCulling | `moreculling` |
| Dynamic FPS | `dynamic_fps` |
| Krypton | `krypton` |
| Reese's Sodium Options | `reeses-sodium-options` |

## Shaders

| Modrinth | mod id |
|----------|--------|
| Iris | `iris` |

Shader **packs** (pasta `shaderpacks/`, não são mods): Complementary Unbound, BSL — ok.  
Banidos por substring no nome: `xray`, `cave finder`, `fullbright`, `wallhack`, `esp`.

## QoL permitido

| Modrinth | mod id |
|----------|--------|
| Mod Menu | `modmenu` |
| YetAnotherConfigLib | `yet_another_config_lib_v3` |
| No Chat Reports | `nochatreports` |
| Debugify | `debugify` |
| ok Zoomer | `ok_zoomer` |

## Proibido (kick se presente)

- Jade, WTHIT, Light Overlay, AppleSkin, MiniHUD
- Minimapa (Xaero, JourneyMap, VoxelMap)
- Distant Horizons
- Sodium Extra (gamma/fullbright)
- Indium (obsoleto com Sodium 0.6+)
- Replay Mod, FreeCam, Meteor, Wurst, etc.
- Resource packs xray / cave / ESP

## Resource packs

Hoje: só **vanilla** (+ pack do servidor quando `allowedPackSha1` for preenchido no plugin).  
Pack do evento: `mineguerra_plugin_2026/resourcepack/MineGuerra_Weapons/` — SHA-1 do zip entra na config depois.

## O que este mod deve reportar

Enviar **todos** os mods carregados (ids + versões), não só os da allowlist. O servidor filtra `minecraft`, `java`, `fabricloader`, `mixinextras` e tudo com prefixo `fabric-`, depois aplica `exact` na lista acima.
