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

`messages` contains the original Simplified Chinese notifications and formatting. English messages are stored in `languages/en_US.yml`, extracted automatically on first use, and may be customized independently.

Run `/chatreload` after changing language, style, chat, updater, or message settings. Updater HTTP client timeouts are applied on the next server restart.

## Keep personal settings private

Do not edit source defaults for a live server. Keep local server files below `run/`, `server/`, or another ignored runtime directory. Never commit `ip_records.yml`, `similar_ips.yml`, `muted_players.yml`, or `ignore_list.yml` because they can contain personal or moderation data.

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

`messages` 原封保留简体中文通知与样式；英文消息位于 `languages/en_US.yml`，首次使用时自动释放，可单独修改。

修改语言、样式、聊天、更新器或消息后执行 `/chatreload`。更新器网络超时参数在下次服务器重启后生效。

请勿将源码默认配置直接当作个人服配置。个人配置应放在 `run/`、`server/` 或其他已忽略运行目录；严禁提交包含个人或管理数据的 IP、禁言及屏蔽数据文件。
