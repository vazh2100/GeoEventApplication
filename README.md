### GeoEventApp
**UI и навигация**: Jetpack Compose, Material 3, Compose Navigation.   
**Сетевое взаимодействие**: Retrofit, Kotlin Serialization.  
**Локальное хранилище**: Room, Data Store.  
**Геолокация**: Location Google Play Services, Accompanist Permission.  
**Тестирование**: JUnit, Mockk.  
**Архитектура**: Мультимодальная, Clean Architecture, Koin DI, MVVM   
**Качество кода**: pre-push hook: push можно сделать, только если проверка кода(Detekt) и тесты прошли успешно  
**CI/CD**: настроен GitHub Workflow, собирающий и публикующий подписанную release сборку в GitHub  
**Техническое задание**:
[TECHNICAL SPECIFICATIONS.md](https://github.com/vazh2100/GeoEventApplication/blob/master/TECHNICALSPECIFICATIONS.md)  
**Описание реализованного функционала**:
[DESCRIPTION.md](https://github.com/vazh2100/GeoEventApplication/blob/master/DESCRIPTION.md)
<div style="display: flex; justify-content: space-between;"> 
<img src="screenshots/Screenshot_Good.png" width="270"  alt=""/>
<img src="screenshots/Screenshot_Bad.png" width="270"  alt=""/>
<img src="screenshots/Screenshot_Filter.png" width="270"  alt=""/>
</div>

#### Перед началом работы
1. Запустить tune-project.sh, чтобы перенести хуки проекта в .git/hooks

#### Дерево проекта
```
0_app
   └── MainActivity.kt
   └── MyApp.kt
   └── Navigation.kt
1_core
   ├── entities
   │   └── DateFormatter.kt
   │   └── DisplayNameEnum.kt
   │   └── InstantDateTimeSerializer.kt
   ├── extensions
   │   └── CapitalizeFirst.kt
   │   └── IfElsy.kt
   │   └── TableSizeFor.kt
   │   └── TakeRandom.kt
   │   └── TakeRandomWith.kt
1_core_a
   ├── entities
   │   └── AssetReader.kt
   │   └── DateConverter.kt
   ├── widgets
   │   └── AppDatePicker.kt
   │   └── DateRangeSelector.kt
   │   └── ErrorMessage.kt
   │   └── ErrorPanel.kt
   │   └── LoadingIndicator.kt
   │   └── TypeSelector.kt
   └── Koin.kt
1_geolocation
   ├── entity
   │   └── GPoint.kt
   │   └── LocationStatus.kt
   ├── repository
   │   └── LocationStateRepository.kt
   ├── usecase
   │   └── IGetLocationStatusUseCase.kt
   └── Koin.kt
1_network
   ├── entity
   │   └── NetworkStatus.kt
   ├── usecase
   │   └── IObserveNetworkStateUseCase.kt
   ├── widget
   │   └── NetworkStatusBar.kt
   └── Koin.kt
1_theme
   └── AppThemeColors.kt
   └── AppTheme.kt
   └── Dimensions.kt
   └── DimensionsProvider.kt
   └── ScaleProvider.kt
   └── Shapes.kt
   └── Theme.kt
   └── Typography.kt
2_events
   ├── data
   │   ├── api
   │   │   └── AssetInterceptor.kt
   │   │   └── EventsApi.kt
   │   │   └── EventsClientProvider.kt
   │   ├── repository
   │   │   └── EventRepository.kt
   │   ├── storage
   │   │   ├── room
   │   │   │   └── DatabaseProvider.kt
   │   │   │   └── EventDao.kt
   │   │   │   └── EventsDatabase.kt
   │   │   │   └── EventTypeConverter.kt
   │   │   └── EventsPreferencesStorage.kt
   ├── entities
   │   └── Event.kt
   │   └── EventSearchParams.kt
   │   └── EventSortType.kt
   │   └── EventsProcessor.kt
   │   └── EventType.kt
   ├── screen
   │   ├── event_detail
   │   │   └── EventDetailScreen.kt
   │   ├── event_list
   │   │   ├── widget
   │   │   │   └── Content.kt
   │   │   │   └── EventListItem.kt
   │   │   │   └── EventList.kt
   │   │   │   └── FilterPanel.kt
   │   │   │   └── GAppBar.kt
   │   │   │   └── LocationStatusBar.kt
   │   │   │   └── RadiusSelector.kt
   │   │   └── EventListScreen.kt
   │   │   └── EventListState.kt
   │   │   └── EventListViewModel.kt
   ├── usecase
   │   └── GetFilteredEventsUseCase.kt
   │   └── GetSavedFiltersUseCase.kt
   └── Koin.kt
2_events
   └── EventsProcessorFilterTest.kt
   └── EventsProcessorSortTest.kt
   └── GetFilteredEventsUseCaseTest.kt
```
