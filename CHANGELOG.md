# Changelog

All notable changes follow [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and Semantic Versioning.

## [1.3.0] - 2026-07-29

### Added

- Added a bilingual, colorized startup banner with plugin version, author, active server core, tested version, language, repository, and open-source privacy status.
- Added semantic console colors for update checking, latest-version confirmation, update availability, successful download, restart instructions, manual fallback, and failures while retaining the existing message text.

### Changed

- Official localized packages now follow the immutable `IPTMUTECHAT-<version>-<language>.jar` contract, producing `IPTMUTECHAT-1.3.0-en.us.jar` and `IPTMUTECHAT-1.3.0-zh.cn.jar` for this release.
- Release automation now fails unless exactly those two versioned localized assets are published with their original filenames.
- Plugin console output now uses Paper/Folia's component logger so the native `[IPTMUTECHAT]` prefix remains consistent while message content is colorized.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 新增中英双语彩色启动横幅，显示插件版本、作者、当前服务端核心、测试版本、语言、项目地址与开源隐私状态。
- 为更新检查、已是最新版、发现新版本、下载成功、重启提示、手动下载与更新失败等后台通知增加对应颜色，同时保留原有消息文本。
- 官方双语包强制使用不可更改的 `IPTMUTECHAT-<版本>-<语言>.jar` 命名；本次发布为 `IPTMUTECHAT-1.3.0-en.us.jar` 与 `IPTMUTECHAT-1.3.0-zh.cn.jar`。
- Release 自动化现会严格检查自定义资源必须恰好为上述两个原始文件名，任何改名或额外资源都会导致发布失败。

## [1.2.3] - 2026-07-29

### Changed

- Added a prominent bilingual open-source and data privacy statement to the top of the project README.
- Clarified that the plugin contains no telemetry, remote logging, data-collection endpoints, or hidden server-data retrieval mechanisms.
- Documented that plugin-generated records remain on the installed server and that update checks communicate only with public GitHub Releases over HTTPS.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 在项目 README 顶部新增醒目的中英双语开源与数据隐私声明。
- 明确插件不包含遥测、远程日志、数据采集端点或隐藏的服务器数据获取机制。
- 明确插件生成的记录仅保存在安装服务器本地，更新检查仅通过 HTTPS 与公开 GitHub Releases 通信。

## [1.2.2] - 2026-07-29

### Changed

- Release downloads now contain only `IPTMUTECHAT-<version>-en.us.jar` and `IPTMUTECHAT-<version>-zh.cn.jar` as custom assets.
- Both localized packages preserve identical compiled code, embedded resources, configuration comments, and official parameter values; only the default `language` parameter differs.
- The startup updater now selects the package matching the active language and verifies the SHA-256 digest supplied by GitHub for that asset.
- Historical `v1.2.0` and `v1.2.1` Releases were retired; published downloads begin with `v1.2.2` and use the two-package layout.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- Release 自定义下载资源现仅提供 `IPTMUTECHAT-<版本>-en.us.jar` 与 `IPTMUTECHAT-<版本>-zh.cn.jar`。
- 两个本地化包保留完全相同的编译代码、内嵌资源、配置注释与官方参数值，仅默认 `language` 参数不同。
- 启动更新器现会按照当前语言选择对应包，并校验 GitHub 为该资源提供的 SHA-256 摘要。
- 历史 `v1.2.0` 与 `v1.2.1` Release 已停止提供；发布下载从 `v1.2.2` 起统一使用双包格式。

## [1.2.1] - 2026-07-29

### Changed

- Reworked the official bilingual project overview to clearly describe player IP lookup, same-IP account correlation, forced chat, personal ignore lists, mute management, and OP privacy controls.
- Clarified that `/iphide` conceals an operator's IP information and same-IP associations from lookup results.
- Expanded the plugin and GitHub metadata descriptions while preserving English-first ordering.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 重写官方双语项目介绍，明确说明玩家 IP 查询、同 IP 账号关联、强制发言、消息屏蔽、禁言管理和 OP 隐私功能。
- 明确 `/iphide` 用于在查询结果中隐藏 OP 自身的 IP 信息与同 IP 账号关联。
- 完善插件与 GitHub 元数据描述，并继续保持英文在前、中文在后。

## [1.2.0] - 2026-07-29

### Added

- Full `en_US` runtime language mode and downloadable English preset.
- Verified GitHub startup updater with semantic version checks, SHA-256 validation, JAR identity checks, safe next-restart installation, and manual fallback URL.
- Bilingual startup banner with author, language, compatibility, tested version, and repository details.
- Automated CI builds and automatic GitHub Releases with official notes, checksums, JAR, and English preset assets.
- Complete bilingual project, configuration, compatibility, command, privacy, support, security, and contribution documentation.
- `/iphide` privacy toggle for authorized operators, including same-IP result filtering and persistent state.

### Changed

- Project author metadata is now `Lazyz`.
- All player-facing hardcoded notifications now use the selected language resource.
- GitHub-facing descriptions list English before Chinese.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 新增完整英文运行模式与可下载英文预设。
- 新增带语义版本、SHA-256、JAR 身份校验和手动回退地址的启动自动更新器。
- 新增双语美化启动横幅、GitHub 自动构建/Release 与全套双语项目文档。
- 新增 `/iphide` OP 隐私开关并持久化隐藏状态。
- 作者信息统一为 `Lazyz`，玩家可见硬编码文本全部改为语言资源。

[1.3.0]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.3.0
[1.2.3]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.3
[1.2.2]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.2
[1.2.1]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.1
[1.2.0]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.0
