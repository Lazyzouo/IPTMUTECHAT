# Configuration

## Official defaults

| Key | Default | Description |
| --- | --- | --- |
| `language` | `zh_CN` | Runtime language; supported values are `zh_CN` and `en_US` |
| `style.ip-info-gradient-start` | `#22D3EE` | `/ipinfo` gradient start |
| `style.ip-info-gradient-end` | `#3B82F6` | `/ipinfo` gradient end |
| `chat.cooldown-seconds` | `1` | Chat cooldown in seconds |
| `chat.prefix` | Official gradient prefix | Shared plugin message prefix |
| `updater.enabled` | `true` | Check the official GitHub Release on startup |
| `updater.auto-download` | `true` | Download a verified update into the server update folder |
| `updater.connect-timeout-seconds` | `10` | GitHub connection timeout |
| `updater.read-timeout-seconds` | `30` | GitHub response/download timeout |

`messages` contains the official Simplified Chinese notifications and formatting. English messages are stored in `languages/en_US.yml`, created automatically on first use, and may be customized independently.

## Automatic configuration migration

On every startup and `/chatreload`, the plugin loads the newest bundled template, overlays all existing parameter values, customized messages, and custom keys, then writes the merged YAML atomically. New official keys, ordering, and comments appear automatically; explicitly obsolete built-in keys are removed. No existing configuration file needs to be deleted.

Before a schema upgrade, the plugin creates `config.yml.v<old-schema>.bak` or `en_US.yml.v<old-schema>.bak` beside the original file. If existing YAML cannot be parsed, it is not overwritten. Correct the reported YAML error or restore the backup, then reload.

The English-default and Chinese-default Release JARs preserve the same compiled code, embedded resources, configuration comments, and official parameter values. Their only difference is the embedded `language` default (`en_US` or `zh_CN`). The source English preset remains available in `presets/config.en_US.yml`; it is not published as a separate Release asset.

Run `/chatreload` after changing language, style, chat, updater, or message settings. Updater HTTP client timeouts are applied on the next server restart.

## Keep personal settings private

Do not edit source defaults for a live server. Keep local server files below `run/`, `server/`, or another ignored runtime directory. Never commit `ip_records.yml`, `similar_ips.yml`, `muted_players.yml`, `ignore_list.yml`, or `whitelist.yml` because they can contain personal or moderation data.

---

# 配置说明

## 官方默认参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `language` | `zh_CN` | 运行语言，可用值为 `zh_CN` 与 `en_US` |
| `style.ip-info-gradient-start` | `#22D3EE` | `/ipinfo` 渐变起始色 |
| `style.ip-info-gradient-end` | `#3B82F6` | `/ipinfo` 渐变结束色 |
| `chat.cooldown-seconds` | `1` | 发言冷却秒数 |
| `chat.prefix` | 官方渐变前缀 | 插件消息统一前缀 |
| `updater.enabled` | `true` | 启动时检查官方 GitHub Release |
| `updater.auto-download` | `true` | 下载通过校验的更新到服务端更新目录 |
| `updater.connect-timeout-seconds` | `10` | GitHub 连接超时 |
| `updater.read-timeout-seconds` | `30` | GitHub 响应/下载超时 |

`messages` 保存官方简体中文通知与样式；英文消息位于 `languages/en_US.yml`，首次使用时自动创建，可单独修改。

## 自动配置迁移

每次启动及执行 `/chatreload` 时，插件都会载入最新内置模板，回填现有的全部参数值、自定义消息与自定义键，再通过原子替换写入合并后的 YAML。新增官方键、排序与注释会自动出现，明确废弃的内置键会被移除，无需删除任何现有配置文件。

架构升级前，插件会在原文件旁创建 `config.yml.v<旧架构>.bak` 或 `en_US.yml.v<旧架构>.bak`。如果现有 YAML 无法解析，插件不会覆盖它；请根据后台错误修正 YAML 或恢复备份后重新加载。

Release 中的英文默认包与中文默认包保留相同的编译代码、内嵌资源、配置注释及官方参数值，唯一差异是内嵌的 `language` 默认值（`en_US` 或 `zh_CN`）。源码中的英文预设仍保留在 `presets/config.en_US.yml`，但不再作为独立 Release 资源发布。

修改语言、样式、聊天、更新器或消息后执行 `/chatreload`。更新器网络超时参数在下次服务器重启后生效。

请勿将源码默认配置直接当作个人服配置。个人配置应放在 `run/`、`server/` 或其他已忽略运行目录；严禁提交包含个人或管理数据的 IP、禁言、屏蔽及 `whitelist.yml` 白名单数据文件。
