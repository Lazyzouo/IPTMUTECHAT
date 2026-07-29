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

`messages` contains the official Simplified Chinese notifications and formatting. English messages are stored in `languages/en_US.yml`, extracted automatically on first use, and may be customized independently. Help styles are migrated by message schema version without changing personal runtime parameters.

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

`messages` 保存官方简体中文通知与样式；英文消息位于 `languages/en_US.yml`，首次使用时自动释放，可单独修改。Help 样式按消息结构版本迁移，不会更改服主的个人运行参数。

Release 中的英文默认包与中文默认包保留相同的编译代码、内嵌资源、配置注释及官方参数值，唯一差异是内嵌的 `language` 默认值（`en_US` 或 `zh_CN`）。源码中的英文预设仍保留在 `presets/config.en_US.yml`，但不再作为独立 Release 资源发布。

修改语言、样式、聊天、更新器或消息后执行 `/chatreload`。更新器网络超时参数在下次服务器重启后生效。

请勿将源码默认配置直接当作个人服配置。个人配置应放在 `run/`、`server/` 或其他已忽略运行目录；严禁提交包含个人或管理数据的 IP、禁言、屏蔽及 `whitelist.yml` 白名单数据文件。
