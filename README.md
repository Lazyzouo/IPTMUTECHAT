# IPTMUTECHAT

[![Release](https://img.shields.io/github/v/release/Lazyzouo/IPTMUTECHAT?display_name=tag&sort=semver)](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest)
[![Build](https://github.com/Lazyzouo/IPTMUTECHAT/actions/workflows/build.yml/badge.svg)](https://github.com/Lazyzouo/IPTMUTECHAT/actions/workflows/build.yml)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk)](https://adoptium.net/)
[![Paper/Folia](https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.1--1.21.11-2C2C2C)](https://papermc.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-22C55E.svg)](LICENSE)

> [!IMPORTANT]
> **Open-Source and Data Privacy Statement**
>
> IPTMUTECHAT is a fully open-source project. Its source code and release workflow are publicly available for inspection, and the plugin contains no telemetry, remote logging, data-collection endpoints, or hidden mechanisms for retrieving server data. All files and records created by the plugin, including player IP records, moderation data, and configuration, are stored only on the server where the plugin is installed. Updates are published as public artifacts in this GitHub repository; the built-in updater only contacts GitHub Releases over HTTPS to check for and download those published versions. The project does not operate any separate service that receives or collects information from your server.
>
> **开源与数据隐私声明**
>
> IPTMUTECHAT 是一个完全开源的项目，源码与发布流程均公开可审查。插件不包含遥测、远程日志、数据采集端点或任何隐藏的服务器数据回传机制。插件创建的所有文件与记录，包括玩家 IP 记录、管理数据及配置，均只保存在安装该插件的服务器本地。每次更新都会先作为公开资源发布到本 GitHub 仓库；内置更新器仅通过 HTTPS 访问 GitHub Releases，以检查并下载已发布版本。项目方不运营任何用于接收或收集你的服务器资料的独立服务。

## Overview

IPTMUTECHAT is a bilingual player investigation and chat moderation plugin for Paper and Folia servers. It gives authorized staff a single, permission-controlled toolkit for reviewing a player's recorded IP address and identifying accounts observed on the same IP, forcing an online player to send a specified chat message, ignoring messages from selected players, and applying permanent or timed mutes. Operators can also use `/iphide` to conceal their own IP information and same-IP account associations. A command-managed protection whitelist lets explicitly trusted players inspect hidden IP records and remain immune to plugin mutes.

Sensitive IP data and protection-whitelist records stay on the server and are never sent to GitHub. Access to IP lookup, forced chat, mute management, OP privacy controls, and whitelist administration is governed by explicit permissions.

> Current release: **1.4.0** · Tested server version: **Paper/Folia 1.21.11** · Author: **Lazyz**

## Features

- Query a player's latest recorded IP and same-IP accounts.
- Let authorized operators hide their own IP information and same-IP associations from lookup results with `/iphide`.
- Manage a persistent protection whitelist whose members can query hidden IP/same-IP records and cannot be muted by this plugin.
- Force an online player to send a specified chat message when the sender has permission.
- Ignore or restore messages from selected players with a personal ignore list.
- Permanent and timed mutes with reasons and status inspection.
- Private-message reply tracking and configurable chat cooldowns.
- Simplified Chinese (`zh_CN`) and English (`en_US`) runtime modes.
- Bold player-facing plugin text, pixel-centered panel content, yellow separator stars, and a centered Help identity line with distinct plugin/version/author colors, white commands, gray descriptions, and single-color section labels.
- Startup update checks with direct download, SHA-256 verification, JAR identity validation, and a manual fallback link.
- Folia-aware plugin metadata and persistent YAML storage.
- Automated GitHub builds and versioned Releases with English-default and Chinese-default JARs, official release notes, and GitHub SHA-256 digests.

## Requirements

| Component | Requirement |
| --- | --- |
| Server | Paper or Folia 1.20.1 through 1.21.11 |
| Tested | Paper/Folia 1.21.11 |
| Plugin bytecode | Java 17 or newer |
| Modern Minecraft | Use the Java version required by your server; 1.20.5+ normally requires Java 21 |
| Unsupported | CraftBukkit/Spigot-only environments are not tested |

## Install

1. Download one package from [the latest Release](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest): `IPTMUTECHAT-<version>-en.us.jar` for English defaults or `IPTMUTECHAT-<version>-zh.cn.jar` for Simplified Chinese defaults.
2. Stop the server and place the JAR in `plugins/`.
3. Start the server once to create `plugins/IPTMUTECHAT/config.yml`.
4. Review the privacy notice and permissions before granting `/ipinfo` access.

Both packages contain identical plugin code, resources, and configuration comments. Only the official default `language` parameter differs. You can still switch either package between `en_US` and `zh_CN` in `config.yml` and run `/chatreload`.

Release asset filenames are immutable: every Release contains exactly `IPTMUTECHAT-<version>-en.us.jar` and `IPTMUTECHAT-<version>-zh.cn.jar` as its two custom assets. The publication workflow rejects renamed or additional assets.

## Commands

| Command | Default access | Purpose |
| --- | --- | --- |
| `/ipinfo <player>` | OP | Show the latest IP and visible same-IP accounts |
| `/iphide` | OP | Toggle privacy for your own IP and same-IP listing |
| `/mute <player> [duration] [reason]` | OP | Apply a permanent or timed mute |
| `/unmute <player>` | OP | Remove a mute |
| `/muteinfo <player>` | OP | Inspect a mute record |
| `/ignore <player>` | Everyone | Toggle a player in your ignore list |
| `/ignorelist` | Everyone | Show your ignore list |
| `/forcesay <player> <message>` | OP | Make an online player send a message |
| `/reply <message>` or `/r` | Everyone | Reply to the last private-message target |
| `/iptmutechat whitelist add|remove|list [player]` | OP | Manage the IP/mute protection whitelist |
| `/chatreload` | OP | Reload configuration and language resources |
| `/chathelp` | Everyone | Show permission-aware help |

Full permission details are in [Commands and permissions](docs/COMMANDS.md).

## Configuration Safety

The tracked [`src/main/resources/config.yml`](src/main/resources/config.yml) contains only official defaults. Put personal server settings under an ignored local server directory such as `run/plugins/IPTMUTECHAT/config.yml`; runtime directories, logs, player IP records, mute data, ignore data, and `whitelist.yml` are excluded by `.gitignore` and must never be committed.

See [Configuration](docs/CONFIGURATION.md), [Updater](docs/UPDATER.md), [Privacy](docs/PRIVACY.md), and [Compatibility](docs/COMPATIBILITY.md).

## Build

```bash
./gradlew clean build
```

The localized artifacts are written to `build/libs/IPTMUTECHAT-<version>-en.us.jar` and `build/libs/IPTMUTECHAT-<version>-zh.cn.jar`. `build.gradle` is the version source of truth; processed `plugin.yml` and both JAR filenames must match it.

## Open Source

IPTMUTECHAT is released under the [MIT License](LICENSE). Contributions are governed by [CONTRIBUTING.md](CONTRIBUTING.md) and the [Code of Conduct](CODE_OF_CONDUCT.md). Security issues should follow [SECURITY.md](SECURITY.md).

---

# 中文说明

## 项目简介

IPTMUTECHAT 是面向 Paper 与 Folia 服务器的双语玩家审查与聊天管理插件。获得对应权限的管理人员可通过统一工具查询玩家最近记录的 IP 地址与使用相同 IP 的关联账号、强制在线玩家发送指定消息、忽略或恢复指定玩家的消息，以及执行永久或定时禁言。OP 还可使用 `/iphide` 隐藏自己的 IP 信息与同 IP 账号关联；通过指令管理的保护白名单可允许受信任玩家查询隐藏记录并免疫本插件禁言。

敏感 IP 数据与保护白名单记录只保存在服务器本地，不会发送至 GitHub。IP 查询、强制发言、禁言管理、OP 隐私控制与白名单管理均受独立权限节点限制。

> 当前版本：**1.4.0** · 测试版本：**Paper/Folia 1.21.11** · 作者：**Lazyz**

## 功能

- 查询玩家最近记录的 IP 与同 IP 账号。
- 获得权限的 OP 可使用 `/iphide`，在查询结果中隐藏自己的 IP 信息和同 IP 账号关联。
- 可通过指令管理持久化保护白名单；名单成员能查询隐藏的 IP/同 IP 记录，并免疫本插件禁言。
- 获得权限的管理人员可强制在线玩家发送指定聊天消息。
- 玩家可通过个人屏蔽列表忽略或恢复指定玩家的消息。
- 支持永久/定时禁言、原因记录及禁言状态查询。
- 支持私聊快速回复与可配置聊天冷却。
- 支持简体中文 `zh_CN` 与英文 `en_US` 运行模式。
- 游戏内插件文本统一以粗体显示，面板内容按星标像素居中且分割线星标统一为黄色；Help 身份栏居中显示，插件名、版本、作者使用不同颜色，指令为白色、说明为灰色，栏目标题使用单一颜色。
- 启动时检查更新，可直接下载并校验 SHA-256 与 JAR 身份；失败时提供手动下载地址。
- 提供自动构建、自动 Release、中英文默认 JAR、官方更新日志及 GitHub SHA-256 摘要。

## 版本限制

| 项目 | 要求 |
| --- | --- |
| 服务端 | Paper 或 Folia 1.20.1 至 1.21.11 |
| 已测试版本 | Paper/Folia 1.21.11 |
| 插件字节码 | Java 17 或更高版本 |
| 新版 Minecraft | 使用服务端要求的 Java；1.20.5+ 通常需要 Java 21 |
| 未测试环境 | 纯 CraftBukkit/Spigot 环境 |

## 安装

1. 从[最新 Release](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest)选择下载：英文默认包 `IPTMUTECHAT-<版本>-en.us.jar`，或简体中文默认包 `IPTMUTECHAT-<版本>-zh.cn.jar`。
2. 停止服务器，将 JAR 放入 `plugins/`。
3. 启动一次服务器，生成 `plugins/IPTMUTECHAT/config.yml`。
4. 授予 `/ipinfo` 权限前，请先阅读隐私说明。

两个包包含完全相同的插件代码、资源与配置注释，仅官方默认 `language` 参数不同。任一包仍可在 `config.yml` 中切换 `en_US` 与 `zh_CN`，然后执行 `/chatreload`。

Release 资源文件名不可更改：每个 Release 的自定义资源必须恰好为 `IPTMUTECHAT-<版本>-en.us.jar` 与 `IPTMUTECHAT-<版本>-zh.cn.jar`。发布工作流会拒绝任何改名或额外资源。

## 配置隔离

仓库中的 [`src/main/resources/config.yml`](src/main/resources/config.yml) 只保存官方默认参数。个人服务端配置应放在已忽略的运行目录，例如 `run/plugins/IPTMUTECHAT/config.yml`。运行数据、日志、玩家 IP、禁言、屏蔽与 `whitelist.yml` 白名单数据均已排除，不应提交到 GitHub。

完整说明请查看[配置](docs/CONFIGURATION.md)、[指令与权限](docs/COMMANDS.md)、[更新器](docs/UPDATER.md)、[隐私](docs/PRIVACY.md)与[兼容性](docs/COMPATIBILITY.md)。
