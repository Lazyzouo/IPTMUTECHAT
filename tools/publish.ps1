param(
    [string]$CommitMessage
)

$ErrorActionPreference = 'Stop'
$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
Set-Location $projectRoot

$versionLine = Select-String -LiteralPath 'build.gradle' -Pattern "^version = '([^']+)'$"
if (-not $versionLine) {
    throw 'Unable to read the version from build.gradle.'
}
$version = $versionLine.Matches[0].Groups[1].Value
if (-not (Select-String -LiteralPath 'CHANGELOG.md' -SimpleMatch "## [$version]" -Quiet)) {
    throw "CHANGELOG.md does not contain a $version release section."
}

$env:GRADLE_USER_HOME = (Resolve-Path '.gradle').Path
& .\gradlew.bat clean build --no-daemon --console=plain
if ($LASTEXITCODE -ne 0) {
    throw 'Gradle build failed.'
}

$processedVersion = (Select-String -LiteralPath 'build/resources/main/plugin.yml' -Pattern "^version: '([^']+)'$").Matches[0].Groups[1].Value
$englishJarPath = "build/libs/IPTMUTECHAT-$version-en.us.jar"
$chineseJarPath = "build/libs/IPTMUTECHAT-$version-zh.cn.jar"
if ($processedVersion -ne $version `
        -or -not (Test-Path -LiteralPath $englishJarPath) `
        -or -not (Test-Path -LiteralPath $chineseJarPath)) {
    throw 'Version verification failed.'
}

git add --all
git diff --cached --quiet
if ($LASTEXITCODE -eq 0) {
    throw 'There are no changes to publish.'
}

if ([string]::IsNullOrWhiteSpace($CommitMessage)) {
    $CommitMessage = "release: IPTMUTECHAT v$version"
}
git commit -m $CommitMessage
if ($LASTEXITCODE -ne 0) {
    throw 'Git commit failed.'
}
git push origin main
if ($LASTEXITCODE -ne 0) {
    throw 'Git push failed.'
}

Write-Host "Published IPTMUTECHAT v$version. GitHub Actions will create the Release."
