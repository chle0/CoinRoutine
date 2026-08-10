# 🪙 CoinRoutine

**CoinRoutine** is a cross-platform cryptocurrency portfolio application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**.

The project demonstrates how to build a production-style mobile application while sharing business logic, data, and UI between **Android and iOS**. It follows **Clean Architecture + MVI**, uses dependency injection, local persistence, networking, biometric authentication, and includes both **unit tests and Compose UI tests**.

> 🚧 This project is actively being developed and serves as a practical demonstration of modern Kotlin Multiplatform development.

---

## ✨ Features

### 📊 Cryptocurrency

* Browse cryptocurrency information
* View coin details
* Display cryptocurrency market data
* Network-based data fetching
* Image loading for cryptocurrency assets
* Loading and error states

### 💼 Portfolio

* Create and manage a cryptocurrency portfolio
* Track portfolio holdings
* Calculate portfolio-related values
* Combine portfolio data with current coin information
* Persist portfolio data locally
* Reactive portfolio updates

### 💰 Trading

* Buy cryptocurrency
* Sell cryptocurrency
* Track trade-related information
* Validate trade input
* Update portfolio after trades

### 🔐 Biometric Authentication

The `feature/biometric` branch introduces biometric authentication to protect the application.

* Android biometric authentication
* Platform-specific biometric implementation
* Authentication state management
* Secure access to protected application content
* Multiplatform architecture using platform-specific implementations where required

### 🧪 Testing

The project places a strong emphasis on automated testing.

* Unit tests
* ViewModel tests
* Repository/domain logic tests
* Coroutine testing
* Flow testing with Turbine
* Assertion testing with AssertK
* Compose UI tests
* Android host tests
* iOS simulator tests
* Fake implementations for testing

---

## 🏗️ Architecture

CoinRoutine follows **Clean Architecture** combined with the **MVI (Model–View–Intent)** pattern.

```text
┌─────────────────────────────────────┐
│              Compose UI             │
│       Android + iOS (CMP)           │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│           Presentation Layer         │
│       MVI / ViewModel / State        │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│             Domain Layer             │
│      Use Cases / Models / Logic      │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│              Data Layer              │
│ Repository / Remote / Local Storage  │
└───────────────┬───────────┬─────────┘
                │           │
                ▼           ▼
             Ktor         Room
            Network       Database
```

### Why MVI?

MVI provides a predictable unidirectional data flow:

```text
User Action
     │
     ▼
   Intent
     │
     ▼
 ViewModel
     │
     ▼
 Use Case
     │
     ▼
 Repository
     │
     ├──────────────► Remote Data
     │
     └──────────────► Local Database
     │
     ▼
 New State
     │
     ▼
 Compose UI
```

This makes state management predictable and makes presentation logic easier to test.

---

## 📁 Project Structure

```text
CoinRoutine/
│
├── androidApp/
│   └── Android application entry point
│
├── iosApp/
│   └── iOS application entry point
│
├── shared/
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── kotlin/
│   │   │   │   └── com/ahmad/raza/coinroutine/
│   │   │   │       │
│   │   │   │       ├── biometric/
│   │   │   │       │
│   │   │   │       ├── coins/
│   │   │   │       │   ├── data/
│   │   │   │       │   ├── domain/
│   │   │   │       │   └── presentation/
│   │   │   │       │
│   │   │   │       ├── portfolio/
│   │   │   │       │   ├── data/
│   │   │   │       │   ├── domain/
│   │   │   │       │   └── presentation/
│   │   │   │       │
│   │   │   │       ├── trade/
│   │   │   │       │   ├── domain/
│   │   │   │       │   └── presentation/
│   │   │   │       │
│   │   │   │       ├── core/
│   │   │   │       ├── di/
│   │   │   │       ├── navigation/
│   │   │   │       └── theme/
│   │   │   │
│   │   │   └── composeResources/
│   │   │
│   │   ├── androidMain/
│   │   ├── iosMain/
│   │   └── commonTest/
│   │
│   └── schemas/
│       └── Room database schemas
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

The repository currently separates `coins`, `portfolio`, `trade`, `biometric`, `core`, dependency injection, navigation, and theme functionality inside the shared module.

---

## 🛠️ Tech Stack

### Kotlin Multiplatform

* Kotlin
* Kotlin Multiplatform
* Kotlin Coroutines
* Kotlinx Serialization
* Kotlinx DateTime

### UI

* Compose Multiplatform
* Jetpack Compose
* Material 3
* Compose Resources
* Coil
* Compose UI Testing

### Architecture

* Clean Architecture
* MVI
* ViewModel
* Unidirectional Data Flow
* Repository Pattern
* Use Case Pattern
* Dependency Injection

### Networking

* Ktor Client
* Kotlinx Serialization
* Platform-specific Ktor engines

### Local Storage

* Room for Kotlin Multiplatform
* Bundled SQLite
* Database schema management

### Dependency Injection

* Koin
* Koin Compose
* Koin ViewModel integration

### Security

* Android BiometricPrompt
* Multiplatform biometric abstraction
* Platform-specific authentication implementation

### Testing

* Kotlin Test
* Kotlin Coroutines Test
* Turbine
* AssertK
* Compose UI Test
* Android host tests
* iOS simulator tests

The current shared Gradle configuration includes Compose, Ktor, Room, SQLite, Koin, Coil, biometric support, Kotlin serialization, and the testing libraries listed above.

---

## 🌍 Supported Platforms

| Platform | Status |
| -------- | ------ |
| Android  | ✅      |
| iOS      | ✅      |

The shared module currently configures Android plus `iosArm64` and `iosSimulatorArm64` targets, while the iOS application lives in the dedicated `iosApp` module.

---

## 🧩 Kotlin Multiplatform Structure

CoinRoutine uses Kotlin Multiplatform to maximize code sharing between Android and iOS.

### `commonMain`

Contains code shared between platforms:

* Domain models
* Use cases
* Repositories
* ViewModels
* MVI state
* MVI intents
* Networking abstractions
* Database access
* Compose UI
* Navigation
* Themes
* Business logic

### `androidMain`

Contains Android-specific implementations such as:

* Android biometric authentication
* Android Ktor engine
* Android-specific integrations
* Android Compose configuration

### `iosMain`

Contains iOS-specific implementations such as:

* iOS networking engine
* iOS platform integrations
* iOS-specific implementations required by Kotlin/Native

---

## 🧪 Testing Strategy

Testing is an important part of CoinRoutine's architecture.

### Unit Tests

Business and presentation logic can be tested independently from the UI.

Examples include:

```text
ViewModel
   ↓
Fake Repository
   ↓
Test Data
   ↓
Expected MVI State
```

The project uses:

* `kotlin.test`
* `kotlinx-coroutines-test`
* Turbine
* AssertK

### UI Tests

Compose UI tests verify important user interactions and rendered states.

Examples:

* Verify UI elements are displayed
* Verify text content
* Verify user interactions
* Verify loading states
* Verify error states
* Verify portfolio/trading flows

### Android Tests

Run Android shared tests with:

```bash
./gradlew :shared:testAndroidHostTest
```

### iOS Tests

Run iOS simulator tests with:

```bash
./gradlew :shared:iosSimulatorArm64Test
```

These are also the test tasks currently documented by the project.

---

## 🔑 API Configuration

CoinRoutine uses an API key loaded from `local.properties` during the build.

Create:

```text
local.properties
```

and add:

```properties
API_KEY=your_api_key_here
```

The project uses BuildKonfig to expose the API key to shared Kotlin code without committing the secret into the repository.

> ⚠️ Never commit your real API key to Git.

---

## 🚀 Getting Started

### Requirements

* Android Studio
* Kotlin Multiplatform support
* Xcode
* JDK
* Android SDK
* iOS Simulator or physical iOS device
* CocoaPods if required by the selected dependencies

### Clone the Repository

```bash
git clone https://github.com/chle0/CoinRoutine.git
cd CoinRoutine
```

### Checkout the Biometric Feature

```bash
git checkout feature/biometric
```

### Configure API Key

Create `local.properties`:

```properties
API_KEY=your_api_key_here
```

### Run Android

Build the Android application:

```bash
./gradlew :androidApp:assembleDebug
```

Then install/run the generated APK on an Android device or emulator.

### Run iOS

Open:

```text
iosApp/
```

in Xcode and run the application on an iOS Simulator or physical device.

The repository also documents Xcode as the entry point for running the iOS application.

---

## 🔐 Biometric Authentication

The `feature/biometric` branch demonstrates adding biometric authentication while keeping the application architecture multiplatform.

The architecture separates the shared authentication contract from platform-specific implementations.

```text
             Shared Code
                 │
        Biometric Abstraction
           ┌─────┴─────┐
           │           │
           ▼           ▼
       Android        iOS
     BiometricPrompt  Local Auth
```

This allows the application to keep its business and presentation logic shared while delegating platform-specific authentication to the appropriate platform APIs.

---

## 🎯 Project Goals

CoinRoutine was created to demonstrate practical experience with:

* Kotlin Multiplatform
* Compose Multiplatform
* Android + iOS development
* Clean Architecture
* MVI architecture
* Reactive state management
* Ktor networking
* Room Multiplatform
* Koin dependency injection
* Local persistence
* Biometric authentication
* Unit testing
* Coroutine testing
* Flow testing
* Compose UI testing
* Platform-specific implementations
* Shared UI and business logic

---

## 📚 What This Project Demonstrates

This project is more than a cryptocurrency application. It is a practical example of how a modern mobile application can be structured to share a significant amount of code between Android and iOS while still allowing platform-specific functionality when necessary.

The architecture emphasizes:

**Shared Code → Clean Boundaries → Predictable State → Testable Logic → Platform-specific Integrations**

---

## 🔮 Future Improvements

Potential improvements include:

* [ ] More comprehensive UI test coverage
* [ ] Additional portfolio analytics
* [ ] Advanced cryptocurrency charts
* [ ] More robust offline-first behavior
* [ ] Improved biometric fallback handling
* [ ] CI/CD pipeline
* [ ] Code coverage reporting
* [ ] Automated release builds
* [ ] More comprehensive integration tests

---

## 👨‍💻 Author

**Ahmad Raza**

Senior Android Developer | Kotlin Multiplatform | Compose Multiplatform

---

## 📄 License

This project is intended primarily as a demonstration and learning project for modern Kotlin Multiplatform and Compose Multiplatform development.
