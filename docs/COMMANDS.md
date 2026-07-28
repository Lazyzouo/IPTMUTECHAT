# Commands and Permissions

| Command | Permission | Default | Notes |
| --- | --- | --- | --- |
| `/ipinfo <player>` | `iptmutechat.ipinfo` | OP | Hidden players are not disclosed |
| `/iphide` | `iptmutechat.iphide` | OP | Player-only privacy toggle |
| `/mute` | `iptmutechat.mute` | OP | Duration examples: `30s`, `5m`, `1h`, `7d`, `permanent` |
| `/unmute` | `iptmutechat.unmute` | OP | Online target required |
| `/muteinfo` | `iptmutechat.muteinfo` | OP | Online target required |
| `/ignore` | `iptmutechat.ignore` | Everyone | `iptmutechat.ignore.exempt` prevents being ignored |
| `/ignorelist` | `iptmutechat.ignore` | Everyone | Shows the caller's list |
| `/forcesay` | `iptmutechat.forcesay` | OP | Online target required |
| `/reply`, `/r` | None | Everyone | Uses the latest tracked private-message target |
| `/chatreload` | `iptmutechat.admin` | OP | Reloads configuration/language |
| `/chathelp` | None | Everyone | Filters entries by permission |

Bypass permissions: `iptmutechat.bypass.muted` and `iptmutechat.bypass.cooldown`, both OP by default.

---

# 指令与权限

| 指令 | 权限 | 默认 | 说明 |
| --- | --- | --- | --- |
| `/ipinfo <玩家>` | `iptmutechat.ipinfo` | OP | 不显示已隐藏玩家 |
| `/iphide` | `iptmutechat.iphide` | OP | 仅玩家可用的隐私开关 |
| `/mute` | `iptmutechat.mute` | OP | 时长示例：`30s`、`5m`、`1h`、`7d`、`permanent` |
| `/unmute` | `iptmutechat.unmute` | OP | 目标需在线 |
| `/muteinfo` | `iptmutechat.muteinfo` | OP | 目标需在线 |
| `/ignore` | `iptmutechat.ignore` | 所有人 | `iptmutechat.ignore.exempt` 可防止被屏蔽 |
| `/ignorelist` | `iptmutechat.ignore` | 所有人 | 显示自己的屏蔽列表 |
| `/forcesay` | `iptmutechat.forcesay` | OP | 目标需在线 |
| `/reply`、`/r` | 无 | 所有人 | 回复最后记录的私聊目标 |
| `/chatreload` | `iptmutechat.admin` | OP | 重载配置与语言 |
| `/chathelp` | 无 | 所有人 | 按权限过滤帮助内容 |

绕过权限：`iptmutechat.bypass.muted` 与 `iptmutechat.bypass.cooldown`，默认均为 OP。
