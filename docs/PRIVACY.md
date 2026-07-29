# Privacy Notice

IPTMUTECHAT records player names, IP addresses, and timestamps to provide `/ipinfo` and same-IP account correlation. Server owners are responsible for lawful use, access control, retention, disclosure, and deletion under their local rules and regulations.

- Keep `iptmutechat.ipinfo` restricted to trusted administrators.
- Do not publish the plugin data directory or support archives containing IP records.
- `/iphide` controls command output; it does not erase historical storage.
- Protection-whitelist members can inspect hidden IP and same-IP records. Grant `iptmutechat.whitelist` only to trusted administrators.
- `whitelist.yml` stores protected player UUIDs and names locally and is never uploaded by the plugin.
- Stop the server before securely removing records from the data files.
- Backups may continue to contain previously recorded personal data.

No player IP or moderation data is sent to GitHub. The updater sends only a normal anonymous HTTPS request containing the plugin version in its User-Agent.

---

# 隐私说明

IPTMUTECHAT 会记录玩家名称、IP 地址与时间戳，用于 `/ipinfo` 和同 IP 账号关联。服主有责任根据所在地规则和法规合法使用、控制访问、设置保留期限、披露及删除这些数据。

- 仅向可信管理员授予 `iptmutechat.ipinfo`。
- 不要公开插件数据目录或包含 IP 记录的支持压缩包。
- `/iphide` 只控制指令输出，不会删除历史存储。
- 保护白名单成员可以查看隐藏的 IP 与同 IP 记录；请仅向可信管理员授予 `iptmutechat.whitelist`。
- `whitelist.yml` 只在服务器本地保存受保护玩家的 UUID 与名称，插件不会上传该文件。
- 如需删除记录，请先停止服务器，再安全处理数据文件。
- 备份中可能仍包含之前记录的个人数据。

插件不会向 GitHub 发送玩家 IP 或管理数据。更新器只发出普通匿名 HTTPS 请求，并在 User-Agent 中包含插件版本。
