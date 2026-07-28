# Contributing

Thank you for improving IPTMUTECHAT.

1. Open or reference an issue for behavior changes.
2. Fork the repository and branch from `main`.
3. Keep changes focused and preserve Paper/Folia compatibility.
4. Add/update both `zh_CN` and `en_US` messages for every player-facing text change.
5. Update documentation and `CHANGELOG.md` when behavior changes.
6. Follow Semantic Versioning in `build.gradle`; it is the single version source.
7. Run `./gradlew clean build` and verify the processed `plugin.yml` version and JAR filename.
8. Submit a pull request using the provided template.

Maintainers may run `tools/publish.ps1` after completing the checklist. It performs the full build and version checks, commits the release, and pushes `main`; GitHub Actions then creates the tag and official Release automatically.

Do not include server IP records, player data, credentials, logs, personal configurations, or built JARs in a pull request.

---

# 贡献指南

感谢参与改进 IPTMUTECHAT。

1. 行为变更应先创建或关联 Issue。
2. Fork 仓库并从 `main` 创建分支。
3. 保持修改范围清晰，并维持 Paper/Folia 兼容性。
4. 所有玩家可见文本都必须同步更新 `zh_CN` 与 `en_US`。
5. 行为变化时同步更新文档和 `CHANGELOG.md`。
6. 按语义化版本更新 `build.gradle`；它是唯一版本来源。
7. 执行 `./gradlew clean build`，核对处理后的 `plugin.yml` 与 JAR 文件名版本。
8. 使用仓库模板提交 Pull Request。

维护者完成检查后可运行 `tools/publish.ps1`。脚本会执行完整构建与版本核对、提交并推送 `main`，随后由 GitHub Actions 自动创建标签与官方 Release。

禁止提交服务器 IP 记录、玩家数据、凭据、日志、个人配置或构建产物。
