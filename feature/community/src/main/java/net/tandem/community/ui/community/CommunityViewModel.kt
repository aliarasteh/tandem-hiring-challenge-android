package net.tandem.community.ui.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.tandem.data.model.entity.CommunityEntity
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    application: Application, private val model: CommunityModel
) : AndroidViewModel(application) {

    @ExperimentalPagingApi
    private val _communityPager by lazy {
        model.getCommunityList().flow.cachedIn(viewModelScope)
    }

    @ExperimentalPagingApi
    fun getCommunityPager(): Flow<PagingData<CommunityEntity>> {
        return _communityPager
    }

    fun likeCommunity(communityId: Int, liked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            model.likeCommunity(communityId, liked)
        }
    }
}