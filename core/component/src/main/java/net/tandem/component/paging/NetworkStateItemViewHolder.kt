package net.tandem.component.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.tandem.component.databinding.NetworkStateItemBinding

/**
 * Created by Ali Arasteh
 */

open class CustomViewHolder<B : ViewBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root)

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(
    parent: ViewGroup, private val retryCallback: () -> Unit
) : CustomViewHolder<NetworkStateItemBinding>(
    NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) {

    fun bindTo(loadState: LoadState) {
        binding.loadingView.isVisible = loadState is Loading
        binding.loadFailView.isVisible = loadState is Error
        binding.retryButton.also {
            it.setOnClickListener { retryCallback() }
        }
    }

}
