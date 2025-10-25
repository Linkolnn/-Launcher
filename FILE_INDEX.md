# Индекс файлов проекта Custom Android Launcher

## 📑 Быстрая навигация

### 📱 Главные экраны приложения
| Файл | Описание | Где находится |
|------|----------|---------------|
| `MainActivity.kt` | Главный экран launcher | `app/src/main/java/.../ui/` |
| `AppListActivity.kt` | Список приложений | `app/src/main/java/.../ui/` |
| `SettingsActivity.kt` | Настройки | `app/src/main/java/.../ui/` |

### 🎨 Layouts (XML)
| Файл | Описание |
|------|----------|
| `activity_main.xml` | Layout главного экрана |
| `activity_app_list.xml` | Layout списка приложений |
| `activity_settings.xml` | Layout настроек |
| `item_app.xml` | Элемент списка приложения |
| `item_app_grid.xml` | Элемент сетки приложения |

### 🗄️ База данных (Room)
| Файл | Описание |
|------|----------|
| `LauncherDatabase.kt` | Room database instance |
| `HiddenApp.kt` | Entity для скрытых приложений |
| `HiddenAppDao.kt` | DAO interface |

### 🔧 Бизнес-логика
| Файл | Описание |
|------|----------|
| `AppViewModel.kt` | ViewModel с логикой |
| `AppRepository.kt` | Repository для приложений |
| `LauncherPreferences.kt` | Настройки приложения |

### 🛠️ Сервисы
| Файл | Описание |
|------|----------|
| `TouchBlockService.kt` | Блокировка сенсорного экрана |

### 📋 Адаптеры
| Файл | Описание |
|------|----------|
| `AppListAdapter.kt` | Adapter для списка |
| `AppGridAdapter.kt` | Adapter для сетки |

### 🎨 Ресурсы
| Категория | Файлы |
|-----------|-------|
| Colors | `colors.xml` |
| Strings | `strings.xml` |
| Themes | `themes.xml`, `values-night/themes.xml` |
| Styles | `styles.xml` |
| Icons | `ic_apps.xml`, `ic_settings.xml`, `ic_search.xml`, `ic_lock.xml` |

### 📚 Документация
| Файл | Для чего |
|------|----------|
| `README.md` | Общее описание проекта |
| `QUICKSTART.md` | Быстрый старт за 5 минут |
| `BUILD_INSTRUCTIONS.md` | Детальная сборка |
| `FEATURES.md` | Описание всех функций |
| `PROJECT_SUMMARY.md` | Техническая сводка |
| `COMPLETION_REPORT.md` | Отчет о завершении |
| `FILE_INDEX.md` | Этот файл |

---

## 📂 Полная структура проекта

```
AndroidLauncher/
│
├── 📄 README.md                              # Главная документация
├── 📄 QUICKSTART.md                          # Быстрый старт
├── 📄 BUILD_INSTRUCTIONS.md                 # Инструкции по сборке
├── 📄 FEATURES.md                           # Описание функций
├── 📄 PROJECT_SUMMARY.md                    # Техническая сводка
├── 📄 COMPLETION_REPORT.md                  # Отчет о завершении
├── 📄 FILE_INDEX.md                         # Этот файл
│
├── 📄 .gitignore                            # Git ignore rules
├── 📄 build.gradle                          # Project-level Gradle
├── 📄 settings.gradle                       # Gradle settings
├── 📄 gradle.properties                     # Gradle properties
├── 📄 local.properties.example              # Пример SDK config
├── 📄 gradlew                               # Gradle wrapper (Unix)
├── 📄 gradlew.bat                           # Gradle wrapper (Windows)
│
├── 📁 gradle/
│   └── 📁 wrapper/
│       └── 📄 gradle-wrapper.properties     # Wrapper config
│
└── 📁 app/
    ├── 📄 build.gradle                      # App-level Gradle
    ├── 📄 proguard-rules.pro                # ProGuard rules
    │
    └── 📁 src/
        └── 📁 main/
            │
            ├── 📄 AndroidManifest.xml       # App manifest (ВАЖНО!)
            │
            ├── 📁 java/com/customlauncher/app/
            │   │
            │   ├── 📄 LauncherApplication.kt          # Application class
            │   │
            │   ├── 📁 ui/
            │   │   ├── 📄 MainActivity.kt             # Главный экран
            │   │   ├── 📄 AppListActivity.kt          # Список приложений
            │   │   ├── 📄 SettingsActivity.kt         # Настройки
            │   │   │
            │   │   ├── 📁 adapter/
            │   │   │   ├── 📄 AppListAdapter.kt       # List adapter
            │   │   │   └── 📄 AppGridAdapter.kt       # Grid adapter
            │   │   │
            │   │   └── 📁 viewmodel/
            │   │       └── 📄 AppViewModel.kt         # ViewModel
            │   │
            │   ├── 📁 service/
            │   │   └── 📄 TouchBlockService.kt        # Screen blocking
            │   │
            │   └── 📁 data/
            │       │
            │       ├── 📁 model/
            │       │   └── 📄 AppInfo.kt              # Data model
            │       │
            │       ├── 📁 database/
            │       │   ├── 📄 LauncherDatabase.kt     # Room DB
            │       │   ├── 📄 HiddenApp.kt            # Entity
            │       │   └── 📄 HiddenAppDao.kt         # DAO
            │       │
            │       ├── 📁 repository/
            │       │   └── 📄 AppRepository.kt        # Repository
            │       │
            │       └── 📁 preferences/
            │           └── 📄 LauncherPreferences.kt  # Preferences
            │
            └── 📁 res/
                │
                ├── 📁 layout/
                │   ├── 📄 activity_main.xml           # Main screen layout
                │   ├── 📄 activity_app_list.xml       # App list layout
                │   ├── 📄 activity_settings.xml       # Settings layout
                │   ├── 📄 item_app.xml                # App list item
                │   └── 📄 item_app_grid.xml           # App grid item
                │
                ├── 📁 drawable/
                │   ├── 📄 ic_apps.xml                 # Apps icon
                │   ├── 📄 ic_settings.xml             # Settings icon
                │   ├── 📄 ic_search.xml               # Search icon
                │   ├── 📄 ic_lock.xml                 # Lock icon
                │   ├── 📄 ic_launcher_foreground.xml  # Launcher icon
                │   └── 📄 swipe_indicator.xml         # Swipe indicator
                │
                ├── 📁 values/
                │   ├── 📄 colors.xml                  # Color palette
                │   ├── 📄 strings.xml                 # String resources
                │   ├── 📄 themes.xml                  # Light theme
                │   └── 📄 styles.xml                  # Custom styles
                │
                ├── 📁 values-night/
                │   └── 📄 themes.xml                  # Dark theme
                │
                ├── 📁 xml/
                │   ├── 📄 backup_rules.xml            # Backup config
                │   └── 📄 data_extraction_rules.xml   # Data extraction
                │
                ├── 📁 mipmap-anydpi-v26/
                │   ├── 📄 ic_launcher.xml             # Adaptive icon
                │   └── 📄 ic_launcher_round.xml       # Round icon
                │
                └── 📁 mipmap-{hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/
                    └── 📄 .gitkeep                    # Placeholders
```

---

## 🔍 Поиск файлов по задаче

### Хочу изменить UI:
👉 Смотри: `app/src/main/res/layout/*.xml`

### Хочу изменить цвета:
👉 Смотри: `app/src/main/res/values/colors.xml`

### Хочу изменить логику приложений:
👉 Смотри: `AppViewModel.kt` и `AppRepository.kt`

### Хочу изменить блокировку экрана:
👉 Смотри: `TouchBlockService.kt`

### Хочу изменить настройки:
👉 Смотри: `LauncherPreferences.kt` и `SettingsActivity.kt`

### Хочу изменить базу данных:
👉 Смотри: `LauncherDatabase.kt`, `HiddenApp.kt`, `HiddenAppDao.kt`

### Хочу добавить новую Activity:
1. Создай файл в `app/src/main/java/.../ui/`
2. Создай layout в `app/src/main/res/layout/`
3. Добавь в `AndroidManifest.xml`

### Хочу изменить зависимости:
👉 Смотри: `app/build.gradle`

### Хочу изменить версию Android:
👉 Смотри: `app/build.gradle` → `compileSdk`, `minSdk`, `targetSdk`

---

## 📊 Статистика файлов

### По типам
- **Kotlin:** ~14 файлов (~2,500 строк)
- **XML (Layout):** 5 файлов (~800 строк)
- **XML (Resources):** 10 файлов (~400 строк)
- **Gradle:** 3 файла (~200 строк)
- **Markdown:** 7 файлов (~18,000 слов)

### По модулям
- **UI Layer:** 7 файлов (Activities, Adapters)
- **Business Layer:** 2 файла (ViewModel, Repository)
- **Data Layer:** 4 файла (Database, Preferences)
- **Service Layer:** 1 файл (TouchBlock)
- **Resources:** 15+ файлов
- **Documentation:** 7 файлов

---

## 🎯 Ключевые файлы для понимания проекта

**Начни с этих файлов (в порядке важности):**

1. **README.md** - общее понимание проекта
2. **AndroidManifest.xml** - launcher configuration
3. **MainActivity.kt** - главная точка входа
4. **AppRepository.kt** - работа с приложениями
5. **AppViewModel.kt** - бизнес-логика
6. **TouchBlockService.kt** - блокировка экрана
7. **activity_main.xml** - UI главного экрана

---

## 🚀 Быстрые ссылки

### Документация
- [Быстрый старт](QUICKSTART.md)
- [Полное руководство](README.md)
- [Инструкции по сборке](BUILD_INSTRUCTIONS.md)
- [Список функций](FEATURES.md)

### Код
- [Application](app/src/main/java/com/customlauncher/app/LauncherApplication.kt)
- [MainActivity](app/src/main/java/com/customlauncher/app/ui/MainActivity.kt)
- [ViewModel](app/src/main/java/com/customlauncher/app/ui/viewmodel/AppViewModel.kt)
- [Repository](app/src/main/java/com/customlauncher/app/data/repository/AppRepository.kt)

### Ресурсы
- [Цвета](app/src/main/res/values/colors.xml)
- [Строки](app/src/main/res/values/strings.xml)
- [Темы](app/src/main/res/values/themes.xml)

---

## 📝 Примечания

### Важно знать:
- Все Activity используют ViewBinding
- Database операции выполняются асинхронно через Coroutines
- UI обновляется через LiveData observers
- Service работает в фоне для блокировки экрана

### Соглашения:
- Kotlin code style: официальный
- Package structure: по функциональности
- Naming: camelCase для функций, PascalCase для классов
- Комментарии: на русском и английском

---

**Используйте этот индекс для быстрой навигации по проекту!** 🗺️
