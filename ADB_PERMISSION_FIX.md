# 🔧 Решение проблем с разрешениями через ADB

## ⚠️ Когда использовать этот метод

Используйте ADB команды если:
- ❌ В разделе "Специальные возможности" **нет переключателя** для ИLauncher
- ❌ "Поверх других приложений" показывает **"функция отключена, поскольку она замедляет работу телефона"**
- ❌ Система блокирует активацию разрешений

## 📱 Проблемные устройства

Чаще всего проблема встречается на:
- **Xiaomi/Redmi** с MIUI 12-14
- **Realme/Oppo** с ColorOS
- **Samsung** с агрессивной оптимизацией
- Устройства с **Android Go**
- Слабые устройства с малым объемом RAM

## 🚀 Пошаговая инструкция

### Шаг 1: Включение отладки по USB

1. **Откройте настройки телефона**
2. Перейдите в **"О телефоне"**
3. Найдите **"Номер сборки"** и нажмите на него **7 раз**
4. Вернитесь в настройки → появится **"Для разработчиков"**
5. В разделе "Для разработчиков" включите:
   - ✅ **Отладка по USB**
   - ✅ **Установка через USB** (если есть)
   - ✅ **Отладка по USB (настройки безопасности)** (если есть на Xiaomi)

### Шаг 2: Установка ADB на компьютер

#### Windows:
1. Скачайте: https://dl.google.com/android/repository/platform-tools-latest-windows.zip
2. Распакуйте в папку `C:\adb`
3. Откройте командную строку (Win+R → cmd)
4. Перейдите в папку: `cd C:\adb`

#### macOS:
1. Скачайте: https://dl.google.com/android/repository/platform-tools-latest-darwin.zip
2. Распакуйте в домашнюю папку
3. Откройте Terminal
4. Перейдите в папку: `cd ~/platform-tools`

#### Linux:
1. Скачайте: https://dl.google.com/android/repository/platform-tools-latest-linux.zip
2. Распакуйте: `unzip platform-tools-latest-linux.zip`
3. Перейдите в папку: `cd platform-tools`

### Шаг 3: Подключение телефона

1. **Подключите телефон** к компьютеру через USB
2. На телефоне появится запрос **"Разрешить отладку по USB?"**
3. Поставьте галочку **"Всегда разрешать"** и нажмите **ОК**
4. Проверьте подключение командой:
```bash
adb devices
```
Должно появиться:
```
List of devices attached
XXXXXXXXX    device
```

### Шаг 4: Выполнение команд для активации разрешений

## 🔴 ВАЖНО: Копируйте и выполняйте команды ТОЧНО как написано!

#### Команды для активации всех разрешений:

```bash
# 1. Включить службу специальных возможностей (полный путь к службе)
adb shell settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService

# 2. Активировать специальные возможности
adb shell settings put secure accessibility_enabled 1

# 3. Разрешить отображение поверх других окон
adb shell appops set com.customlauncher.app SYSTEM_ALERT_WINDOW allow

# 5. Дополнительно для MIUI/ColorOS - разрешить фоновую активность
adb shell cmd appops set com.customlauncher.app RUN_IN_BACKGROUND allow
adb shell cmd appops set com.customlauncher.app RUN_ANY_IN_BACKGROUND allow

# 6. Отключить оптимизацию батареи для приложения
adb shell dumpsys deviceidle whitelist +com.customlauncher.app
```

### Шаг 5: Проверка результата

1. **Отключите телефон** от компьютера
2. **Откройте настройки** ИLauncher
3. Проверьте статус разрешений:
   - ✅ Специальные возможности: **Включено**
   - ✅ Наложение поверх окон: **Разрешено**

## 🆘 Если не помогло

### Альтернативный метод для упрямых устройств:

```bash
# Полный сброс настроек специальных возможностей
adb shell settings delete secure enabled_accessibility_services
adb shell settings delete secure accessibility_enabled

# Повторная активация с полным путем к службе
adb shell settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService
adb shell settings put secure accessibility_enabled 1
```

### Для Xiaomi/MIUI дополнительно:

```bash
# Отключить MIUI оптимизацию для приложения
adb shell settings put system miui_optimization false
adb shell pm grant com.customlauncher.app android.permission.WRITE_SETTINGS
adb shell settings put system miui_optimization true
```

## 💡 Полезные команды для диагностики

```bash
# Проверить статус службы специальных возможностей
adb shell settings get secure enabled_accessibility_services

# Проверить разрешение overlay
adb shell appops get com.customlauncher.app SYSTEM_ALERT_WINDOW

# Посмотреть все разрешения приложения
adb shell dumpsys package com.customlauncher.app | grep permission
```

## ❓ Частые вопросы

**Q: Команда выполнилась, но разрешения не активировались**
A: Перезагрузите телефон после выполнения команд

**Q: Появляется ошибка "device unauthorized"**
A: На телефоне разрешите отладку по USB (появится окно с запросом)

**Q: Ошибка "com.customlauncher.app not found"**
A: Сначала установите ИLauncher на телефон

**Q: После перезагрузки разрешения сбрасываются**
A: Отключите оптимизацию батареи и автозапуск других лаунчеров

## ⚡ Быстрое решение (все команды одной строкой)

Для Windows PowerShell:
```powershell
adb shell "settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService && settings put secure accessibility_enabled 1 && appops set com.customlauncher.app SYSTEM_ALERT_WINDOW allow"
```

Для Linux/macOS Terminal:
```bash
adb shell 'settings put secure enabled_accessibility_services com.customlauncher.app/com.customlauncher.app.service.SystemBlockAccessibilityService && settings put secure accessibility_enabled 1 && appops set com.customlauncher.app SYSTEM_ALERT_WINDOW allow'
```

## 📞 Поддержка

Если проблема не решилась, сообщите:
- Модель телефона
- Версия Android
- Версия прошивки (MIUI/ColorOS/OneUI)
- Скриншот настроек разрешений
- Вывод команды: `adb shell dumpsys package com.customlauncher.app | grep permission`

---

💡 **Совет:** Сохраните эту инструкцию - она может понадобиться после обновления системы или сброса настроек!
