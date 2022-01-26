package net.tandem.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application, private val model: MainModel
) : AndroidViewModel(application) {

    fun deleteAllCommunityItems() {
        viewModelScope.launch(Dispatchers.IO) {
            model.deleteAllCommunityItems()
        }
    }
}