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

## Shaders (Iris)

**Nenhum shader permitido.** Iris pode estar instalado na assinatura de mods, mas o pack tem que estar **desligado** (string vazia no handshake). Qualquer shader ligado → kick `Shader nao permitido.`

## Resource packs

Permitidos:
- **Vanilla** (Default)
- **Fabric Mods** (id `fabric` — recursos embutidos dos mods, sempre ligado)
- Pack do **servidor**
- Zip **MineGuerra Weapons** (`file/MineGuerra_Weapons.zip`)

SHA-1 do zip MineGuerra (recalcular se as texturas mudarem):

```
b5d400b5b99d32a7b55ef22beeac475dbd70293b
```

Qualquer outro pack da pasta `resourcepacks/` (`file/...`) → kick. Programmer Art / High Contrast são built-in e passam.

## Ignorados no check

`minecraft`, `java`, `fabricloader`, `mixinextras`, prefixo `fabric-`.

Bibliotecas **jar-in-jar** (não instale na pasta `mods/`): Cloth math, TwelveMonkeys, ANTLR, MixinSquared, etc. O cliente não as reporta; o servidor ignora ids `cloth-basic-math`, `conditional-mixin`, `mixinsquared`, `transition`, `trender` e prefixos Maven `com_`, `org_`, `io_`, `net_`.

## O que este mod reporta

Todos os mods carregados (ids + versões). O servidor filtra internals e aplica `exact` na lista acima.
