# Инструкции по сборке Android Launcher

## Быстрый старт

### Предварительные требования

1. **Java Development Kit (JDK) 17 или выше**
   ```bash
   java -version
   ```
   
2. **Android Studio** (Arctic Fox или новее)
   - Скачайте с [developer.android.com](https://developer.android.com/studio)
   
3. **Android SDK**
   - API Level 26+ (Android 8.0 Oreo)
   - API Level 34 рекомендуется для компиляции

### Метод 1: Сборка через Android Studio (Рекомендуется)

#### Шаг 1: Настройка проекта
1. Запустите Android Studio
2. Выберите `File` → `Open`
3. Откройте папку `AndroidLauncher`
4. Дождитесь синхронизации Gradle (может занять несколько минут)

#### Шаг 2: Настройка SDK
1. Откройте `File` → `Project Structure`
2. Убедитесь, что JDK установлен на версию 17+
3. Проверьте, что Android SDK установлен

#### Шаг 3: Сборка APK

**Debug версия:**
1. В меню выберите `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
2. Дождитесь завершения сборки
3. APK будет в: `app/build/outputs/apk/debug/app-debug.apk`

**Release версия:**
1. Создайте keystore для подписи APK:
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
   ```

2. В файле `app/build.gradle` добавьте:
   ```gradle
   android {
       ...
       signingConfigs {
           release {
               storeFile file("my-release-key.jks")
               storePassword "your-password"
               keyAlias "my-key-alias"
               keyPassword "your-password"
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               ...
           }
       }
   }
   ```

3. Выберите `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
4. APK будет в: `app/build/outputs/apk/release/app-release.apk`

### Метод 2: Сборка через командную строку

#### Шаг 1: Настройка окружения

**Linux/Mac:**
```bash
cd AndroidLauncher
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

**Windows (PowerShell):**
```powershell
cd AndroidLauncher
$env:ANDROID_HOME = "C:\Users\YourUsername\AppData\Local\Android\Sdk"
$env:Path += ";$env:ANDROID_HOME\tools;$env:ANDROID_HOME\platform-tools"
```

#### Шаг 2: Сборка

**Debug APK:**
```bash
# Linux/Mac
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

**Release APK:**
```bash
# Linux/Mac
./gradlew assembleRelease

# Windows
gradlew.bat assembleRelease
```

### Установка на устройство

#### Через ADB (Android Debug Bridge)

1. **Включите отладку по USB** на Android устройстве:
   - Откройте `Настройки` → `О телефоне`
   - Нажмите 7 раз на "Номер сборки"
   - Вернитесь в `Настройки` → `Для разработчиков`
   - Включите "Отладка по USB"

2. **Подключите устройство** к компьютеру через USB

3. **Проверьте подключение:**
   ```bash
   adb devices
   ```
   Должно показать ваше устройство

4. **Установите APK:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

#### Через файл

1. Скопируйте APK на устройство
2. Откройте файл на устройстве
3. Разрешите установку из неизвестных источников (если требуется)
4. Установите приложение

### Настройка лаунчера после установки

1. **Нажмите кнопку Home** на устройстве
2. Выберите **"Custom Launcher"**
3. Нажмите **"Всегда"**
4. Предоставьте разрешение **"Поверх других приложений"**:
   - Настройки → Приложения → Custom Launcher → Разрешения
   - Включите "Показ поверх других окон"

## Решение проблем

### Ошибка: "SDK location not found"

Создайте файл `local.properties` в корне проекта:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

**Примеры путей:**
- Linux: `/home/username/Android/Sdk`
- Mac: `/Users/username/Library/Android/sdk`
- Windows: `C\:\\Users\\username\\AppData\\Local\\Android\\sdk`

### Ошибка: "Gradle sync failed"

1. Очистите кеш:
   ```bash
   ./gradlew clean
   ```

2. Обновите Gradle wrapper:
   ```bash
   ./gradlew wrapper --gradle-version 8.0
   ```

3. В Android Studio: `File` → `Invalidate Caches / Restart`

### Ошибка: "Installed Build Tools revision X is corrupted"

1. Откройте Android Studio
2. `Tools` → `SDK Manager`
3. Во вкладке "SDK Tools" удалите и переустановите Android SDK Build-Tools

### APK не устанавливается на устройстве

1. Проверьте версию Android (минимум 8.0)
2. Очистите место на устройстве
3. Удалите старую версию (если установлена)
4. Разрешите установку из неизвестных источников

## Структура выходных файлов

```
app/build/outputs/
├── apk/
│   ├── debug/
│   │   └── app-debug.apk          # Debug версия
│   └── release/
│       └── app-release.apk        # Release версия
├── bundle/
│   └── release/
│       └── app-release.aab        # Android App Bundle для Google Play
└── logs/
```

## Создание Android App Bundle (для Google Play)

```bash
./gradlew bundleRelease
```

AAB файл будет в: `app/build/outputs/bundle/release/app-release.aab`

## Тестирование

### Unit тесты
```bash
./gradlew test
```

### Instrumented тесты (на устройстве)
```bash
./gradlew connectedAndroidTest
```

## Дополнительная информация

### Минимальные системные требования для сборки:
- **RAM:** 8 GB (16 GB рекомендуется)
- **Место на диске:** 10 GB свободного места
- **Процессор:** Многоядерный процессор

### Версии зависимостей:
- Kotlin: 1.9.0
- Android Gradle Plugin: 8.1.0
- Gradle: 8.0
- Material Components: 1.11.0
- Room: 2.6.1
- Lifecycle: 2.7.0

### Полезные команды Gradle:

```bash
# Посмотреть все задачи
./gradlew tasks

# Очистить проект
./gradlew clean

# Запустить lint проверку
./gradlew lint

# Посмотреть зависимости
./gradlew dependencies

# Собрать и установить debug версию
./gradlew installDebug
```

## Поддержка

Если у вас возникли проблемы:
1. Проверьте версии JDK и Android SDK
2. Очистите кеш Gradle: `rm -rf ~/.gradle/caches/`
3. Пересоберите проект: `./gradlew clean build`
4. Проверьте логи в `app/build/outputs/logs/`

---

**Успешной сборки!** 🚀
