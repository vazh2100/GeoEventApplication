# GeoEventApp

## Скриншоты

 <div style="display: flex; justify-content: space-between;"> <img src="screenshots/Screenshot_Good.png" width="270" /> <img src="screenshots/Screenshot_Bad.png" width="270" /> <img src="screenshots/Screenshot_Filter.png" width="270" /> </div>

## Установка проекта

1. Склонируйте репозиторий:
   ```bash
   git clone <repository-url>
   ```
2. Откройте проект в **Android Studio 2024**

---

## JSON с тестовыми данными

Приложение использует файл `events.json` для имитации API-ответа c помощью class AssetInterceptor,
который подменяет ответ на запрос Retrofit этим файлом.  
Эти файлы находятся по пути:

```
app/src/main/assets/events.json
app/src/main/java/com/vazh2100/geoeventapp/data/inteceptor/AssetInterceptor.kt
```

### Пример данных:

```json
[
  {
    "id": 0,
    "name": "Fun and Fancy Free",
    "description": "Other specified psychosexual disorders",
    "type": "Exhibition",
    "latitude": 26.096382,
    "longitude": 107.454787,
    "city": "Jiusi",
    "date": "2024-12-16T04:50:02Z"
  },
  {
    "id": 1,
    "name": "Wilde",
    "description": "Gonococcal endophthalmia",
    "type": "Festival",
    "latitude": 24.086481,
    "longitude": 112.346259,
    "city": "Qiashui",
    "date": "2024-12-06T13:55:07Z"
  }
]
```

### Структура JSON:

| Поле          | Тип      | Описание                                     |
|---------------|----------|----------------------------------------------|
| `id`          | `Int`    | Уникальный идентификатор события.            |
| `name`        | `String` | Название события.                            |
| `description` | `String` | Краткое описание события.                    |
| `type`        | `String` | Тип события (например, выставка, фестиваль). |
| `latitude`    | `Double` | Географическая широта места события.         |
| `longitude`   | `Double` | Географическая долгота места события.        |
| `city`        | `String` | Город, где проходит событие.                 |
| `date`        | `String` | Дата и время события в формате ISO 8601.     |

---

## Тестирование

Для запуска модульных тестов:

1. Откройте файл:
   ```
   app/src/test/java/com/vazh2100/geoeventapp/domain/entity/EventFilterTest.kt
   ```
2. Выберите **Run 'EventFilterTest'**.
3. Тесты проверяют:
   - Фильтрацию событий по типу.
   - Фильтрацию событий по радиусу.

Чтобы протестировать неудачный сетевой запрос добавьте throw Exception() в

```
app/src/main/java/com/vazh2100/geoeventapp/data/repository/EventRepository.kt
```

```kotlin
   private suspend fun refreshEvents() {
   throw Exception() // добавьте эту строку
   delay(5000L)
   val eventsFromApi = mainApi.getEvents()
   eventDao.deleteAllEvents()
   eventDao.insertEvents(eventsFromApi)
   preferenceStorage.setLastUpdateTime(getNowDate())
}
```

---

