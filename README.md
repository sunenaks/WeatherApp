# WeatherApp

An Android Jetpack Compose app that displays 5 day weather forecasts based on user's location. This project is fully developed in kotlin

---

Table of contents
- Project overview
- Architecture
- Screenshots
- Repo layout (high level)
- Requirements
- Setup
  - Clone
  - API key configuration
  - Permissions
- Run & debug
  - From Android Studio
  - From terminal

---

**Project overview**

The app starts at `WeatherForecastScreen` and fetches weather using the device location. If location services (GPS) are off or permissions are denied, the app navigates to an `ErrorScreen` that explains the problem and offers a button to open the system Location Settings. After enabling GPS and returning to the app, the app should re-check the location and reattempt the fetch, then navigate back to `WeatherForecastScreen`.
The background of the screen changes based on the weather of the day selected. And weather data close to noon time is selected for showing forecasts from the weather api.

The data shown in this project is from the following public repository : 
https://openweathermap.org/forecast5#list

**Architecture**

This project is based on clean architecture + MVVM to make the project

**Screenshots**

<img width="250" height="800" alt="Screenshot_20260121_183409" src="https://github.com/user-attachments/assets/01f0d92e-ae3c-4899-a074-287c12a76b6f" />
<img width="250" height="800" alt="Screenshot_20260121_183419" src="https://github.com/user-attachments/assets/862adde1-a8ae-47e8-914c-c627323a29be" />
<img width="250" height="800" alt="Screenshot_20260121_183439" src="https://github.com/user-attachments/assets/8158cd6e-c1a1-4f7c-846d-862ca3be70f8" />
<img width="250" height="800" alt="Screenshot_20260121_183450" src="https://github.com/user-attachments/assets/11f2cca5-c890-4802-8547-912a459f1884" />


**Repo layout (high level)**
- `app/` — Android app module
  - `src/main/java/.../presentation/` — UI, navigation (`AppNavGraph`), `MainActivity`, ViewModels
  - `src/main/res/` — themes, drawables
  - `build.gradle.kts` — module build config
- Top-level Gradle files: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, etc.

**Requirements**
- Android Studio (latest stable recommended)
- JDK 11+ (check Gradle settings)
- Android device/emulator

**Setup**

1) Clone

```bash
git clone <your-repo-url>
cd WeatherApp
```

2) API key

The project references a weather API key through `BuildConfig` (e.g. `BuildConfig.API_KEY`). Do not commit secrets. Recommended options:

- local-only: put the key in `local.properties` (do not commit):

```
WEATHER_API_KEY="your_api_key_here"
```

Then expose it via your `app/build.gradle.kts` as a `buildConfigField` (or use an existing property mapping already present in the project). In this project, I have `weatherapp.properties` and committed it for the user's reference.
You can generate your own api key from the following link: https://home.openweathermap.org/api_keys
Run & debug

From Android Studio
- Open the project, select an emulator or device, and Run.

From terminal

```bash
# Build
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

```


