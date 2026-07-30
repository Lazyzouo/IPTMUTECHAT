# Changelog

All notable changes follow [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and Semantic Versioning.

## [1.4.4] - 2026-07-30

### Changed

- Reduced the startup banner from 88 to 62 columns, matching the requested 60-character border layout.
- Matched the reference field spacing with a 14-column bilingual label area, spaced colons, a dashed inner divider, and a single unlabelled open-source statement row.
- Removed the chevron from banner-line prefixes while retaining it for normal console notices, and removed the duplicate chevron from the startup-success line.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 将启动横幅由 88 列缩短至 62 列，匹配指定的 60 个填充字符边框布局。
- 按参考格式使用 14 列双语标签、两侧留空格的冒号、内部短横分隔线及无冒号的单行开源声明。
- 横幅行的插件前缀不再附带箭头，普通控制台通知继续保留箭头，并移除启动成功行中可能重复的箭头。

## [1.4.3] - 2026-07-30

### Changed

- Reworked the startup banner into a wide management-service layout with centered bilingual headings and full cyan borders matching the requested reference style.
- Reordered the detail rows to Version, Author, Tested, Language, GitHub, and Open source; removed the separate Core row and applied semantic green, yellow, aqua, blue, and white values.
- Retained the colored plugin console prefix and display-width-aware closing border on every banner line.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 参考指定样式，将启动横幅改为宽版管理服务布局，使用居中的双语标题与完整青色边框。
- 详情栏调整为版本、作者、测试、语言、GitHub 与开源声明，移除独立核心栏，并分别应用绿色、黄色、青色、蓝色与白色数值。
- 保留彩色插件控制台前缀，并继续按显示宽度为每一行生成闭合右边框。

## [1.4.2] - 2026-07-30

### Fixed

- Added display-width-aware right padding and a closing border to every startup banner detail row.
- Widened the detail label column so the bilingual open-source label aligns with the other fields without increasing the compact banner width.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 为启动横幅的每一条详情栏增加按显示宽度计算的右侧补白及闭合边框。
- 加宽详情标签栏，使双语开源标签与其他字段整齐对齐，同时保持现有紧凑横幅宽度不变。

## [1.4.1] - 2026-07-30

### Changed

- Reduced the startup banner width and shortened its title while retaining all plugin, platform, compatibility, repository, and privacy details.
- Replaced the native unstyled logger prefix on startup and updater notices with the plugin's bold purple/red console prefix so the complete prefix now renders in color.
- Standardized the Version, Author, Core, Tested, and Language rows in white; the single open-source privacy row now uses green while the GitHub row retains its link color.
- Redesigned every prefixed in-game notification with bold status markers, semantic colors, compact action titles, a consistent separator, neutral body text, and highlighted dynamic values while retaining the existing Help and panel layouts.
- Added scoped configuration and language migrations that refresh only notification templates while preserving server-owned language, updater, cooldown, prefix, and color parameters.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 缩小服务器启动横幅并精简标题，同时保留插件、核心、兼容版本、项目地址与隐私声明等完整信息。
- 启动横幅及更新器通知不再使用无法着色的原生日志前缀，现统一显示插件的紫红粗体彩色控制台前缀。
- 版本、作者、核心、测试版本与语言栏统一改为白色；开源隐私声明改为绿色，GitHub 栏保留链接色。
- 全面美化所有带插件前缀的游戏内通知，统一使用粗体状态符号、语义颜色、简洁操作标题、分隔符、灰色正文及醒目的动态值，同时保留 Help 与各类信息面板的现有布局。
- 新增限定于通知模板的中英文配置迁移，不会修改服主的语言、更新器、冷却、插件前缀或颜色参数。

## [1.4.0] - 2026-07-29

### Added

- Added a command-managed protection whitelist through `/iptmutechat whitelist add|remove|list [player]`, protected by the new `iptmutechat.whitelist` permission.
- Whitelist members can use `/ipinfo` without the normal lookup permission, inspect records hidden by `/iphide`, and see hidden accounts in same-IP results.
- Whitelist members are immune to plugin mutes. Adding a member clears any existing plugin mute, and later mute attempts are rejected with a localized notice.
- Added local UUID/name persistence in `whitelist.yml`; the file remains server-owned runtime data and is excluded from official defaults and publication.

### Changed

- Centered the Help plugin/version/author identity line on the separator's `✧` marker.
- Restyled Help commands in white and descriptions in gray, with distinct colors for plugin name, version, and author plus single-color player/administrator section labels.
- Standardized separator `✧` markers to bright yellow while preserving existing panel centering and bold player-facing text.
- Added scoped Chinese and English Help-style migration without changing personal language, updater, cooldown, prefix, or color parameters.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 新增可通过 `/iptmutechat whitelist add|remove|list [玩家]` 管理的保护白名单，并新增 `iptmutechat.whitelist` 权限。
- 白名单成员无需普通查询权限即可使用 `/ipinfo`，可查看被 `/iphide` 隐藏的记录及同 IP 隐藏账号。
- 白名单成员免疫本插件禁言；加入名单时会清除已有禁言，后续禁言操作会被拒绝并显示双语提示。
- 白名单 UUID 与玩家名仅保存在服务器本地的 `whitelist.yml`，不会进入官方参数或发布资源。
- Help 的插件名/版本/作者栏现按分隔线 `✧` 居中；插件名、版本与作者使用不同颜色。
- Help 指令统一为白色、作用说明统一为灰色，“玩家指令”和“管理指令”各自使用单一颜色。
- 所有分隔线 `✧` 星标统一为亮黄色，并保留现有面板居中与全局粗体效果。
- 新增限定于中英文 Help 文本的样式迁移，不会更改服主的语言、更新器、冷却、前缀或颜色参数。

## [1.3.1] - 2026-07-29

### Changed

- Standardized every player-facing plugin notification, Help line, and panel entry to render in bold through the shared in-game message renderer.
- Centered all content inside the mute-record, ignore-list, and IP-information panels on the `✧` marker in their separator lines; the Help menu intentionally retains its existing layout.
- Added pixel-aware width calculation for ASCII, CJK, Unicode, bold glyphs, and dynamic values so localized and personalized panel content remains centered.
- Applied centering at runtime without rewriting server-owned configuration or changing any official parameter values.

### Compatibility

- Supported: Paper/Folia 1.20.1-1.21.11.
- Tested: Paper/Folia 1.21.11.
- Plugin bytecode: Java 17.

### 中文更新摘要

- 通过统一的游戏内消息渲染层，确保所有玩家可见的插件通知、Help 文本与面板条目均以粗体显示。
- 禁言档案、屏蔽列表与 IP 查询面板内的全部内容现会基于分割线的 `✧` 星标居中；Help 菜单按要求保留原有布局。
- 新增针对 ASCII、中日韩字符、Unicode、粗体字符与动态变量的像素宽度计算，确保中英文及个性化面板内容保持居中。
- 居中处理在运行时完成，不会重写服主个人配置，也不会更改任何官方参数值。

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

[1.4.0]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.4.0
[1.3.1]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.3.1
[1.3.0]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.3.0
[1.2.3]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.3
[1.2.2]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.2
[1.2.1]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.1
[1.2.0]: https://github.com/Lazyzouo/IPTMUTECHAT/releases/tag/v1.2.0
