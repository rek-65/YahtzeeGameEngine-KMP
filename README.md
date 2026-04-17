# Yahtzee Engine (Kotlin Multiplatform)

A platform-independent Yahtzee game engine implemented as a Kotlin Multiplatform (KMP) library.

---

## Overview

This project provides a fully functional Yahtzee game engine with:

- Complete scoring logic
- Turn management
- Multi-player support
- Yahtzee bonus handling
- Joker rule handling
- Deterministic and testable architecture

This is a **non-UI library** designed to be consumed by applications on multiple platforms.

---

## Project Type

- **Library only**
- **No UI included**
- **No platform-specific dependencies in core logic**

---

## Supported Platforms

This library is built using Kotlin Multiplatform and supports:

- JVM (Android, Windows, Linux)
- iOS (via KMP)
- Native targets (Linux / Windows via Kotlin/Native)

---

## Artifacts

### JVM Consumers

Use the JVM runtime jar:

shared-jvm-<version>.jar

This is the correct artifact for:

- Android apps
- Desktop JVM applications (Windows/Linux)

---

### Sources

shared-jvm-<version>-sources.jar

---

### Documentation

shared-<version>-javadoc.jar

> Note: This is generated using Dokka and contains HTML-based documentation.

---

## Multiplatform Usage

Kotlin Multiplatform consumers should depend on the published library using Gradle.  
The correct platform artifact will be resolved automatically.

---

## Build Requirements

- JDK 11
- Gradle (wrapper included)
- PowerShell / Terminal

> Android Studio is **not required**

---

## Build (PowerShell)

From project root:

.\gradlew.bat clean  
.\gradlew.bat :shared:build

---

## Run Tests

.\gradlew.bat :shared:testDebugUnitTest

---

## Publish to Local Maven

.\gradlew.bat :shared:publishToMavenLocal

---

## Generate Documentation

.\gradlew.bat :shared:dokkaGenerate  
.\gradlew.bat :shared:dokkaHtmlJar

---

## Design Principles

- Pure logic engine (UI-independent)
- Deterministic and testable behavior
- Clear separation of concerns
- Minimal platform coupling
- Ready for multiplayer and external integrations

---

## Notes

- iOS targets are included via Kotlin Multiplatform
- Building full Apple applications requires macOS + Xcode
- This repository represents the multiplatform evolution of the engine architecture

---

## License

MIT License (see LICENSE file)