### GeoEventApp
-**UI и навигация**: Jetpack Compose, Material 3, Compose Navigation.  
-**DI**: Koin DI.  
-**Сетевое взаимодействие**: Retrofit, Kotlin Serialization.  
-**Локальное хранилище**: Room, Data Store.  
-**Геолокация**: Location Google Play Services, Accompanist Permission.  
-**Тестирование**: JUnit, Mockk.  
-**Архитектура**: MVVM, DI, Clean Architecture  
-**Техническое задание**: [TECHNICAL SPECIFICATIONS.md](https://github.com/vazh2100/GeoEventApplication/blob/master/TECHNICALSPECIFICATIONS.md)
 <div style="display: flex; justify-content: space-between;"> <img src="screenshots/Screenshot_Good.png" width="270" /> <img src="screenshots/Screenshot_Bad.png" width="270" /> <img src="screenshots/Screenshot_Filter.png" width="270" /> </div>

#### Экраны  
1. **Список событий**  
2. **Детальное отображение события**
   
#### Экран "Список событий"
-позволяет пользователю перейти на детальный экран события.  
-позволяет применить фильтр событий по типу, по начальной и/или конечной дате, по радиусу.  
-если координаты пользователя не доступны, то фильтр по радиусу недоступен и не применяется при фильтрации.  
-если геолокация доступна, то отображает радиус событий.  
-позволяет пользователю отменить временные изменения фильтра по кнопке "Cancel" или сохранить изменения фильтра по кнопке "Apply".  
-отображает в реальном времени доступ в интернет.  
-отображает в реальном времени разрешение на отслеживание геолокации.  
-отображает в реальном времени включён ли GPS на устройстве.  
-отображает в реальном времени координаты пользователя.  
-предлагает предоставить разрешение на геолокацию или перейти в настройки приложения, если пользователь ранее отказал в предоставлении разрешения.  
-в процессе загрузки отображается индикатор загрузки.  
-если в процессе получения событий возникла ошибка, то ошибка отображается вместо списка с понятным для пользователя сообщением.  
-если список пуст, то отображается сообщение вместо списка.  

#### Экран "Детальное отображение события" 
-позволяет добавить событие в календарь с геометкой.  

#### Другие функции  
-сохраняет применённый фильтр между сессиями работы приложения.  
-сохраняет последнюю дату, когда события были получены из интернета.  
-загружает новые события из сети, если прошло больше 30 минут с момента последней загрузки.  
-если нет сети, то события берутся из локального хранилища, независимо от валидности кэша.  

#### JSON с mock данными
Приложение использует файл [events.json](https://github.com/vazh2100/GeoEventApplication/blob/master/app/src/main/assets/events.json) для имитации API-ответа Retrofit c помощью [AssetInterceptor](https://github.com/vazh2100/GeoEventApplication/blob/master/app/src/main/java/com/vazh2100/geoeventapp/data/network/inteceptor/AssetInterceptor.kt).

```json
[
  {
    "id": 1, // Уникальный идентификатор события.  
    "name": "Wilde", // Название события. 
    "description": "Gonococcal endophthalmia", // Краткое описание события.
    "type": "Festival", // Тип события (например, выставка, фестиваль)
    "latitude": 24.086481, // Географическая широта места события. 
    "longitude": 112.346259, //  Географическая долгота места события.
    "city": "Qiashui", // Город, где проходит событие.  
    "date": "2024-12-06T13:55:07Z" // Дата и время события в формате ISO 8601.
  }
]
```
#### Покрытие тестами  
-написан Unit-тест, проверяющий фильтрацию событий в `EventRepository`.  
Для запуска теста:
1. Откройте файл:
   [app/src/test/java/com/vazh2100/geoeventapp/domain/entity/EventFilterTest.kt](https://github.com/vazh2100/GeoEventApplication/blob/master/app/src/test/java/com/vazh2100/geoeventapp/domain/entity/EventFilterTest.kt)
2. Выберите **Run 'EventFilterTest'**.
3. Тесты проверяют:
   - Фильтрацию событий по типу.
   - Фильтрацию событий по радиусу.

Чтобы протестировать неудачный сетевой запрос добавьте throw Exception()
в [app/src/main/java/com/vazh2100/geoeventapp/data/repository/EventRepository.kt](https://github.com/vazh2100/GeoEventApplication/blob/master/app/src/main/java/com/vazh2100/geoeventapp/data/repository/EventRepository.kt)

```kotlin
   private suspend fun refreshEvents() {
   delay(5000L)
   throw Exception() // добавьте эту строку
   val eventsFromApi = mainApi.getEvents()
   eventDao.deleteAllEvents()
   eventDao.insertEvents(eventsFromApi)
   preferenceStorage.setLastUpdateTime(getNowDate())
}
```
