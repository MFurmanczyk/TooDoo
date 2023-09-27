# TooDoo
Simple to-do app with task categories and responsive UI made with Kotlin and Jetpack Compose. Currently, work in progress.

## About the app
TooDoo is a personal tasks management app for Android. It aims to help users organize their tasks in a transparent manner by categorizing tasks into custom categories. (WIP) The app reminds users about tasks for the current day. 
(WIP) Created tasks (eg. shopping lists) can be sent to others via SMS. 

## Screenshots

## Features
### Done

* 📱 Adaptive screen content - layout depends on screen size
* 📤 Custom categories - users can define their own categories
* ✅ Tasks - users can create tasks with multiple steps, categories, and due date
* 📋 Dashboard - display categories with their overall progress and undone tasks that are due for the current day

### Planned

* 📆 Calendar - screen that displays all tasks (done and not done) for a selected day
* 📤 Categories - screen that displays all categories with information about current progress within a category
* ✉️ SMS - sending task via SMS
* ❗Notifications - the app notifies the user about tasks that are planned to be done on that day

## Modules
The app is built with multiple modules:
* data - responsible for [model](https://github.com/MFurmanczyk/TooDoo/tree/master/data/src/main/java/com/mfurmanczyk/toodoo/data/model) classes 
(with [relationships](https://github.com/MFurmanczyk/TooDoo/tree/master/data/src/main/java/com/mfurmanczyk/toodoo/data/model/relationship) defined) and all communication with the 
[database](https://github.com/MFurmanczyk/TooDoo/tree/master/data/src/main/java/com/mfurmanczyk/toodoo/data/database). Provides 
[task](https://github.com/MFurmanczyk/TooDoo/blob/master/data/src/main/java/com/mfurmanczyk/toodoo/data/repository/TaskRepository.kt), 
[category](https://github.com/MFurmanczyk/TooDoo/blob/master/data/src/main/java/com/mfurmanczyk/toodoo/data/repository/CategoryRepository.kt) and 
[step](https://github.com/MFurmanczyk/TooDoo/blob/master/data/src/main/java/com/mfurmanczyk/toodoo/data/repository/StepRepository.kt) repositories and all related dependencies that need to be injected.
* preferences - responsible for providing [preferences](https://github.com/MFurmanczyk/TooDoo/blob/master/preferences/src/main/java/com/mfurmanczyk/toodoo/preferences/repository/DataStorePreferencesRepository.kt) 
repository that allows users to customize the app (currently only username preference is available - defined on first-time launch)
* mobile - this module contains all [Composable UI](https://github.com/MFurmanczyk/TooDoo/tree/master/mobile/src/main/java/com/mfurmanczyk/toodoo/mobile/view/screen) 
with [view models](https://github.com/MFurmanczyk/TooDoo/tree/master/mobile/src/main/java/com/mfurmanczyk/toodoo/mobile/viewmodel), [navigation graph](https://github.com/MFurmanczyk/TooDoo/tree/master/mobile/src/main/java/com/mfurmanczyk/toodoo/mobile/view/navigation), 
[components](https://github.com/MFurmanczyk/TooDoo/tree/master/mobile/src/main/java/com/mfurmanczyk/toodoo/mobile/view/component) 
used in multiple screens and [top-level](https://github.com/MFurmanczyk/TooDoo/blob/master/mobile/src/main/java/com/mfurmanczyk/toodoo/mobile/TooDooApp.kt) composable that is responsible for displaying adaptive screen content.


## Technology & Techniques
Used technology and programming techniques: 
* Android SDK
* Kotlin
* Jetpack Compose
* Multi-modules
* Coroutines
* Navigation
* MVVM pattern
* Dependency Injection with Hilt
* Kotlin flows
* Datastore
* Room database
* [Color picker](https://vanpra.github.io/compose-material-dialogs/ColorPicker/) - credits to [vanpra](https://github.com/vanpra)
