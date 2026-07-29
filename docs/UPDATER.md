# Verified Updater

On every server start, the updater requests the latest stable Release from `Lazyzouo/IPTMUTECHAT`.

1. Semantic versions are compared with the running plugin version.
2. If current, the console explicitly reports that the version is up to date.
3. If newer and automatic download is enabled, the updater selects the exact `en.us.jar` or `zh.cn.jar` asset for the active language.
4. The asset's GitHub-provided SHA-256 digest, download size, archive readability, plugin name, and embedded version are verified.
5. The verified JAR is moved into the server update folder for installation on the next restart.
6. Any failure leaves the running plugin untouched and prints the official manual Release URL.

Only stable GitHub Releases are considered. The updater never executes downloaded code in the current server process and never deletes the active plugin JAR.

---

# 安全更新器

服务器每次启动时，更新器会查询 `Lazyzouo/IPTMUTECHAT` 的最新稳定 Release。

1. 比较当前插件与 Release 的语义化版本。
2. 已是最新版时，在后台明确提示。
3. 有新版本且启用自动下载时，更新器会按照当前语言选择精确版本的 `en.us.jar` 或 `zh.cn.jar` 资源。
4. 校验 GitHub 为资源提供的 SHA-256 摘要、下载大小、压缩包可读性、插件名称与内嵌版本。
5. 将通过校验的 JAR 移入服务端更新目录，在下次重启时安装。
6. 任一步失败都不会修改当前插件，并会输出官方 Release 手动下载地址。

更新器只接受稳定 Release，不会在当前服务器进程中执行新下载代码，也不会删除正在运行的插件 JAR。
