<p align="center">
  <a href="https://github.com/EnAccess/micropowermanager">
    <img
      src="https://micropowermanager.io/mpmlogo_raw.png"
      alt="MicroPowerManager Customer Registration App"
      width="320"
    >
  </a>
</p>
<p align="center">
    <em>Decentralized utility management made simple. Manage customers, revenues and assets with this all-in one open source platform.</em>
</p>
<p align="center">
  <img
    alt="Project Status"
    src="https://img.shields.io/badge/Project%20Status-stable-green"
  >
  <img
    alt="GitHub Workflow Status"
    src="https://img.shields.io/github/actions/workflow/status/EnAccess/micropowermanager/check-generic.yaml"
  >
  <a href="https://github.com/EnAccess/micropowermanager/blob/main/LICENSE" target="_blank">
    <img
      alt="License"
      src="https://img.shields.io/github/license/EnAccess/micropowermanager"
    >
  </a>
</p>

---

# MicroPowerManager - Customer Registration App

MicroPowerManager (MPM) is a decentralized utility and customer management tool.
Manage customers, revenues and assets with this all-in one Open Source platform.

## Get Started

This repository contains the source code for the [MicroPowerManager Customer Registration App](https://micropowermanager.io/usage-guide/android-apps.html).

### Prerequsites

- Install [Android Studio](https://developer.android.com/studio)
- Git clone the repository

### Build the app

To build the app

- Open the project in Android Studio
- Configure `AdoptOpenJDK 8 HotSpot` as Gradle JDK.

  - Open Setting (**Android Studio > Settings > Build, Execution, Deployment > Build Tools > Gradle**)
  - If not installed yet, select **Download JDK...** from the **Gradle JDK** dropdown and select

    | Field    | Value                                    |
    | -------- | ---------------------------------------- |
    | Version  | `1.8`                                    |
    | Vendor   | `Eclipse Temurin (AdoptOpenJDK HotSpot)` |
    | Location | `<default>`                              |

    ![Android Studio AdoptOpenJDK 8 HotSpot](docs/images/android-studio-adopt-openjdk8.png)

  - If already installed, select **temurin-1.8** from the **Gradle JDK** dropdown

- Click **Sync Project with Gradle files**
