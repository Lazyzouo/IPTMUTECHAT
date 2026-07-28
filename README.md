# IPTMUTECHAT

[![Release](https://img.shields.io/github/v/release/Lazyzouo/IPTMUTECHAT?display_name=tag&sort=semver)](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest)
[![Build](https://github.com/Lazyzouo/IPTMUTECHAT/actions/workflows/build.yml/badge.svg)](https://github.com/Lazyzouo/IPTMUTECHAT/actions/workflows/build.yml)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk)](https://adoptium.net/)
[![Paper/Folia](https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.1--1.21.11-2C2C2C)](https://papermc.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-22C55E.svg)](LICENSE)

IPTMUTECHAT is a bilingual administration plugin for Paper and Folia servers. It combines player IP history and same-IP account correlation with practical chat moderation, privacy controls, persistent mute/ignore data, and a verified GitHub updater.

> Current release: **1.2.0** · Tested server version: **Paper/Folia 1.21.11** · Author: **Lazyz**

## Features

- Query a player's latest recorded IP and same-IP accounts.
- Let authorized operators hide their own IP record with `/iphide`.
- Permanent and timed mutes with reasons and status inspection.
- Per-player ignore lists, reply tracking, chat cooldowns, and forced chat.
- Simplified Chinese (`zh_CN`) and English (`en_US`) runtime modes.
- Startup update checks with direct download, SHA-256 verification, JAR identity validation, and a manual fallback link.
- Folia-aware plugin metadata and persistent YAML storage.
- Automated GitHub builds and versioned Releases with release notes, checksums, and an English preset.

## Requirements

| Component | Requirement |
| --- | --- |
| Server | Paper or Folia 1.20.1 through 1.21.11 |
| Tested | Paper/Folia 1.21.11 |
| Plugin bytecode | Java 17 or newer |
| Modern Minecraft | Use the Java version required by your server; 1.20.5+ normally requires Java 21 |
| Unsupported | CraftBukkit/Spigot-only environments are not tested |

## Install

1. Download `IPTMUTECHAT-<version>.jar` from [the latest Release](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest).
2. Stop the server and place the JAR in `plugins/`.
3. Start the server once to create `plugins/IPTMUTECHAT/config.yml`.
4. Review the privacy notice and permissions before granting `/ipinfo` access.

For English, set `language: en_US` and run `/chatreload`, or download the official `config.en_US.yml` Release asset, rename it to `config.yml`, and place it in `plugins/IPTMUTECHAT/` while the server is stopped.

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
| `/chatreload` | OP | Reload configuration and language resources |
| `/chathelp` | Everyone | Show permission-aware help |

Full permission details are in [Commands and permissions](docs/COMMANDS.md).

## Configuration Safety

The tracked [`src/main/resources/config.yml`](src/main/resources/config.yml) contains only official defaults. Put personal server settings under an ignored local server directory such as `run/plugins/IPTMUTECHAT/config.yml`; runtime directories, logs, player IP records, mute data, and ignore data are excluded by `.gitignore` and must never be committed.

See [Configuration](docs/CONFIGURATION.md), [Updater](docs/UPDATER.md), [Privacy](docs/PRIVACY.md), and [Compatibility](docs/COMPATIBILITY.md).

## Build

```bash
./gradlew clean build
```

The artifact is written to `build/libs/IPTMUTECHAT-<version>.jar`. `build.gradle` is the version source of truth; processed `plugin.yml` and the JAR filename must match it.

## Open Source

IPTMUTECHAT is released under the [MIT License](LICENSE). Contributions are governed by [CONTRIBUTING.md](CONTRIBUTING.md) and the [Code of Conduct](CODE_OF_CONDUCT.md). Security issues should follow [SECURITY.md](SECURITY.md).

---

# 中文说明

IPTMUTECHAT 是面向 Paper 与 Folia 服务器的双语管理插件，集成玩家 IP 历史与同 IP 账号关联、聊天管理、隐私控制、持久化禁言/屏蔽数据，以及带完整性校验的 GitHub 自动更新器。

> 当前版本：**1.2.0** · 测试版本：**Paper/Folia 1.21.11** · 作者：**Lazyz**

## 功能

- 查询玩家最近记录的 IP 与同 IP 账号。
- 获得权限的 OP 可使用 `/iphide` 隐藏自己的 IP 档案。
- 支持永久/定时禁言、原因记录及禁言状态查询。
- 支持玩家屏蔽列表、快速回复、聊天冷却与强制发言。
- 支持简体中文 `zh_CN` 与英文 `en_US` 运行模式。
- 启动时检查更新，可直接下载并校验 SHA-256 与 JAR 身份；失败时提供手动下载地址。
- 提供自动构建、自动 Release、更新日志、校验文件及英文预设。

## 版本限制

| 项目 | 要求 |
| --- | --- |
| 服务端 | Paper 或 Folia 1.20.1 至 1.21.11 |
| 已测试版本 | Paper/Folia 1.21.11 |
| 插件字节码 | Java 17 或更高版本 |
| 新版 Minecraft | 使用服务端要求的 Java；1.20.5+ 通常需要 Java 21 |
| 未测试环境 | 纯 CraftBukkit/Spigot 环境 |

## 安装

1. 从[最新 Release](https://github.com/Lazyzouo/IPTMUTECHAT/releases/latest)下载 `IPTMUTECHAT-<版本>.jar`。
2. 停止服务器，将 JAR 放入 `plugins/`。
3. 启动一次服务器，生成 `plugins/IPTMUTECHAT/config.yml`。
4. 授予 `/ipinfo` 权限前，请先阅读隐私说明。

英文模式可将 `language` 改为 `en_US` 后执行 `/chatreload`；也可下载 Release 中的官方 `config.en_US.yml`，停服后改名为 `config.yml` 并放入插件目录。

## 配置隔离

仓库中的 [`src/main/resources/config.yml`](src/main/resources/config.yml) 只保存官方默认参数。个人服务端配置应放在已忽略的运行目录，例如 `run/plugins/IPTMUTECHAT/config.yml`。运行数据、日志、玩家 IP、禁言和屏蔽数据均已排除，不应提交到 GitHub。

完整说明请查看[配置](docs/CONFIGURATION.md)、[指令与权限](docs/COMMANDS.md)、[更新器](docs/UPDATER.md)、[隐私](docs/PRIVACY.md)与[兼容性](docs/COMPATIBILITY.md)。
