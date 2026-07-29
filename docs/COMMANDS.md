# Commands and Permissions

| Command | Permission | Default | Notes |
| --- | --- | --- | --- |
| `/ipinfo <player>` | `iptmutechat.ipinfo` | OP | Hidden players are disclosed only to protection-whitelist members |
| `/iphide` | `iptmutechat.iphide` | OP | Player-only privacy toggle |
| `/mute` | `iptmutechat.mute` | OP | Duration examples: `30s`, `5m`, `1h`, `7d`, `permanent` |
| `/unmute` | `iptmutechat.unmute` | OP | Online target required |
| `/muteinfo` | `iptmutechat.muteinfo` | OP | Online target required |
| `/ignore` | `iptmutechat.ignore` | Everyone | `iptmutechat.ignore.exempt` prevents being ignored |
| `/ignorelist` | `iptmutechat.ignore` | Everyone | Shows the caller's list |
| `/forcesay` | `iptmutechat.forcesay` | OP | Online target required |
| `/reply`, `/r` | None | Everyone | Uses the latest tracked private-message target |
| `/iptmutechat whitelist add|remove|list [player]` | `iptmutechat.whitelist` | OP | Manages trusted hidden-IP viewers with mute immunity |
| `/chatreload` | `iptmutechat.admin` | OP | Reloads configuration/language |
| `/chathelp` | None | Everyone | Filters entries by permission |

Protection-whitelist membership grants `/ipinfo` access, reveals records hidden by `/iphide`, includes hidden same-IP accounts in results, and makes the member immune to plugin mutes. Adding a member removes any existing plugin mute; later mute attempts are rejected. The whitelist is stored locally in `whitelist.yml`.

Bypass permissions: `iptmutechat.bypass.muted` and `iptmutechat.bypass.cooldown`, both OP by default.

---

# 指令与权限

| 指令 | 权限 | 默认 | 说明 |
| --- | --- | --- | --- |
| `/ipinfo <玩家>` | `iptmutechat.ipinfo` | OP | 仅保护白名单成员可查看隐藏玩家 |
| `/iphide` | `iptmutechat.iphide` | OP | 仅玩家可用的隐私开关 |
| `/mute` | `iptmutechat.mute` | OP | 时长示例：`30s`、`5m`、`1h`、`7d`、`permanent` |
| `/unmute` | `iptmutechat.unmute` | OP | 目标需在线 |
| `/muteinfo` | `iptmutechat.muteinfo` | OP | 目标需在线 |
| `/ignore` | `iptmutechat.ignore` | 所有人 | `iptmutechat.ignore.exempt` 可防止被屏蔽 |
| `/ignorelist` | `iptmutechat.ignore` | 所有人 | 显示自己的屏蔽列表 |
| `/forcesay` | `iptmutechat.forcesay` | OP | 目标需在线 |
| `/reply`、`/r` | 无 | 所有人 | 回复最后记录的私聊目标 |
| `/iptmutechat whitelist add|remove|list [玩家]` | `iptmutechat.whitelist` | OP | 管理可查看隐藏 IP 且免疫禁言的受信任玩家 |
| `/chatreload` | `iptmutechat.admin` | OP | 重载配置与语言 |
| `/chathelp` | 无 | 所有人 | 按权限过滤帮助内容 |

保护白名单成员无需额外 `iptmutechat.ipinfo` 权限即可使用 `/ipinfo`，可查看被 `/iphide` 隐藏的记录和同 IP 账号，并免疫本插件禁言。加入名单时会自动解除已有的本插件禁言，后续禁言请求会被拒绝。名单仅保存在服务器本地的 `whitelist.yml`。

绕过权限：`iptmutechat.bypass.muted` 与 `iptmutechat.bypass.cooldown`，默认均为 OP。
