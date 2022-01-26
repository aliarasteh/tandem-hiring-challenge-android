## Introduction
This project is implemented for the Tandem hiring challenge. An application that will get information about a language learning
community and display the data in a list as you can see in the below screenshot. check [**community directory**](https://github.com/aliarasteh/tandem-hiring-challenge-android/tree/main/feature/community) and read related [README](https://github.com/aliarasteh/tandem-hiring-challenge-android/tree/main/feature/community/README.md) for detail and notes.

You can download and test application from **files** directory or just click on below link.

[**click to download apk file**](https://mgit.mparsict.com/android/libs/basemodule/-/raw/master/files/tandem-hiring-challenge.apk)



<br>
<p align="center">
 <img src="https://mgit.mparsict.com/android/libs/basemodule/-/raw/master/files/screenshot.png" width="250"/></p>
<br>




*  [App Structure](#structure)
*  [Technologies](#technologies)
*  [Setup And Run](#setup)
*  [Roadmap](#roadmap)
*  [Third Party Libraries](#libraries)
*  [License](#license)
*  [Contact](#contact)



### <a name="structure">App Structure</a> 

Although this is a simple project for test challenge purposes that only shows a community list, It was assumed that it is part of a real app, hence is designed for a project with more features.
project is designed with a **modular architecture** which brings us great advantages in maintenance, testability, development speed, and code separation and readability.
here are modules defined in the app structure, you can see more detail about each module by selecting one.

- [app]()
- core
  - [component](https://github.com/aliarasteh/tandem-hiring-challenge-android/tree/main/core/component)
  - [data](https://github.com/aliarasteh/tandem-hiring-challenge-android/tree/main/core/data)

- feature
  - [community](https://github.com/aliarasteh/tandem-hiring-challenge-android/tree/main/feature/community)
  - other features


App navigation is implemented using the [**navigation component**](https://developer.android.com/guide/navigation/navigation-getting-started). we have a single MainActivity and several fragments whose navigation and communication including passing data are managed by the navigation component.



### <a name="technologies">Technologies</a> 

- [**Kotlin**](https://kotlinlang.org/) used as main language offering great features like null-safety, extension functions and Coroutines
- Android recommended [app architecture](https://developer.android.com/topic/architecture) mostly known as **MVVM**
- [**Data Binding**](https://developer.android.com/topic/libraries/data-binding) library for binding UI components to data sources
- Using [**Hilt**](https://developer.android.com/topic/libraries/data-binding) library for dependency injection
- Android [**navigation component**](https://developer.android.com/guide/navigation/navigation-getting-started) used for managing navigation flow
- [**Paging library **](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) to load and display pages of data from local storage or over network
- [**Room**](https://developer.android.com/training/data-storage/room) as abstraction layer over SQLite database and great integration with paging



### <a name="setup">Setup And Run</a> 

1. Clone the project from:  

```sh
git clone https://github.com/aliarasteh/tandem-hiring-challenge-android.git
```

2. Simply build and run project. you can change server address if needed
   1. find AppConfig class in data module
   2. set API_URL = "https://tandem2019.web.app/api/" and run project



### <a name="roadmap">Roadmap</a> 

- [x] implement base app structure and define architecture
- [x] create common tools and helper classes
- [ ] implement community list
  - [x] create community model for managing repository related actions
  - [x] implement community list with paging library
  - [ ] write test cases (not completed)
- [x] documentation



### <a name="libraries">Third Party Libraries</a> 

here are third part libraries used in this sample

1. **Balloon** - useful tool for showing tooltips and popups [GitHub](https://github.com/skydoves/Balloon)
2. **Coil** - An image loading library for Android backed by Kotlin Coroutines [GitHub](https://coil-kt.github.io/coil/)
3. **Retrofit** - A type-safe HTTP client for Android and Java and great tool for networking [GitHub](https://github.com/square/retrofit)



### <a name="license">License</a> 

Distributed under the MIT License.



### <a name="contact">Contact</a> 
Ali Arasteh -  ali_arasteh@live.com - [LinkedIn](https://www.linkedin.com/in/aliarasteh/)







