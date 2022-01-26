## **Data Module**

This is the core data module responsible for managing local and network repositories.



### **Architecture overview**

The clean architecture assumes separation of concerns with UI(Activity, Fragment, View), Presentation(ViewModel), and Data(Repository) layers. ViewModel helps to handle user interaction, save the current state of the app and automatically manage Android UI components lifecycle via LiveData.
The repository serves as a data point, where ViewModel knows nothing about the source of data. It is up to the repository to decide whether local or remote data should be given back to the user. It serves well to handle data synchronization conflicts.



### **Repository with Kotlin Coroutines based on single source of truth strategy**

In order to avoid synchronization discrepancies, it is required to define a single source of truth. The Repository serves as an abstract source of data for the Presentation Layer (viewModel). Although Repository should decide which data source is the truth. we have a local database as a source of truth. Whenever data is requested we will give back a local copy, then we fetch remote data, save it to the database, which will automatically notify the Repository about new data available.



check [**SingleSourceOfTruthStrategy.kt**](https://github.com/aliarasteh/tandem-hiring-challenge-android/blob/feature/community_list/core/data/src/main/java/net/tandem/data/SingleSourceOfTruthStrategy.kt) file for implementation detail



<br>

<p align="center">
 <img src="https://mgit.mparsict.com/android/libs/basemodule/-/raw/master/files/data_flow.png"/></p>

<br>