#!/usr/bin/env python3
import sys
import zipfile
from pathlib import Path


def verify_pair(source_path: Path, target_path: Path) -> None:
    with zipfile.ZipFile(source_path, "r") as source, zipfile.ZipFile(target_path, "r") as target:
        source_names = source.namelist()
        if source_names != target.namelist():
            raise RuntimeError("localized JAR entry list differs from the source JAR")

        marker = b"language: zh_CN"
        replacement = b"language: en_US"
        for name in source_names:
            source_data = source.read(name)
            target_data = target.read(name)
            if name == "config.yml":
                if source_data.count(marker) != 1:
                    raise RuntimeError("expected one official zh_CN language parameter")
                if target_data != source_data.replace(marker, replacement, 1):
                    raise RuntimeError("localized config changed beyond the language parameter")
            elif target_data != source_data:
                raise RuntimeError(f"localized JAR changed entry: {name}")


def main() -> int:
    if len(sys.argv) == 4 and sys.argv[1] == "--verify":
        verify_pair(Path(sys.argv[2]), Path(sys.argv[3]))
        return 0
    if len(sys.argv) != 3:
        raise SystemExit(
            "usage: localize_jar.py <source-jar> <english-jar> | "
            "localize_jar.py --verify <source-jar> <english-jar>"
        )

    source_path = Path(sys.argv[1])
    target_path = Path(sys.argv[2])
    target_path.parent.mkdir(parents=True, exist_ok=True)

    localized = False
    with zipfile.ZipFile(source_path, "r") as source, zipfile.ZipFile(target_path, "w") as target:
        target.comment = source.comment
        for entry in source.infolist():
            data = source.read(entry.filename)
            if entry.filename == "config.yml":
                marker = b"language: zh_CN"
                replacement = b"language: en_US"
                if data.count(marker) != 1:
                    raise RuntimeError("expected one official zh_CN language parameter")
                data = data.replace(marker, replacement, 1)
                localized = True
            target.writestr(entry, data)

    if not localized:
        target_path.unlink(missing_ok=True)
        raise RuntimeError("config.yml was not found in the plugin JAR")
    verify_pair(source_path, target_path)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
