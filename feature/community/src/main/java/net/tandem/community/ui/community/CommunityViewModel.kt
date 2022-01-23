package net.tandem.community.ui.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.tandem.data.model.response.CommunityResponse
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    application: Application, private val model: CommunityModel
) : AndroidViewModel(application) {

    val communityResponse = MutableLiveData<CommunityResponse>()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val result = model.getCommunityList()
            communityResponse.postValue(result)
        }
    }
}