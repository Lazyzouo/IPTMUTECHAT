package com.icu.iptmutechat.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.icu.iptmutechat.IPTMUTECHAT;
import com.icu.iptmutechat.config.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Locale;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class GitHubUpdateManager {

    public static final String REPOSITORY_URL = "https://github.com/Lazyzouo/IPTMUTECHAT";
    private static final URI LATEST_RELEASE_API = URI.create(
            "https://api.github.com/repos/Lazyzouo/IPTMUTECHAT/releases/latest");
    private static final long MAX_UPDATE_SIZE_BYTES = 50L * 1024L * 1024L;

    private final IPTMUTECHAT plugin;
    private final ConfigManager configManager;
    private final HttpClient httpClient;

    public GitHubUpdateManager(IPTMUTECHAT plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(configManager.getUpdateConnectTimeoutSeconds()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public void checkForUpdates() {
        if (!configManager.isUpdaterEnabled()) {
            plugin.getLogger().info(configManager.getRawMessage("updater-disabled"));
            return;
        }

        plugin.getLogger().info(configManager.getRawMessage("updater-checking"));
        HttpRequest request = request(LATEST_RELEASE_API);
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .orTimeout(configManager.getUpdateReadTimeoutSeconds(), TimeUnit.SECONDS)
                .thenApply(this::parseReleaseResponse)
                .thenAccept(this::processRelease)
                .exceptionally(error -> {
                    logFailure(unwrap(error));
                    return null;
                });
    }

    private ReleaseInfo parseReleaseResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new CompletionException(new IOException(
                    "GitHub API returned HTTP " + response.statusCode()));
        }

        JsonObject release = JsonParser.parseString(response.body()).getAsJsonObject();
        String tag = release.get("tag_name").getAsString();
        String pageUrl = release.has("html_url")
                ? release.get("html_url").getAsString()
                : REPOSITORY_URL + "/releases/latest";
        JsonArray assets = release.getAsJsonArray("assets");
        return new ReleaseInfo(tag, pageUrl, assets);
    }

    private void processRelease(ReleaseInfo release) {
        SemanticVersion current = SemanticVersion.parse(plugin.getDescription().getVersion());
        SemanticVersion latest = SemanticVersion.parse(release.tag());
        if (latest.compareTo(current) <= 0) {
            plugin.getLogger().info(configManager.getRawMessage(
                    "updater-latest", "version", current.toString()));
            return;
        }

        plugin.getLogger().info(configManager.getRawMessage(
                "updater-available", "current", current.toString(), "latest", latest.toString()));
        if (!configManager.isAutoDownloadEnabled()) {
            plugin.getLogger().info(configManager.getRawMessage(
                    "updater-manual", "url", release.pageUrl()));
            return;
        }

        try {
            Path downloadedFile = downloadVerifiedUpdate(release, latest);
            plugin.getLogger().info(configManager.getRawMessage(
                    "updater-downloaded", "version", latest.toString(), "file", downloadedFile.toString()));
            plugin.getLogger().info(configManager.getRawMessage("updater-restart"));
        } catch (Exception e) {
            logFailure(e);
        }
    }

    private Path downloadVerifiedUpdate(ReleaseInfo release, SemanticVersion latest)
            throws IOException, InterruptedException, NoSuchAlgorithmException {
        String languageSuffix = "en_US".equalsIgnoreCase(configManager.getLanguage())
                ? "en.us"
                : "zh.cn";
        String jarName = "IPTMUTECHAT-" + latest + "-" + languageSuffix + ".jar";
        Asset jarAsset = findAsset(release.assets(), jarName);
        if (jarAsset == null) {
            throw new IOException("Release is missing the localized update asset: " + jarName);
        }

        String expectedHash = parseSha256Digest(jarAsset.digest());
        Path updateDirectory = plugin.getServer().getUpdateFolderFile().toPath();
        Files.createDirectories(updateDirectory);
        Path temporaryFile = updateDirectory.resolve(jarName + ".download");
        Path destination = updateDirectory.resolve(jarName);

        try {
            HttpResponse<Path> response = httpClient.send(
                    request(URI.create(jarAsset.url())), HttpResponse.BodyHandlers.ofFile(temporaryFile));
            if (response.statusCode() != 200) {
                throw new IOException("Update download returned HTTP " + response.statusCode());
            }
            long fileSize = Files.size(temporaryFile);
            if (fileSize <= 0 || fileSize > MAX_UPDATE_SIZE_BYTES) {
                throw new IOException("Downloaded update has an invalid size: " + fileSize);
            }

            String actualHash = sha256(temporaryFile);
            if (!MessageDigest.isEqual(
                    expectedHash.getBytes(StandardCharsets.US_ASCII),
                    actualHash.getBytes(StandardCharsets.US_ASCII))) {
                throw new IOException("SHA-256 verification failed");
            }
            verifyPluginJar(temporaryFile, latest.toString());
            moveIntoPlace(temporaryFile, destination);
            return destination;
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private String parseSha256Digest(String digest) throws IOException {
        if (digest == null || !digest.startsWith("sha256:")) {
            throw new IOException("Release asset does not provide a SHA-256 digest");
        }
        String checksum = digest.substring("sha256:".length()).toLowerCase(Locale.ROOT);
        if (!checksum.matches("[0-9a-f]{64}")) {
            throw new IOException("Release asset SHA-256 digest is invalid");
        }
        return checksum;
    }

    private Asset findAsset(JsonArray assets, String expectedName) {
        if (assets == null) return null;
        for (JsonElement element : assets) {
            JsonObject asset = element.getAsJsonObject();
            String name = asset.get("name").getAsString();
            if (name.equals(expectedName)) {
                String digest = asset.has("digest") && !asset.get("digest").isJsonNull()
                        ? asset.get("digest").getAsString()
                        : null;
                return new Asset(name, asset.get("browser_download_url").getAsString(), digest);
            }
        }
        return null;
    }

    private HttpRequest request(URI uri) {
        return HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(configManager.getUpdateReadTimeoutSeconds()))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "IPTMUTECHAT/" + plugin.getDescription().getVersion())
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();
    }

    private void verifyPluginJar(Path jarPath, String expectedVersion) throws IOException {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry pluginEntry = jar.getJarEntry("plugin.yml");
            if (pluginEntry == null) {
                throw new IOException("Downloaded JAR does not contain plugin.yml");
            }
            try (InputStreamReader reader = new InputStreamReader(
                    jar.getInputStream(pluginEntry), StandardCharsets.UTF_8)) {
                YamlConfiguration pluginDescription = YamlConfiguration.loadConfiguration(reader);
                if (!"IPTMUTECHAT".equals(pluginDescription.getString("name"))
                        || !expectedVersion.equals(pluginDescription.getString("version"))) {
                    throw new IOException("Downloaded JAR identity or version is invalid");
                }
            }
        }
    }

    private String sha256(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (var input = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                digest.update(buffer, 0, read);
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private void moveIntoPlace(Path source, Path destination) throws IOException {
        try {
            Files.move(source, destination,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException ignored) {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Throwable unwrap(Throwable error) {
        return error instanceof CompletionException && error.getCause() != null
                ? error.getCause()
                : error;
    }

    private void logFailure(Throwable error) {
        String reason = error.getMessage() != null ? error.getMessage() : error.getClass().getSimpleName();
        plugin.getLogger().log(Level.WARNING, configManager.getRawMessage(
                "updater-failed", "reason", reason, "url", REPOSITORY_URL + "/releases/latest"));
    }

    private record Asset(String name, String url, String digest) {}

    private record ReleaseInfo(String tag, String pageUrl, JsonArray assets) {}
}
