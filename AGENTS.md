# Project Instructions

## Versioning

- For every user-requested plugin code, behavior, or configuration update, increment the `version` in `build.gradle` before building.
- Use semantic versioning (`MAJOR.MINOR.PATCH`):
  - Increment `PATCH` for bug fixes, text changes, styling changes, and other small backward-compatible updates.
  - Increment `MINOR` for substantial backward-compatible features or major feature expansions.
  - Increment `MAJOR` for breaking changes, incompatible configuration/data changes, or major redesigns.
- Treat `build.gradle` as the single source of truth. The resource-processing task must continue to populate the same version in `plugin.yml`, and the built JAR filename must contain that version.
- After changing the version, run a full build and verify both the processed `plugin.yml` version and the generated JAR filename.

## Publishing

- For every plugin release, update `CHANGELOG.md` with English first and Chinese second.
- Never commit runtime server folders, personal configuration, logs, credentials, IP records, mute data, or ignore data.
- After a successful full build and version verification, commit and push the completed update to `origin/main` when the GitHub remote is available.
- The GitHub Release workflow is responsible for creating the version tag, official bilingual Release notes, GitHub-provided SHA-256 digests, and the two localized JAR assets.

## Immutable Release Asset Contract

- Every GitHub Release must contain exactly two custom assets named `IPTMUTECHAT-<version>-en.us.jar` and `IPTMUTECHAT-<version>-zh.cn.jar`.
- These filenames are immutable. Never rename either file before upload, during publication, after publication, or in maintenance automation.
- Do not publish aliases, unversioned JARs, generic names, checksums, configuration presets, or any other custom Release assets.
- The Release workflow must fail when either required filename is missing, changed, duplicated, or accompanied by an extra custom asset.
- GitHub's automatically generated source archives are not custom Release assets and remain available through the repository.
