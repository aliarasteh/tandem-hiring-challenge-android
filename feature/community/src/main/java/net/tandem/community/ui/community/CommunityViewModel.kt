package net.tandem.community.ui.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.tandem.community.ui.community.adapter.CommunityItem
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    application: Application, private val model: CommunityModel
) : AndroidViewModel(application) {

    @ExperimentalPagingApi
    private val _communityPager by lazy {
        model.getCommunityList().flow.map { pagingData ->
            pagingData.map { entity ->
                val natives = if (entity.natives.isNullOrEmpty())
                    "-"
                else
                    entity.natives!!.joinToString { it.uppercase() }
                val learns = if (entity.natives.isNullOrEmpty())
                    "-"
                else
                    entity.learns!!.joinToString { it.uppercase() }
                CommunityItem(
                    id = entity.id,
                    topic = entity.topic ?: "",
                    firstName = entity.firstName ?: "",
                    pictureUrl = entity.pictureUrl ?: "",
                    natives = natives,
                    learns = learns,
                    referenceCount = (entity.referenceCount ?: 0).toString(),
                    liked = entity.liked,
                    isReferenced = (entity.referenceCount ?: 0) > 0
                )
            }
        }.cachedIn(viewModelScope)
    }

    @ExperimentalPagingApi
    fun getCommunityPager(): Flow<PagingData<CommunityItem>> {
        return _communityPager
    }

    fun likeCommunity(communityId: Int, liked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            model.likeCommunity(communityId, liked)
        }
    }
}